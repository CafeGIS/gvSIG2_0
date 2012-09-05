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
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.fmap.dal.store.raster;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class RasterStoreParameters extends AbstractDataParameters implements
		DataStoreParameters, FilesystemStoreParameters {

    public static final String DYNCLASS_NAME = "RasterStoreParameters";

	protected static final String FIELD_FILENAME = "filename";
	private static final String FIELD_SRS = "srs";

	protected static DynClass DYNCLASS = null;

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			field = dynClass.addDynField(FIELD_FILENAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("File name");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);


			field = dynClass.addDynField(FIELD_SRS);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("SRS");
			field.setType(DataTypes.SRS);
			field.setMandatory(true);

			DYNCLASS = dynClass;
		}

	}

	public RasterStoreParameters() {
		super();
		initialize();
	}

	protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(
						DYNCLASS);
	}

	public String getDataStoreName() {
		return RasterStoreProvider.NAME;
	}

	public boolean isValid() {
		return (this.getFileName() != null);
	}


	public String getFileName() {
		return (String) this.getDynValue(FIELD_FILENAME);
	}

	public File getFile() {
		return new File((String) this.getDynValue(FIELD_FILENAME));
	}

	public void setFile(File aFile) {
		this.setDynValue(FIELD_FILENAME, aFile.getPath());
	}

	public void setFileName(String aFileName) {
		this.setDynValue(FIELD_FILENAME, aFileName);
	}

	public String getDescription() {
		return RasterStoreProvider.DESCRIPTION;
	}

	public String getSRSID() {
		IProjection srs = (IProjection) getDynValue(FIELD_SRS);
		if (srs == null) {
			return null;
		}
		return srs.getAbrev();
	}

	public void setSRSID(String srsid) {
		if (srsid == null) {
			setDynValue(FIELD_SRS, null);
		} else {
			setDynValue(FIELD_SRS, CRSFactory.getCRS(srsid));
		}
	}

	public void setSRS(IProjection srs) {
		setDynValue(FIELD_SRS, srs);
	}

	public IProjection getSRS() {
		if (this.getSRSID() == null) {
			return null;
		}
		return (IProjection) getDynValue(FIELD_SRS);
	}

}
