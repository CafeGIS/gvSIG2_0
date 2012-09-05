package org.gvsig.fmap.data.feature.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.impl.AbstractFeatureCollection;

public abstract class DBDataFeatureCollection extends
		AbstractFeatureCollection {

	protected boolean isStringEmpty(String s) {
		return s == null || s == "";
	}

	public boolean add(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		checkModified();
		//FIXME: Arreglar (lanzar select directamente)
		Iterator iterator= this.iterator();
		while(iterator.hasNext()){
			Feature feature=(Feature)iterator.next();
			if (feature.getReference().equals(((Feature)o).getReference())){
				return true;
			}
		}
		return false;
	}

	public Object[] toArray() {
		checkModified();
		ArrayList features= new ArrayList();
		Iterator iterator= this.iterator();
		while(iterator.hasNext()){
			Feature feature=(Feature)iterator.next();
			features.add(feature);
		}
		return features.toArray();
	}

	public Object[] toArray(Object[] a) {
		checkModified();
		ArrayList features= new ArrayList();
		Iterator iterator= this.iterator();
		while(iterator.hasNext()){
			Feature feature=(Feature)iterator.next();
			features.add(feature);
		}
		return features.toArray(a);
	}

	public boolean containsAll(Collection c) {
		checkModified();
		Iterator iter = c.iterator();
		while (iter.hasNext()){
			if (!this.contains(iter.next())){
				return false;
			}
		}
		return true;

	}

}
