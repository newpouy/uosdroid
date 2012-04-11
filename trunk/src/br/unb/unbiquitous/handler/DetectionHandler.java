package br.unb.unbiquitous.handler;

import android.os.Handler;
import android.os.Message;
import br.unb.R;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;
import br.unb.unbiquitous.marker.decoder.DecodeState;

import com.google.droidar.util.Log;

public class DetectionHandler extends Handler {

	/************************************************
	 * CONSTANTS
	 ************************************************/
	private static final String TAG = DecodeQRCodeHandler.class.getSimpleName(); 
	
	/************************************************
	 * VARIABLES
	 ************************************************/

	private DecodeState qrCodeDecodeState;
	
	private DecodeQRCodeHandler decodeHandler;
	private RepositionMarkerHandler repositionHandler;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public DetectionHandler(){
		qrCodeDecodeState = DecodeState.WAITTING;
	}
	
	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	@Override
	public synchronized void handleMessage(Message message) {
		DecodeDTO dto;
		switch (message.what) {
		
		case R.id.marker_found:
			
			Log.i(TAG, "Mensagem recebida: Marcador encontrado.");
			
			dto = (DecodeDTO) message.obj;
			
			synchronized (qrCodeDecodeState) {
				
				// Só envio novas requisições quando a thread estiver pronta
				if ( qrCodeDecodeState.equals(DecodeState.WAITTING) ){
					qrCodeDecodeState = DecodeState.RUNNING;
					enviarMsgDecodificar(dto);
				}
			}
			
			enviarMsgReposicionar(dto);
			break;
		case R.id.qrcode_decoded_succeeded:
			
			Log.i(TAG, "Mensagem recebida: QRCode decodificado com sucesso.");
			
			synchronized (qrCodeDecodeState) {
				// QRCode decodificado com sucesso
				qrCodeDecodeState = DecodeState.WAITTING;
			}
			
			dto = (DecodeDTO) message.obj;
			enviarMsgCriar(dto);
			break;
		case R.id.qrcode_decode_failed:
			Log.i(TAG, "Mensagem recebida: Falha na decodificação do QRCode .");

			synchronized (qrCodeDecodeState) {
				qrCodeDecodeState = DecodeState.WAITTING;
			}
			
			break;
		}
		
		
	}
	
	/************************************************
	 * PRIVATE METHODS
	 ************************************************/
	
	/**
	 * 
	 * @param dto
	 */
	private void enviarMsgDecodificar(DecodeDTO dto){
		if (decodeHandler == null) return;
		
		Message message = Message.obtain(decodeHandler, R.id.decode, dto);
        message.sendToTarget();

        Log.i(TAG, "Mensagem enviada: Decodificar o QRCode.");
	}
	
	
	/**
	 * 
	 * @param dto
	 */
	private void enviarMsgReposicionar(DecodeDTO dto){
		if (repositionHandler == null) return;

		Message message = Message.obtain(repositionHandler, R.id.reposition, dto);
        message.sendToTarget();

        Log.i(TAG, "Mensagem enviada: Reposicionar o objeto.");
	}
	
	/**
	 * 
	 * @param dto
	 */
	private void enviarMsgCriar(DecodeDTO dto){
		if (repositionHandler == null) return;
		
		Message message = Message.obtain(repositionHandler, R.id.create, dto);
		message.sendToTarget();

		Log.i(TAG, "Mensagem enviada: Criar novo objeto.");
	}
	
	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/

	public DecodeQRCodeHandler getDecodeHandler() {
		return decodeHandler;
	}

	public void setDecodeHandler(DecodeQRCodeHandler decodeHandler) {
		this.decodeHandler = decodeHandler;
	}

	public RepositionMarkerHandler getRepositionHandler() {
		return repositionHandler;
	}

	public void setRepositionHandler(RepositionMarkerHandler repositionHandler) {
		this.repositionHandler = repositionHandler;
	}
	
}
