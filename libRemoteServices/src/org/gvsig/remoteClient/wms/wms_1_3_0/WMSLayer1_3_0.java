
package org.gvsig.remoteClient.wms.wms_1_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wms.WMSDimension;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * <p>WMS Layer for WMS 1.3.0</p>
 * 
 */
public class WMSLayer1_3_0 extends org.gvsig.remoteClient.wms.WMSLayer {
    
    
    public ArrayList getDimensions()
    {   
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
    
    public WMSLayer1_3_0()
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
        
        readLayerAttributes( parser );
        
        currentTag = parser.nextTag();
        
        while (!end) 
        {
            switch(currentTag)
            {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.LAYER)==0)
                    {	
                        WMSLayer1_3_0 lyr = new WMSLayer1_3_0();						
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
                    else if (parser.getName().compareTo(CapabilitiesTags.CRS)==0)
                    {
                    	//TODO:
                    	//comentar esto y añadir solo los SRS o CRS que incluyan un extent...
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
                        value = parser.getAttributeValue("",CapabilitiesTags.CRS);
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
                        
                        //X and Y spatial resolution in the units if that CRS.
                        //value = parser.getAttributeValue("",CapabilitiesTags.RESX);
                        //if ((value != null) && (Utilities.isNumber(value)))
                            //bbox.setYmax(Double.parseDouble(value));
                        //value = parser.getAttributeValue("",CapabilitiesTags.RESY);
                        //if ((value != null) && (Utilities.isNumber(value)))
                            //bbox.setYmax(Double.parseDouble(value));
                        
                        addBBox(bbox);
                        addSrs(bbox.getSrs());
                    }	
                    else if (parser.getName().compareTo(CapabilitiesTags.EX_GEOGRAPHICBOUNDINGBOX)==0)
                    {
                    	//minimum bounding rectangle in decimal degrees of the area covered by the layer.
                        bbox = parseEXGeographicBBTag(parser);	
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
                        WMSStyle1_3_0 style = new WMSStyle1_3_0();
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
  
    
    /**
     * <p>Parses the EX_GeographicBoundingBox </p>
     */    
    private BoundaryBox parseEXGeographicBBTag(KXmlParser parser) throws IOException, XmlPullParserException 
    {
    	int currentTag;
    	boolean end = false;
    	BoundaryBox bbox = new BoundaryBox ();
    	String value;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.EX_GEOGRAPHICBOUNDINGBOX);
    	currentTag = parser.next();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.WESTBOUNDLONGITUDE)==0)
					{
						value = parser.nextText();	                       
	                    if ((value != null) && (Utilities.isNumber(value)))
	                    	bbox.setXmin(Double.parseDouble(value));
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.EASTBOUNDLONGITUDE)==0)
					{
						value = parser.nextText();	                       
	                    if ((value != null) && (Utilities.isNumber(value)))
	                    	bbox.setXmax(Double.parseDouble(value));
					}
					else if (parser.getName().compareTo(CapabilitiesTags.NORTHBOUNDLATITUDE)==0)
					{
						value = parser.nextText();	                       
	                    if ((value != null) && (Utilities.isNumber(value)))
	                    	bbox.setYmax(Double.parseDouble(value));
					}
					else if (parser.getName().compareTo(CapabilitiesTags.SOUTHBOUNDLATITUDE)==0)
					{
						value = parser.nextText();	                       
	                    if ((value != null) && (Utilities.isNumber(value)))
	                    	bbox.setYmin(Double.parseDouble(value));
					}					
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.EX_GEOGRAPHICBOUNDINGBOX) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
					break;
			 }
             if (!end)
                 currentTag = parser.next();
    	}    	
    	parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.EX_GEOGRAPHICBOUNDINGBOX);
    	
    	//TODO: 
    	bbox.setSrs("CRS:84");
    	return bbox;
    }
    
    public String toString(){
        return super.toString();
    }
}
