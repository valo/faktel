package com.faktel.features;

import java.util.Observable;

import com.faktel.mvc.Model;
import com.faktel.mvc.View;
import com.faktel.mvc.commands.Command;

public class DummyView extends View {

	public DummyView(Model model) {
		super(model);
	}

	public void update(Observable o, Object arg) {
		System.out.println(o + " executing " + arg);
		assert(null != o);
		assert(null != arg);
		assert(arg instanceof Command);
		assert(o instanceof Model);
		assert(arg instanceof Command);
	}

}
