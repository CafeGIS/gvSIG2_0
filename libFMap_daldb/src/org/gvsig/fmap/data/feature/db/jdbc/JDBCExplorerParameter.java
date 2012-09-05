package org.gvsig.fmap.data.feature.db.jdbc;

import java.util.Map;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.data.feature.db.DBExplorerParameters;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;

public abstract class JDBCExplorerParameter extends DBExplorerParameters {

	public abstract String getUrl();

	protected Map createDefaultValuesMap() {
		Map defaultValues = super.createDefaultValuesMap();
		defaultValues.put("showInformationDBTables", Boolean.FALSE);
		return defaultValues;
	}

	public boolean isShowInformationDBTables() {
		return ((Boolean) this.getAttribute("showInformationDBTables")).booleanValue();
	}

	public void setShowInformationDBTables(boolean showInformationDBTables) {
		this.put("showInformationDBTables",
				new Boolean(showInformationDBTables));
	}

	public XMLEntity getXMLEntity() {
		XMLEntity entity = super.getXMLEntity();
		entity.putProperty("showInformationDBTables",
				this
				.isShowInformationDBTables());
		return entity;

	}

	public void loadFromXMLEntity(XMLEntity xmlEntity)
			throws InitializeException {
		super.loadFromXMLEntity(xmlEntity);
		try {
			this.setShowInformationDBTables(xmlEntity
					.getBooleanProperty("showInformationDBTables"));
		} catch (NotExistInXMLEntity e) {
			// Default value
			this.remove("showInformationDBTables");
		}
	}

}
