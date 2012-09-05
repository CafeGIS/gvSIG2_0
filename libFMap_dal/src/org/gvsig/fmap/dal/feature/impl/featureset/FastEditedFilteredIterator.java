package org.gvsig.fmap.dal.feature.impl.featureset;

import org.gvsig.fmap.dal.exception.DataEvaluatorException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.tools.evaluator.EvaluatorException;

public class FastEditedFilteredIterator extends FastEditedIterator {

	public FastEditedFilteredIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		super(featureSet, index);
	}


	public boolean match(DefaultFeature feature) throws DataException {
		try {
			return ((Boolean) this.filter.evaluate(feature)).booleanValue();
		} catch (EvaluatorException e) {
			throw new DataEvaluatorException(e);
		}
	}
}
