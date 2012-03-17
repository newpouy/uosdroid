package com.google.droidar.commands.system;

import com.google.droidar.commands.undoable.UndoableCommand;
import com.google.droidar.util.Log;



public class TestCommand extends UndoableCommand {

	private int x = 0;

	@Override
	public boolean override_do() {
		x++;
		Log.out("Test Command do. Count: " + x);
		return true;
	}

	@Override
	public boolean override_undo() {
		x--;
		Log.out("Test Command undo. Count: " + x);
		return true;
	}

}
