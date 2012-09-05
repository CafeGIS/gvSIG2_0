package org.gvsig.fmap.dal.exception;

/**
 * FIXME
 *
 */

public class CloseException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 9186224366705822305L;
	private final static String MESSAGE_FORMAT = "Exception closing '%(store)s'.";
	private final static String MESSAGE_KEY = "_CloseException";

	public CloseException(String store, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}

}
