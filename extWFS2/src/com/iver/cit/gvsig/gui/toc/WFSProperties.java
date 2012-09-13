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
 
package com.iver.cit.gvsig.gui.toc;

import java.awt.BorderLayout;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.fmap.dal.store.wfs.WFSStoreParameters;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.WFSParamsPanel;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeatureManager;
import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSProperties extends AbstractThemeManagerPage{
	private WFSParamsPanel panel = null;
	private FLyrVect layer = null;
	private final String wfs_properties_extensionpoint_name = "WFSPropertiesDialog";
	
	public WFSProperties() {
		super();
		setLayout(new BorderLayout());		
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage#acceptAction()
	 */
	public void acceptAction() {
		applyAction();		
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage#applyAction()
	 */	
	public void applyAction() {		
		FLyrVect newLayer = (FLyrVect)panel.getLayer();
		if (newLayer != null){
			try{
				layer.getMapContext().getLayers().replaceLayer(layer.getName(), newLayer);
				layer = newLayer;
			} catch (LoadLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage#cancelAction()
	 */
	public void cancelAction() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage#getName()
	 */
	public String getName() {
		return PluginServices.getText(this, "WFS");
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage#setModel(org.gvsig.fmap.mapcontext.layers.FLayer)
	 */
	public void setModel(FLayer layer) {
		this.layer = (FLyrVect)layer;
		PanelGroupManager manager = PanelGroupManager.getManager();
		manager.registerPanelGroup(WFSParamsPanel.class);
		manager.setDefaultType(WFSParamsPanel.class);
		try {
			boolean firstExecution = (panel == null);
			WFSServerExplorer serverExplorer = (WFSServerExplorer)((DataStore)((FLyrVect)layer).getDataStore()).getExplorer();
			WFSSelectedFeatureManager selectedFeatureManager = WFSSelectedFeatureManager.getInstance(serverExplorer);
			panel = (WFSParamsPanel) manager.getPanelGroup(null);
			panel.loadPanels(new PanelGroupLoaderFromExtensionPoint(wfs_properties_extensionpoint_name));
			panel.setServerExplorer(serverExplorer);
			panel.setVisible(true);			
			if (firstExecution){				
				add(panel, BorderLayout.CENTER);				
			}			
			this.doLayout();
			//Refresh the panel 
			
			WFSStoreParameters storeParameters = (WFSStoreParameters)((FLyrVect)layer).getDataStore().getParameters();
			String featureType = storeParameters.getFeatureType();
			panel.setSelectedFeature(selectedFeatureManager.getFeatureInfo(storeParameters.getFeatureNamespace(),
					storeParameters.getFeatureType()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}

