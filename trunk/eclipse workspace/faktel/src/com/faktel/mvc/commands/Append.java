package com.faktel.mvc.commands;

import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;


public class Append extends Command {
	public Append(Grid newRows) {
		super(CommandName.APPEND, UNUSED_INDEX, newRows);
	}

	public Append(GridRow newRow) {
		super(CommandName.APPEND, UNUSED_INDEX, Grid.newGrid(newRow));
	}

}
