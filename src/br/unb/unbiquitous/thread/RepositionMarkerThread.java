package br.unb.unbiquitous.thread;

import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;
import br.unb.unbiquitous.manager.ARManager;

/**
 * 
 * @author ricardoandrade
 * 
 */
public class RepositionMarkerThread extends Thread {

	/************************************************
	 * CONSTANS
	 ************************************************/
	private static final String TAG = RepositionMarkerThread.class.getSimpleName();
	
	/************************************************
	 * VARIABLES
	 ************************************************/
	private Handler handler;
	private final CountDownLatch handlerInitLatch;
	private ARManager arManager;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/

	public RepositionMarkerThread(ARManager arManager) {
		this.arManager = arManager;
		this.handlerInitLatch = new CountDownLatch(1);
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	public Handler getHandler() {
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
		handlerInitLatch.countDown();
		Looper.loop();
	}



}
