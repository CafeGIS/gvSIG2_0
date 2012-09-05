package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class NoNewFeatureInsertException extends DataException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1597158676397711190L;
	private final static String MESSAGE_FORMAT = "Exception intializing '%(store)s'.";
	private final static String MESSAGE_KEY = "_InitializeException";

	public NoNewFeatureInsertException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}


}
