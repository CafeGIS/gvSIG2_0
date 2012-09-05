package org.gvsig.fmap.dal.exception;

public class RemoveException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4463253141583983345L;
	private final static String MESSAGE_FORMAT = "Exception removing '%(resource)'.";
	private final static String MESSAGE_KEY = "_RemoveException";

	public RemoveException(String resource, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", resource);
	}

}
