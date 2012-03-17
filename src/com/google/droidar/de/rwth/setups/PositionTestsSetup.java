package com.google.droidar.de.rwth.setups;

import com.google.droidar.actions.ActionCalcRelativePos;
import com.google.droidar.actions.ActionMoveCameraBuffered;
import com.google.droidar.actions.ActionRotateCameraBuffered;
import com.google.droidar.commands.Command;
import com.google.droidar.commands.DebugCommandPositionEvent;
import com.google.droidar.commands.ui.CommandInUiThread;
import com.google.droidar.commands.ui.CommandShowToast;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.EventManager;
import com.google.droidar.system.Setup;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.R;
import android.app.Activity;


public class PositionTestsSetup extends Setup {

	protected static final int ZDELTA = 5;
	private GLCamera camera;
	private World world;
	private ActionCalcRelativePos gpsAction;
	private GeoObj posA;
	private GeoObj posB;
	private GeoObj posC;
	private GeoObj posD;
	private GeoObj posE;

	public PositionTestsSetup() {
		camera = new GLCamera();
		world = new World(camera);
		gpsAction = new ActionCalcRelativePos(world, camera);
		posA = new GeoObj(50.780231, 6.065505);
		posB = new GeoObj(50.780815, 6.06662);
		posC = new GeoObj(50.780557, 6.06735);
		posD = new GeoObj(50.779892, 6.065955);
		posE = new GeoObj(50.780408, 6.066492);

	}

	@Override
	public void _a_initFieldsIfNecessary() {

	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {

		spawnObj(posA, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posB, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posC, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posD, GLFactory.getInstance().newCircle(Color.green()));
		spawnObj(posE, GLFactory.getInstance().newCircle(Color.blue()));

		renderer.addRenderElement(world);
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.onTouchMoveAction = new ActionMoveCameraBuffered(camera, 5, 25);

		ActionRotateCameraBuffered rot = new ActionRotateCameraBuffered(camera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);

		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				5, 25));
		// eventManager.addOnLocationChangedAction(gpsAction);
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.setRightViewAllignBottom();

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

		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {
				gpsAction.resetWorldZeroPositions(camera.getGPSLocation());
				return false;
			}
		}, "Reset world zero pos");

		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posA), "Go to pos A");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posB), "Go to pos B");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posC), "Go to pos C");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posD), "Go to pos D");
		guiSetup.addButtonToBottomView(new DebugCommandPositionEvent(gpsAction,
				posE), "Go to pos E");

		addSpawnButtonToUI(posA, "Spawn at posA", guiSetup);
		addSpawnButtonToUI(posB, "Spawn at posB", guiSetup);
		addSpawnButtonToUI(posC, "Spawn at posC", guiSetup);
		addSpawnButtonToUI(posD, "Spawn at posD", guiSetup);
		addSpawnButtonToUI(posE, "Spawn at posE", guiSetup);

		addGpsPosOutputButtons(guiSetup);

	}

	private void addGpsPosOutputButtons(GuiSetup guiSetup) {
		guiSetup.addButtonToBottomView(new CommandInUiThread() {

			@Override
			public void executeInUiThread() {
				Vec pos = camera.getGPSPositionVec();
				String text = "latitude=" + pos.y + ", longitude=" + pos.x;
				CommandShowToast.show(myTargetActivity, text);
			}
		}, "Show Camera GPS pos");

		guiSetup.addButtonToBottomView(new CommandInUiThread() {

			@Override
			public void executeInUiThread() {
				GeoObj pos = EventManager.getInstance()
						.getCurrentLocationObject();
				String text = "latitude=" + pos.getLatitude() + ", longitude="
						+ pos.getLongitude();
				CommandShowToast.show(myTargetActivity, text);
			}
		}, "Show real GPS pos");

		guiSetup.addButtonToBottomView(new CommandInUiThread() {

			@Override
			public void executeInUiThread() {
				GeoObj pos = EventManager.getInstance()
						.getZeroPositionLocationObject();
				String text = "latitude=" + pos.getLatitude() + ", longitude="
						+ pos.getLongitude();
				CommandShowToast.show(myTargetActivity, text);
			}
		}, "Show zero GPS pos");
	}

	private void addSpawnButtonToUI(final GeoObj pos, String buttonText,
			GuiSetup guiSetup) {
		guiSetup.addButtonToTopView(new Command() {
			@Override
			public boolean execute() {

				MeshComponent mesh = GLFactory.getInstance().newArrow();
				spawnObj(pos, mesh);
				return true;
			}

		}, buttonText);
	}

	private void spawnObj(final GeoObj pos, MeshComponent mesh) {
		GeoObj x = new GeoObj(pos);

		mesh.setPosition(Vec.getNewRandomPosInXYPlane(new Vec(), 0.1f, 1f));
		x.setComp(mesh);
		CommandShowToast.show(myTargetActivity, "Object spawned at "
				+ x.getMySurroundGroup().getPosition());
		world.add(x);
	}
}
