package com.google.droidar.commands.system;

import com.google.droidar.commands.Command;
import com.google.droidar.system.TaskManager;



public class CommandAddHighPrioTask extends Command {

	private Command myCommandToAdd;

	public CommandAddHighPrioTask(Command commandToAdd) {
		myCommandToAdd = commandToAdd;
	}

	@Override
	public boolean execute() {
		TaskManager.getInstance().addHighPrioTask(myCommandToAdd);
		return true;
	}

}
