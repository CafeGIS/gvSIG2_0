
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
package org.gvsig.catalog.metadataxml;
import java.io.ByteArrayInputStream;

/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class XMLTreeNumberOfRecordsAnswer {

/**
 * 
 * 
 * 
 * @return 
 * @param numberOfRecords 
 * @param firstRecord 
 * @param lastRecord 
 */
    public static XMLNode getNode(int numberOfRecords, int firstRecord, int lastRecord) {        
        String message = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
            "<Elements> " + "<NumberOfRecords>" +
            String.valueOf(numberOfRecords) + "</NumberOfRecords>" +
            "<FirstRecord>" + String.valueOf(firstRecord) + "</FirstRecord>" +
            "<LastRecord>" + String.valueOf(lastRecord) + "</LastRecord>" +
            "</Elements>";
        ByteArrayInputStream buffer = new ByteArrayInputStream(message.getBytes());
        return XMLTree.xmlToTree(buffer);
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public static int getNumberOfRecords(XMLNode node) {        
        return Integer.parseInt(XMLTree.searchNodeValue(node, "NumberOfRecords"));
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public static int getFirstRecord(XMLNode node) {        
        return Integer.parseInt(XMLTree.searchNodeValue(node, "FirstRecord"));
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public static int getLastRecord(XMLNode node) {        
        return Integer.parseInt(XMLTree.searchNodeValue(node, "LastRecord"));
    } 
 }
