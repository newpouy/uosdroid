package br.unb.unbiquitous.thread;

import java.util.HashMap;

import org.apache.http.util.ByteArrayBuffer;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
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
	private float[] mat;
	private long start = 0;
	private long now = 0;
	private int fcount = 0;
	private double fps = 0;
	private boolean calcFps = false;
	
	/************************************************
	 * VARIABLES - MANAGER
	 ************************************************/
	
	private ARManager arManager;
	private DecodeManager decodeManager;
	
	/************************************************
	 * VARIABLES - DECODING
	 ************************************************/
	
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
							GLSurfaceView openglView,
							HashMap<String, MarkerObject> markerObjectMap,
							UnrecognizedMarkerListener unrecognizedMarkerListener,
							DecoderObject decoderObject) {
		
		this.setup = setup;
		this.openglView = openglView;
		
		this.decoderObject = decoderObject;
		this.decodeManager = new DecodeManager(decoderObject);
		this.arManager = new ARManager(setup, markerObjectMap);
		
		repositionThread = new RepositionMarkerThread(arManager);
		decodeQRCodeThread = new DecodeQRCodeThread(decodeManager, repositionThread,arManager);

		mat = new float[1 + 18 * 5];

		// application will exit even if this thread remains active.
		setDaemon(true);
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	@Override
	public synchronized void run() {
		while (true) {
			while (!busy || stopRequest) {
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
				
				if (decoderObject.getOrientation() != 99 && isMarkerFound() ){
					byteArrayBuffer = new ByteArrayBuffer(this.frame.length);
					byteArrayBuffer.append(frame, 0, frame.length);
					decodeQRCodeThread.registerFrame(byteArrayBuffer, mat);
				}
				
				busy = false;
				((PreviewPost2_0)preview).reAddCallbackBuffer(frame);
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
			repositionThread.setStopRequest(false);
			decodeQRCodeThread.setStopRequest(false);
		} else {
			// this is a new thread.
			super.start();
			
			repositionThread.start();
			decodeQRCodeThread.start();
		}

	}

	/**
	 * 
	 */
	public void stopThread() {
		stopRequest = true;
		repositionThread.setStopRequest(true);
		decodeQRCodeThread.setStopRequest(true);
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
		decoderObject.getMarkerDetection().detectMarkers(frame, mat, frameHeight, frameWidth,false, decoderObject.getOrientation());
		
			
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
