package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreUpdateFeatureException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8532519106338060835L;
	private final static String MESSAGE_FORMAT = "Can't update feature in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_StoreUpdateFeatureException";

	public StoreUpdateFeatureException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}