package hale.bc.server.repository.exception;

public class DuplicatedEntryException extends DataAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 929394079845075985L;

    public DuplicatedEntryException(String entry) {
        super("Duplicated entry - [" + entry + "].");
    }

}
