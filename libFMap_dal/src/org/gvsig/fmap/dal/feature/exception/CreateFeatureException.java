package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class CreateFeatureException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -7335241420683266251L;
	private final static String MESSAGE_FORMAT = "Can't create feature in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_CreateFeatureException";

	public CreateFeatureException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}