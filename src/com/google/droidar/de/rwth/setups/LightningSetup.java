package com.google.droidar.de.rwth.setups;


import javax.microedition.khronos.opengles.GL10;

import com.google.droidar.actions.ActionMoveObject;
import com.google.droidar.commands.Command;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.LightSource;
import com.google.droidar.gl.animations.AnimationRotate;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.DefaultARSetup;
import com.google.droidar.system.EventManager;
import com.google.droidar.util.EfficientList;
import com.google.droidar.util.Vec;
import com.google.droidar.util.Wrapper;
import com.google.droidar.worldData.Entity;
import com.google.droidar.worldData.MoveComp;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.app.Activity;
import android.util.Log;


public class LightningSetup extends DefaultARSetup {

	protected static final String LOG_TAG = "LightningSetup";

	private static float zMoveFactor = 1f;

	private Wrapper targetMoveWrapper;

	private LightSource spotLight;

	private Obj lightObject;

	public LightningSetup() {
		super();
		lightObject = new Obj();
		targetMoveWrapper = new Wrapper(lightObject);
	}

	@Override
	public boolean _a2_initLightning(EfficientList<LightSource> lights) {
		lights.add(LightSource.newDefaultAmbientLight(GL10.GL_LIGHT0));
		spotLight = LightSource.newDefaultDefuseLight(GL10.GL_LIGHT1, new Vec(
				0, 0, 0));
		lights.add(spotLight);
		return true;
	}

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {

		addNewObjToWorld(world, objectFactory);
		MeshComponent innerGroup = new Shape();
		innerGroup.addChild(spotLight);
		innerGroup.addChild(objectFactory.newCircle(Color.red()));
		innerGroup.setPosition(new Vec(0, 3, 0));

		MeshComponent outerGroup = new Shape();
		outerGroup.addAnimation(new AnimationRotate(30, new Vec(0, 0, 1)));
		outerGroup.addChild(innerGroup);
		outerGroup.addChild(objectFactory.newCircle(Color.blue()));

		spotLight.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				targetMoveWrapper.setTo(lightObject);
				return true;
			}
		});

		lightObject.setComp(outerGroup);
		lightObject.setComp(new MoveComp(1));
		world.add(lightObject);

	}

	private void addNewObjToWorld(World world, GLFactory objectFactory) {
		final Obj o = new Obj();

		MeshComponent mesh = objectFactory.newCube();
		// mesh = newCube();
		mesh = objectFactory.newDiamond(Color.red());
		mesh.setScale(new Vec(2, 3, 1));
		mesh.addChild(new AnimationRotate(30, new Vec(0, 0, -1)));

		o.setComp(mesh);
		o.setOnClickCommand(new Command() {

			@Override
			public boolean execute() {
				targetMoveWrapper.setTo(o);
				return true;
			}
		});
		o.setComp(new MoveComp(1));
		world.add(o);

		world.add(o);
	}

	private Entity newCube() {
		Shape s = new Shape();
		s.add(new Vec());
		s.add(new Vec(2, 2, 0));
		s.add(new Vec(2, 4, 0));

		s.add(new Vec());
		s.add(new Vec(2, 4, 0));
		s.add(new Vec(2, 10, 0));

		return s;
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		super._c_addActionsToEvents(eventManager, arView, updater);

		// clear some inputs set in default methods
		eventManager.onLocationChangedAction = null;
		eventManager.onTrackballEventAction = null;

		eventManager.addOnTrackballAction(new ActionMoveObject(
				targetMoveWrapper, getCamera(), 10, 100));
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (targetMoveWrapper.getObject() instanceof Obj) {
					MoveComp mover = ((Obj) targetMoveWrapper.getObject())
							.getComp(MoveComp.class);
					if (mover != null)
						mover.myTargetPos.z -= zMoveFactor;
					else {
						Vec pos = ((Obj) targetMoveWrapper.getObject())
								.getPosition();
						if (pos != null)
							pos.z -= zMoveFactor;
						else
							Log.e(LOG_TAG, "Cant move object, has no position!");
					}
					return true;
				}
				return false;
			}
		}, "Obj Down");
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				if (targetMoveWrapper.getObject() instanceof Obj) {
					MoveComp mover = ((Obj) targetMoveWrapper.getObject())
							.getComp(MoveComp.class);
					if (mover != null)
						mover.myTargetPos.z += zMoveFactor;
					else {
						Vec pos = ((Obj) targetMoveWrapper.getObject())
								.getPosition();
						if (pos != null)
							pos.z += zMoveFactor;
						else
							Log.e(LOG_TAG, "Cant move object, has no position!");
					}
					return true;
				}
				return false;
			}
		}, "Obj up");
	}

}
