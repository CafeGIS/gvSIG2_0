package org.gvsig.fmap.dal.exception;

public class CopyParametersException extends DataRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -401333851837941591L;
	private final static String MESSAGE_FORMAT = "Exception in copy '%(name)'.";
	private final static String MESSAGE_KEY = "_CopyParametersException";

	public CopyParametersException(String name, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
	}

}
