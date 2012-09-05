package org.gvsig.catalog.csw.parsers;

import java.net.URL;

import org.gvsig.catalog.csw.drivers.CSWCapabilities;
import org.gvsig.catalog.csw.drivers.CSWCatalogServiceDriver;
import org.gvsig.catalog.metadataxml.XMLNode;


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
public class CSWDescribeRecordParser {
	private URL url = null;
	private CSWCatalogServiceDriver driver = null;
	private CSWCapabilities capabilities = null; 
	
	public CSWDescribeRecordParser(URL url, CSWCatalogServiceDriver driver) {
		this.url = url;
		this.driver = driver;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.csw.parsers.CSWAbstractCapabilitiesParser#parse(es.gva.cit.catalogClient.metadataxml.XMLNode)
	 */
	public void parse(XMLNode node) {
		parseSchemaComponent(node.searchNode(CSWConstants.SCHEMA_COMPONENT));
		
	}
	
	/**
	 * Parses the SchemaComponent tag
	 * @param node
	 */
	private void parseSchemaComponent(XMLNode node){
		if (node == null){
			return;
		}
		XMLNode schema = node.searchNode(CSWConstants.SCHEMA);
	}
}
