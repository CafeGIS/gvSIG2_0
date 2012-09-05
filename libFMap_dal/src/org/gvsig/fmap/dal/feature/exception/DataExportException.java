package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class DataExportException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2776287871021464378L;

	private final static String MESSAGE_FORMAT = "Can't export to %(target)s.";
	private final static String MESSAGE_KEY = "_DataExportException";

	public DataExportException(Throwable cause, String target) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		this.setValue("target", target);
	}
}
