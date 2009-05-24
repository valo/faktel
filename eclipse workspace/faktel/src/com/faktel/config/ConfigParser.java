package com.faktel.config;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.faktel.filters.FilterArgs;
import com.faktel.filters.FilterInfo;
import com.faktel.io.InputReader;
import com.faktel.io.InputReaderFactory;
import com.faktel.io.OutputWriter;
import com.faktel.io.OutputWriterFactory;
import com.faktel.mvc.View;

/**
 * Configuration parser.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class ConfigParser {
	private static final Logger logger = Logger
			.getLogger(ConfigParserImpl.class.getName());

	private InputReader m_input;
	@Deprecated
	private List<FilterInfo> m_flowFilters = new LinkedList<FilterInfo>();
	private Collection<OutputWriter> m_writers = new LinkedList<OutputWriter>();
	
	private Box m_layout;
	private Map<String, View> m_viewMapping;

	/**
	 * Create configuration parser by configuration file
	 */
	public ConfigParser(File config) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(config, new ConfigParserImpl());
	}

	/**
	 * Get parsed input reader.
	 * 
	 * @return Input reader
	 */
	public InputReader getInputReader() {
		return m_input;
	}

	/**
	 * Get parsed row filters
	 * 
	 * @return Row filters
	 */
	public List<FilterInfo> getRowFilters() {
		return m_flowFilters;
	}

	/**
	 * Get parsed output writers
	 * 
	 * @return Output writers
	 */
	public Collection<OutputWriter> getOutputWriters() {
		return m_writers;
	}
	
	public JComponent getLayout() {
		return m_layout;
	}
	
	public Map<String, View> getViewMapping() {
		return m_viewMapping;
	}

	/**
	 * XML configuration parser implementation.
	 * 
	 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
	 * @version 1.0
	 */
	private class ConfigParserImpl extends DefaultHandler {
		// inputs
		private static final String INPUT = "input";
		private static final String INPUT_ARG_FILE = "input";
		private static final String INPUT_ARG_MAPPING = "mappingFile";
		// filters
		private static final String FILTER = "filter";
		private static final String FILTER_ARG_CLASS = "class";
		// filter arguments
		private static final String ARGUMENT = "arg";
		private static final String ARGUMENT_ARG_NAME = "name";
		private static final String ARGUMENT_ARG_VALUE = "value";
		// outputs
		private static final String OUTPUT = "output";
		private static final String OUTPUT_ARG_TYPE = "type";
		// flow
		private static final String FLOW = "flow";
		private static final String PROVIDER_NAME = "name";
		
		// layout
		private static final String LAYOUT = "layout";

		private Map<String, InputReader> m_genricInputs = new LinkedHashMap<String, InputReader>();
		private Map<String, FilterInfo> m_declaredFilters = new LinkedHashMap<String, FilterInfo>();
		private Map<String, OutputWriter> m_genericWriters = new LinkedHashMap<String, OutputWriter>();

		private boolean m_inFlowSection;
		private Stack<DefaultHandler> m_handlerStack = new Stack<DefaultHandler>();
		private StringBuilder m_builder = new StringBuilder();
		// filter args
		private String m_name;
		private String m_filterClass;
		private String m_outputType;
		private FilterArgs m_argruments = new FilterArgs();

		/**
		 * Handle xml start tag
		 */
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			if (m_handlerStack.size() > 0) {
				m_handlerStack.peek().startElement(uri, localName, name, attributes);
				return;
			}
			
			if (name.equalsIgnoreCase(LAYOUT)) {
				m_handlerStack.push(new LayoutParser(this));
				return;
			}
			
			if (!m_inFlowSection) {
				if (name.equalsIgnoreCase(INPUT)) {
					String providerName = attributes.getValue(PROVIDER_NAME);
					String inputStr = attributes.getValue(INPUT_ARG_FILE);
					String mappingStr = attributes.getValue(INPUT_ARG_MAPPING);
					addInput(providerName, inputStr, mappingStr);
				} else if (name.equalsIgnoreCase(FILTER)) {
					m_name = attributes.getValue(PROVIDER_NAME);
					m_filterClass = attributes.getValue(FILTER_ARG_CLASS);
					if (m_filterClass == null) {
						logger
								.info("Filter["
										+ m_name
										+ "] cannot be created because its class name is not specified");
					}
				} else if (name.equalsIgnoreCase(ARGUMENT)) {
					String argname = attributes.getValue(ARGUMENT_ARG_NAME);
					String argvalue = attributes.getValue(ARGUMENT_ARG_VALUE);
					Collection<String> col = m_argruments.get(attributes
							.getValue(ARGUMENT_ARG_NAME));
					if (col == null) {
						col = new LinkedList<String>();
						m_argruments.put(argname, col);
					}
					col.add(argvalue);
				} else if (name.equalsIgnoreCase(OUTPUT)) {
					m_name = attributes.getValue(PROVIDER_NAME);
					m_outputType = attributes.getValue(OUTPUT_ARG_TYPE);
				} else if (name.equalsIgnoreCase(FLOW)) {
					m_inFlowSection = true;
				}
			} else {
				// we are in FLOW section
				String flowName = attributes.getValue(PROVIDER_NAME);
				if (name.equalsIgnoreCase(INPUT)) {
					InputReader ir = m_genricInputs.get(flowName);
					if (ir == null) {
						logger.warning("FlowInputReader[" + flowName
								+ "] is not defined");
					} else {
						m_input = ir;
					}
				} else if (name.equalsIgnoreCase(FILTER)) {
					FilterInfo fi = m_declaredFilters.get(flowName);
					if (fi == null) {
						logger.warning("FlowFilter[" + flowName
								+ "] is not defined");
					} else {
						m_flowFilters.add(fi);
					}
				} else if (name.equalsIgnoreCase(OUTPUT)) {
					OutputWriter wr = m_genericWriters.get(flowName);
					if (wr == null) {
						logger.warning("FlowWriter[" + flowName
								+ "] is not defined");
					} else {
						m_writers.add(wr);
					}
				}
			}// FLOW section
		}

		/**
		 * Handle xml end tag
		 */
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (name.equalsIgnoreCase(LAYOUT)) {
				m_layout = ((LayoutParser)m_handlerStack.peek()).getLayout();
				m_viewMapping = ((LayoutParser)m_handlerStack.peek()).getViewMapping();
				m_handlerStack.pop();
				
				return;
			}
			
			if (!m_inFlowSection) {
				if (name.equalsIgnoreCase(FILTER)) {
					//create FilterInfo
					FilterArgs args = (FilterArgs) m_argruments.clone();
					FilterInfo fi = new FilterInfo(m_name, m_filterClass, args);
					FilterInfo old = m_declaredFilters.put(fi.getName(), fi);
					if (old != null) {
						logger.warning("Filter " + old
								+ " already exists. Replaced with " + fi);
					}
					m_filterClass= null;
					m_name = null;
					m_argruments.clear();

				} else if (name.equalsIgnoreCase(OUTPUT)) {
					addOutput(m_name, m_outputType,
							new HashMap<String, Collection<String>>(
									m_argruments));

					m_name = null;
					m_outputType = null;
					m_argruments.clear();
				}
			}

			m_builder.setLength(0);
		}

		/**
		 * Handle xml content value
		 */
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			m_builder.append(ch);
		}

		/**
		 * Add new input reader.
		 * 
		 * @param providerName
		 *            Input reader name
		 * @param inputStr
		 *            inputFile
		 * @param mappingStr
		 *            mappingFile
		 */
		private void addInput(String providerName, String inputStr,
				String mappingStr) {
			if (inputStr == null || inputStr.length() == 0) {
				logger.warning("InputProvider[" + providerName
						+ "]: input file is not specified.");
				return;
			}
			File inputFile = new File(inputStr);
			if (!inputFile.exists()) {
				logger.warning("InputProvider[" + providerName
						+ "]: input file does not exists: " + inputFile);
				return;
			}

			if (mappingStr == null || mappingStr.length() == 0) {
				logger.warning("InputProvider[" + providerName
						+ "]: mapping file is not specified.");
				return;
			}
			File mappingFile = new File(mappingStr);
			if (!mappingFile.exists()) {
				logger.warning("InputProvider[" + providerName
						+ "]: mapping file does not exists: " + mappingFile);
				return;
			}

			InputReader ir = InputReaderFactory.createInputReader(inputFile,
					mappingFile);
			if (ir == null) {
				logger.warning("Cannot generate InputProvider[" + providerName
						+ "]");
			} else {
				m_genricInputs.put(providerName, ir);
			}
		}

		private void addOutput(String providerName, String outputType,
				Map<String, Collection<String>> args) {
			OutputWriter wr = OutputWriterFactory.createOutputWriter(
					outputType, args);
			if (wr == null) {
				logger.warning("Cannot generate OutputProvider[" + providerName
						+ "]");
			} else {
				m_genericWriters.put(providerName, wr);
			}
		}
	}
}
