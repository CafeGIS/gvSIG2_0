
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
package org.gvsig.gazetteer.idec.parsers;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.gazetteer.drivers.GazetteerCapabilitiesParser;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.i18n.Messages;


/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class IdecCapabilitiesParser extends GazetteerCapabilitiesParser{
	
	public IdecCapabilitiesParser(IGazetteerServiceDriver driver) {
		super(driver);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.GazeteerCapabilitiesParser#parseCapabilities(es.gva.cit.catalog.metadataxml.XMLNode)
	 */
	protected void parseCapabilities(XMLNode node) {
		String prefix = "";
		if (node.getName().equals("wsdl:definitions")){
			prefix = "wsdl:";
		}

		if (!(node.getName().equals(prefix + "definitions"))){        		
			capabilities.setServerMessage(Messages.getText("errorNotSupportedProtocol"));
			capabilities.setAvailable(false);
			return;
		}

		XMLNode[] nodes = XMLTree.searchMultipleNode(node,prefix + "message");

		if (nodes.length == 0){
			capabilities.setServerMessage(Messages.getText("El servidor no timplementa ninguna " +
						"operación"));
			capabilities.setAvailable(false);
			return;			    
		}

		String msg = "El servidor soporta las operaciones: ";

		msg = msg + XMLTree.searchAtribute(nodes[0],"name");

		for (int i=1 ; i<nodes.length ; i++){
			msg = msg + ", " + XMLTree.searchAtribute(nodes[i],"name");
		}

		driver.setServerAnswerReady(msg);

		driver.setProjection("EPSG:23031");  		
	}
}
