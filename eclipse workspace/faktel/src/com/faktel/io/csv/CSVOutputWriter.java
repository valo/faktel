package com.faktel.io.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.Utils;
import com.faktel.io.DefaultFileMapping;
import com.faktel.io.OutputWriter;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Writer implementation over csv files.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class CSVOutputWriter extends DefaultFileMapping implements OutputWriter {
	private static final Logger logger = Logger.getLogger(CSVOutputWriter.class
			.getName());
	private static final String SOURCE_KEY = "output";
	private static final String MAPPING_KEY = "mappingFile";

	public CSVOutputWriter(Map<String, Collection<String>> args) {
		super(new File(Utils.takeFirstArg((args.get(SOURCE_KEY)))),
				new File(Utils.takeFirstArg(args.get(MAPPING_KEY))));
	}

	/**
	 * Output given data.
	 * 
	 * @param data
	 *            which should be output
	 * @return null
	 */
	public Object doOutput(Object data) throws IOException {
		if (data instanceof List<?>) {
			logger.info("Writing  '" + data.getClass() + "' to "
					+ m_dataFile.getAbsolutePath());
			CSVWriter writer = new CSVWriter(new FileWriter(m_dataFile));
			writer.writeAll((List<?>) data);
			writer.flush();
			writer.close();
		} else if (data != null) {
			logger.warning(CSVInputReader.class
					+ " doesn't not supports class " + data.getClass());
		}

		return null;
	}
}
