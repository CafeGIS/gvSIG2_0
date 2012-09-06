package org.gvsig.tools.evaluator;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

public class EvaluatorException extends BaseException {

	/**
	 *
	 */
	private static final long serialVersionUID = -7636817907827177835L;
	private final static String MESSAGE_FORMAT = "Evaluator Error.";
	private final static String MESSAGE_KEY = "_OperationException";

	protected Map values = new HashMap();

	public EvaluatorException(Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}

	protected void setValue(String name, Object value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}

}
