package br.unb.unbiquitous.thread;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.http.util.ByteArrayBuffer;

import nativeLib.NativeLib;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import br.unb.unbiquitous.handler.DetectionHandler;
import br.unb.unbiquitous.manager.ARManager;
import br.unb.unbiquitous.manager.DecodeManager;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.detection.MarkerDetectionSetup;
import br.unb.unbiquitous.marker.detection.UnrecognizedMarkerListener;

import com.google.droidar.gl.MarkerObject;
import com.google.droidar.preview.Preview;
import com.google.droidar.preview.PreviewPost2_0;

/**
 * 
 * @author ricardoandrade
 *
 */
public class DetectionThread extends Thread {
	
	/************************************************
	 * CONSTANTS
	 ************************************************/
	private static final String TAG = DetectionThread.class.getSimpleName();
	
	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private int frameWidth;
	private int frameHeight;
	private byte[] frame = null;
	public boolean busy = false;
	private boolean stopRequest = false;
	private GLSurfaceView openglView;
	private Preview preview;
	private NativeLib nativelib;
	private float[] mat;
	private long start = 0;
	private long now = 0;
	private int fcount = 0;
	private double fps = 0;
	private boolean calcFps = false;
//	private HashMap<String, MarkerObject> markerObjectMap;
//	private UnrecognizedMarkerListener unrecognizedMarkerListener;
	
	/************************************************
	 * VARIABLES - MANAGER
	 ************************************************/
	
	private ARManager arManager;
	private DecodeManager decodeManager;
	
	/************************************************
	 * VARIABLES - DECODING
	 ************************************************/
	
	private DetectionHandler detectionHandler;
	private final CountDownLatch handlerInitLatch;
	
	private DecoderObject decoderObject;
	private MarkerDetectionSetup setup;
	private RepositionMarkerThread repositionThread;
	private DecodeQRCodeThread decodeQRCodeThread;
	private ByteArrayBuffer byteArrayBuffer;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	/**
	 * 
	 */
	public DetectionThread( MarkerDetectionSetup setup, 
							NativeLib nativeLib, 
							GLSurfaceView openglView,
							HashMap<String, MarkerObject> markerObjectMap,
							UnrecognizedMarkerListener unrecognizedMarkerListener,
							DecoderObject decoderObject) {
		
		this.setup = setup;
		this.openglView = openglView;
//		this.markerObjectMap = markerObjectMap;
//		this.unrecognizedMarkerListener = unrecognizedMarkerListener;
		this.nativelib = nativeLib;
		
		this.decoderObject = decoderObject;
		this.decodeManager = new DecodeManager(decoderObject);
		this.arManager = new ARManager(setup, markerObjectMap);
		this.handlerInitLatch = new CountDownLatch(1);

		// TODO make size dynamically after the init function.
		mat = new float[1 + 18 * 5];

//		this.iMotionDetection = new RgbMotionDetection();
		// application will exit even if this thread remains active.
//		setDaemon(true);
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	

	
	
	/**
	 * 
	 */
	@Override
	public synchronized void run() {
		
		/* inicializando as threads */
//		Looper.prepare();

//		detectionHandler = new DetectionHandler(nativelib, decoderObject, openglView, preview);
//		
		repositionThread = new RepositionMarkerThread(arManager);
		repositionThread.start();

		decodeQRCodeThread = new DecodeQRCodeThread(decodeManager, repositionThread,arManager);
		decodeQRCodeThread.start();
//		handlerInitLatch.countDown();
		
//		Looper.loop();
		
		while (true) {
			while (busy == false || stopRequest == true) {
				try {
					wait();// wait for a new frame
				} catch (InterruptedException e) {
				}
			}

			if (stopRequest == true) {
				// do nothing
			} else {
				if (calcFps) {
					// calculate the fps
					if (start == 0) {
						start = SystemClock.uptimeMillis();
					}
					fcount++;
					if (fcount == 30) {
						now = SystemClock.uptimeMillis();
						fps = 30 / ((now - start) / 1000.0);
						// Log.i("AR", "fps:" + fps);
						start = 0;
						fcount = 0;
					}
				}
				boolean qrDecodeBusy = false;
				
//				Log.i(TAG, "Orientation =" +  decoderObject.getOrientation());
				if (decoderObject.getOrientation() != 99 && isMarkerFound() ){
					
//					DecodeDTO decodeDTO = new DecodeDTO();
//					decodeDTO.setFrame(frame);
//					decodeDTO.setRotacao(mat);
//					
//					Message message = Message.obtain(detectionHandler, R.id.marker_found, decodeDTO);
//					message.sendToTarget();
//
//					Log.i(TAG, "Mensagem enviada: Marcador encontrado.");
					
					byteArrayBuffer = new ByteArrayBuffer(this.frame.length);
					byteArrayBuffer.append(frame, 0, frame.length);
					decodeQRCodeThread.registerFrame(byteArrayBuffer, mat);
					
					
//					decodeManager.isQRCodeFound(frame, frameWidth, frameHeight);
				
				}
				
				busy = false;
				//((PreviewPost2_0)preview).reAddCallbackBuffer(frame);
				((PreviewPost2_0)preview).reAddCallbackBufferFocus(frame,!decodeQRCodeThread.isBusy());
			}

			yield();
		}

	}
	
	/**
	 * 
	 * @param data
	 */
	public synchronized void nextFrame(byte[] data) {
		if (busy == false /* this.getState() == Thread.State.WAITING */) {
			// ok, we are ready for a new frame:
			busy = true;
			this.frame = data;
			// do the work:
			synchronized (this) {
				this.notify();
			}
		} else {
			// the Thread is busy, we just ignore the frame and go on.
		}
	}

	/**
	 * 
	 * @param height
	 * @param width
	 */
	public void setImageSizeAndRun(int height, int width) {
		frameHeight = height;
		frameWidth = width;

		if (stopRequest == true) {
			// this means the thread is active, no starting is needed,
			// just reset the flag.
			stopRequest = false;
		} else {
			// this is a new thread.
			super.start();
		}

	}

	/**
	 * 
	 */
	public void stopThread() {
		stopRequest = true;
	}

	
	/************************************************
	 * PRIVATE METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	private boolean isMarkerFound(){
		
		// Pass the frame to the native code and find the
		// marker information.
		// The false at the end is a remainder of the calibration.
		nativelib.detectMarkers(frame, mat, frameHeight, frameWidth,false, decoderObject.getOrientation());
		
			
		// Needs to be reworked to. Either just deleted, or changed into
		// some timer delay
		openglView.requestRender();
		
		return (int) mat[0] > 0;
		
	}
	
	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
	
	public double getFps() {
		return fps;
	}

	public void setPreview(Preview preview) {
		this.preview = preview;
	}

	public void calculateFrameRate() {
		calcFps = true;
	}

//	public void setMarkerObjectMap(HashMap<String, MarkerObject> markerObjectMap) {
//		this.markerObjectMap = markerObjectMap;
//	}

}
