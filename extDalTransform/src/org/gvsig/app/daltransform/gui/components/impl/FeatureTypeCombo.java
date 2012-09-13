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

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class FeatureTypeCombo extends JComboBox{
	protected static final Logger logger = LoggerFactory.getLogger(FeatureTypeCombo.class);

	public FeatureTypeCombo() {
		super();
		this.setModel(new DefaultComboBoxModel());
	}

	public void addFeatureStore(FeatureStore featureStore){
		removeAllItems();
		List<FeatureType> featureTypes;
		try {
			featureTypes = featureStore.getFeatureTypes();
			for (int i=0 ; i<featureTypes.size() ; i++){
				((DefaultComboBoxModel)getModel()).addElement(featureTypes.get(i));
			}
		} catch (DataException e) {
			logger.error("Error updating the panel", e);
		}		
	}
	
	public FeatureType getSelectedFeatureType(){
		return (FeatureType)getSelectedItem();
	}	
}

