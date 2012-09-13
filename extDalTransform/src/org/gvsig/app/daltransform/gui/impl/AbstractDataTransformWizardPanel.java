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

import javax.swing.JPanel;

import org.gvsig.app.daltransform.gui.DataTransformWizard;
import org.gvsig.app.daltransform.gui.DataTransformWizardPanel;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public abstract class AbstractDataTransformWizardPanel extends JPanel implements DataTransformWizardPanel {
	protected static final Logger logger = LoggerFactory.getLogger(AbstractDataTransformWizardPanel.class);
	private DefaultDataTransformWizard dataTransformWizard = null;

	/**
	 * @param featureStore
	 */
	public AbstractDataTransformWizardPanel() {
		super();
	}	
	
	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizardPanel#setDataTransformWizard(org.gvsig.app.daltransform.DataTransformWizard)
	 */
	public void setDataTransformWizard(DataTransformWizard dataTransformWizard) {
		this.dataTransformWizard = (DefaultDataTransformWizard)dataTransformWizard;		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#getJPanel()
	 */
	public JPanel getJPanel() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#getPanelTitle()
	 */
	public String getPanelTitle() {
		return PluginServices.getText(this, "transform_parameters");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#isNextButtonEnabled()
	 */
	public boolean isNextButtonEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#lastPanel()
	 */
	public void lastPanel() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#nextPanel()
	 */
	public void nextPanel() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#updatePanel()
	 */
	public void updatePanel() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the featureStore
	 */
	public FeatureStore getFeatureStore() {
		if (dataTransformWizard != null){
			return dataTransformWizard.getFeatureStore();
		}
		return null;
	}
	
	/**
	 * @return the dataTransformWizard
	 */
	public DefaultDataTransformWizard getDataTransformWizard() {
		return dataTransformWizard;
	}	
	
}

