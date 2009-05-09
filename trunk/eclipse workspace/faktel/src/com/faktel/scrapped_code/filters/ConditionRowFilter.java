package com.faktel.scrapped_code.filters;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.faktel.InvoiceRow;
import com.faktel.Utils;
import com.faktel.filters.RowFilter;

/** Execute conditional operations over row data.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public abstract class ConditionRowFilter implements RowFilter{

private static final Logger logger = Logger.getLogger(ConditionRowFilter.class.getName());
	
	private Map<String, Collection<String>> m_fields;
	private static String PATTERN_FORMAT = "^(.*)%s(.*)\\?(.*):(.*)";
	
	private enum OPERATION{
		GREATER,
		GREATER_EQ,
		SMALLER,
		SMALLER_EQ,
		EQUAL,
		NOT_EQUAL,
		UNKNOWN,
	}
	
	public ConditionRowFilter(Map<String, Collection<String>> args) {
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
					Field f = r.getClass().getDeclaredField(fieldName);
					if (!f.isAccessible()) f.setAccessible(true);
					Object res = doOperation(operation, r);
					if (res != null) f.set(r, res);
				}
				catch (Exception e) {
					logger.severe("Cannot set value of the field " + fieldName + ". Exception: " + e);
				}
			}
		}
		
		return refData;
	}
	
	//TODO make me complex :)
	private Object doOperation(String operation, InvoiceRow r) {
		OPERATION operator = OPERATION.UNKNOWN;
		Matcher m = null;
		{
			operator = OPERATION.GREATER_EQ;
			m = Pattern.compile(String.format(PATTERN_FORMAT, ">=")).matcher(operation);
		}
		if (!m.matches()) {
			operator = OPERATION.GREATER;
			m = Pattern.compile(String.format(PATTERN_FORMAT, ">")).matcher(operation);
		}
		if (!m.matches()) {
			operator = OPERATION.SMALLER_EQ;
			m = Pattern.compile(String.format(PATTERN_FORMAT, "<=")).matcher(operation);
		}
		if (!m.matches()) {
			operator = OPERATION.SMALLER;
			m = Pattern.compile(String.format(PATTERN_FORMAT, "<")).matcher(operation);
		}
		if (!m.matches()) {
			operator = OPERATION.NOT_EQUAL;
			m = Pattern.compile(String.format(PATTERN_FORMAT, "!=")).matcher(operation);
		}
		if (!m.matches()) {
			operator = OPERATION.EQUAL;
			m = Pattern.compile(String.format(PATTERN_FORMAT, "==")).matcher(operation);
		}
		
		if (!m.matches()) {
			logger.warning("Invalid operation: " + operation);
			return null;
		}

		String tmp = null;
		
		double val1 = Utils.takeNumberValue(Utils.getFieldValue(tmp = m.group(1), r, tmp)).doubleValue();
		double val2 = Utils.takeNumberValue(Utils.getFieldValue(tmp = m.group(2), r, tmp)).doubleValue();
		boolean bRes = false;
		switch (operator)  {
			case GREATER_EQ: 
				bRes = greater(val1, val2) || equal(val1, val2);
				break;
			case GREATER:
				bRes = greater(val1, val2);
				break;
			case SMALLER_EQ: 
				bRes = smaller(val1, val2) || equal(val1, val2);
				break;
			case SMALLER: 
				bRes = smaller(val1, val2);
				break;
			case EQUAL:
				bRes = equal(val1, val2);
				break;
			case NOT_EQUAL: 
				bRes = !equal(val1, val2);
				break;
			default:
				return null;
		}
		
		return OperationRowFilter.doOperation(bRes? m.group(3) : m.group(4), r);
	}
	
	private static double EPS = 0.0000000001;
	private static boolean greater(double val1, double val2) {
		return val1 - EPS > val2;
	}
	private static boolean smaller(double val1, double val2) {
		return greater(val2, val1);
	}
	
	private static boolean equal(double val1, double val2) {
		return val1 == val2 || (val1 + EPS > val2 && val1 - EPS < val2);
	}
}
