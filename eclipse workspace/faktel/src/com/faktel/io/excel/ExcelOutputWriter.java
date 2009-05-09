package com.faktel.io.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.faktel.Utils;
import com.faktel.io.DefaultFileMapping;
import com.faktel.io.OutputWriter;

/**
 * Writer implementation over Excel files.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class ExcelOutputWriter extends DefaultFileMapping implements
		OutputWriter {
	private static final Logger logger = Logger
			.getLogger(ExcelOutputWriter.class.getName());
	private static final String SOURCE_KEY = "output";
	private static final String MAPPING_KEY = "mappingFile";
	private static final String SHEET_NAME_KEY = "sheetName";

	private Map<String, Collection<String>> m_args;

	public ExcelOutputWriter(Map<String, Collection<String>> args) {
		super(new File(Utils.takeFirstArg(args.get(SOURCE_KEY))), new File(
				Utils.takeFirstArg(args.get(MAPPING_KEY))));
		m_args = args;
	}

	/**
	 * Output given data.
	 * 
	 * @param data
	 *            which should be output
	 * @return null
	 */
	@SuppressWarnings( { "unchecked", "deprecation" })
	public Object doOutput(Object data) throws IOException {
		if (data instanceof Collection) {
			HSSFWorkbook wb = new HSSFWorkbook();
			Collection<String> sheets = m_args.get(SHEET_NAME_KEY);
			HSSFSheet sheet = null;
			if (sheets != null && sheets.size() > 0) {
				String sheetName = sheets.iterator().next();
				sheet = wb.createSheet(sheetName);
			} else {
				sheet = wb.createSheet();
			}

			sheet.setDefaultColumnWidth((short) 20);

			int rowIdx = 0;
			for (String[] dataRow : (Collection<String[]>) data) {
				HSSFRow row = sheet.createRow(rowIdx++);
				for (int i = 0; i < dataRow.length; i++) {
					HSSFCell cell = row.createCell((short) i);
					cell.setCellValue(dataRow[i]);
				}
			}

			logger.info("Writing '" + data.getClass() + "' to "
					+ m_dataFile.getAbsolutePath());
			FileOutputStream fileOut = new FileOutputStream(m_dataFile);
			wb.write(fileOut);
			fileOut.close();
		} else if (data != null) {
			logger.warning(ExcelOutputWriter.class
					+ " doesn't not supports class " + data.getClass());
		}

		return null;
	}
}
