package com.faktel.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

@SuppressWarnings("serial")
public class NotImplementedYetException extends FaktelThrowable {

	public static void warnAboutMe(java.util.logging.Logger logger) {
		try {
			throw new NotImplementedYetException();
		} catch (NotImplementedYetException e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			//e.printStackTrace(pw);
			synchronized (pw) {
				StackTraceElement[] trace = e.getStackTrace();
				pw.println(trace[2]);
			}
			String method = sw.toString();
			logger.log(Level.WARNING, "Method not implemented! Please implement " + method);
		}
	}
}
