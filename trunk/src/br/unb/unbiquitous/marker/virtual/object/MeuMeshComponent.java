package br.unb.unbiquitous.marker.virtual.object;

import javax.microedition.khronos.opengles.GL10;

import com.google.droidar.gl.Color;
import com.google.droidar.gl.Renderable;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.worldData.Visitor;

public class MeuMeshComponent extends MeshComponent {

	public MeuMeshComponent(){
		this(null);
	}
	
	protected MeuMeshComponent(Color color) {
		super(color);
	}

	@Override
	public boolean accept(Visitor visitor) {
		return visitor.default_visit(this);
	}

	@Override
	public void draw(GL10 gl, Renderable parent) {
		
	}

}
