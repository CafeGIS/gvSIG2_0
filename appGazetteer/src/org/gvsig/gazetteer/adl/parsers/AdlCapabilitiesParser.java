
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
package org.gvsig.gazetteer.adl.parsers;

import java.net.MalformedURLException;
import java.net.URL;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.gazetteer.adl.drivers.ADLGazetteerServiceDriver;
import org.gvsig.gazetteer.adl.protocols.ADLThesaurus;
import org.gvsig.gazetteer.drivers.GazetteerCapabilities;
import org.gvsig.gazetteer.drivers.GazetteerCapabilitiesParser;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.i18n.Messages;


/**
 * This Class parses the ADL getCapabilities file
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class AdlCapabilitiesParser extends GazetteerCapabilitiesParser{

	public AdlCapabilitiesParser(IGazetteerServiceDriver driver,
			GazetteerCapabilities capabilities) {
		super(driver, capabilities);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.GazeteerCapabilitiesParser#parseCapabilities(es.gva.cit.catalog.metadataxml.XMLNode)
	 */
	protected void parseCapabilities(XMLNode node) {
		if (!(node.getName().equals("gazetteer-service"))){
			capabilities.setServerMessage(Messages.getText("errorNotSupportedProtocol"));
			capabilities.setAvailable(false);
			return;
		}

		driver.setServerAnswerReady(XMLTree.searchNodeValue(node,
		"get-capabilities-response->gazetteer-capabilities->description"));

		String host = XMLTree.searchNodeAtribute(node,
				"get-capabilities-response->gazetteer-capabilities->thesauri->thesaurus",
		"xlink:href");

		if (host == null){
			host = "http://middleware.alexandria.ucsb.edu:8080/thes/FTT";
			driver.setServerAnswerReady("No se ha podido cargar el "+
					"Tesauro correctamante. Se va ha usar el Tesauro de alejandría, " +
					"pero no se puede garantizar que el tesauro el servidor sea el mismo " +
			"(http://middleware.alexandria.ucsb.edu:8080/thes/FTT)");
		}

		try {
			ADLThesaurus t = new ADLThesaurus(new URL(host));
			((ADLGazetteerServiceDriver)driver).setFeatureTypes(t.getFeatures());
		} catch (MalformedURLException e) {
			capabilities.setServerMessage("La URL del tesauro no es correcta");
			capabilities.setAvailable(false);
			return;
		}

		driver.setProjection("EPSG:4326");		
	}
 }
