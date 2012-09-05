package org.gvsig.fmap.dal.exception;

public class CopyException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 6998431565823817880L;
	private final static String MESSAGE_FORMAT = "Exception in copy '%(name)s'.";
	private final static String MESSAGE_KEY = "_CopyException";

	public CopyException(String name, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
	}

}
