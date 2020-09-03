package excecao;


public class NaoAutorizadoException extends Throwable {
    private final static long serialVersionUID = 1;

    public NaoAutorizadoException() {
    	super();
    }

    public NaoAutorizadoException(String msg) {
    	super(msg);
    }
}