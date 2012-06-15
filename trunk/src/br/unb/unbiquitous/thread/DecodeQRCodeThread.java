package br.unb.unbiquitous.thread;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;
import br.unb.unbiquitous.manager.ARManager;
import br.unb.unbiquitous.manager.DecodeManager;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;
import br.unb.unbiquitous.util.DecodeProgram;
import br.unb.unbiquitous.util.Medicao;

/**
 * Thread responsável pela decodificação do QRCode.
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
	              
	private RepositionMarkerThread repositionMarkerThread;
	private ARManager arManager;
	
	private Medicao medicao = new Medicao();

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
	 * Método invocado quando a thread é inicializada.
	 */
	@Override
	public synchronized void run() {
		
		decodeDTO = repositionMarkerThread.getDecodeDTO();
		
		while(!stopRequest){
			while(!busy){
				try {
					wait(); // esperando pelo novo frame
				} catch (InterruptedException e) {}
				
				Log.i(TAG, "Frame recebido: Decodificando QRCode.");
				
				medicao.start();
				
				// Tentando decodificar o QRCode.
				if(decodeManager.isQRCodeFound(byteArrayBuffer.toByteArray(), FRAME_WIDTH, FRAME_HEIGHT, DecodeProgram.ZBAR)){
					
					medicao.stop();
					Log.i(TAG, "Frame recebido: QRCode decodificado com sucesso.");
					
					synchronized (decodeDTO) {
						
						decodeDTO.setAppName(decodeManager.getLastMarkerName());
						decodeDTO.setRotacao(rotacao);
						
						arManager.inserirObjetoVirtual(decodeManager.getLastMarkerName(), rotacao);
					}
				}else{
					decodeManager.getDecoderObject().getMedicoes().add(medicao);
					medicao = new Medicao();
					arManager.retirarObjetosVirtuais();
				}
			}

			synchronized (this) {
				busy = false;
			}
		}
	}
	
	/**
	 * Método responsável por receber o frame contendo o marcador detectado 
	 * pela DetectionThread. Seta os dados necessários para a decodificação 
	 * e aciona esta thread para fazer uma nova detecção.
	 * 
	 * 
	 * @param byteArrayBuffer
	 * @param rotacao
	 */
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