
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
import org.gvsig.catalog.querys.Coordinates;


/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class GeonetworkISO19115Record extends Record {
	
	public GeonetworkISO19115Record() {  
		
	}
	
	/**
	 * Constructor
	 * @param node Node with the answer
	 * @param serverURL Server URL. Necessary to load the image (just Geonetwork)
	 */
	public GeonetworkISO19115Record(URI uri,XMLNode node) {        
		super(uri,node);
		setTitle(XMLTree.searchNodeValue(node,
				"dataIdInfo->idCitation->resTitle"));
		setAbstract_(XMLTree.searchNodeValue(node, "dataIdInfo->idAbs"));
		setPurpose(XMLTree.searchNodeValue(node, "dataIdInfo->idPurp"));
		setKeyWords(XMLTree.searchMultipleNodeValue(node,
		"dataIdInfo->descKeys->keyword"));
		setResources(getResources("distInfo->distTranOps->onLineSrc"));
		setFileID(XMLTree.searchNodeValue(node,"mdFileID"));
		//Caution: getImageUrl uses serverURL and FileID!!!
		XMLNode[] nodes = XMLTree.searchMultipleNode(node,"dataIdInfo->graphOver");
		if ((nodes != null) && (nodes.length > 0)){
			String imageURL = XMLTree.searchNodeValue(nodes[0], "bgFileName");
			if (imageURL != null){
				setImageURL(imageURL);
			}
		}        
	} 

	/**
	 * @return 
	 * @param label 
	 */
	private Resource[] getResources(String label) {        
		XMLNode[] nodes = XMLTree.searchMultipleNode(getNode(), label);
		Coordinates coordinates = null;
		String srs = null;

		if (nodes == null) {
			return null;
		}
		Resource[] resources = new Resource[nodes.length];
		if (nodes.length > 0){
			srs = XMLTree.searchNodeValue(getNode(),"refSysInfo->MdCoRefSys->refSysID->identCode");
			coordinates = new Coordinates(XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->westBL"),
					XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->northBL"),
					XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->eastBL"),
					XMLTree.searchNodeValue(getNode(),"dataIdInfo->geoBox->southBL"));
		}

		for (int i = 0; i < resources.length; i++){
			resources[i] = new Resource(XMLTree.searchNodeValue(nodes[i],
					"linkage"),
					XMLTree.searchNodeValue(nodes[i], "protocol"),
					XMLTree.searchNodeValue(nodes[i], "orName"),
					XMLTree.searchNodeValue(nodes[i], "orDesc"),
					XMLTree.searchNodeAtribute(nodes[i], "orFunct->OnFunctCd",
					"value"),
					srs,	
					coordinates);
			if (resources[i].getLinkage() == null){
				resources[i].setLinkage("");
			}
		}
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.schemas.discoverer.Record#accept(java.net.URI, es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public boolean accept(URI uri, XMLNode node) {
		if (node.getName().equals("Metadata")){
			if (XMLTree.searchNode(node, "mdFileID") != null){
				return true;
			}
		}
		return false;
	}     


}
