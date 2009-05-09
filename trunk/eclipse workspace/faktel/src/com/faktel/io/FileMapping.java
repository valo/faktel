package com.faktel.io;

import java.io.File;

/** Data storage container, which contains source and mapping file.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public interface FileMapping {
	
	/** 
	 * Get storage file.
	 * 
	 * @return Storage source
	 */
	public File getSource();
	
	/** Get file mapping.
	 * 
	 * @return File mapping
	 */
	public File getMapping();
}
