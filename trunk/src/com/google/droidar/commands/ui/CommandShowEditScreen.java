package com.google.droidar.commands.ui;

import com.google.droidar.commands.Command;
import com.google.droidar.gui.simpleUI.EditItem;
import com.google.droidar.gui.simpleUI.SimpleUI;

import android.app.Activity;


public class CommandShowEditScreen extends Command {

	private Activity myCurrentActivity;
	private EditItem myObjectToEdit;
	private Object myOptionalMessage;

	public CommandShowEditScreen(Activity currentActivity, EditItem objectToEdit) {
		myCurrentActivity = currentActivity;
		myObjectToEdit = objectToEdit;
	}

	public CommandShowEditScreen(Activity currentActivity,
			EditItem objectToEdit, Object optionalMessage) {
		myCurrentActivity = currentActivity;
		myObjectToEdit = objectToEdit;
		myOptionalMessage = optionalMessage;
	}

	@Override
	public boolean execute() {
		SimpleUI.showEditScreen(myCurrentActivity, myObjectToEdit,
				myOptionalMessage);
		return true;
	}
}
