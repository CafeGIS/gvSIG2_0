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

package org.gvsig.app.join.daltransform;

import org.gvsig.app.daltransform.gui.impl.SelectDataStoreWizardPanel;
import org.gvsig.fmap.dal.exception.DataException;

import com.iver.andami.PluginServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class SelectSecondDataStoreWizardPanel extends SelectDataStoreWizardPanel{
	private SelectParametersWizardPanel selectParametersWizardPanel = null;
	
	/**
	 * @param featureTransformWizardModel
	 */
	public SelectSecondDataStoreWizardPanel(SelectParametersWizardPanel selectParametersWizardPanel) {
		super();	
		this.selectParametersWizardPanel = selectParametersWizardPanel;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.SelectDataStoreWizard#getPanelTitle()
	 */	
	public String getPanelTitle() {
		return PluginServices.getText(this, "transform_second_datastore_selection");
	}

	/* (non-Javadoc)
	 * @see jwizardcomponent.JWizardPanel#next()
	 */	
	public void nextPanel() {
		try {
			selectParametersWizardPanel.updateFeatureStores(getSelectedFeatureStore());
		} catch (DataException e) {
			logger.error("Error updating the params panel", e);
		}
				
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.SelectDataStoreWizard#update()
	 */
	public void updatePanel() {
		removeFeatureStore(getDataTransformWizard().getFeatureStore());
		super.updatePanel();
	}
	
	
}

