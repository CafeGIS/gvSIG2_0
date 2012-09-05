package org.gvsig.fmap.dal.feature.impl.featureset;

import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureReference;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

public class EditedIterator extends FilteredIterator {

	Iterator newsFeatures;
	boolean featureIsNew;


	public EditedIterator(DefaultFeatureSet featureSet) {
		super(featureSet);
	}

	public EditedIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		super(featureSet);
		this.newsFeatures = null;
		if (index > 0) {
			if (featureSet.provider.canIterateFromIndex()) {
				try {
					this.iterator = featureSet.provider.iterator(index);
				} catch (IllegalArgumentException e) {
					this.iterator = featureSet.provider.iterator();
					skypto(index);
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

	protected Iterator getIterator() {
		if (this.featureIsNew){
			return this.newsFeatures;
		} else if (this.iterator.hasNext()) {
			featureIsNew=false;
			return this.iterator;
		} else{
			featureIsNew = true;
			this.newsFeatures = this.fset.store.getFeatureManager()
					.getInserted();
			return this.newsFeatures;
		}
	}

	protected DefaultFeature createFeature(FeatureProvider data) {

		DefaultFeature f = null;
		data.setNew(featureIsNew);
		try {
			FeatureReference ref = new DefaultFeatureReference(fset.store, data);
			f = (DefaultFeature) fset.store.getFeatureManager().get(ref,
					fset.store, data.getType());
		} catch (DataException e) {
			RuntimeException ex = new RuntimeException();
			e.initCause(e);
			throw ex;
		}
		if (f == null) {
			// La feature no se ha editado.
			f = new DefaultFeature(fset.store, data);
		}
		return f;
	}

	protected boolean isDeletedOrHasToSkip(FeatureProvider data) {

		// XXX
		// si recorriendo los originales nos
		// encontramos uno nuevo, no lo devolvemos
		// porque se recorrera mas tarde.
		// Esto es una interaccion con los indices
		// ya que estos contienen todas las features ya
		// sea nuevas o no

		if (data.isNew() && !featureIsNew) {
			return true;
		}
		FeatureReference ref = new DefaultFeatureReference(fset.store, data);
		return fset.store.getFeatureManager().isDeleted(ref);
	}

	public void dispose() {
		super.dispose();
		if (newsFeatures instanceof DisposableIterator) {
			((DisposableIterator) newsFeatures).dispose();
		}
		newsFeatures = null;
	}


}
