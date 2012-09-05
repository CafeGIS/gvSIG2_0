package org.gvsig.fmap.data.feature.db.jdbc;

import java.util.Map;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.data.feature.db.DBStoreParameters;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;

public abstract class JDBCStoreParameters extends
		DBStoreParameters {

	public static final int IS_VIEW = 1;
	public static final int NOT_IS_VIEW = 0;
	public static final int UNKNOW_IS_VIEW = -1;

	protected Map createDefaultValuesMap() {
		Map defaultParams = super.createDefaultValuesMap();
		defaultParams.put("isView", new Integer(UNKNOW_IS_VIEW));
		return defaultParams;
	}

	public abstract String getUrl();

	public void setIsView(int isView) {
		this.put("isView", new Integer(UNKNOW_IS_VIEW));
	}

	public int getIsView() {
		return ((Integer) this.getAttribute("isView")).intValue();
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xmlEntity = super.getXMLEntity();

		xmlEntity.putProperty("isView", this.getIsView());
		return xmlEntity;
	}

	public void loadFromXMLEntity(XMLEntity xmlEntity)
			throws InitializeException {

		super.loadFromXMLEntity(xmlEntity);

		try {
			int isView = xmlEntity.getIntProperty("isView");

			if (!(isView == IS_VIEW || isView == NOT_IS_VIEW)) {
				this.remove("isView");
			} else {
				this.setIsView(isView);
			}
		} catch (NotExistInXMLEntity e) {
			this.remove("isView");
		}
	}

	public String tableID() {
		if (this.getSchema() == null || this.getSchema() == "") {
			return this.getTableName();
		}
		return this.getSchema()+"."+this.getTableName();
	}

}
