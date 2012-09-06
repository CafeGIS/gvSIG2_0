package org.gvsig.tools.persistence.validation;

import org.gvsig.tools.persistence.PersistenceException;

public class ValidationException extends PersistenceException {
	private static final long serialVersionUID = 2753728491693187472L;
	private final static String MESSAGE_FORMAT = "Validation_error(s)_were_found";
    private final static String MESSAGE_KEY = "_ValidationException";
    private ValidationResult result;
    
	public ValidationException(ValidationResult result) {
		this(MESSAGE_FORMAT, result);
	}

	public ValidationException(String message, ValidationResult result) {
		super(message, MESSAGE_KEY, serialVersionUID);
		this.result = result;
	}

	public ValidationException(String message, ValidationResult result, Throwable cause) {
		super(message, cause, MESSAGE_KEY, serialVersionUID);
		this.result = result;
	}

	public ValidationResult getValidationResult() {
		return result;
	}

}
