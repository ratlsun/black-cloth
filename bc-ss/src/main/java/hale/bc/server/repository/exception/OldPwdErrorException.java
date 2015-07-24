package hale.bc.server.repository.exception;

public class OldPwdErrorException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 929394079845075985L;

    public OldPwdErrorException() {
        super("Old password error！");
    }

}
