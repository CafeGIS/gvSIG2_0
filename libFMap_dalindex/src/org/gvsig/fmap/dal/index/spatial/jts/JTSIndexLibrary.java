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
* 2008 {{Company}}   {{Task}}
*/


package org.gvsig.fmap.dal.index.spatial.jts;

import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;


public class JTSIndexLibrary extends BaseLibrary {

    public void initialize() throws ReferenceNotRegisteredException {
    	super.initialize();

    	DALLibrary dal = new DALLibrary();
    	dal.initialize();
    	DataManagerProviderServices man = (DataManagerProviderServices) DALLocator.getDataManager();


    	if (!man.getFeatureIndexProviders().contains(JTSQuadtree.NAME)) {
			man.registerFeatureIndexProvider(JTSQuadtree.NAME, "Quadtree index based on JTS",
					JTSQuadtree.class, DataTypes.GEOMETRY);
			man.setDefaultFeatureIndexProviderName(DataTypes.GEOMETRY, JTSQuadtree.NAME);
    	}
    }

    public void postInitialize() throws ReferenceNotRegisteredException {
        // TODO: log library postInitialization
        // Nothing to check
    }
}

