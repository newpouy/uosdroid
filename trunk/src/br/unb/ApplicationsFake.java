package br.unb;

public class ApplicationsFake {

	private String nome;

	// TODO [Ricardo] verificar se d‡ para implementar um esquema para o id seja œnico
	// talvez isso ai funcione com o Dao list returnando o ID.
	private int id;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
