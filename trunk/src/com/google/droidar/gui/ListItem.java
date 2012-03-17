package com.google.droidar.gui;

import com.google.droidar.commands.Command;
import com.google.droidar.system.Container;

import android.view.View;
import android.view.ViewGroup;


/**
 * Every object which has to be displayed in a {@link CustomListActivity} has to
 * implement this interface. Also see {@link Container}.
 * 
 * @author Spobo
 * 
 */
public interface ListItem {

	/**
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	View getMyListItemView(View viewToUseIfNotNull, ViewGroup parentView);

	/**
	 * @return normally this should return the default onClick command if the
	 *         class already has one
	 */
	Command getListClickCommand();

	Command getListLongClickCommand();

}
