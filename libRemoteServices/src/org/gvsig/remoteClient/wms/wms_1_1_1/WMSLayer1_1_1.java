
package org.gvsig.remoteClient.wms.wms_1_1_1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wms.WMSDimension;
import org.gvsig.remoteClient.wms.WMSExtent;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * <p>WMS Layer for WMS 1.1.1</p>
 * 
 */
public class WMSLayer1_1_1 extends org.gvsig.remoteClient.wms.WMSLayer {
    
	 /**
     * <p>Extents defined for the layer in the capabilities doc</p>
     */
    private java.util.ArrayList extents = new ArrayList();
    
    /**
     * <p> gets the extent vector defined in this layer</p>
     * @return 
     */
    public ArrayList getExtents() {        
        return extents;
    } 
    
    public WMSExtent getExtent(String name)
    {
    	for(int i = 0; i < extents.size(); i++ ){
    		if(((WMSExtent)extents.get(i)).getName().compareTo(name)==0)
    		{
    			return (WMSExtent)extents.get(i);
    		}
    	}
    	return null;
    }
    
    public ArrayList getDimensions()
    {   
        WMSDimension dimension;
        WMSExtent extent;
    	for(int i = 0; i < dimensions.size(); i++)
    	{
    		dimension = (WMSDimension)dimensions.get(i);
    		extent = getExtent(dimension.getName()); 
    		if(extent != null)
    		{    			
    			((WMSDimension)dimensions.get(i)).setDimensionExpression( extent.getExtentExpression());    			
    		}
    	}    	
    	
        WMSDimension pDimension;
        WMSDimension myDimension;    
        ArrayList myDimensions = (ArrayList) this.dimensions.clone();        
        ArrayList pDimensions;        
        if (parent!=null)
        {
        	pDimensions = parent.getDimensions();
        	for (int i= 0; i < pDimensions.size(); i++){
        		pDimension = (WMSDimension)pDimensions.get(i);
        		myDimension = getDimension(pDimension.getName());
        		if (myDimension != null){
        			pDimensions.remove(pDimension);
        		}
        	}
        	myDimensions.addAll(pDimensions);
        }
        return myDimensions;
    }
    
    /**
     * <p>Adds an extent to the extent vector </p>
     * @param extent 
     */
    public void addExtent(org.gvsig.remoteClient.wms.WMSExtent extent) {        
        extents.add(extent);
    }   
    
    public WMSLayer1_1_1()
    {
        children = new ArrayList();
    }
    /**
     * <p>Parses the contents of the parser(WMSCapabilities)
     * to extract the information about an WMSLayer</p>
     * 
     */
    public void parse(KXmlParser parser, TreeMap layerTreeMap)
    throws IOException, XmlPullParserException
    {
        int currentTag;
        boolean end = false;
        String value;
        BoundaryBox bbox;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.LAYER);
       
        readLayerAttributes(parser);
        currentTag = parser.nextTag();
        
        while (!end) 
        {
            switch(currentTag)
            {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.LAYER)==0)
                    {	
                        WMSLayer1_1_1 lyr = new WMSLayer1_1_1();						
                        //parser.next(); 
                        lyr.parse(parser, layerTreeMap);
                        lyr.setParent(this);                       
                        this.children.add(lyr);
                        // Jaume
                        if (lyr.getName()!=null)
                            layerTreeMap.put(lyr.getName(), lyr);
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.ATTRIBUTION)==0){
                        // TODO comprobar que esto se necesite o se deseche
                        parser.skipSubTree();
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.NAME)==0)
                    {		
                        value = parser.nextText();
                        if (value != null) setName(value);						
                    }	
                    else if (parser.getName().compareTo(CapabilitiesTags.TITLE)==0)
                    {
                        value = parser.nextText();
                        if (value != null) setTitle(value);
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.ABSTRACT)==0)
                    {
                        value = parser.nextText();
                        if (value != null) setAbstract(value);
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.SRS)==0)
                    {
                        value = parser.nextText();
                        if (value != null){
                            String[] mySRSs = value.split(" ");
                            for (int i = 0; i < mySRSs.length; i++) {
                                addSrs(mySRSs[i]);    
                            }                            
                        }
                    }					
                    else if (parser.getName().compareTo(CapabilitiesTags.BOUNDINGBOX)==0)
                    {
                        bbox = new BoundaryBox();
                        value = parser.getAttributeValue("",CapabilitiesTags.SRS);
                        if (value != null)
                            bbox.setSrs(value);
                        value = parser.getAttributeValue("",CapabilitiesTags.MINX);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setXmin(Double.parseDouble(value));	
                        value = parser.getAttributeValue("",CapabilitiesTags.MINY);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setYmin(Double.parseDouble(value));	
                        value = parser.getAttributeValue("",CapabilitiesTags.MAXX);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setXmax(Double.parseDouble(value));	
                        value = parser.getAttributeValue("",CapabilitiesTags.MAXY);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setYmax(Double.parseDouble(value));	
                        addBBox(bbox);
                        addSrs(bbox.getSrs());
                    }	
                    else if (parser.getName().compareTo(CapabilitiesTags.LATLONBOUNDINGBOX)==0)
                    {
                        bbox = new BoundaryBox();
                        bbox.setSrs(CapabilitiesTags.EPSG_4326);
                        value = parser.getAttributeValue("",CapabilitiesTags.MINX);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setXmin(Double.parseDouble(value));	
                        value = parser.getAttributeValue("",CapabilitiesTags.MINY);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setYmin(Double.parseDouble(value));	
                        value = parser.getAttributeValue("",CapabilitiesTags.MAXX);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setXmax(Double.parseDouble(value));	
                        value = parser.getAttributeValue("",CapabilitiesTags.MAXY);
                        if ((value != null) && (Utilities.isNumber(value)))
                            bbox.setYmax(Double.parseDouble(value));	
                        addBBox(bbox);
                        setLatLonBox(bbox);
                        addSrs(bbox.getSrs());
                    }						
                    else if (parser.getName().compareTo(CapabilitiesTags.SCALEHINT)==0)
                    {
                        value = parser.getAttributeValue("",CapabilitiesTags.MIN);
                        if ((value != null) && (Utilities.isNumber(value)))
                            setScaleMin(Double.parseDouble(value));
                        value = parser.getAttributeValue("",CapabilitiesTags.MAX);
                        if ((value != null) && (Utilities.isNumber(value)))
                            setScaleMax(Double.parseDouble(value));																	
                    }						
                    else if (parser.getName().compareTo(CapabilitiesTags.STYLE)==0)
                    {
                        WMSStyle1_1_1 style = new WMSStyle1_1_1();
                        style.parse(parser);
                        if ((style != null) && (style.getName() != null))
                        {
                            styles.add(style);
                        }
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.DIMENSION)==0)
                    {
                        WMSDimension dim = new WMSDimension();
                        dim.parse(parser);
                        if ((dim != null) && (dim.getName() != null))
                        {
                            addDimension(dim);
                        }
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.EXTENT)==0)
                    {                    	
                        WMSExtent extent = new WMSExtent();
                        extent.parse(parser);
                        if ((extent != null) && (extent.getName() != null))
                        {
                            addExtent(extent);
                            
                        }
                    }                      
                    else if (parser.getName().compareTo(CapabilitiesTags.KEYWORDLIST)==0)
                    {
                    	parseKeywordList(parser);
                    }                    
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.LAYER) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:					
                    break;
            }
            if (!end)
            	currentTag = parser.next();
        }
        parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.LAYER);
    }      
    
    public String toString(){
        return super.toString();
    }
}
