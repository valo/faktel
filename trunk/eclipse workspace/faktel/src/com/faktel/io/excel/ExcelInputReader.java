package com.faktel.io.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.faktel.io.DefaultFileMapping;
import com.faktel.io.InputReader;

/** Reader implementation over xls files. 
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class ExcelInputReader extends DefaultFileMapping implements InputReader {
	private static final Logger logger = Logger.getLogger(ExcelInputReader.class.getName());
	
	/** Create Excel parser.
	 * 
	 * @param xls Excel storage file
	 * @param mappingFile Mapping file
	 */
	public ExcelInputReader(File xls, File mappingFile) {
		super(xls, mappingFile);
	}
	
	/**
	 * Utility method which will help us to retrieve the value of a cell regardless of its type.
	 *
	 * @param cell XLS Cell
	 * @return String representation of Excel cell data
	 */
	@SuppressWarnings("deprecation")
	private String getCellValue(HSSFCell cell) {
		if (cell == null) return null;

	    String result = null;

	    int cellType = cell.getCellType();
	    switch (cellType) {
		    case HSSFCell.CELL_TYPE_BLANK:
		    	result = "";
		        break;
		    case HSSFCell.CELL_TYPE_BOOLEAN:
		    	result = cell.getBooleanCellValue() ? "true" : "false";
		        break;
		    case HSSFCell.CELL_TYPE_ERROR:
		        result = "ERROR: " + cell.getErrorCellValue();
		        break;
		    case HSSFCell.CELL_TYPE_FORMULA:
		        result = cell.getCellFormula();
		        break;
		    case HSSFCell.CELL_TYPE_NUMERIC:
		        HSSFCellStyle cellStyle = cell.getCellStyle();
		        short dataFormat = cellStyle.getDataFormat();
	
		        // assumption is made that dataFormat = 15,
		        // when cellType is HSSFCell.CELL_TYPE_NUMERIC is equal to a DATE format.
		        if (dataFormat == 15) {
		        	result = cell.getDateCellValue().toString();
		        } 
		        else {
		        	result = String.valueOf (cell.getNumericCellValue());
		        }	
		        break;
		    case HSSFCell.CELL_TYPE_STRING:
		    	result = cell.getStringCellValue();
		    	break;
		    default: break;
	    }

	    return result;
	}

	/** Drain rows from given source.
	 * 
	 * @return Collection of drained data
	 * @throws IOException if I/O errors occurs during file reading
	 */
	public Collection<String[]> readRowData() throws IOException{
		HSSFWorkbook wb = null;
		InputStream is = null;
	    try {
	    	wb = new HSSFWorkbook (is = new FileInputStream((File)m_dataFile));
	    } 
	    catch (IOException ioe) {
	    	logger.warning("XLS file '" + m_dataFile + "' could not be parsed. Check whether this is not a HTML file which should be converted to valid xls file");
	    	throw ioe;
	    }
	    finally {
	    	if (is != null) try { is.close();} catch (IOException e) {}
	    }

	    Collection<String[]> res = new LinkedList<String[]>();
	    int numOfSheets = wb.getNumberOfSheets();
	    // loop for every work-sheet in the workbook
	    for (int i = 0; i < numOfSheets; i++) {
	    	HSSFSheet sheet = wb.getSheetAt (i);

	    	for (Iterator<?> rows = sheet.rowIterator(); rows.hasNext(); ) {
	    		HSSFRow row = (HSSFRow)rows.next ();
	    		
	    		// loop for every cell in each row
	    		short c1 = row.getFirstCellNum();
	    		short c2 = row.getLastCellNum();
			    String[] rowLine = new String[c2 - c1];
	    		for (short c = c1; c < c2; c++) {
	    			HSSFCell cell = row.getCell(c);
	    			rowLine[c - c1] = (cell != null)? getCellValue (cell) : null;
	    		}
	    		
	    		res.add(rowLine);
	    	}
	    }
	    
	    return res;
	}
}
