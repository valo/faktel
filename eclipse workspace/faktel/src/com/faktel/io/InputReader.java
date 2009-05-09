package com.faktel.io;

import java.io.IOException;
import java.util.Collection;

/** Reader which transform given input to row data. 
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public interface InputReader extends FileMapping{
	
	/** Drain rows from given source.
	 * 
	 * @return Collection of drained data
	 * @throws IOException if I/O errors occurs during file reading
	 */
	public Collection<String[]> readRowData() throws IOException;
}
