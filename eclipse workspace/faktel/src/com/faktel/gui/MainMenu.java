package com.faktel.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

public class MainMenu extends JMenuBar {
	private static final long serialVersionUID = 7694698099129747446L;
	
	public MainMenu() {
		// File menu
		JMenu fileMenu = new JMenu("File");
		{
			// Open action
			JMenuItem open = new JMenuItem("Open");
			open.addActionListener(m_openAction);
			fileMenu.add(open);
		}
		add(fileMenu);
	}
	
	private ActionListener m_openAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return true;//f.getName().endsWith(".xls") || f.getName().endsWith(".csv");
				}

				@Override
				public String getDescription() {
					return null;
				}
			});
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = chooser.showOpenDialog(FakGUI.getApplication());
			
			if (result == JFileChooser.APPROVE_OPTION) {
				for (File selected : chooser.getSelectedFiles()) {
					FakGUI.getApplication().addFile(selected);
				}
			}
		}
	};
	
	public ActionListener getFileOpenAction() {
		return m_openAction;
	}
}
