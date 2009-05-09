package com.faktel.io;

import java.io.IOException;


/** Write final row data transformed information to specific output. 
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public interface OutputWriter extends FileMapping{
	
	/** Output given data by specific way.
	 * 
	 * @param data Data which will be written out
	 * @return Specific output or null if there is no specific output
	 * @throws IOException if I/O exception occurs
	 */
	public Object doOutput(Object data) throws IOException;
}
