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
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.app.eventtheme.dal.feature;

import java.util.Arrays;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.AbstractFeatureStoreTransform;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class EventThemeTransform extends AbstractFeatureStoreTransform {
	private String xFieldName = null;
	private String yFieldName = null;
	private String geometryFieldName = null;
	private FeatureType originalFeatureType;
	private static final GeometryManager geometryManager = GeometryLocator.getGeometryManager();
		
	public EventThemeTransform() {
		super();
	}

	public void initialize(FeatureStore store, String geometryFieldName, String xFieldName, String yFieldName) throws DataException{
		setFeatureStore(store);
		this.xFieldName = xFieldName;
		this.yFieldName = yFieldName;
		if ((geometryFieldName == null) || (geometryFieldName.equals(""))){
			this.geometryFieldName = "the_geom";
		}else{
			this.geometryFieldName = geometryFieldName;
		}
		this.originalFeatureType = this.getFeatureStore()
		.getDefaultFeatureType();
		
		EditableFeatureType type = this.getFeatureStore().getDefaultFeatureType().getEditable();
		if (type.get(this.geometryFieldName) == null){
			EditableFeatureAttributeDescriptor attributeDescriptor = type.add(this.geometryFieldName,  DataTypes.GEOMETRY);
			attributeDescriptor.setGeometryType(TYPES.POINT);
			attributeDescriptor.setGeometrySubType(SUBTYPES.GEOM2D);			
		}
		
		type.setDefaultGeometryAttributeName(this.geometryFieldName);
		FeatureType[] types = new FeatureType[] { type.getNotEditableCopy() };
		setFeatureTypes(Arrays.asList(types), types[0]);
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStoreTransform#applyTransform(org.gvsig.fmap.dal.feature.Feature, org.gvsig.fmap.dal.feature.EditableFeature)
	 */
	public void applyTransform(Feature source, EditableFeature target)
			throws DataException {
		this.copySourceToTarget(source, target);
		
		Object x = source.get(xFieldName);
		Object y = source.get(yFieldName);
		Double dx = Double.valueOf(x.toString());
		Double dy = Double.valueOf(y.toString());
		try {			
			Point point = geometryManager.createPoint(dx, dy, SUBTYPES.GEOM2D);
			target.set(geometryFieldName, point);
			target.setDefaultGeometry(point);				
		} catch (CreateGeometryException e) {
			throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(TYPES.POINT, SUBTYPES.GEOM2D, e);
		}		
	}
	
	/**
	 * @param source
	 * @param target
	 */
	private void copySourceToTarget(Feature source, EditableFeature target) {
		FeatureAttributeDescriptor attr, attrTrg;
		FeatureType ftSrc = source.getType();
		FeatureType ftTrg = target.getType();


		for (int i = 0; i < source.getType().size(); i++) {
			attr = ftSrc.getAttributeDescriptor(i);
			if (ftTrg.getIndex(attr.getName()) > -1) {
				try {
					target.set(attr.getName(), source.get(i));
				} catch (IllegalArgumentException e) {
					attrTrg = ftTrg.getAttributeDescriptor(attr.getName());
					target.set(attrTrg.getIndex(), attrTrg.getDefaultValue());
				}

			}
		}

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStoreTransform#getSourceFeatureTypeFrom(org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		return this.originalFeatureType;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeatureStoreTransform#isTransformsOriginalValues()
	 */
	public boolean isTransformsOriginalValues() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#saveToState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#setState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void setState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#loadFromState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void loadFromState(PersistentState state)
			throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

}

