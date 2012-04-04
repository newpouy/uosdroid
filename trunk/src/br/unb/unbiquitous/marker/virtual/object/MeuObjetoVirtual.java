package br.unb.unbiquitous.marker.virtual.object;

import javax.security.auth.callback.TextOutputCallback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import br.unb.unbiquitous.marker.command.VirtualObjectCommand;

import com.google.droidar.de.rwth.BasicMarker;
import com.google.droidar.gl.Color;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.animations.AnimationFaceToCamera;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.util.IO;
import com.google.droidar.util.Log;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.World;

public class MeuObjetoVirtual extends BasicMarker {

	private Obj objetoTexto;
	private World world;
	private Activity activity;
	private GLCamera glCamera;
	private String id;
	private MeshComponent textMeshComponent;
	private MeshComponent shapeMeshComponent;
	private VirtualObjectCommand virtualObjectCommand;
	private AnimationFaceToCamera animationFaceToCamera;
	private MeshComponent coordinateSystem;
	boolean firstTime = true;

	public MeuObjetoVirtual(String id, GLCamera camera, World world,
			Activity activity) {
		super(id, camera);
		this.world = world;
		this.activity = activity;
		this.glCamera = camera;
		this.id = id;
		
		virtualObjectCommand = new VirtualObjectCommand();
		animationFaceToCamera = new AnimationFaceToCamera(glCamera);
		coordinateSystem = GLFactory.getInstance().newCoordinateSystem();
		
	}

	public MeuObjetoVirtual(String id, GLCamera camera) {
		super(id, camera);
	}

	@Override
	public void setObjectPos(Vec positionVec) {
		
		if(firstTime){
			
			// Criando o objeto virtual
			criar();

			objetoTexto = this.desenharTexto(this.id,positionVec, activity, glCamera);
			
			world.add(shapeMeshComponent);
			world.add(objetoTexto);
			
			// Setando os comandos quando o objeto virtual for clicado.
			textMeshComponent.setOnClickCommand(virtualObjectCommand);
			textMeshComponent.setOnLongClickCommand(virtualObjectCommand);
			shapeMeshComponent.setOnClickCommand(virtualObjectCommand);
			shapeMeshComponent.setOnLongClickCommand(virtualObjectCommand);
			firstTime = false;
		}
		
		Vec vec = positionVec.copy();
//		vec.y = vec.y - 3;
		
		shapeMeshComponent.setPosition(vec);
		textMeshComponent.setPosition(positionVec);
	}

	@Override
	public void setObjRotation(float[] rotMatrix) {	}
	
	private Obj desenharTexto(String textToDisplay, Vec textPosition, Context context, GLCamera glCamera){
		float textSize = 2.5f;

		// acima de 4 drivers o tem que ser textSize = 2.
		textToDisplay = textToDisplay + "\n \n - teclado" + "\n - mouse" + "\n - câmera" + "\n - tela" + "\n ...";
		
		TextView v = new TextView(context);
		v.setTypeface(null, Typeface.BOLD);
		v.setTextColor(Color.white().toIntRGB());
		v.setText(textToDisplay);
		
		Obj o = new Obj();
	    textMeshComponent = GLFactory.getInstance().newTexturedSquare("textBitmap"	+ textToDisplay, IO.loadBitmapFromView(v), textSize);
		textMeshComponent.setPosition(textPosition);
		textMeshComponent.addAnimation(animationFaceToCamera);
//		shapeMeshComponent.addChild(coordinateSystem);
		o.setComp(this.textMeshComponent);
		return o;
	}
	
	public void criar(){
		shapeMeshComponent = new Shape();
//		shapeMeshComponent.addChild(coordinateSystem);
		shapeMeshComponent.addChild(this.desenharQuadrado(Color.blackTransparent()));
		shapeMeshComponent.addAnimation(animationFaceToCamera);
	}
	
	public Shape desenharQuadrado(Color canBeNull) {
		Shape shape = new Shape(canBeNull);
		shape.add(new Vec(-1.5f, 1.75f, 0));
		shape.add(new Vec(-1.5f, -1.75f, 0));
		shape.add(new Vec(1.5f, -1.75f, 0));

		shape.add(new Vec(1.5f, -1.75f, 0));
		shape.add(new Vec(1.5f, 1.75f, 0));
		shape.add(new Vec(-1.5f, 1.75f, 0));
		
		shape.setPosition(new Vec(0, -1f, -0.5f));
		shape.setRotation(new Vec(90, 0, 0));

		return shape;
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
