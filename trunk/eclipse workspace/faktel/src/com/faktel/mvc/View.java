package com.faktel.mvc;

public abstract class View implements java.util.Observer{

	public View(Model model) {
		super();
		this.model = model;
		this.model.addObserver(this);
	}

	private Model model;
}
