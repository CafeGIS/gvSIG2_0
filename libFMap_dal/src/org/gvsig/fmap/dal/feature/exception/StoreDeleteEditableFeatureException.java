package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class StoreDeleteEditableFeatureException extends DataException {


	/**
	 *
	 */
	private static final long serialVersionUID = -996334019316255990L;

	private final static String MESSAGE_FORMAT = "Can't delete an editable feature in store %(store)s.";
	private final static String MESSAGE_KEY = "_DeleteEditableFeatureException";

	public StoreDeleteEditableFeatureException(String store) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.setValue("store", store);
	}
}
