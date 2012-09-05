package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreDeleteFeatureException extends DataException {


	/**
	 *
	 */
	private static final long serialVersionUID = 5730673193453142604L;
	private final static String MESSAGE_FORMAT = "Can't delete feature in store %(store)s.";
	private final static String MESSAGE_KEY = "_StoreDeleteFeatureException";

	public StoreDeleteFeatureException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		this.setValue("store", store);
	}
}
