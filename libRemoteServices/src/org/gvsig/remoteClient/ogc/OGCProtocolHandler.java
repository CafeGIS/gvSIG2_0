/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.remoteClient.ogc;

import java.io.File;
import java.io.IOException;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSOperation;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class OGCProtocolHandler {
	/**
	 * procotol handler name
	 */
    protected String name;
    /**
     * protocol handler version
     */
    protected String version;
    /**
     * host of the WMS to connect
     */
    protected String host;
    /**
     *  port number of the comunication channel of the WMS to connect
     */
    protected String port;    
    
    /**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the port.
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
    
    /**
     * parses the data retrieved by the Capabilities XML document
     */
    public abstract boolean parseCapabilities(File f);
    
    public abstract OGCServiceInformation getServiceInformation();
		
	/**
	 * Just for not repeat code. Gets the correct separator according 
	 * to the server URL
	 * @param h
	 * @return
	 */
	protected static String getSymbol(String h) {
		String symbol;
		if (h.indexOf("?")==-1) 
			symbol = "?";
		else if (h.indexOf("?")!=h.length()-1)
			symbol = "&";
		else
			symbol = "";
		return symbol;
	}  
	
	/**
	 * Parse an operation into a DcpType tag
	 * @param parser
	 * The KXMLParser
	 * @param operation
	 * The WFS operation to parse
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	protected void parserDcpType(KXmlParser parser, String operation) throws XmlPullParserException, IOException {        
		int currentTag;
		boolean end = false;

		currentTag = parser.next();

		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if(parser.getName().compareTo(CapabilitiesTags.HTTP)==0){
					parseHTTPTag(parser, operation);
				}	         
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.DCPTYPE) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}     
	}

	/**
	 * Parse an operation into a HTTP tag
	 * @param parser
	 * The KXMLParser
	 * @param operation
	 * The WFS operation to parse
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	protected void parseHTTPTag(KXmlParser parser, String operation) throws XmlPullParserException, IOException {        
		int currentTag;
		boolean end = false;

		currentTag = parser.next();
		int protocol = -1;
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				String value = null;
				if(parser.getName().compareTo(CapabilitiesTags.GET)==0){
					protocol = OGCClientOperation.PROTOCOL_GET;
					addOperationByAttribute(parser, operation, protocol);
				}else if(parser.getName().compareTo(CapabilitiesTags.POST)==0){
					protocol = OGCClientOperation.PROTOCOL_POST;
					addOperationByAttribute(parser, operation, protocol);
				}else if(parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE)==0){
					addOperationByAttribute(parser, operation, protocol);
				}				
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.HTTP) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = parser.next();
			}
		}     
	}
	
	/**
	 * Add an operation and the online resource 
	 * @param parser
	 * The parser
	 * @param operation
	 * The operation to add
	 * @param protocol
	 * The parser to add
	 */
	protected void addOperationByAttribute(KXmlParser parser, String operation, int protocol){
		String value = null;
		if (protocol > -1){
			for (int i=0 ; i<parser.getAttributeCount() ; i++){
				if ((parser.getAttributeName(i).toUpperCase().compareTo(CapabilitiesTags.ONLINERESOURCE.toUpperCase()) == 0) ||
						(parser.getAttributeName(i).equals(CapabilitiesTags.XLINK_HREF))){					
					value = parser.getAttributeValue(i);
				}
			}								
			if (value == null){
				getServiceInformation().addOperation(operation, protocol);
			}else{
				getServiceInformation().addOperation(operation, protocol, value);
			}
		}  
	}
}
