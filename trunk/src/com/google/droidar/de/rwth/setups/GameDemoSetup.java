package com.google.droidar.de.rwth.setups;

import com.google.droidar.gamelogic.ActionThrowFireball;
import com.google.droidar.gamelogic.GameParticipant;
import com.google.droidar.gamelogic.Stat;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.DefaultARSetup;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.app.Activity;
import br.unb.unbiquitous.activity.R;

public class GameDemoSetup extends DefaultARSetup {
	private GameParticipant p;
	private ActionThrowFireball e;

	public GameDemoSetup() {
		p = new GameParticipant("Player", "Karlo", R.drawable.hippopotamus64);
		p.addStat(new Stat(Stat.INTELLIGENCE, R.drawable.icon, 2));
		e = new ActionThrowFireball("Fireball");
		p.addAction(e);
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		super._d_addElementsToUpdateThread(updater);
		updater.addObjectToUpdateCycle(p);
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addViewToTop(e.getNewDefaultView(getActivity()));
	}
}
