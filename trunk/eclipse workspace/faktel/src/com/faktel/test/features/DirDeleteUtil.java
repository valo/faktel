package com.faktel.test.features;

import java.io.File;

/**
 * Contains a method for deleting a directories.
 * 
 * @author mitex
 */
public class DirDeleteUtil {
	
	/**
	 * Deletes the specified directory. The directory can be non-empty.
	 * @param path The directory to be deleted.
	 * @return True, if and only if the operation is successful.
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
}
