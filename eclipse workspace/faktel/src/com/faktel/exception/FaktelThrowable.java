package com.faktel.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author rumen Indicate this method is not yet implemented
 */
@SuppressWarnings("serial")
public class FaktelThrowable extends Throwable {

	public String getStackTraceAsString() {
		return getStackTraceAsString(this);
	}

	public static String getStackTraceAsString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String res = sw.toString();
		return res;
	}

}
