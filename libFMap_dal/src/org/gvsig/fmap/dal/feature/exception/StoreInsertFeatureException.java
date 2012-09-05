package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreInsertFeatureException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1309051657301097896L;
	private final static String MESSAGE_FORMAT = "Can't insert feature in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_StoreInsertFeatureException";

	public StoreInsertFeatureException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}