package com.faktel.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The main menu of the faktel GUI. Also includes some common commands in the
 * GUI.
 * 
 * @author teodor.stoev, mitex
 */
public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = 7694698099129747446L;

	protected static final String LAST_SETTINGS_FOLDER = "last_settings_folder";

	protected static final String LAST_INVOICES_FOLDER = "last_invoices_folder";
	
	public MainMenu() {
		// File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		{
			JMenuItem openSettings = new JMenuItem("Open settings...");
			openSettings.addActionListener(m_openSettingsAction);
			openSettings.setMnemonic('O');
			fileMenu.add(openSettings);

			JMenuItem open = new JMenuItem("Add invoices...");
			open.setMnemonic('A');
			open.addActionListener(m_openAction);
			fileMenu.add(open);
			
			fileMenu.insertSeparator(2);
			
			JMenuItem process = new JMenuItem("Process");
			process.setMnemonic('P');
			process.addActionListener(m_processAction);
			fileMenu.add(process);
		}
		add(fileMenu);
		
		JMenuItem helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		add(helpMenu);
		{
			JMenuItem aboutFaktel = new JMenuItem("About...");
			aboutFaktel.setMnemonic('A');
			aboutFaktel.addActionListener(m_openAboutBoxAction);
			helpMenu.add(aboutFaktel);
		}
	}

	/**
	 * A command for setting a settings file 
	 */
	private ActionListener m_openSettingsAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(Preferences.systemNodeForPackage(FakGUI.class).get(LAST_SETTINGS_FOLDER, ".")));
			chooser.setFileFilter(new FileNameExtensionFilter("XML Document", "xml"));
			int result = chooser.showOpenDialog(FakGUI.getApplication());
			
			if (result == JFileChooser.APPROVE_OPTION) {
				FakGUI.getApplication().setSettingsFile(chooser.getSelectedFile());
				Preferences.systemNodeForPackage(FakGUI.class).put(LAST_SETTINGS_FOLDER, chooser.getCurrentDirectory().getAbsolutePath());
			}
		}
	};

	private ActionListener m_editSettingsAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			File settingsFile = FakGUI.getApplication().getSettingsFile();
			if (settingsFile != null && settingsFile.exists()) {
				ConfigFileEditor configFileEditor = new ConfigFileEditor(FakGUI
						.getApplication(), settingsFile);
				configFileEditor.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(FakGUI.getApplication(),
						"Please first select a configuration file.");
			}
		}
		
	};

	/**
	 * A command for adding a file to the list of invoices to be processed
	 */
	private ActionListener m_openAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(Preferences.systemNodeForPackage(FakGUI.class).get(LAST_INVOICES_FOLDER, ".")));
			chooser.setFileFilter(new FileNameExtensionFilter("Excel and CSV files", "xls", "csv"));
			chooser.setMultiSelectionEnabled(true);
			//chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int result = chooser.showOpenDialog(FakGUI.getApplication());
			
			if (result == JFileChooser.APPROVE_OPTION) {
				for (File selected : chooser.getSelectedFiles()) {
					FakGUI.getApplication().addFile(selected);
				}
				Preferences.systemNodeForPackage(FakGUI.class).put(LAST_INVOICES_FOLDER, chooser.getCurrentDirectory().getAbsolutePath());
			}
		}
	};
	
	private ActionListener m_processAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			FakGUI.getApplication().process();
		}
	};

	private ActionListener m_clearFileListAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			FakGUI.getApplication().clearFileList();
		}
	};
		
	/**
	 * A command for displaying the About box 
	 */
	private ActionListener m_openAboutBoxAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			AboutBox aboutBox = new AboutBox();
			
			aboutBox.showDialog();
		}
	};
	
	public ActionListener getFileOpenAction() {
		return m_openAction;
	}
	
	public ActionListener getProcessAction() {
		return m_processAction;
	}

	public ActionListener getFileOpenSettingsAction() {
		return m_openSettingsAction;
	}

	public ActionListener getFileClearAction() {
		return m_clearFileListAction;
	}

	public ActionListener getFileEditSettingsAction() {
		return m_editSettingsAction;
	}
}
