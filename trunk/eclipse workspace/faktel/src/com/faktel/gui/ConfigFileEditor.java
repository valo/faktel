package com.faktel.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Dialog window that allows the user to edit the configuration file.
 * TODO: this is a temporary solution.
 * 
 * @author mitex
 *
 */
public class ConfigFileEditor extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private File settingsFile;
	
	public ConfigFileEditor(Component component, File settingsFile) {
		this.settingsFile = settingsFile;

		this.setSize(640, 480);
		this.setLocationRelativeTo(null);
		this.setTitle("Edit configuration");
		this.getContentPane().setLayout(
				new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		final JTextArea textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(readFile());
		textArea.setCaretPosition(0);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		this.getContentPane().add(scrollPane);
		
		JPanel buttonsPanel = new JPanel();
		
		JButton okButton = new JButton("Save");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				writeFile(textArea.getText());
				ConfigFileEditor.this.setVisible(false);
			}
			
		});
		buttonsPanel.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigFileEditor.this.setVisible(false);
			}
			
		});
		buttonsPanel.add(cancelButton);
		
		this.getContentPane().add(buttonsPanel);
}
	
	private String readFile() {
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(settingsFile));
			int length = (int) settingsFile.length();
			char[] buffer = new char[length];
			input.read(buffer, 0, length);
			String string = new String(buffer);
			return new String(string.getBytes(),"UTF-8");
		} catch (IOException e) {
			Logger.getLogger(this.getClass().getName()).throwing(
					this.getClass().getName(), "readFile", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Logger.getLogger(this.getClass().getName()).throwing(
							this.getClass().getName(), "readFile", e);
				}
			}
		}
		return "";
	}
	
	private void writeFile(String text) {
		 Writer output = null;
		try {
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(settingsFile), "UTF8"));
			output.write(text);
		} catch (IOException e) {
			Logger.getLogger(this.getClass().getName()).throwing(
					this.getClass().getName(), "writeFile", e);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				Logger.getLogger(this.getClass().getName()).throwing(
						this.getClass().getName(), "writeFile", e);
			}
		}
	}
}
