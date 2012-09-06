
package org.gvsig.remoteClient.wms.wms_1_1_0;

import java.io.IOException;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p>Represents the layer style defined by the OGC Specifications for WMS 1.1.0</p>
 * 
 */
public class WMSStyle1_1_0 extends org.gvsig.remoteClient.wms.WMSStyle {

/**
 * <p>URL pointing to the legend for a layer with this style</p>
 */

/**
 * <p>Parses the STYLE TAG according with the OGC Specifications for the WMS 1.1.1</p>
 */
    public void parse(KXmlParser parser) throws IOException, XmlPullParserException
    {        
    	int currentTag;
    	boolean end = false;
    	
    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.STYLE);
    	currentTag = parser.nextTag();
    	
    	while (!end) 
    	{
			 switch(currentTag)
			 {
				case KXmlParser.START_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.NAME)==0)
					{						
						setName(parser.nextText());						
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.TITLE)==0)
					{
						setTitle(parser.nextText());
					}
					else if (parser.getName().compareTo(CapabilitiesTags.ABSTRACT)==0)
					{
						setAbstract(parser.nextText());
					}	
					else if (parser.getName().compareTo(CapabilitiesTags.LEGENDURL)==0)
					break;
				case KXmlParser.END_TAG:
					if (parser.getName().compareTo(CapabilitiesTags.STYLE) == 0)
						end = true;
					break;
				case KXmlParser.TEXT:					
					break;
			 }
			 if (!end)
			 {
				 currentTag = parser.next();
			 }
    	}
    	parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.STYLE);
    } 
 }
