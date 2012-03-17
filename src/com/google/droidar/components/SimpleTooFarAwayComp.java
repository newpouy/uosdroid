package com.google.droidar.components;

import com.google.droidar.commands.Command;
import com.google.droidar.commands.ui.CommandShowToast;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.RenderList;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.util.Log;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.MoveComp;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.Updateable;

import android.app.Activity;


public class SimpleTooFarAwayComp extends TooFarAwayComp {

	private static final String LOG_TAG = "SimpleTooFarAwayComp";
	private Shape arrow;
	private MeshComponent plugin;
	private Shape group;
	private MoveComp mover = new MoveComp(3);

	public SimpleTooFarAwayComp(float maxDistance, GLCamera camera,
			final Activity a) {
		super(maxDistance, camera);

		arrow = new Shape(Color.green());
		arrow.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				CommandShowToast.show(a, "Distance: "
						+ (int) arrow.getPosition().getLength() + "m");
				return true;
			}
		});
		arrow.addChild(mover);

		group = new Shape();
		buildGroup();

	}

	@Override
	public void isNowCloseEnough(Updateable parent, MeshComponent parentsMesh,
			Vec direction) {
		if (parent instanceof Obj) {
			((Obj) parent).setComp(plugin);
			plugin = null;
			buildGroup();
		}
	}

	@Override
	public void isNowToFarAway(Updateable parent, MeshComponent parentsMesh,
			Vec direction) {
		Log.d(LOG_TAG, "Is now to far away");
		if (parent instanceof Obj) {
			plugin = parentsMesh;
			buildGroup();
			((Obj) parent).setComp(group);
		}

	}

	private void buildGroup() {
		group.removeAllChildren();
		group.addChild(plugin);
		group.addChild(arrow);
	}

	@Override
	public void onGrayZoneEvent(Updateable parent, MeshComponent parentsMesh,
			Vec direction, float grayZonePercent) {
		arrow.getColor().alpha = grayZonePercent / 100;
	}

	@Override
	public void onFarAwayEvent(Updateable parent, MeshComponent parentsMesh,
			Vec direction) {
		Log.d(LOG_TAG, "far away event");
		Vec lineEndPos = direction.copy().setLength(-10);
		arrow.setMyRenderData(GLFactory.getInstance()
				.newDirectedPath(lineEndPos, null).getMyRenderData());
		Vec pos = direction.setLength(direction.getLength() - 10);
		pos.z -= 5;
		mover.myTargetPos = pos;
	}

}
