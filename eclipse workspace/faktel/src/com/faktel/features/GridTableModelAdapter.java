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
	
	@Override
	public String getColumnName(int column) {
		if (m_grid.size() == 0) {
			return "";
		}
		
		return m_grid.get(0).get().get(column).toString();
	}

	public int getRowCount() {
		return m_grid.size() - 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return m_grid.get(rowIndex + 1).get().get(columnIndex);
	}

}
