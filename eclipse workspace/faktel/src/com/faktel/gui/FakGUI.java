package com.faktel.gui;

import java.awt.Dimension;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.faktel.FakEngine;
import com.faktel.config.ConfigParser;
import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;
import com.faktel.mvc.View;

/**
 * GUI of the Faktel project
 * 
 * @author teodor.stoev, mitex
 */
public class FakGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private MainMenu   m_mainMenu;
	private JList      m_fileList;
	private File       m_settingsFile;
	private Box		   m_mainContainer;
	private JTextField m_settingsFileField;

	private Map<String, View> m_viewMapping;
	
	private static FakGUI s_application;
	
	public FakGUI() {
		super();
		
		// Initialize the main menu
		setJMenuBar(m_mainMenu = new MainMenu());
		
		// Initialize the main container
		m_mainContainer = new Box(BoxLayout.Y_AXIS);
		add(m_mainContainer);
		m_mainContainer.setVisible(true);
		
		/* Select settings file */
		{
			Box row = new Box(BoxLayout.X_AXIS);
			row.setBorder(BorderFactory.createTitledBorder("Settings file:"));
			m_mainContainer.add(row);
			
			m_settingsFileField = new JTextField();
			m_settingsFileField.setEditable(false);
			row.add(m_settingsFileField);
			
			JButton chooseSettings = new JButton("Open settings");
			chooseSettings.addActionListener(m_mainMenu.getFileOpenSettingsAction());
			row.add(chooseSettings);
		}
		
		/* Manage file list */
		{
			Box row = new Box(BoxLayout.X_AXIS);
			row.setBorder(BorderFactory.createTitledBorder("Files list:"));
			m_mainContainer.add(row);
			
			m_fileList = new JList(new FileListModel());
			JScrollPane scrollPane = new JScrollPane(m_fileList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setSize(new Dimension(400, 250));
			row.add(scrollPane);
			
			Box buttonsArea = new Box(BoxLayout.Y_AXIS);
			row.add(buttonsArea);
			
			JButton addFilesButton = new JButton("Add invoices");
			addFilesButton.addActionListener(m_mainMenu.getFileOpenAction());
			buttonsArea.add(addFilesButton);

			JButton clearFilesButton = new JButton("Clear list");
			clearFilesButton.addActionListener(m_mainMenu.getFileClearAction());
			buttonsArea.add(clearFilesButton);

			JButton processButton = new JButton("Process");
			processButton.addActionListener(m_mainMenu.getProcessAction());
			buttonsArea.add(processButton);
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(getPreferredSize());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Adds a file to the list of files for processing
	 * 
	 * @param f The file to the added to the list
	 */
	public void addFile(File f) {
		((FileListModel)m_fileList.getModel()).addFile(f);
	}

	/**
	 * Sets the settings file, which contains the XML with all the configuration
	 * 
	 * @param settingsFile The settings file
	 */
	public void setSettingsFile(File settingsFile) {
		m_settingsFileField.setText(settingsFile.getName());
		m_settingsFile = settingsFile;
	}
	
	/**
	 * Returns a view with a given name.
	 * 
	 * @param name The name of the view
	 * @return The requested view or null if such does not exist
	 */
	public View getView(String name) {
		return m_viewMapping.get(name);
	}

	/**
	 * Reads the configuration and the input invoices and starts the filters
	 */
	public void process() {
		File configFile = m_settingsFile;
		
		if (configFile == null) {
			JOptionPane.showMessageDialog(this, "Please select a settings file");
			return;
		}
		
		if (!configFile.isFile() || !configFile.exists()) {
			JOptionPane.showMessageDialog(this, "The settings file you specified does not exist or is not a regular file. Please select a valid settings file.");
			return;
		}
		
		if (m_fileList.getModel().getSize() == 0) {
			JOptionPane.showMessageDialog(this, "Please select some files for processing.");
			return;
		}

		ConfigParser parser;
		try {
			parser = new ConfigParser(configFile);
			m_mainContainer.add(parser.getLayout());
			m_viewMapping = parser.getViewMapping();
			
			setSize(getPreferredSize());
			
			// FIXME: debug
			Grid grid = new Grid();
			grid.add(new GridRow(new Object[] {"Apples", "Oranges", "Mangos", "Grapes"}));
			grid.add(new GridRow(new Object[] { 10, 8, 13, 22 }));
			grid.add(new GridRow(new Object[] { 3, 17, 22, 56 }));
			getView("table").displayGrid(grid);
			getView("pie").displayGrid(grid);
			FakEngine engine = new FakEngine(parser);
			engine.process();
		} 
		catch (Exception e) {
			// FIXME: probably this is a good point to send a report for the crashes
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, 
										  "Error occured while processing the files.\nError: " + e.getMessage(), 
										  "Error", 
										  JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static FakGUI getApplication() { return s_application; }

	public static void main(String[] args) throws Exception {
		s_application = new FakGUI();
		s_application.setTitle("FakTel");
		s_application.setName("FakTel");
	}

	public void clearFileList() {
		((FileListModel)m_fileList.getModel()).clearList();
	}
}
