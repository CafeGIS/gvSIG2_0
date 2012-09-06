package org.gvsig.tools.evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EvaluatorFieldsInfo {
	private Map fields = new HashMap();
	private String[] names = null;

	public String[] getFieldNames() {
		if (names == null) {
			names = (String[]) fields.keySet().toArray(new String[0]);
		}
		return names;
	}

	public EvaluatorFieldValue[] getFieldValues(String name) {
		return (EvaluatorFieldValue[]) ((List) this.fields.get(name))
				.toArray(new EvaluatorFieldValue[0]);
	}

	public void addFieldValue(String name) {
		EvaluatorFieldValue field = new EvaluatorFieldValue(name);
		this.addFieldValue(field);
	}

	public void addMatchFieldValue(String name, Object value) {
		EvaluatorFieldValue field = new EvaluatorFieldValueMatch(name, value);
		this.addFieldValue(field);
	}

	public void addRangeFieldValue(String name, Object value1, Object value2) {
		EvaluatorFieldValue field = new EvaluatorFieldValueRange(name, value1,
				value2);
		this.addFieldValue(field);
	}

	public void addNearestFieldValue(String name, int count, Object value) {
		EvaluatorFieldValue field = new EvaluatorFieldValueNearest(name, count,
				value);
		this.addFieldValue(field);
	}

	public void addNearestFieldValue(String name, int count, Object tolerance,Object value) {
		EvaluatorFieldValue field = new EvaluatorFieldValueNearest(name, count,
				tolerance,
				value);
		this.addFieldValue(field);
	}

	private void addFieldValue(EvaluatorFieldValue field) {
		List list = (List) this.fields.get(field.getFieldName());
		if (list == null) {
			this.names = null;
			list = new ArrayList();
			this.fields.put(field.getFieldName(), list);
		}
		list.add(field);
	}
}
