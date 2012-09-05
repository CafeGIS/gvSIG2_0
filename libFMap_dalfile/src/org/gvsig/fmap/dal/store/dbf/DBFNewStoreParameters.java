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

package org.gvsig.fmap.dal.store.dbf;

import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class DBFNewStoreParameters extends DBFStoreParameters implements
		NewFeatureStoreParameters {

    public static final String DYNCLASS_NAME = "DBFNewStoreParameters";

	private static final String FIELD_DBFFILENAME = "dbffilename";

	protected static DynClass DYNCLASS = null;

	private FeatureType defaultFeatureType;

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {

			dynClass = dynman.add(DYNCLASS_NAME,
					"new DBF File Store parameters");
			field = DBFLibrary.addCodePageField(dynClass);
			dynClass.extend(DBFStoreParameters.DYNCLASS);
			DYNCLASS = dynClass;
		}

	}

	public byte getCodePage(){
		return ((Byte) this.getDynValue(DBFLibrary.DYNFIELD_CODEPAGE_NAME))
				.byteValue();
	}

	public void setCodePage(byte value){
		// TODO check value
		setDynValue(DBFLibrary.DYNFIELD_CODEPAGE_NAME, new Byte(value));
	}

	public FeatureType getDefaultFeatureType() {
		return this.defaultFeatureType;
	}

	public void setDefaultFeatureType(FeatureType defaultFeatureType) {
		this.defaultFeatureType = defaultFeatureType;
	}

	protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(
						DBFNewStoreParameters.DYNCLASS);
	}

}
