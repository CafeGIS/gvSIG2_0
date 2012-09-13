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
 
package org.gvsig.app.daltransform.gui.impl;

import jwizardcomponent.FinishAction;

import org.gvsig.app.daltransform.gui.DataTransformGui;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DataTransformSelectionAction extends FinishAction{
	private static final Logger logger = LoggerFactory.getLogger(DataTransformSelectionAction.class);
	private DefaultDataTransformWizard dataTransformWizard = null;
	
	public DataTransformSelectionAction(DefaultDataTransformWizard dataTransformWizard) {
		super(dataTransformWizard.getWizardComponents());		
		this.dataTransformWizard = dataTransformWizard;
	}

	/* (non-Javadoc)
	 * @see jwizardcomponent.Action#performAction()
	 */
	public void performAction() {		
		//Gets the selected transformation
		DataTransformGui featureTransformGui = dataTransformWizard.getDataTransformGui();
			
		//Gets the selected FeatureStore
		FeatureStore featureStore = dataTransformWizard.getFeatureStore();
				
		try {			
			//Gets the transform
			FeatureStoreTransform featureStoreTransform = featureTransformGui.createFeatureStoreTransform(featureStore);
			
			//Apply the transformation
			featureStore.getTransforms().add(featureStoreTransform);
			
			//Create and load a new layer...
			if (dataTransformWizard.isLayerLoaded()){
				FLayer layer = LayerFactory.getInstance().createLayer(featureTransformGui.toString(),
						featureStore);
				dataTransformWizard.getMapContext().getLayers().addLayer(layer);
			}
		} catch (DataException e) {
			logger.error("Error creating the transformation", e);
		} catch (LoadLayerException e) {
			logger.error("Error loading the layer", e);
		}
		//Closing the window
		PluginServices.getMDIManager().closeWindow(dataTransformWizard);
	}

}

