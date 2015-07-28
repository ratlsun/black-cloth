package hale.bc.server.repository.exception;

public class DataAccessException extends Exception {

	private static final long serialVersionUID = 6889194121598504426L;

	public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
