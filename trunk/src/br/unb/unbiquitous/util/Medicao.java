package br.unb.unbiquitous.util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Medicao {

	private Long tempoPrimeiraAparicao;
	private List<Long> temposSemPerderAlvo = new ArrayList<Long>();

	private Long inicio;
	private Long fim;
	
	public void start(){
		inicio = Calendar.getInstance().getTimeInMillis();
		fim = null;
	}
	
	public void stop(){
		fim = Calendar.getInstance().getTimeInMillis();
		long tempoTotal = (inicio - fim ) / 1000;
		registrarTempo(tempoTotal);
		
		inicio = null;
		fim = null;
	}

	public void registrarTempo(Long tempo){
		if(tempoPrimeiraAparicao == null){
			tempoPrimeiraAparicao = tempo;
		}else{
			temposSemPerderAlvo.add(tempo);
		}
		
	}

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
