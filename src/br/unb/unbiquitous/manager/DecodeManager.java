package br.unb.unbiquitous.manager;

import java.util.Calendar;


import android.util.Log;
import br.unb.unbiquitous.jni.ZbarJni;
import br.unb.unbiquitous.marker.decoder.DecoderObject;

/**
 * 
 * @author ricardoandrade
 *
 */
public class DecodeManager {

	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private DecoderObject decoderObject;
	private String lastMarkerName;
	private boolean newMarker;
	private static final String TAG = "DecodeManager";
	private ZbarJni zbar = new ZbarJni();

	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	/**
	 * 
	 */
	public DecodeManager(DecoderObject decoderObject){
		this.decoderObject = decoderObject;
	}
	
	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * Método responsável por retornar se um QRCode foi encontrado para o frame 
	 * informado.
	 */
	public boolean isQRCodeFound(byte [] frame, int frameWidth, int frameHeight){
		

		// Decodificando o QRCode
		Log.i(TAG, "Decodificando o QRCode.");

//		Calendar inicio = Calendar.getInstance();
//
//		decoderObject.getQrCodeDecoder().decode(frame, frameWidth, frameHeight);
//		
//		Calendar fim = Calendar.getInstance();
//		
//		long tempoTotalDecodificacao = fim.getTimeInMillis() - inicio.getTimeInMillis();
//		
//		Log.i(TAG, "Tempo de decodificação: " + tempoTotalDecodificacao + " ms.");
//		
//		if(decoderObject.getQrCodeDecoder().getTextDecoded() != null){
//			lastMarkerName = decoderObject.getQrCodeDecoder().getTextDecoded();
//			Log.i(TAG, "Código decodificado = "+ decoderObject.getQrCodeDecoder().getTextDecoded());
//		}else{
//			Log.i(TAG, "Não foi possível decodificar o marcador.");
//		}
//		
//		return decoderObject.getQrCodeDecoder().getTextDecoded() != null;
		
		
		return zbarDecode(frame, frameWidth, frameHeight) != null;
		
	}
	


	/************************************************
	 * PRIVATE METHODS
	 ************************************************/

	private String zbarDecode(byte [] frame, int frameWidth, int frameHeight){
		Calendar inicio = Calendar.getInstance();

		String texto = zbar.decode(frameWidth, frameHeight, frame);
		
		Calendar fim = Calendar.getInstance();
		
		long tempoTotalDecodificacao = fim.getTimeInMillis() - inicio.getTimeInMillis();
		
		Log.i(TAG, "Tempo de decodificação: " + tempoTotalDecodificacao + " ms.");
		
		if(texto != null){
			lastMarkerName = texto;
			Log.i(TAG, "Código decodificado = "+ texto + ", em " + tempoTotalDecodificacao + " ms.");
		}else{
			Log.i(TAG, "Não foi possível decodificar o marcador." + ", em " + tempoTotalDecodificacao + " ms.");
		}
		
		return texto;
		
	}
	
	// TODO retirar isso depois
	/**
	 * Método responsável por comparar se o último QRCode detectado corresponde a um 
	 * novo QRCode.
	 * @return
	 */
	private boolean isNewMarkerDecoded(){
		
		if( ( lastMarkerName == null && decoderObject.getQrCodeDecoder().getTextDecoded() != null ) || 
				( lastMarkerName != null && lastMarkerName.equals(decoderObject.getQrCodeDecoder().getTextDecoded()))){
			
			Log.i(TAG, "Novo marcador encontrado.");
			
			lastMarkerName = decoderObject.getQrCodeDecoder().getTextDecoded();
			return true;			
		}
		
		return false; 
	}

	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/

	public boolean isNewMarker(){
		return this.newMarker;
	}

	public String getLastMarkerName() {
		return lastMarkerName;
	}

}
