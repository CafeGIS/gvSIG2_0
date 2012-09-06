package org.gvsig.tools.persistence.validation;

/**
 * <p>Encapsulates a validation error and allows to query
 * it.</p>.
 *
 * @see ValidationResult
 * @see org.gvsig.tools.dynobject.DynStruct
 * @see org.gvsig.tools.persistence.PersistenceManager
 *
 * @author Cesar Martinez Izquierdo <cesar.martinez@iver.es>
 * @author IVER T.I. <http://www.iver.es>
 */
public interface ValidationError {

	/**
	 * <p>Validation error: The specified attribute name does not exist in the
	 * DynStruct definition.</p>
	 */
	public final int INVALID_NAME = 0;
	/**
	 * <p>Validation error: The name of the attribute is correct, but the type
	 * of the assigned value does not match in the DynStruct definition.
	 */
	public final int WRONG_VALUE = 1;

	/**
	 * <p>Validation error: The definition of the persisted object was not registered
	 * so it was not possible to validate it.</p>
	 */
	public final int MISSING_DEFINITION = 2;

	/**
	 * <p>Gets the kind of error. Possible values include: ValidationError.INVALID_NAME,
	 * ValidationError.WRONG_VALUE and ValidationError.MISSING_DEFINITION.</p>
	 *
	 * @return ValidationError.INVALID_NAME, ValidationError.WRONG_VALUE or MISSING_DEFINITION
	 * @see ValidationError.INVALID_NAME
	 * @see ValidationError.WRONG_VALUE
	 * @see ValidationError.MISSING_DEFINITION
	 */
	public int getType();

	/**
	 * <p>Gets the name of the attribute which failed the validation.</p>.
	 *
	 * @return The name of the field which failed the validation.
	 */
	public String getFieldName();

	/**
	 * <p>Gets the value of the attribute which failed the validation.</p>
	 *
	 * @return
	 */
	public Object getValue();

	/**
	 * <p>Gets a textual report of the error. Normally, this should include
	 * information about the type of error, the name of the involved attribute
	 * and its assigned value.</p>
	 *
	 * @return A plain explanation of the error.
	 */
	public String getInfo();
}
