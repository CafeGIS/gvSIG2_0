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

package org.gvsig.fmap.dal.store.shp;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.shp.utils.SHP;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.exception.NotYetImplemented;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

public class SHPStoreParameters extends DBFStoreParameters {

    public static final String DYNCLASS_NAME = "SHPStoreParameters";

	private static final String FIELD_SHXFILENAME = "shxfilename";
    private static final String FIELD_SHPFILENAME = "shpfilename";

	private static final String FIELD_SRS = "srs";

    protected static DynClass DYNCLASS = null;

    protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager()
				.createDynObject(DYNCLASS);
	}

	public String getDataStoreName() {
		return SHPStoreProvider.NAME;
	}

	public boolean isValid() {
		return super.isValid() && (this.getSHPFileName() != null);
	}

	public File getFile() {
		return new File(this.getSHPFileName());
	}

	public void setFile(File aFile) {
		this.setSHPFile(aFile);
	}
	public void setFile(String aFileName) {
		this.setSHPFileName(aFileName);
	}
	public String getSHPFileName() {
		return (String) this.getDynValue(FIELD_SHPFILENAME);
	}

	public File getSHPFile() {
		return new File((String) this.getDynValue(FIELD_SHPFILENAME));
	}

	public void setSHPFile(File aFile) {
		this.setDynValue(FIELD_SHPFILENAME, aFile.getPath());
		if (this.getDBFFileName() == null){
			this.setDBFFile(SHP.getDbfFile(aFile));
		}
		if (this.getSHXFileName() == null) {
			this.setSHXFile(SHP.getShxFile(aFile));
		}
	}

	public void setSHPFileName(String aFileName) {
		this.setDynValue(FIELD_SHPFILENAME, aFileName);
		if (this.getDBFFileName() == null) {
			this.setDBFFile(SHP.getDbfFile(new File(aFileName)));
		}
		if (this.getSHXFileName() == null) {
			this.setSHXFile(SHP.getShxFile(new File(aFileName)));
		}
	}

	public String getSHXFileName() {
		return (String) this.getDynValue(FIELD_SHXFILENAME);
	}

	public File getSHXFile() {
		return new File((String) this.getDynValue(FIELD_SHXFILENAME));
	}
	public void setSHXFile(File aFile) {
		this.setDynValue(FIELD_SHXFILENAME, aFile.getPath());
	}

	public void setSHXFileName(String aFileName) {
		this.setDynValue(FIELD_SHXFILENAME, aFileName);
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

	public String getDescription() {
		return SHPStoreProvider.DESCRIPTION;
	}

	public void getState(PersistentState arg0) throws PersistenceException {
		// TODO Auto-generated method stub
		throw new NotYetImplemented();
	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub
		super.loadFromState(state);
		throw new NotYetImplemented();
	}

    protected static void registerDynClass() {
        DynObjectManager dynman = ToolsLocator.getDynObjectManager();
        DynClass dynClass;
        DynField field;
        if (DYNCLASS == null) {
        	DynClass dbfDynClass = DBFStoreParameters.DYNCLASS;
			dynClass = dynman.add(DYNCLASS_NAME);

            field = dynClass.addDynField(FIELD_SHPFILENAME);
            field.setDescription("SHP file name");
            field.setType(DataTypes.STRING);
            field.setMandatory(true);

            field = dynClass.addDynField(FIELD_SHXFILENAME);
            field.setDescription("SHX file name");
            field.setType(DataTypes.STRING);
            field.setMandatory(true);

            field = dynClass.addDynField(FIELD_SRS);
			field.setDescription("SRS");
			field.setType(DataTypes.SRS);
			field.setMandatory(true);

            // The SHP store parameters extend the DBF store parameters
            dynClass.extend(dbfDynClass);

            DYNCLASS = dynClass;
        }
    }
}
