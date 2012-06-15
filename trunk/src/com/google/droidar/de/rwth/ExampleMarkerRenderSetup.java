package com.google.droidar.de.rwth;

import com.google.droidar.actions.ActionBufferedCameraAR;
import com.google.droidar.commands.system.CommandPlaySound;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.animations.AnimationFaceToCamera;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.EventManager;
import com.google.droidar.util.IO;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import android.app.Activity;
import br.unb.unbiquitous.activity.R;
import br.unb.unbiquitous.marker.setup.MarkerDetectionSetup;
import br.unb.unbiquitous.marker.setup.MarkerObjectMap;
import br.unb.unbiquitous.marker.setup.UnrecognizedMarkerListener;


public class ExampleMarkerRenderSetup extends MarkerDetectionSetup {

	private GLCamera camera;
	private World world;

	@Override
	public UnrecognizedMarkerListener getUnrecognizedMarkerListener() {
		return null;
	}

	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
		markerObjectMap.put(new CameraMarker("0", camera));
	}

	@Override
	public void _a_initFieldsIfNecessary() {
		camera = new GLCamera();
		world = new World(camera);
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		renderer.addRenderElement(world);
		Obj o = new Obj();
		o.setComp(AndroidMeshData.getAndroidMesh());
		world.add(o);
		// initWorld(world);
	}

	private synchronized void initWorld(World world) {

		world.add(GLFactory.getInstance().newSolarSystem(new Vec(0, 0, 5)));
		world.add(GLFactory.getInstance().newHexGroupTest(new Vec(0, 0, -0.1f)));

		MeshComponent c = GLFactory.getInstance().newCube(null);
		c.setPosition(new Vec(-3, 3, 0));
		c.setScale(new Vec(0.5f, 0.5f, 0.5f));
		c.setOnClickCommand(new CommandPlaySound("/sdcard/train.mp3"));
		Obj geoC = new Obj();
		geoC.setComp(c);
		world.add(geoC);

		MeshComponent c2 = GLFactory.getInstance().newCube(null);
		c2.setPosition(new Vec(3, 3, 0));
		// GeoObj geoC = new GeoObj(GeoObj.normaluhr, c);
		Obj geoC2 = new Obj();
		geoC2.setComp(c2);
		world.add(geoC2);

		Obj hex = new Obj();
		Shape hexMesh = GLFactory.getInstance().newHexagon(
				new Color(0, 0, 1, 0.7f));
		hexMesh.getPosition().add(new Vec(0, 0, -1));
		hexMesh.scaleEqual(4.5f);
		hex.setComp(hexMesh);

		world.add(hex);

		Obj grid = new Obj();
		MeshComponent gridMesh = GLFactory.getInstance().newGrid(Color.blue(),
				1, 10);
		grid.setComp(gridMesh);
		world.add(grid);

		Obj treangle = new Obj();
		MeshComponent treangleMesh = GLFactory.getInstance().newTexturedSquare(
				"worldIconId",
				IO.loadBitmapFromId(myTargetActivity, R.drawable.icon));
		treangleMesh.setPosition(new Vec(0, -2, 1));
		treangleMesh.setPosition(new Vec(0, 0, 0));
		treangleMesh.addChild(new AnimationFaceToCamera(camera, 0.5f));
		treangle.setComp(treangleMesh);
		world.add(treangle);

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.onTouchMoveAction = new ActionBufferedCameraAR(camera);

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
	}

}
