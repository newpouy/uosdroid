package br.unb.unbiquitous.marker.detection;


import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import br.unb.unbiquitous.jni.MarkerDetectionJni;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.thread.DetectionThread;

import com.google.droidar.preview.Preview;
import com.google.droidar.preview.PreviewPost2_0;
import com.google.droidar.system.Setup;
import com.google.droidar.util.CameraCalibration;
import com.google.droidar.util.IO;

/**
 * 
 * @author ricardoandrade
 *
 */
public abstract class MarkerDetectionSetup extends Setup {

	/************************************************************
	 * VARIABLES
	 ************************************************************/
	
	private final String CALIB_PATH = "ARCameraCalibration";
	private Preview cameraPreview;
	private DetectionThread myThread;
	private MarkerDetectionJni nativeLib = new MarkerDetectionJni();
	
	private CameraCalibration calib = null;
	private Camera.Size cameraSize;
	private LayoutParams optimalLayoutParams;
	public  Activity activity;
	private DecoderObject decoderObject;
	protected MarkerObjectMap markerObjectMap;
	
	/************************************************************
	 * ABSTRACT METHODS
	 ************************************************************/

	public abstract UnrecognizedMarkerListener getUnrecognizedMarkerListener();
	public abstract void _a3_registerMarkerObjects(MarkerObjectMap markerObjectMap);
	
	/************************************************************
	 * PUBLIC METHODS
	 ************************************************************/
	
	@Override
	public void initializeCamera() {

		markerObjectMap = new MarkerObjectMap();
		
		Camera mCamera = Camera.open();

		Camera.Parameters parameters = mCamera.getParameters();
		cameraSize = parameters.getPreviewSize(); 
	
		optimalLayoutParams = new LayoutParams(cameraSize.width, cameraSize.height);

		mCamera.release();
		tryToLoadCameraSettings();

		// initialize native code.
		int[] constants = new int[2];
		
		nativeLib.initThread(constants, calib.cameraMatrix,	calib.distortionMatrix);

		myThread = new DetectionThread(this, nativeLib, myGLSurfaceView,markerObjectMap, getUnrecognizedMarkerListener(), this.decoderObject);
		
		cameraPreview = new PreviewPost2_0(myTargetActivity, myThread,cameraSize);

		// TODO [ Ricardo ] Isso aqui poderá sair pois o controle para visualizacao sera feito no detection thread.
		_a3_registerMarkerObjects(markerObjectMap);

	}

	@Override
	public void addCameraOverlay() {
		myTargetActivity.addContentView(cameraPreview, optimalLayoutParams);
	}

	@Override
	public void addGLSurfaceOverlay() {
		myTargetActivity.addContentView(myGLSurfaceView, optimalLayoutParams);
	}
	
	@Override
	public void onDestroy(Activity a) {
		super.onDestroy(a);
		if (cameraPreview != null)
			cameraPreview.releaseCamera();
		// Ensure app is gone after back button is pressed!
		if (myThread != null)
			myThread.stopThread();
	}

	@Override
	public void releaseCamera() {
		super.releaseCamera();
		if (cameraPreview != null)
			cameraPreview.releaseCamera();
	}
	
	/************************************************************
	 * PRIVATE METHODS
	 ************************************************************/
	

	private void tryToLoadCameraSettings() {
		// Load previously stored calibrations
		SharedPreferences settings = myTargetActivity.getSharedPreferences(CALIB_PATH, 0);
		String fileName = settings.getString("calibration", null);
		if (fileName != null) {
			try {
				calib = (CameraCalibration) IO.loadSerializable("/sdcard/"
						+ CALIB_PATH + "/" + fileName + ".txt");
				Log.d("AR", "using old calibration");
			} catch (Exception e) {
				Log.d("AR", "default value, file is empty");
				calib = CameraCalibration.defaultCalib(cameraSize.width,
						cameraSize.height);
			}
		} else {
			Log.d("AR", "default value, file not found");
			calib = CameraCalibration.defaultCalib(cameraSize.width,
					cameraSize.height);
		}
	}
	
	/************************************************************
	 * GETTERS AND SETTERS
	 ************************************************************/
	
	public DecoderObject getDecoderObject() {
		return decoderObject;
	}

	public void setDecoderObject(DecoderObject decoderObject) {
		this.decoderObject = decoderObject;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Preview getCameraPreview() {
		return cameraPreview;
	}

	public void setCameraPreview(Preview cameraPreview) {
		this.cameraPreview = cameraPreview;
	}

	public MarkerDetectionJni getNativeLib() {
		return nativeLib;
	}

	public void setNativeLib(MarkerDetectionJni nativeLib) {
		this.nativeLib = nativeLib;
	}

	public Camera.Size getCameraSize() {
		return cameraSize;
	}

	public void setCameraSize(Camera.Size cameraSize) {
		this.cameraSize = cameraSize;
	}

	public LayoutParams getOptimalLayoutParams() {
		return optimalLayoutParams;
	}

	public void setOptimalLayoutParams(LayoutParams optimalLayoutParams) {
		this.optimalLayoutParams = optimalLayoutParams;
	}
	
}
