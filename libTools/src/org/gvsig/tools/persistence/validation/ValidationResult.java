package org.gvsig.tools.persistence.validation;

/**
 * <p>ValidationResult is encapsulates the result of a validation.
 * It allows to query whether the validation was passed or failed
 * and which errors were found (if any).</p>.
 *
 * @see ValidationError
 * @see org.gvsig.tools.dynobject.DynStruct
 * @see org.gvsig.tools.persistence.PersistenceManager
 *
 * @author Cesar Martinez Izquierdo <cesar.martinez@iver.es>
 * @author IVER T.I. <http://www.iver.es>
 */
public interface ValidationResult {
	/**
	 * <p>Returns <code>true</code> if the validation was passed, and
	 * <code>false</code> otherwise.
	 *
	 * @see #getErrors()
	 *
	 * @return True if no errors were found in the validation process,
	 * and false otherwise.
	 */
	public boolean isCorrect();

	/**
	 * <p>Gets a textual report of the validation result. Normally, this should
	 * include the result of the validation (passed or failed) and a report
	 * of the found errors (if any).</p>
	 *
	 * @return A plain explanation of the validation result.
	 */
	public String getInfo();

	/**
	 * <p>Gets all the errors found during the validation process.</p>
	 *
	 * @return An array of ValidationErrors, with as many elements as
	 * errors were found during the validation process.
	 */
	public ValidationError[] getErrors();
}
