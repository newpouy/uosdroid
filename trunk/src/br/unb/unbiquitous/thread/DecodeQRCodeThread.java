package br.unb.unbiquitous.thread;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;
import br.unb.unbiquitous.manager.ARManager;
import br.unb.unbiquitous.manager.DecodeManager;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;
import br.unb.unbiquitous.marker.decoder.DecodeState;

/**
 * 
 * @author ricardoandrade
 *
 */
final class DecodeQRCodeThread extends Thread {
	
	/************************************************
	 * CONSTANTS
	 ************************************************/
	
	private static final String TAG = DecodeQRCodeThread.class.getSimpleName();
	private static final int FRAME_WIDTH = 848;
	private static final int FRAME_HEIGHT = 480;

	/************************************************
	 * VARIABLES
	 ************************************************/
	private DecodeManager decodeManager ;
	
	private ByteArrayBuffer byteArrayBuffer;
	private float[] rotacao; 
	private DecodeDTO decodeDTO;
	
	private boolean busy;
	private boolean stopRequest;
	              
	private DecodeState decodeState = DecodeState.WAITTING;
	private RepositionMarkerThread repositionMarkerThread;
	private ARManager arManager;

	/************************************************
	 * CONSTRUCTOR
	 ************************************************/

	public DecodeQRCodeThread(DecodeManager decodeManager, RepositionMarkerThread repositionMarkerThread, ARManager arManager){
		this.decodeManager = decodeManager;
		this.repositionMarkerThread = repositionMarkerThread;
		this.arManager = arManager;
	}
	

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/

	/**
	 * 
	 */
	@Override
	public synchronized void run() {
		
		decodeDTO = repositionMarkerThread.getDecodeDTO();
		
		while(true){
			while(!busy || stopRequest){
				try {
					wait();// wait for a new frame
				} catch (InterruptedException e) {}
				
				Log.i(TAG, "Frame recebido: Decodificando QRCode.");
				
				if(decodeManager.isQRCodeFound(byteArrayBuffer.toByteArray(), FRAME_WIDTH, FRAME_HEIGHT)){
					Log.i(TAG, "Frame recebido: QRCode decodificado com sucesso.");
					synchronized (decodeDTO) {
						
						decodeDTO.setAppName(decodeManager.getLastMarkerName());
						decodeDTO.setRotacao(rotacao);
						
						arManager.inserirNovoObjetoVirtual(decodeManager.getLastMarkerName(), rotacao);
					}
				}
			}

			synchronized (this) {
				busy = false;
			}
		}
	}
	
	public void registerFrame(ByteArrayBuffer byteArrayBuffer, float[] rotacao){
	
		Log.i(TAG, "Setando rotação.");
		synchronized (decodeDTO) {
			decodeDTO.setRotacao(rotacao);
		}

		Log.i(TAG, "Frame recebido.");
		if(!busy){
			Log.e(TAG, "Busy = " + busy);
			this.byteArrayBuffer = byteArrayBuffer;
			this.rotacao = rotacao;
			busy = true;
			
			synchronized (this) {
				this.notify();
			}
		}
		
	}

	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
	
	public DecodeState getDecodeState() {
		return decodeState;
	}

	public void setDecodeState(DecodeState decodeState) {
		this.decodeState = decodeState;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}


	public boolean isStopRequest() {
		return stopRequest;
	}


	public void setStopRequest(boolean stopRequest) {
		this.stopRequest = stopRequest;
	}
	
	
}
