package org.gvsig.tools.exception;


public class NotYetImplemented extends BaseRuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = -4448165879403648365L;
	private final static String MESSAGE_FORMAT = "Operation %(operation)s not yet implemented.";
	private final static String MESSAGE_KEY = "_NotYetImplemented";

	public NotYetImplemented() {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.setValue("operation","");
	}

	public NotYetImplemented(String operation) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.setValue("operation", "'".concat(operation).concat("'") );
	}

}