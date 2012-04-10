package br.unb.unbiquitous.marker.decoder;

import com.google.droidar.util.Log;

import br.unb.manager.ar.ARManager;
import br.unb.manager.decode.DecodeManager;

/**
 * 
 * @author ricardoandrade
 * 
 */
public class RepositionThread extends Thread {

	/************************************************
	 * CONSTANS
	 ************************************************/
	private static final String TAG = "RepositionThread";

	/************************************************
	 * VARIABLES
	 ************************************************/
	private ARManager arManager;
	private float[] rotacao;
	private boolean decodificar;
	
	
	private int frameWidth;
	private int frameHeight;
	private byte[] frame;

	private DecodeManager decodeManager;

	/************************************************
	 * CONSTRUCTOR
	 ************************************************/

	public RepositionThread(ARManager arManager, DecodeManager decodeManager) {
		this.arManager = arManager;
		this.decodeManager = decodeManager;
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/

	@Override
	public synchronized void run() {
//		while(true){
			while(!decodificar){
				try {
					
					//Esperando o frame para decodificação
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Log.i(TAG, "Procurando o QRCode.");
			
			if(decodeManager.isQRCodeFound(frame, frameWidth, frameHeight) && decodeManager.isNewMarker()){
				// Achou um novo marcador
				
				Log.i(TAG, "Decodificou um novo marcador");
	
	//			arManager.retirarObjetosVirtuais();
				arManager.inserirNovoObjetoVirtual(decodeManager.getLastMarkerName(), rotacao);
			}
			
			// Reposicionar o objeto
			decodificar = false;
			yield();
//		}
	}
		
		

	/************************************************
	 * PRIVATE METHODS
	 ************************************************/

	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/


	public synchronized void setRotacao(float[] rotacao) {
		this.rotacao = rotacao;
	}

	public boolean isDecodificar() {
		return decodificar;
	}

	public synchronized void setDecodificar(boolean decodificar) {
		this.decodificar = decodificar;
	}

	public synchronized void setFrame(byte[] frame) {
		this.frame = frame;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public synchronized void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public synchronized  void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}



}
