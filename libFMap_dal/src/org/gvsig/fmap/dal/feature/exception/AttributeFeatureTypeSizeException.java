package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class AttributeFeatureTypeSizeException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2997663523084109588L;

	private final static String MESSAGE_FORMAT = "invalid size '%(size)s'.";
	private final static String MESSAGE_KEY = "_AttributeFeatureTypeSizeException";

	public AttributeFeatureTypeSizeException(int size) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("size", Integer.toString(size));
	}
}