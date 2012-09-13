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
 
package org.gvsig.app.daltransform.gui;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;

/**
 * This interface has to be implemented by the transformation
 * wizard. It has to provide 
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface DataTransformWizard {
	
	/**
	 * Returns <code>true</code> if the transformation 
	 * can be applied. 
	 * @param isApplicable
	 * If the transformation can be applied.
	 */
	public void setApplicable(boolean isApplicable);
	
	/**
	 * Returns the  {@link FeatureStore} that can is used
	 * to apply the transformation.
	 * @return
	 * The selected {@link FeatureStore}
	 */
	public FeatureStore getFeatureStore();
	
	/**
	 * Returns <code>true</code> if the selected {@link FeatureStore}
	 * has been selected from a layer. 
	 * @return
	 * If the selected {@link FeatureStore} has been selected
	 * from a layer. 
	 */
	public boolean isFeatureStoreLayer();
	
	/**
	 * Returns the selected {@link DataTransformGui}. It is used
	 * to create the {@link FeatureStoreTransform}.
	 * @return
	 * The selected {@link DataTransformGui}.
	 */
	public DataTransformGui getDataTransformGui();	
}

