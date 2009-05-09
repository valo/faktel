package com.faktel.mvc.commands;

import java.util.ArrayList;

import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;

/**
 * Implement this for each command
 * 
 */
public abstract class Command {
	protected Command(CommandName name, int idx, Grid changedRows) {
		super();
		this.name = name;
		this.idx = idx;
		this.changedRows = changedRows;
	}

	public static final int UNUSED_INDEX = -2;
	public static final Grid UNUSED_ELEMENTS = null;

	public CommandName name;
	public int idx;
	public ArrayList<GridRow> changedRows;

	public String toString() {
		String res = name + (UNUSED_INDEX == idx ? "" : ("@" + idx))
				+ (null == changedRows ? "" : (": " + changedRows));
		return res;
	}

}
