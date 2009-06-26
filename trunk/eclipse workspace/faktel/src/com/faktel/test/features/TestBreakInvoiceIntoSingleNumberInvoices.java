package com.faktel.test.features;

import java.io.File;
import java.io.IOException;

import com.faktel.InvoiceRows;
import com.faktel.Utils;
import com.faktel.features.BreakInvoiceIntoSingleNumberInvoices;
import com.faktel.filters.FilterArgs;
import com.faktel.io.InputReader;
import com.faktel.io.InputReaderFactory;
import com.faktel.io.invoice.InvoiceRowReader;
import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;

import junit.framework.TestCase;

public class TestBreakInvoiceIntoSingleNumberInvoices extends TestCase {
	private InputReader m_reader = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		File inputFile = new File("examples/mtel/0164577446_invoice_detailREAL.xls");
		File mappingFile = new File("config/mtel-mapping.in");
		m_reader = InputReaderFactory.createInputReader(inputFile, mappingFile);
	}
	
	public void testExecute() {
		FilterArgs args = new FilterArgs();
		BreakInvoiceIntoSingleNumberInvoices filter = new BreakInvoiceIntoSingleNumberInvoices(args);
		
		InvoiceRowReader rowReader = new InvoiceRowReader(m_reader);
		
		InvoiceRows rowData = null;
		try {
			rowData = rowReader.readAllRowData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Model model = new Model(rowData);
		File runDir = Utils.createFolderForCurrentRun();
		File workingDir = Utils.createFolderForFilter(String.format("%03d-", 0)
				+ filter.getClass().getName(), runDir);
		
		Grid result = filter.execute(model, workingDir, runDir);
		
		String breakdownFileName = null;
		File breakdownFile = null;
		
		assertEquals("Invoice split into 228 single-number invoices 228 rows in total.", result.get(0).get().get(0).toString());
		
		assertEquals("Invoice for 359885966565 has ", result.get(2).get().get(0).toString());
		assertEquals("1", result.get(2).get().get(1).toString());
		assertEquals(" entries.", result.get(2).get().get(2).toString());
		
		breakdownFileName = workingDir.getAbsolutePath() + File.separator + "359885966565.xls";
		breakdownFile = new File(breakdownFileName);
		assertTrue(breakdownFile.exists());
		
		assertEquals("Invoice for 359888547669 has ", result.get(3).get().get(0).toString());
		assertEquals("1", result.get(3).get().get(1).toString());
		assertEquals(" entries.", result.get(3).get().get(2).toString());
		
		breakdownFileName = workingDir.getAbsolutePath() + File.separator + "359888547669.xls";
		breakdownFile = new File(breakdownFileName);
		assertTrue(breakdownFile.exists());
		
		assertEquals("Invoice for 359886754425 has ", result.get(4).get().get(0).toString());
		assertEquals("1", result.get(4).get().get(1).toString());
		assertEquals(" entries.", result.get(4).get().get(2).toString());
		
		breakdownFileName = workingDir.getAbsolutePath() + File.separator + "359886754425.xls";
		breakdownFile = new File(breakdownFileName);
		assertTrue(breakdownFile.exists());
	}
	
}
