package br.unb.unbiquitous.manager;

import java.util.Calendar;

import android.util.Log;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.util.DecodeProgram;

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
	private static final String TAG = "DecodeManager";
	private String lastMarkerName;

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
	 * M√©todo respons√°vel por retornar se um QRCode foi encontrado para o frame 
	 * informado.
	 */
	public boolean isQRCodeFound(byte [] frame, int frameWidth, int frameHeight, DecodeProgram decodeProgram){
		
		String texto;

		// Decodificando o QRCode
		Log.i(TAG, "Decodificando o QRCode.");

		Calendar inicio = Calendar.getInstance();

		if(decodeProgram.equals(DecodeProgram.ZXING)){
			texto = decoderObject.getQrCodeDecoder().decode(frame, frameWidth, frameHeight).getText();
		}else{
			texto = decoderObject.getZbar().decode(frameWidth, frameHeight, frame);
		}
		
		Calendar fim = Calendar.getInstance();
		
		long tempoTotalDecodificacao = fim.getTimeInMillis() - inicio.getTimeInMillis();
		
		if(texto != null){
			lastMarkerName = texto;
			Log.i(TAG, "Código decodificado = "+ texto + ". Tempo de decodificação = " + tempoTotalDecodificacao + " ms.");
		}else{
			Log.i(TAG, "Não foi possível decodificar o marcador. Tempo de decodificacao = " + tempoTotalDecodificacao + " ms.");
		}

		return texto != null;
	}
	
	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/

	public String getLastMarkerName() {
		return lastMarkerName;
	}
	


	
	
}
