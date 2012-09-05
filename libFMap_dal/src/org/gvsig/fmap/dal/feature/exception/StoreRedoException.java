package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreRedoException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3619737749583530176L;
	private final static String MESSAGE_FORMAT = "Can't redo in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_StoreRedoException";

	public StoreRedoException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}