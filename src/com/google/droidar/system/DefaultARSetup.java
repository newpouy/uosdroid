package com.google.droidar.system;

import com.google.droidar.actions.Action;
import com.google.droidar.actions.ActionCalcRelativePos;
import com.google.droidar.actions.ActionMoveCameraBuffered;
import com.google.droidar.actions.ActionRotateCameraBuffered;
import com.google.droidar.actions.ActionWASDMovement;
import com.google.droidar.actions.ActionWaitForAccuracy;
import com.google.droidar.actions.EventListenerGroup;
import com.google.droidar.commands.Command;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.util.Log;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.R;
import android.app.Activity;
import android.location.Location;


/**
 * This is an example how you can use the default setup: <br>
 * <code>
 * ArActivity.startWithSetup(currentActicity, new DefaultARSetup() { <br>
 * 	 public void addObjectsTo(World world, GLFactory factory) { <br>
 * 		GeoObj obj = new GeoObj();<br>
 * 		obj.setComp(factory.newCube()); world.add(obj); <br>
 * 		obj.setVirtualPosition(new Vec()); <br>
 * 	 } <br> 
 * });
 * <code>
 * 
 * @author Spobo
 * 
 */
public abstract class DefaultARSetup extends Setup {

	protected static final int ZDELTA = 5;
	private static final String LOG_TAG = "DefaultARSetup";

	public GLCamera camera;
	public World world;
	private ActionWASDMovement wasdAction;
	private GLRenderer myRenderer;
	private boolean addObjCalledOneTieme;
	private ActionWaitForAccuracy minAccuracyAction;
	private Action rotateGLCameraAction;

	public DefaultARSetup() {
		camera = new GLCamera(new Vec(0, 0, 2));
		world = new World(camera);
	}

	@Override
	public void _a_initFieldsIfNecessary() {

	}

	public World getWorld() {
		return world;
	}

	public GLCamera getCamera() {
		return camera;
	}

	/**
	 * This will be called when the GPS accuracy is high enough
	 * 
	 * @param renderer
	 * @param world
	 * @param objectFactory
	 */
	public abstract void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory);

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		myRenderer = renderer;
		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(final EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		wasdAction = new ActionWASDMovement(camera, 25, 50, 20);
		rotateGLCameraAction = new ActionRotateCameraBuffered(camera);
		
		arView.onTouchMoveAction = wasdAction;
		eventManager.addOnOrientationChangedAction(rotateGLCameraAction);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		eventManager.addOnLocationChangedAction(new ActionCalcRelativePos(
				world, camera));
		minAccuracyAction = new ActionWaitForAccuracy(getActivity(), 24.0f,
				10) {
			@Override
			public void minAccuracyReachedFirstTime(Location l,
					ActionWaitForAccuracy a) {
				callAddObjectsToWorldIfNotCalledAlready();
				if (eventManager.onLocationChangedAction instanceof EventListenerGroup)
					((EventListenerGroup) eventManager.onLocationChangedAction)
							.remove(a);
			}
		};
		eventManager.addOnLocationChangedAction(minAccuracyAction);
	}

	protected void callAddObjectsToWorldIfNotCalledAlready() {
		if (!addObjCalledOneTieme)
			addObjectsTo(myRenderer, world, GLFactory.getInstance());
		else
			Log.w(LOG_TAG, "callAddObjectsToWorldIfNotCalledAlready() "
					+ "called more then one time!");
		addObjCalledOneTieme = true;
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);
		updater.addObjectToUpdateCycle(wasdAction);
		updater.addObjectToUpdateCycle(rotateGLCameraAction);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.setRightViewAllignBottom();

		guiSetup.addViewToTop(minAccuracyAction.getView());

		guiSetup.addImangeButtonToRightView(R.drawable.arrow_up_float,
				new Command() {
					@Override
					public boolean execute() {
						camera.changeZPositionBuffered(+ZDELTA);
						return false;
					}
				});
		guiSetup.addImangeButtonToRightView(R.drawable.arrow_down_float,
				new Command() {
					@Override
					public boolean execute() {
						camera.changeZPositionBuffered(-ZDELTA);
						return false;
					}
				});

	}

}
