package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreEditException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -5529148262961765052L;

	private final static String MESSAGE_FORMAT = "Can't enter in editing mode for store '%(store)s.";
	private final static String MESSAGE_KEY = "_StoreEditException";

	public StoreEditException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		this.setValue("store", store);
	}
}
