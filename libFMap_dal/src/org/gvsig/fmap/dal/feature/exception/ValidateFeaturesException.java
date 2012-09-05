package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class ValidateFeaturesException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6175223093153209836L;
	private final static String MESSAGE_FORMAT = "Can't validate features of store '%(store)s'.";
	private final static String MESSAGE_KEY = "_ValidateFeaturesException";

	public ValidateFeaturesException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}