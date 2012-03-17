package com.google.droidar.de.rwth.setups;


import java.util.HashMap;

import com.google.droidar.commands.Command;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.GLText;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.DefaultARSetup;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.World;

import android.app.Activity;


public class FastChangingTextSetup extends DefaultARSetup {

	HashMap<String, MeshComponent> textMap = new HashMap<String, MeshComponent>();
	private GLText text;

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {

		text = new GLText("11223344swrvgweln@@@@", myTargetActivity, textMap,
				getCamera());

		Obj o = new Obj();
		o.setComp(text);
		world.add(o);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addSearchbarToView(guiSetup.getBottomView(), new Command() {

			@Override
			public boolean execute() {
				return false;
			}

			@Override
			public boolean execute(Object transfairObject) {
				if (transfairObject instanceof String) {
					String s = (String) transfairObject;
					if (text != null)
						text.changeTextTo(s);
				}
				return true;
			}
		}, "Enter text");
	}

}
