package com.google.droidar.actions;

import com.google.droidar.actions.algos.BufferAlgo2;
import com.google.droidar.actions.algos.SensorAlgo1;
import com.google.droidar.gl.GLCamera;


public class ActionRotateCameraBuffered4 extends ActionWithSensorProcessing {

	public ActionRotateCameraBuffered4(GLCamera targetCamera) {
		super(targetCamera);
	}

	@Override
	protected void initAlgos() {
		accelAlgo = new SensorAlgo1(0.5f);
		magnetAlgo = new SensorAlgo1(0.8f);
		accelBufferAlgo = new BufferAlgo2(0.2f);
		magnetBufferAlgo = new BufferAlgo2(0.2f);

		orientAlgo = new SensorAlgo1(0.5f);// TODO
		orientationBufferAlgo = new BufferAlgo2(0.2f); // TODO
	}

}
