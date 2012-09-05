package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class IllegalFeatureException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1367845204038351116L;
	private final static String MESSAGE_FORMAT = "Feature not allowed for the store '%(store)s'.";
	private final static String MESSAGE_KEY = "_IllegalFeatureException";

	public IllegalFeatureException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}