package br.unb.unbiquitous.marker.ar;

import android.app.Activity;

import com.google.droidar.de.rwth.BasicMarker;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.World;

public class MeuObjetoVirtual extends BasicMarker {

	private Obj objetoTexto;
	private World world;
	private Activity activity;
	private GLCamera glCamera;
	private int id;
	
	boolean firstTime = true;
	

	
	
	public MeuObjetoVirtual(int id, GLCamera camera, World world,
			Activity activity) {
		super(id, camera);
		this.world = world;
		this.activity = activity;
		this.glCamera = camera;
		this.id = id;
	}

	public MeuObjetoVirtual(int id, GLCamera camera) {
		super(id, camera);
	}

	@Override
	public void setObjectPos(Vec positionVec) {
		/*
		 * the first time this method is called an object could be
		 * created and added to the world
		 */
		if (firstTime) {
			firstTime = false;
			objetoTexto =  GLFactory.getInstance().newTextObject("texto", positionVec, activity, glCamera);
			world.add(objetoTexto);
		}
		objetoTexto.setPosition(positionVec);
	}

	@Override
	public void setObjRotation(float[] rotMatrix) {
	}

	public Obj getObjetoTexto() {
		return objetoTexto;
	}

	public void setObjetoTexto(Obj objetoTexto) {
		this.objetoTexto = objetoTexto;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public GLCamera getGlCamera() {
		return glCamera;
	}

	public void setGlCamera(GLCamera glCamera) {
		this.glCamera = glCamera;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	
	
	
}
