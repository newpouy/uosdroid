package com.google.droidar.actions;

import com.google.droidar.actions.algos.BufferAlgo1;
import com.google.droidar.actions.algos.SensorAlgo1;
import com.google.droidar.gl.GLCamera;


public class ActionRotateCameraBuffered extends ActionWithSensorProcessing {

	public ActionRotateCameraBuffered(GLCamera targetCamera) {
		super(targetCamera);
	}

	@Override
	public void initAlgos() {
		accelAlgo = new SensorAlgo1(0.1f);
		magnetAlgo = new SensorAlgo1(1.4f);
		orientAlgo = new SensorAlgo1(0.005f);// TODO find correct values

		accelBufferAlgo = new BufferAlgo1(0.1f, 4f);
		magnetBufferAlgo = new BufferAlgo1(0.1f, 4f);
	}

}
