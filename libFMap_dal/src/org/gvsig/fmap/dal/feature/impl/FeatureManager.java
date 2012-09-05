
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.expansionadapter.ExpansionAdapter;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class FeatureManager {
    private ExpansionAdapter expansionAdapter;
    private ArrayList deleted = new ArrayList();//<FeatureID>
    private int deltaSize=0;
	private Map added = new LinkedHashMap();
	private HashMap modifiedFromOriginal=new HashMap();
    public FeatureManager(ExpansionAdapter expansionAdapter){
    	this.expansionAdapter=expansionAdapter;
    }


    public Feature delete(FeatureReference id) {
        deleted.add(id);
		Integer num = (Integer) added.remove(id);
        Feature feature=null;
        if (num == null || num.intValue() == -1) {
        	feature = (Feature)modifiedFromOriginal.remove(id);
		}else{
			feature = (Feature) expansionAdapter.getObject(num.intValue());
		}
        deltaSize--;
        return feature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     */
    public void add(Feature feature) {
        int pos = expansionAdapter.addObject(feature);
        added.put(feature.getReference(),new Integer(pos));
        deleted.remove(feature.getReference());
        deltaSize++;
    }

    /**
     * DOCUMENT ME!
     * @param id DOCUMENT ME!
     */
    public Feature deleteLastFeature() {
        expansionAdapter.deleteLastObject();
        Feature feature=(Feature)expansionAdapter.getObject(expansionAdapter.getSize()-1);
        added.remove(feature.getReference());
        modifiedFromOriginal.remove(feature.getReference());
        deltaSize--;
        return feature;
    }

    /**
     * Returns a Feature of the default type.
     *
     * @param id
     *            the feature reference
     * @param store
     *            the store to get the feature from
     * @return a Feature with the given reference
     * @throws DataException
     *             if there is an error getting the Feature
     */
    public Feature get(FeatureReference id, FeatureStore store)
            throws DataException {
        return get(id, store, null);
    }

    /**
     * Returns a Feature of the given type.
     *
     * @param id
     *            the feature reference
     * @param store
     *            the store to get the feature from
     * @param featureType
     *            the type of the feature to return
     * @return a Feature with the given reference
     * @throws DataException
     *             if there is an error getting the Feature
     */
    public Feature get(FeatureReference id, FeatureStore store,
			FeatureType featureType) throws DataException {
    	// FIXME: y si el featuretype que paso esta modificado.
    	//        Deberia buscarlo en el featuretypemanager ?
		//
    	//        Si no existe feature con ese id... ¿ retorna null ?
    	//        en EditedDefaultIterator se hace uso de ese comportamiento.
    	//
    	Integer intNum =((Integer) added.get(id));
    	if (intNum == null){
    		intNum =((Integer) modifiedFromOriginal.get(id));
        	if (intNum == null){
        		return null;
        	}
    	}
        int num = intNum.intValue();
        if (num==-1) {
			return null;
		}
        Feature feature=(Feature)expansionAdapter.getObject(num);
        if (featureType== null){
        	featureType = store.getDefaultFeatureType();
        }
       	return getCorrectFeature(feature, store,featureType);
    }

    private Feature getCorrectFeature(Feature feature, FeatureStore store,
			FeatureType featureType) throws DataException {
    	 Iterator iterator=featureType.iterator();
         EditableFeature newFeature=feature.getEditable();//store.createNewFeature(featureType, false);
         FeatureType orgType = feature.getType();
         int orgIndex;
         while (iterator.hasNext()) {
 			FeatureAttributeDescriptor fad = (FeatureAttributeDescriptor) iterator.next();
 			orgIndex = orgType.getIndex(fad.getName());
 			if (orgIndex<0){
 				continue;
 			}
 			if (fad.isAutomatic() || fad.isReadOnly()
					|| fad.getEvaluator() != null) {
				continue;
			}
 			Object value = feature.get(orgIndex);
			if (value == null && !fad.allowNull()) {
				continue;
			}
			newFeature.set(fad.getIndex(), value);
 		}
        return newFeature.getNotEditableCopy();
	}

	/**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param oldFeature DOCUMENT ME!
     */
    public int update(Feature feature, Feature oldFeature) {
    	int oldNum=-1;
        int num = expansionAdapter.addObject(feature);
        FeatureReference id=feature.getReference();
        if (added.containsKey(id)){
        	oldNum=((Integer)added.get(id)).intValue();
        	added.put(id,new Integer(num));
        }else{
        	if (modifiedFromOriginal.get(id)!=null) {
				oldNum=((Integer)modifiedFromOriginal.get(id)).intValue();
			}
        	modifiedFromOriginal.put(id,new Integer(num));
        }
        return oldNum;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void restore(FeatureReference id) {
        deleted.remove(id);
        deltaSize++;
    }
    public void restore(FeatureReference id,int num){
    	if (added.containsKey(id)){
        	added.put(id,new Integer(num));
        }else{
        	modifiedFromOriginal.put(id,new Integer(num));
        }
    }


    public boolean isDeleted(Feature feature){
    	return deleted.contains(feature.getReference());
    }

    public boolean isDeleted(FeatureReference featureID) {
		return deleted.contains(featureID);
	}

	public void clear() {
		added.clear();
		modifiedFromOriginal.clear();
	    expansionAdapter.close();
	    deleted.clear();//<FeatureID>
	    deltaSize=0;
	}


	public boolean hasChanges() {
		return added.size()>0 || modifiedFromOriginal.size() > 0 || deleted.size() > 0;
	}


//	public Iterator newsFeaturesIterator(long index) {
//		if (index==0)
//			return newsFeaturesIterator();
//
//		return Arrays.asList(added.values()).listIterator((int)index);
//	}
//
//	public Iterator newsFeaturesIterator() {
//		return added.values().iterator();
//	}
	public Iterator getDeleted() {
		return new DeletedIterator();

	}

	class DeletedIterator implements Iterator {
		private Boolean hasnext = null;
		private Iterator iter;
		private DefaultFeatureReference obj;

		public DeletedIterator(){
			iter = deleted.iterator();
		}

		public boolean hasNext() {
			if (hasnext != null) {
				return hasnext.booleanValue();
			}
			hasnext = Boolean.FALSE;
			while (iter.hasNext()) {
				obj = (DefaultFeatureReference) iter.next();
				if (obj.isNewFeature()) {
					continue;
				}
				hasnext = Boolean.TRUE;
				break;
			}
			return hasnext.booleanValue();
		}

		public Object next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			hasnext = null;
			return obj;
		}

		public void remove() {
			throw new UnsupportedOperationException();

		}

	}

	public Iterator getInserted() {
		return new InsertedIterator();
	}

	class InsertedIterator implements Iterator {

		private Iterator addedIter;
		private DefaultFeature obj;
		private Boolean hasnext = null;

		public InsertedIterator(){
			addedIter = added.values().iterator();
		}

		public boolean hasNext() {
			if (hasnext != null) {
				return hasnext.booleanValue();
			}
			hasnext = Boolean.FALSE;
			int pos;
			while (addedIter.hasNext()) {
				pos = ((Integer) addedIter.next()).intValue();
				obj = (DefaultFeature) expansionAdapter.getObject(pos);
				if (!deleted.contains(obj.getReference())) {
					hasnext = Boolean.TRUE;
					break;
				}
			}
			return hasnext.booleanValue();
		}

		public Object next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			hasnext = null;
			return obj.getData();
		}

		public void remove() {
			throw new UnsupportedOperationException();

		}

	}
	public Iterator getUpdated() {
		return new UpdatedIterator();
	}
	class UpdatedIterator implements Iterator{
		private Boolean hasnext = null;
		private Iterator iter;
		private DefaultFeature obj;
		private int pos;

		public UpdatedIterator() {
			iter = expansionAdapter.iterator();
			pos = -1;
		}

		public boolean hasNext() {
			if (hasnext != null) {
				return hasnext.booleanValue();
			}
			hasnext = Boolean.FALSE;
			while (iter.hasNext()) {
				pos++;
				obj = (DefaultFeature) iter.next();
				if (deleted.contains(obj.getReference())){
					continue;
				}else if (!modifiedFromOriginal.containsValue(new Integer(pos))){
					continue;
				}else {
					hasnext = Boolean.TRUE;
					break;
				}
			}
			return hasnext.booleanValue();
		}

		public Object next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			hasnext = null;
			return obj.getData();
		}

		public void remove() {
			throw new UnsupportedOperationException();

		}
	}

	public boolean hasNews() {
		return !added.isEmpty();
	}

	public long getDeltaSize() {
		return deltaSize;
	}
}
