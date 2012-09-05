package org.gvsig.fmap.dal.store.dxf;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class DXFStoreParameters extends AbstractDataParameters implements
		DataStoreParameters, FilesystemStoreParameters,
		NewFeatureStoreParameters {

    public static final String DYNCLASS_NAME = "DXFStoreParameters";

    private static final String FIELD_FILEPATH = "filepath";
    private static final String FIELD_SRS = "srsid";

	private static DynClass DYNCLASS = null;

    public DXFStoreParameters() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager()
				.createDynObject(DYNCLASS);
	}

	public String getDataStoreName() {
		return DXFStoreProvider.NAME;
	}

	public String getDescription() {
		return DXFStoreProvider.DESCRIPTION;
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

	public String getFileName() {
		return (String) getDynValue(FIELD_FILEPATH);
	}

	public void setFileName(String file) {
		setDynValue(FIELD_FILEPATH, file);
	}

	public boolean isValid() {
		if (getSRSID() == null || getSRSID().equals("")) {
			return false;
		}
		if (getFileName() == null) {
			return false;
		}
		return true;
	}

	public File getFile() {
		return new File(this.getFileName());
	}

	public void setFile(File file) {
		this.setFileName(file.getPath());
	}

	public FeatureType getDefaultFeatureType() {
		return null; //TODO ????
	}

	public void setDefaultFeatureType(FeatureType defaultFeatureType) {
		throw new UnsupportedOperationException();
	}

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

            field = dynClass.addDynField(FIELD_FILEPATH);
            field.setType(DataTypes.STRING);
            field.setDescription("The DXF file path");
            field.setTheTypeOfAvailableValues(DynField.SINGLE);
            field.setMandatory(true);
            DYNCLASS = dynClass;
        }
    }

//	public XMLEntity getXMLEntity() {
//		XMLEntity xmlEntity = super.getXMLEntity();
//		xmlEntity.putProperty("srsid", this.getSRSID());
//		xmlEntity.putProperty("file", this.getFileName());
//		return xmlEntity;
//	}
//
//	public void loadFromXMLEntity(XMLEntity xmlEntity)
//			throws InitializeException {
//		try {
//			this.setSRSID(xmlEntity.getStringProperty("srsid"));
//			this.setFileName(xmlEntity.getStringProperty("file"));
//		} catch (NotExistInXMLEntity e) {
//			throw new InitializeException(e);
//		}
//	}


//	public DataParameters getCopy() throws DataException {
//		DXFStoreParameters copy = new DXFStoreParameters();
//		this.copyValuesTo(copy);
//		return copy;
//	}

}