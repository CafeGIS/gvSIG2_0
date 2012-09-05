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

public class OrderedIterator extends DefaultIterator {

	protected Feature lastFeature = null;

	OrderedIterator(DefaultFeatureSet featureSet, Iterator iterator, long index) {
		super(featureSet);
		// FIXME QUE PASA CON SIZE > Integer.MAX_VALUE ?????
		if (featureSet.orderedData == null) {
			List data = new ArrayList();
			Object item;
			while (iterator.hasNext()) {
				item = iterator.next();
				if (item instanceof DefaultFeature) {
					data.add(((DefaultFeature) item).getData());
				} else {
					data.add(item);
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

	public OrderedIterator(DefaultFeatureSet featureSet, long index) {
		super(featureSet);
		if (index < Integer.MAX_VALUE) {
			this.iterator = featureSet.orderedData.listIterator((int) index);
		} else {
			this.iterator = featureSet.orderedData.iterator();
			this.skypto(index);
		}
	}

	public void remove() {
		super.remove();
		this.iterator.remove();
	}

	protected DefaultFeature createFeature(FeatureProvider fData)
			throws DataException {
		fData.setNew(false);
		return new DefaultFeature(fset.store, fData);

	}

	public void dispose() {
		super.dispose();
		lastFeature = null;
	}
}

