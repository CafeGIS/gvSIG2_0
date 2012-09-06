
package org.gvsig.remoteClient.wms;

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
 * <p></p>
 *
 */
public class WMSProtocolHandlerFactory {
/**
 * <p></p>
 *
 */
    public org.gvsig.remoteClient.wms.WMSProtocolHandler wMSProtocolHandler;

    private static ArrayList supportedVersions = new ArrayList();

    static {
        /*
         * Se meten en el array versions las distintas versiones
         * del protocolo en orden descendente
         */
    	//versions.add(WMSProtocolHandler1_3_0.class);
        //versions.add(WMSProtocolHandler1_1_1.class);
    	supportedVersions.add("1.3.0");
    	supportedVersions.add("1.1.1");
    	supportedVersions.add("1.1.0");
     }

    /**
     * Método que dada una respuesta de getCapabilities y un iterador sobre una
     * coleccion de WMSClient's ordenada descendentemente devuelve el cliente
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
     public static WMSProtocolHandler negotiate(String host) throws ConnectException, IOException {

        if (supportedVersions.size() == 0)
        {
        	return null;
        }

        try
        {
        	String highestVersionSupportedByServer  = getSuitableWMSVersion(host,"");
        	if (supportedVersions.contains(highestVersionSupportedByServer))
        	{
        		//we support the highest version supported by the server
        		// this is the best case
        		return createVersionDriver(highestVersionSupportedByServer);
        	}


        else
        	{
        		// in case we dont support the highest version from the server
        		// we start the negotiation process in which we have to get the higest version
        		// the WMS supports and we are able to read.
        		Iterator iVersion = supportedVersions.iterator();
        		String wmsVersion;
        		String gvSIGVersion;

        		while (iVersion.hasNext()) {
		                gvSIGVersion = (String)iVersion.next();
		                wmsVersion = getSuitableWMSVersion(host,gvSIGVersion);
		                //TODO:
		                //compare with the version returned by the WMS!!!!!
		                // send GetCapabilities and read the version to compare.
		                int res = wmsVersion.compareTo(gvSIGVersion);

		                if (res == 0) { //Si es la misma que nuestra version
		                    return createVersionDriver(gvSIGVersion);
		                } else if (res > 0) { //Si es mayor que nuestra version
		                    throw new Exception("Server Version too high: " + wmsVersion);
		                } else { //Si es menor que nuestra version
		                         //Obtenemos la primera version menor o igual que tengamos
		                    String lowerVersion = WMSProtocolHandlerFactory.getDriverVersion(wmsVersion, iVersion);

		                    if (lowerVersion == null) { //Si no hay ninguna
		                        throw new Exception("Lowest server version is " + wmsVersion);
		                    } else {
		                        if (lowerVersion.equals(wmsVersion)) {
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
      * Sends a GetCapabilities to the WMS server to get the version
      * if the version parameter is null, the WMS will return the highest version supported
      * if not it will return the lower highest version than the one requested.
      * @param host
      * @param version
      * @return suitable version supported by the server
      */
     private static String getSuitableWMSVersion(String host, String _version) throws ConnectException, IOException
     {
    	String request = WMSProtocolHandler.buildCapabilitiesSuitableVersionRequest(host, _version);
    	String version = new String();
    	StringReader reader = null;
    	//InputStreamReader reader;
    	//InputStream is = null;
    	DataInputStream dis = null;
		try
		{
			URL url = new URL(request);
            byte[] buffer = new byte[1024];//new byte[1024*256];
//            is = url.openStream();
//            reader = new InputStreamReader(is);
            //int numberOfBytes = is.read(buffer);
            //String readed = new String(buffer);
            dis = new DataInputStream(url.openStream());
            dis.readFully(buffer);
            String string = new String(buffer);

            // patch for ArcIMS + WMS connector > 9.0 bug
            int a = string.toLowerCase().indexOf("<?xml");
            if (a !=-1)
            	string = string.substring(a, string.length());
            // end patch


            reader = new StringReader(string);
	    	KXmlParser kxmlParser = null;
	    	kxmlParser = new KXmlParser();
    		kxmlParser.setInput(reader);
			kxmlParser.nextTag();
    		if ( kxmlParser.getEventType() != KXmlParser.END_DOCUMENT )
    		{
    			if ((kxmlParser.getName().compareTo(CapabilitiesTags.CAPABILITIES_ROOT1_1_0)==0)
    				||(kxmlParser.getName().compareTo(CapabilitiesTags.CAPABILITIES_ROOT1_1_1)==0)
    				||(kxmlParser.getName().compareTo(CapabilitiesTags.CAPABILITIES_ROOT1_3_0)==0))
    			{
    				version = kxmlParser.getAttributeValue("", CapabilitiesTags.VERSION);
    			}
    		}
    		// do not forget to close the Stream.
    		reader.close();
    		dis.close();
			return version;
		}
		catch(ConnectException conEx)
		{
			throw new ConnectException(conEx.getMessage());
		}
		catch(IOException ioEx)
		{
			throw new IOException(ioEx.getMessage());
		}
		catch(XmlPullParserException xmlEx)
		{
			xmlEx.printStackTrace();
			return "";
		}
		finally{
			if (reader != null)
			{
				try{
					reader.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			if (dis != null)
			{
				try {
					dis.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
     }

     /**
      * It creates an instance of a WMSDriver class.
      *
      * @param String, with the version of the driver to be created
      * @return WMSDriver.
      */
       private static WMSProtocolHandler createVersionDriver(String version)
       {
         try
         {
           Class driver;
           version = version.replace('.', '_');
           driver = Class.forName("org.gvsig.remoteClient.wms.wms_"+version+".WMSProtocolHandler" + version);
           return (WMSProtocolHandler)driver.newInstance();
         }
         catch (Exception e)
         {
        	 e.printStackTrace();
           //throw new Exception("WMSDriverFactory. Unknown driver version " + e);
        	 return null;
         }
       }

 }
