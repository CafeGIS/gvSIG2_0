package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class AlreadyEditingException extends DataException {


	/**
	 *
	 */
	private static final long serialVersionUID = 6589349326410416899L;

	private final static String MESSAGE_FORMAT = "Store '%(store)s' is already in editing mode.";
	private final static String MESSAGE_KEY = "_AlreadyEditingException";

	public AlreadyEditingException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}