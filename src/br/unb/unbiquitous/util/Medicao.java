package br.unb.unbiquitous.util;
import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;
import android.util.Log;

/**
 * Classe responsável por representar uma medição
 * para os testes de reconhecimento dos marcadores. 
 * 
 * @author ricardoandrade
 *
 */
public class Medicao {

	/*********************************************
	 * VARIABLES
	 *********************************************/
	
	private static final String TAG_MEDICAO = "TESTES";
	
	private Float tempoPrimeiraAparicao;
	private List<Float> temposSemPerderAlvo = new ArrayList<Float>();
	
	private Boolean inProcess = Boolean.FALSE;

	private Long inicio;
	private Long fim;
	
	private static Medicao instance = new Medicao();

	/*********************************************
	 * VARIABLES
	 *********************************************/
	private Medicao() {
		Log.e(TAG_MEDICAO, "+++++++++  INICIO DO RELATORIO DOS TESTES +++++++++ ");
	}
	  
	/*********************************************
	 * PUBLIC MEHTODS
	 *********************************************/
	
	/**
	 * Método responsável por iniciar a contagem. 
	 */
	public void start(){
		if(inProcess) return;
		inicio = SystemClock.uptimeMillis();
		fim = null;
	}
	
	/**
	 * Método responsável por encerrar a contagem.
	 */
	public void stop(){
		if(inicio == null) return;
		
		synchronized (inProcess) {
			fim = SystemClock.uptimeMillis();
			Float tempoTotal = (float)( ((float)fim - (float)inicio) / 1000);
			registrarTempo(tempoTotal);
			
			inicio = null;
			fim = null;
			inProcess = false;
		}
	}
	
	
	
	public void cancel(){
		synchronized (this) {
			inicio = null;
			fim = null;
			inProcess = false;
		}
	}
	
	public static Medicao getInstance() {
	   return instance;
	}
	
	public static void newInstance(){
		synchronized (instance) {
			instance = new Medicao();
		}
	}
	
	/*********************************************
	 * PRIVATE MEHTODS
	 *********************************************/
	
	/**
	 * Método responsável por registrar o tempo decorrido pela 
	 * medição.
	 */
	private void registrarTempo(Float tempo){
		if(tempoPrimeiraAparicao == null){
			Log.e(TAG_MEDICAO, "+++++++++ [TESTE] TEMPO PRIMEIRA APARICAO = " + tempo + "s.");
			tempoPrimeiraAparicao = tempo;
		}else{
			Log.e(TAG_MEDICAO, "+++++++++ [TESTE] RECORRENCIA = " + tempo + "s.");
			temposSemPerderAlvo.add(tempo);
		}
		
	}

	/*********************************************
	 * GETTERS AND SETTERS
	 *********************************************/
	public void setInProcess(boolean processing){
		synchronized (inProcess) {
			inProcess = true;
		}
	}
	
	public Long getInicio() {
		return inicio;
	}

	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}

	public boolean getInProcess(){
		return inProcess;
	}
	public Float getTempoPrimeiraAparicao() {
		return tempoPrimeiraAparicao;
	}

	public void setTempoPrimeiraAparicao(Float tempoPrimeiraAparicao) {
		this.tempoPrimeiraAparicao = tempoPrimeiraAparicao;
	}

	public List<Float> getTemposSemPerderAlvo() {
		return temposSemPerderAlvo;
	}

	public void setTemposSemPerderAlvo(List<Float> temposSemPerderAlvo) {
		this.temposSemPerderAlvo = temposSemPerderAlvo;
	}
	
}
