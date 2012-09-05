package org.gvsig.fmap.dal.exception;

public class CreateException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 9186224366705822305L;
	private final static String MESSAGE_FORMAT = "Exception creating '%(resource)'.";
	private final static String MESSAGE_KEY = "_CreateException";

	public CreateException(String resource, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", resource);
	}

}
