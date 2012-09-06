package org.gvsig.tools.evaluator;

public class EvaluatorFieldValueNearest extends EvaluatorFieldValue {

	private int count;
	private Object tolerance;
	private Object value1;

	EvaluatorFieldValueNearest(String fieldName, int count,
			Object tolerance,
			Object value) {
		super(fieldName, NEAREST);
		this.count = count;
		this.tolerance = tolerance;
		this.value1 = value;
	}

	EvaluatorFieldValueNearest(String fieldName, int count, Object value) {
		super(fieldName, NEAREST);
		this.count = count;
		this.tolerance = null;
		this.value1 = value;
	}


	/**
	 * Get the count used in the nearest operation.
	 *
	 * @return the count or -1 if not aplicable.
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Get the tolerance.
	 *
	 * @return the tolerance or -1 if not aplicable.
	 */
	public Object getTolerance() {
		return this.tolerance;
	}

	/**
	 * Get the value.
	 *
	 * @return the match value or null if not aplicable.
	 */
	public Object getValue() {
		return this.value1;
	}
}
