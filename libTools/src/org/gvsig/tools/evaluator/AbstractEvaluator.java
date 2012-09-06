package org.gvsig.tools.evaluator;


public abstract class AbstractEvaluator implements Evaluator {
	private EvaluatorFieldsInfo fieldsInfo = new EvaluatorFieldsInfo();

	public String getDescription() {
		return "";
	}

	public String getCQL() {
		return null;
	}

	public EvaluatorFieldsInfo getFieldsInfo() {
		return fieldsInfo;
	}


}
