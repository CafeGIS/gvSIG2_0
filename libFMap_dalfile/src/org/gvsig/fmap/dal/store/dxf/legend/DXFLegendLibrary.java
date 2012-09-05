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
package org.gvsig.fmap.dal.store.dxf.legend;

import org.gvsig.fmap.dal.store.dxf.DXFStoreProvider;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * @author jmvivo
 *
 */
public class DXFLegendLibrary extends BaseLibrary {

//	public static final String DXFLEGEND_DYNCLASS_NAME = "DXFLegend";

	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();
	}

	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();

		DynObjectManager dynMan = ToolsLocator.getDynObjectManager();
		DynClass dynClass = dynMan.get(DXFStoreProvider.DYNCLASS_NAME);

		if (dynClass == null) {
			dynClass = dynMan.add(DXFStoreProvider.DYNCLASS_NAME);
		}




		DXFGetLegendBuilder.register(dynClass);
		DXFGetLegend.register(dynClass);
		DXFGetLabeling.register(dynClass);



	}

}
