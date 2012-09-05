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
* 2008 IVER T.I. S.A.   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.mapcontext;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.fmap.mapcontext.impl.DefaultMapContextDrawer;
import org.gvsig.fmap.mapcontext.impl.DefaultMapContextManager;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.LibraryException;
import org.gvsig.tools.locator.LocatorException;

/**
 * @author jmvivo
 *
 */
public class MapContextLibrary extends BaseLibrary {

	private ToolsLibrary toolsLib;
	private GeometryLibrary geomLibrary;

	public void initialize() throws LocatorException {
		super.initialize();
		toolsLib = new ToolsLibrary();

		toolsLib.initialize();

		geomLibrary = new GeometryLibrary();

		geomLibrary.initialize();


		LayerFactory.getInstance().registerLayerToUseForStore(
				FeatureStore.class, FLyrVect.class);
		
		MapContextLocator
                .registerMapContextManager(DefaultMapContextManager.class);
	}

	public void postInitialize() throws LocatorException {
		super.postInitialize();

		toolsLib.postInitialize();
		geomLibrary.postInitialize();


		DynObjectManager dynManager = ToolsLocator.getDynObjectManager();

		DynField field;
		DynClass fLayerDynClass = dynManager.get(FLayer.DYNCLASS_NAME);
		if (fLayerDynClass == null) {
			fLayerDynClass = dynManager.add(FLayer.DYNCLASS_NAME);

			field = fLayerDynClass.addDynField("name");
			field.setType(DataTypes.STRING);

			field = fLayerDynClass.addDynField("SRS");
			field.setType(DataTypes.SRS);
		}
		
		try {
            MapContextLocator.getMapContextManager()
                    .setDefaultMapContextDrawer(DefaultMapContextDrawer.class);
        } catch (MapContextException ex) {
            throw new LibraryException(ex);
        }
	}

}
