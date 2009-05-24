package com.faktel.features;

import java.util.Observable;

import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;
import com.faktel.mvc.View;
import com.faktel.mvc.commands.Command;

public class DummyView extends View {

	public DummyView(String name) {
		super(name);
	}

	@Override
	public boolean displayGrid(Grid grid) {
		return false;
	}

}
