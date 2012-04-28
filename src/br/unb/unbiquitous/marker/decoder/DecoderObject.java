package br.unb.unbiquitous.marker.decoder;

import com.google.zxing.client.android.camera.CameraManager;

import br.unb.unbiquitous.jni.MarkerDetectionJni;
import br.unb.unbiquitous.jni.ZbarJni;
import android.app.Activity;

/**
 * Essa classe é responsável por disponibilizar objetos
 * usados na decodificação de marcadores e QRCodes.
 * 
 * @author ricardoandrade
 *
 */
public class DecoderObject {

	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private Integer orientation;
	private ZbarJni zbar ;
	private MarkerDetectionJni markerDetection;
	private QRCodeDecoder qrCodeDecoder;
	
	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
	
	public DecoderObject(Activity activity) {
		qrCodeDecoder = new QRCodeDecoder(new CameraManager(activity.getApplication()));
		orientation = new Integer(0);
		markerDetection = new MarkerDetectionJni();
		zbar  = new ZbarJni();
	}

	public Integer getOrientation() {
		return orientation;
	}

	public void setOrientation(Integer rotation) {
		this.orientation = rotation;
	}

	public ZbarJni getZbar() {
		return zbar;
	}

	public void setZbar(ZbarJni zbar) {
		this.zbar = zbar;
	}

	public MarkerDetectionJni getMarkerDetection() {
		return markerDetection;
	}

	public void setMarkerDetection(MarkerDetectionJni markerDetection) {
		this.markerDetection = markerDetection;
	}

	public QRCodeDecoder getQrCodeDecoder() {
		return qrCodeDecoder;
	}

	public void setQrCodeDecoder(QRCodeDecoder qrCodeDecoder) {
		this.qrCodeDecoder = qrCodeDecoder;
	}
	
}
