package com.faktel.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

/**
 * The main menu of the faktel GUI. Also includes some common commands in the
 * GUI.
 * 
 * @author valentinmihov
 */
public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = 7694698099129747446L;

	protected static final String LAST_SETTINGS_FOLDER = "last_settings_folder";

	protected static final String LAST_INVOICES_FOLDER = "last_invoices_folder";
	
	public MainMenu() {
		// File menu
		JMenu fileMenu = new JMenu("File");
		{
			JMenuItem openSettings = new JMenuItem("Open settings...");
			openSettings.addActionListener(m_openSettingsAction);
			fileMenu.add(openSettings);

			JMenuItem open = new JMenuItem("Add invoices...");
			open.addActionListener(m_openAction);
			fileMenu.add(open);
		}
		add(fileMenu);
		
		JMenuItem process = new JMenuItem("Process");
		process.addActionListener(m_processAction);
		add(process);
	}

	/**
	 * A command for setting a settings file 
	 */
	private ActionListener m_openSettingsAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(Preferences.systemNodeForPackage(FakGUI.class).get(LAST_SETTINGS_FOLDER, ".")));
			chooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".xml");
				}

				@Override
				public String getDescription() {
					return "XML Document";
				}
			});
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = chooser.showOpenDialog(FakGUI.getApplication());
			
			if (result == JFileChooser.APPROVE_OPTION) {
				FakGUI.getApplication().setSettingsFile(chooser.getSelectedFile());
				Preferences.systemNodeForPackage(FakGUI.class).put(LAST_SETTINGS_FOLDER, chooser.getCurrentDirectory().getAbsolutePath());
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
			chooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().endsWith(".xls") || f.getName().endsWith(".csv");
				}

				@Override
				public String getDescription() {
					return "Excel and CSV files";
				}
			});
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
}
