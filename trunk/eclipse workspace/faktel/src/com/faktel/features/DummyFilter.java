package com.faktel.features;

import java.io.File;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.filters.FilterArgs;
import com.faktel.filters.RowFilter;
import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;
import com.faktel.mvc.Model;

public class DummyFilter implements RowFilter {

	public static final String MYFIELD = "DummyFilter.CopyMeIWantToTravel";

	public DummyFilter(FilterArgs args) {
		System.out.println("Dummy constructor was invoked");
		Utils.takeFirstArg(args.get("argument1"));
		System.out.println("Arguments supplied: " + args);
	}

	public Grid execute(Model model, File workingDir, File allFiltersDir) {

		if (!model.getDataPool().exists(MYFIELD)) {
			java.util.Date date = new java.util.Date(System.currentTimeMillis());
			model.getDataPool().put(MYFIELD, date);
			GridRow row = new GridRow(new Object[] {
					"Running filter for the first time at ", date });
			model.gridAppend(row);
			model.gridAppend(new GridRow());
		} else {
			Object o = model.getDataPool().get(MYFIELD);
			GridRow row = new GridRow(new Object[] {
					"This filter was previously executed on", o });
			model.gridAppend(row);
		}

		int maxRowsToCareAbout = 3;
		for (InvoiceRow invRow : model.getRows()) {
			GridRow gridRow = new GridRow();
			gridRow.set(invRow.toArrayList());
			model.gridAppend(gridRow);
			if (0 == --maxRowsToCareAbout) {
				break;
			}
		}
	
		//result from DummyFilter
		Grid res = model.gridGetCopy();
		
		//append result from other filter too :)
		res.addAll(
				new BreakInvoiceIntoSingleNumberInvoices(new FilterArgs()).execute(model,
						workingDir, allFiltersDir));
		return res; 
	}

	public boolean cleanup(Model model, File workingDir, File commonDir) {
		return true;
	}

	public boolean prepare(FilterArgs args) {
		return true;
	}

}
