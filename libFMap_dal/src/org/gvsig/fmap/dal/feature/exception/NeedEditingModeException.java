package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class NeedEditingModeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4295472399799040181L;

	private final static String MESSAGE_FORMAT = "Operation not allowed if not in editing mode for store '%(store)s'.";
	private final static String MESSAGE_KEY = "_NeedEditingModeException";

	public NeedEditingModeException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}
