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

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class NumericFeatureTypeAttributesCombo extends FeatureTypeAttributesCombo{

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.combos.FeatureTypeAttributesCombo#addFeatureAttributeDescriptor(org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor)
	 */
	public void addFeatureAttributeDescriptor(
			FeatureAttributeDescriptor featureAttributeDescriptor) {
		int type = featureAttributeDescriptor.getDataType();
		if ((type == DataTypes.DOUBLE) ||
				(type == DataTypes.FLOAT) ||
				(type == DataTypes.INT) ||
				(type == DataTypes.LONG)){
			super.addFeatureAttributeDescriptor(featureAttributeDescriptor);
		}
	}	
}

