
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
package org.gvsig.catalog.drivers;
import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.querys.CatalogQuery;

/**
 * This interface has one method for each operation and for each
 * communication protocol. Each method returns the protocol
 * message.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public interface IProtocolMessages {
/**
 * operation : getCapabilities
 * protocol : HTTP GET
 * 
 * 
 * @return NameValuePair with the request parameters
 * @param upper If the 'G' of the getCapabilities must be written in uppercase
 */
    public NameValuePair[] getHTTPGETCapabilities(boolean upper);
/**
 * operation : describeRecords
 * protocol : HTTP GET
 * 
 * 
 * @return NameValuePair with the request parameters
 */
    public NameValuePair[] getHTTPGETDescribeRecords();
/**
 * operation : getRecords
 * protocol : HTTP GET
 * 
 * 
 * @return NameValuePair with the request parameters
 * @param query 
 * @param firstRecord 
 */
    public NameValuePair[] getHTTPGETRecords(CatalogQuery query, int firstRecord);
/**
 * operation : getCapabilities
 * protocol : HTTP POST
 * 
 * 
 * @return String with the POST message
 */
    public String getHTTPPOSTCapabilities();
/**
 * operation : describeRecords
 * protocol : HTTP POST
 * 
 * 
 * @return String with the POST message
 */
    public String getHTTPPOSTDescribeRecords();
/**
 * operation : getRecords
 * protocol : HTTP POST
 * 
 * 
 * @return String with the POST message
 * @param query 
 * @param firstRecord 
 */
    public String getHTTPPOSTRecords(CatalogQuery query, int firstRecord);
/**
 * operation : getCapabilities
 * protocol : SOAP
 * 
 * 
 * @return String with the SOAP message
 */
    public String getSOAPCapabilities();
/**
 * operation : describeRecords
 * protocol : SOAP
 * 
 * 
 * @return String with the SOAP message
 */
    public String getSOAPDescribeRecords();
/**
 * operation : getRecords
 * protocol : SOAP
 * 
 * 
 * @return String with the SOAP message
 * @param query 
 * @param firstRecord 
 */
    public String getSOAPRecords(CatalogQuery query, int firstRecord);
}


