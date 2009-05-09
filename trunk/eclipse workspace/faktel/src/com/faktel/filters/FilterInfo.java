package com.faktel.filters;

public class FilterInfo {
	public FilterInfo(String name, String className, FilterArgs arguments) {
		super();
		this.name = name;
		this.className = className;
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public FilterArgs getArguments() {
		return arguments;
	}

	String name;
	String className;
	FilterArgs arguments;
	
	public String toString(){
		return name + ":" + className + ", Arguments:" + arguments;
	}
}
