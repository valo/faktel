package com.faktel.scrapped_code.gui;
public class FakEngineGUI {
	
}
/*
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import com.faktel.config.ConfigParser;
import com.faktel.data.InvoiceRow;
import com.faktel.data.workers.InvoiceRowReader;
import com.faktel.data.workers.InvoiceRowWriter;
import com.faktel.filters.RowFilter;
import com.faktel.io.InputReaderFactory;
import com.faktel.io.OutputWriter;
import com.faktel.mvc.Model;
import com.faktel.views.DummyView;
public class FakEngineGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 361508399778650126L;

	private static final Logger logger = Logger.getLogger(FakEngineGUI.class.getName());
	
	private static final String NEW   = "New";
	private static final String OPEN  = "Open";
	private static final String EXIT  = "Exit";
	private static final String ABOUT = "About";
	private static final String RECENT = "Recent files";
	private static final String RECENT_COMMAND = "Recent ";
	private static final String START = "Start processing";
	private static final String VIEW  = "View results";
	private static final String PROCESS = "Process: <no_file>";

	private static File mappingFile = new File("config/mtel-mapping.in");
	
	private InvoiceRowReader m_reader;
	private Collection<RowFilter> m_filters;
	private Collection<InvoiceRowWriter> m_writers = new LinkedList<InvoiceRowWriter>();
	
	
	private static String m_title = "FakTel Demo";
	private JTextArea m_textResults = new JTextArea();
	private JButton m_button = new JButton(START);
	private JProgressBar m_progressBar;
	private JLabel m_fileLabel = new JLabel(PROCESS);
	private JFileChooser m_chooser;
	private File m_recentFile;
	private JMenu m_recentMenu;
	private LinkedList<File> m_recentPaths;

	private File m_file;

	//TODO this thing definitely should not be here.
	// refactor, extract all data and separate from algorithm.
	private Model model;
	
	public FakEngineGUI(Collection<RowFilter> filters, Collection<OutputWriter> writers) throws IOException {
		super(m_title);
		m_filters = filters;
		for (OutputWriter wr : writers)
			m_writers.add(new InvoiceRowWriter(wr));
		
		try {
			File tempFile = File.createTempFile("none", "none");
			File tempDir = tempFile.getParentFile();
			tempFile.delete();
			m_recentFile = new File(tempDir, "faktel-recent.ini");
		} catch (IOException e) {

		}
		
		m_chooser = new JFileChooser();
		m_chooser.setCurrentDirectory(new File("a"));
		System.out.println(m_chooser.getCurrentDirectory());
		m_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		JPanel mainPanel = new JPanel();
		mainPanel.add(m_fileLabel, BorderLayout.NORTH);
		mainPanel.add(m_textResults, BorderLayout.CENTER);
		m_textResults.setPreferredSize(new Dimension(480, 300));
		m_textResults.setEditable(false);
		
		//south
		JPanel southPanel = new JPanel();
		mainPanel.add(southPanel, BorderLayout.SOUTH);
        //m_progressBar = new JProgressBar(0, 100);
		m_progressBar = new JProgressBar();
        m_progressBar.setValue(0);
        m_progressBar.setString("Not started");
        m_progressBar.setStringPainted(true);
		southPanel.add(m_progressBar, BorderLayout.CENTER);
        
        //button
		southPanel.add(m_button, BorderLayout.WEST);
		m_button.setActionCommand(START);
		m_button.addActionListener(this);
		m_button.setEnabled(false);
		m_button.setMnemonic(KeyEvent.VK_ENTER);
		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem newItem = new JMenuItem(NEW, KeyEvent.VK_N);
		newItem.addActionListener(this);
		newItem.setActionCommand(NEW);
		menu.add(newItem);
		
		JMenuItem openItem = new JMenuItem(OPEN, KeyEvent.VK_O);
		openItem.addActionListener(this);
		openItem.setActionCommand(OPEN);
		menu.add(openItem);
		menu.addSeparator();
		
		m_recentMenu = new JMenu(RECENT);
		m_recentMenu.setMnemonic(KeyEvent.VK_R);
		populateRecentMenu();
		menu.add(m_recentMenu);
		
		menu.addSeparator();
		JMenuItem exitItem = new JMenuItem(EXIT, KeyEvent.VK_E);
		exitItem.addActionListener(this);
		exitItem.setActionCommand(EXIT);
		menu.add(exitItem);
		
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem about = new JMenuItem(ABOUT, KeyEvent.VK_A);
		about.addActionListener(this);
		about.setActionCommand(ABOUT);
		help.add(about);
		
		JMenuBar menubar = new JMenuBar();
		menubar.add(menu);
		menubar.add(help);
		setJMenuBar(menubar);
		getContentPane().add(mainPanel);
		setSize(500, 430);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setIconImage(getIcon("resources/FakTel.png"));
		setVisible(true);
	}
	
	private Image getIcon(String name) {
		try {
			return new ImageIcon(FakEngineGUI.class.getResource(name)).getImage();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void populateRecentMenu() {
		ObjectInputStream fis = null;
		try {
			fis = new ObjectInputStream(new FileInputStream(m_recentFile));
			m_recentPaths = (LinkedList<File>) fis.readObject();
		}
		catch (Exception e) {}
		finally { if (fis != null) try { fis.close(); } catch (IOException e) {} }
		refreshRecentMenu();		
	}

	public FakEngineGUI(ConfigParser config) throws IOException{
		this(config.getRowFilters(), config.getOutputWriters());
	}
	
	@SuppressWarnings("unchecked")
	private void doJob() {
		if (m_reader == null) return; 
		int step = 100 / (1 + m_filters.size() + m_writers.size());
		int currComponent = 0;
		m_progressBar.setValue(currComponent);
		m_progressBar.setString(String.format("Completed %d", currComponent) + "%");
		String msg = "Start processing file " + ((File)m_reader.getStorageSource()).getName() + "\n";
		m_textResults.append(msg);
		Collection<InvoiceRow> rowData = null;
		try {
			rowData = m_reader.readAllRowData();
		} 
		catch (IOException e) {
			logger.warning("IOException have been occured while parsing file " + m_reader.getStorageSource() + ". Exception: " + e);
		}
		
		if (rowData == null) {
			logger.warning("Did not find any row data from source " + m_reader.getStorageSource());
			rowData = new LinkedList<InvoiceRow>();
		}
		
		currComponent += step;
		m_progressBar.setValue(currComponent);
		m_progressBar.setString(String.format("Completed %d", currComponent) + "%");
		msg = String.format("%d record(s) have been readed.\n", rowData.size());
		m_textResults.append(msg);
		
		//filter given data
		Collection<InvoiceRow> transferedObject = new LinkedList<InvoiceRow>(rowData);
		for (RowFilter filter : m_filters) {
			File workingDir = null;
			Map<String, String> specificFilterArgs = null;
			transferedObject = (Collection<InvoiceRow>) filter.execute( new DummyView( model ), rowData, workingDir, transferedObject, specificFilterArgs);
			currComponent += step;
			m_progressBar.setValue(currComponent);
			m_progressBar.setString(String.format("Completed %d", currComponent) + "%");
			msg = String.format("Filter[%s] finishes.\n", filter.getClass().getName());
			m_textResults.append(msg);
		}
		
		//output transformed data
		for (InvoiceRowWriter writer : m_writers) {
			try {
				transferedObject = (Collection<InvoiceRow>) writer.writeAllRowData(transferedObject);
				currComponent += step;
				m_progressBar.setValue(currComponent);
				m_progressBar.setString(String.format("Completed %d", currComponent) + "%");
				msg = String.format("Writer[%s] finishes.\n", writer.getClass().getName());
				m_textResults.append(msg);
			} 
			catch (IOException e) {
				logger.warning("I/O exception occurs while executing writer '" + writer + "'. Exception: " + e);
			}
		}
		
		currComponent = 100;
		m_progressBar.setValue(currComponent);
		m_progressBar.setString(String.format("Completed %d", currComponent) + "%");
		m_textResults.append("Done.\n");
		m_button.setActionCommand(VIEW);
		m_button.setText(VIEW);
		m_button.setEnabled(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals(NEW)) {
			newFile();
		}
		else if (cmd.equals(OPEN)) {
			 if (m_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				 File file = m_chooser.getSelectedFile();
				 openFile(file);
			 }
		}
		else if (cmd.equals(EXIT)) {
			System.exit(0);
		}
		else if (cmd.equals(START)) {
			m_button.setEnabled(false);
			new Thread() {
				public void run() {
					doJob();
				}
			}.start();
		}
		else if (cmd.equals(VIEW)) {
			try {
				String p = m_file.getParentFile().getAbsolutePath().replace("/", "\\");
				String[] command = new String[] {"explorer", p};
				Runtime.getRuntime().exec(command);
			}
			catch (Exception ex) {
				
			}
		}
		else if (cmd.startsWith(RECENT_COMMAND)) {
			newFile();
			String idx = cmd.substring(RECENT_COMMAND.length());
			File f = m_recentPaths.get(Integer.parseInt(idx));
			openFile(f);
		}
	}
	
	private void newFile() {
		m_fileLabel.setText(PROCESS);
		m_textResults.setText("");
		m_button.setActionCommand(START);
		m_button.setText(START);
		m_button.setEnabled(false);
		m_progressBar.setValue(0);
		m_progressBar.setString("Not started");
		m_reader = null;
	}
	
	private void openFile(File file) {
		if (file.exists()) {
			 m_file = file;
			 m_reader  = new InvoiceRowReader(InputReaderFactory.createInputReader(file, mappingFile));
			 m_fileLabel.setText("File: " + file.getName());
			 m_button.setEnabled(true);	
			 
			 if (m_recentPaths == null) m_recentPaths = new LinkedList<File>();
			 if (!m_recentPaths.contains(file)) {
				 m_recentPaths.addFirst(file);
				 if (m_recentPaths.size() > 10) m_recentPaths.removeLast();
			 }
			 else {
				 //increase priority
				 m_recentPaths.remove(file);
				 m_recentPaths.addFirst(file);
			 }
			 
			 ObjectOutputStream oos = null;
			 try {
				 oos = new ObjectOutputStream(new FileOutputStream(m_recentFile));
				 oos.writeObject(m_recentPaths); 
			 }
			 catch (Exception e) { }
			 finally { if (oos != null) try { oos.close();} catch (IOException e) {} }
			 refreshRecentMenu();
		}
		else {
			JOptionPane.showOptionDialog(
					null, 
					String.format("File %s doesn't exists.", file), 
					"File error",
		            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		            null, null, null);
		}
	}
	
	private void refreshRecentMenu() {
		if (m_recentPaths != null) {
			for (Component c : m_recentMenu.getComponents()) {
				((JMenuItem)c).removeActionListener(this);
			}
			m_recentMenu.removeAll();
			int idx = 0;
			for (File f : m_recentPaths) {
				JMenuItem item = new JMenuItem(f.getName());
				item.setToolTipText(f.getAbsolutePath());
				item.setActionCommand(RECENT_COMMAND + idx++);
				item.addActionListener(this);
				m_recentMenu.add(item);
			}	
		}
	}

	public static void main(String[] args) throws Exception{
		boolean debug = true;
		if (debug && args.length == 0) {
			args = new String[]{"config/settings.xml"};
		}
		
		if (args.length == 0) {
			System.err.println("Config file is not specified. Exitting..");
			System.exit(1);
		}
		File configFile = new File(args[0]);
		if (!configFile.isFile() || !configFile.exists()) {
			System.err.println("Config file '" + configFile + "' does not exists");
			System.exit(2);
		}

		ConfigParser parser = new ConfigParser(configFile);
		new FakEngineGUI(parser);		
	}
}
*/