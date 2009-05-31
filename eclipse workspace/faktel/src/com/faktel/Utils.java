package com.faktel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import au.com.bytecode.opencsv.CSVWriter;

import com.faktel.filters.FilterArgs;
import com.faktel.filters.FilterInfo;
import com.faktel.filters.RowFilter;
import com.faktel.io.excel.ExcelOutputWriter;
import com.faktel.mvc.Grid;

/**
 * @author rumen
 * 
 */
public class Utils {
	private static final Logger logger = Logger
			.getLogger(Utils.class.getName());

	/**
	 * Take first argument in the collection.
	 * 
	 * @param col
	 *            collection of Strings
	 * @return First member
	 */
	public static String takeFirstArg(Collection<String> col) {
		if (col != null)
			for (String o : col)
				return o;
		return null;
	}

	/**
	 * Get field value.
	 * 
	 * @param name
	 *            Field name
	 * @param o
	 *            Object
	 * @param defValue
	 *            Default value
	 * @return
	 */
	public static Object getFieldValue(String name, Object o, Object defValue) {
		try {
			Field f = o.getClass().getDeclaredField(name);
			if (!f.isAccessible())
				f.setAccessible(true);
			return f.get(o);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * Threat object as number
	 * 
	 * @param o
	 *            Object
	 * @return Number
	 */
	public static Number takeNumberValue(Object o) {
		Number res = null;
		if (o instanceof Integer || o instanceof Float || o instanceof Double) {
			res = (Number) o;
		} else if (o != null) {
			// treat as string
			String val = o.toString();
			try {
				try {
					res = Integer.parseInt(val);
				} catch (NumberFormatException nfe) {
					try {
						res = Float.parseFloat(val);
					} catch (NumberFormatException nfe2) {
						res = Double.parseDouble(val.replace(",", "."));
					}
				}
			} catch (Exception e) {
				logger.warning("Cannot take number value of " + o);
			}
		}

		return res;
	}

	/**
	 * Get row data field by name
	 * 
	 * @param name
	 *            Field name
	 * @return Row data field
	 * @throws Exception
	 *             if field cannot be found
	 */
	public static Field getRowDataField(String name) throws Exception {
		Field f = InvoiceRow.class.getDeclaredField(name);
		if (!f.isAccessible())
			f.setAccessible(true);
		return f;
	}

	public static Collection<Field> precomputeFields(
			Collection<String> fieldNames, Class<?> cl) {
		Collection<Field> res = new LinkedList<Field>();
		for (String name : fieldNames) {
			try {
				Field fld = cl.getDeclaredField(name);
				if (!fld.isAccessible())
					fld.setAccessible(true);
				res.add(fld);
			} catch (Exception e) {
				logger.severe(String.format("Field %s.%s doesn't exists", cl
						.getName(), name));
			}
		}

		return res;
	}

	public static final String getRootPath() {
		String rootPath = null;
		/*
		 * //requires apache commons try { rootPath =
		 * URLDecoder.decode(FileUtils.class.getProtectionDomain
		 * ().getCodeSource().getLocation().getPath(), "UTF-8");
		 * 
		 * String osName = System.getProperty("os.name");
		 * 
		 * if (osName.toUpperCase().contains("WINDOWS")) { rootPath =
		 * rootPath.substring(1); }
		 * 
		 * rootPath = new File(rootPath).getParent();
		 * 
		 * } catch (UnsupportedEncodingException e) { e.printStackTrace();
		 * 
		 * // If an error occurred, default to current directory. rootPath =
		 * System.getProperty("user.dir"); }
		 * 
		 * Globals.getLogger().info(MessageFormat.format("getRootPath: {0}",
		 * rootPath));
		 */
		return rootPath;
	}

	public static final String getAppPath() {
		return System.getProperty("user.dir");
	}

	@SuppressWarnings("deprecation")
	public static final String getCurrentDateAsFileName() {
		Calendar calendar = new GregorianCalendar().getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		
		String res = String.format("%04d-%02d-%02d_%02d-%02d-%02d", year,
				month, day, hours, minutes, seconds);
		return res;
	}

	public static final File createUniqueFolder(String name, File rootFolder) {

		File res = new File(rootFolder.getAbsolutePath() + File.separator
				+ name);
		rootFolder.mkdirs();
		if (!res.mkdirs()) {
			int attempts = 100;
			while ((0 < attempts--) && (!res.isDirectory())) {
				try {
					res = File.createTempFile(name, "", rootFolder);
					res.delete();
					res.mkdir();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	public static final File createFolderForCurrentRun() {
		File res = null;
		String timestamp = getCurrentDateAsFileName();
		String curdir = getAppPath();
		res = createUniqueFolder(timestamp, new File(curdir + File.separator
				+ "results"));
		return res;
	}

	public static final File createFolderForFilter(String filterName,
			File rootFolder) {
		File res = null;
		String name = escapeToFileName(filterName);
		res = createUniqueFolder(name, rootFolder);
		return res;
	}

	/**
	 * Convert a given String to a valid file name.
	 * 
	 * @param filterName
	 * @return String similar to filterName, but changed in a way that it can be
	 *         a file name.
	 */
	@SuppressWarnings("deprecation")
	private static final String escapeToFileName(String filterName) {
		String res = null;
		res = URLEncoder.encode(filterName);
		res = res.replace("?", "_");
		return res;
	}

	public static void outputFilterResultToCsv(Grid resultData,
			String outputName) {
		File csv = new File(outputName);
		logger.info("Writing  '" + resultData.getClass() + "' to "
				+ csv.getAbsolutePath());
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(csv));
			writer.writeAll(resultData.toListOfStringArrays());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.warning("Failed writing  '" + resultData.getClass()
					+ "' to " + csv.getAbsolutePath());
			logger.throwing(Utils.class.getName(), "...", e);
		}
	}

	@SuppressWarnings("deprecation")
	public static void outputFilterResultToXls(Grid grid, String fileName,
			String sheetName) {
		File xls = new File(fileName);
		logger.info("Writing  '" + grid.getClass() + "' to "
				+ xls.getAbsolutePath());
		if (grid instanceof Collection) {

			// open workbook if exists
			HSSFWorkbook wb;
			FileInputStream fileIn = null;
			try {
				fileIn = new FileInputStream(new File(fileName));
				wb = new HSSFWorkbook(fileIn);
			} catch (IOException e) {
				wb = new HSSFWorkbook();
			} finally {
				try {
					if (null != fileIn) {
						fileIn.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// create new sheet and fill it in with data
			HSSFSheet sheet = wb.createSheet(sheetName);
			sheet.setDefaultColumnWidth((short) 50);
			int rowIdx = 0;
			for (String[] dataRow : (Collection<String[]>) grid
					.toListOfStringArrays()) {
				HSSFRow row = sheet.createRow(rowIdx++);
				for (int i = 0; i < dataRow.length; i++) {
					HSSFCell cell = row.createCell((short) i);
					cell.setCellValue(dataRow[i]);
				}
			}

			// logger.info("Writing '" + grid.getClass() + "' to " + fileName);

			// write new workbook
			FileOutputStream fileOut = null;
			try {
				fileOut = new FileOutputStream(new File(fileName));
				wb.write(fileOut);
				fileOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (grid != null) {
			logger.warning(ExcelOutputWriter.class
					+ " doesn't not supports class " + grid.getClass());
		}
	}

	/**
	 * Add new row filter.
	 * 
	 * @param filterName
	 *            Filter name
	 * @param className
	 *            Filter class name
	 * @param args
	 *            Filter arguments
	 * @throws Exception
	 */
	public static final RowFilter createFilter(FilterInfo info)
			throws Exception {
		RowFilter res = null;
		try {
			Class<?> cl = Class.forName(info.getClassName());
			Constructor<?> constructor = cl
					.getDeclaredConstructor(FilterArgs.class);
			res = (RowFilter) constructor.newInstance(info.getArguments());
		} catch (Exception e) {
			logger.warning("Failed to create filter[" + info.getName()
					+ "]. Exception: " + e);
			throw e;
		}
		return res;
	}

	public static final void shellExecute(String cmd) {
		Runtime rt = Runtime.getRuntime();
		Process ps;
		try {
			ps = rt.exec(cmd);
			/*
			 * OutputStream os = ps.getOutputStream(); InputStream is =
			 * ps.getInputStream(); DataOutputStream dos = new
			 * DataOutputStream(os); dos.writeBytes("i");
			 * dos.writeBytes("test"); dos.writeBytes("u001B");
			 * dos.writeBytes(":wq"); dos.flush();
			 */
			ps.waitFor();
			// dos.close();
			System.out.println(ps.exitValue());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
