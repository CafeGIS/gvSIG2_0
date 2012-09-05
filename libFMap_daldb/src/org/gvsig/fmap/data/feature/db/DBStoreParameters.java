package org.gvsig.fmap.data.feature.db;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.fmap.geom.primitive.Envelope;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;

public abstract class DBStoreParameters extends AbstractDataParameters
		implements DataStoreParameters, DBParameters {

	protected Map createDefaultValuesMap() {
		Map defaultValues = new HashMap(16);
		defaultValues.put("user", null);
		defaultValues.put("passw", null);
		defaultValues.put("schema", null);
		defaultValues.put("catalog", null);
		defaultValues.put("host", null);
		defaultValues.put("port", null);
		defaultValues.put("db", null);

		defaultValues.put("fields", null);

		defaultValues.put("baseFilter", null);
		defaultValues.put("baseOrder", null);
		defaultValues.put("tableName", null);

		defaultValues.put("fieldsId", null);

		defaultValues.put("sqlSoure", null);
		defaultValues.put("workingArea", null);
		defaultValues.put("SRISD", null);
		defaultValues.put("defaultGeometryField", null);

		return defaultValues;
	}

	public String getDb() {
		return (String) this.getAttribute("db");
	}

	public void setDb(String db) {
		this.put("db", db);
	}
	public String getHost() {
		return (String) this.getAttribute("host");
	}
	public void setHost(String host) {
		this.put("host", host);
	}
	public String getPort() {
		return (String) this.getAttribute("port");
	}
	public void setPort(String port) {
		this.put("port", port);
	}
	public String getSchema() {
		return (String) this.getAttribute("schema");
	}
	public void setSchema(String schema) {
		this.put("schema", schema);
	}
	public String getPassw() {
		return (String) this.getAttribute("passw");
	}
	public void setPassw(String passw) {
		this.put("passw", passw);
	}

	public String getUser() {
		return (String) this.getAttribute("user");
	}

	public void setUser(String user) {
		this.put("user", user);
	}

	public String getCatalog() {
		return (String) this.getAttribute("catalog");
	}

	public void setCatalog(String catalog) {
		this.put("catalog", catalog);
	}

	public String getBaseFilter() {
		return (String) this.getAttribute("baseFilter");
	}

	public void setBaseFilter(String baseFilter) {
		this.put("baseFilter", baseFilter);
	}

	public String getBaseOrder() {
		return (String) this.getAttribute("baseOrder");
	}
	public void setBaseOrder(String baseOrder) {
		this.put("baseOrder", baseOrder);
	}
	public String[] getFields() {
		return (String[]) this.getAttribute("fields");
	}
	public void setFields(String[] fields) {
		this.put("fields", fields);
	}
	private String arrayToString(Object[] array) {
		StringBuffer buff = new StringBuffer();
		if (array.length < 1) {
			return "";
		}
		for (int i = 0; i < array.length; i++) {
			buff.append(array[i] + ",");
		}

		String r = buff.toString();
		return r.substring(0, r.length() - 1);
	}

	public String getFieldsString(){
		String[] fields = this.getFields();
		if (fields == null || fields.length == 0) {
			return "*";
		} else if (fields.length == 1) {
			return fields[0];
		} else {
			return arrayToString(fields);
		}
	}

	public String getTableName() {
		return (String) this.getAttribute("tableName");
	}
	public void setTableName(String tableName) {
		this.put("tableName", tableName);
	}
	public String tableID(){
		String schema = this.getSchema();
		if (schema == null || schema == "") {
			return this.getTableName();
		}
		return schema + "." + this.getTableName();
	}
	public String[] getFieldsId() {
		return (String[]) this.getAttribute("fieldsId");
	}
	public void setFieldsId(String[] fieldsId) {
		this.put("fieldsId", fieldsId);
	}

	public String getFieldsIdString() {
		String[] fieldsId = this.getFieldsId();
		if (fieldsId == null || fieldsId.length == 0) {
			return "";
		} else if (fieldsId.length == 1) {
			return fieldsId[0];
		} else {
			return arrayToString(fieldsId);
		}
	}

	public boolean isValid() {
		if (this.getSqlSoure() != null) {
			return this.getTableName() == null && this.getFieldsId() != null
					&& this.getFieldsId().length > 0;
		}
		return this.getTableName() != null && this.getFields() != null
				&& this.getFields().length > 0 && this.getFieldsId() != null
				&& this.getFieldsId().length > 0 && this.getDb() != null;
	}
	public String getSqlSoure() {
		return (String) this.getAttribute("sqlSoure");
	}
	public void setSqlSoure(String sqlSoure) {
		this.put("sqlSoure", sqlSoure);
	}
	public String getDefaultGeometryField() {
		return (String) this.getAttribute("defaultGeometryField");
	}
	public void setDefaultGeometryField(String geometryColumn) {
		this.put("defaultGeometryField", geometryColumn);
	}
	public String getSRISD() {
		return (String) this.getAttribute("SRISD");
	}
	public void setSRISD(String srisd) {
		this.put("SRISD", srisd);
	}
	public Envelope getWorkingArea() {
		return (Envelope) this.getAttribute("workingArea");
	}
	public void setWorkingArea(Envelope workingArea) {
		this.put("workingArea", workingArea);
	}


	public XMLEntity getXMLEntity() {
		XMLEntity xmlEntity = super.getXMLEntity();

		xmlEntity.putProperty("user", this.getUser());
		//xmlEntity.putProperty("passw", this.passw);
		xmlEntity.putProperty("schema", this.getSchema());
		xmlEntity.putProperty("catalog", this.getCatalog());
		xmlEntity.putProperty("host", this.getHost());
		xmlEntity.putProperty("port", this.getPort());
		xmlEntity.putProperty("db", this.getDb());

		xmlEntity.putProperty("tableName", this.getTableName());

		xmlEntity.putProperty("fieldsString", this.getFieldsString());
		xmlEntity.putProperty("fieldsIdString", this.getFieldsIdString());

		xmlEntity.putProperty("baseFilter", this.getBaseFilter());
		xmlEntity.putProperty("baseOrder", this.getBaseOrder());
		xmlEntity.putProperty("sqlSoure", this.getSqlSoure());
		// TODO xmlEntity.putProperty("workingArea", this.workingArea.toString());

		xmlEntity.putProperty("SRISD", this.getSRISD());

		xmlEntity
				.putProperty("defaultGeometryField", this
				.getDefaultGeometryField());

		return xmlEntity;
	}

	public void loadFromXMLEntity(XMLEntity xmlEntity)
			throws InitializeException {

		try {
			this.setUser(xmlEntity.getStringProperty("user"));
		} catch (NotExistInXMLEntity e) {
			this.remove("user");
		}

		try {
			this.setPassw(xmlEntity.getStringProperty("passw"));
		} catch (NotExistInXMLEntity e) {
			this.remove("passw");
		}

		try {
			this.setSchema(xmlEntity.getStringProperty("schema"));
		} catch (NotExistInXMLEntity e) {
			this.remove("schema");
		}

		try {
			this.setCatalog(xmlEntity.getStringProperty("catalog"));
		} catch (NotExistInXMLEntity e) {
			this.remove("catalog");
		}

		try {
			this.setHost(xmlEntity.getStringProperty("host"));
		} catch (NotExistInXMLEntity e) {
			throw new InitializeException("host property not set", this
					.getDataStoreName());
		}
		try {
			this.setPort(xmlEntity.getStringProperty("port"));
		} catch (NotExistInXMLEntity e) {
			throw new InitializeException("port property not set", this
					.getDataStoreName());
		}
		try {
			this.setDb(xmlEntity.getStringProperty("db"));
		} catch (NotExistInXMLEntity e) {
			throw new InitializeException("db property not set", this
					.getDataStoreName());
		}

		try {
			this.setTableName(xmlEntity.getStringProperty("tableName"));
		} catch (NotExistInXMLEntity e) {
			this.remove("tableName");
		}

		try {
			this.setBaseFilter(xmlEntity.getStringProperty("baseFilter"));
		} catch (NotExistInXMLEntity e) {
			this.remove("baseFilter");
		}

		try {
			this.setBaseOrder(xmlEntity.getStringProperty("baseOrder"));
		} catch (NotExistInXMLEntity e) {
			this.remove("baseOrder");
		}

		try {
			this.setSqlSoure(xmlEntity.getStringProperty("sqlSoure"));
		} catch (NotExistInXMLEntity e) {
			this.remove("sqlSoure");
		}

		try {
			this.setSRISD(xmlEntity.getStringProperty("SRISD"));
		} catch (NotExistInXMLEntity e) {
			this.remove("SRISD");
		}

		try {
			this.setDefaultGeometryField(xmlEntity
					.getStringProperty("defaultGeometryField"));
		} catch (NotExistInXMLEntity e) {
			this.remove("defaultGeometryField");
		}

		String tmp;
		try {
			tmp = xmlEntity.getStringProperty("fieldsString");
			if (tmp.equals("*")) {
				this.setFields(null);
			} else {
				this.setFields(tmp.split(","));
			}
		} catch (NotExistInXMLEntity e) {
			this.remove("fields");
		}

		try {
			tmp = xmlEntity.getStringProperty("fieldsIdString");
			this.setFieldsId(tmp.split(","));
		} catch (NotExistInXMLEntity e) {
			this.remove("fieldsId");
		}

	}

}
