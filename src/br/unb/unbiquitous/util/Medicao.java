package br.unb.unbiquitous.util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	
	private Long tempoPrimeiraAparicao;
	private List<Long> temposSemPerderAlvo = new ArrayList<Long>();

	private Long inicio;
	private Long fim;
	
	/*********************************************
	 * PUBLIC MEHTODS
	 *********************************************/
	
	/**
	 * Método responsável por iniciar a contagem. 
	 */
	public void start(){
		inicio = Calendar.getInstance().getTimeInMillis();
		fim = null;
	}
	
	/**
	 * Método responsável por encerrar a contagem.
	 */
	public void stop(){
		fim = Calendar.getInstance().getTimeInMillis();
		long tempoTotal = (inicio - fim ) / 1000;
		registrarTempo(tempoTotal);
		
		inicio = null;
		fim = null;
	}
	
	/*********************************************
	 * PRIVATE MEHTODS
	 *********************************************/
	
	/**
	 * Método responsável por registrar o tempo decorrido pela 
	 * medição.
	 */
	private void registrarTempo(Long tempo){
		if(tempoPrimeiraAparicao == null){
			tempoPrimeiraAparicao = tempo;
		}else{
			temposSemPerderAlvo.add(tempo);
		}
		
	}

	/*********************************************
	 * GETTERS AND SETTERS
	 *********************************************/
	
	public Long getTempoPrimeiraAparicao() {
		return tempoPrimeiraAparicao;
	}

	public void setTempoPrimeiraAparicao(Long tempoPrimeiraAparicao) {
		this.tempoPrimeiraAparicao = tempoPrimeiraAparicao;
	}

	public List<Long> getTemposSemPerderAlvo() {
		return temposSemPerderAlvo;
	}

	public void setTemposSemPerderAlvo(List<Long> temposSemPerderAlvo) {
		this.temposSemPerderAlvo = temposSemPerderAlvo;
	}
	
}
