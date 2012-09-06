package org.gvsig.tools.dynobject.exception;

import org.gvsig.tools.exception.BaseException;

public class DynMethodIllegalCodeException extends DynMethodException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8651495467468815121L;
	private final static String MESSAGE_FORMAT = "Can't invoke method %(name) with code %(badcode)m real code %(code).";
	private final static String MESSAGE_KEY = "_DynMethodIllegalCodeException";

	public DynMethodIllegalCodeException(String name, int code, int badcode) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
		setValue("code", new Integer(code));
		setValue("badcode", new Integer(badcode));
	}
}
