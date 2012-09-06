package org.gvsig.remoteClient.wms;

import java.io.IOException;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

public class WMSExtent {
	    
    private String name; 
    /**
     * Indicates that the server will round off inexact dimension values
     * to the nearest valid value, or (if it is null or zero) it will not.
     */
    private String nearestValue; 
    /**
     * Indicates that temporal data are normally kept current and that the
     * request parameter TIME <b>may</b> include the keyword 'current' 
     * instead of an ending value. 
     */
    private String current;
    
    /**
     * cotains the expression for this dimension's extent.
     */
    private String extentExpression;
    private String extDefaultValue;

    public String getName() {        
        return name;
    } 
    
    /**
     * Tells that the temporal data are normally kept current and that
     * the request parameter TIME may include the keyword 'current'
     * instead of an ending value.
     *
     * @return <b>true</b> if the server does keep the data, <b>false</b> else.
     */
    public boolean allowsCurrentTime() {
        return (current!=null && !current.equals("0"));
    }
    
    /**
     * Gets the value that would be used along this dimension if a Web
     * request omits a value for the dimension.
     * 
     * @return Returns the defaultValue.
     */
    public String getDefaultValue() {
        return extDefaultValue;
    }
    
    /**
     * Returns the extent expression as it was written in the Capabilities 
     * document.
     * @return String
     */
    public String getExtentExpression() {
        return extentExpression;
    }
       

    /**
     * @return Returns the nearestValues.
     */
    public boolean allowsNearestValue() {
        return (nearestValue!=null && !nearestValue.equals("0"));
    }	 

	   /**
	 * Parses the EXTENT tag in the WMS capabilities, filling the Extend fills of the
	 * WMSDimension object and loading the data into memory to be easily accesed.
	 */
	public void parse(KXmlParser parser) throws IOException, XmlPullParserException{
	    parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.EXTENT);
	    name			 = parser.getAttributeValue("", CapabilitiesTags.DIMENSION_NAME);
	    extDefaultValue  = parser.getAttributeValue("", CapabilitiesTags.DEFAULT);
	    nearestValue    = parser.getAttributeValue("", CapabilitiesTags.EXTENT_NEAREST_VALUE);
	    current          = parser.getAttributeValue("", CapabilitiesTags.EXTENT_CURRENT);
	    extentExpression = parser.nextText();
	}	
}
