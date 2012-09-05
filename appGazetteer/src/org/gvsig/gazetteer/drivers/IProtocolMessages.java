
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
package org.gvsig.gazetteer.drivers;
import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.gazetteer.querys.GazetteerQuery;

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
 * operation : describeFeature
 * protocol : HTTP GET
 * 
 * @param fetaure Feature to describe
 * 
 * @return NameValuePair with the request parameters
 * @param feature 
 */
    public NameValuePair[] getHTTPGETDescribeFeature(String feature);
/**
 * operation : getFeature
 * protocol : HTTP GET
 * 
 * 
 * @return NameValuePair with the request parameters
 * @param query 
 * @param firstRecord 
 */
    public NameValuePair[] getHTTPGETFeatures(GazetteerQuery query, int firstRecord);
/**
 * operation : getCapabilities
 * protocol : HTTP POST
 * 
 * 
 * @return String with the POST message
 */
    public String getHTTPPOSTCapabilities();
/**
 * operation : describeFeature
 * protocol : HTTP POST
 * 
 * @param fetaure Feature to describe
 * 
 * @return String with the POST message
 * @param feature 
 */
    public String getHTTPPOSTDescribeFeature(String feature);
/**
 * operation : getFeature
 * protocol : HTTP POST
 * 
 * 
 * @return String with the POST message
 * @param query 
 * @param firstRecord 
 */
    public String getHTTPPOSTFeature(GazetteerQuery query, int firstRecord);
/**
 * operation : getCapabilities
 * protocol : SOAP
 * 
 * 
 * @return String with the SOAP message
 */
    public String getSOAPCapabilities();
/**
 * operation : describeFeature
 * protocol : SOAP
 * 
 * @param fetaure Feature to describe
 * 
 * @return String with the SOAP message
 * @param feature 
 */
    public String getSOAPDescribeFeature(String feature);
/**
 * operation : getFeature
 * protocol : SOAP
 * 
 * 
 * @return String with the SOAP message
 * @param query 
 * @param firstRecord 
 */
    public String getSOAPFeature(GazetteerQuery query, int firstRecord);
}


