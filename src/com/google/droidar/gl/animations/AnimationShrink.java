package com.google.droidar.gl.animations;


import javax.microedition.khronos.opengles.GL10;

import com.google.droidar.gl.Renderable;
import com.google.droidar.util.Log;
import com.google.droidar.worldData.Updateable;
import com.google.droidar.worldData.Visitor;


public class AnimationShrink extends GLAnimation {

	private static final String LOG_TAG = "Grow Animation";
	private float myGrothSize = 1;
	final private float myShrinkFactor;

	public AnimationShrink(float timeTillFullGrothInSeconds) {
		myShrinkFactor = 1 / timeTillFullGrothInSeconds;
		Log.d(LOG_TAG, "My shrink factor is " + myShrinkFactor);
	}

	@Override
	public void render(GL10 gl, Renderable parent) {
		gl.glScalef(myGrothSize, myGrothSize, myGrothSize);
	}

	@Override
	public boolean update(float timeDelta, Updateable parent) {
		if (myGrothSize > 0) {
			myGrothSize -= myShrinkFactor * timeDelta;
		} else {
			myGrothSize = 0;
		}
		return true;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

}
