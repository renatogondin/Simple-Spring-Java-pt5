package excecao;


public class ClienteTemMuitosMotoristasException extends Exception {
    private final static long serialVersionUID = 1;

    public ClienteTemMuitosMotoristasException() {
	super();
    }

    public ClienteTemMuitosMotoristasException(String msg) {
	super(msg);
    }
}