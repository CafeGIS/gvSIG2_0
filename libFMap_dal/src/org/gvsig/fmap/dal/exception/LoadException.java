package org.gvsig.fmap.dal.exception;

public class LoadException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static String MESSAGE_FORMAT = "Can't load '%(resource)'.";
	private final static String MESSAGE_KEY = "_OpenException";

	public LoadException(Throwable cause, String resource) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", resource);
	}

}
