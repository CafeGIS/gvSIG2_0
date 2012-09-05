package org.gvsig.catalog.schemas;

import java.net.URI;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;


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
 * $Id: LaitsGmuServicesRecord.java 600 2007-09-19 11:30:05 +0000 (Wed, 19 Sep 2007) jpiera $
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
public class LaitsGmuServicesRecord extends Record {

	public  LaitsGmuServicesRecord() {        

	}

	/**
	 * Constructor
	 * 
	 * 
	 * @param node Node with the answer
	 * @param serverURI Server URL. Necessary to load the image (just Geonetwork)
	 */
	public  LaitsGmuServicesRecord(URI uri,XMLNode node) {        
		super(uri,node);
		setTitle(XMLTree.searchNodeValue(node,"name"));
		setAbstract_(XMLTree.searchNodeValue(node, "description"));	          
		setPurpose(null);
		setKeyWords(null);
		setResources(getResources(node));
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
	private Resource[] getResources(XMLNode node) {        
		XMLNode[] slots = XMLTree.searchMultipleNode(node, "rim:Slot");
		if (slots != null){
			Resource[] resources = new Resource[1];
			String resource = null;
			String serviceType = null;
			for (int i=0 ; i<slots.length ; i++){
				XMLNode slot = slots[i];
				String attType = slot.getAttribute("name");
				if (attType.equals("connectPointLinkage")){
					resource = XMLTree.searchNodeValue(slot,"rim:ValueList->rim:Value");
				}else if(attType.equals("serviceType")){
					serviceType = XMLTree.searchNodeValue(slot,"rim:ValueList->rim:Value");
				}
			}
			resources[0] = new Resource(resource,getProtocol(serviceType),null,null,null,null,null);
			return resources;
		}else{
			return new Resource[0];
		}

//		for (int i = 0; i < resources.length; i++){
//		resources[i] = new Resource(XMLTree.searchNodeValue(nodes[i],
//		"URL"),
//		Resource.DOWNLOAD,
//		XMLTree.searchNodeValue(nodes[i], "orName"),
//		XMLTree.searchNodeValue(nodes[i], "URLDescription"),
//		XMLTree.searchNodeAtribute(nodes[i], "orFunct->OnFunctCd",
//		"value"),
//		srs,	
//		coordinates);
//		if (resources[i].getLinkage() == null){
//		resources[i].setLinkage("");
//		}

//		}


//		return resources;	    
	}

	private String getProtocol(String serviceType) {
		if (serviceType == null){
			return Resource.UNKNOWN;
		}
		if (serviceType.compareTo("WMS")==0){
			return Resource.WMS;
		}else if (serviceType.compareTo("WCS")==0){
			return Resource.WCS;
		}else if (serviceType.compareTo("WFS")==0){
			return Resource.WFS;
		}else if (serviceType.compareTo("urn:uuid:677F6003-E6E0-4B5C-B930-09B36EF6E0FB")==0){
			return Resource.WMS;
		}
		return Resource.UNKNOWN;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.schemas.discoverer.Record#accept(java.net.URI, es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public boolean accept(URI uri, XMLNode node) {
		if (node.getName().equals("rim:Service")){
			return true;
		}
		return false;
	} 
}

