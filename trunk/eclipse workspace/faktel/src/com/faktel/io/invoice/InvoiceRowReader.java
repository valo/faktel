package com.faktel.io.invoice;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.faktel.InvoiceRow;
import com.faktel.InvoiceRowFactory;
import com.faktel.InvoiceRows;
import com.faktel.config.MappingParser;
import com.faktel.io.InputReader;

/** Row data reader, which wraps input reader class.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class InvoiceRowReader {
	private InputReader m_reader;
	
	/** Create row data reader, as input reader will be wrapped.
	 * 
	 * @param ir Input reader
	 */
	public InvoiceRowReader(InputReader ir) {
		m_reader = ir;
	}
	
	/** Drain all row data taken from input reader. 
	 * 
	 * @return Drained row data
	 * @throws IOException if I/O error occurs
	 */
	public InvoiceRows readAllRowData() throws IOException {
		InvoiceRowFactory factory = null;
		
		Collection<String[]> lines = m_reader.readRowData();
		InvoiceRows rows = new InvoiceRows();
		if (lines.size() > 1) {
			for (String[] row : lines) {
				//first row are headers
				if (factory == null) {
					Map<String, String> mapping = MappingParser.readMapping(m_reader.getMapping());
					MappingParser.checkIfMappingForAllFieldsExists(row, mapping);
					factory = new InvoiceRowFactory(row, mapping);
				}
				else {
					InvoiceRow ir =factory.createRowData(row); 
					rows.add(ir);	
				}	
			}
		}
		
		return rows;
	}
	
	/** Get storage file.
	 * 
	 * @return Storage source
	 */
	public Object getStorageSource() {
		return m_reader.getSource();
	}
}
