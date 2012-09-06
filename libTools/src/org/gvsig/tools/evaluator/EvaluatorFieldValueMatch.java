package org.gvsig.tools.evaluator;

public class EvaluatorFieldValueMatch extends EvaluatorFieldValue {

	private Object value;

	EvaluatorFieldValueMatch(String fieldName, Object value) {
		super(fieldName, MATCH);
		this.value = value;
	}

	/**
	 * Get the value to mach.
	 *
	 * @return the match value or null if not aplicable.
	 */
	public Object getValue() {
		return this.value;
	}

}
