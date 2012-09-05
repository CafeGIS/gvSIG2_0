package org.gvsig.fmap.dal.feature.impl.featureset;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureReference;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

public class FastEditedIterator extends EditedIterator {
	DefaultFeature myFeature;

	public FastEditedIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		super(featureSet);
		this.initializeFeature();
		if (index > 0) {
			if (featureSet.provider.canIterateFromIndex()) {
				try {
					this.iterator = featureSet.provider.fastIterator(index);
				} catch (IllegalArgumentException e) {
					this.iterator = featureSet.provider.fastIterator();
					this.newsFeatures = null;
					skypto(index);
				} catch (UnsupportedOperationException e) {
					this.iterator = featureSet.provider.fastIterator();
					this.newsFeatures = null;
					skypto(index);
				}
			} else {
				this.iterator = featureSet.provider.fastIterator();
				this.newsFeatures = null;
				skypto(index);
			}
		} else {
			this.iterator = featureSet.provider.fastIterator();
			this.newsFeatures = featureSet.store.getFeatureManager()
					.getInserted();
		}

	}

	protected void initializeFeature() {
		myFeature = new DefaultFeature(fset.store);
	}

	protected DefaultFeature createFeature(FeatureProvider data) {

		DefaultFeature f = null;
		try {
			data.setNew(featureIsNew);
			FeatureReference ref = new DefaultFeatureReference(fset.store,
					data);
			f = (DefaultFeature) fset.store.getFeatureManager().get(
					ref,
					fset.store, data.getType());
		} catch (DataException e) {
			RuntimeException ex = new RuntimeException();
			e.initCause(e);
			throw ex;
		}
		if (f == null) {
			this.myFeature.setData(data);
		} else {
			// TODO Sacamos una copia del data o no???
			this.myFeature.setData(f.getData().getCopy());
		}
		return this.myFeature;

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
