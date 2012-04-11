package br.unb.unbiquitous.thread;

import java.util.concurrent.CountDownLatch;

import android.os.Looper;
import br.unb.unbiquitous.handler.DetectionHandler;
import br.unb.unbiquitous.handler.RepositionMarkerHandler;
import br.unb.unbiquitous.manager.ARManager;

/**
 * 
 * @author ricardoandrade
 * 
 */
public class RepositionMarkerThread extends Thread {

	/************************************************
	 * VARIABLES
	 ************************************************/
	private RepositionMarkerHandler handler;
	private final CountDownLatch handlerInitLatch;
	private ARManager arManager;
	private DetectionHandler detectionHandler;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/

	public RepositionMarkerThread(DetectionHandler detectionHandler, ARManager arManager) {
		this.detectionHandler = detectionHandler;
		this.arManager = arManager;
		this.handlerInitLatch = new CountDownLatch(1);
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	public RepositionMarkerHandler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		Looper.prepare();
		handler = new RepositionMarkerHandler(arManager);
		detectionHandler.setRepositionHandler(handler);
		handlerInitLatch.countDown();
		Looper.loop();
	}



}
