package com.faktel.features;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.InvoiceRows;
import com.faktel.filters.FilterArgs;
import com.faktel.filters.RowFilter;
import com.faktel.gui.FakGUI;
import com.faktel.io.InputReader;
import com.faktel.io.InputReaderFactory;
import com.faktel.mvc.Grid;
import com.faktel.mvc.GridRow;
import com.faktel.mvc.Model;
import com.faktel.mvc.View;

public class JoinTableFilter implements RowFilter {
	private static final Logger s_logger = Logger.getLogger(JoinTableFilter.class.getName());
	
	private String m_otherTableFilename;
	private String m_joinColumn;
	private String m_outputViewName;

	public JoinTableFilter(FilterArgs args) {
		if (args.get("outputView") != null && args.get("outputView").size() >= 1) {
			m_outputViewName = args.get("outputView").iterator().next();
		}
		
		if (args.get("otherTableFile") != null && args.get("otherTableFile").size() >= 1) {
			m_otherTableFilename = args.get("otherTableFile").iterator().next();
		} else {
			throw new IllegalArgumentException("You have to specify otherTableFile arguments of the JoinFilter");
		}
		
		if (args.get("joinColumn") != null && args.get("joinColumn").size() >= 1) {
			m_joinColumn = args.get("joinColumn").iterator().next();
		} else {
			throw new IllegalArgumentException("You have to specify joinColumn arguments of the JoinFilter");
		}
	}
	
	public boolean cleanup(Model model, File workingDir, File commonDir) {
		return false;
	}

	@SuppressWarnings("unchecked")
	public Grid execute(Model model, File workingDir, File allFiltersDir) {
		Map<String, InvoiceRows> allNumbers =  (Map<String, InvoiceRows>) model.getDataPool().get(BreakInvoiceIntoSingleNumberInvoices.POOL_DATA_SMALL_INVOICES);
		
		if (allNumbers == null) {
			s_logger.severe("You don't have a result from BreakInvoiceIntoSingleNumberInvoices filter! Probably you haven't run the filter before this one");
			return null;
		}
		
		InputReader otherTable = InputReaderFactory.createInputReader(new File(m_otherTableFilename), null);
		Collection<String[]> otherTableData = null;
		
		try {
			otherTableData = otherTable.readRowData();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		int joinColumnIndex = -1;
		String[] firstRow = otherTableData.iterator().next();
		for (int i = 0;i < firstRow.length;i++) {
			if (firstRow[i].equalsIgnoreCase(m_joinColumn)) {
				joinColumnIndex = i;
				break;
			}
		}
		
		if (joinColumnIndex == -1) {
			s_logger.severe("Can't find a column with name " + m_joinColumn + "in the table");
			return null;
		}
		
		Grid result = new Grid();
		result.add(new GridRow(new Object[] {
			"Name",
			"Entries count"
		}));
		Iterator<String[]> currentRow = otherTableData.iterator();
		currentRow.next();
		while(currentRow.hasNext()) {
			String[] row = currentRow.next();
			InvoiceRows invoiceRows = allNumbers.get(row[joinColumnIndex]);
			
			if (invoiceRows == null) {
				continue;
			}
			// FIXME: Should make the output columns configurable
			result.add(new GridRow(new Object[] {
					row[2],
					"" + invoiceRows.size(),
			}));
		}
		
		if (m_outputViewName != null && FakGUI.getApplication() != null) {
			View outputView = FakGUI.getApplication().getView(m_outputViewName);
			
			if (outputView != null) {
				outputView.displayGrid(result);
			}
		}
		
		return result;
	}

	public boolean prepare(FilterArgs args) {
		return true;
	}
}
