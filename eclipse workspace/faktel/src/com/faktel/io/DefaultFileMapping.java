package com.faktel.io;

import java.io.File;

/** Default implementation of Data storage.
 * 
 * @author <a href="mailto:ivailo.radev@gmail.com">Ivaylo Radev</a>
 * @version 1.0
 */
public class DefaultFileMapping implements FileMapping {

	protected File m_dataFile;
	protected File m_mappingFile;
	
	/** 
	 * Create data storage.
	 */
	protected DefaultFileMapping() {
		
	}
	
	/** 
	 * Create data storage.
	 */
	public DefaultFileMapping(File src, File mappingFile) {
		setDataSource(src);
		setMappingFile(mappingFile);
	}
	
	/** 
	 * Get storage file.
	 * 
	 * @return Storage source
	 */
	public File getSource() {
		return m_dataFile;
	}
	
	/** 
	 * Set storage file.
	 */
	protected void setDataSource(File src) {
		m_dataFile = src;
	}
	
	/** Get file mapping.
	 * 
	 * @return File mapping
	 */
	public File getMapping() {
		return m_mappingFile;
	}
	
	/** 
	 * Set file mapping.
	 */
	protected void setMappingFile(File mapping) {
		m_mappingFile = mapping;
	}
}
