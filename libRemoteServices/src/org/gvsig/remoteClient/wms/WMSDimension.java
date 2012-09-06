
package org.gvsig.remoteClient.wms;

import java.io.IOException;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p></p>
 * 
 */
public class WMSDimension {
    
    private String name;
    private String units;
    private String unitSymbol;
    /**
     * Declares what value would be used along this dimension if a Web
     * request omits a value for this dimension.
     */
    private String dimDefaultValue;
    /**
     * Indicates that the request may include multiple values or
     * (if it is null or zero) have to include <b>only<b/> single 
     * value for this dimension. 
     */
    private String multipleValues;
    /**
     * Indicates that the server will round off inexact dimension values
     * to the nearest valid value, or (if it is null or zero) it will not.
     */
    private String nearestValues;
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

    private String dimensionExpression;
   
    public String getName() {        
        return name;
    } 
 
    public String getUnits() {        
        return units;
    }
  
    public String getUnitSymbol() {        
        return unitSymbol;
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
    public String getDimDefaultValue() {
        return dimDefaultValue;
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
     * @return Returns the multipleValues.
     */
    public boolean allowsMultipleValues() {
        return (multipleValues!=null && !multipleValues.equals("0"));
    }   
    
    
    /**
     * @return Returns the dimensionExpression.
     */
    public String getDimensionExpression() {
        return dimensionExpression;
    }
    
    public void setDimensionExpression(String exp) {
        dimensionExpression = exp;
    }

    /**
     * @return Returns the nearestValues.
     */
    public boolean allowsNearestValues() {
        return (nearestValues!=null && !nearestValues.equals("0"));
    }
    

    /**
     * Parses the DIMENSION tag in the WMS capabilities, filling the WMSDimension object
     * and loading the data into memory to be easily accesed
     */
    public void parse(KXmlParser parser) throws IOException, XmlPullParserException{
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DIMENSION);
        name                = parser.getAttributeValue("", CapabilitiesTags.DIMENSION_NAME);
        units               = parser.getAttributeValue("", CapabilitiesTags.DIMENSION_UNITS);
        unitSymbol          = parser.getAttributeValue("", CapabilitiesTags.DIMENSION_UNIT_SYMBOL);
        dimDefaultValue     = parser.getAttributeValue("", CapabilitiesTags.DEFAULT);
        dimensionExpression = parser.nextText(); 
    }
    
}
