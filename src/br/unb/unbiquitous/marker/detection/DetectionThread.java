package br.unb.unbiquitous.marker.detection;

import java.util.HashMap;
import java.util.Map;

import nativeLib.NativeLib;

import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;
import br.unb.ApplicationsFake;
import br.unb.QRCodeDecoder;

import com.google.droidar.gl.MarkerObject;
import com.google.droidar.preview.Preview;

public class DetectionThread extends Thread {
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
	private HashMap<Integer, MarkerObject> markerObjectMap;
	private UnrecognizedMarkerListener unrecognizedMarkerListener;
	
	private Map<String, ApplicationsFake> mapa;
	
	
	private QRCodeDecoder qrCodeDecoder;

	public DetectionThread(NativeLib nativeLib, GLSurfaceView openglView,
			HashMap<Integer, MarkerObject> markerObjectMap,
			UnrecognizedMarkerListener unrecognizedMarkerListener,
			QRCodeDecoder qrCodeDecoder) {
		this.openglView = openglView;
		this.markerObjectMap = markerObjectMap;
		this.nativelib = nativeLib;
		this.unrecognizedMarkerListener = unrecognizedMarkerListener;
		
		
		this.qrCodeDecoder = qrCodeDecoder;

		// TODO make size dynamically after the init function.
		mat = new float[1 + 18 * 5];

		// application will exit even if this thread remains active.
		setDaemon(true);
		
		
		// TODO [Ricardo] arrumar isso aqui
		
		ApplicationsFake applicationsFake = new ApplicationsFake();
		applicationsFake.setId(0);
		applicationsFake.setNome("TesteApp");

		mapa = new HashMap<String, ApplicationsFake>();
		mapa.put(applicationsFake.getNome(), applicationsFake);
		
		
	}

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
				// Pass the frame to the native code and find the
				// marker information.
				// The false at the end is a remainder of the calibration.
				nativelib.detectMarkers(frame, mat, frameHeight, frameWidth,
						false);

				// Needs to be reworked to. Either just deleted, or changed into
				// some timer delay
				openglView.requestRender();

				// Write all current information of the detected markers into
				// the marker hashmap and notify markers they are recognized.
				int posicaoInicialMarcador, posicaoFinalMarcador, rotacaoMarcador, idMarcador = 0;

				
				/* ******************************************************** 
				 * A partir daqui o c�digo original foi comentado 
				 * ********************************************************/

				// Verifica se achou algum marcador 
				// estou considerando que s� ter� um marcador por frame
				// TODO [Ricardo] Arrumar isso para v�rios marcadores
				if((int) mat[0] > 0){
					posicaoInicialMarcador = (1);
					posicaoFinalMarcador = posicaoInicialMarcador + 15;
					rotacaoMarcador = posicaoFinalMarcador + 1;
					idMarcador = rotacaoMarcador + 1;
					
					// FIXME Importante
					// Por enquanto o idMarcador = 0, definido no myOpenDVDetection.c
					// A ideia aqui � decodificar o QRCode atribuir a uma string, posteriormente
					// buscar esse nome em um mapa contendo o nome de todas aplica��es que est�o
					// presentes no ambiente.
					
					//TODO implementar
					// Decodificando o QRCode
					
					// -------------------------------------------------------------

//					qrCodeDecoder.decode(frame, frameWidth, frameHeight);
					
//					if(qrCodeDecoder.getTextDecoded() != null){
//						Log.i("DetectionThread", "Código decodificado = "+ qrCodeDecoder.getTextDecoded());
//					}else{
//						Log.i("DetectionThread", "Não foi possível decodificar o marcador.");
//					}
					
					
					// -------------------------------------------------------------
					
					
					/*
					String textoDecodificado = "TesteApp";
					
					mapa.get(textoDecodificado);
					
					MarkerObject markerObj = markerObjectMap.get((int) mat[idMarcador]);

					Log.i("PosicaoMarcador", "Posicao inicial = "+ posicaoInicialMarcador + ", final = "+ posicaoFinalMarcador);
					
					if (markerObj != null) {
						// Marcador n�o foi encontrado
						markerObj.OnMarkerPositionRecognized(mat, posicaoInicialMarcador,posicaoFinalMarcador);
					} else {
						
						// Marcador foi detectado
						if (unrecognizedMarkerListener != null) {
							
							
							unrecognizedMarkerListener.onUnrecognizedMarkerDetected(
																						(int) mat[idMarcador], 
																						mat, 
																						posicaoInicialMarcador,
																						posicaoFinalMarcador, 
																						(int) mat[rotacaoMarcador]
															          				);
						}
					}
					
					*/
					 
				}
				
				/*
				for (int i = 0; i < (int) mat[0]; i++) {
					startIdx = (1 + i * 18);
					endIdx = startIdx + 15;
					rotIdx = endIdx + 1;
					idIdx = rotIdx + 1;
					
					// Log.d(LOG_TAG, "StartIdx");

					MarkerObject markerObj = markerObjectMap
							.get((int) mat[idIdx]);

					if (markerObj != null) {
						markerObj.OnMarkerPositionRecognized(mat, startIdx,
								endIdx);
					} else {
						if (unrecognizedMarkerListener != null) {
							unrecognizedMarkerListener
									.onUnrecognizedMarkerDetected(
											(int) mat[idIdx], mat, startIdx,
											endIdx, (int) mat[rotIdx]);
						}
					}
				}
				*/
				
				
				
				
				busy = false;
				preview.reAddCallbackBuffer(frame);
			}

			yield();
		}

	}

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

	public void stopThread() {
		stopRequest = true;
	}

	public double getFps() {
		return fps;
	}

	public void setPreview(Preview preview) {
		this.preview = preview;
	}

	public void calculateFrameRate() {
		calcFps = true;
	}

	public void setMarkerObjectMap(
			HashMap<Integer, MarkerObject> markerObjectMap) {
		this.markerObjectMap = markerObjectMap;
	}
}
