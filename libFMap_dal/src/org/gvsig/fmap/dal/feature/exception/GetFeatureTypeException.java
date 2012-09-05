package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class GetFeatureTypeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6691256137001562925L;
	private final static String MESSAGE_FORMAT = "Can't get feature type of store '%(store)s'.";
	private final static String MESSAGE_KEY = "_GetFeatureTypeException";

	public GetFeatureTypeException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}