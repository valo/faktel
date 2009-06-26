package com.faktel.test.features;

import java.io.File;
import java.io.IOException;

import com.faktel.InvoiceRows;
import com.faktel.Utils;
import com.faktel.features.DummyFilter;
import com.faktel.filters.FilterArgs;
import com.faktel.io.InputReader;
import com.faktel.io.InputReaderFactory;
import com.faktel.io.invoice.InvoiceRowReader;
import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;

import junit.framework.TestCase;

public class TestDummyFilter extends TestCase {
	private InputReader m_reader = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		File inputFile = new File("examples/mtel/0164577446_invoice_detailREAL.xls");
		File mappingFile = new File("config/mtel-mapping.in");
		m_reader = InputReaderFactory.createInputReader(inputFile, mappingFile);
	}

	public void testExecute() {
		FilterArgs args = new FilterArgs();
		DummyFilter dummy = new DummyFilter(args);
		
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
				+ dummy.getClass().getName(), runDir);
		
		Grid result = dummy.execute(model, workingDir, runDir);
		
		assertEquals("Running filter for the first time at ", result.get(0).get().get(0).toString());
		
		assertEquals("8935901087001503099", result.get(2).get().get(0).toString());
		assertEquals("359888086789", result.get(2).get().get(1).toString());
		
		assertEquals("8935901087001582846", result.get(3).get().get(0).toString());
		assertEquals("359885070446", result.get(3).get().get(1).toString());
		
		assertEquals("8935901087001667308", result.get(4).get().get(0).toString());
		assertEquals("359887061866", result.get(4).get().get(1).toString());
	}

}
