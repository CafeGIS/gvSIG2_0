package org.gvsig.fmap.geom.operation.towkb;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

public abstract class WKBEncodingException extends BaseException {

	/**
	 *
	 */
	private static final long serialVersionUID = -7340057779411755446L;

	protected Map values = new HashMap();

	public WKBEncodingException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public WKBEncodingException(String messageFormat, String messageKey, long code) {
		super(messageFormat, messageKey, code);
	}

	protected void setValue(String name, String value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}
}
