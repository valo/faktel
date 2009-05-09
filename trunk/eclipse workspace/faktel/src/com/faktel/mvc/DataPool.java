package com.faktel.mvc;

import java.util.TreeMap;


/**
 * 
 * @author rumen 
 * Data pool.
 * Used to hold modifiable data passed through the filters. 
 */

public class DataPool {
	private static final long serialVersionUID = 5914987472217800083L;
	private TreeMap<String, Object> objects = new TreeMap<String, Object>();
//	private TreeMap<String, String> strings = new TreeMap<String, String>();
//	private TreeMap<String, Integer> ints = new TreeMap<String, Integer>();
	

	/**
	 * Avoid use :)
	 */
	@Deprecated
	public TreeMap<String, Object> getObjectMap() {
		return objects;
	}
	
	public boolean exists(String key){
		return objects.containsKey(key);
	}
	
	public Object get(String key){
		return objects.get(key);
	}
	
	public String getString(String key){
		return (String)objects.get(key);
	}
	
	public int getInt(String key){
		Object res = objects.get(key);
		return ((Integer)res).intValue();
	}
	
	public Object put(String key, Object value){
		Object res = objects.put(key, value);
		return res;
	}

	public Object remove(String key){
		Object res = objects.remove(key);
		return res;
	}

	
}
