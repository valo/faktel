package com.faktel.features;

import com.faktel.mvc.Grid;
import com.faktel.mvc.View;

public class DummyView extends View {
	private static final long serialVersionUID = 569797224106190757L;

	public DummyView(String name) {
		super(name);
	}

	@Override
	public boolean displayGrid(Grid grid) {
		return false;
	}

}
