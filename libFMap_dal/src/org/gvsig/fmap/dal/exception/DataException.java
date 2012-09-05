package org.gvsig.fmap.dal.exception;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

public abstract class DataException extends BaseException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1951723534438433226L;

	protected Map values = new HashMap();

	public DataException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public DataException(String messageFormat, String messageKey, long code) {
		super(messageFormat, messageKey, code);
	}

	protected void setValue(String name, String value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}
}
