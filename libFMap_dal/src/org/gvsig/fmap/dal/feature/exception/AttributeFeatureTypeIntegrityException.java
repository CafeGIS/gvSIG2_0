package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataListException;

public class AttributeFeatureTypeIntegrityException extends DataListException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1599605985631014641L;

	private final static String MESSAGE_FORMAT = "Integrity error in attribute '%(attrname)s'.";
	private final static String MESSAGE_KEY = "_AttributeFeatureTypeIntegrityException";

	public AttributeFeatureTypeIntegrityException(String attrname) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("attrname", attrname);
	}
}