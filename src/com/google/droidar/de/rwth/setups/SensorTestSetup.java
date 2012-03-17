package com.google.droidar.de.rwth.setups;

import com.google.droidar.actions.Action;
import com.google.droidar.actions.ActionBufferedCameraAR;
import com.google.droidar.actions.ActionMoveCameraBuffered;
import com.google.droidar.actions.ActionRotateCameraBuffered;
import com.google.droidar.actions.ActionRotateCameraBuffered3;
import com.google.droidar.actions.ActionRotateCameraBuffered4;
import com.google.droidar.actions.ActionRotateCameraBufferedDebug;
import com.google.droidar.actions.ActionRotateCameraBufferedDirect;
import com.google.droidar.actions.ActionRotateCameraUnbuffered;
import com.google.droidar.actions.ActionRotateCameraUnbuffered2;
import com.google.droidar.commands.Command;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.animations.AnimationRotate;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.ErrorHandler;
import com.google.droidar.system.EventManager;
import com.google.droidar.system.Setup;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.app.Activity;


public class SensorTestSetup extends Setup {

	private GLCamera camera;
	private World world;
	private Action rotActionB1;
	private Action rotActionB3;
	private Action rotActionB4;
	private Action rotActionDebug;
	private Action rotActionUnB;
	private Action rotActionUnB2;
	private Action rotActionB2;

	@Override
	public void _a_initFieldsIfNecessary() {
		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");

		/*
		 * the following are just example rotate actions, take a look at the
		 * implementation to see how to create own CameraBuffered actions
		 */

		camera = new GLCamera();
		rotActionB1 = new ActionRotateCameraBuffered(camera);
		rotActionB2 = new ActionRotateCameraBufferedDirect(camera);
		rotActionB3 = new ActionRotateCameraBuffered3(camera);
		rotActionB4 = new ActionRotateCameraBuffered4(camera);
		rotActionDebug = new ActionRotateCameraBufferedDebug(camera);
		rotActionUnB = new ActionRotateCameraUnbuffered(camera);
		rotActionUnB2 = new ActionRotateCameraUnbuffered2(camera);

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		world = new World(camera);

		MeshComponent compasrose = new Shape();

		MeshComponent middle = objectFactory.newDiamond(Color.green());
		middle.setPosition(new Vec(0, 0, -2.8f));
		middle.addChild(new AnimationRotate(40, new Vec(0, 0, 1)));
		compasrose.addChild(middle);

		int smallDistance = 10;
		int longDistance = 60;

		MeshComponent north = objectFactory.newDiamond(Color.redTransparent());
		north.setPosition(new Vec(0, smallDistance, 0));

		MeshComponent north2 = objectFactory.newDiamond(Color.red());
		north2.setPosition(new Vec(0, longDistance, 0));

		MeshComponent east = objectFactory.newDiamond(Color.blueTransparent());
		east.setPosition(new Vec(smallDistance, 0, 0));

		MeshComponent east2 = objectFactory.newDiamond(Color.blue());
		east2.setPosition(new Vec(longDistance, 0, 0));

		MeshComponent south = objectFactory.newDiamond(Color.blueTransparent());
		south.setPosition(new Vec(0, -smallDistance, 0));

		MeshComponent south2 = objectFactory.newDiamond(Color.blue());
		south2.setPosition(new Vec(0, -longDistance, 0));

		MeshComponent west = objectFactory.newDiamond(Color.blueTransparent());
		west.setPosition(new Vec(-smallDistance, 0, 0));

		MeshComponent west2 = objectFactory.newDiamond(Color.blue());
		west2.setPosition(new Vec(-longDistance, 0, 0));

		compasrose.addChild(north2);
		compasrose.addChild(north);
		compasrose.addChild(east2);
		compasrose.addChild(east);
		compasrose.addChild(south2);
		compasrose.addChild(south);
		compasrose.addChild(west2);
		compasrose.addChild(west);

		currentPosition.setComp(compasrose);
		world.add(currentPosition);

		renderer.addRenderElement(world);

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		eventManager.addOnOrientationChangedAction(rotActionB1);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		worldUpdater.addObjectToUpdateCycle(world);
		worldUpdater.addObjectToUpdateCycle(rotActionB1);
		worldUpdater.addObjectToUpdateCycle(rotActionB3);
		worldUpdater.addObjectToUpdateCycle(rotActionB4);
		worldUpdater.addObjectToUpdateCycle(rotActionDebug);
		worldUpdater.addObjectToUpdateCycle(rotActionUnB);
		worldUpdater.addObjectToUpdateCycle(rotActionUnB2);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.addButtonToBottomView(new myRotateAction(rotActionB1),
				"Camera Buffered 1");
		guiSetup.addButtonToBottomView(new myRotateAction(rotActionB2),
				"Camera Buffered 2");
		guiSetup.addButtonToBottomView(new myRotateAction(rotActionB3),
				"Camera Buffered 3");
		guiSetup.addButtonToBottomView(new myRotateAction(rotActionB4),
				"Camera Buffered 4");
		guiSetup.addButtonToBottomView(new myRotateAction(rotActionUnB),
				"Camera Unbuffered 1");
		guiSetup.addButtonToBottomView(new myRotateAction(rotActionUnB2),
				"Camera Unbuffered 2");
	}

	class myRotateAction extends Command {

		private Action myAction;

		public myRotateAction(Action a) {
			myAction = a;
		}

		@Override
		public boolean execute() {
			EventManager.getInstance().onOrientationChangedAction = myAction;
			return true;
		}

	}

}
