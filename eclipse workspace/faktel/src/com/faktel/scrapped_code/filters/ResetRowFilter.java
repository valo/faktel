package com.faktel.scrapped_code.filters;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.filters.RowFilter;

/** Reset accumulated collection.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public abstract class ResetRowFilter implements RowFilter{
	private boolean m_deleteAll;
	
	public ResetRowFilter(Map<String, Collection<String>> args) {
		m_deleteAll = Boolean.parseBoolean((String)Utils.takeFirstArg(args.get("DeleteAll")));
	}
	
	public Object execute(Collection<InvoiceRow> orginalData, File workingDir, Object referenced, Object specificParams) {
		return m_deleteAll? new LinkedList<InvoiceRow>() : orginalData;
	}
	
}
