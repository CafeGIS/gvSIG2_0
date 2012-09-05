package org.gvsig.fmap.dal.exception;

public class OpenException extends DataException {
	/**
	 *
	 */
	private static final long serialVersionUID = -8318431541669401823L;
	private final static String MESSAGE_FORMAT = "Exception opening '%(store)'.";
	private final static String MESSAGE_KEY = "_OpenException";

	public OpenException(String store, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}

}
