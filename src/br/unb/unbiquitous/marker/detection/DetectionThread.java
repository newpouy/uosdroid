package br.unb.unbiquitous.marker.detection;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import nativeLib.NativeLib;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;
import br.unb.ApplicationsFake;
import br.unb.DecoderObject;

import com.google.droidar.gl.MarkerObject;
import com.google.droidar.preview.Preview;
import com.google.zxing.Result;
import com.jwetherell.motion_detection.detection.IMotionDetection;
import com.jwetherell.motion_detection.detection.RgbMotionDetection;
import com.jwetherell.motion_detection.image.ImageProcessing;

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
	
	private DecoderObject decoderObject;
	private IMotionDetection iMotionDetection;
	private boolean flagControleMovimento = false;
	private String lastAppName;
	int tentativas = 0;
	
	
	private static final int MAX_TENTATIVAS = 50;
	
	private int[] rgb;

	public DetectionThread(NativeLib nativeLib, GLSurfaceView openglView,
			HashMap<Integer, MarkerObject> markerObjectMap,
			UnrecognizedMarkerListener unrecognizedMarkerListener,
			DecoderObject decoderObject) {
		this.openglView = openglView;
		this.markerObjectMap = markerObjectMap;
		this.nativelib = nativeLib;
		this.unrecognizedMarkerListener = unrecognizedMarkerListener;
		
		this.decoderObject = decoderObject;

		// TODO make size dynamically after the init function.
		mat = new float[1 + 18 * 5];
		
//		this.iMotionDetection = new RgbMotionDetection();
		// application will exit even if this thread remains active.
		setDaemon(true);
		// TODO [Ricardo] arrumar isso aqui
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
				
				
				isMarkerFound();
				
				// TODO [Ricardo] arrumar o esquema da porcentagem para calibrar.
				
				// Se entrar aqui é porque um marcador foi encontrado.
//				if (flagControleMovimento ){// && isMotionDetected()){
//					
//					Log.i("DetectionThread", "Um marcador foi encontrado e houve uma deteccao de movimento.");
//					
//					// Ate no máximo o número de tentativas
//					if (tentativas > MAX_TENTATIVAS){
//						
//						Log.i("DetectionThread", "Atingido as tentativas máximas");
//						tentativas = 0;
//						flagControleMovimento = false;
//					}else{
//
//						tentativas++;
//					
//						// Achou um marcador, mas não conseguiu decoficar.
//						if(lastAppName == null){
//							
//							Log.i("DetectionThread", "Achou um marcador, mas nao conseguiu decodificar.");
//							
//							// Tenta decodificar o qrcode
//							if(isQRCodeFound()){
//								
//								Log.i("DetectionThread", "conseguiu decodificar o marcador.");
//								this.lastAppName = decoderObject.getQrCodeDecoder().getTextDecoded();
//								controlarRecursosRealidadeAumentada();
//							}
//						}else{
//						
//							
//							// Achou um marcador e conseguiu decodificar o qrcode.
//							// Agora precisa achar as posicoes do marcador para realinhar
//							// o objeto virtual.
//							if(isMarkerFound()){
//								
//								Log.i("DetectionThread", "Achou um marcador.");
//								
//								controlarRecursosRealidadeAumentada();
//							}
//						}
//					}
//					
//				}else{
//					
//					Log.i("DetectionThread", "Não foi detectado movimento.");
//					
//					tentativas++;
//					this.lastAppName = null;
//					
//					// primeiro caso
//					if (isMarkerFound()){
//						Log.i("DetectionThread","Marcador encontrado.");
//						// Tenta decodificar o qrcode
//						if(isQRCodeFound()){
//							Log.i("DetectionThread","Marcador decodificado com sucesso.");
//							this.lastAppName = decoderObject.getQrCodeDecoder().getTextDecoded();
//							controlarRecursosRealidadeAumentada();
//						}
//
//						flagControleMovimento = true;
//					
//					}
//				}
				
				
				/* ******************************************************** 
				 * A partir daqui o c�digo original foi comentado 
				 * ********************************************************/

				// Verifica se achou algum marcador 
				// estou considerando que s� ter� um marcador por frame
				// TODO [Ricardo] Arrumar isso para v�rios marcadores
				if((int) mat[0] == 9999){ // > 0
					
					// Write all current information of the detected markers into
					// the marker hashmap and notify markers they are recognized.
					int posicaoInicialMarcador, posicaoFinalMarcador, rotacaoMarcador, idMarcador = 0;

					posicaoInicialMarcador = 1;
					posicaoFinalMarcador = posicaoInicialMarcador + 15;
					rotacaoMarcador = posicaoFinalMarcador + 1;
					idMarcador = rotacaoMarcador + 1;
					
					// FIXME Importante
					// Por enquanto o idMarcador = 0, definido no myOpenDVDetection.c
					// A ideia aqui � decodificar o QRCode atribuir a uma string, posteriormente
					// buscar esse nome em um mapa contendo o nome de todas aplica��es que est�o
					// presentes no ambiente.
					
					
					
					
					// -------------------------------------------------------------
					
					
//					String textoDecodificado = "TesteApp";
//					
//					mapa.get(textoDecodificado);
					
					
//					MarkerObject markerObj = markerObjectMap.get(5);
//
//					Log.i("PosicaoMarcador", "Posicao inicial = "+ posicaoInicialMarcador + ", final = "+ posicaoFinalMarcador);
//					
//					if (markerObj != null) {
//						// Marcador não foi encontrado
//						markerObj.OnMarkerPositionRecognized(mat, posicaoInicialMarcador,posicaoFinalMarcador);
//					} else {
//						
//						// Marcador foi detectado
//						if (unrecognizedMarkerListener != null) {
//							
//							
//							unrecognizedMarkerListener.onUnrecognizedMarkerDetected(
//																						(int) mat[idMarcador], 
//																						mat, 
//																						posicaoInicialMarcador,
//																						posicaoFinalMarcador, 
//																						(int) mat[rotacaoMarcador]
//															          				);
//						}
//					}
					
					 
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
	
	private boolean isMotionDetected(){
		
		//TODO [Ricardo] implementar o esquema de similaridade dos frames
		
		rgb = ImageProcessing.decodeYUV420SPtoRGB(frame, frameWidth, frameHeight);
		
		return this.iMotionDetection.detect(rgb, frameWidth, frameHeight);
	}
	
	private boolean isQRCodeFound(){

		// Decodificando o QRCode

		Calendar inicio = Calendar.getInstance();
		
		Result result = decoderObject.getQrCodeDecoder().decode(frame, frameWidth, frameHeight);
		
		Calendar fim = Calendar.getInstance();
		
		long tempoTotalDecodificacao = fim.getTimeInMillis() - inicio.getTimeInMillis();
		
		Log.i("DetectionThread", "Tempo de decodificação: " + tempoTotalDecodificacao);
		
		if(decoderObject.getQrCodeDecoder().getTextDecoded() != null){
			Log.i("DetectionThread", "Código decodificado = "+ decoderObject.getQrCodeDecoder().getTextDecoded());
		}else{
			Log.i("DetectionThread", "Não foi possível decodificar o marcador.");
		}
		
		return decoderObject.getQrCodeDecoder().getTextDecoded() != null;
	}
	
	private void controlarRecursosRealidadeAumentada(){
		
		MarkerObject markerObj = markerObjectMap.get(5);

		Log.i("PosicaoMarcador", "Posicao inicial = "+ 1 + ", final = "+ 16);
		
		if (markerObj != null) {
			// Marcador não foi encontrado
			markerObj.OnMarkerPositionRecognized(mat, 1,16);
		} else {
			
			// Marcador foi detectado
			if (unrecognizedMarkerListener != null) {
				
				
				unrecognizedMarkerListener.onUnrecognizedMarkerDetected(
																			(int) mat[5], 
																			mat, 
																			1,
																			16, 
																			(int) mat[17]
												          				);
			}
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
