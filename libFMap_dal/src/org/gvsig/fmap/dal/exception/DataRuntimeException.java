package org.gvsig.fmap.dal.exception;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseRuntimeException;

public abstract class DataRuntimeException extends BaseRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -5882808169007262182L;
	protected Map values = new HashMap();

	public DataRuntimeException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public DataRuntimeException(String messageFormat, String messageKey, long code) {
		super(messageFormat, messageKey, code);
	}

	protected void setValue(String name, String value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}
}
