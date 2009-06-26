package com.faktel.test.config;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import javax.swing.Box;

import com.faktel.config.ConfigParser;
import com.faktel.features.SimpleGrid;
import com.faktel.features.VerticalPieChartView;
import com.faktel.io.OutputWriter;
import com.faktel.mvc.View;

import junit.framework.TestCase;

public class TestConfigParser extends TestCase {
	private ConfigParser m_parser = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		File configFile = new File("config/settings.xml");
		try {
			m_parser = new ConfigParser(configFile);
		} catch (Exception e) {
			fail("An error occured while parsing config file.");
		}
	}
	
	public void testHasOutputWriters() {
		assertNotNull(m_parser.getOutputWriters());
		
		Collection<OutputWriter> writers = m_parser.getOutputWriters();
		assertEquals(false, writers.isEmpty());
	}

	public void testHasLayout() {
		assertNotNull(m_parser.getLayout());
		assertEquals(Box.class, m_parser.getLayout().getClass());
	}
	
	public void testHasViewMapping() {
		Map<String, View> viewMapping = m_parser.getViewMapping();
		
		assertNotNull(viewMapping.get("table"));
		assertEquals(SimpleGrid.class, viewMapping.get("table").getClass());
		
		assertNotNull(viewMapping.get("pie"));
		assertEquals(VerticalPieChartView.class, viewMapping.get("pie").getClass());
	}
}
