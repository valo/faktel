package com.faktel;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;


/** Row data factory.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class InvoiceRowFactory {
	private static final Logger logger = Logger.getLogger(InvoiceRowFactory.class.getName());
	private String[] m_fields; 
	
	/** Initialize row data factory
	 * 
	 * @param headers Headers coming from storage file
	 * @param mapping Mapping between storage file data and RowData fields
	 */
	public InvoiceRowFactory(String headers[], Map<String, String> mapping) {
		m_fields = new String[headers.length];
		for (int idx = 0; idx < headers.length; idx++) {
			String header = headers[idx].trim();
			String field = mapping.get(header);
			if (field != null) {
				try {
					InvoiceRow.class.getDeclaredField(field);
				}
				catch (Exception ex) {
					logger.info("Field '" + field + "' does not exists");
					field = null;
				}
			}
			else {
				logger.info("Header '" + header + "' is not mapped");
			}
			
			m_fields[idx] = field;
		}
	}
	
	/** Factory method to create row data.
	 * 
	 * @param row Row from storage file
	 * @return Generated row data
	 */
	public InvoiceRow createRowData(String[] row) {
		InvoiceRow rd = new InvoiceRow(row);
		
		for (int idx = 0; idx < m_fields.length; idx++) {
			String field = m_fields[idx];
			String value = row[idx];
			if (field != null) {
				try {
					Field fld = InvoiceRow.class.getDeclaredField(field);
					if (!fld.isAccessible()) fld.setAccessible(true);
					fld.set(rd, value);
				}
				catch (Exception ex) {
					logger.warning("Cannot set fields '" + field + "' with value: " + value);
				}
			}
		}
		return rd;
	}
}
