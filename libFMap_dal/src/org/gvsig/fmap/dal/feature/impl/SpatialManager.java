
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

package org.gvsig.fmap.dal.feature.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureIndex;
import org.gvsig.fmap.dal.feature.FeatureIndexes;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.primitive.Envelope;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class SpatialManager {
    protected boolean isFullExtentDirty = true;
	private FeatureStore featureStore;
	private FeatureIndex featureIndex = null;
	private Envelope originalEnvelope = null;
	private Envelope fullEnvelope = null;
	private ArrayList feaOperation=new ArrayList();
	private boolean noSpatialData = false;

	public SpatialManager(FeatureStore featureStore, Envelope originalEnvelope)
			throws DataException {
		this.featureStore=featureStore;
		FeatureIndexes findexes=featureStore.getIndexes();
		// Comprobamos si hay algun campo espacial a manejar

		FeatureType fType = this.featureStore.getDefaultFeatureType();
		// TODO Multy FType !!
		if (fType.getDefaultGeometryAttributeIndex() < 0) {
			noSpatialData = true;
			return;
		}
		FeatureAttributeDescriptor attr = fType.getAttributeDescriptor(fType
				.getDefaultGeometryAttributeIndex());
		this.originalEnvelope = originalEnvelope;
		if (originalEnvelope != null) {
			this.fullEnvelope = originalEnvelope.getGeometry().getEnvelope();
		} else {
			FeatureAttributeDescriptor geoAttr = fType.getAttributeDescriptor(fType.getDefaultGeometryAttributeIndex());
			try {
				this.fullEnvelope = GeometryLocator.getGeometryManager()
						.createEnvelope(geoAttr.getGeometrySubType());
			} catch (Exception e) {
				// FIXME Excpetion
				throw new RuntimeException(e);
			}
		}
		if (!fType.hasOID()) {
			return;
		}

		Iterator iterator = findexes.iterator();
		FeatureIndex index;
		while (iterator.hasNext()) {
			index = (FeatureIndex) iterator.next();
			if (index.getAttributeNames().size() == 1
					&& index.getAttributeNames().contains(attr.getName())) {
				featureIndex = index;
				break;
			}
		}

		if (featureIndex == null) {
			featureIndex = featureStore.createIndex(fType, attr.getName(),
					"QuadtreeJts");
		}
	}



    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param oldFeature DOCUMENT ME!
     */
    public void updateFeature(Feature feature, Feature oldFeature) {
    	if (noSpatialData) {
			return;
		}
    	if (featureIndex != null) {
			featureIndex.delete(oldFeature);
			feaOperation.add(new FeatureOperation(((DefaultFeature) oldFeature)
					.getReference(), FeatureOperation.DELETE));
			featureIndex.insert(feature);
			feaOperation.add(new FeatureOperation(((DefaultFeature) feature)
					.getReference(), FeatureOperation.INSERT));
			// } else {
			// fullEnvelope.add(feature.getDefaultEnvelope());
		}
        isFullExtentDirty = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     */
    public void insertFeature(Feature feature) {
    	if (noSpatialData) {
			return;
		}
    	if (featureIndex != null) {
			featureIndex.insert(feature);
			feaOperation.add(new FeatureOperation(((DefaultFeature) feature)
					.getReference(), FeatureOperation.INSERT));
		} else if (!isFullExtentDirty) {
			fullEnvelope.add(feature.getDefaultEnvelope());
		}
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     */
    public void deleteFeature(Feature feature) {
    	if (noSpatialData) {
			return;
		}
    	if (featureIndex != null) {
			featureIndex.delete(feature);
			feaOperation.add(new FeatureOperation(((DefaultFeature) feature)
					.getReference(), FeatureOperation.DELETE));
		}
    	isFullExtentDirty = true;
    }

	public void clear() {
	}

	public Envelope getEnvelope() throws DataException {
    	if (noSpatialData) {
			return null;
		}
    	if (!isFullExtentDirty){
    		return this.fullEnvelope;
    	}

		// FIXME in every changes when anyone ask for envelope it was regenerated.
		//       if we assume that the envelope may not be the minimum in edit mode
		//       this call must be very much faster


		FeatureAttributeDescriptor attr = featureStore.getDefaultFeatureType()
				.getAttributeDescriptor(
						featureStore.getDefaultFeatureType()
								.getDefaultGeometryAttributeIndex());
		Envelope fullEnvelope = null;
		try {
			fullEnvelope = GeometryLocator.getGeometryManager().createEnvelope(
					attr.getGeometrySubType());
		} catch (Exception e) {
			// FIXME Exception
			throw new RuntimeException(e);
		}
		FeatureSet set = null;
		DisposableIterator iterator = null;
		try {
			set = featureStore.getFeatureSet();
			iterator = set.fastIterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();
				Envelope envelope = feature.getDefaultEnvelope();
				fullEnvelope.add(envelope);
			}
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
			if (set != null) {
				set.dispose();
			}
		}
		this.fullEnvelope = fullEnvelope;
		this.isFullExtentDirty = false;
		return fullEnvelope;
	}



	public void cancelModifies() {
    	if (noSpatialData) {
			return;
		}
    	if (featureIndex != null){
			Iterator iterator=feaOperation.iterator();
			while (iterator.hasNext()) {
				try {
					FeatureOperation fo = (FeatureOperation) iterator.next();
					if (fo.getOperation()==FeatureOperation.INSERT){
						featureIndex.delete(fo.getFeatureReference().getFeature());
					}else if (fo.getOperation()==FeatureOperation.DELETE){
						featureIndex.insert(fo.getFeatureReference().getFeature());
					}
				} catch (DataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	fullEnvelope = originalEnvelope.getGeometry().getEnvelope();
		isFullExtentDirty = false;
	}

	private class FeatureOperation{
		final static int INSERT=0;
		final static int DELETE=1;
		private FeatureReference ref;
		private int operation;
		public FeatureOperation(FeatureReference fe,int op){
			ref=fe;
			operation=op;
		}
		public FeatureReference getFeatureReference() {
			return ref;
		}
		public void setFeatureReference(FeatureReference ref) {
			this.ref = ref;
		}
		public int getOperation() {
			return operation;
		}
		public void setOperation(int operation) {
			this.operation = operation;
		}
	}

}
