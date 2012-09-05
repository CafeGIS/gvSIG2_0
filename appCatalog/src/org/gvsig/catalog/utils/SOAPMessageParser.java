
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
package org.gvsig.catalog.utils;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;

/**
 * This class is used to parse a SOAP message
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SOAPMessageParser {

/**
 * Cut the SOAP Head
 * 
 * 
 * @return A new node without the SOAP Head
 * @param node Node retreived
 */
    public static XMLNode cutHead(XMLNode node) {        
        XMLNode[] children = node.getSubnodes();
        for (int i = 0; i < node.getNumSubNodes(); i++)
            if (children[i].getName().equals("SOAP-ENV:Body")) {
                return children[i];
            }
        return null;
    } 

/**
 * Get the fault code (if it exists)
 * 
 * 
 * @return The Fault message or NULL
 * @param node The node retrieved
 */
    public static String getFault(XMLNode node) {        
        if (node == null) {
            return null;
        }
        node = cutHead(node);
        if (node.getName().equals("SOAP-ENV:Fault")) {
            return "FAULT-CODE = " +
            XMLTree.searchNodeValue(node, "faultcode") + "\nREASON = " +
            "Este error se produce normalmente cuando el " +
            "servidor no implementa la operacion getCapabilities";
        }
        //XMLTree.searchNodeValue(node,"faultstring");
        return null;
    } 
 }
