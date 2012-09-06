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
/*
 * $Id: WCSProtocolHandlerFactory.java 6422 2006-07-18 11:41:05Z jaume $
 * $Log$
 * Revision 1.4  2006-07-18 11:41:05  jaume
 * no hay nada nuevo pero el team synchronize está pesado
 *
 * Revision 1.3  2006/04/25 06:47:50  jaume
 * clean up unnecessary imports
 *
 * Revision 1.2  2006/03/15 08:54:42  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2006/03/08 09:08:31  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/06 15:18:32  jaume
 * *** empty log message ***
 *
 */
package org.gvsig.remoteClient.wcs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author jaume dominguez faus
 *
 */
public class WCSProtocolHandlerFactory {

	public org.gvsig.remoteClient.wcs.WCSProtocolHandler wCSProtocolHandler;

	private static ArrayList supportedVersions = new ArrayList();

	static {
		supportedVersions.add("1.0.0");
	}

	/**
	 * Método que dada una respuesta de getCapabilities y un iterador sobre una
	 * coleccion de WCSClient's ordenada descendentemente devuelve el cliente
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
	public static WCSProtocolHandler negotiate(String host) throws ConnectException, IOException {

		if (supportedVersions.size() == 0)
		{
			return null;
		}

		try
		{
			String highestVersionSupportedByServer  = getSuitableWCSVersion(host,"");
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
				// the WCS supports and we are able to read.
				Iterator iVersion = supportedVersions.iterator();
				String wcsVersion;
				String gvSIGVersion;

				while (iVersion.hasNext()) {
					gvSIGVersion = (String)iVersion.next();
					wcsVersion = getSuitableWCSVersion(host,gvSIGVersion);
					//TODO:
					//compare with the version returned by the WMS!!!!!
					// send GetCapabilities and read the version to compare.
					int res = wcsVersion.compareTo(gvSIGVersion);

					if (res == 0) { //Si es la misma que nuestra version
						return createVersionDriver(gvSIGVersion);
					} else if (res > 0) { //Si es mayor que nuestra version
						throw new Exception("Server Version too high: " + wcsVersion);
					} else { //Si es menor que nuestra version
						//Obtenemos la primera version menor o igual que tengamos
						String lowerVersion = WCSProtocolHandlerFactory.getDriverVersion(wcsVersion, iVersion);

						if (lowerVersion == null) { //Si no hay ninguna
							throw new Exception("Lowest server version is " + wcsVersion);
						} else {
							if (lowerVersion.equals(wcsVersion)) {
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
//			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sends a GetCapabilities to the WCS server to get the version
	 * if the version parameter is null, the WCS will return the highest version supported
	 * if not it will return the lower highest version than the one requested.
	 * @param host
	 * @param version
	 * @return suitable version supported by the server
	 */
	private static String getSuitableWCSVersion(String host, String _version) throws ConnectException, IOException {
		String request = WCSProtocolHandler.buildCapabilitiesSuitableVersionRequest(host, _version);
		String version = new String();
		StringReader reader = null;
		//InputStreamReader reader;
		//InputStream is = null;
		DataInputStream dis = null;
		try {
			URL url = new URL(request);
			byte[] buffer = new byte[1024];//new byte[1024*256];
//			is = url.openStream();
//			reader = new InputStreamReader(is);
			//int numberOfBytes = is.read(buffer);
			//String readed = new String(buffer);
			dis = new DataInputStream(url.openStream());
			dis.readFully(buffer);

			reader = new StringReader(new String(buffer));
			KXmlParser kxmlParser = null;
			kxmlParser = new KXmlParser();
			kxmlParser.setInput(reader);
			kxmlParser.nextTag();
			if ( kxmlParser.getEventType() != KXmlParser.END_DOCUMENT ) {
				if ((kxmlParser.getName().compareTo(CapabilitiesTags.WCS_CAPABILITIES_ROOT1_0_0)==0)) {
					version = kxmlParser.getAttributeValue("", CapabilitiesTags.VERSION);
				}
			}
			// do not forget to close the Stream.
			reader.close();
			dis.close();
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
			} if (dis != null) {
				try {
					dis.close();
				} catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * It creates an instance of a WCSDriver class.
	 *
	 * @param String, with the version of the driver to be created
	 * @return WCSDriver.
	 */
	private static WCSProtocolHandler createVersionDriver(String version) {
		try {
			Class driver;
			version = version.replace('.', '_');
			driver = Class.forName("org.gvsig.remoteClient.wcs.wcs_"+version+".WCSProtocolHandler" + version);
			return (WCSProtocolHandler)driver.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			//throw new Exception("WCSDriverFactory. Unknown driver version " + e);
			return null;
		}
	}


}
