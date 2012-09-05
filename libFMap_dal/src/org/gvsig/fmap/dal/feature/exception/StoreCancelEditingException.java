package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreCancelEditingException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4303159486787919718L;
	private final static String MESSAGE_FORMAT = "Can't cancel editing in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_StoreCancelEditingException";

	public StoreCancelEditingException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}