
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
package org.gvsig.catalog.drivers;
import org.gvsig.catalog.drivers.profiles.IProfile;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.querys.DiscoveryServiceQuery;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * All classes that implement a new catalog protocol must to inherit to this class.ç
 * It contains the common atributes for the protocols.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public abstract class AbstractCatalogServiceDriver extends AbstractDiscoveryServiceDriver implements ICatalogServiceDriver {
	private String sortBy = null;
	private String startPosition;
	private String maxRecords;
	private String outputFormat = null;
	private String[] outputSchema = null;
	private CatalogQuery query = null;
	private GetRecordsReply recordsReply = null;
	private String serverProfile = null;
	private ServerData serverData = null;

	/**
	 * Used to set a ServerData that contains the properties
	 * that have been fixed by the user
	 */
	public void setServerData(ServerData serverData){
		this.serverData = serverData;
	}
	
	/**
	 * Return the server data
	 */
	public ServerData getServerData(){
		return serverData;
	}
	
	/**
	 * Gets the profile. It has to be implemented
	 * by all the drivers with profile
	 * @return
	 * The profile
	 */
	public IProfile getProfile(){
		return null;
	}
	
	/**
	 * This function returns the number of records that have been retrieved.
	 * It Reads a Node value.
	 * 
	 * 
	 * @return The number of records
	 * @param node The answer tree
	 * @param label Node Name that contains the value
	 * @param attribute Attributes wich contain the value
	 */
	public int getNumberOfRecords(XMLNode node, String label, String attribute) {        
		String sNumberOfRecords = null;
		int numberOfRecords;

		if (attribute != null)
			sNumberOfRecords = XMLTree.searchNodeAtribute(node,
					label,
					attribute);
		else
			sNumberOfRecords = XMLTree.searchNodeValue(node,label);

		try {
			numberOfRecords = Integer.parseInt(sNumberOfRecords);
		} catch (Exception e) {
			return -1;
		}

		return numberOfRecords;
	} 
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#createQuery()
	 */
	public DiscoveryServiceQuery createQuery(){
		return new CatalogQuery();
	}
	
	/**
	 * @return Returns the query.
	 */
	public CatalogQuery getQuery() {        
		return query;
	} 

	/**
	 * @param query The query to set.
	 */
	public void setQuery(CatalogQuery query) {        
		if (query != null) {
			this.query = query;
			recordsReply = new GetRecordsReply(0);
		}
	} 

	/**
	 * @return Returns the maxRecords.
	 */
	public String getMaxRecords() {        
		return maxRecords;
	} 

	/**
	 * @param maxRecords The maxRecords to set.
	 */
	public void setMaxRecords(String maxRecords) {        
		this.maxRecords = maxRecords;
	} 

	/**
	 * @return Returns the outputFormat.
	 */
	public String getOutputFormat() {        
		return outputFormat;
	} 

	/**
	 * @param outputFormat The outputFormat to set.
	 */
	public void setOutputFormat(String outputFormat) {        
		this.outputFormat = outputFormat;
	} 

	/**
	 * @return Returns the outputSchema.
	 */
	public String[] getOutputSchema() {        
		return outputSchema;
	} 

	/**
	 * @param outputSchema The outputSchema to set.
	 */
	public void setOutputSchema(String[] outputSchema) {        
		this.outputSchema = outputSchema;
	} 

	/**
	 * @param outputSchema 
	 */
	public void setOutputSchema(String outputSchema) {        
		String[] s = new String[1];
		s[0] = outputSchema;
		this.outputSchema = s;
	} 

	/**
	 * @return Returns the sortBy.
	 */
	public String getSortBy() {        
		return sortBy;
	} 

	/**
	 * @param sortBy The sortBy to set.
	 */
	public void setSortBy(String sortBy) {        
		this.sortBy = sortBy;
	} 

	/**
	 * @return Returns the startPosition.
	 */
	public String getStartPosition() {        
		return startPosition;
	} 

	/**
	 * @param startPosition The startPosition to set.
	 */
	public void setStartPosition(String startPosition) {        
		this.startPosition = startPosition;
	} 

	/**
	 * @return the recordsReply
	 */
	public GetRecordsReply getRecordsReply() {
		return recordsReply;
	}

}
