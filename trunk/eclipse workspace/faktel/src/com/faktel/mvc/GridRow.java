package com.faktel.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Row from the grid of rows shown in the view.
 * 
 */
public class GridRow {

	// TODO - change the data holder from String is to List.
	// Each element kept in the list should have appropriate toString()
	private List<Object> row = new ArrayList<Object>();

	public GridRow() {
		super();
	}
	
	public GridRow(Object[] objects) {
		super();
		set(objects);
	}

	public List<Object> get() {
		return row;
	}

	public List<Object> set(List<Object> newRow) {
		assert (null != newRow);
		List<Object> res = row;
		row = newRow;
		return res;
	}

	@Deprecated
	public List<Object> set(Object[] newRow) {
		assert (null != newRow);
		int len = newRow.length;
		ArrayList<Object> arrRow = new ArrayList<Object>(len);
		for (int i = 0; i < len; i++) {
			arrRow.add(i, newRow[i]);
		}
		return set(arrRow);
	}

	public List<Object> clear() {
		List<Object> res = row;
		return res;

	}

	public String toString() {
		return Arrays.deepToString(row.toArray());
	}

	public String[] toStringArray() {
		int len = row.size();
		String[] res = new String[len];
		for (int i = 0; i < len; i++) {
			Object o = row.get(i);
			res[i] = o.toString();
		}
		return res;
	}
}
