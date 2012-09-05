package org.gvsig.fmap.data.feature.db;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.exception.InitializeException;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;


public abstract class DBExplorerParameters extends
		DataServerExplorerParameters implements DBParameters {


	protected Map createDefaultValuesMap() {
		Map defaultValues = new HashMap(7);
		defaultValues.put("user", null);
		defaultValues.put("passw", null);
		defaultValues.put("schema", null);
		defaultValues.put("catalog", null);
		defaultValues.put("host", null);
		defaultValues.put("port", null);
		defaultValues.put("db", null);
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

	public XMLEntity getXMLEntity() {
		XMLEntity xmlEntity = super.getXMLEntity();

		xmlEntity.putProperty("user", this.getUser());
		//xmlEntity.putProperty("passw", this.passw);
		xmlEntity.putProperty("schema", this.getSchema());
		xmlEntity.putProperty("catalog", this.getCatalog());
		xmlEntity.putProperty("host", this.getHost());
		xmlEntity.putProperty("port", this.getPort());
		xmlEntity.putProperty("db", this.getDb());

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
					.getDataExplorerName());
		}
		try {
			this.setPort(xmlEntity.getStringProperty("port"));
		} catch (NotExistInXMLEntity e) {
			throw new InitializeException("port property not set", this
					.getDataExplorerName());
		}
		try {
			this.setDb(xmlEntity.getStringProperty("db"));
		} catch (NotExistInXMLEntity e) {
			throw new InitializeException("db property not set", this
					.getDataExplorerName());
		}
	}

}
