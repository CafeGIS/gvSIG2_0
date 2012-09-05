/**
 *
 */
package org.gvsig.fmap.dal.store.lidar;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author jmvivo
 *
 */
public class LiDARStoreParameters extends AbstractDataParameters implements
		DataStoreParameters, FilesystemStoreParameters {
	public static final String DYNCLASS_NAME = "LiDARStoreParameters";

	protected static final String FIELD_FILENAME = "filename";
	private static final String FIELD_SRS = "srsid";

	protected static DynClass DYNCLASS = null;

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

            field = dynClass.addDynField(FIELD_SRS);
			field.setType(DataTypes.SRS);
			field.setDescription("The SRS identifier");
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setMandatory(true);

			field = dynClass.addDynField(FIELD_FILENAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("LiDAR file name");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);
			DYNCLASS = dynClass;
		}

	}

	public LiDARStoreParameters() {
		super();
		initialize();
	}

	protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(
						LiDARStoreParameters.DYNCLASS);
	}

	public String getDataStoreName() {
		return LiDARStoreProvider.NAME;
	}

	public boolean isValid() {
		return (this.getFileName() != null);
	}

	public File getFile() {
		return new File(this.getFileName());
	}

	public void setFile(File aFile) {
		this.setDynValue(FIELD_FILENAME, aFile.getPath());
	}

	public void setFile(String aFileName) {
		this.setDynValue(FIELD_FILENAME, aFileName);
	}

	public String getFileName() {
		return (String) this.getDynValue(FIELD_FILENAME);
	}
	public void setFileName(String aFileName) {
		this.setDynValue(FIELD_FILENAME, aFileName);
	}

	public String getDescription() {
		return DBFStoreProvider.DESCRIPTION;
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
