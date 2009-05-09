package com.faktel.io.csv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import com.faktel.io.DefaultFileMapping;
import com.faktel.io.InputReader;

import au.com.bytecode.opencsv.CSVReader;

/** Reader implementation over csv files. 
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class CSVInputReader extends DefaultFileMapping implements InputReader{
	
	/** Create CSV parser.
	 * 
	 * @param csv CSV storage file
	 * @param mappingFile Mapping file
	 */
	public CSVInputReader(File csv, File mappingFile) {
		super(csv, mappingFile);
	}
	
	/** Drain rows from given source.
	 * 
	 * @return Collection of drained data
	 * @throws IOException if I/O errors occurs during file reading
	 */
	public Collection<String[]> readRowData() throws IOException {
		CSVReader reader = new CSVReader(new FileReader((File)m_dataFile));
		Collection<String []> res = new LinkedList<String[]>();
		String [] nextLine = null;
		while ((nextLine = reader.readNext()) != null) {
			res.add(nextLine);
		}
		
		return res;
	}
}
