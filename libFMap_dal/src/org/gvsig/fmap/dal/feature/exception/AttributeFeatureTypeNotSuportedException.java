package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataException;

public class AttributeFeatureTypeNotSuportedException extends DataException {


	/**
	 *
	 */
	private static final long serialVersionUID = 7317879648245754186L;
	private final static String MESSAGE_FORMAT = "Attribute type %(typeValue)s ('%(typeName)s') of attribute '%(attrname)s' no supported for '%(storeName)s'.";
	private final static String MESSAGE_KEY = "_AttributeFeatureTypeNotSuportedException";

	public AttributeFeatureTypeNotSuportedException(String attrname,
			int typeValue,
			String typeName, String storeName) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("attrname", attrname);
		setValue("typeValue", "" + typeValue);
		setValue("typeName", typeName);
		setValue("storeName", storeName);

	}
}