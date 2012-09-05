package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class WriteNotAllowedException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4935538241520381746L;
	private final static String MESSAGE_FORMAT = "Unsuported writing for the store '%(store)s'.";
	private final static String MESSAGE_KEY = "_WriteNotAllowedException";

	public WriteNotAllowedException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}