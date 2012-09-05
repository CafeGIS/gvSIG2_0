package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class IllegalFeatureTypeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3151888912258726109L;
	private final static String MESSAGE_FORMAT = "Feature type not allowed for the store '%(store)s'.";
	private final static String MESSAGE_KEY = "_IllegalFeatureTypeException";

	public IllegalFeatureTypeException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}