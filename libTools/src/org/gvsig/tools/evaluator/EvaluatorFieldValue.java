package org.gvsig.tools.evaluator;

public class EvaluatorFieldValue {

	final public static int GENERIC = 0;
	final public static int MATCH = 1;
	final public static int RANGE = 2;
	final public static int NEAREST = 3;

	private int type;
	private String fieldName;


	EvaluatorFieldValue(String fieldName) {
		this(fieldName, GENERIC);
	}

	EvaluatorFieldValue(String fieldName, int type) {
		this.fieldName = fieldName;
		this.type = type;
	}

	/**
	 * Get the type of operation realiced over the field.
	 *
	 * The posibles values are: MATCH RANGE or NEAREST
	 *
	 * @return an int with the type of operation
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Get the fieldName over the operation is realiced.
	 *
	 * @return String name of field.
	 */
	public String getFieldName() {
		return this.fieldName;
	}

}
