
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
 * This class parses the Indicio CSW server answer files.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class IdecISO19115Record extends Record {

	public  IdecISO19115Record() {   

	}

	/**
	 * @param node 
	 */
	public  IdecISO19115Record(URI uri,XMLNode node) {        
		super(uri,node);
		setTitle(XMLTree.searchNodeValue(node,
				"identificationInfo->MD_DataIdentification->citation->title"));
		setAbstract_(XMLTree.searchNodeValue(node,
		"identificationInfo->MD_DataIdentification->abstract"));
		setPurpose(XMLTree.searchNodeValue(node,
		"identificationInfo->MD_DataIdentification->purpose"));
		setKeyWords(XMLTree.searchMultipleNodeValue(node,
		"identificationInfo->MD_DataIdentification->descriptiveKeywords->keyword"));
		setResources(getResources("distributionInfo->distributor->distributorTransferOptions->onLine"));
		//setImage(getImageUrl("identificationInfo->MD_DataIdentification->graphicOverview->fileName"));
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

		for (int i = 0; i < resources.length; i++){

			String sProtocol = XMLTree.searchNodeValue(nodes[i], "protocol");
			String sName = XMLTree.searchNodeValue(nodes[i], "orName");

			if (sProtocol == null){
				sProtocol =   Resource.WEBSITE;
				sName =  "orName";
			}

			resources[i] = new Resource(XMLTree.searchNodeValue(nodes[i],
			"linkage"),
			sProtocol,
			sName,
			"orDesc",
			"",
			"",	
			null);
		}
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.schemas.discoverer.Record#accept(java.net.URI, es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public boolean accept(URI uri, XMLNode node) {
		if (node.getName().endsWith("MD_Metadata")){
			if (node.searchNode("identificationInfo->MD_DataIdentification->citation->title")!=null){
				return true;
			}
		}
		return false;
	} 
}
