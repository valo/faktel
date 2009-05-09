package com.faktel;

import java.util.TreeMap;

/**
 * @author rumen
 * Collection which stores all the parsed data for calls.
 * Filled in by parsers.
 */
//TODO should be immutable through all the filters 
public class InvoiceRows extends TreeMap<Integer, InvoiceRow> {

	private static final long serialVersionUID = -2697988747425263997L;

}
