package org.gvsig.fmap.dal.feature.impl.featureset;

import org.gvsig.fmap.dal.exception.DataEvaluatorException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.tools.evaluator.EvaluatorException;


public class EditedFilteredIterator extends EditedIterator {

	EditedFilteredIterator(DefaultFeatureSet fset, long index)
			throws DataException {
		super(fset, index);
	}

	public boolean match(DefaultFeature feature) throws DataException {
		try {
			return ((Boolean) this.filter.evaluate(feature)).booleanValue();
		} catch (EvaluatorException e) {
			throw new DataEvaluatorException(e);
		}
	}

}