package com.faktel.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.faktel.InvoiceRow;
import com.faktel.io.csv.CSVOutputWriter;
import com.faktel.io.excel.ExcelOutputWriter;

/** Factory, transforming given infrastructure to source.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class OutputWriterFactory {
	private static final Logger logger = Logger.getLogger(OutputWriterFactory.class.getName());

	private static Map<String, Class<?>> m_outputTypes;
	
	static {
		m_outputTypes = new HashMap<String, Class<?>>();
		m_outputTypes.put("csvwriter", CSVOutputWriter.class);
		m_outputTypes.put("excelwriter", ExcelOutputWriter.class);
		m_outputTypes.put("xlswriter", ExcelOutputWriter.class);
	}
	
	private static Class<?> findClass(String className) {
		Class<?> cl = m_outputTypes.get(className.toLowerCase());
		if (cl == null) {
			try {cl = Class.forName(className); } catch (ClassNotFoundException e) { }
		}
		
		return cl;
	}
	
	/** 
	 * Create output writer
	 * 
	 * @param type Writer type
	 * @param args Writer args
	 * @return generated writer
	 */
	public static OutputWriter createOutputWriter(String type, Map<String, Collection<String>> args) {
		Class<?> cl = findClass(type);
		if (cl == null) {
			logger.warning("Cannot find output provider from type: " + type);
		}
		else {
			try {
				Constructor<?> cons = cl.getConstructor(Map.class);
				return (OutputWriter) cons.newInstance(args);	
			}
			catch (Exception ex) {
				logger.warning("Exception occurs during creation of output writer from type='" + type + "'. Exception: " + ex);
			}
		}
		
		return null;
	}
	
	/** 
	 * Create input reader
	 * 
	 * @param os Source stream
	 * @param mappingFile Mapping file
	 * @return generated writer
	 */
	public static Collection<InvoiceRow> createInputReader(OutputStream os, File mappingFile) throws IOException {
		throw new NotImplementedException();
	}
}
