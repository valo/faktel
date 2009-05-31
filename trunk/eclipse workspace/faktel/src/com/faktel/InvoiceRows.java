package com.faktel;

import java.util.ArrayList;

import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;

/**
 * @author rumen
 * Collection which stores all the parsed data for calls.
 * Filled in by parsers.
 */
//TODO should be immutable through all the filters 
public class InvoiceRows extends ArrayList<InvoiceRow> {

	private static final long serialVersionUID = -2697988747425263997L;
	
	public Grid toGrid(){
		Grid res = new Grid();
		for ( InvoiceRow row: this){
			GridRow gridRow = row.toGridRow();
			res.add(gridRow);
		}
		return res;
	}
	

}
