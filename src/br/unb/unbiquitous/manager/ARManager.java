package br.unb.unbiquitous.manager;

import java.util.HashMap;

import android.app.Activity;
import android.util.Log;
import br.unb.unbiquitous.activity.MainUOSActivity;
import br.unb.unbiquitous.marker.detection.MultiMarkerSetup;

import com.google.droidar.gl.MarkerObject;
import com.google.droidar.system.Setup;

/**
 * 
 * @author ricardoandrade
 *
 */
public class ARManager {
	
	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private Setup setup;
	private HashMap<String, MarkerObject> markerObjectMap;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	/**
	 * 
	 */
	public ARManager(Setup setup, HashMap<String, MarkerObject> markerObjectMap){
		this.setup = setup;
		this.markerObjectMap = markerObjectMap;
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	
	/**
	 * 
	 */
	public void inserirNovoObjetoVirtual(String appName, float[] rotacao) {

		if (isAppNameValid(appName)) {

			Log.e("AppName", "AppName decodificado = " + appName);
			
			MarkerObject markerObj = markerObjectMap.get(appName);

			// TODO Fazer a validacao na hydra

			//Verifica se o objeto já foi criado anteriormente.
			if (markerObj != null) {

				// Marcador foi encontrado
				markerObj.OnMarkerPositionRecognized(rotacao, 1, 16);
			} else {
				criarObjetoVirtual(appName);
			}
		}
	}
	
	public void reposicionarObjetoVirtual(String appName, float[] rotacao){
//		appName = "MouseDriverDevice";
		MarkerObject markerObj = markerObjectMap.get(appName);

		// Verifica se o objeto já foi criado anteriormente.
		if (markerObj != null) {
			// Marcador foi encontrado
			markerObj.OnMarkerPositionRecognized(rotacao, 1, 16);
		}
	}
	
	/**
	 * 
	 */
	public void retirarObjetosVirtuais() {
		
		
//		unrecognizedMarkerListener.onUnrecognizedMarkerDetected(5, mat, 1, 16,
//				0 // ver
//				);
	}
	
	/************************************************
	 * PRIVATE METHODS
	 ************************************************/
	
	private boolean isAppNameValid(String appName){
		return ((MainUOSActivity)setup.getActivity()).getHydraConnection().isDeviceValid(appName);
	}
	
	/**
	 * 
	 */
	private void criarObjetoVirtual(String appName) {
		((MultiMarkerSetup) setup).addMarkerObject(appName);
	}

	
	
	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
}
