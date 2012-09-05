package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class UnknownDataTypeException extends DataException {


	/**
	 *
	 */
	private static final long serialVersionUID = -7481521283837021787L;
	private final static String MESSAGE_FORMAT = "Unknown data type '%(typeValue)s' of attribute '%(attrname)s' from '%(storeName)s'.";
	private final static String MESSAGE_KEY = "_UnknownDataTypeException";

	public UnknownDataTypeException(String attrname,
			String typeValue,
			String storeName) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("attrname", attrname);
		setValue("typeValue", typeValue);
		setValue("storeName", storeName);

	}
}