package com.faktel.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.faktel.InvoiceRow;
import com.faktel.io.csv.CSVInputReader;
import com.faktel.io.excel.ExcelInputReader;

/** Factory, transforming given source to known infrastructure.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class InputReaderFactory {
	
	/** 
	 * Create input reader
	 * 
	 * @param file Source file
	 * @param mappingFile Mapping file
	 * @return generated reader
	 */
	public static InputReader createInputReader(File file, File mappingFile) {
		//dummy check
		InputReader ir = null;
		if (file.getName().endsWith(".xls")) {
			ir = new ExcelInputReader(file, mappingFile);
		}
		else if (file.getName().endsWith(".csv")) {
			ir = new CSVInputReader(file, mappingFile);
		}
		
		if (ir == null) throw new IllegalArgumentException("File '" + file.getName() + "' is not supported");
		return ir;
	}
	
	/** 
	 * Create input reader
	 * 
	 * @param is Source stream
	 * @param mappingFile Mapping file
	 * @return generated reader
	 */
	public static Collection<InvoiceRow> createInputReader(InputStream is, File mappingFile) throws IOException {
		throw new NotImplementedException();
	}
}
