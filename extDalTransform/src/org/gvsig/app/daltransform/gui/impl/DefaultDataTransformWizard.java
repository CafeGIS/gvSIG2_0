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

import java.util.List;

import javax.swing.ImageIcon;

import org.gvsig.app.daltransform.gui.DataTransformGui;
import org.gvsig.app.daltransform.gui.DataTransformWizard;
import org.gvsig.app.daltransform.gui.DataTransformWizardPanel;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.wizard.WizardAndami;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultDataTransformWizard extends WizardAndami implements DataTransformWizard{
	private List<DataTransformWizardPanel> transformWizardPanels = null;
	//The three default panels
	private LoadLayerWizardPanel loadLayerWizardPanel = null;
	private SelectDataStoreWizardPanel selectDataStoreWizardPanel = null;
	private SelectTransformWizardPanel selectTransformWizardPanel = null;
	
	/**
	 * @param wizard
	 */
	public DefaultDataTransformWizard(ImageIcon logo) {
		super(logo);	
		loadLayerWizardPanel = new LoadLayerWizardPanel();
		selectDataStoreWizardPanel = new SelectDataStoreWizardPanel();
		selectTransformWizardPanel = new SelectTransformWizardPanel();
		initialize();
	}

	private void initialize() {
		getWizardComponents().getFinishButton().setEnabled(false);
		getWindowInfo().setTitle(PluginServices.getText(this, "transform_wizard"));
		addDataTransformWizardPanel(selectTransformWizardPanel);
		addDataTransformWizardPanel(selectDataStoreWizardPanel);		
		getWizardComponents().setFinishAction(new DataTransformSelectionAction(this));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.DataTransformWizard#setApplicable(boolean)
	 */
	public void setApplicable(boolean isEnabled) {
		getWizardComponents().getNextButton().setEnabled(isEnabled);		
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#getDataTransformGui()
	 */
	public DataTransformGui getDataTransformGui() {
		return selectTransformWizardPanel.getFeatureTransformGui();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#setDataTransformGui(org.gvsig.app.daltransform.DataTransformGui)
	 */
	public void updateGui() {
		//Remove all the panels
		for (int i=2 ; i<getWizardComponents().getWizardPanelList().size() ; i++){
			getWizardComponents().removeWizardPanel(i);
		}	
		//Add new panels
		transformWizardPanels = getDataTransformGui().createPanels();
		for (int i=0 ; i<transformWizardPanels.size() ; i++){
			addDataTransformWizardPanel(transformWizardPanels.get(i));
		}
		addDataTransformWizardPanel(loadLayerWizardPanel);			
	}
	
	public void addDataTransformWizardPanel(DataTransformWizardPanel dataTransformWizardPanel){
		dataTransformWizardPanel.setDataTransformWizard(this);
		getWizardComponents().addWizardPanel(
				new DataTransformWizardContainer(getWizardComponents(), dataTransformWizardPanel));	
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#getFeatureStore()
	 */
	public FeatureStore getFeatureStore() {
		return selectDataStoreWizardPanel.getFeatureStore();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#isFeatureStoreLayer()
	 */
	public boolean isFeatureStoreLayer() {
		return selectDataStoreWizardPanel.isFeatureStoreLayer();
	}
	
	public boolean isLayerLoaded(){
		return loadLayerWizardPanel.isLayerLoaded();
	}
	
	public MapContext getMapContext(){
		return loadLayerWizardPanel.getMapContext();
	}
}

