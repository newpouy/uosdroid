package br.unb.unbiquitous.marker.decoder;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.android.PlanarYUVLuminanceSource;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeDecoder {

	private final MultiFormatReader multiFormatReader;
	private boolean running = true;
	private CameraManager cameraManager;

	private String textDecoded;

	public QRCodeDecoder(CameraManager cameraManager) {
		multiFormatReader = new MultiFormatReader();
		this.cameraManager = cameraManager;
	}

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * 
	 * @param data
	 *            The YUV preview frame.
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	public Result decode(byte[] data, int width, int height) {
		Result rawResult = null;
		PlanarYUVLuminanceSource source = cameraManager.buildLuminanceSource(
				data, width, height);
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decode(bitmap);
				textDecoded = rawResult.getText();
				return rawResult;
			} catch (ReaderException re) {
				// continue
			} finally {
				multiFormatReader.reset();
			}
		}
		return null;
	}

	public String getTextDecoded() {
		return textDecoded;
	}

	
}
