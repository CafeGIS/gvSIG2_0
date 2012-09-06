package org.gvsig.tools.dynobject.exception;

import org.gvsig.tools.exception.BaseException;

public abstract class DynMethodException extends BaseException {


	/**
	 *
	 */
	private static final long serialVersionUID = 6581546517547877738L;

	public DynMethodException(String messageFormat, String messageKey,
			long serialVersionUID2) {
		super(messageFormat, messageKey, serialVersionUID2);
	}

}
