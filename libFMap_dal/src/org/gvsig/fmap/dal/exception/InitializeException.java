package org.gvsig.fmap.dal.exception;


public class InitializeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3054877505579218816L;
	private final static String MESSAGE_FORMAT = "Exception intializing '%(resource)'.";
	private final static String MESSAGE_KEY = "_InitializeException";

	public InitializeException(String resource, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", resource);
	}

	public InitializeException(Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", "{unknow}");
	}

	protected InitializeException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	protected InitializeException(String messageFormat, String messageKey,
			long code) {
		super(messageFormat, messageKey, code);
	}

}
