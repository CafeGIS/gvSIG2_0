package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class NullFeatureTypeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8969897372454817704L;
	private final static String MESSAGE_FORMAT = "Can't allowed a feature type to null in store %(store)s.";
	private final static String MESSAGE_KEY = "_NullFeatureTypeException";

	public NullFeatureTypeException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.setValue("store", store);
	}
}
