package es.unex.sextante.gvsig.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.operation.tojts.ToJTS;

import com.vividsolutions.jts.geom.Geometry;

import es.unex.sextante.dataObjects.FeatureImpl;
import es.unex.sextante.dataObjects.IFeature;
import es.unex.sextante.dataObjects.IFeatureIterator;

public class gvFeatureIterator implements IFeatureIterator {

	private FeatureSet set;
    private DisposableIterator featureIterator;

    gvFeatureIterator(
			org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect layer)
			throws DataException {
        try {
			set = layer.getFeatureStore().getFeatureSet();
			featureIterator = set.fastIterator();
		} catch (DataException e) {
			if (featureIterator != null) {
				featureIterator.dispose();
			}
			if (set != null) {
				set.dispose();
			}
			throw e;
		}
    }

    public boolean hasNext() {
       return featureIterator.hasNext();
    }

    public IFeature next() throws java.util.NoSuchElementException{
        try{
        	Feature f = (Feature) featureIterator.next();
           	org.gvsig.fmap.geom.Geometry geometry = f.getDefaultGeometry();
        	Geometry geom = (Geometry)geometry.invokeOperation(ToJTS.CODE, null);
        	FeatureType type=f.getType();
        	Iterator<FeatureAttributeDescriptor> iter=type.iterator();
        	ArrayList<Object> objects=new ArrayList<Object>();
        	while (iter.hasNext()) {
				FeatureAttributeDescriptor descriptor = iter.next();
				String name=descriptor.getName();
				if (!type.getDefaultGeometryAttributeName().equals(name)){
					objects.add(f.get(name));
				}
			}
        	FeatureImpl feature = new FeatureImpl(geom, objects.toArray(new Object[0]));
        	return feature;
        }catch (Exception e){
        	throw new RuntimeException(e);
        }

    }

	public void close() {
		if (featureIterator != null) {
			featureIterator.dispose();
			featureIterator = null;
		}
		if (set != null) {
			set.dispose();
			set = null;
		}
	}

}
