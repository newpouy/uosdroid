package br.unb.unbiquitous.marker.detection;

import java.util.HashMap;

import nativeLib.NativeLib;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;
import br.unb.manager.ar.ARManager;
import br.unb.manager.decode.DecodeManager;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.decoder.RepositionThread;

import com.google.droidar.gl.MarkerObject;
import com.google.droidar.preview.Preview;

public class DetectionThread extends Thread {
	
	private static final String TAG = "DetectionThread";
	
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
	private HashMap<String, MarkerObject> markerObjectMap;
	private UnrecognizedMarkerListener unrecognizedMarkerListener;
	
	/************************************************
	 * VARIABLES - DETECTING
	 ************************************************/
//	private IMotionDetection iMotionDetection;
	
	/************************************************
	 * VARIABLES - AUGMENTED REALITY
	 ************************************************/
	
	private ARManager arManager;
	
	/************************************************
	 * VARIABLES - DECODING
	 ************************************************/
	
	private DecoderObject decoderObject;
	private boolean isRepositionThreadRunning = false;
	
	private DecodeManager decodeManager;
	private RepositionThread repositionThread;
	private boolean decoderThreadRunning = false;
	
	
	int tentativas = 0;
	private MarkerDetectionSetup setup;
	
	
	
	private static final int MAX_TENTATIVAS = 10000;
	
	private int[] rgb;

	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	/**
	 * 
	 */
	public DetectionThread( MarkerDetectionSetup setup, NativeLib nativeLib, GLSurfaceView openglView,
			HashMap<String, MarkerObject> markerObjectMap,
			UnrecognizedMarkerListener unrecognizedMarkerListener,
			DecoderObject decoderObject) {
		
		this.setup = setup;
		this.openglView = openglView;
		this.markerObjectMap = markerObjectMap;
		this.nativelib = nativeLib;
		this.unrecognizedMarkerListener = unrecognizedMarkerListener;
		
		this.decoderObject = decoderObject;
		this.decodeManager = new DecodeManager(decoderObject);
		this.arManager = new ARManager(setup, markerObjectMap);

		this.repositionThread = new RepositionThread(arManager, decodeManager);

		// TODO make size dynamically after the init function.
		mat = new float[1 + 18 * 5];
		
		
		
//		this.iMotionDetection = new RgbMotionDetection();
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
				
				Log.i(TAG, "Orientation =" +  decoderObject.getOrientation());
				if (decoderObject.getOrientation() != 99 && isMarkerFound() ){
					// Achou um marcaodr
					Log.i(TAG, "Achou um marcador.");
					
					// Quando decodificar o primeiro qrcode, starta a thread de reposicao
					if ( !isRepositionThreadRunning){
						repositionThread.setFrameHeight(frameHeight);
						repositionThread.setFrameWidth(frameWidth);
						repositionThread.setDecodificar(false);
						repositionThread.setPriority(MAX_PRIORITY);
						repositionThread.start();
						isRepositionThreadRunning = true;
					}

					synchronized (repositionThread) {
						repositionThread.setFrame(frame);
						repositionThread.setRotacao(mat);
						repositionThread.setDecodificar(true);
						repositionThread.notify();
					}
					
					// Já achou um marcador
					if (decodeManager.getLastMarkerName() != null){
						arManager.reposicionarObjetoVirtual(decodeManager.getLastMarkerName(), mat);
					}
						
						
				}else{
					// Nao achou nenhum marcador.
					repositionThread.setDecodificar(false);
				}
				
				busy = false;
				preview.reAddCallbackBuffer(frame);
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

	public void setMarkerObjectMap(HashMap<String, MarkerObject> markerObjectMap) {
		this.markerObjectMap = markerObjectMap;
	}

}
