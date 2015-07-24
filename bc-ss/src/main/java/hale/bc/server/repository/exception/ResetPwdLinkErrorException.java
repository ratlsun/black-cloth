package hale.bc.server.repository.exception;

public class ResetPwdLinkErrorException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 929394079845075985L;

    public ResetPwdLinkErrorException() {
        super("Reset password link error！");
    }

}
