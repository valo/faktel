package com.faktel.mvc;

import java.util.logging.Logger;

import com.faktel.InvoiceRows;
import com.faktel.exception.NotImplementedYetException;
import com.faktel.mvc.commands.Append;

public class Model extends java.util.Observable {

	public Model(InvoiceRows rows) {
		super();
		this.rows = rows;
	}

	/**
	 * A const reference to the invoice provided by the mobile operator.
	 */
	private final InvoiceRows rows;

	/**
	 * Data shared between all the filters, views and controller.
	 */
	private DataPool dataPool = new DataPool();
	/**
	 * The grid shown in view This is a list of lists containing objects. Each
	 * object should have a good toString() implementation.
	 */
	private Grid grid = new Grid();

	public DataPool getDataPool() {
		return dataPool;
	}

	public InvoiceRows getRows() {
		return (InvoiceRows) rows.clone();
	}

	public void gridAppend(GridRow row) {
		grid.add(row);
		setChanged();
		notifyObservers(new Append(row));
	}

	public void gridInsert(int pos, GridRow row) {
		NotImplementedYetException.warnAboutMe(Logger.getLogger(Model.class
				.getName()));
	}

	public Grid gridSet(Grid newGrid) {
		Grid res = grid;
		grid = newGrid;
		return res;
	}

	public Grid gridGetCopy() {
		return (Grid) grid.clone();
	}

	public Grid gridClear() {
		return gridSet(new Grid());
	}

}
