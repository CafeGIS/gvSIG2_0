
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
 * This class parses a XMLNode that contains a DublinCore tree
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class DublinCoreRecord extends Record {

	public  DublinCoreRecord() {   

	}

	/**
	 * @param node 
	 */
	public  DublinCoreRecord(URI uri,XMLNode node) {        
		super(uri,node);    	
		setTitle(XMLTree.searchNodeValue(node,"dc:title"));
		setAbstract_(XMLTree.searchNodeValue(node, "dc:subject"));
		setPurpose(XMLTree.searchNodeValue(node, "dc:description"));
		setResources(getResources(node));          
	} 

	/**
	 * It parses the resource tags
	 * 
	 * 
	 * @return 
	 * @param node 
	 */
	public Resource[] getResources(XMLNode node) {        
		String format = XMLTree.searchNodeValue(node, "dc:format");
		String source = XMLTree.searchNodeValue(node, "dc:source");
		Coordinates coordinates = new Coordinates(XMLTree.searchNodeValue(getNode(),"dc:spatial->dcmiBox:Box->dcmiBox:westlimit"),
				XMLTree.searchNodeValue(getNode(),"dc:spatial->dcmiBox:Box->dcmiBox:northlimit"),
				XMLTree.searchNodeValue(getNode(),"dc:spatial->dcmiBox:Box->dcmiBox:eastlimit"),
				XMLTree.searchNodeValue(getNode(),"dc:spatial->dcmiBox:Box->dcmiBox:southlimit"));

		if ((source == null) || (format == null)){
			return null;
		}

		if (format.toLowerCase().equals("shapefile"))
			format = Resource.DOWNLOAD;

		if (format.toLowerCase().trim().equals("web page"))
			format = Resource.WEBSITE;

		Resource[] resources = new Resource[1];
		resources[0] = new Resource(source,format,"","","","",coordinates);

		return resources;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.schemas.discoverer.Record#accept(java.net.URI, es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public boolean accept(URI uri, XMLNode node) {
		if ((node.getName().equals("dc:metadata")) ||
				(node.getName().equals("simpledc")) ||
				(node.getName().equals("csw:SummaryRecord")) ||
				(node.getName().equals("csw:Record"))){
			return true;
		}
		return false;
	} 
}
