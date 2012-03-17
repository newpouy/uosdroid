package com.google.droidar.gl.animations;


import javax.microedition.khronos.opengles.GL10;

import com.google.droidar.gl.Color;
import com.google.droidar.gl.HasColor;
import com.google.droidar.gl.Renderable;
import com.google.droidar.util.Log;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Updateable;
import com.google.droidar.worldData.Visitor;


public class AnimationColorMorph extends GLAnimation {

	private static final float MIN_DISTANCE = 0.001f;
	private float myDurationInMS;
	private Color myTargetColor;

	public AnimationColorMorph(float durationInMS, Color targetColor) {
		myDurationInMS = durationInMS;
		myTargetColor = targetColor;
	}

	@Override
	public void render(GL10 gl, Renderable parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean update(float timeDelta, Updateable parent) {

		if (parent instanceof HasColor) {
			Vec colorDistance = Color.morphToNewColor(
					((HasColor) parent).getColor(), myTargetColor, timeDelta
							/ myDurationInMS);
			if (!(colorDistance.getLength() > MIN_DISTANCE)) {
				Log.d("NodeListener", "color morph finnished for " + parent);
			}
			return (colorDistance.getLength() > MIN_DISTANCE);
		}
		return false;
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}
}
