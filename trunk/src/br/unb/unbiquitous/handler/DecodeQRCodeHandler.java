package br.unb.unbiquitous.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import br.unb.R;
import br.unb.unbiquitous.manager.DecodeManager;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;

/**
 * 
 * @author ricardoandrade
 *
 */
final class DecodeQRCodeHandler extends Handler {
	
	/************************************************
	 * CONSTANTS
	 ************************************************/
	private static final String TAG = DecodeQRCodeHandler.class.getSimpleName();
	private static final int FRAME_WIDTH = 840;
	private static final int FRAME_HEIGHT = 484;
	
	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private DecodeManager decodeManager;
	private DetectionHandler detectionHandler;

	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public DecodeQRCodeHandler(DetectionHandler detectionHandler, DecodeManager decodeManager) {
		this.decodeManager = decodeManager;
		this.detectionHandler = detectionHandler;
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.decode:
			
			Log.i(TAG, "Mesagem recebida: Decodificar o QRCode.");
			
			decode(((DecodeDTO) message.obj).getFrame());
			break;
		}
	}

	/************************************************
	 * PRIVATE METHODS
	 ************************************************/

	/**
	 * 
	 */
	private void decode(byte[] frame) {
		long start = System.currentTimeMillis();

		if (decodeManager.isQRCodeFound(frame, FRAME_WIDTH, FRAME_HEIGHT)) {

			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode in " + (end - start) + " ms");
			
			DecodeDTO dto = new DecodeDTO();
			dto.setAppName(decodeManager.getLastMarkerName());

			Message message = Message.obtain(detectionHandler,	R.id.qrcode_decoded_succeeded, dto);
			message.sendToTarget();
			
		} else {
			Message message = Message.obtain(detectionHandler, R.id.qrcode_decode_failed);
			message.sendToTarget();
		}
	}

}
