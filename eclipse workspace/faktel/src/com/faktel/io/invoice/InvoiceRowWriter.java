package com.faktel.io.invoice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.config.MappingParser;
import com.faktel.io.OutputWriter;

/** Row data writer, which wraps output writer class.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class InvoiceRowWriter {
	private static final Logger logger = Logger.getLogger(InvoiceRowWriter.class.getName());
	
	private OutputWriter m_writer;
	
	/** Create row data reader, as input reader will be wrapped.
	 * 
	 * @param ir Input reader
	 */
	public InvoiceRowWriter(OutputWriter wr) {
		m_writer = wr;
	}
	
	/** Write all row data 
	 * 
	 * @param data Data which will be output
	 * @return Drained row data
	 * @throws IOException if I/O errors occurs
	 */
	public Object writeAllRowData(Collection<InvoiceRow> data) throws IOException {
		Map<String, String> mapping = MappingParser.readMapping(m_writer.getMapping());
		
		Collection<String[]> rows = new LinkedList<String[]>();
		Collection<String> headers = new LinkedList<String>();
		Collection<Field> fields = new LinkedList<Field>();
		for (Map.Entry<String, String> entry : mapping.entrySet()) {
			try {
				fields.add(Utils.getRowDataField(entry.getKey()));
				headers.add(entry.getValue());	
			}
			catch (Exception e) {
				logger.warning("Field " + entry.getKey() + " cannot be found");
			}
		}
		//write headers
		rows.add(headers.toArray(new String[0]));
		
		for (InvoiceRow row : data) {
			String[] str = new String[fields.size()];
			int idx = 0;
			for (Field fld : fields) {
				try {
					Object o = fld.get(row);
					str[idx++] = o == null? "" : o.toString();
				} 
				catch (Exception e) {
					e.printStackTrace();
					str[idx++] = "";
				} 
			}
			rows.add(str);
		}
		m_writer.doOutput(rows);
		
		return rows;
	}
}
