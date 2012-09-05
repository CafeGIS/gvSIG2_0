
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
package org.gvsig.catalog.protocols;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;

import org.apache.soap.Constants;
import org.apache.soap.Envelope;
import org.apache.soap.SOAPException;
import org.apache.soap.messaging.Message;
import org.apache.soap.transport.SOAPTransport;
import org.apache.soap.util.xml.XMLParserUtils;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SOAPProtocol implements IProtocols {

/**
 * 
 * 
 * 
 * @return 
 * @param url 
 * @param object 
 * @param firstRecord 
 */
    public Collection doQuery(URL url, Object object, int firstRecord) {        
        String message = (String) object;
        File fileAnswer = null;
       
        //get the envelope to send
        try {
            InputStream buffer = new ByteArrayInputStream(message.getBytes());
            DocumentBuilder xdb = XMLParserUtils.getXMLDocBuilder();
            Document doc = xdb.parse(new InputSource(buffer));
            if (doc == null) {
                throw new SOAPException(Constants.FAULT_CODE_CLIENT,
                    "parsing error");
            }
            Envelope msgEnv = Envelope.unmarshall(doc.getDocumentElement());
            // send the message
            Message msg = new Message();
            msg.send(url, "urn:this-is-the-action-uri", msgEnv);
            // receive whatever from the transport and dump it to the screen
            SOAPTransport st = msg.getSOAPTransport();
            BufferedReader br = st.receive();
            String line;
            //File to save the answer
            fileAnswer = new File("auxiliar");
            FileWriter fwAnswer = new FileWriter(fileAnswer);
            fwAnswer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            //Process the answer	    		    
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                fwAnswer.write(line);
                fwAnswer.flush();
            }
            fwAnswer.close();
               
         
        } catch (Exception e) {
            return null;
        }
        Collection col = new java.util.ArrayList();
        col.add(XMLTree.xmlToTree(fileAnswer));
        return col;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param url 
 */
    public static boolean isProtocolSupported(URL url) {        
        return true;
    } 

/**
 * This function returns a SOAP message
 * 
 * 
 * @return 
 * @param message the XML message
 * @param schemas 
 */
    public static String setSOAPMessage(String message, String[] schemas) {        
        String soap =  "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
        	"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" ;
        if (schemas != null){
            for (int i=0 ; i<schemas.length ; i++){
                soap = soap + " " + schemas[i];
            }
        }
        soap = soap + ">";
        soap = soap + "<SOAP:Body>" + message + "</SOAP:Body>" + "</SOAP:Envelope>";
        return soap;
        
    } 
 }
