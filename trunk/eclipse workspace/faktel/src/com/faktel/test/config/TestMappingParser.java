package com.faktel.test.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.faktel.config.MappingParser;
import com.faktel.io.InputReader;
import com.faktel.io.InputReaderFactory;

import junit.framework.TestCase;

public class TestMappingParser extends TestCase {
	private InputReader m_reader = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		File inputFile = new File("examples/mtel/0164577446_invoice_detailREAL.xls");
		File mappingFile = new File("config/mtel-mapping.in");
		m_reader = InputReaderFactory.createInputReader(inputFile, mappingFile);
	}

	public void testReadMapping() {
		Map<String, String> mapping = null;
		try {
			mapping = MappingParser.readMapping(m_reader.getMapping());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertEquals("sim", mapping.get("SIM Карта"));
		assertEquals("telNumber", mapping.get("Телефонен номер"));
		assertEquals("description", mapping.get("Описание на услугата"));
	}
	
	public void testCheckIfMappingForAllFieldsExists() {
		Map<String, String> mapping = null;
		try {
			mapping = MappingParser.readMapping(m_reader.getMapping());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] fields = new String[] { "SIM Карта", "Телефонен номер", "Описание на услугата" };
		
		assertTrue(MappingParser.checkIfMappingForAllFieldsExists(fields, mapping));
		
		fields = new String[] { "SIM Карта", "Телефонен номер", "Описание на услугата", "Несъществуващо" };
		
		assertTrue(MappingParser.checkIfMappingForAllFieldsExists(fields, mapping));
	}
}
