package org.gvsig.fmap.dal.exception;


public class DataEvaluatorException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4694862217382272837L;
	private final static String MESSAGE_FORMAT = "Evaluator exception";
	private final static String MESSAGE_KEY = "_DataEvaluatorException";

	public DataEvaluatorException(Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}

}
