package com.faktel.mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * The grid which would be displayed by views. Ќа срещата се разбрахме всички
 * изгледи да могат да показват таблица. “ова е структурата, ко€то представ€
 * тази таблица.
 * 
 */
@SuppressWarnings("serial")
public class Grid extends ArrayList<GridRow> {
	public static Grid newGrid(GridRow row) {
		Grid res = new Grid();
		res.add(row);
		return res;
	}

	public String toString() {
		int size = this.size();
		String res = "";

		final int maxRows = 5;
		int min = Math.min(size, maxRows);
		String sep = "";
		for (int i = 0; i < min; i++) {
			res += sep + this.get(i);
			sep = "\r\n\t";
		}
		if (min < size) {
			res += sep + " ... " + size
					+ " rows in total (including the ones above).";
		}
		return res;
	}

	public List<String[]> toListOfStringArrays() {
		List<String[]> res = new ArrayList<String[]>();
		for(GridRow row: this){
			res.add(row.toStringArray());
		}
		return res;
	}
	
	
}
