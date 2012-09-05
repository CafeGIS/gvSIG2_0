package org.gvsig.fmap.dal.feature.impl;

import java.util.Iterator;

/**
 * 
 * Envuelve un iterador de Features para que se comporte como
 * un iterador de FeatireDatas.
 * 
 */
public class FeatureToFeatureProviderIterator implements Iterator {
	Iterator featuresIterator;
	
	public FeatureToFeatureProviderIterator(Iterator it) {
		featuresIterator=it;
	}	
	
	public boolean hasNext() {
		return featuresIterator.hasNext();
	}

	public Object next() {
		return ((DefaultFeature)featuresIterator.next()).getData();
	}

	public void remove() {
		featuresIterator.remove();
	}
	
}
