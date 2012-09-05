package org.gvsig.fmap.mapcontext.layers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gvsig.fmap.mapcontext.layers.operations.LayerCollection;


/**
 * Interator for iterate in a Layers tree
 * <P>
 * Extend this class to create an expecific layer iterator
 * and override the method <code>evaluate</code> for check
 * if a layer will be in the iteration list.
 * <P> 
 * @author jmvivo
 *
 */
public class LayersIterator implements Iterator {
	ArrayList layersList  =new ArrayList();
	int index = 0;
	
	public LayersIterator() {
		
	}
	
	public LayersIterator(FLayer layer) {
		this.appendLayer(layer);
	}
	
	protected void appendLayer(FLayer layer) {
		if (this.evaluate(layer)) {
			layersList.add(layer);
		}
		if (layer instanceof LayerCollection) {
			appendLayers((LayerCollection)layer);			
		}
	}
	
	private void appendLayers(LayerCollection layers) {
		int i;
		for (i=0;i< layers.getLayersCount();i++) {
			appendLayer(layers.getLayer(i));
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();		
	}

	public boolean hasNext() {
		return  index < layersList.size();
	}

	public Object next() {		
		return nextLayer();
	}
	
    /**
     * Returns the next layer in the iteration.
     *
     * @return the next layer in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     * 
     * @see next()
     */
	public FLayer nextLayer() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		FLayer aux = (FLayer)layersList.get(index);
		index++;
		return aux;
	}
	
	/**
	 * Called before add a layer to the iteration
	 * list.	 
	 * @param layer the layer to check
	 * @return true if the layer will be in the iteration list
	 */
	public boolean evaluate(FLayer layer) {
		return true;
	}

}
