package org.gvsig.fmap.dal.feature.impl.featureset;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

public class FastFilteredIterator extends FilteredIterator {

	DefaultFeature myFeature;

	FastFilteredIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		super(featureSet);
		initializeFeature();

		this.iterator = featureSet.provider.fastIterator();
		if (index > 0) {
			this.skypto(index);
		}
	}

	protected DefaultFeature createFeature(FeatureProvider fData)
			throws DataException {
		fData.setNew(false);
		this.myFeature.setData(fData);

		if (this.fset.transform.isEmpty()) {
			return myFeature;
		} else {
			return (DefaultFeature) this.fset.transform.applyTransform(
					myFeature, fset.getDefaultFeatureType());
		}
	}

	protected void initializeFeature() {
		myFeature = new DefaultFeature(fset.store);
	}

	public void remove() {
		super.remove();
		this.initializeFeature();
	}

	public void dispose() {
		super.dispose();
		myFeature = null;
	}


}
