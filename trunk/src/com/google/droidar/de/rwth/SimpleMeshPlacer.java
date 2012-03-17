package com.google.droidar.de.rwth;

import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.MarkerObject;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.util.Vec;


public class SimpleMeshPlacer extends BasicMarker {

	protected MeshComponent myTargetMesh;

	public SimpleMeshPlacer(int id, MeshComponent mesh, GLCamera camera) {
		super(id, camera);
		myTargetMesh = mesh;
	}

	@Override
	public void setObjRotation(float[] rotMatrix) {
		myTargetMesh.setRotationMatrix(rotMatrix);
	}

	@Override
	public void setObjectPos(Vec positionVec) {
		myTargetMesh.setPosition(positionVec);
	}

}
