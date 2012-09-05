package org.gvsig.fmap.dal.feature.impl.featureset;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.DataEvaluatorException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorException;

public class FilteredIterator extends DefaultIterator {

	protected boolean nextChecked;
	protected DefaultFeature current;
	protected Evaluator filter;


	protected FilteredIterator(DefaultFeatureSet featureSet) {
		super(featureSet);
		this.current = null;
		this.nextChecked = false;
		this.filter = featureSet.query.getFilter();
	}

	FilteredIterator(DefaultFeatureSet featureSet, long index)
			throws DataException {
		super(featureSet);
		this.iterator = featureSet.provider.iterator();
		if (index > 0) {
			this.skypto(index);
		}
		this.current = null;
		this.nextChecked = false;
		this.filter = featureSet.query.getFilter();
	}

	protected void skypto(long index) {
		// TODO: Comprobar si esta bien la condicion de n<=
		for (long n = 0; n <= index && this.hasNext(); n++, this.next()) {
			;
		}
	}

	protected void doNext() throws DataException {
		nextChecked = true;
		DefaultFeature feature;
		FeatureProvider data;
		Object obj;
		while (this.getIterator().hasNext()) {
			obj =this.getIterator().next();
			if (obj instanceof FeatureProvider){
				data = (FeatureProvider)obj;
				if (isDeletedOrHasToSkip(data)) {
					continue;
				}
				feature = this.createFeature(data);
			} else {
				feature = (DefaultFeature)obj;
				if (isDeletedOrHasToSkip(feature.getData())) {
					continue;
				}
			}
			if (this.match(feature)) {
				this.current = feature;
				return;
			}
		}
		this.current = null;
	}

	protected Iterator getIterator() {
		return this.iterator;
	}

	public boolean hasNext() {
		fset.checkModified();
		if (nextChecked) {
			return this.current != null;
		}
		try {
			doNext();
		} catch( DataException e) {
			NullPointerException ex = new NullPointerException();
			ex.initCause(e);
			throw ex;
		}
		return this.current != null;
	}

	public boolean match(DefaultFeature feature) throws DataException {
		try {
			if (filter==null) {
				return true;
			}
			return ((Boolean) this.filter.evaluate(feature)).booleanValue();
		} catch (EvaluatorException e) {
			throw new DataEvaluatorException(e);
		}
	}

	public Object next() {
		fset.checkModified();
		if (!nextChecked) {
			hasNext();
		}
		if (this.current == null) {
			throw new NoSuchElementException();
		}
		this.lastFeature = null;
		nextChecked = false;
		DefaultFeature feature = this.current;
		this.current = null;
		this.lastFeature = feature;
		return feature;
	}

	public void dispose() {
		super.dispose();
		current = null;
		filter = null;
	}

}
