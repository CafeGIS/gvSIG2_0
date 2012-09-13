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

import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;

/**
 * This interface is used to establish a relationship between 
 * feature transformations and their user interfaces. It creates 
 * the panels that are used to set the parameters that the 
 * transformation needs. 
 * 
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface DataTransformGui {
    
	/**
	 * Creates a feature transformation from a feature store. The
	 * class that implements this interface can create a transformation
	 * using this feature store and all the parameters that the user has
	 * selected. All these parameters must be known by this class
	 * @param featureStore
	 * The selected feature store
	 * @return
	 * the transformation
	 * @throws DataException
	 */
	public FeatureStoreTransform createFeatureStoreTransform(FeatureStore featureStore) throws DataException;
		
    /**
     * Creates a list of panels to set the parameters used on the transformation.
     * @return
     * a set of panels with the parameters of the transformation
     */
	public List<DataTransformWizardPanel> createPanels();

	/**
	 * @return the name that is displayed in the feature transformation
	 * list
	 */
	public String getName();
	
	/**
	 * @return a description of the feature transformation
	 */
	public String getDescription();
}

