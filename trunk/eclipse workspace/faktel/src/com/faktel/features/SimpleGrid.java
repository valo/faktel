package com.faktel.features;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.faktel.mvc.Grid;
import com.faktel.mvc.View;

public class SimpleGrid extends View {
	private static final long serialVersionUID = 856044933984188321L;
	
	private JScrollPane m_scrollPane;
	private JTable m_table;
	
	public SimpleGrid(String name) {
		super(name);
		m_table = new JTable();
		m_scrollPane = new JScrollPane(m_table);
		
		add(m_scrollPane);
		m_scrollPane.setVisible(true);
		m_table.setVisible(true);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setVisible(true);
	}
	
	@Override
	public boolean displayGrid(Grid grid) {
		m_table.setModel(new GridTableModelAdapter(grid));
		return true;
	}
}
