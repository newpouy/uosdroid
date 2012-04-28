package br.unb.unbiquitous.marker.detection;

import android.app.Activity;
import android.util.Log;
import br.unb.unbiquitous.marker.virtual.object.MeuObjetoVirtual;

import com.google.droidar.actions.Action;
import com.google.droidar.actions.ActionMoveCameraBuffered;
import com.google.droidar.actions.ActionRotateCameraBuffered;
import com.google.droidar.commands.Command;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.gl.MarkerObject;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gui.GuiSetup;
import com.google.droidar.system.EventManager;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;

/**
 * 
 * @author ricardoandrade
 *
 */
public class SingleMarkerSetup extends MarkerDetectionSetup {
	
	/************************************************************
	 * VARIABLES
	 ************************************************************/
	private GLCamera camera;
	private World world;
	private MeshComponent meshComponent;
	private MeuObjetoVirtual meuObjetoVirtual;
	
	/************************************************************
	 * PUBLIC METHODS
	 ************************************************************/
	
	@Override
	public void _a_initFieldsIfNecessary() {
		camera = new GLCamera(new Vec(0, 0, 10));
		world = new World(camera);
	}

	@Override
	public UnrecognizedMarkerListener getUnrecognizedMarkerListener() {
		return new UnrecognizedMarkerListener() {

			public void onUnrecognizedMarkerDetected(	int markerCode,
														float[] mat, 
														int startIdx, 
														int endIdx, 
														int rotationValue) {
				
				Log.i("Retirando marcador", "unrecognized markerCode=" + markerCode);
			}
		};

	}
	
	public void addMarkerObject(MarkerObject markerObject){
		markerObjectMap.put(markerObject);
	}

	public void addMarkerObject(String appName){
		meuObjetoVirtual = new MeuObjetoVirtual(appName, camera, world, this.activity);
		this.markerObjectMap.put(meuObjetoVirtual);
	}
	
	@Override
	public void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap) {
		this.markerObjectMap = markerObjectMap;
	}

	@Override
	public void _b_addWorldsToRenderer(GLRenderer renderer,	GLFactory objectFactory, GeoObj currentPosition) {
		renderer.addRenderElement(world);
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
	
	/**
	 * Adiciona os botões na tela. Chamados no Setup.
	 */
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

				meshComponent.setPosition(rayPosition.add(rayDirection));

				return false;
			}
		}, "Place 2 meters infront");
	}
}
