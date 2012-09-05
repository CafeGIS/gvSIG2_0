
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
package org.gvsig.catalog.schemas;
import java.net.URI;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;


/**
 * This class is used to parse the metadata retreived using
 * the CSW deegree server.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class DeegreeISO19115Record extends Record {

	public  DeegreeISO19115Record() {        

	}
	
	/**
	 * @param node 
	 */
	public  DeegreeISO19115Record(URI uri,XMLNode node) {        
		super(uri,node);

		setTitle(XMLTree.searchNodeValue(node,
				"iso19115:identificationInfo->smXML:MD_DataIdentification->smXML:citation->smXML:CI_Citation->smXML:title->smXML:CharacterString"));
		setAbstract_(XMLTree.searchNodeValue(node,
		"iso19115:identificationInfo->smXML:MD_DataIdentification->smXML:abstract->smXML:CharacterString"));
		setPurpose(XMLTree.searchNodeValue(node,
		"iso19115:identificationInfo->smXML:MD_DataIdentification->smXML:purpose->smXML:CharacterString"));
		setKeyWords(XMLTree.searchMultipleNodeValue(node,
		"iso19115:identificationInfo->smXML:MD_DataIdentification->smXML:descriptiveKeywords->smXML:MD_Keywords->smXML:keyword->smXML:CharacterString"));
		setResources(getResources("iso19115:distributionInfo->smXML:MD_Distribution->smXML:distributor->smXML:MD_Distributor->smXML:distributorContact->smXML:CI_ResponsibleParty->smXML:contactInfo->smXML:CI_Contact->smXML:onlineResource->smXML:CI_OnlineResource"));
		setImageURL("iso19115:identificationInfo->smXML:MD_DataIdentification->smXML:graphicOverview->smXML:MD_BrowseGraphic->smXML:fileName->smXML:CharacterString");
	} 

	/**
	 * It parses the online resources
	 * 
	 * 
	 * @return Resource
	 * @param label Label that contains the resource root
	 */
	private Resource[] getResources(String label) {        
		XMLNode[] nodes = XMLTree.searchMultipleNode(getNode(), label);

		if (nodes == null) {
			return null;
		}
		Resource[] resources = new Resource[nodes.length];

		for (int i = 0; i < resources.length; i++)
			resources[i] = new Resource(XMLTree.searchNodeValue(nodes[i],"smXML:linkage->smXML:URL"),
					XMLTree.searchNodeValue(nodes[i],"smXML:protocol->smXML:CharacterString"),
					XMLTree.searchNodeValue(nodes[i],"smXML:name->smXML:CharacterString"),
					XMLTree.searchNodeValue(nodes[i],"smXML:description->smXML:CharacterString"),
					"",
					"",	
					null);
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.schemas.discoverer.Record#accept(java.net.URI, es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public boolean accept(URI uri, XMLNode node) {
		if (node.getName().equals("iso19115:MD_Metadata")){
			return true;
		}
		return false;
	} 
}
