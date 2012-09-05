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

package org.gvsig.fmap.dal.store.jdbc;

import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class JDBCNewStoreParameters extends JDBCStoreParameters implements
        NewFeatureStoreParameters {

    public static final String DYNCLASS_NAME = "JDBCNewStoreParameters";

    private FeatureType defaultFeatureType;

    protected static DynClass DYNCLASS = null;

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {

			dynClass = dynman.add(DYNCLASS_NAME,
					"new JDBC Store parameters");
			dynClass.extend(JDBCStoreParameters.DYNCLASS_NAME);
			DYNCLASS = dynClass;
		}

	}

    protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(DYNCLASS);
	}


    public FeatureType getDefaultFeatureType() {
        return this.defaultFeatureType;
    }

    public void setDefaultFeatureType(FeatureType defaultFeatureType) {
        this.defaultFeatureType = defaultFeatureType;
    }

}