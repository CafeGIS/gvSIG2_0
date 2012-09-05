package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreUndoException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -944510163710231013L;
	private final static String MESSAGE_FORMAT = "Can't undo in store '%(store)s'.";
	private final static String MESSAGE_KEY = "_StoreUndoException";

	public StoreUndoException(Throwable cause, String store) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}
}