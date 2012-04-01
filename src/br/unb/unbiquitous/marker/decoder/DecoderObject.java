package br.unb.unbiquitous.marker.decoder;

import android.app.Activity;

import com.google.zxing.client.android.camera.CameraManager;

public class DecoderObject {

	private CameraManager cameraManager;
	private QRCodeDecoder qrCodeDecoder;
	private Integer orientation ;

	public DecoderObject(Activity activity) {
		cameraManager = new CameraManager(activity.getApplication());
		qrCodeDecoder = new QRCodeDecoder(cameraManager);
		orientation = new Integer(0);
	}

	public Integer getOrientation() {
		return orientation;
	}

	public void setOrientation(Integer rotation) {
		this.orientation = rotation;
	}

	public QRCodeDecoder getQrCodeDecoder() {
		return qrCodeDecoder;
	}

	public void setQrCodeDecoder(QRCodeDecoder qrCodeDecoder) {
		this.qrCodeDecoder = qrCodeDecoder;
	}

	
}
