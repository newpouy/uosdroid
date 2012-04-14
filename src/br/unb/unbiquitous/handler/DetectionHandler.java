package br.unb.unbiquitous.handler;

import org.apache.http.util.ByteArrayBuffer;

import nativeLib.NativeLib;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import br.unb.R;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;
import br.unb.unbiquitous.marker.decoder.DecodeState;
import br.unb.unbiquitous.marker.decoder.DecoderObject;

import com.google.droidar.preview.Preview;
import com.google.droidar.util.Log;

/**
 * 
 * @author ricardoandrade
 *
 */
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
	private DecoderObject decoderObject;
	private GLSurfaceView openglView;
	
	private NativeLib nativeLib;
	private ByteArrayBuffer byteArrayBuffer;
	private float[] mat;
	private int frameWidth = 848;
	private int frameHeight = 480;
	
	private Preview preview;
	
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public DetectionHandler(NativeLib nativeLib, 
							DecoderObject decoderObject, 
							GLSurfaceView openglView,
							Preview preview){
		
		this.qrCodeDecodeState = DecodeState.WAITTING;
		this.nativeLib = nativeLib;
		this.decoderObject = decoderObject;
		this.openglView = openglView;
		this.preview = preview;
		
		mat = new float[1 + 18 * 5];
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
			
		case R.id.frame_received:
			Log.i(TAG, "Mensagem recebida: Frame recebido.");
			
			this.byteArrayBuffer = (ByteArrayBuffer) message.obj;
			
			if (decoderObject.getOrientation() != 99 && isMarkerFound() ){
				
				dto = new DecodeDTO();
				dto.setRotacao(mat);
				dto.setByteArrayBuffer(byteArrayBuffer);
				
				synchronized (qrCodeDecodeState) {
					
					// Só envio novas requisições quando a thread estiver pronta
					if ( qrCodeDecodeState.equals(DecodeState.WAITTING) ){
						qrCodeDecodeState = DecodeState.RUNNING;
						enviarMsgDecodificar(dto);
					}
				}
				
				enviarMsgReposicionar(dto);
				
			}
			preview.reAddCallbackBuffer(byteArrayBuffer.toByteArray());
			
			break;
		case R.id.resize_image:
			
			// TODO [Ricardo] Ver se precisa ser implementado pois o tamanho da tela da câmera não é alterada.
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
	

	/**
	 * 
	 */
	private boolean isMarkerFound(){
		
		// Pass the frame to the native code and find the
		// marker information.
		// The false at the end is a remainder of the calibration.
		nativeLib.detectMarkers(byteArrayBuffer.toByteArray(), mat, frameHeight, frameWidth,false, decoderObject.getOrientation());
		
			
		// Needs to be reworked to. Either just deleted, or changed into
		// some timer delay
		openglView.requestRender();
		
		return (int) mat[0] > 0;
		
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