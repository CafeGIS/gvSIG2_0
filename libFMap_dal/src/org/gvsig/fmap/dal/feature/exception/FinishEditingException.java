package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class FinishEditingException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5568229519842721381L;

	private final static String MESSAGE_FORMAT = "Can't finish edition.";
	private final static String MESSAGE_KEY = "_FinishEditingException";

	public FinishEditingException(Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}
}