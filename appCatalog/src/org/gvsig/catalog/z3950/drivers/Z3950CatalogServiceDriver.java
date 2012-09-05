
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
package org.gvsig.catalog.z3950.drivers;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.gvsig.catalog.drivers.AbstractCatalogServiceDriver;
import org.gvsig.catalog.drivers.CatalogCapabilities;
import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.protocols.Z3950Protocol;
import org.gvsig.catalog.querys.CatalogQuery;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * This class implements the Z39.50 protocol.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://www.loc.gov/z3950/agency/
 */
public class Z3950CatalogServiceDriver extends AbstractCatalogServiceDriver {

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {        
		Collection nodes = new ArrayList();;
		Z3950Messages messages = new Z3950Messages(this);
		String message = messages.getCapabilities(new Z3950Protocol().openConnection(
				uri));
		ByteArrayInputStream buffer = new ByteArrayInputStream(message.getBytes());
		nodes.add(XMLTree.xmlToTree(buffer));

		setServerAnswerReady(XMLTree.searchNodeValue((XMLNode)nodes.toArray()[0], "Servidor"));
		CatalogCapabilities capabilities = new CatalogCapabilities();
		capabilities.setVersion("3.0");
		return capabilities;
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getRecords(java.net.URI, es.gva.cit.catalogClient.querys.Query, int)
	 */
	public GetRecordsReply getRecords(URI uri, CatalogQuery query, int firstRecord) {        
		setQuery(query);
		Z3950Messages messages = new Z3950Messages(this);
		String message = messages.getRecords(getQuery(),Z3950Protocol.getDatabase(uri));
		System.out.println(message);
		new Z3950Protocol().doQuery(getRecordsReply(), uri, message, firstRecord);		
		return getRecordsReply();
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#isProtocolSupported(java.net.URI)
	 */
	public boolean isProtocolSupported(URI uri) {        
		return new Z3950Protocol().isProtocolSupported(uri);
	} 

	/**
	 * @return 
	 */
	public String getQueryProfile() {        
		return "Z3950";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return ServerData.SERVER_SUBTYPE_CATALOG_Z3950;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getDefaultPort()
	 */
	public int getDefaultPort() {
		return 2100;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getDefaultSchema()
	 */
	public String getDefaultSchema() {
		return "z3950";
	} 
}
