package br.unb.unbiquitous.thread;

import java.awt.Stroke;

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
	private ARManager arManager;
	private DecodeDTO decodeDTO;
	
	private boolean stopRequest;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/

	public RepositionMarkerThread(ARManager arManager){
		this.arManager = arManager;
		decodeDTO = new DecodeDTO();
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/

	/**
	 * 
	 */
	@Override
	public void run() {
		while(true){
			if(!stopRequest){
				arManager.reposicionarObjetoVirtual(decodeDTO.getAppName(), decodeDTO.getRotacao());
			}
		}
		
	}

	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
	
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

	public boolean isStopRequest() {
		return stopRequest;
	}

	public void setStopRequest(boolean stopRequest) {
		this.stopRequest = stopRequest;
	}
	
	


}
