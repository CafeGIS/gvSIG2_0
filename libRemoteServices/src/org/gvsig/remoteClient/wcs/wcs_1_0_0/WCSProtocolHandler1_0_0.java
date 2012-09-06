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
package org.gvsig.remoteClient.wcs.wcs_1_0_0;

import java.io.File;
import java.io.IOException;

import org.gvsig.remoteClient.ogc.OGCServiceInformation;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.DescribeCoverageTags;
import org.gvsig.remoteClient.utils.EncodingXMLParser;
import org.gvsig.remoteClient.wcs.CoverageOfferingBrief;
import org.gvsig.remoteClient.wcs.WCSProtocolHandler;
import org.gvsig.remoteClient.wcs.WCSStatus;
import org.gvsig.remoteClient.wcs.request.WCSDescribeCoverageRequest;
import org.gvsig.remoteClient.wcs.request.WCSGetCoverageRequest;
import org.gvsig.remoteClient.wcs.wcs_1_0_0.request.WCSDescribeCoverageRequest1_0_0;
import org.gvsig.remoteClient.wcs.wcs_1_0_0.request.WCSGetCoverageRequest1_0_0;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author jaume
 *
 */
public class WCSProtocolHandler1_0_0 extends WCSProtocolHandler{

	public WCSProtocolHandler1_0_0() {
		this.version = "1.0.0";
		this.name = "WCS1.0.0";
		this.serviceInfo = new ServiceInformation();

	}

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.OGCProtocolHandler#parseCapabilities(java.io.File)
     */
    /**
     * <p>Parse the xml data retrieved from the WCS, it will parse the WCS Capabilities</p>
     *
     */
    public boolean parseCapabilities(File f)
    {
    	int tag;
    	EncodingXMLParser parser = null;
      	parser = new EncodingXMLParser();
    	try
    	{
    		parser.setInput(f);
    		parser.nextTag();

            if ( parser.getEventType() != KXmlParser.END_DOCUMENT )
            {
                parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.WCS_CAPABILITIES_ROOT1_0_0);
                tag = parser.nextTag();
                while(tag != KXmlParser.END_DOCUMENT)
                {
                    switch(tag)
                    {
                        case KXmlParser.START_TAG:
                            if (parser.getName().compareTo(CapabilitiesTags.SERVICE)==0)
                            {
                                parseServiceTag(parser);
                            }
                            else if (parser.getName().compareTo(CapabilitiesTags.CAPABILITY)==0)
                            {
                                parseCapabilityTag(parser);
                            }
                            else if (parser.getName().compareTo(CapabilitiesTags.WCS_CONTENTMETADATA)==0)
                            {
                            	parseContentMetadataTag(parser);
                            }
                            break;
                        case KXmlParser.END_TAG:
                            break;
                        case KXmlParser.TEXT:
                        	if (parser.getName()!=null)
                        		System.out.println("[TEXT]["+parser.getText().trim()+"]");
                            break;
                    }
                    tag = parser.next();
                }
                parser.require(KXmlParser.END_DOCUMENT, null, null);
            }
        }
        catch(XmlPullParserException parser_ex){
            System.out.println(parser_ex.getMessage());
            parser_ex.printStackTrace();
            return false;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.wcs.WCSProtocolHandler#parseDescribeCoverage(java.io.File)
     */
    public boolean parseDescribeCoverage(File f) {
    	int tag;
    	EncodingXMLParser parser = null;
      	parser = new EncodingXMLParser();
    	try
    	{
    		parser.setInput(f);
    		parser.nextTag();
            if (parser.getEventType() != KXmlParser.END_DOCUMENT){
                parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.COVERAGE_DESCRIPTION);
                tag = parser.nextTag();
                while (tag != KXmlParser.END_DOCUMENT){
                    switch (tag){
                        case KXmlParser.START_TAG:
                            if (parser.getName().compareTo(DescribeCoverageTags.COVERAGE_OFFERING)==0){
                                WCSCoverage1_0_0 lyr = new WCSCoverage1_0_0();
                                lyr.parse(parser);
                                if (lyr!=null){
                                    layerPool.put(lyr.getName(), lyr);
                                }
                            }
                            break;
                    }
                    tag = parser.next();
                }
                parser.require(KXmlParser.END_DOCUMENT, null, null);
            }
        } catch (XmlPullParserException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
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
                        //TODO Parse exception tags...
                    	parser.skipSubTree();
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
             currentTag = parser.next();
        }
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
                    if (parser.getName().compareToIgnoreCase(CapabilitiesTags.NAME)==0)
                    {
                        serviceInfo.name = parser.nextText();
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.WCS_LABEL)==0)
                    {
                        serviceInfo.title = parser.nextText();
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.WCS_DESCRIPTION)==0)
                    {
                        serviceInfo.abstr = parser.nextText();
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.WCS_KEYWORDS)==0)
                    {
                        parser.skipSubTree();
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.ACCESSCONSTRAINTS)==0)
                    {
                        parser.skipSubTree();
                    }
                    else if (parser.getName().compareTo(CapabilitiesTags.FEES)==0)
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
             currentTag = parser.next();
        }
//      parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.SERVICE);
    }

    /**
     * <p>Parses the Request tag </p>
     */
    private void parseRequestTag(KXmlParser parser) throws IOException, XmlPullParserException
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
    				currentTag = parser.nextTag();
    				parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DCPTYPE);
    				{
						currentTag = parser.nextTag();
						if(parser.getName().compareTo(CapabilitiesTags.HTTP)==0)
						{
							currentTag = parser.nextTag();
							if(parser.getName().compareTo(CapabilitiesTags.GET)==0)
							{
								currentTag = parser.nextTag();
								if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE)==0)
								{
									String value = new String();
									value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
									if (value != null){
										serviceInfo.operations.put(value, null);
									}
								}
							}
						}
					}

    			}
    			else if (parser.getName().compareTo(CapabilitiesTags.DESCRIBECOVERAGE)==0)
    			{
    				currentTag = parser.nextTag();
    				parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DCPTYPE);
    				{
						currentTag = parser.nextTag();
						if(parser.getName().compareTo(CapabilitiesTags.HTTP)==0)
						{
							currentTag = parser.nextTag();
							if(parser.getName().compareTo(CapabilitiesTags.GET)==0)
							{
								currentTag = parser.nextTag();
								if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE)==0)
								{
									String value = new String();
									value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
									if (value != null){
										serviceInfo.operations.put(value, null);
									}
								}
							}
						}
					}
    			}
    			else if (parser.getName().compareTo(CapabilitiesTags.GETCOVERAGE)==0)
    			{
    				currentTag = parser.nextTag();
    				parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.DCPTYPE);
    				{
						currentTag = parser.nextTag();
						if(parser.getName().compareTo(CapabilitiesTags.HTTP)==0)
						{
							currentTag = parser.nextTag();
							if(parser.getName().compareTo(CapabilitiesTags.GET)==0)
							{
								currentTag = parser.nextTag();
								if (parser.getName().compareTo(CapabilitiesTags.ONLINERESOURCE)==0)
								{
									String value = new String();
									value = parser.getAttributeValue("", CapabilitiesTags.XLINK_HREF);
									if (value != null){
										serviceInfo.operations.put(value, null);
									}
								}
							}
						}
					}
    			}
    			break;
    		case KXmlParser.END_TAG:
    			if (parser.getName().compareTo(CapabilitiesTags.REQUEST) == 0)
    				end = true;
    			break;
    		case KXmlParser.TEXT:
    			break;
    		}
    		currentTag = parser.next();
    	}
    	// TODO: does not get such a tag when arrives here!!!!!!
    	//parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.REQUEST);
    }

    private void parseContentMetadataTag(KXmlParser parser) throws XmlPullParserException, IOException {
    	int currentTag;
    	boolean end = false;

    	parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.WCS_CONTENTMETADATA);
    	currentTag = parser.next();

    	while (!end)
    	{
    		switch(currentTag)
    		{
    		case KXmlParser.START_TAG:
    			if (parser.getName().compareTo("wcs:"+CapabilitiesTags.WCS_COVERAGEOFFERINGBRIEF)==0) {
    				CoverageOfferingBrief cob = new CoverageOfferingBrief();
    				cob.parse(parser);
    				layerPool.put(cob.getName(), cob);
    			}
    			break;
    		case KXmlParser.END_TAG:
    			if (parser.getName().compareTo(CapabilitiesTags.WCS_CONTENTMETADATA) == 0)
    				end = true;
    			break;
    		case KXmlParser.TEXT:
    			break;
    		}
    		currentTag = parser.next();
    	}
    	// TODO: does not get such a tag when arrives here!!!!!!
    	//parser.require(KXmlParser.END_TAG, null, CapabilitiesTags.REQUEST);
    }

	public OGCServiceInformation getServiceInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wcs.WCSProtocolHandler#createDescribeCoverageRequest(org.gvsig.remoteClient.wcs.WCSStatus)
	 */
	public WCSDescribeCoverageRequest createDescribeCoverageRequest(
			WCSStatus status) {
		return new WCSDescribeCoverageRequest1_0_0(status, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.wcs.WCSProtocolHandler#createGetCoverageRequest(org.gvsig.remoteClient.wcs.WCSStatus)
	 */
	public WCSGetCoverageRequest createGetCoverageRequest(WCSStatus status) {
		return new WCSGetCoverageRequest1_0_0(status, this);
	}
}

