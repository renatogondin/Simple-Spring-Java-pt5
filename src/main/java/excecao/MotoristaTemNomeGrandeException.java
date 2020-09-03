package excecao;


import anotacoes.ConstraintViolada;

@ConstraintViolada(nome="nome", msg="NOME DEVE SER MENOR!")
public class MotoristaTemNomeGrandeException extends RuntimeException {
	private final static long serialVersionUID = 1;

	public MotoristaTemNomeGrandeException() {
		super();
	}

	public MotoristaTemNomeGrandeException(String msg) {
		super(msg);
	}
}
