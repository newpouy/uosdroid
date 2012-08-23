package br.unb.unbiquitous.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CalculoMedicao {
	
	/*********************************************
	 * CONSTANTS
	 *********************************************/
	
	private static final String TAG = CalculoMedicao.class.getSimpleName(); 
	
	/*********************************************
	 * VARIABLES
	 *********************************************/
	private static CalculoMedicao calculoMedicao;
	
	private List<Medicao> medicoes = new ArrayList<Medicao>();

	private Float taxaErro;
	private Float taxaNaoDecodificacao;
	private Float tempoMedioPrimeiraAparicao;
	private Float tempoMedioRecorrencia;
	private Integer totalRecorrencia;
	private Integer totalPrimeiraAparicao;

	/*********************************************
	 * CONSTRUCTOR
	 *********************************************/
	
	private CalculoMedicao(){}
	
	/*********************************************
	 * PUBLIC METHODS
	 *********************************************/
	
	public static CalculoMedicao getInstance(){
		
		if(calculoMedicao == null){
			calculoMedicao = new CalculoMedicao();
		}
		
		return calculoMedicao;
	}
	
	public void registrar(TipoMedicao tipoMedicao, Float tempo) {

		Medicao medicao = new Medicao();
		medicao.setTipoMedicao(tipoMedicao);
		medicao.setTempo(tempo);

		Log.e("Medicao", "Tipo: " + tipoMedicao + "\t tempo: "+tempo);
		
		medicoes.add(medicao);
	}

	public void calcular() {

		tempoMedioPrimeiraAparicao = calcularTempoMedio(TipoMedicao.PRIMEIRA_APARICAO);
		tempoMedioRecorrencia = calcularTempoMedio(TipoMedicao.RECORRENCIA);
		
		taxaErro = calcularTaxa(TipoMedicao.PERDEU_MARCADOR) * 100;
		taxaNaoDecodificacao = calcularTaxa(TipoMedicao.NAO_CONSEGUIU_DECODIFICAR) * 100;
		totalRecorrencia = getTotal(TipoMedicao.RECORRENCIA);
		totalPrimeiraAparicao = getTotal(TipoMedicao.PRIMEIRA_APARICAO);
		
		Log.e(TAG,"++++++++ Relatório +++++++");
		Log.e(TAG,"Tempo médio da primeira aparição = " + tempoMedioPrimeiraAparicao + "s");
		Log.e(TAG,"Tempo médio de recorrência = " + tempoMedioRecorrencia + "s");
		Log.e(TAG,"Taxa de erro = " + taxaErro + "%");
		Log.e(TAG,"Taxa que não conseguiu decodificar = " + taxaNaoDecodificacao + "%");
		
	}
	
	public void resetarMedicoes(){
		this.medicoes = new ArrayList<Medicao>();
	}

	/*********************************************
	 * PRIVATE METHODS
	 *********************************************/
	
	private Float calcularTempoMedio(TipoMedicao tipoMedicao) {

		float total = 0;
		float tempoTotal = 0;

		if(medicoes.isEmpty()) return 0f;
		
		for (Medicao medicao : medicoes) {
			if (medicao.getTipoMedicao().equals(tipoMedicao)) {
				total++;
				tempoTotal += medicao.getTempo();
			}
		}

		return Float.valueOf(tempoTotal/total);
	}

	private Float calcularTaxa(TipoMedicao tipoMedicao){
		
		float count = 0;
		
		if(medicoes.isEmpty()) return 0f;
		
		for (Medicao medicao : medicoes) {
			if(medicao.getTipoMedicao().equals(tipoMedicao)){
				count++;
			}
		}
		return Float.valueOf(count / ((float) medicoes.size()));
	}

	private Integer getTotal(TipoMedicao tipoMedicao){
		int count = 0;
		
		for (Medicao medicao : medicoes) {
			if(medicao.getTipoMedicao().equals(tipoMedicao)){
				count++;
			}
		}
		
		return count;
	}
	
	/*********************************************
	 * GETTERS AND SETTERS
	 *********************************************/
	
	public List<Medicao> getMedicoes() {
		return medicoes;
	}

	public void setMedicoes(List<Medicao> medicoes) {
		this.medicoes = medicoes;
	}

	public Float getTaxaErro() {
		return taxaErro;
	}

	public void setTaxaErro(Float taxaErro) {
		this.taxaErro = taxaErro;
	}

	public Float getTaxaNaoDecodificacao() {
		return taxaNaoDecodificacao;
	}

	public void setTaxaNaoDecodificacao(Float taxaNaoDecodificacao) {
		this.taxaNaoDecodificacao = taxaNaoDecodificacao;
	}

	public Float getTempoMedioPrimeiraAparicao() {
		return tempoMedioPrimeiraAparicao;
	}

	public void setTempoMedioPrimeiraAparicao(Float tempoMedioPrimeiraAparicao) {
		this.tempoMedioPrimeiraAparicao = tempoMedioPrimeiraAparicao;
	}

	public Float getTempoMedioRecorrencia() {
		return tempoMedioRecorrencia;
	}

	public void setTempoMedioRecorrencia(Float tempoMedioRecorrencia) {
		this.tempoMedioRecorrencia = tempoMedioRecorrencia;
	}

	public Integer getTotalRecorrencia() {
		return totalRecorrencia;
	}

	public void setTotalRecorrencia(Integer totalRecorrencia) {
		this.totalRecorrencia = totalRecorrencia;
	}

	public Integer getTotalPrimeiraAparicao() {
		return totalPrimeiraAparicao;
	}

	public void setTotalPrimeiraAparicao(Integer totalPrimeiraAparicao) {
		this.totalPrimeiraAparicao = totalPrimeiraAparicao;
	}
	
	
	
	
}
