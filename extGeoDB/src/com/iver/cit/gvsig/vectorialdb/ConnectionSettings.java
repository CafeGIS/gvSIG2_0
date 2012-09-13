package com.iver.cit.gvsig.vectorialdb;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.resource.db.DBParameters;


/**
 * @author Fernando González Cortés
 */
public class ConnectionSettings {
    private String host;
    private String port;
    private String db;
    private String schema;
    private String driver;
    private String user;
    private String name;
    private String passw;

    public String getDb() {
        return db;
    }
    public void setDb(String db) {
        this.db = db;
    }
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String toString(){
        return host+","+port+","+db+","+driver+","+user+","+name;
    }

    public void setFromString(String str) {
        String[] values = str.split(",");
        host = values[0];
        port = values[1];
        db = values[2];
        driver = values[3];
        user = values[4];
        name = values[5];
        if (values.length == 7) {
			passw = values[6];
		}
    }
    public String getPassw() {
        return passw;
    }
    public void setPassw(String passw) {
        this.passw = passw;
    }
    public String getConnectionString(){
    	DataManager dm=DALLocator.getDataManager();
    	try {
			DBParameters dsp=(DBParameters)dm.createStoreParameters(getDriver());
		} catch (InitializeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProviderNotRegisteredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String connectionString = "";//vecDriver.getConnectionStringBeginning() + "//" + getHost();

        connectionString += (":" + getPort());

        connectionString += ("/" + getDb());

        return connectionString;
    }
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
}
