package org.gvsig.fmap.dal.feature.impl;

import java.util.Comparator;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataEvaluatorRuntimeException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

public class DefaultFeatureComparator implements Comparator {

	private FeatureQueryOrder order;


	public DefaultFeatureComparator(FeatureQueryOrder order) {
		this.order = order;
		// TODO optimizar en un array???

	}


	private int myCompare(Object arg0, Object arg1) {
		if (arg0 == null){
			if (arg1 == null){
				return 0;
			} else{
				return 1;
			}
		} else if (arg1 == null){
			if (arg0 == null) {
				return 0;
			} else {
				return 1;
			}
		}
		if (arg0 instanceof Comparable) {
			return ((Comparable) arg0).compareTo(arg1);
		} else if (arg1 instanceof Comparable) {
			return ((Comparable) arg1).compareTo(arg0) * -1;
		}

		if (arg0.equals(arg1)){
			return 0;
		} else{
			return -1;
		}

	}

	public int compare(Object arg0, Object arg1) {
		Iterator iter = this.order.iterator();
		int returnValue = 0;
		Feature f0 = (Feature) arg0;
		Feature f1 = (Feature) arg1;
		Object item;
		String attrName;
		Evaluator evaluator;
		while (returnValue == 0 && iter.hasNext()) {
			item = iter.next();
			if (item instanceof String) {
				attrName = (String) item;
				returnValue = this
						.myCompare(f0.get(attrName), f1
						.get(attrName));
			} else {
				evaluator = (Evaluator) item;
				try {
					returnValue = this.myCompare(evaluator
							.evaluate((EvaluatorData) f0), evaluator
							.evaluate((EvaluatorData) f1));
				} catch (EvaluatorException e) {
					throw new DataEvaluatorRuntimeException(e);
				}
			}
		}

		return returnValue;
	}

}
