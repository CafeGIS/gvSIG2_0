package org.gvsig.tools.dynobject.exception;

import org.gvsig.tools.dynobject.DynClass;

public class IllegalDynMethodException extends DynMethodException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2958323225403802109L;
	private final static String MESSAGE_FORMAT = "method %(name) not found in class %(badclass).";
	private final static String MESSAGE_KEY = "_IllegalDynMethodException";

	public IllegalDynMethodException(String name, Class badClass) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
		setValue("badclass", badClass.getName());
	}

	public IllegalDynMethodException(String name, DynClass badDynClass) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("name", name);
		setValue("badclass", badDynClass.getName());
	}
}
