package com.faktel.filters;

import java.io.File;

import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;

/**
 * This is base interface of filters.
 * 
 */
public interface RowFilter {

	/**
	 * 
	 * @return true if preparation to execute filter was ok, false otherwise.
	 *         When return is false then the execute method will not be called.
	 *         Method cleanup will always be called.
	 */
	public boolean prepare(FilterArgs args);

	/**
	 * @param model
	 * @param workingDir
	 *            File A folder where this filter can read and write. No other
	 *            filters will access this folder.
	 * @param allFiltersDir
	 *            File A folder supplied to all folders. Usually it is the
	 *            parent of workingDir
	 * @return Grid to be shown in views as a final result
	 */
	public Grid execute(Model model, File workingDir, File allFiltersDir);

	public boolean cleanup(Model model, File workingDir, File commonDir);
	
}
