package org.gvsig.fmap.dal.exception;


public class UnsupportedEncodingException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 7149758900102271415L;
	private final static String MESSAGE_FORMAT = "Unsupported encoding Exception.";
	private final static String MESSAGE_KEY = "_UnsupportedEncodingException";

	public UnsupportedEncodingException(Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}
}
