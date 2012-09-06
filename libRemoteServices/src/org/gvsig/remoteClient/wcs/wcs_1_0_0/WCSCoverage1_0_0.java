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
/**
 *
 */
package org.gvsig.remoteClient.wcs.wcs_1_0_0;


import java.io.IOException;

import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.utils.DescribeCoverageTags;
import org.gvsig.remoteClient.wcs.WCSCoverage;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author jaume
 *
 */
public class WCSCoverage1_0_0 extends WCSCoverage{
	String axisDescriptionName;

	/* (non-Javadoc)
     * @see org.gvsig.remoteClient.wcs.WCSLayer#parse(org.kxml2.io.KXmlParser)
     */
    public void parse(KXmlParser parser) throws XmlPullParserException, IOException {
        int currentTag;
        String value;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.COVERAGE_OFFERING);
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.NAME)==0)
                    {
                        value = parser.nextText();
                        if (value != null) setName(value);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.LABEL)==0){
                        value = parser.nextText();
                        if (value != null) setTitle(value);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.LONLAT_ENVELOPE)==0){
                    	BoundaryBox bBox = parseLonLatGMLEnvelope(parser);
                        bBox.setSrs(DescribeCoverageTags.WGS84);
                        addBBox(bBox);
                        setLonLatBox(bBox);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.DOMAINSET) == 0) {
                    	parseDomainSet(parser);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.RANGESET) == 0) {
                    	parseRangeSet(parser);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.SUPPORTED_CRSS) == 0) {
                    	parseSupportedCRSS(parser);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.SUPPORTED_FORMATS) == 0) {
                    	setNativeFormat(parser.getAttributeValue("", DescribeCoverageTags.NATIVE_FORMAT));
                    	parser.nextTag();
                    	parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.FORMATS);
                    	while (parser.getName().compareTo(DescribeCoverageTags.FORMATS) == 0) {
                    		String[] formats = parser.nextText().split(SEPARATOR);
                    		for (int i = 0; i < formats.length; i++) {
                    			addFormat(formats[i]);
                    		}
                    		parser.nextTag();
                    	}
                    } else if (parser.getName().compareTo(DescribeCoverageTags.SUPPORTED_INTERPOLATIONS) == 0) {
                    	boolean bInterpolationsEnd = false;
                    	int interpolationsTag = parser.next();
                    	while (!bInterpolationsEnd) {
                    		switch(interpolationsTag) {
	                    		case KXmlParser.START_TAG:
	                    			if (parser.getName().compareTo(DescribeCoverageTags.INTERPOLATION_METHOD)==0) {
	                    				addInterpolationMethod(parser.nextText());
	                    			}
	                    			break;
	                    		case KXmlParser.END_TAG:
	                    			if (parser.getName().compareTo(DescribeCoverageTags.SUPPORTED_INTERPOLATIONS) == 0)
	                    				bInterpolationsEnd = true;
	                    			break;
	                    		case KXmlParser.TEXT:
	                    			if (parser.getName()!=null)
	                    				System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
	                    			break;
                    		}
                    		interpolationsTag = parser.next();
                    	}
                    } else {
                    	if (!parser.getName().equals("rangeSet")) // just a patch to avoid too much messages
                    		System.out.println("Skiped "+parser.getName());
                    }

                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.COVERAGE_OFFERING) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
    }

    private void parseSupportedCRSS(KXmlParser parser) throws XmlPullParserException, IOException {
    	int currentTag;
    	boolean end = false;

    	parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.SUPPORTED_CRSS);
    	currentTag = parser.next();

    	while (!end)
    	{
    		switch(currentTag)
    		{
	    		case KXmlParser.START_TAG:
	    			if (parser.getName().compareTo(DescribeCoverageTags.REQUEST_RESPONSE_CRSS)==0) {
	    				String[] values = parser.nextText().split(SEPARATOR);
	    				for (int i = 0; i < values.length; i++) {
							addSRSs(values[i]);
						}
	    			}
	    			else if (parser.getName().compareTo(DescribeCoverageTags.NATIVE_CRS) == 0) {
	    				setNativeSRS(parser.nextText());
	    			} else if (parser.getName().compareTo(DescribeCoverageTags.REQUEST_CRSS) == 0) {
	    				// TODO
                    	System.out.println("Skiped "+parser.getName());
	    			} else if (parser.getName().compareTo(DescribeCoverageTags.RESPONSE_CRSS) == 0) {
	    				// TODO
                    	System.out.println("Skiped "+parser.getName());
                    } else {
                    	System.out.println("Unrecognized "+parser.getName());
	    			}

	    			break;
	    		case KXmlParser.END_TAG:
	    			if (parser.getName().compareTo(DescribeCoverageTags.SUPPORTED_CRSS) == 0)
	    				return;//end = true;
	    			break;
	    		case KXmlParser.TEXT:
	    			if (parser.getName()!=null)
	    				System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
	    			break;
    		}
    		currentTag = parser.next();
    	}
    }

	private void parseRangeSet(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.RANGESET);
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.NAME)==0)
                    	setRangeSetName(parser.nextText());
                    else if (parser.getName().compareTo(DescribeCoverageTags.LABEL) == 0)
                    	setRangeSetLabel(parser.nextText());
                    else if (parser.getName().compareToIgnoreCase(DescribeCoverageTags.AXISDESCRIPTION) == 0) {
                    	// Reading all axe description
                    	int axisTag = parser.nextTag();
                    	boolean endAxe = false;
                    	while (!endAxe) {
                            switch(axisTag)
                            {
                               case KXmlParser.START_TAG:
                            	   if (parser.getName().compareTo(DescribeCoverageTags.AXISDESCRIPTION)==0) {
                            		   AxisDescription as = parseAxisDescription(parser);
                            		   axisPool.put(as.getName(), as);
                            		   axisDescriptionName = as.getName();
                            	   }
                            	   break;
                               case KXmlParser.END_TAG:
                                   if (parser.getName().compareToIgnoreCase(DescribeCoverageTags.AXISDESCRIPTION) == 0)
                                	   endAxe = true;
                                   break;
                               case KXmlParser.TEXT:
                            	   if (parser.getName()!=null)
                            		   System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                            	   break;
                            }
                            axisTag = parser.next();
                    	}
                    } else {
                    	System.out.println("Unrecognized "+parser.getName());
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.RANGESET) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
	}

	private AxisDescription parseAxisDescription(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.AXISDESCRIPTION);
        AxisDescription as = new AxisDescription();
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                	if (parser.getName().compareTo(DescribeCoverageTags.NAME) == 0) {
                		as.setName(parser.nextText());
                	} else if (parser.getName().compareTo(DescribeCoverageTags.LABEL) == 0) {
                		as.setLabel(parser.nextText());
                	} else if (parser.getName().compareTo(DescribeCoverageTags.VALUES) == 0) {
                		int valuesTag = parser.nextTag();
                		boolean valuesEnd = false;
                		while (!valuesEnd) {
                			switch(valuesTag)	{
	                			case KXmlParser.START_TAG:
	                				if (parser.getName().compareTo(DescribeCoverageTags.SINGLEVALUE) == 0) {
	                					as.addSingleValues(parser.nextText());
	                				} else if (parser.getName().compareTo(DescribeCoverageTags.INTERVAL) == 0) {
	                					as.setInterval(parser.nextText());
	                				} else if (parser.getName().compareTo(DescribeCoverageTags.DEFAULT) == 0) {
	                					as.setDefaultValue(parser.nextText());
	                				}
	                				break;
	                			case KXmlParser.END_TAG:
	                				if (parser.getName().compareTo(DescribeCoverageTags.VALUES) == 0)
	                					valuesEnd = true;
	                				break;
	                			case KXmlParser.TEXT:
	                				if (parser.getName()!=null)
	                					System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
	                				break;
	                			}
                			valuesTag = parser.next();
                		}
                	} else if (parser.getName().compareTo(DescribeCoverageTags.NULLVALUES)==0) {
                		// TODO
                		System.err.println("Missing NULL VALUE PARSING on WCS 1.0.0 Client");
                    } else {
                    	System.out.println("Unrecognized "+parser.getName());
                	}

                	break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareToIgnoreCase(DescribeCoverageTags.AXISDESCRIPTION) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
        return as;
	}

	private void parseDomainSet(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.DOMAINSET);
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.SPATIALDOMAIN)==0)
                    {
                    	parseSpatialDomain(parser);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.TEMPORALDOMAIN)==0) {
                    	parseTemporalDomain(parser);
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.DOMAINSET) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
	}

	private void parseTemporalDomain(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.TEMPORALDOMAIN);
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                	if (parser.getName().compareTo(DescribeCoverageTags.GML_TIMEPOSITION) == 0) {
                		addTimePosition(parser.nextText());
                	} else if (parser.getName().compareTo(DescribeCoverageTags.TIMEPERIOD) == 0) {
                		boolean timePeriodEnd = false;
                		int timePeriodTag = parser.next();
                		String[] myTimePeriod = new String[3];
                		while (!timePeriodEnd) {
                			switch (timePeriodTag) {
                			case KXmlParser.START_TAG:
                				if (parser.getName().compareTo(DescribeCoverageTags.BEGINPOSITION) == 0) {
                					myTimePeriod[0] = parser.nextText();
                				} else if (parser.getName().compareTo(DescribeCoverageTags.ENDPOSITION) == 0) {
                					myTimePeriod[1] = parser.nextText();
                				} else if (parser.getName().compareTo(DescribeCoverageTags.TIMERESOLUTION) == 0) {
                					myTimePeriod[2] = parser.nextText();
                                } else {
                                	System.out.println("Unrecognized "+parser.getName());
                				}
                				break;
                			case KXmlParser.END_TAG:
                				if (parser.getName().compareTo(DescribeCoverageTags.TEMPORALDOMAIN) == 0)
                					end = true;
                				break;
                			case KXmlParser.TEXT:
                				if (parser.getName()!=null)
                					System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                				break;
                			}
                			timePeriodTag = parser.next();
                		}
                		super.timePeriod = "";
                		for (int i = 0; i < myTimePeriod.length-1; i++) {
                			if (myTimePeriod[i]!=null) {
                				super.timePeriod += myTimePeriod[i];
                			}
                			if (myTimePeriod[i+1]!=null) {
                				super.timePeriod += "/";
                			}
						}
                    } else {
                    	System.out.println("Unrecognized "+parser.getName());
                	}
                	break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.TEMPORALDOMAIN) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
	}

	private void parseSpatialDomain(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.SPATIALDOMAIN);
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.GML_ENVELOPE)==0) {
                    	BoundaryBox bBox = parseGMLEnvelope(parser);
                    	addBBox(bBox);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.GRID)==0) {
                    	parseGrid(parser);
                    } else if (parser.getName().compareTo(DescribeCoverageTags.RECTIFIEDGRID)==0) {
                    	parseRectifiedGrid(parser);
                    } else {
                    	System.out.println("Unrecognized "+parser.getName());
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.SPATIALDOMAIN) == 0)
                        return;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
 	}

	private void parseGrid(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
		String value;
		boolean end = false;
		parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.GRID);
		rg = new RectifiedGrid(Integer.parseInt(parser.getAttributeValue("", DescribeCoverageTags.DIMENSION)));

		currentTag = parser.next();

		while (!end)
		{
			switch(currentTag)	{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(DescribeCoverageTags.GML_LIMITS) == 0) {
					parser.nextTag();
					if (parser.getName().compareTo(DescribeCoverageTags.GML_GRIDENVELOPE)==0) {
						boolean gridEnd = false;
						int gridCurrentTag = parser.next();
						while (!gridEnd){
							switch (gridCurrentTag) {
							case KXmlParser.START_TAG:

								if (parser.getName().compareTo(DescribeCoverageTags.GML_LOW) == 0) {
									value = parser.nextText();
									String[] ss = value.split(SEPARATOR);
									int[] limits = new int[ss.length];
									for (int i = 0; i < limits.length; i++) {
										limits[i] = Integer.parseInt(ss[i]);
									}
									rg.addLowGridEnvelopLimit(limits);
								} else if (parser.getName().compareTo(DescribeCoverageTags.GML_HIGH) == 0) {
									value = parser.nextText();
									String[] ss = value.split(SEPARATOR);
									int[] limits = new int[ss.length];
									for (int i = 0; i < limits.length; i++) {
										limits[i] = Integer.parseInt(ss[i]);
									}
									rg.addHighGridEnvelopLimit(limits);
								} else {
									System.out.println("Unrecognized "+parser.getName());
								}
								break;
							case KXmlParser.END_TAG:
								if (parser.getName().compareTo(DescribeCoverageTags.GML_GRIDENVELOPE) == 0)
									gridEnd = true;
								break;
							case KXmlParser.TEXT:
								if (parser.getName()!=null)
									System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
								break;
							}
							gridCurrentTag = parser.next();
						}
					}
				} else if (parser.getName().compareTo(DescribeCoverageTags.GML_AXISNAME)==0) {
					rg.addAxisName(parser.nextText());
				} else {
					System.out.println("Unrecognized "+parser.getName());
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(DescribeCoverageTags.GRID) == 0)
					return;//end = true;
				break;
			case KXmlParser.TEXT:
				if (parser.getName()!=null)
					System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
				break;
			}
			currentTag = parser.next();
		}
	}

	private void parseRectifiedGrid(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
		String value;
		boolean end = false;
		parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.RECTIFIEDGRID);
		rg = new RectifiedGrid(Integer.parseInt(parser.getAttributeValue("", DescribeCoverageTags.DIMENSION)));

		// TODO maybe this is wrong
		rg.addToOffsetVector(new double[] { -1, -1});
		rg.addToOffsetVector(new double[] { -1, -1});

		currentTag = parser.next();

		while (!end)
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(DescribeCoverageTags.GML_LIMITS) == 0) {
					parser.nextTag();
					if (parser.getName().compareTo(DescribeCoverageTags.GML_GRIDENVELOPE)==0) {
						boolean gridEnd = false;
						int gridCurrentTag = parser.next();
						while (!gridEnd)
						{
							switch (gridCurrentTag) {
							case KXmlParser.START_TAG:

								if (parser.getName().compareTo(DescribeCoverageTags.GML_LOW) == 0) {
									value = parser.nextText();
									String[] ss = value.split(SEPARATOR);
									int[] limits = new int[ss.length];
									for (int i = 0; i < limits.length; i++) {
										limits[i] = Integer.parseInt(ss[i]);
									}
									rg.addLowGridEnvelopLimit(limits);
								} else if (parser.getName().compareTo(DescribeCoverageTags.GML_HIGH) == 0) {
									value = parser.nextText();
									String[] ss = value.split(SEPARATOR);
									int[] limits = new int[ss.length];
									for (int i = 0; i < limits.length; i++) {
										limits[i] = Integer.parseInt(ss[i]);
									}
									rg.addHighGridEnvelopLimit(limits);
								} else {
									System.out.println("Unrecognized "+parser.getName());
								}
								break;
							case KXmlParser.END_TAG:
								if (parser.getName().compareTo(DescribeCoverageTags.GML_GRIDENVELOPE) == 0)
									gridEnd = true;
								break;
							case KXmlParser.TEXT:
								if (parser.getName()!=null)
									System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
								break;
							}
							gridCurrentTag = parser.next();
						}
					}
				} else if (parser.getName().compareTo(DescribeCoverageTags.GML_AXISNAME)==0) {
					rg.addAxisName(parser.nextText());
				} else if (parser.getName().compareTo(DescribeCoverageTags.GML_ORIGIN)==0) {
					parser.nextTag();
					if (parser.getName().compareTo(DescribeCoverageTags.GML_POS)==0) {
						value = parser.nextText();
						String[] ss = value.split(SEPARATOR);
						double[] ori = new double[ss.length];
						for (int i = 0; i < ori.length; i++) {
							ori[i] = Double.parseDouble(ss[i]);
						}
						rg.setOrigin(ori);
					}
				} else if (parser.getName().compareTo(DescribeCoverageTags.OFFSETVECTOR) == 0) {
					value = parser.nextText();
					String[] ss = value.split(SEPARATOR);
					double[] offset = new double[ss.length];
					for (int i = 0; i < offset.length; i++) {
						offset[i] = Double.parseDouble(ss[i]);
					}
					rg.addToOffsetVector(offset);
				} else {
					System.out.println("Unrecognized "+parser.getName());
				}
				break;
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(DescribeCoverageTags.RECTIFIEDGRID) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:
				if (parser.getName()!=null)
					System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
				break;
			}
			currentTag = parser.next();
		}
	}


	private BoundaryBox parseLonLatGMLEnvelope(KXmlParser parser) throws XmlPullParserException, IOException {
		BoundaryBox bBox = new BoundaryBox();
		int currentTag;
        String value;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.LONLAT_ENVELOPE);
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.GML_POS)==0) {
                    	bBox.setSrs(DescribeCoverageTags.WGS84);
                    	String[] coordinates;
                    	double minx, miny, maxx, maxy;
                    	value = parser.nextText();
                    	coordinates = value.split(SEPARATOR);
                    	minx = Double.parseDouble(coordinates[0]);
                    	miny = Double.parseDouble(coordinates[1]);

                    	parser.nextTag();

                    	parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.GML_POS);
                    	value = parser.nextText();
                    	coordinates = value.split(SEPARATOR);
                    	maxx = Double.parseDouble(coordinates[0]);
                    	maxy = Double.parseDouble(coordinates[1]);

                    	bBox.setXmin(minx);
                    	bBox.setYmin(miny);
                    	bBox.setXmax(maxx);
                    	bBox.setYmax(maxy);
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.LONLAT_ENVELOPE) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }
        return bBox;
	}

	private BoundaryBox parseGMLEnvelope(KXmlParser parser) throws XmlPullParserException, IOException {
		BoundaryBox bBox = new BoundaryBox();
		int currentTag;
        String value;
        boolean end = false;

        parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.GML_ENVELOPE);
        bBox.setSrs(parser.getAttributeValue("", DescribeCoverageTags.SRSNAME));
        currentTag = parser.next();

        while (!end)
        {
             switch(currentTag)
             {
                case KXmlParser.START_TAG:
                	if (parser.getName().compareTo(DescribeCoverageTags.GML_POS)==0) {

                    	String[] coordinates;
                    	double minx, miny, maxx, maxy;
                    	value = parser.nextText();
                    	coordinates = value.split(SEPARATOR);
                    	minx = Double.parseDouble(coordinates[0]);
                    	miny = Double.parseDouble(coordinates[1]);

                    	parser.nextTag();
                    	//parser.nextTag();
                    	parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.GML_POS);
                    	value = parser.nextText();
                    	coordinates = value.split(SEPARATOR);
                    	maxx = Double.parseDouble(coordinates[0]);
                    	maxy = Double.parseDouble(coordinates[1]);

                    	bBox.setXmin(minx);
                    	bBox.setYmin(miny);
                    	bBox.setXmax(maxx);
                    	bBox.setYmax(maxy);
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(DescribeCoverageTags.GML_ENVELOPE) == 0)
                        end = true;
                    break;
                case KXmlParser.TEXT:
                	if (parser.getName()!=null)
                		System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
                	break;
             }
             currentTag = parser.next();
        }

        return bBox;
	}
}
