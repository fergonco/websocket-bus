package co.geomati.websocketBus;

public class CallbackException extends Exception {

	private static final long serialVersionUID = 1L;

	public CallbackException() {
		super();
	}

	public CallbackException(String message, Throwable cause) {
		super(message, cause);
	}

	public CallbackException(String message) {
		super(message);
	}

	public CallbackException(Throwable cause) {
		super(cause);
	}

}
