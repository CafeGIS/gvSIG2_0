
package org.gvsig.remoteClient.wms.wms_1_3_0;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.EncodingXMLParser;
import org.gvsig.remoteClient.utils.ExceptionTags;
import org.gvsig.remoteClient.wms.WMSServiceInformation;
import org.gvsig.remoteClient.wms.WMSStatus;
import org.gvsig.remoteClient.wms.request.WMSGetFeatureInfoRequest;
import org.gvsig.remoteClient.wms.request.WMSGetLegendGraphicRequest;
import org.gvsig.remoteClient.wms.request.WMSGetMapRequest;
import org.gvsig.remoteClient.wms.wms_1_3_0.request.WMSGetFeatureInfoRequest1_1_3;
import org.gvsig.remoteClient.wms.wms_1_3_0.request.WMSGetLegendGraphicRequest1_1_3;
import org.gvsig.remoteClient.wms.wms_1_3_0.request.WMSGetMapRequest1_1_3;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p>
 * Describes the handler to comunicate to a WMS 1.3.0
 * </p>
 */
public class WMSProtocolHandler1_3_0 extends org.gvsig.remoteClient.wms.WMSProtocolHandler {
	private WMSLayer1_3_0 fakeRootLayer;
    
	public WMSProtocolHandler1_3_0()
	{
		this.version = "1.3.0";
		this.name = "WMS1.3.0";
		this.serviceInfo = new WMSServiceInformation(); 
		this.layers = new TreeMap();		   
	}
    
//------------------------------------------------------------------------------
// Parsing methods....    
//------------------------------------------------------------------------------    

    public boolean parseCapabilities(File f)
    {   
    	rootLayer = null;
    	
    	int tag;
      	EncodingXMLParser kxmlParser = null;
    	kxmlParser = new EncodingXMLParser();
    	try
    	{
    	   	kxmlParser.setInput(f);
			kxmlParser.nextTag();
    		if ( kxmlParser.getEventType() != KXmlParser.END_DOCUMENT ) 
    		{    		
    			kxmlParser.require(KXmlParser.START_TAG, null, CapabilitiesTags.CAPABILITIES_ROOT1_3_0);    			
    			tag = kxmlParser.nextTag();
				 while(tag != KXmlParser.END_DOCUMENT)
				 {
                     switch(tag)
					 {                       
						case KXmlParser.START_TAG:
							if (kxmlParser.getName().compareTo(CapabilitiesTags.SERVICE )==0)
							{
								parseServiceTag(kxmlParser);
							}	
							else if (kxmlParser.getName().compareTo(CapabilitiesTags.CAPABILITY)==0)
							{
								parseCapabilityTag(kxmlParser);
							}
							break;
						case KXmlParser.END_TAG:							
							break;
						case KXmlParser.TEXT:
							//System.out.println("[TEXT]["+kxmlParser.getText()+"]");							
						break;
					 }
    				 tag = kxmlParser.next();
    			 }//while !END_DOCUMENT
    		}
    	}
    	catch(XmlPullParserException parser_ex){    		
    		parser_ex.printStackTrace();
    		return false;
    	}
   		catch (IOException ioe) {			
   			ioe.printStackTrace();
   			return false;
 		} finally {
            return true;
        }
   		// In the parsing process the layer has been filled  		
    } 
    
    /**
     * <p>Parses the Service Information </p>
     */    
    private void parseServiceTag(KXmlParser parser) throws IOException, XmlPullParserException 
    {
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.SERVICE);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.NAME)==0)
					{
						serviceInfo.name = parser.nextText(); 
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.TITLE)==0)
					{
						serviceInfo.title = parser.nextText(); 
					}
					else if (parser.getName().compareTo(CapabilitiesTags.ABSTRACT)==0)
					{
						serviceInfo.abstr = parser.nextText(); 
					}
					else if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE)==0)
					{
				    	String value = new String();
				        value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
				        if (value != null){
				        	serviceInfo.online_resource = value;
				        }
					}					
					else if ((parser.getName().compareTo(CapabilitiesTags.KEYWORDLIST)==0) ||
							(parser.getName().compareTo(CapabilitiesTags.CONTACTINFORMATION)==0))
					{
						parser.skipSubTree();
					}					
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.SERVICE) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
				break;
			 }
             if (!end)
                 currentTag = parser.next();
    	}
    	parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.SERVICE);
    }
    
    /**
     * <p>Parses the Capability Tag </p>
     */    
    private void parseCapabilityTag(KXmlParser parser) throws IOException, XmlPullParserException
    { 	
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.CAPABILITY);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.REQUEST)==0)
					{
						parseRequestTag(parser); 
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.EXCEPTION)==0)
					{
						//TODO:
						//Add to serviceInformation the supported exception formats.
					}
					else if (parser.getName().compareTo(CapabilitiesTags.LAYER)==0)
					{
						WMSLayer1_3_0 lyr = new WMSLayer1_3_0();
                        if (rootLayer == null)
                            rootLayer = lyr;
                        else {
                            // Handles when there is no general root layer, will use
                            // a fake non-queryable one.
                            if (!rootLayer.equals(getFakeRootLayer())){
                                WMSLayer1_3_0 aux = (WMSLayer1_3_0) rootLayer;
                                rootLayer  = getFakeRootLayer();
                                rootLayer.getChildren().add(aux);
                            }
                            rootLayer.getChildren().add(lyr);
                        }
						lyr.parse(parser, layers);
						
                        if (lyr.getName()!=null)
						    layers.put(lyr.getName(), lyr); 											
					}
					else if ((parser.getName().compareTo(CapabilitiesTags.VENDORSPECIFICCAPABILITIES)==0) ||
							(parser.getName().compareTo(CapabilitiesTags.USERDEFINEDSYMBOLIZATION )==0))                            
					{
						parser.skipSubTree();
					}					
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.CAPABILITY) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
				break;
			 }
			 if (!end)
				 currentTag = parser.next();
    	}
    	//parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.CAPABILITY);    	
    }  
    
    private WMSLayer1_3_0 getFakeRootLayer(){
        if (fakeRootLayer == null){
            fakeRootLayer = new WMSLayer1_3_0();
            fakeRootLayer.setTitle(serviceInfo.title);
            fakeRootLayer.setQueryable(false);
            fakeRootLayer.setName(null);
        }
        return fakeRootLayer;
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
            System.out.println(parser_ex.getMessage());
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
    
    /*
     * (non-Javadoc)
     * @see org.gvsig.remoteClient.wms.WMSProtocolHandler#createGetFeatureInfoRequest(org.gvsig.remoteClient.wms.WMSStatus, int, int)
     */
	protected WMSGetFeatureInfoRequest createGetFeatureInfoRequest(
			WMSStatus status, int x, int y) {
		return new WMSGetFeatureInfoRequest1_1_3(status, this, x, y);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wms.WMSProtocolHandler#createGetMapRequest(org.gvsig.remoteClient.wms.WMSStatus)
	 */
	protected WMSGetMapRequest createGetMapRequest(WMSStatus status) {
		return new WMSGetMapRequest1_1_3(status, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wms.WMSProtocolHandler#createGetLegendGraphicRequest(org.gvsig.remoteClient.wms.WMSStatus, java.lang.String)
	 */
	protected WMSGetLegendGraphicRequest createGetLegendGraphicRequest(
			WMSStatus status, String layerName) {
		return new WMSGetLegendGraphicRequest1_1_3(status, this, layerName);
	}
  }
