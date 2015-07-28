package hale.bc.server.service.exception;


public class InvalidPasswordException extends ApplicationException {

	private static final long serialVersionUID = 929394079845075985L;

    public InvalidPasswordException(String pwd) {
        super("Invalid password - [" + pwd + "].");
    }

}
