package com.faktel.scrapped_code.filters;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.filters.RowFilter;

/** Execute mathematical operations over row data.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public abstract class OperationRowFilter implements RowFilter{

	private static final Logger logger = Logger.getLogger(AggregateRowFilter.class.getName());
	
	private Map<String, Collection<String>> m_fields;
	
	private enum OPERATION{
		PLUS,
		MINUS,
		MUL,
		DIV,
		WHAT_PERCENT,
		PERCENT,
		EQUAL
	}
	
	public OperationRowFilter(Map<String, Collection<String>> args) {
		m_fields = args;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(Collection<InvoiceRow> orginalData, File workingDir, Object referenced, Object specificParams) {
		if (!(referenced instanceof Collection)) return null;
		Collection<InvoiceRow> refData = (Collection<InvoiceRow>) referenced;
		
		for (InvoiceRow r : refData) {
			for (Map.Entry<String, Collection<String>> entry : m_fields.entrySet()) {
				String fieldName = entry.getKey();
				String operation = (String)Utils.takeFirstArg(entry.getValue());
				try {
					Object res = doOperation(operation, r);
					Field f = r.getClass().getDeclaredField(fieldName);
					if (!f.isAccessible()) f.setAccessible(true);
					f.set(r, res);
				}
				catch (Exception e) {
					logger.severe("Cannot set value of the field " + fieldName + ". Exception: " + e);
				}
			}
		}
		
		return refData;
	}
	
	//TODO make me complex :)
	public static Object doOperation(String operation, InvoiceRow r) {
		OPERATION operator = OPERATION.EQUAL;
		int idx = -1;
		if (idx == -1) {
			operator = OPERATION.PLUS;
			idx = operation.indexOf("+");
		}
		if (idx == -1) {
			operator = OPERATION.MINUS;
			idx = operation.indexOf("-");
		}
		if (idx == -1) {
			operator = OPERATION.MUL;
			idx = operation.indexOf("*");
		}
		if (idx == -1) {
			operator = OPERATION.DIV;
			idx = operation.indexOf("/");
		}
		if (idx == -1) {
			operator = OPERATION.WHAT_PERCENT;
			idx = operation.indexOf("?%");
		}
		if (idx == -1) {
			operator = OPERATION.PERCENT;
			idx = operation.indexOf("%");
		}
		if (idx == -1 ) {
			operator = OPERATION.EQUAL;
		}
		
		Object firstArg = null;
		Object secArg   = null;
		switch (operator)  {
			case PLUS: 
				firstArg = Utils.getFieldValue((String)(firstArg = operation.substring(0, idx).trim()), r, firstArg);
				secArg   = Utils.getFieldValue((String)(secArg   = operation.substring(idx + 1).trim()), r, secArg);
				return Utils.takeNumberValue(firstArg).doubleValue() + Utils.takeNumberValue(secArg).doubleValue();
			case MINUS:
				firstArg = Utils.getFieldValue((String)(firstArg = operation.substring(0, idx).trim()), r, firstArg);
				secArg   = Utils.getFieldValue((String)(secArg   = operation.substring(idx + 1).trim()), r, secArg);
				return Utils.takeNumberValue(firstArg).doubleValue() - Utils.takeNumberValue(secArg).doubleValue();
			case MUL: 
				firstArg = Utils.getFieldValue((String)(firstArg = operation.substring(0, idx).trim()), r, firstArg);
				secArg   = Utils.getFieldValue((String)(secArg   = operation.substring(idx + 1).trim()), r, secArg);
				return Utils.takeNumberValue(firstArg).doubleValue() * Utils.takeNumberValue(secArg).doubleValue();
			case DIV: 
				firstArg = Utils.getFieldValue((String)(firstArg = operation.substring(0, idx).trim()), r, firstArg);
				secArg   = Utils.getFieldValue((String)(secArg   = operation.substring(idx + 1).trim()), r, secArg);
				return Utils.takeNumberValue(firstArg).doubleValue() / Utils.takeNumberValue(secArg).doubleValue();
			case WHAT_PERCENT:
				firstArg = Utils.getFieldValue((String)(firstArg = operation.substring(0, idx).trim()), r, firstArg);
				secArg   = Utils.getFieldValue((String)(secArg   = operation.substring(idx + 1).trim()), r, secArg);
				return Utils.takeNumberValue(firstArg).doubleValue() * 100 / Utils.takeNumberValue(secArg).doubleValue();
			case PERCENT: 
				firstArg = Utils.getFieldValue((String)(firstArg = operation.substring(0, idx).trim()), r, firstArg);
				secArg   = Utils.getFieldValue((String)(secArg   = operation.substring(idx + 1).trim()), r, secArg);
				return Utils.takeNumberValue(firstArg).doubleValue() * Utils.takeNumberValue(secArg).doubleValue() / 100;
			default:
				return Utils.getFieldValue(operation.trim(), r, operation);
		}
	}		
}
