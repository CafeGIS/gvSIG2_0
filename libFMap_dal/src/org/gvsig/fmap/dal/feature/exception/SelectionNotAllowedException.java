package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class SelectionNotAllowedException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6572957305406890773L;
	private final static String MESSAGE_FORMAT = "Selection not allowed in store %(store)s.";
	private final static String MESSAGE_KEY = "_SelectionNotAllowedException";

	public SelectionNotAllowedException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.setValue("store", store);
	}
}
