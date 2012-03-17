package com.google.droidar.gl.animations;


import javax.microedition.khronos.opengles.GL10;

import com.google.droidar.gl.Renderable;
import com.google.droidar.util.Log;
import com.google.droidar.worldData.UpdateTimer;
import com.google.droidar.worldData.Updateable;
import com.google.droidar.worldData.Visitor;


public class AnimationGrow extends GLAnimation {

	private static final String LOG_TAG = "Grow Animation";
	private float myGrothSize;
	final private float myGrothFactor;
	private UpdateTimer myStopCondition;

	public AnimationGrow(float timeTillFullGrothInSeconds) {
		/*
		 * TODO maybe better to pass the stop condition directly? more flexible?
		 */
		myStopCondition = new UpdateTimer(timeTillFullGrothInSeconds, null);
		myGrothFactor = 1 / timeTillFullGrothInSeconds;
		Log.d(LOG_TAG, "My grow factor is " + myGrothFactor);
	}

	@Override
	public void render(GL10 gl, Renderable parent) {
		gl.glScalef(myGrothSize, myGrothSize, myGrothSize);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent) {
		if (myStopCondition.update(timeDelta, parent)) {
			return false;
		}
		myGrothSize += myGrothFactor * timeDelta;
		if (myGrothSize > 1) {
			myGrothSize = 1;
			Log.e(LOG_TAG,
					"Grouth was > 1, should not happen when grothFactor correct");
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

}
