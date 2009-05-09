package com.faktel.scrapped_code.filters;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.filters.RowFilter;
import com.faktel.io.InputReader;
import com.faktel.io.InputReaderFactory;

/** Bind group with accumulated row data.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public abstract class GroupBinderRowFilter implements RowFilter {
	//log utility
	private static final Logger logger = Logger.getLogger(GroupBinderRowFilter.class.getName());

	private static final String SOURCE   = "source";
	private static final String BIND     = "bind";
	private static final String OVERRIDE = "override";


	private Collection<String> m_sources;
	private Collection<String> m_bindingFields;
	private Collection<String> m_overrideFields;
	
	public GroupBinderRowFilter(Map<String, Collection<String>> args) {
		m_sources        = args.get(SOURCE);
		m_bindingFields  = args.get(BIND);
		m_overrideFields = args.get(OVERRIDE);
	}
	
	private int findIndex(String[] col, String item) {
		for (int i = 0; i < col.length; i++) {
			if (col[i].equalsIgnoreCase(item)) return i;
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(Collection<InvoiceRow> orginalData, File workingDir, Object referenced, Object specificParams) {
		if (!(referenced instanceof Collection)) return null;
		Collection<InvoiceRow> refData = (Collection<InvoiceRow>) referenced;
		
		if (m_sources == null) {
			logger.warning("Cannot find a source for Group mapping");
		}
		else {
			for (String f : m_sources) {
				File file = new File(f);
				if (!file.exists() || file.isDirectory()) {
					logger.severe("Regular file " + file + " doesn't exists");
					continue;
				}
				
				InputReader ir = InputReaderFactory.createInputReader(file, null);
				try {
					Collection<String[]> group = ir.readRowData();
					String[] header = null;
					for (String[] row : group) {
						if (header == null) {
							header = row;
						}
						else {
							NEXT_ROW:
							for (InvoiceRow r : refData) {
								//look for binding fields
								boolean found = false;
								for (String b : m_bindingFields) {
									int idx = findIndex(header, b);
									if (idx == -1) {
										logger.warning("Field " + b + " cannot be found in group headers");
										continue NEXT_ROW;
									}
									try {
										if (row[idx].equals(Utils.getRowDataField(b).get(r))) {
											found = true;
											break;
										}
									} 
									catch (Exception e) {
										logger.warning("Field " + b + " cannot be found");
										continue NEXT_ROW;
									}
								}	
								
								if (!found) continue NEXT_ROW;
								
								//this row is bound correctly, start overriding
								for (String over : m_overrideFields) {
									int idx = findIndex(header, over);
									if (idx == -1) {
										logger.warning("Field " + over + " cannot be found in group headers");
										continue NEXT_ROW;
									}
									try {
										Utils.getRowDataField(over).set(r, row[idx]);
									} 
									catch (Exception e) {
										logger.warning("Field " + over + " cannot be found");
										continue NEXT_ROW;
									}
								}	
							}
						}
					}
				} 
				catch (IOException e) {
					logger.severe("IO error occurs while reading from file " + f + ". Exception: " + e);
					continue;
				}
			}
						
		}
		
		return refData;
	}
	
}
