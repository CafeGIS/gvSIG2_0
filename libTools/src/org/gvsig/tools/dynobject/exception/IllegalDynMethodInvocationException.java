package org.gvsig.tools.dynobject.exception;

import org.gvsig.tools.dynobject.DynClass;

public class IllegalDynMethodInvocationException extends DynMethodException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4671773584912776614L;
	private final static String MESSAGE_FORMAT = "Can't invoke method %(name) for class %(badclass).";
	private final static String MESSAGE_KEY = "_IllegalDynMethodInvocationException";

	public IllegalDynMethodInvocationException(String name, Class badClass) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
		setValue("badclass", badClass.getName());
	}

	public IllegalDynMethodInvocationException(String name, DynClass dynClass) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
		setValue("badclass", dynClass.getName());
	}
}
