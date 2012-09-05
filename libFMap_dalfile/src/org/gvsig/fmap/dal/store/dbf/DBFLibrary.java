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

import org.gvsig.fmap.dal.DALFileLocator;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectValueItem;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;


public class DBFLibrary extends BaseLibrary {

	public static final String DYNFIELD_CODEPAGE_NAME = "codePage";

	static DynField addCodePageField(DynClass dynClass) {

		DynField dynField = dynClass.addDynField(DYNFIELD_CODEPAGE_NAME);
		dynField.setDescription("Code Page");
		dynField.setDefaultValue(new Byte((byte) 0x2));
		dynField.setType(DataTypes.BYTE);
		dynField.setTheTypeOfAvailableValues(DynField.CHOICE);
		dynField.setAvailableValues(new DynObjectValueItem[] {
			new DynObjectValueItem(new Byte((byte) 0x1),"DOS USA"),
			new DynObjectValueItem(new Byte((byte) 0x2),"DOS Multilingual"),
			new DynObjectValueItem(new Byte((byte) 0x3),"Windows ANSI"),
			new DynObjectValueItem(new Byte((byte) 0x4),"Standard Macintosh"),
			new DynObjectValueItem(new Byte((byte) 0x64),"EE MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x65),"Nordic MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x66),"Russian MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x67),"Icelandic MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x68),"Kamenicky (Czech) MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x69),"Mazovia (Polish) MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x6A),"Greek MS-DOS (437G)"),
			new DynObjectValueItem(new Byte((byte) 0x6B),"Turkish MS-DOS"),
			new DynObjectValueItem(new Byte((byte) 0x96),"Russian Macintosh"),
			new DynObjectValueItem(new Byte((byte) 0x97),"Eastern European Macintosh"),
			new DynObjectValueItem(new Byte((byte) 0x98),"Greek Macintosh"),
			new DynObjectValueItem(new Byte((byte) 0xC8),"Windows EE"),
			new DynObjectValueItem(new Byte((byte) 0xC9),"Russian Windows"),
			new DynObjectValueItem(new Byte((byte) 0xCA),"Turkish Windows"),
			new DynObjectValueItem(new Byte((byte) 0xCB),"Greek Windows"),

		});
		return dynField;

	}

	public void postInitialize() throws ReferenceNotRegisteredException {
		super.postInitialize();


		DBFStoreParameters.registerDynClass();
		DBFNewStoreParameters.registerDynClass();
		DBFStoreProvider.registerDynClass();

        DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
				.getDataManager();

		if (!dataman.getStoreProviders().contains(DBFStoreProvider.NAME)) {
			dataman.registerStoreProvider(DBFStoreProvider.NAME,
					DBFStoreProvider.class, DBFStoreParameters.class);
		}



		DALFileLocator.getFilesystemServerExplorerManager().registerProvider(
				DBFStoreProvider.NAME, DBFStoreProvider.DESCRIPTION,
				DBFFilesystemServerProvider.class);
	}

}
