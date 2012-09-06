package org.gvsig.tools.persistence.impl.validation;

import org.gvsig.tools.persistence.validation.ValidationError;
import org.gvsig.tools.persistence.validation.ValidationResult;

public class DefaultValidationResult implements ValidationResult {

	public ValidationError[] getErrors() {
		return new ValidationError[0];
	}

	public String getInfo() {
		return "Validation is correct";
	}

	public boolean isCorrect() {
		return true;
	}
}
