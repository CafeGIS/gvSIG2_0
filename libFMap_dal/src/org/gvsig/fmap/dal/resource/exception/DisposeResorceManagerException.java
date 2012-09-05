package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.exception.DataListException;

public class DisposeResorceManagerException extends DataListException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1818272776768733342L;

	private final static String MESSAGE_FORMAT = "Exception disposing.";
	private final static String MESSAGE_KEY = "_DisposeResorceManagerException";

	public DisposeResorceManagerException() {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
	}
}