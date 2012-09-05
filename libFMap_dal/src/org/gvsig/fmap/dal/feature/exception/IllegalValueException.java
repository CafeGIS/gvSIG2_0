/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.feature.exception;

import org.gvsig.fmap.dal.exception.DataRuntimeException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;

/**
 * @author jmvivo
 *
 */
public class IllegalValueException extends DataRuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 7398202284121091833L;
	private final static String MESSAGE_FORMAT = "Value not allowed for the attribute '%(attribute)'[%(typename)]: %(value).";
	private final static String MESSAGE_KEY = "_IllegalValueException";

	public IllegalValueException(FeatureAttributeDescriptor attribute,
			Object value) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("attribute", attribute.getName());
		setValue("typename", attribute.getDataTypeName());
		if (value == null) {
			setValue("value", "{null}");
		} else {
			setValue("value", value.toString());
		}
	}
	public IllegalValueException(FeatureAttributeDescriptor attribute,
			Object value, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("attribute", attribute.getName());
		setValue("typename", attribute.getDataTypeName());
		if (value == null) {
			setValue("value", "{null}");
		} else {
			setValue("value", value.toString());
		}

	}
}
