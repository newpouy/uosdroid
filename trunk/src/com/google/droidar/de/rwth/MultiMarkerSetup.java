package com.google.droidar.de.rwth;

import com.google.droidar.actions.Action;
import com.google.droidar.actions.ActionMoveCameraBuffered;
import com.google.droidar.actions.ActionRotateCameraBuffered;
import com.google.droidar.commands.Command;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.EventManager;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

import br.unb.QRCodeDecoder;
import br.unb.unbiquitous.marker.detection.MarkerDetectionSetup;
import br.unb.unbiquitous.marker.detection.MarkerObjectMap;
import br.unb.unbiquitous.marker.detection.UnrecognizedMarkerListener;
import android.app.Activity;


public class MultiMarkerSetup extends MarkerDetectionSetup {

	private GLCamera camera;
	private World world;
	private MeshComponent mesh1;
	private MeshComponent mesh2;
	
	@Override
	public void _a_initFieldsIfNecessary() {
		camera = new GLCamera(new Vec(0, 0, 10));
		world = new World(camera);
		mesh1 = new Shape();

		mesh1.addChild(GLFactory.getInstance().newCoordinateSystem());
		// mesh.add(GLFactory.getInstance().newCircle(new Color(0, 0, 1,
		// 0.6f)));
		mesh1.addChild(GLFactory.getInstance().newCube());

		mesh2 = new Shape();
		mesh2.addChild(GLFactory.getInstance().newCoordinateSystem());
		mesh2.addChild(GLFactory.getInstance().newCircle(
				new Color(0, 0, 1, 0.6f)));
		// mesh1.add(GLFactory.getInstance().newCube());

	}

	@Override
	public UnrecognizedMarkerListener _a2_getUnrecognizedMarkerListener() {
		return new UnrecognizedMarkerListener() {

			public void onUnrecognizedMarkerDetected(int markerCode,
					float[] mat, int startIdx, int endIdx, int rotationValue) {
				System.out.println("unrecognized markerCode=" + markerCode);
			}
		};

	}

	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
		markerObjectMap.put(new SimpleMeshPlacer(0, mesh1, camera));
		markerObjectMap.put(new SimpleMeshPlacer(1, mesh2, camera));

		/*
		 * example for more complex behavior:
		 */
		markerObjectMap.put(new BasicMarker(2, camera) {

			MeshComponent targetMesh;
			boolean firstTime = true;

			@Override
			public void setObjectPos(Vec positionVec) {
				/*
				 * the first time this method is called an object could be
				 * created and added to the world
				 */
				if (firstTime) {
					firstTime = false;
					Obj aNewObject = new Obj();
					targetMesh = GLFactory.getInstance().newArrow();
					aNewObject.setComp(targetMesh);
					world.add(aNewObject);
				}
				targetMesh.setPosition(positionVec);
			}

			@Override
			public void setObjRotation(float[] rotMatrix) {
				if (targetMesh != null)
					targetMesh.setRotationMatrix(rotMatrix);
			}
		});
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,
			GLFactory objectFactory, GeoObj currentPosition) {
		renderer.addRenderElement(world);
		Obj o = new Obj();
		o.setComp(mesh1);
		world.add(o);

		Obj o2 = new Obj();
		o2.setComp(mesh2);
		world.add(o2);

		world.add(objectFactory.newHexGroupTest(new Vec()));

	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {
		arView.onTouchMoveAction = new ActionMoveCameraBuffered(camera, 5, 25);
		Action rot = new ActionRotateCameraBuffered(camera);
		updater.addObjectToUpdateCycle(rot);
		eventManager.addOnOrientationChangedAction(rot);
		eventManager.addOnTrackballAction(new ActionMoveCameraBuffered(camera,
				1, 25));

	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		updater.addObjectToUpdateCycle(world);

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		guiSetup.addButtonToBottomView(new Command() {

			@Override
			public boolean execute() {

				Vec rayPosition = new Vec();
				Vec rayDirection = new Vec();
				camera.getPickingRay(rayPosition, rayDirection,
						GLRenderer.halfWidth, GLRenderer.halfHeight);

				System.out.println("rayPosition=" + rayPosition);
				System.out.println("rayDirection=" + rayDirection);

				rayDirection.setLength(5);

				mesh1.setPosition(rayPosition.add(rayDirection));

				return false;
			}
		}, "Place 2 meters infront");

	}

	
	
	
}
