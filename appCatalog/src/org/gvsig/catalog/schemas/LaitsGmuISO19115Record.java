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
 * $Id: LaitsGmuRecord.java 561 2007-07-27 06:49:30Z jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/23 07:14:24  jorpiell
 * Catalog refactoring
 *
 * Revision 1.1.2.2.4.2  2007/07/11 13:01:51  jorpiell
 * Catalog UI updated
 *
 * Revision 1.1.2.2.4.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 * Revision 1.1.2.2  2006/11/15 00:08:08  jjdelcerro
 * *** empty log message ***
 *
 * Revision 1.2  2006/11/13 10:01:01  jorpiell
 * Nuevos parsers para el servidor de GMU
 *
 * Revision 1.1.2.1  2006/11/08 12:57:12  jorpiell
 * Se han hecho numerosos cambios para que pueda funcionar el nomenclator de Simon y el catálogo de GMU
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class LaitsGmuISO19115Record extends Record {	

	public  LaitsGmuISO19115Record() {        

	}

	/**
	 * Constructor
	 * 
	 * 
	 * @param node Node with the answer
	 * @param serverURL Server URL. Necessary to load the image (just Geonetwork)
	 */
	public  LaitsGmuISO19115Record(URI uri, XMLNode node) {        
		super(uri,node);
		setTitle(XMLTree.searchNodeValue(node,"granuleShortName"));
		setAbstract_(XMLTree.searchNodeValue(node, "description"));
		setPurpose(null);
		setKeyWords(null);
		setResources(getResources("OnlineAccessURLs->OnlineAccessURL"));
		setFileID(null);	       
		//Caution: getImageUrl uses serverURL and FileID!!!
		setImage(null);

	} 

	/**
	 * 
	 * 
	 * 
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
			srs = XMLTree.searchNodeValue(getNode(),"ogc:BBOX->referenceSystemNameCode");
			coordinates = new Coordinates(XMLTree.searchNodeValue(getNode(),"ogc:BBOX->westBoundingCoordinate"),
					XMLTree.searchNodeValue(getNode(),"ogc:BBOX->northBoundingCoordinate"),
					XMLTree.searchNodeValue(getNode(),"ogc:BBOX->eastBoundingCoordinate"),
					XMLTree.searchNodeValue(getNode(),"ogc:BBOX->southBoundingCoordinate"));
		}


		for (int i = 0; i < resources.length; i++){
			resources[i] = new Resource(XMLTree.searchNodeValue(nodes[i],
					"URL"),
					Resource.DOWNLOAD,
					XMLTree.searchNodeValue(nodes[i], "orName"),
					XMLTree.searchNodeValue(nodes[i], "URLDescription"),
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
		return false;
	}     


}
