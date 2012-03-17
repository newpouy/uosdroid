package com.google.droidar.de.rwth.setups;

import com.google.droidar.actions.Action;
import com.google.droidar.actions.ActionBufferedCameraAR;
import com.google.droidar.actions.ActionCalcRelativePos;
import com.google.droidar.actions.ActionMoveCameraBuffered;
import com.google.droidar.actions.ActionPlaceObject;
import com.google.droidar.actions.ActionRotateCameraBuffered;
import com.google.droidar.commands.Command;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.ErrorHandler;
import com.google.droidar.system.EventManager;
import com.google.droidar.system.Setup;
import com.google.droidar.util.Vec;
import com.google.droidar.util.Wrapper;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.app.Activity;


public class PlaceObjectsSetup extends Setup {

	private GLCamera camera;
	private World world;

	private Wrapper placeObjectWrapper;

	@Override
	public void _a_initFieldsIfNecessary() {

		// allow the user to send error reports to the developer:
		ErrorHandler.enableEmailReports("droidar.rwth@gmail.com",
				"Error in DroidAR App");

		placeObjectWrapper = new Wrapper();

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		camera = new GLCamera(new Vec(0, 0, 10));
		world = new World(camera);

		Obj placerContainer = new Obj();
		placerContainer.setComp(objectFactory.newArrow());
		world.add(placerContainer);

		placeObjectWrapper.setTo(placerContainer);

		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.addOnTouchMoveAction(new ActionBufferedCameraAR(camera));
		Action rot1 = new ActionRotateCameraBuffered(camera);
		Action rot2 = new ActionPlaceObject(camera, placeObjectWrapper, 50);

		updater.addObjectToUpdateCycle(rot1);
		updater.addObjectToUpdateCycle(rot2);

		eventManager.addOnOrientationChangedAction(rot1);
		eventManager.addOnOrientationChangedAction(rot2);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater worldUpdater) {
		worldUpdater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity context) {

		guiSetup.addButtonToTopView(new Command() {
			@Override
			public boolean execute() {
				final Obj placerContainer = newObject();
				world.add(placerContainer);
				placeObjectWrapper.setTo(placerContainer);
				return true;
			}

			private Obj newObject() {
				final Obj placerContainer = new Obj();
				Color c = Color.getRandomRGBColor();
				c.alpha = 0.7f;
				MeshComponent arrow = GLFactory.getInstance().newDiamond(c);
				arrow.setOnClickCommand(new Command() {
					@Override
					public boolean execute() {
						placeObjectWrapper.setTo(placerContainer);
						return true;
					}
				});
				placerContainer.setComp(arrow);
				return placerContainer;
			}
		}, "Place next!");

		guiSetup.setTopViewCentered();
	}

}
