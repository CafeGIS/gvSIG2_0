package org.gvsig.fmap.dal.store.jdbc.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class InvalidResultSetIdException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6819010675588720112L;

	private final static String MESSAGE_FORMAT = "ResultSet id (%(id)s) invalid or closed.";
	private final static String MESSAGE_KEY = "_InvalidResultSetIdException";

	public InvalidResultSetIdException(int id) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("id", id + "");
	}
}
