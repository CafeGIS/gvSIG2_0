package org.gvsig.catalog.csw.drivers;

import org.gvsig.catalog.csw.drivers.profiles.CSWISO19115Profile;
import org.gvsig.catalog.csw.parsers.CSWConstants;
import org.gvsig.catalog.drivers.profiles.IProfile;
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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CSWISO19115CatalogServiceDriver extends CSWCatalogServiceDriver{
	public static String SERVICE_NAME = "CSW/ISO 19115";

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return SERVICE_NAME;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.CSWCatalogServiceDriver#retrieveResults(es.gva.cit.catalog.metadataxml.XMLNode)
	 */
	protected XMLNode[] retrieveResults(XMLNode root){
		XMLNode resultNode = XMLTree.searchNode(root, CSWConstants.SEARCH_RESULTS);
		if (resultNode == null){
			return new XMLNode[0];
		}
		XMLNode[] results = new XMLNode[resultNode.getNumSubNodes()];
		for (int i=0 ; i<resultNode.getNumSubNodes() ; i++){
			results[i] = resultNode.getSubNode(i);
		}
		return results;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.AbstractCatalogServiceDriver#getProfile()
	 */
	public IProfile getProfile() {
		return new CSWISO19115Profile();
	}

}
