package org.gvsig.tools.evaluator.sqljep;

import java.util.Map;
import java.util.Map.Entry;

import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorFieldValue;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;
import org.medfoster.sqljep.BaseJEP;
import org.medfoster.sqljep.ParseException;

public class SQLJEPEvaluator extends BaseJEP implements Evaluator {

	private String expresion;
	private EvaluatorData data;
	private boolean not_parsed;

	public SQLJEPEvaluator(String expresion) {
		super(expresion);
		this.expresion = expresion;
		this.data = null;
		not_parsed = true;
	}

	public String getName() {
		return "SQLJEP(" + expresion + ")";
	}

	public String getCQL() {
		return expresion;
	}

	public String getDescription() {
		return null;
	}

	public EvaluatorFieldValue[] getFieldValues(String name) {
		return null;
	}

	public Object evaluate(EvaluatorData data) {
		this.data = data;
		Object value = null;
		try {
			if (not_parsed) {
				this.parseExpression();
				not_parsed = false;
			}
			value = this.getValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.data = null;
		return value;
	}

	public int findColumn(String name) {
		return -1;
	}

	public Comparable getColumnObject(int arg0) throws ParseException {
		return null;
	}

	public Entry getVariable(String name) throws ParseException {
		return new MyEntry(name);
	}

	private class MyEntry implements Map.Entry {
		private String key;

		public MyEntry(String key) {
			this.key = key;
		}

		public Object getKey() {
			return this.key;
		}

		public Object getValue() {
			if (data.hasDataValue(key)) {
				return data.getDataValue(key);
			}
			return data.getDataValue(key);
		}

		public Object setValue(Object arg0) {
			return null;
		}

	}

	public EvaluatorFieldsInfo getFieldsInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
