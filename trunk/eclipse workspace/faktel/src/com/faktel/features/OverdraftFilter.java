package com.faktel.features;

import java.io.File;

import com.faktel.filters.FilterArgs;
import com.faktel.filters.RowFilter;
import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;

public class OverdraftFilter implements RowFilter {

	public boolean cleanup(Model model, File workingDir, File commonDir) {
		return false;
	}

	public Grid execute(Model model, File workingDir, File allFiltersDir) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean prepare(FilterArgs args) {
		// TODO Auto-generated method stub
		return false;
	}

}
