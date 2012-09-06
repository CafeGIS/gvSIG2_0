package org.gvsig.tools.persistence.impl.validation;

import org.gvsig.tools.persistence.validation.ValidationError;

public class DefaultValidationError implements ValidationError {
	protected int errorType;
	protected String fieldName;
	protected Object value;

	public DefaultValidationError(String fieldName, Object value, int errorType) {
		this.errorType = errorType;
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getInfo() {
		switch (errorType) {
		case ValidationError.WRONG_VALUE:
			return "Validation error -- value: "+value.toString()+"\n does not match definition for attribute name: " + fieldName;
		case ValidationError.INVALID_NAME:
			return "Validation error -- Attribute definition does not include the attribute name: " + fieldName;
		case ValidationError.MISSING_DEFINITION:
			return "Validation error -- Missing attribute definition";
		default:
			return "Unknown validation error";
		}
	}

	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
