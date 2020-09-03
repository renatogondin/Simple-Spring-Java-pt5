package modelo;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")

public class Usuario {
	private String conta;
	private String senha;
	private List<Perfil> perfis = new ArrayList<Perfil>();

	// ********* Construtores *********

	public Usuario() {
	}

	public Usuario(String conta, String senha) {
		this.conta = conta;
		this.senha = senha;
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "conta")
	public String getConta() {
		return this.conta;
	}
	
	@Column(name="senha")
	public String getSenha() {
		return this.senha;
	}

	
		// ********* Métodos do Tipo Set *********


	public void setConta(String conta) {
		this.conta = conta;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	
	// ********* Métodos para Associações *********

	/*
	 * Com o atributo mappedBy. Sem ele a JPA irá procurar pela tabela
	 * PRODUTO_LANCE. Com ele, ao se tentar recuperar um produto e todos os seus
	 * lances, o join de PRODUTO e LANCE irá acontecer através da chave estrangeira
	 * existente em LANCE. Sem ele a JPA irá procurar pela tabela PRODUTO_LANCE.
	 */
	@OneToMany(mappedBy = "usuario")
	@OrderBy
	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
}
