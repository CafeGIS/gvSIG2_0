
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
package org.gvsig.catalog;
import org.gvsig.catalog.drivers.AbstractCatalogServiceDriver;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.drivers.ICatalogServiceDriver;
import org.gvsig.catalog.querys.CatalogQuery;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * This class represents a catalogClient. It must be created to
 * use the catalog service
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class CatalogClient extends DiscoveryServiceClient{
	
	/**
	 * Constructor
	 * @param sUri
	 * The URI typed by the user
	 * @param database
	 * The selected database
	 * @param driver
	 * The selected catalog driver
	 */
	public CatalogClient(String sUri, String database, ICatalogServiceDriver driver) {        
		super(sUri,driver);			
	} 

	/**
	 * @return Node array with the retrieved records
	 * @param query It contains the values to do the query (title="XXX",abstract="YYY",...)
	 * @param firstRecord Number of the first record to retrieve
	 */
	public GetRecordsReply getRecords(CatalogQuery query, int firstRecord) {        
		return ((ICatalogServiceDriver)getDriver()).getRecords(getUri(),query,firstRecord);
	} 

	/**
	 * This method is used to create a new Query
	 * @return 
	 */
	public CatalogQuery createNewQuery() {        
		return new CatalogQuery();   
	}	
	
	/**
	 * @return the serverData
	 */
	public ServerData getServerData() {
		return ((AbstractCatalogServiceDriver)getDriver()).getServerData();
	}

	/**
	 * @param serverData the serverData to set
	 */
	public void setServerData(ServerData serverData) {
		((AbstractCatalogServiceDriver)getDriver()).setServerData(serverData);
	}
}
