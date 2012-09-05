
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.utiles.swing.jcomboServer;
import java.util.Date;
import java.util.Properties;

import com.iver.utiles.DateTime;

/**
 * This class represents a data server, that can be a WMS, WFS, Catalog or
 * any kind of server. It contains the server URL and has a couple of 
 * attributes that describe the server type (serverType and serverSubType).
 * It contains the date when the server was created and the date when the server
 * was accessed last time. * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ServerData {

	/**
	 * 
	 * 
	 */
	public static final String SERVER_TYPE_CATALOG = "CATALOG";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_TYPE_GAZETTEER = "GAZETTEER";
	
	/**
	 * 
	 * 
	 */
	public static final String SERVER_TYPE_MULTIPLE = "MULTIPLE";
	/**
	 * 
	 * 
	 */
	public static final String SERVER_TYPE_WMS = "WMS";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_TYPE_WCS = "WCS";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_TYPE_WFS = "WFS";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_CATALOG_Z3950 = "Z3950";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_CATALOG_SRW = "SRW";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_CATALOG_CSW = "CSW";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_GAZETTEER_WFSG = "WFS-G";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_GAZETTEER_ADL = "ADL";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_GAZETTEER_IDEC = "IDEC";

	/**
	 * 
	 * 
	 */
	public static final String SERVER_SUBTYPE_GAZETTEER_WFS = "WFS";

	/**
	 * 
	 * 
	 */
	private Date added = null;

	/**
	 * 
	 * 
	 */
	private Date lastAccess = null;

	/**
	 * 
	 * 
	 */
	private String serviceType = null;

	/**
	 * 
	 * 
	 */
	private String serviceSubType = null;

	/**
	 * 
	 * 
	 */
	private String serverAddress = null;

	/**
	 * 
	 * 
	 */
	private String database = null;
	
	private Properties properies = new Properties();

	/**
	 * 
	 * 
	 * 
	 * @param serverAddress Server address
	 * @param added When the server was added
	 * @param lastAccess When the server was used last time
	 * @param serviceType Service type
	 * @param serviceSubType Service subtype
	 */
	public  ServerData(String serverAddress, Date added, Date lastAccess, String serviceType, String serviceSubType) {        
		this.added = added;
		this.lastAccess = lastAccess;
		this.serviceType = serviceType;
		this.serviceSubType = serviceSubType;
		this.serverAddress = serverAddress;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param serverAddress Server address
	 * @param added When the server was added
	 * @param lastAccess When the server was used last time
	 * @param serviceType Service type
	 * @param serviceSubType Service subtype
	 * @param database Database name
	 */
	public  ServerData(String serverAddress, Date added, Date lastAccess, String serviceType, String serviceSubType,String database) {        
		this.added = added;
		this.lastAccess = lastAccess;
		this.serviceType = serviceType;
		this.serviceSubType = serviceSubType;
		this.serverAddress = serverAddress;
		this.database = database;
	} 

	/**
	 * Constructor for a new Server
	 * 
	 * 
	 * @param serverAddress Server Address
	 * @param serviceType Server Type
	 * @param serviceSubType Server Subtype
	 */
	public  ServerData(String serverAddress, String serviceType, String serviceSubType) {        

		this.serverAddress = serverAddress;
		this.added = DateTime.getCurrentDate();
		this.lastAccess = DateTime.getCurrentDate();
		this.serviceType = serviceType;
		this.serviceSubType = serviceSubType;
	} 

	/**
	 * Constructor for a new Server
	 * 
	 * 
	 * @param serverAddress Server Address
	 * @param serviceType Server Type
	 * @param serviceSubType Server Subtype
	 */
	public  ServerData(String serverAddress, String serviceType, String serviceSubType,String database) {        

		this.serverAddress = serverAddress;
		this.added = DateTime.getCurrentDate();
		this.lastAccess = DateTime.getCurrentDate();
		this.serviceType = serviceType;
		this.serviceSubType = serviceSubType;
		this.database = database;
	} 
	
	/**
	 * Constructor for a new Server
	 * 
	 * 
	 * @param serverAddress Server Address
	 * @param serviceType Server Type
	 */
	public  ServerData(String serverAddress, String serviceType) {        

		this.serverAddress = serverAddress;
		this.added = DateTime.getCurrentDate();
		this.lastAccess = DateTime.getCurrentDate();
		this.serviceType = serviceType;
		this.serviceSubType = "";
		this.database = "";
	} 

	/**
	 * This method updates the last access attribute. New value
	 * is the current time.
	 * 
	 */
	public void updateLastAccess() {        
		lastAccess = DateTime.getCurrentDate();
	} 

	/**
	 * The server address field have to be showed in the combo
	 * 
	 * 
	 * @return String
	 */
	public String toString() {        
		return getServerAddress();
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the added.
	 */
	public Date getAdded() {        
		return added;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param added The added to set.
	 */
	public void setAdded(Date added) {        
		this.added = added;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the lastAccess.
	 */
	public Date getLastAccess() {        
		return lastAccess;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param lastAccess The lastAccess to set.
	 */
	public void setLastAccess(Date lastAccess) {        
		this.lastAccess = lastAccess;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the serverAddress.
	 */
	public String getServerAddress() {        
		return serverAddress;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param serverAddress The serverAddress to set.
	 */
	public void setServerAddress(String serverAddress) {        
		this.serverAddress = serverAddress;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the serviceSubType.
	 */
	public String getServiceSubType() {        
		return serviceSubType;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param serviceSubType The serviceSubType to set.
	 */
	public void setServiceSubType(String serviceSubType) {        
		this.serviceSubType = serviceSubType;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the serviceType.
	 */
	public String getServiceType() {        
		return serviceType;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param serviceType The serviceType to set.
	 */
	public void setServiceType(String serviceType) {        
		this.serviceType = serviceType;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return Returns the properies.
	 */
	public Properties getProperies() {
		return properies;
	}

	/**
	 * @param properies The properies to set.
	 */
	public void setProperies(Properties properies) {
		this.properies = properies;
	} 
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public String getProperty(String propertyName){
		return (String)getProperies().getProperty(propertyName);
	}
}
