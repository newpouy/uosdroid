package br.unb.unbiquitous.marker.virtual.object;

import android.app.Activity;
import br.unb.unbiquitous.marker.command.VirtualObjectCommand;

import com.google.droidar.de.rwth.BasicMarker;
import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.scenegraph.MeshComponent;
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
	private VirtualObjectCommand objetoVirtualCommand;

	boolean firstTime = true;

	public MeuObjetoVirtual(String id, GLCamera camera, World world,
			Activity activity) {
		super(id, camera);
		this.world = world;
		this.activity = activity;
		this.glCamera = camera;
		this.id = id;
		
		// Criando o objeto virtual
		meshComponent = new MeuMeshComponent();
		objetoTexto = GLFactory.getInstance().newTextObject(this.id,positionVec, activity, glCamera);
		meshComponent.addChild(objetoTexto);
		world.add(meshComponent);
		
		// Setando os comandos quando o objeto virtual for clicado.
		objetoVirtualCommand = new VirtualObjectCommand();
		meshComponent.setOnClickCommand(objetoVirtualCommand);
	}

	public MeuObjetoVirtual(String id, GLCamera camera) {
		super(id, camera);
	}

	@Override
	public void setObjectPos(Vec positionVec) {
		this.positionVec = positionVec;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
