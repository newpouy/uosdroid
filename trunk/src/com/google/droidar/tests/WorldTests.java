package com.google.droidar.tests;

import com.google.droidar.gl.GLCamera;
import com.google.droidar.gl.animations.AnimationGrow;
import com.google.droidar.gl.animations.GLAnimation;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.gl.scenegraph.Shape;
import com.google.droidar.system.Container;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.World;


public class WorldTests extends SimpleTesting {

	@Override
	public void run() throws Exception {
		GLCamera cam = new GLCamera();
		World w = new World(cam);
		Obj o = new Obj();
		MeshComponent s = new Shape();
		MeshComponent s2 = new Shape();
		GLAnimation a = new AnimationGrow(23f);
		s.addChild(s2);
		s.addAnimation(a);
		assertTrue(s.getChildren() instanceof Container);
		assertTrue(((Container) s.getChildren()).getAllItems().myLength == 2);
		s.remove(a);
		assertTrue(s.getChildren() instanceof Container);
		assertTrue(((Container) s.getChildren()).getAllItems().myLength == 1);
		s.remove(a);
		assertTrue(s.getChildren() instanceof Container);
		assertTrue(((Container) s.getChildren()).getAllItems().myLength == 1);
		s.remove(s2);
		// TODO should s still has a container as a child or just null?
		assertTrue(s.getChildren() instanceof Container);
		assertTrue(((Container) s.getChildren()).getAllItems().myLength == 0);

		o.setComp(s);
		assertTrue(s != null);
		assertTrue(o.getComp(MeshComponent.class) == s);
		o.remove(s);
		assertTrue(o.getComp(MeshComponent.class) == null);
		assertTrue(w.getAllItems().myLength == 0);
		w.add(o);
		assertTrue(w.getAllItems().myLength == 1);
		assertTrue(w.getAllItems().contains(o) >= 0);
		w.remove(o);
		assertTrue(w.getAllItems().myLength == 0);
		assertTrue(w.getAllItems().contains(o) == -1);
	}

}
