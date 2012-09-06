package org.gvsig.remoteClient.wms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.gvsig.remoteClient.exceptions.ServerErrorException;
import org.gvsig.remoteClient.exceptions.WMSException;
import org.gvsig.remoteClient.ogc.OGCProtocolHandler;
import org.gvsig.remoteClient.ogc.OGCServiceInformation;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.ExceptionTags;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wms.request.WMSGetFeatureInfoRequest;
import org.gvsig.remoteClient.wms.request.WMSGetLegendGraphicRequest;
import org.gvsig.remoteClient.wms.request.WMSGetMapRequest;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p> Abstract class that represents handlers to comunicate via WMS protocol.
 * </p>
 *
 */
public abstract class WMSProtocolHandler extends OGCProtocolHandler{
	/**
	 * Encoding used to parse different xml documents.
	 */
	protected String encoding = "UTF-8";
    /**
     * WMS metadata
     */
    protected WMSServiceInformation serviceInfo;
    public TreeMap layers;
    public WMSLayer rootLayer;

    /**
     * returns the alfanumeric information of the layers at the specified point.
     * the diference between the other getfeatureInfo method is that this will
     * be implemented by each specific version because the XML from the server will be
     * parsed and presented by a well known structure.
     */

    public String getName() {
    	return name;
    }

    /*
     * (non-Javadoc)
     * @see org.gvsig.remoteClient.ogc.OGCProtocolHandler#getServiceInformation()
     */
    public OGCServiceInformation getServiceInformation() {
        return serviceInfo;
    }

    /**
	 * <p>Builds a GetCapabilities request that is sent to the WMS
	 * the response will be parse to extract the data needed by the
	 * WMS client</p>
	 * @param override, if true the previous downloaded data will be overridden
	 */
    public void getCapabilities(WMSStatus status, boolean override, ICancellable cancel)
    {
    	URL request = null;
		try
		{
			request = new URL(buildCapabilitiesRequest(status));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			if (override)
				Utilities.removeURL(request);
			File f = Utilities.downloadFile(request,"wms_capabilities.xml", cancel);
			if (f == null)
				return;
			clear();
			parseCapabilities(f);
	    } catch(Exception e)
		{
			//TODO
			e.printStackTrace();
		}
    }

    private void clear() {
		layers.clear();
		serviceInfo.clear();
	}

	/**
     * <p>It will send a GetFeatureInfo request to the WMS
     * Parsing the response and redirecting the info to the WMS client</p>
     * TODO: return a stored file instead a String.
     */
    public String getFeatureInfo(WMSStatus status, int x, int y, int featureCount, ICancellable cancel)
    {
    	StringBuffer output = new StringBuffer();
    	String outputFormat = new String();
    	String ServiceException = "ServiceExceptionReport";
    	StringBuffer sb = new StringBuffer();
    	sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		try
		{
			WMSGetFeatureInfoRequest request = createGetFeatureInfoRequest(status, x, y);
			URL url = request.getURL();
	    	outputFormat = url.openConnection().getContentType();
	    	File f = request.sendRequest();
			if (f == null){
				return "";
			}

			FileReader fReader = new FileReader(f);
			char[] buffer = new char[1024*256];
			for (int i = fReader.read(buffer); i>0; i = fReader.read(buffer))
		    {
		    	String str = new String(buffer,0,i);
		    	output.append(str);
		    }
		    if ( (outputFormat == null) || (outputFormat.indexOf("xml") != -1)
		    		||output.toString().toLowerCase().startsWith("<?xml")
		    		||(outputFormat.indexOf("gml") != -1))
		    {
		    	int tag;
		    	KXmlParser kxmlParser = null;
		    	kxmlParser = new KXmlParser();
		    	//kxmlParser.setInput(new StringReader(output.toString()));
		    	kxmlParser.setInput(new FileReader(f));

		    	tag = kxmlParser.nextTag();
		    	if (kxmlParser.getName().compareTo(ServiceException)==0)
				{
		    		sb.append("<INFO>").append(parseException( output.toString().getBytes())).append("</INFO>");
		    		return sb.toString();
				}
				else if (kxmlParser.getName().compareToIgnoreCase("ERROR")==0)
				{
					return output.toString();
				}
				else
				{
					return output.toString();
				}
	    	}
	    	else
	    	{  		
	    		//Para que funcione con el GetFeatureInfo Viewer generico hay que devolver:
	    		 return output.toString();
	    	}
		}
    	catch(XmlPullParserException parserEx)
    	{
    		if (output.toString().toLowerCase().indexOf("xml") != -1)
    		{
    			return output.toString().trim();
    		}
    		else
    		{
    	   		sb.append("<INFO>").append("Info format not supported").append("</INFO>");
        		return sb.toString();
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		sb.append("<INFO>").append("Info format not supported").append("</INFO>");
    		return sb.toString();

    	}
    }
    /**
     * <p>Builds a GetMap request that is sent to the WMS
     * the response (image) will be redirect to the
     * WMS client</p>
     */
    public byte[] _getMap(WMSStatus status) throws ServerErrorException, WMSException
    {
    	try
		{
			//TODO:
			//pass this buildXXXRequest to the WMSProtocolHandlerXXX: The request can depend on the WMS version.
			WMSGetMapRequest request = createGetMapRequest(status);
			URL url = request.getURL();
			
			URLConnection conn = url.openConnection();
			System.out.println(request.toString());
            String type = conn.getContentType();


	    	byte[] imageBytes = null;
	    	byte[] buffer = new byte[1024*256];
            InputStream is = conn.getInputStream();
	    	int readed = 0;

	    	for (int i = is.read(buffer); i>0; i = is.read(buffer)){
                // Creates a new buffer to contain the previous readed bytes and the next bunch of bytes
	    		byte[] buffered = new byte[readed+i];
	    		for (int j = 0; j < buffered.length; j++) {
	    			if (j<readed){
                        // puts the previously downloaded bytes into the image buffer
	    				buffered[j] = imageBytes[j];
	    			}
	    			else {
                        // appends the recently downloaded bytes to the image buffer.
	    				buffered[j] = buffer[j-readed];
	    			}
				}
	    		imageBytes = (byte[]) buffered.clone();
	    		readed += i;
	    	}

	    	if ((type !=null && !type.subSequence(0,5).equals("image"))
	    		||(Utilities.isTextData(imageBytes)))
	    	{
               	WMSException wmsEx = null;

            	String exceptionMessage = parseException(imageBytes);
                if (exceptionMessage==null)
                {
                 	String error = new String(imageBytes);
                	int pos = error.indexOf("<?xml");
                	if (pos!= -1)
                	{
                		String xml = error.substring(pos,error.length());
                		exceptionMessage = parseException(xml.getBytes());
                        if (exceptionMessage == null)
                        	exceptionMessage = new String(imageBytes);
                	}
                }
             	wmsEx = new WMSException(exceptionMessage);
            	wmsEx.setWMSMessage(new String(imageBytes));
                throw wmsEx;
            }
			return imageBytes;
		}
		catch(IOException e)
		{
			e.printStackTrace();
            throw new ServerErrorException();
		}
    }

    public File getLegendGraphic(WMSStatus status, String layerName, ICancellable cancel) throws ServerErrorException, WMSException
    {
    	try
		{
			WMSGetLegendGraphicRequest request = createGetLegendGraphicRequest(status, layerName);
			File f = request.sendRequest();
	    	if (f== null)
	    		return null;
            if (Utilities.isTextFile(f)) {
	    		FileInputStream fis = new FileInputStream(f);
	    		FileChannel fc = fis.getChannel();
	    		byte[] data = new byte[(int)fc.size()];
	    		ByteBuffer bb = ByteBuffer.wrap(data);
	    		fc.read(bb);

	    		WMSException wmsEx = null;

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
             	wmsEx = new WMSException(exceptionMessage);
            	wmsEx.setWMSMessage(new String(data));
            	Utilities.removeURL(request);
                throw wmsEx;
            }
			return f;
		}
		catch(IOException e)
		{
			e.printStackTrace();
            throw new ServerErrorException();
		}
    }

    public File getMap(WMSStatus status, ICancellable cancel) throws ServerErrorException, WMSException
    {
    	try
		{
			WMSGetMapRequest request = createGetMapRequest(status);
			File f = request.sendRequest();
			
			if (f== null)
	    		return null;
            if (Utilities.isTextFile(f)) {
	    		FileInputStream fis = new FileInputStream(f);
	    		FileChannel fc = fis.getChannel();
	    		byte[] data = new byte[(int)fc.size()];   // fc.size returns the size of the file which backs the channel
	    		ByteBuffer bb = ByteBuffer.wrap(data);
	    		fc.read(bb);

	    		WMSException wmsEx = null;

            	String exceptionMessage = parseException(data);
                if (exceptionMessage==null)
                {
                 	String error = new String(data);
                	int pos = error.indexOf("<?xml");
                	if (pos!= -1)
                	{
                		String xml = error.substring(pos,error.length());
                		exceptionMessage = parseException(xml.getBytes());
//                        if (exceptionMessage == null)
//                        	exceptionMessage = new String(data);
                	}
                    if (exceptionMessage == null)
                    	exceptionMessage = new String(data);

                }
             	wmsEx = new WMSException(exceptionMessage);
            	wmsEx.setWMSMessage(new String(data));

            	// Since it is an error file, It must be deleted from the cache
            	Utilities.removeURL(request);
                throw wmsEx;
            }
			return f;
		}
		catch(IOException e)
		{
			e.printStackTrace();
            throw new ServerErrorException();
		}
    }


    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.wms.WMSProtocolHandler#parseException(byte[])
     */
    protected String parseException(byte[] data) {
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
     * Builds the GetCapabilitiesRequest according to the OGC WMS Specifications
     * without a VERSION, to get the highest version than a WMS supports.
     */
    public static String buildCapabilitiesSuitableVersionRequest(String _host, String _version)
    {
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

				if (token.toUpperCase().compareTo("SERVICE=WMS") == 0)
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
    		_host += "REQUEST=GetCapabilities&SERVICE=WMS&VERSION=" + _version + "&EXCEPTIONS=XML";
    	else
    		_host += "REQUEST=GetCapabilities&SERVICE=WMS&EXCEPTIONS=XML";

    	return _host;
    }

    /**
     * Builds the GetCapabilitiesRequest according to the OGC WMS Specifications
     * @param WMSStatus
     */
    private String buildCapabilitiesRequest(WMSStatus status)
    {
		StringBuffer req = new StringBuffer();
		String symbol = null;

		String onlineResource;
		if (status == null || status.getOnlineResource() == null)
			onlineResource = getHost();
		else
			onlineResource = status.getOnlineResource();
		symbol = getSymbol(onlineResource);

		req.append(onlineResource).append(symbol).append("REQUEST=GetCapabilities&SERVICE=WMS&");
		req.append("VERSION=").append(getVersion()).append("&EXCEPTIONS=XML");
		return req.toString();
    }  
   
     public void close() {
        // your code here
    } 
     
     /**
 	 * @param status
 	 * The WMS status
 	 * @param protocolHandler
 	 * The handler to parse the requests
 	 * @return an object to send the GetMap requests
 	 */
 	protected abstract WMSGetMapRequest createGetMapRequest(WMSStatus status);
 	
 	protected abstract WMSGetFeatureInfoRequest createGetFeatureInfoRequest(WMSStatus status, int x, int y);
 	
 	protected abstract WMSGetLegendGraphicRequest createGetLegendGraphicRequest(WMSStatus status, String layerName);

 	
 	/**
     * <p>Parses the Request tag </p>
     */ 
    protected void parseRequestTag(KXmlParser parser) throws IOException, XmlPullParserException
    {	
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.REQUEST);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.GETCAPABILITIES)==0)
					{
						parserDcpType(parser, CapabilitiesTags.GETCAPABILITIES);
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.GETMAP)==0)
					{	
						parseGetMapTag(parser);						
					}
					else if (parser.getName().compareTo(CapabilitiesTags.GETFEATUREINFO)==0)
					{
						parseGetFeatureInfoTag(parser);
					}		
					else if (parser.getName().compareTo(CapabilitiesTags.DESCRIBELAYER)==0)
					{
						parserDcpType(parser, CapabilitiesTags.DESCRIBELAYER);
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.GETLEGENDGRAPHIC)==0)
					{
						parseGetLegendGraphicTag(parser);
					}					
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.REQUEST) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
				break;
			 }
			 if(!end)
				 currentTag = parser.next();
    	}
    	// TODO: does not get such a tag when arrives here!!!!!!
    	//parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.REQUEST);    	
    }   
 	 /**
     * <p>Parses the GetMap tag </p>
     */ 
    protected void parseGetMapTag(KXmlParser parser) throws IOException, XmlPullParserException
    {	
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.GETMAP);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.FORMAT)==0)
					{
						serviceInfo.formats.add(parser.nextText());
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.DCPTYPE)==0)
					{		
						parserDcpType(parser, CapabilitiesTags.GETMAP);						
					}			
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.GETMAP) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
				break;
			 }
			 if(!end)
				 currentTag = parser.next();
    	}	
    }    
    
    /**
     * <p>Parses the GetFeatureInfo tag </p>
     */ 
    protected void parseGetFeatureInfoTag(KXmlParser parser) throws IOException, XmlPullParserException
    {	
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.GETFEATUREINFO);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.FORMAT)==0)
					{
						//TODO:
						// add the supported formats by the GetFeatureInfo request
						//serviceInfo.formats.add(parser.nextText());
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.DCPTYPE)==0)
					{			
						parserDcpType(parser, CapabilitiesTags.GETFEATUREINFO);		
					}			
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.GETFEATUREINFO) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
				break;
			 }
			 if(!end)
				 currentTag = parser.next();
    	}	
    }     
 
    /**
     * <p>Parses the GetLegendGraphic tag </p>
     */ 
    protected void parseGetLegendGraphicTag(KXmlParser parser) throws IOException, XmlPullParserException
    {	
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.GETLEGENDGRAPHIC);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.FORMAT)==0)
					{
						//TODO:
						// add the supported formats by the GetLegendGraphic request
						//serviceInfo.formats.add(parser.nextText());
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.DCPTYPE)==0)
					{			
						parserDcpType(parser, CapabilitiesTags.GETLEGENDGRAPHIC);		
					}			
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.GETLEGENDGRAPHIC) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
				break;
			 }
			 if(!end)
				 currentTag = parser.next();
    	}	
    }
 }
