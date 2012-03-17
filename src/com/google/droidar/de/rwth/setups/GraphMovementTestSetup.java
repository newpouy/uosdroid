package com.google.droidar.de.rwth.setups;

import com.google.droidar.geo.DefaultNodeEdgeListener;
import com.google.droidar.geo.GeoGraph;
import com.google.droidar.geo.GeoObj;
import com.google.droidar.gl.CustomGLSurfaceView;
import com.google.droidar.gl.GLFactory;
import com.google.droidar.gl.GLRenderer;
import com.google.droidar.system.DefaultARSetup;
import com.google.droidar.system.EventManager;
import com.google.droidar.util.EfficientList;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.SystemUpdater;
import com.google.droidar.worldData.World;


public class GraphMovementTestSetup extends DefaultARSetup {

	@Override
	public void addObjectsTo(GLRenderer renderer, World world,
			GLFactory objectFactory) {

		EfficientList<GeoObj> l = new EfficientList<GeoObj>();
		l.add(newOb(10, 10));
		l.add(newOb(10, 20));
		l.add(newOb(20, 20));
		l.add(newOb(20, 10));
		l.add(newOb(30, 30));
		l.add(newOb(40, 40));
		l.add(newOb(50, 50));
		l.add(newOb(60, 40));
		l.add(newOb(60, 30));
		l.add(newOb(50, 30));
		l.add(newOb(40, 30));
		l.add(newOb(30, 40));
		l.add(newOb(30, 50));
		l.add(newOb(30, 60));
		GeoGraph g = GeoGraph.convertToGeoGraph(l, true,
				new DefaultNodeEdgeListener(getCamera()));
		world.add(g);
	}

	private GeoObj newOb(float x, float y) {
		GeoObj o = new GeoObj();
		o.setVirtualPosition(new Vec(x, y, 0));
		return o;
	}

	@Override
	public void _c_addActionsToEvents(EventManager eventManager,
			CustomGLSurfaceView arView, SystemUpdater updater) {

		super._c_addActionsToEvents(eventManager, arView, updater);
		eventManager.onLocationChangedAction = null;
	}

}
