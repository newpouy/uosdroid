package br.unb.unbiquitous.thread;

import java.util.concurrent.CountDownLatch;

import android.os.Looper;
import br.unb.unbiquitous.handler.DecodeQRCodeHandler;
import br.unb.unbiquitous.handler.DetectionHandler;
import br.unb.unbiquitous.manager.DecodeManager;

/**
 * 
 * @author ricardoandrade
 *
 */
final class DecodeQRCodeThread extends Thread {

	/************************************************
	 * VARIABLES
	 ************************************************/
	private DecodeQRCodeHandler handler;
	private final CountDownLatch handlerInitLatch;
	private final DecodeManager decodeManager;
	private DetectionHandler detectionHandler;


	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public DecodeQRCodeThread(DetectionHandler detectionHandler, DecodeManager decodeManager) {
		
		this.detectionHandler = detectionHandler;
		this.handlerInitLatch = new CountDownLatch(1);
		this.decodeManager = decodeManager;
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	public DecodeQRCodeHandler getHandler() {
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
		handler = new DecodeQRCodeHandler(detectionHandler, decodeManager);
		detectionHandler.setDecodeHandler(handler);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
