package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataListException;


public class FeatureTypeIntegrityException extends DataListException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1599605985631014641L;

	private final static String MESSAGE_FORMAT = "Feature type (%(typeId)s) incorrect.";
	private final static String MESSAGE_KEY = "_FeatureTypeIntegrityException";

	public FeatureTypeIntegrityException(String typeId) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("typeId", typeId);
	}

}
