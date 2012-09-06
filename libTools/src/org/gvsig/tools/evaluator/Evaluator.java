package org.gvsig.tools.evaluator;

/**
 * This interface provides the necessary methods for processing CQL expressions.
 *
 *
 */
public interface Evaluator {

	/**
	 * Evaluate with the data passed as parameter.
	 *
	 * @param data
	 * @return the result of the evaluation.
	 */
	public Object evaluate(EvaluatorData data) throws EvaluatorException;

	/**
	 * Get the symbolic name of the evaluator.
	 *
	 * @return evaluator's name
	 */
	public String getName();

	/**
	 * Get a description of the action performed with the evaluator.
	 *
	 * @return evaluator's description
	 */
	public String getDescription();

	/**
	 * Get a CQL representation of the evaluator.
	 *
	 * @return the CQL string or null if not supported.
	 */
	public String getCQL();

	/**
	 * Get information about fiels used in the evaluator.
	 * 
	 * @return
	 */
	public EvaluatorFieldsInfo getFieldsInfo();
}
