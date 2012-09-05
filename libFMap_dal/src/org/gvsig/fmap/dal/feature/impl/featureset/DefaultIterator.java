package org.gvsig.fmap.dal.feature.impl.featureset;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;


class DefaultIterator implements DisposableIterator {

	protected Iterator iterator;
	protected DefaultFeatureSet fset;
	protected Feature lastFeature = null;

	public DefaultIterator(DefaultFeatureSet featureSet) {
		this.fset = featureSet;
	}

	public DefaultIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		this.fset = featureSet;
		if (index > 0) {
			if (featureSet.provider.canIterateFromIndex()) {
				try {
					this.iterator = featureSet.provider.iterator(index);
				} catch (UnsupportedOperationException e) {
					this.iterator = featureSet.provider.iterator();
					skypto(index);
				}
			} else {
				this.iterator = featureSet.provider.iterator();
				skypto(index);
			}
		} else {
			this.iterator = featureSet.provider.iterator();
		}
	}

	protected void skypto(long index) {
		// TODO: Comprobar si esta bien la condicion de n<=
		for (long n = 0; n <= index && this.getIterator().hasNext(); n++, this
				.getIterator()
				.next()) {
			;
		}
	}

	public boolean hasNext() {
		fset.checkModified();
		return this.getIterator().hasNext();
	}

	public Object next() {
		fset.checkModified();
		lastFeature = null;
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		try {
			lastFeature = this.createFeature((FeatureProvider) this.getIterator()
					.next());
			return lastFeature;
		} catch (DataException e) {
			RuntimeException ex = new RuntimeException();
			e.initCause(e);
			throw ex;
		}
	}

	public void remove() {
		if (!fset.store.isEditing()) {
			throw new UnsupportedOperationException();
		}
		if (lastFeature == null) {
			throw new IllegalStateException();
		}
		try {
			this.fset.delete(this.lastFeature);
		} catch (DataException e) {
			// FIXME Cambiar excepcion a una Runtime de DAL
			throw new RuntimeException(e);
		}
		lastFeature = null;
	}

	protected DefaultFeature createFeature(FeatureProvider fData)
			throws DataException {
		fData.setNew(false);
		if (this.fset.transform.isEmpty()) {
			return new DefaultFeature(fset.store, fData);
		} else {
			return (DefaultFeature) this.fset.transform.applyTransform(
					new DefaultFeature(fset.store, fData), fset
							.getDefaultFeatureType());
		}
	}

	protected Iterator getIterator() {
		return this.iterator;
	}

	protected boolean isDeletedOrHasToSkip(FeatureProvider data) {
		return false;
	}

	protected void doNext() throws DataException {

	}

	public void dispose() {
		if (iterator instanceof DisposableIterator){
			((DisposableIterator)this.iterator).dispose();
		}
		this.iterator = null;
		this.fset = null;
		this.lastFeature = null;
	}

}
