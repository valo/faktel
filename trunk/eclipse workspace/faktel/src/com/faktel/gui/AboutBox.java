package com.faktel.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * About box of the faktel project
 * 
 * @author teodor.stoev
 */
public class AboutBox extends JDialog {
	private static final long serialVersionUID = 1L;

	public AboutBox() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		this.setTitle("About Faktel");
		
		//Create a panel and add the buttons
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(10, 8));
		
		Box controlsBox = Box.createVerticalBox();
		
		JLabel projectName = new JLabel("Faktel");
		
		JLabel projectVersion = new JLabel("Version 0.8 alpha");
		
		JLabel projectAuthors = new JLabel("Created by Румен Бъров, Теодор Стоев, Димитър Г. Димитров, Светла Николова, Димитър Димитров  ");
		
		JButton closeButton = new JButton("OK");
		closeButton.addActionListener(new CloseButtonListener());
		
		controlsBox.add(Box.createRigidArea(new Dimension(12, 8)));
		controlsBox.add(projectName);
		controlsBox.add(Box.createVerticalStrut(10));
		controlsBox.add(projectVersion);
		controlsBox.add(Box.createVerticalStrut(10));
		controlsBox.add(projectAuthors);
		controlsBox.add(Box.createVerticalStrut(10));
		controlsBox.add(closeButton);
		
		contentPane.add(controlsBox, BorderLayout.PAGE_START);
	}
	
	private class CloseButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
		}
	}
	
	public void showDialog() {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
