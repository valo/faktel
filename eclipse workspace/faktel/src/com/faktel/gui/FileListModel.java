package com.faktel.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class FileListModel extends AbstractListModel {
	private static final long serialVersionUID = 5506141799840614303L;
	
	public List<File> m_files = new ArrayList<File>();

	public Object getElementAt(int index) {
		return m_files.get(index);
	}

	public int getSize() {
		return m_files.size();
	}
	
	public boolean addFile(File file) {
		for (File f : m_files) {
			if (f.equals(file)) return false;
		}
		
		m_files.add(file);
		fireContentsChanged(this, m_files.size() - 1, m_files.size() - 1);
		return true;
	}
}
