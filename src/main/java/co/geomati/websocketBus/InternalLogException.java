package co.geomati.websocketBus;

public class InternalLogException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalLogException() {
		super();
	}

	public InternalLogException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalLogException(String message) {
		super(message);
	}

	public InternalLogException(Throwable cause) {
		super(cause);
	}

}
