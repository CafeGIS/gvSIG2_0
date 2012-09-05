package org.gvsig.fmap.dal.exception;

/**
 * FIXME
 *
 *
 */

public class WriteException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3656543768356545436L;
	private final static String MESSAGE_FORMAT = "Exception writing '%(resource)'.";
	private final static String MESSAGE_KEY = "_WriteException";

	public WriteException(String resource, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", resource);
	}

}
