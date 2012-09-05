/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.mapcontext.layers.vectorial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.AbstractFeatureStoreTransform;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * @author jmvivo
 *
 */
public class ReprojectDefaultGeometry extends AbstractFeatureStoreTransform {

	private IProjection targetSRS;
	private ICoordTrans ct;
	private IProjection sourceSRS;
	private FeatureType orgDefFeatureType;
	private List orgFeatureTypes;

	public IProjection getTargetSRS() {
		return targetSRS;
	}

	public void setTargetSRS(IProjection targetSRS) {
		this.targetSRS = targetSRS;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStoreTransform#applyTransform(org.gvsig.fmap.dal.feature.Feature, org.gvsig.fmap.dal.feature.EditableFeature)
	 */
	public void applyTransform(Feature source, EditableFeature target)
			throws DataException {
		FeatureAttributeDescriptor attr;
		Iterator iter = target.getType().iterator();
		int defGeomIndex = target.getType().getDefaultGeometryAttributeIndex();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor)iter.next();
			if (attr.getIndex() == defGeomIndex) {
				Geometry geom =source.getDefaultGeometry();
				geom.reProject(ct);
				target.setDefaultGeometry(geom);
			} else {
				target.set(attr.getIndex(), source.get(attr.getName()));
			}

		}

	}

	public void setFeatureStore(FeatureStore store) {
		try {
			orgDefFeatureType = store.getDefaultFeatureType();
			orgFeatureTypes = store.getFeatureTypes();
			EditableFeatureType defFType = orgDefFeatureType
					.getEditable();
			EditableFeatureAttributeDescriptor attr = (EditableFeatureAttributeDescriptor) defFType
					.getAttributeDescriptor(defFType
							.getDefaultGeometryAttributeName());
			sourceSRS = attr.getSRS();
			ct = sourceSRS.getCT(targetSRS);
			attr.setSRS(this.targetSRS);
			FeatureType defaultType = defFType.getNotEditableCopy();
			List types = new ArrayList();
			Iterator iter = orgFeatureTypes.iterator();
			FeatureType tmp;
			while (iter.hasNext()) {
				tmp = (FeatureType) iter.next();
				if (tmp.getId().equals(defaultType.getId())) {
					types.add(defaultType);
				} else {
					types.add(tmp);
				}
			}

			this.setFeatureTypes(types, defaultType);
		} catch (DataException e) {
			// FIXME
			throw new RuntimeException(e);
		}

		super.setFeatureStore(store);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStoreTransform#getSourceFeatureTypeFrom(org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		EditableFeatureType result = null;
		FeatureType tmp;
		EditableFeatureAttributeDescriptor attr;
		Iterator iter = orgFeatureTypes.iterator();
		Iterator iterAttr;
		while (iter.hasNext()) {
			tmp = (FeatureType) iter.next();
			if (tmp.getId().equals(targetFeatureType.getId())) {
				result = tmp.getEditable();
				iterAttr = result.iterator();
				while (iterAttr.hasNext()) {
					attr = (EditableFeatureAttributeDescriptor) iterAttr.next();
					if (targetFeatureType.getIndex(attr.getName()) < 0) {
						iterAttr.remove();
					}
				}
				break;
			}
		}
		return result.getNotEditableCopy();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStoreTransform#isTransformsOriginalValues()
	 */
	public boolean isTransformsOriginalValues() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#loadState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#loadFromState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

}
