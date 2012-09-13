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
 
package org.gvsig.app.daltransform.gui.components.impl;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class FeatureTypeAttributesCombo extends JComboBox {
	protected static final Logger logger = LoggerFactory.getLogger(FeatureTypeAttributesCombo.class);

	public FeatureTypeAttributesCombo() {
		super();
		this.setModel(new DefaultComboBoxModel());
	}

	public void addFeatureAttrubutes(FeatureStore featureStore) throws DataException{
		addFeatureAttributes(featureStore.getDefaultFeatureType());
	}
	
	public void addFeatureAttributes(FeatureType featureType){
		removeAllItems();
		FeatureAttributeDescriptor[] descriptors = featureType.getAttributeDescriptors();
		for (int i=0 ; i<descriptors.length ; i++){
			addFeatureAttributeDescriptor(descriptors[i]);						
		}		
	}

	public void addFeatureAttributeDescriptor(FeatureAttributeDescriptor featureAttributeDescriptor){
		((DefaultComboBoxModel)getModel()).addElement(new FeatureTypeAttributeWrapper(featureAttributeDescriptor));
	}
		
	public FeatureAttributeDescriptor getSelectedFeatureAttributeDescriptor(){
		return ((FeatureTypeAttributeWrapper)getSelectedItem()).getFeatureAttributeDescriptor();
	}	
}

