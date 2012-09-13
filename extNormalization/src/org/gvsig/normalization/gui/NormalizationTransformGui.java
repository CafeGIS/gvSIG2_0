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

package org.gvsig.normalization.gui;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.app.daltransform.gui.DataTransformGui;
import org.gvsig.app.daltransform.gui.DataTransformWizardPanel;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.normalization.NormalizationTransform;
import org.gvsig.normalization.algorithm.NormalizationAlgorithm;
import org.gvsig.normalization.algorithm.impl.DefaultNormalizationAlgorithm;
import org.gvsig.normalization.pattern.Patternnormalization;

import com.iver.andami.PluginServices;

/**
 * Normalization tarnsform gui
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class NormalizationTransformGui implements DataTransformGui{
	
	private NormalizationPanel panel = null;
	private NormalizationPanelController model = null;
	
	public NormalizationTransformGui() {
		super();
		model = NormalizationPanelController.getInstance();
	}	
	
	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.FeatureTransformGui#createPanels(org.gvsig.app.daltransform.gui.FeatureTransformWizardModel)
	 */
	public List<DataTransformWizardPanel> createPanels() {
		if (panel == null){
			
			panel = new NormalizationPanel(model);
		}
		List<DataTransformWizardPanel> panels = new ArrayList<DataTransformWizardPanel>();
		panels.add(panel);
		return panels;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.FeatureTransformGui#getDescription()
	 */
	public String getDescription() {
		return PluginServices.getText(this, "normalization_description");
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */	
	public String toString() {
		return PluginServices.getText(this, "normalization");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.FeatureTransformGui#getName()
	 */
	public String getName() {
		return PluginServices.getText(this, "normalization");
	}

	public FeatureStoreTransform createFeatureStoreTransform(
			FeatureStore featureStore) throws DataException {
		//PATTERN
		Patternnormalization pat = model.getPattern();
		
		// ALGORITHM
		NormalizationAlgorithm algorithm = new DefaultNormalizationAlgorithm(
				pat);

		// TRANSFORM
		NormalizationTransform transform = new NormalizationTransform();
		transform.initialize(featureStore, pat, 5, algorithm);
		
		return transform;
	}
}

