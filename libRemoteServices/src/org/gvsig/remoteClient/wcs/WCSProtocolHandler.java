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
package org.gvsig.remoteClient.wcs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.gvsig.remoteClient.exceptions.ServerErrorException;
import org.gvsig.remoteClient.exceptions.WCSException;
import org.gvsig.remoteClient.ogc.OGCProtocolHandler;
import org.gvsig.remoteClient.utils.ExceptionTags;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wcs.request.WCSDescribeCoverageRequest;
import org.gvsig.remoteClient.wcs.request.WCSGetCoverageRequest;
import org.gvsig.remoteClient.wms.ICancellable;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
/**
 *
 * @author jaume
 *
 */
public abstract class WCSProtocolHandler extends OGCProtocolHandler {
	/**
	 * Encoding used to parse different xml documents.
	 */
	protected String encoding = "UTF-8";
	protected Hashtable layerPool = new Hashtable();

	/**
     * WCS metadata
     */
    protected ServiceInformation serviceInfo = new ServiceInformation();

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.OGCProtocolHandler#setHost(java.lang.String)
	 */
	public void setHost(String host) {
		try {
			// Validates the URL if doesn't produces an exception
			new URL(host);

			int index = host.indexOf("?");
			
			if (index == -1)
				super.setHost(host);
			else
				super.setHost(host.substring(0, index));
		}
		catch (MalformedURLException m) {
			// Bad URL -> hold it
			super.setHost(host);
		}
	}

	/**
     * <p>
     * Builds a GetCapabilities request that is sent to the WCS
     * the response will be parse to extract the data needed by the
     * WCS client.
     * </p>
     * @param override, if true the cache is ignored
     */
    public void getCapabilities(WCSStatus status, boolean override, ICancellable cancel) {
           URL request = null;
            try {
                request = new URL(buildCapabilitiesRequest(status));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            try {
            	if (override)
    				Utilities.removeURL(request);
            	File f =  Utilities.downloadFile(request,"wcs_capabilities.xml", cancel);
            	if (f!=null)
            		parseCapabilities(f);
            } catch(Exception e) {
            	e.printStackTrace();
            }
    }

    /**
     * Builds a complete URL-string that can be used to send a GetCapabilities request.
     * @return String
     */
    private String buildCapabilitiesRequest(WCSStatus status) {
    	StringBuffer req = new StringBuffer();
		String symbol = null;

		String onlineResource;
		if (status == null || status.getOnlineResource() == null)
			onlineResource = getHost();
		else
			onlineResource = status.getOnlineResource();
		symbol = getSymbol(onlineResource);

		req.append(onlineResource).append(symbol).append("REQUEST=GetCapabilities&SERVICE=WCS&");
		req.append("VERSION=").append(getVersion()).append("&EXCEPTIONS=XML");
		return req.toString();
    }



    /**
     * parses the data retrieved by the DescribeCoverage XML document
     */
    public abstract boolean parseDescribeCoverage(File f);

    /**
     * Send a DescribeCoverage request using the settings passed in the status argument.
     * If status is null, then default settings are used.
     * @param override
     * @return String
     */
    public void describeCoverage(WCSStatus status, boolean override, ICancellable cancel) {
       try {
    	   WCSDescribeCoverageRequest request = createDescribeCoverageRequest(status);
           File f = request.sendRequest();
        	if (f!=null)
        		parseDescribeCoverage(f);
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * Send a GetCoverage request using the settings passed in the status.
     * @return String
     */
    public File getCoverage(WCSStatus status, ICancellable cancel) throws ServerErrorException, WCSException {
    	try
		{
			//TODO:
			//pass this buildXXXRequest to the WCSProtocolHandlerXXX: The request can depend on the WCS version.
			WCSGetCoverageRequest request = createGetCoverageRequest(status);
            File f = request.sendRequest();

            if (f!=null && Utilities.isTextFile(f)) {
	    		FileInputStream fis = new FileInputStream(f);
	    		FileChannel fc = fis.getChannel();
	    		byte[] data = new byte[(int)fc.size()];   // fc.size returns the size of the file which backs the channel
	    		ByteBuffer bb = ByteBuffer.wrap(data);
	    		fc.read(bb);

	    		WCSException wcsEx = null;

            	String exceptionMessage = parseException(data);
                if (exceptionMessage==null)
                {
                 	String error = new String(data);
                	int pos = error.indexOf("<?xml");
                	if (pos!= -1)
                	{
                		String xml = error.substring(pos,error.length());
                		exceptionMessage = parseException(xml.getBytes());
                	}
                    if (exceptionMessage == null)
                    	exceptionMessage = new String(data);

                }
             	wcsEx = new WCSException(exceptionMessage);
            	wcsEx.setWCSMessage(new String(data));

            	// Since it is an error file, It must be deleted from the cache
            	Utilities.removeURL(request);
                throw wcsEx;
            }
			return f;
		}
		catch(IOException e)
		{
			e.printStackTrace();
            throw new ServerErrorException();
		}
    }


    /**
     * Parses the WCS Exception document.
     * @param bytes, byte[]
     * @return
     */
    private String parseException(byte[] data) {
    	// TODO: açò està fusilat del WMS, comprovar que funciona.
    	ArrayList errors = new ArrayList();
        KXmlParser kxmlParser = new KXmlParser();
        try
        {
            kxmlParser.setInput(new ByteArrayInputStream(data), encoding);
            kxmlParser.nextTag();
            int tag;
            if ( kxmlParser.getEventType() != KXmlParser.END_DOCUMENT )
            {
                kxmlParser.require(KXmlParser.START_TAG, null, ExceptionTags.EXCEPTION_ROOT);
                tag = kxmlParser.nextTag();
                 while(tag != KXmlParser.END_DOCUMENT)
                 {
                     switch(tag)
                     {
                        case KXmlParser.START_TAG:
                            if (kxmlParser.getName().compareTo(ExceptionTags.SERVICE_EXCEPTION)==0){
                                String errorCode = kxmlParser.getAttributeValue("", ExceptionTags.CODE);
                                errorCode = (errorCode != null) ? "["+errorCode+"] " : "";
                                String errorMessage = kxmlParser.nextText();
                                errors.add(errorCode+errorMessage);
                            }
                            break;
                        case KXmlParser.END_TAG:
                            break;

                     }
                     tag = kxmlParser.nextTag();
                 }
                 //kxmlParser.require(KXmlParser.END_DOCUMENT, null, null);
            }
        }
        catch(XmlPullParserException parser_ex){
            parser_ex.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String message = errors.size()>0? "" : null;
        for (int i = 0; i < errors.size(); i++) {
            message += (String) errors.get(i)+"\n";
        }
        return message;
	}	

    /**
     * Builds the GetCapabilitiesRequest according to the OGC WCS Specifications
     * without a VERSION, to get the highest version than a WCS supports.
     */
    public static String buildCapabilitiesSuitableVersionRequest(String _host, String _version) {
		int index = _host.indexOf('?');
		
		if (index > -1) {
			String host = _host.substring(0, index + 1);
			String query = _host.substring(index + 1, _host.length());
			
			StringTokenizer tokens = new StringTokenizer(query, "&");
			String newQuery = "", token;

			// If there is a field or a value with spaces, (and then it's on different tokens) -> unify them
			while (tokens.hasMoreTokens()) {
				token = tokens.nextToken().trim();

				if (token.toUpperCase().compareTo("REQUEST=GETCAPABILITIES") == 0)
					continue;

				if (token.toUpperCase().compareTo("SERVICE=WCS") == 0)
					continue;

				if ((_version != null) && (_version.length() > 0)) {
    				if (token.toUpperCase().compareTo("VERSION=" + _version) == 0)
    					continue;
				}

				if (token.toUpperCase().compareTo("EXCEPTIONS=XML") == 0)
					continue;

				newQuery += token + "&";
			}

        	_host = host + newQuery;
		}
		else {
			_host += "?";
		}

    	if ((_version != null) && (_version.compareTo("") != 0))
    		_host += "REQUEST=GetCapabilities&SERVICE=WCS&VERSION=" + _version + "&EXCEPTIONS=XML";
    	else
    		_host += "REQUEST=GetCapabilities&SERVICE=WCS&EXCEPTIONS=XML";

    	return _host.replaceAll(" ", "%20");
    }

	public ArrayList getFormats() {
		return new ArrayList(serviceInfo.formats);
	}

    public class ServiceInformation {

        public String online_resource = null;
        public String version;
        public String name;
        public String scope;
        public String title;
        public String abstr;
        public String keywords;
        public String fees;
        public String operationsInfo;
        public String personname;
        public String organization;
        public String function;
        public String addresstype;
        public String address;
        public String place;
        public String province;
        public String postcode;
        public String country;
        public String phone;
        public String fax;
        public String email;
        public Vector formats;
        public HashMap operations; // operations that WCS supports

        public ServiceInformation()
        {
            version = new String();
            name = new String();
            scope = new String();
            title = new String();
            abstr = new String();
            keywords = new String();
            fees = new String();
            operationsInfo = new String();
            personname = new String();
            organization = new String();
            function = new String();
            addresstype = new String();
            address = new String();
            place = new String();
            province = new String();
            postcode = new String();
            country = new String();
            phone = new String();
            fax = new String();
            email = new String();
            operations = new HashMap();
        }

     }

	public Hashtable getLayers() {
		return layerPool;
	}
	
	public abstract WCSDescribeCoverageRequest createDescribeCoverageRequest(WCSStatus status);
	
	public abstract WCSGetCoverageRequest createGetCoverageRequest(WCSStatus status);
	
	
}
