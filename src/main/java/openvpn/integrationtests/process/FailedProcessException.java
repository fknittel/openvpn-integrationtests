package openvpn.integrationtests.process;

public class FailedProcessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FailedProcessException(String message, SimpleProcessBuilder builder) {
		super("While executing " + builder.toString() + ": " + message);
	}

	public FailedProcessException(String message, SimpleProcessBuilder builder, Exception cause) {
		super("Exception while executing " + builder.toString() + ": " + message, cause);
	}
}