package org.gvsig.fmap.dal.feature.impl.featureset;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

public class FastDefaultIterator extends DefaultIterator {

	DefaultFeature myFeature;

	public FastDefaultIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		super(featureSet);
		this.initializeFeature();
		if (index > 0) {
			if (featureSet.provider.canIterateFromIndex()) {
				try {
					this.iterator = featureSet.provider.fastIterator(index);
				} catch (UnsupportedOperationException e) {
					this.iterator = featureSet.provider.fastIterator();
					skypto(index);
				}
			} else {
				this.iterator = featureSet.provider.fastIterator();
				skypto(index);
			}
		} else {
			this.iterator = featureSet.provider.fastIterator();
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
					myFeature, fset
							.getDefaultFeatureType());
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
