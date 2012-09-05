package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreUpdateFeatureTypeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3117799038163318881L;
	private final static String MESSAGE_FORMAT = "Can't update feature type in store '%(store)s.";
	private final static String MESSAGE_KEY = "_StoreUpdateFeatureTypeException";

	public StoreUpdateFeatureTypeException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		this.setValue("store", store);
	}
}
