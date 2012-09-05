
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
/*
* CVS MESSAGES:
*
* $Id: SRWMessages.java 585 2007-09-03 10:21:55 +0000 (Mon, 03 Sep 2007) jpiera $
* $Log$
* Revision 1.6.10.1  2007/07/10 11:18:04  jorpiell
* Added the registers
*
* Revision 1.6  2006/03/21 07:13:52  jorpiell
* El gazetteer ya reproyecta. Se han tenido que modificar los parsers de todos los protocolos.
*
* Revision 1.4.2.2  2006/03/14 07:18:45  jorpiell
* Se ha añadido la goma de borrar y se ha cambiado el tamaño de los botones
*
* Revision 1.5  2006/03/01 13:23:20  jorpiell
* Se ha añadido al Head lo de la 0.6 y se ha hecho un commit
*
* Revision 1.4.2.1  2006/02/02 13:44:59  jorpiell
* Se ha tenido que cambiar el driver SRW para acceder al servidor de catálogo de la IDEE. Además se ha creado un nuevo filtro para este servidor
*
* Revision 1.4  2006/01/12 13:52:24  jorpiell
* Se ha añadido un boton close en las dos pantallas de connect. Además se ha cambiado el comportamiento de las ventanas para adaptarlo a la nueva forma de buscar multihilo
*
* Revision 1.3  2006/01/10 17:23:23  jorpiell
* Se ha hecho una pequeña modificación en el gazeteer: ahora funcionan las búsquedas restringiendo el área en el WFSG. Hay muchos cambios porque se ha hecho un CONTROL+SHIFT+O sobre todo el proyecto para eliminar los imports y para ordenarlos
*
* Revision 1.2  2006/01/10 09:32:49  jorpiell
* Se ha echo un commit de las versiones modificadas del catalogo y del gazetteer usando el Poseidon. Se han renombrado algunas clases por considerar que tenian un nombre confuso y se han cambiado algunas relaciones entre clases (con la intención de separar GUI de la parte de control). Han habido clases que no han sido tocadas, pero como han sido formateadas usando el Poseidon TODAS las CLASES del proyecto han cambiado de versión.
*
* Revision 1.1  2005/12/22 08:31:43  jorpiell
* Aqui tambien se han producido muchos cambis, porque hemos acabado de cambiar la estructura del catálogo: Se han creado todas las clases "XXXMessages", que sacan toda la parte de los mensajes de los drivers. Ademas se ha incluido en "CatalogClient" la operación "getCapabilities", que libera a la interfaz de algunas operaciones que hacía anteriormente.
*
*
*/
package org.gvsig.catalog.srw.drivers;
import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.drivers.IProtocolMessages;
import org.gvsig.catalog.protocols.SOAPProtocol;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.srw.filters.IDEESRWFilter;
import org.gvsig.catalog.srw.filters.SRWFilter;


/**
 * This class create the SRW protocol messages
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SRWMessages implements IProtocolMessages {
/**
 * 
 * 
 */
    private SRWCatalogServiceDriver driver;

/**
 * 
 * 
 * 
 * @param driver 
 */
    public  SRWMessages(SRWCatalogServiceDriver driver) {        
        this.driver = driver;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param upper 
 */
    public NameValuePair[] getHTTPGETCapabilities(boolean upper) {        
     NameValuePair nvp1 = new NameValuePair("OPERATION", "explain");
     NameValuePair nvp2 = new NameValuePair("VERSION", driver.getVersion());
        return new NameValuePair[] { nvp1, nvp2 };
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public NameValuePair[] getHTTPGETDescribeRecords() {        
        // TODO Auto-generated method stub
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param query 
 * @param firstRecord 
 */
    public NameValuePair[] getHTTPGETRecords(CatalogQuery query, int firstRecord) {        
       
        NameValuePair nvp1 = new NameValuePair("operation", "searchRetrieve");
        NameValuePair nvp2 = new NameValuePair("version", driver.getVersion());
        NameValuePair nvp3 = new NameValuePair("query", new SRWFilter().getQuery(query));
        NameValuePair nvp4 = new NameValuePair("maximumRecords", "10");
        NameValuePair nvp5 = new NameValuePair("recordPacking",
                driver.getOutputFormat());
        NameValuePair nvp6 = new NameValuePair("startRecord",
                new String(new Integer(firstRecord).toString()));
        return new NameValuePair[] { nvp1, nvp2, nvp3, nvp4, nvp5, nvp6 };
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public String getHTTPPOSTCapabilities() {        
        // TODO Auto-generated method stub
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public String getHTTPPOSTDescribeRecords() {        
        // TODO Auto-generated method stub
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param query 
 * @param firstRecord 
 */
    public String getHTTPPOSTRecords(CatalogQuery query, int firstRecord) {        
        // TODO Auto-generated method stub
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public String getSOAPCapabilities() {        
        String soapMessage =
            "<SRW:explainRequest xmlns:SRW=\"http://www.loc.gov/zing/srw/\">" +
            "<SRW:version>" + driver.getVersion() + "</SRW:version>" +
            "</SRW:explainRequest>";
        return SOAPProtocol.setSOAPMessage(soapMessage,null);
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public String getSOAPDescribeRecords() {        
        // TODO Auto-generated method stub
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param query 
 * @param firstRecord 
 */
    public String getSOAPRecords(CatalogQuery query, int firstRecord) {        
    	
        String soapMessage =
            "<srw:searchRetrieveRequest xmlns:srw=\"http://www.loc.gov/zing/srw/\">" +
            "<srw:query><![CDATA[" + new IDEESRWFilter().getQuery(query) + "]]></srw:query>" +
            "<srw:sortKeys xsi:nil=\"true\"/>" + "<srw:startRecord>" +
            driver.getStartPosition() + "</srw:startRecord>" + "<srw:maximumRecords>" +
            driver.getMaxRecords() + "</srw:maximumRecords>" + "<srw:recordPacking>" +
            driver.getOutputFormat() + "</srw:recordPacking>" + "<srw:recordSchema>" +
            driver.getOutputSchema()[0] + "</srw:recordSchema>" +
            "<srw:resultSetTTL>" + driver.getResultSetTTL() + "</srw:resultSetTTL>" +
            "</srw:searchRetrieveRequest>";
        return SOAPProtocol.setSOAPMessage(soapMessage,null);
    } 
 }
