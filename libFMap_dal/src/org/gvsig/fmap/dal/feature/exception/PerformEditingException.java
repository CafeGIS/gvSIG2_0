package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class PerformEditingException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4303159486787919718L;
	private final static String MESSAGE_FORMAT = "Can't perform editing in '%(resource)s'.";
	private final static String MESSAGE_KEY = "_PerformEditingException";

	public PerformEditingException(String resource, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("resource", resource);
	}
}