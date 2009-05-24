package com.faktel.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import com.faktel.features.SimpleGrid;
import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;

public class FakGUI extends JFrame {
	private static final long serialVersionUID = -3360812071998304818L;
	
	private MainMenu m_mainMenu;
	private JList    m_fileList;
	private Box		 m_mainContainer;
	
	private static FakGUI s_application;
	
	public FakGUI() {
		super();
		
		// Init the main menu
		setJMenuBar(m_mainMenu = new MainMenu());
		
		// Init the main container
		m_mainContainer = new Box(BoxLayout.Y_AXIS);
		add(m_mainContainer);
		m_mainContainer.setVisible(true);
		
		// This is going to be the first row
		{
			Container firstRow = new JPanel();
			firstRow.setVisible(true);
			m_mainContainer.add(firstRow);
			
			m_fileList = new JList(new FileListModel());
			m_fileList.setPreferredSize(new Dimension(400, 200));
			m_fileList.setVisible(true);
			JScrollPane scrollPane = new JScrollPane(m_fileList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setPreferredSize(new Dimension(400, 250));
			scrollPane.setBorder(BorderFactory.createTitledBorder("Files list:"));
			firstRow.add(scrollPane);
			
			JButton addFilesButton = new JButton("Add invoices");
			addFilesButton.addActionListener(m_mainMenu.getFileOpenAction());
			firstRow.add(addFilesButton);
		}

		{
			Container secondRow = new Box(BoxLayout.X_AXIS);
			secondRow.setVisible(true);
			m_mainContainer.add(secondRow);
			
			SimpleGrid gridView = new SimpleGrid("test");
			secondRow.add(gridView);
			
			Grid grid = new Grid();
			Object[] tmp1 = {"1", "2", "3", "4"};
			grid.add(new GridRow(tmp1));
			
			Object[] tmp2 = {"5", "6", "7", "8"};
			grid.add(new GridRow(tmp2));
			
			gridView.displayGrid(grid);
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(getPreferredSize());
		setVisible(true);
	}
	
	public void addFile(File f) {
		((FileListModel)m_fileList.getModel()).addFile(f);
	}
	
	public static FakGUI getApplication() { return s_application; }

	public static void main(String[] args) throws Exception {
		s_application = new FakGUI();
/*		boolean debug = true;
		if (debug && args.length == 0) {
			args = new String[] { "config/settings.xml" };
		}

		if (args.length == 0) {
			System.err.println("Config file is not specified. Exitting..");
			System.exit(1);
		}
		File configFile = new File(args[0]);
		if (!configFile.isFile() || !configFile.exists()) {
			System.err.println("Config file '" + configFile
					+ "' does not exists");
			System.exit(2);
		}

		ConfigParser parser = new ConfigParser(configFile);
		FakEngine engine = new FakEngine(parser);
		engine.process();
		System.out.println("Bye!");
		// Fix dummy bug
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6476706
		System.exit(0);*/
	}
}
