package org.gvsig.remoteClient.wfs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.EncodingXMLParser;
import org.gvsig.remoteClient.utils.Utilities;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

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
 * $Id: WFSProtocolHandlerFactory.java 27881 2009-04-07 14:47:27Z jpiera $
 * $Log$
 * Revision 1.2  2007-02-09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 * Revision 1.1  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSProtocolHandlerFactory {
	public WFSProtocolHandler wFSProtocolHandler;
	
	private static ArrayList supportedVersions = new ArrayList();
	
	static {
		supportedVersions.add("1.0.0");
		supportedVersions.add("1.1.0");
	}
	
	/**
	 * Método que dada una respuesta de getCapabilities y un iterador sobre una
	 * coleccion de WFSClient's ordenada descendentemente devuelve el cliente
	 * cuya version es igual o inmediatamente inferior
	 *
	 * @param caps Capabilities con la respuesta del servidor
	 * @param clients Iterador de conjunto ordenado descendientemente
	 *
	 * @return cliente cuya version es igual o inmediatamente inferior
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 
	 */
	private static String getDriverVersion(String version, Iterator clients) throws InstantiationException, IllegalAccessException {
		while (clients.hasNext()) {
			String clientVersion = (String)clients.next();
			int ret = version.compareTo(clientVersion);
			
			if (ret >= 0) {
				return clientVersion;
			}
		}
		return null;
	}
	
	/**
	 * Establece la versión con la que se comunicará con el servidor y devuelve
	 * el objeto Capabilities obtenido con dicha versión
	 *
	 * @param host maquina con la que se negocia
	 *
	 * @return instancia de un cliente capaz de negociar con el host que se
	 *         pasa como parámetro
	 */
	public static WFSProtocolHandler negotiate(String host) throws ConnectException, IOException {
		
		if (supportedVersions.size() == 0){
			return null;
		}
		
		try	{        	
			String highestVersionSupportedByServer  = getSuitableWFSVersion(host,"");
			if (supportedVersions.contains(highestVersionSupportedByServer))
			{
				// we support the highest version supported by the server
				// this is the best case 
				return createVersionDriver(highestVersionSupportedByServer);
			}
			else
			{
				// in case we dont support the highest version from the server
				// we start the negotiation process in which we have to get the higest version
				// the WFS supports and we are able to read.
				Iterator iVersion = supportedVersions.iterator();
				String wfsVersion;
				String gvSIGVersion;
				
				while (iVersion.hasNext()) { 
					gvSIGVersion = (String)iVersion.next();
					wfsVersion = getSuitableWFSVersion(host,gvSIGVersion);
					//TODO:
					//compare with the version returned by the WMS!!!!!
					// send GetCapabilities and read the version to compare.
					int res = wfsVersion.compareTo(gvSIGVersion);
					
					if (res == 0) { //Si es la misma que nuestra version                
						return createVersionDriver(gvSIGVersion);
					} else if (res > 0) { //Si es mayor que nuestra version
						throw new Exception("Server Version too high: " + wfsVersion);
					} else { //Si es menor que nuestra version
						//Obtenemos la primera version menor o igual que tengamos
						String lowerVersion = WFSProtocolHandlerFactory.getDriverVersion(wfsVersion, iVersion);
						
						if (lowerVersion == null) { //Si no hay ninguna
							throw new Exception("Lowest server version is " + wfsVersion);
						} else {
							if (lowerVersion.equals(wfsVersion)) { 
								return createVersionDriver(lowerVersion);
							} else { //Si hay una version menor que la que retorno el servidor
								//iV = lower;
							}
						}
					}
				}
			}//case we had to start the negotiation process.
			return null; // if it did not find any suitable version.        
		}
		catch(ConnectException conEx)
		{
			throw conEx;
		}
		catch(IOException ioEx)
		{
			throw ioEx;
		}        
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Sends a GetCapabilities to the WFS server to get the version
	 * if the version parameter is null, the WFS will return the highest version supported
	 * if not it will return the lower highest version than the one requested.
	 * @param host
	 * @param version
	 * @return suitable version supported by the server 
	 */
	private static String getSuitableWFSVersion(String host, String _version) throws ConnectException, IOException {    	 
		String request = WFSProtocolHandler.buildCapabilitiesSuitableVersionRequest(host, _version);
		String version = new String(); 
		Reader reader = null;
		try {   	
			File file = Utilities.downloadFile(new URL(request), "wfs-suitable-version", null);
						
			reader = new FileReader(file);
			EncodingXMLParser kxmlParser = new EncodingXMLParser();
			kxmlParser.setInput(reader);		
			kxmlParser.nextTag();
			if ( kxmlParser.getEventType() != KXmlParser.END_DOCUMENT ) {    		
				String tag = kxmlParser.getName();
				if (tag.split(":").length == 2){
					tag = tag.split(":")[1];
				}
				if ((tag.compareTo(CapabilitiesTags.WFS_CAPABILITIES_ROOT1_0_0)==0)) {
					version = kxmlParser.getAttributeValue("", CapabilitiesTags.VERSION);
				}
			}
			// do not forget to close the Stream.    		
			reader.close(); 			
			return version;		
		} catch(ConnectException conEx) {
			throw new ConnectException(conEx.getMessage()); 			
		} catch(IOException ioEx) {
			throw new IOException(ioEx.getMessage()); 	
		} catch(XmlPullParserException xmlEx) {
			xmlEx.printStackTrace();
			return "";
		} finally {		
			if (reader != null) {
				try {
					reader.close();
				} catch(Exception ex) {
					ex.printStackTrace(); 
				}
			}
		} 
	}
	
	/**
	 * It creates an instance of a WFSDriver class.
	 *
	 * @param String, with the version of the driver to be created
	 * @return WFSDriver.
	 */
	public static WFSProtocolHandler createVersionDriver(String version) {    	   
		try {
			Class driver;
			version = version.replace('.', '_');
			driver = Class.forName("org.gvsig.remoteClient.wfs.wfs_"+version+".WFSProtocolHandler" + version);
			return (WFSProtocolHandler)driver.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			//throw new Exception("WFSDriverFactory. Unknown driver version " + e);
			return null;
		}
	}    
	
	
	
}
