package org.gvsig.catalog.schemas;

import java.net.URI;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.querys.Coordinates;


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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class LaitsGmuEbRIMRecord extends Record{
	private static final String ROOT = "laitscsw:DataGranule";

	public  LaitsGmuEbRIMRecord() {        

	}

	public  LaitsGmuEbRIMRecord(URI uri, XMLNode node) {        
		super(uri,node);
		setTitle(node.searchNodeValue("name"));
		setAbstract_(node.searchNodeValue("description"));
		setPurpose(null);
		setKeyWords(null);
		setResources(getResources("OnlineAccessURLs->OnlineAccessURL"));
		setFileID(node.getAttribute("id"));	       
		setImageURL(node.searchNodeValue("previewURL"));
	} 	
	
	private Resource[] getResources(String label) {        
		XMLNode[] nodes = XMLTree.searchMultipleNode(getNode(), label);
		
		if (nodes == null) {
			return null;
		}
		
		Coordinates coordinates = null;
		String srs = XMLTree.searchNodeValue(getNode(),"ogc:BBOX->referenceSystemNameCode");
		coordinates = new Coordinates(XMLTree.searchNodeValue(getNode(),"ogc:BBOX->westBoundingCoordinate"),
				XMLTree.searchNodeValue(getNode(),"ogc:BBOX->northBoundingCoordinate"),
				XMLTree.searchNodeValue(getNode(),"ogc:BBOX->eastBoundingCoordinate"),
				XMLTree.searchNodeValue(getNode(),"ogc:BBOX->southBoundingCoordinate"));
			
		Resource[] resources = new Resource[nodes.length];
		
		for (int i = 0; i < resources.length; i++){
			resources[i] = new Resource(nodes[i].searchNodeValue("URL"),
					Resource.DOWNLOAD,
					null,
					null,
					null,
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
		System.out.println(node.getName());
		if (node.getName().equals(ROOT)){
			return true;
		}
		return false;
	} 
}
