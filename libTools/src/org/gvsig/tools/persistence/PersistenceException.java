package org.gvsig.tools.persistence;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.ListBaseException;

public class PersistenceException extends ListBaseException {
	private static final long serialVersionUID = -3729654883985281840L;
	private final static String MESSAGE_FORMAT = "Error getting or setting the state of the object.";
	private final static String MESSAGE_KEY = "_PersistenceException";

	protected Map values = new HashMap();

	public PersistenceException(String messageFormat) {
		super(messageFormat, MESSAGE_KEY, serialVersionUID);
	}

	public PersistenceException(Throwable cause) {
		this(MESSAGE_FORMAT, cause);
	}

	protected PersistenceException(String messageFormat, String messageKey, long code) {
		super(messageFormat, messageKey, code);
	}
	
	protected PersistenceException(String messageFormat, Throwable cause, String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public PersistenceException(String messageFormat, Throwable cause) {
		super(messageFormat, cause, MESSAGE_KEY, serialVersionUID);
	}
}
