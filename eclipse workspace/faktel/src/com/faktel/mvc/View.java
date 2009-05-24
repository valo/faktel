package com.faktel.mvc;

import java.util.Observable;

import javax.swing.JComponent;

/*
 * An abstract class, which defines a container displaying the result from a computation
 * to the user 
 */
public abstract class View extends JComponent implements java.util.Observer {

	private static final long serialVersionUID = -7257998569769294379L;
	
	private String m_name;

	public View(String name) {
		super();
		m_name = name;
	}
	
	public String getName() {
		return m_name;
	}
	
	public abstract boolean displayGrid(Grid grid);
	
	public void bindToModel(Model model) {
		model.addObserver(this);
	}
	
	public void unbindToModel(Model model) {
		model.deleteObserver(this);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Model) {
			displayGrid(((Model)o).gridGetCopy());
		}
	}
}
