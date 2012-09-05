package org.gvsig.fmap.dal.exception;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.ListBaseException;

public abstract class DataListException extends ListBaseException {


	/**
	 *
	 */
	private static final long serialVersionUID = -131815899234291797L;

	protected Map values = new HashMap();

	public DataListException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public DataListException(String messageFormat, String messageKey, long code) {
		super(messageFormat, messageKey, code);
	}

	protected void setValue(String name, String value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}
}
