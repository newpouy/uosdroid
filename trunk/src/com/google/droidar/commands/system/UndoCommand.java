package com.google.droidar.commands.system;

import com.google.droidar.commands.Command;
import com.google.droidar.commands.undoable.CommandProcessor;


public class UndoCommand extends Command {

	@Override
	public boolean execute() {
		return CommandProcessor.getInstance().undo();
	}

}
