package org.gvsig.fmap.dal.feature.impl.featureset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

public class FastOrderedIterator extends DefaultIterator {
	DefaultFeature myFeature;
	protected Feature lastFeature = null;

	FastOrderedIterator(DefaultFeatureSet featureSet, Iterator iterator, long index) {
		super(featureSet);
		this.initializeFeature();
		if (featureSet.orderedData == null) {
			// FIXME QUE PASA CON SIZE > Integer.MAX_VALUE ?????

			List data = new ArrayList();
			Object item;
			while (iterator.hasNext()) {
				item = iterator.next();
				if (item instanceof DefaultFeature){
					data.add(((DefaultFeature) item).getData().getCopy());
				} else {
					data.add(((FeatureProvider)item).getCopy());
				}
			}
			Collections.sort(data, new FeatureProviderComparator(featureSet.store,
					featureSet.query.getOrder()));
			featureSet.orderedData = data;
		}
		if (iterator instanceof DisposableIterator) {
			((DisposableIterator) iterator).dispose();
		}
		iterator = null;

		if (index < Integer.MAX_VALUE) {
			this.iterator = featureSet.orderedData.listIterator((int) index);
		} else {
			this.iterator = featureSet.orderedData.iterator();
			this.skypto(index);
		}
	}

	public FastOrderedIterator(DefaultFeatureSet featureSet, long index) {
		super(featureSet);
		this.initializeFeature();

		if (index < Integer.MAX_VALUE) {
			this.iterator = featureSet.orderedData.listIterator((int) index);
		} else {
			this.iterator = featureSet.orderedData.iterator();
			this.skypto(index);
		}
	}

	protected DefaultFeature createFeature(FeatureProvider fData) {
		fData.setNew(false);
		this.myFeature.setData(fData);
		return this.myFeature;
	}

	protected void initializeFeature() {
		myFeature = new DefaultFeature(fset.store);
	}

	public Object next() {
		lastFeature = null;
		lastFeature = (Feature) super.next();
		return lastFeature;
	}

	public void remove() {
		if (!fset.store.isEditing()) {
			throw new UnsupportedOperationException();
		}
		if (this.lastFeature == null) {
			throw new IllegalStateException();
		}
		try {
			fset.store.delete(this.lastFeature);
		} catch (DataException e) {
			// FIXME Cambiar excepcion a una Runtime de DAL
			throw new RuntimeException(e);
		}
		this.iterator.remove();
		this.initializeFeature();
	}

	public void dispose() {
		super.dispose();
		myFeature = null;
		lastFeature = null;
	}
}

