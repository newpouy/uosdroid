package br.unb.unbiquitous.thread;

import java.util.concurrent.CountDownLatch;

import android.os.Looper;
import br.unb.unbiquitous.handler.DetectionHandler;
import br.unb.unbiquitous.handler.RepositionMarkerHandler;
import br.unb.unbiquitous.manager.ARManager;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;

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
	private CountDownLatch handlerInitLatch;
	private ARManager arManager;
	private DetectionHandler detectionHandler;
	
	private DecodeDTO decodeDTO;
	private float[] rotacao;
	
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/

	public RepositionMarkerThread(ARManager arManager){
		this.arManager = arManager;
		decodeDTO = new DecodeDTO();
	}
	
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
//		Looper.prepare();
//		handler = new RepositionMarkerHandler(arManager);
//		detectionHandler.setRepositionHandler(handler);
//		handlerInitLatch.countDown();
//		Looper.loop();
		
		
		
		while(true){
			arManager.reposicionarObjetoVirtual(decodeDTO.getAppName(), decodeDTO.getRotacao());
		}
		
		
	}

	public void reposicionar(DecodeDTO decodeDTO){
		this.decodeDTO = decodeDTO;
	}

	public synchronized DecodeDTO getDecodeDTO() {
		return decodeDTO;
	}

	public void setDecodeDTO(DecodeDTO decodeDTO) {
		this.decodeDTO = decodeDTO;
	}
	
	public void setRotacao(float[] rotacao){
		synchronized (decodeDTO) {
			decodeDTO.setRotacao(rotacao);
		}
	}


}
