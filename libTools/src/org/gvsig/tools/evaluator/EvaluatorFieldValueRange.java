package org.gvsig.tools.evaluator;

public class EvaluatorFieldValueRange extends EvaluatorFieldValue {


	private Object value1;
	private Object value2;

	EvaluatorFieldValueRange(String fieldName, Object value1,
			Object value2) {
		super(fieldName, RANGE);
		this.value1 = value1;
		this.value1 = value2;
	}

	/**
	 * Get the initial value used in the range operation.
	 *
	 * @return the first value or null if not aplicable.
	 */
	public Object getValue1() {
		return this.value1;
	}

	/**
	 * Get the final value used in the range operation.
	 *
	 * @return the final value or null if not aplicable.
	 */
	public Object getValue2() {
		return this.value2;
	}

}
