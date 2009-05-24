package com.faktel.features;

import javax.swing.table.AbstractTableModel;

import com.faktel.mvc.Grid;

public class GridTableModelAdapter extends AbstractTableModel {
	private static final long serialVersionUID = 2298119600213141258L;
	
	private Grid m_grid;
	
	public GridTableModelAdapter(Grid grid) {
		m_grid = grid;
	}

	public int getColumnCount() {
		if (m_grid.size() == 0) {
			return 0;
		}
		
		return m_grid.get(0).get().size(); 
	}

	public int getRowCount() {
		return m_grid.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return m_grid.get(rowIndex).get().get(columnIndex);
	}

}
