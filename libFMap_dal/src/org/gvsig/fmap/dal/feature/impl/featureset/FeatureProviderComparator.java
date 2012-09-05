package org.gvsig.fmap.dal.feature.impl.featureset;

import java.util.Comparator;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataEvaluatorRuntimeException;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder.FeatureQueryOrderMember;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorException;

/*
 *
 * Compara dos Feature o FeatureProvider.
 */
public class FeatureProviderComparator implements Comparator {

	private FeatureQueryOrder order;
	private DefaultFeature f1;
	private DefaultFeature f2;
	private DefaultFeatureStore store;
	private OrderElement[] elements = null;

	private class OrderElement {
		FeatureQueryOrderMember order = null;
		int fieldIndex = -1;
	}

	public FeatureProviderComparator(DefaultFeatureStore store, FeatureQueryOrder order) {
		this.order = order;
		this.store = store;
		this.f1 = new DefaultFeature(this.store);
		this.f2 = new DefaultFeature(this.store);
	}

	private void initElements(FeatureType type) {
		elements = new OrderElement[this.order.size()];
		OrderElement cur;
		Iterator iter = order.iterator();
		FeatureQueryOrderMember orderItem;
		int i = 0;
		while (iter.hasNext()) {
			cur = new OrderElement();
			cur.order = (FeatureQueryOrderMember) iter.next();
			if (!cur.order.hasEvaluator()) {
				cur.fieldIndex = type.getIndex(cur.order.getAttributeName());
			}
			elements[i] = cur;
			i++;
		}

	}

	public int compare(Object arg0, Object arg1) {
		FeatureQueryOrderMember order;
		Comparable v1;
		Comparable v2;
		Object o1;
		Object o2;
		int v;
		int fieldIndex;

		f1.setData((FeatureProvider) arg0);
		f2.setData((FeatureProvider) arg1);

		if (elements == null){
			initElements(f1.getType());
		}

		OrderElement element;

		for (int i = 0; i < elements.length; i++) {
			element = elements[i];

			if (element.order.hasEvaluator()) {
				Evaluator evaluator = element.order.getEvaluator();
				try {
					o1 = evaluator.evaluate(f1);
					o2 = evaluator.evaluate(f2);
				} catch (EvaluatorException e) {
					throw new DataEvaluatorRuntimeException(e);
				}
			} else {
				o1 = f1.get(element.fieldIndex);
				o2 = f2.get(element.fieldIndex);
			}
			if (o1 == null) {
				if (o2 == null) {
					return 0;
				} else {
					v = 1;
				}
			} else {
				if (o1 instanceof Comparable && o2 instanceof Comparable) {
					v1 = (Comparable) o1;
					v2 = (Comparable) o2;
					v = v1.compareTo(v2);
				} else {
					v = -1;
				}
			}

			if (v != 0) {
				if (element.order.getAscending()) {
					return v;
				} else {
					return -v;
				}
			}
		}
		return 0;
	}

}
