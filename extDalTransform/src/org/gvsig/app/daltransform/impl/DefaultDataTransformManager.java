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
 
package org.gvsig.app.daltransform.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.app.daltransform.DataTransformManager;
import org.gvsig.app.daltransform.gui.DataTransformGui;
import org.gvsig.app.daltransform.gui.impl.DefaultDataTransformWizard;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultDataTransformManager implements DataTransformManager{
	private static final String TRANSFORM_GUI_EXTENSION_POINT = "TransformGuiExtensionPoint";
	private static final Logger logger = LoggerFactory.getLogger(DefaultDataTransformManager.class);
	private ExtensionPointManager extensionPoints = ToolsLocator.getExtensionPointManager();
	public static final int WIDTH = 315;
	public static final int HEIGHT = 600;

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.feature.transform.FeatureTransformManager#registerFeatureTransform(java.lang.String, java.lang.Class)
	 */
	public void registerDataTransform(String name,
			Class featureTransformPage) {
		ExtensionPoint extensionPoint = extensionPoints.add(TRANSFORM_GUI_EXTENSION_POINT, "");
		extensionPoint.append(name, name, featureTransformPage);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.feature.transform.FeatureTransformManager#getFeatureTransforms()
	 */
	public ArrayList<DataTransformGui> getDataTransforms() {
		ArrayList<DataTransformGui> transformArray = new ArrayList();
		
		ExtensionPoint ep = extensionPoints.add(TRANSFORM_GUI_EXTENSION_POINT);
		Iterator iterator = ep.iterator();
		while (iterator.hasNext()) {
			try {				
				transformArray.add((DataTransformGui)((ExtensionPoint.Extension) iterator
						.next()).create());				
			} catch (Exception e) {
				logger.error("Error creating a FeatureTranformationGui", e);
			} 
		}
		return transformArray;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformManager#createWizard()
	 */
	public IWindow createWizard() {
		DefaultDataTransformWizard wizard = new DefaultDataTransformWizard(
				PluginServices.getIconTheme().get("feature-transform"));
		wizard.setSize(HEIGHT, WIDTH);		
		return wizard;
	}

}

