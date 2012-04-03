package br.unb.unbiquitous.marker.virtual.object;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import br.unb.unbiquitous.marker.command.VirtualObjectCommand;

import com.google.droidar.de.rwth.BasicMarker;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.animations.AnimationFaceToCamera;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.util.IO;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.World;

public class MeuObjetoVirtual extends BasicMarker {

	private Obj objetoTexto;
	private World world;
	private Activity activity;
	private GLCamera glCamera;
	private String id;
	private MeshComponent meshComponent;
	private Vec positionVec;
	private VirtualObjectCommand virtualObjectCommand;

	boolean firstTime = true;

	public MeuObjetoVirtual(String id, GLCamera camera, World world,
			Activity activity) {
		super(id, camera);
		this.world = world;
		this.activity = activity;
		this.glCamera = camera;
		this.id = id;
		
		virtualObjectCommand = new VirtualObjectCommand();
		
	}

	public MeuObjetoVirtual(String id, GLCamera camera) {
		super(id, camera);
	}

	@Override
	public void setObjectPos(Vec positionVec) {
		
		if(firstTime){
			
//			meshComponent.addChild(GLFactory.getInstance().newCoordinateSystem());
//			meshComponent.addChild(GLFactory.getInstance().newCube());

//			objetoTexto = GLFactory.getInstance().newTextObject(this.id,positionVec, activity, glCamera);
			objetoTexto = this.desenhar(this.id,positionVec, activity, glCamera);
//			meshComponent.addChild(objetoTexto);
			
			world.add(objetoTexto);
			
			// Criando o objeto virtual

			// Setando os comandos quando o objeto virtual for clicado.
			meshComponent.setOnClickCommand(virtualObjectCommand);
			
			firstTime = false;
		}
		
		this.positionVec = positionVec;
		meshComponent.setPosition(positionVec);
//		objetoTexto.setPosition(positionVec);
	}

	@Override
	public void setObjRotation(float[] rotMatrix) {
		meshComponent.setRotationMatrix(rotMatrix);
	}
	
	private Obj desenhar(String textToDisplay, Vec textPosition, Context context, GLCamera glCamera){
		float textSize = 10;

		TextView v = new TextView(context);
		v.setTypeface(null, Typeface.BOLD);
		// Set textcolor to black:
		// v.setTextColor(new Color(0, 0, 0, 1).toIntARGB());
		v.setText(textToDisplay);

		Obj o = new Obj();
		this.meshComponent = GLFactory.getInstance().newTexturedSquare("textBitmap"	+ textToDisplay, IO.loadBitmapFromView(v), textSize);
		this.meshComponent.setPosition(textPosition);
//		this.meshComponent.addAnimation(new AnimationFaceToCamera(glCamera));
		o.setComp(this.meshComponent);
		return o;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
