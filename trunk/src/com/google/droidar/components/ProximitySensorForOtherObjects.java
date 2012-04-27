package com.google.droidar.components;

import com.google.droidar.commands.Command;
import com.google.droidar.gl.scenegraph.MeshComponent;
import com.google.droidar.util.EfficientList;
import com.google.droidar.util.Log;
import com.google.droidar.util.QuadTree;
import com.google.droidar.util.Vec;
import com.google.droidar.worldData.Entity;
import com.google.droidar.worldData.LargeWorld;
import com.google.droidar.worldData.Obj;
import com.google.droidar.worldData.RenderableEntity;
import com.google.droidar.worldData.UpdateTimer;
import com.google.droidar.worldData.Updateable;
import com.google.droidar.worldData.Visitor;
import com.google.droidar.worldData.World;



public class ProximitySensorForOtherObjects implements Entity {
	private static final float DEFAULT_UPDATE_TIME = 1;
	private static final String LOG_TAG = "ProximitySensorForOtherObjects";
	private World myWorld;
	private float myMaxDistance;
	private Command myCommand;
	private UpdateTimer myTimer;

	public ProximitySensorForOtherObjects(World world, float distance,
			Command commandToExecuteWhenProximityReached) {
		myWorld = world;
		myMaxDistance = distance;
		myCommand = commandToExecuteWhenProximityReached;
		myTimer = new UpdateTimer(DEFAULT_UPDATE_TIME, null);
	}

	@Override
	public Updateable getMyParent() {
		Log.e(LOG_TAG, "Get parent called which is not "
				+ "implemented for this component!");
		return null;
	}

	@Override
	public void setMyParent(Updateable parent) {
		// can't have children so the parent does not have to be stored
		Log.e(LOG_TAG, "Set parent called which is not "
				+ "implemented for this component!");
	}

	@Override
	public boolean update(float timeDelta, Updateable parent) {

		if (myTimer.update(timeDelta, this)) {
			if (parent instanceof Obj) {
				Obj obj = (Obj) parent;
				MeshComponent myMesh = obj.getGraphicsComponent();
				if (myMesh != null) {
					if (myWorld instanceof LargeWorld)
						findObjectsCloseTo(obj, myMesh, (LargeWorld) myWorld);
					else
						findObjectsCloseTo(obj, myMesh, myWorld.getAllItems());
				}
			} else {
				Log.w(LOG_TAG,
						"Sensor is not child of a Obj and therefor cant run!");
			}
		}
		return true;
	}

	/**
	 * A {@link LargeWorld} uses a {@link QuadTree} and it has a getObjects
	 * close to x functionality build in.
	 * 
	 * @param obj
	 * @param myMesh
	 * @param largeWorld
	 */
	private void findObjectsCloseTo(Obj obj, MeshComponent myMesh,
			LargeWorld largeWorld) {
		EfficientList<RenderableEntity> list = largeWorld.getItems(
				obj.getPosition(), myMaxDistance);
		for (int i = 0; i < list.myLength; i++) {
			myCommand.execute(list.get(i));
		}
	}

	private void findObjectsCloseTo(Obj obj, MeshComponent myMesh,
			EfficientList<RenderableEntity> list) {
		if (list != null) {
			for (int i = 0; i < list.myLength; i++) {
				if (list.get(i) != obj && list.get(i) instanceof Obj) {
					Vec objPos = ((Obj) list.get(i))
							.getPosition();
					if (objPos != null) {
						float currentDistance = Vec.distance(
								myMesh.getPosition(), objPos);
						if (0 <= currentDistance
								&& currentDistance < myMaxDistance) {
							myCommand.execute(list.get(i));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean accept(Visitor visitor) {
		// TODO Auto-generated method stub
		return false;
	}

}