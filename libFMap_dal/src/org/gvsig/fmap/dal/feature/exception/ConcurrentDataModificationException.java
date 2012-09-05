package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataRuntimeException;

public class ConcurrentDataModificationException extends DataRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8352205502388246169L;
	private final static String MESSAGE_FORMAT = "Concurrent modification in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_ConcurrentDataModificationException";

	public ConcurrentDataModificationException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}