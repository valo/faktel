package com.faktel.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.exception.NotImplementedYetException;

/** Mapping utility class.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class MappingParser {
	public static final Logger logger = Logger.getLogger(MappingParser.class.getName());	

	/** 
	 * Create field mapping
	 * 
	 * @param mappingFile Mapping file
	 * @return field mapping
	 * @throws IOException if an I/O errors occurs during file reading
	 */
	public static Map<String, String> readMapping(File mappingFile) throws IOException{
		FileInputStream is = null;
		BufferedReader reader = null;
		try {
			Map<String, String> mapping = new LinkedHashMap<String, String>();
			reader = new BufferedReader(new InputStreamReader(is = new FileInputStream(mappingFile), Charset.forName("UTF-8")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) continue;
				
				int idx = line.indexOf(":");
				if (idx > 0) {
					mapping.put(line.substring(0, idx).trim(), line.substring(idx + 1).trim());
				}
			}
			
			return mapping;
		}
		finally {
			if (reader != null) reader.close();
			if (is != null) is.close();	
		}
	}
	
	/**
	 * @param fields - all those fields should exist in the mapping
	 * @param mapping - mapping from fields to DataRow field names 
	 * @return
	 */
	public static boolean checkIfMappingForAllFieldsExists(String[] fields, Map<String,String> mapping){
		NotImplementedYetException.warnAboutMe(MappingParser.logger);
		return true;
	}
}
