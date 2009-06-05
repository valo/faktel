package com.faktel.features;

import java.io.File;

import com.faktel.filters.FilterArgs;
import com.faktel.filters.RowFilter;
import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;

public class OverdraftFilter implements RowFilter {

	@Override
	public boolean cleanup(Model model, File workingDir, File commonDir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Grid execute(Model model, File workingDir, File allFiltersDir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean prepare(FilterArgs args) {
		// TODO Auto-generated method stub
		return false;
	}

}
