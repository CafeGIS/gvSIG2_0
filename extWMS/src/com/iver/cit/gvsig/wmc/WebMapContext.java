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

/* CVS MESSAGES:
*
* $Id: WebMapContext.java 28433 2009-05-06 10:37:01Z vcaballero $
* $Log$
* Revision 1.9  2007-03-06 17:06:43  caballero
* Exceptions
*
* Revision 1.8  2007/01/08 07:57:34  jaume
* *** empty log message ***
*
* Revision 1.7  2006/09/26 14:41:48  jaume
* *** empty log message ***
*
* Revision 1.6  2006/09/25 10:23:26  caballero
* Projection
*
* Revision 1.5  2006/09/15 10:44:24  caballero
* extensibilidad de documentos
*
* Revision 1.4  2006/07/21 10:31:05  jaume
* *** empty log message ***
*
* Revision 1.3  2006/05/24 16:36:28  jaume
* *** empty log message ***
*
* Revision 1.2  2006/05/12 07:47:39  jaume
* removed unnecessary imports
*
* Revision 1.1  2006/05/03 07:51:21  jaume
* *** empty log message ***
*
* Revision 1.10  2006/05/02 15:57:44  jaume
* Few better javadoc
*
* Revision 1.9  2006/04/25 11:40:56  jaume
* *** empty log message ***
*
* Revision 1.8  2006/04/25 10:01:56  jaume
* fixed extend bug when importing
*
* Revision 1.7  2006/04/21 12:46:53  jaume
* *** empty log message ***
*
* Revision 1.6  2006/04/21 11:53:50  jaume
* *** empty log message ***
*
* Revision 1.5  2006/04/21 11:02:25  jaume
* few translations
*
* Revision 1.4  2006/04/21 10:27:32  jaume
* exporting now supported
*
* Revision 1.3  2006/04/20 17:11:54  jaume
* Attempting to export
*
* Revision 1.2  2006/04/19 16:34:29  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/19 07:57:29  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/12 17:10:53  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.wmc;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.mapcontext.exceptions.UnsupportedVersionLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.rendering.legend.XmlBuilder;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.exceptions.ImportMapContextException;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode.FMapWMSStyle;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.ProjectView;

/**
 * Class that loads and produces WebMapContext files and holds its attributes.
 *
 * You can create a blank WebMapContext instance using the default constructor
 * or an already initialized instance by specifying a source file. In the last
 * case the file will be readed and the values loaded an holded by the instance.
 *
 * You can get the equivalent WebMapContext XML document by calling the method
 * getXML() supplying the ProjectView that you want to export and depending on
 * the value of the fileVersion field (currently you can use "1.1.0" (default),
 * "1.0.0" or "0.4.1" to specify the destination document version.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class WebMapContext {
	public final static String FILE_EXTENSION = ".cml";

	static final ArrayList supportedVersions = new ArrayList();
	static public final ArrayList exportVersions    = new ArrayList();
	static private HashSet supportedLayers = new HashSet();
	private File mapContextFile;
	private String encoding = "UTF-8";
	private String WMC_START_TAG;

	// MapContext stuff
	public String fileVersion = null;
	public Dimension windowSize = null;
	public String srs = null;
	public Rectangle2D bBox = null;
	public String title = null;
	public String id = null;
	public String xmlns = null;
	public String xmlns_xlink = null;
	public String xmlns_xsi = null;;
	public String xsi_schemaLocation = null;
	public String _abstract = null;
	public ArrayList keywordList = null;
	public Dimension logoURLSize = null;
	public String logoURLFormat = null;
	public String logoURL = null;
	public String descriptionURLFormat = null;
	public String descriptionURL = null;
	public boolean contactInfo = false;
	public String contactPerson = null;
	public String contactOrganization = null;
	public String contactPosition = null;
	public String address = null;
	public String city = null;
	public String stateOrProvince = null;
	public String postCode = null;
	public String country = null;
	public String telephone = null;
	public String fax = null;
	public String email = null;

	private StringBuffer errorMessages;

	/**
	 * list of FLyrWMS.
	 */
	ArrayList<FLyrWMS> layerList = null;

	/**
	 * key: server URL (URL)
	 * value: server title (String)
	 */
	Hashtable serverTitles = null;

	/**
	 * key: layer FLyrWMS
	 * value: layer abstract (String)
	 */
	Hashtable layerAbstracts = null;

	/**
	 * key: layer FLyrWMS
	 * value: layer formats (String[])
	 */
	Hashtable layerFormats = null;

	/**
	 * key: layer FLyrWMS
	 * value: styles (FMapWMSStyle[])
	 */
	Hashtable layerStyles = null;


	static {
		supportedVersions.add("1.1.0");
		supportedVersions.add("1.0.0");
		supportedVersions.add("0.1.4");
	}

	static {
		exportVersions.add("1.1.0");
		exportVersions.add("1.0.0");
		exportVersions.add("0.1.4");
	}

	static {
		supportedLayers.add(FLyrWMS.class);
	}


	/**
	 * Initializes the WebMapContext properties from the values in the WebMapContext (.cml)
	 * file passed in the argument.
	 * @param file
	 * @throws UnsupportedVersionLayerException
	 * @throws ImportMapContextException
	 */
	public void readFile(File file) throws UnsupportedVersionLayerException, ImportMapContextException {
		this.mapContextFile = file;
		errorMessages = new StringBuffer();
		if (getVersion()!=null) {
			if (supportedVersions.contains(getVersion())) {
				WMC_START_TAG = WebMapContextTags.VIEW_CONTEXT;
				if (getVersion().equals("0.1.4")) {
					WMC_START_TAG = WebMapContextTags.VIEW_CONTEXT_0_1_4;
					parse1_1_0(file);
				} else if (getVersion().equals("1.1.0")) {
					parse1_1_0(file);
				} else if (getVersion().equals("1.0.0")) {
					parse1_1_0(file);
				} else {
					parseDefaultVersion(file);
				}

				// Once parsed, check errors
				if (errorMessages.length()>0) {
					throw new ImportMapContextException(errorMessages.toString(), false);
				}
			} else {
				throw new UnsupportedVersionLayerException(file.getName(),null);
			}
		}
	}


	/**
	 * If no version was recognized then will parse the default one which is supposed
	 * to be the best available.
	 * @param file
	 */
	private void parseDefaultVersion(File file) {
		parse1_1_0(file);
	}

	/**
	 * Reads the header of the Weg Map Context file and determines which version
	 * it belongs to.
	 * @return String.
	 */
	private String getVersion() {
		if (fileVersion == null) {
			String v = null;
			try {
				FileReader fr = new FileReader(mapContextFile);
				KXmlParser parser = new KXmlParser();
				parser.setInput(fr);
				parser.nextTag();
	    		if ( parser.getEventType() != KXmlParser.END_DOCUMENT )	{
	    			if ((parser.getName().compareTo(WebMapContextTags.VIEW_CONTEXT) == 0) ||
	    				 parser.getName().compareTo(WebMapContextTags.VIEW_CONTEXT_0_1_4) == 0) {
	    				v = parser.getAttributeValue("", WebMapContextTags.VERSION);
	    			}
	    		}
			} catch (FileNotFoundException fnfEx) {
			} catch (XmlPullParserException xmlEx) {
				xmlEx.printStackTrace();
			} catch (IOException e) {
			}
			fileVersion = v;
		}
		return fileVersion;
	}

	/**
	 * Reads a Web Map Context version 1.1.0. As far as v1.0.0 is a subset of
	 * v1.1.0 it can be used to read files belonging to 1.0.0 as well.
	 * @param file, the web map context file.
	 */
	private void parse1_1_0(File file) {
		try {
			FileReader fr;
			try
	    	{
				fr = new FileReader(file);
	    		BufferedReader br = new BufferedReader(fr);
	    		char[] buffer = new char[100];
	    		br.read(buffer);
	    		StringBuffer st = new StringBuffer(new String(buffer));
	    		String searchText = "encoding=\"";
	    		int index = st.indexOf(searchText);
	    		if (index>-1) {
	    			st.delete(0, index+searchText.length());
	    			encoding = st.substring(0, st.indexOf("\""));
	    		}
	    	} catch(FileNotFoundException ex)	{
	    		ex.printStackTrace();
	    	} catch (IOException e) {
				e.printStackTrace();
			}

			fr = new FileReader(file);
			KXmlParser parser = new KXmlParser();
			parser.setInput(new FileInputStream(file), encoding);
			int tag = parser.nextTag();
			if ( parser.getEventType() != KXmlParser.END_DOCUMENT )
			{
				parser.require(KXmlParser.START_TAG, null, WMC_START_TAG);
				while(tag != KXmlParser.END_DOCUMENT) {
					switch(tag) {
						case KXmlParser.START_TAG:
							if (parser.getName().compareTo(WMC_START_TAG) == 0) {
								id = parser.getAttributeValue("", WebMapContextTags.ID);
								xmlns = parser.getAttributeValue("", WebMapContextTags.XMLNS);
								xmlns_xlink = parser.getAttributeValue("", WebMapContextTags.XMLNS_XLINK);
								xmlns_xsi = parser.getAttributeValue("", WebMapContextTags.XMLNS_XSI);
								xsi_schemaLocation = parser.getAttributeValue("", WebMapContextTags.XSI_SCHEMA_LOCATION) ;
							} else if (parser.getName().compareTo(WebMapContextTags.GENERAL) == 0) {
								parseGeneral1_1_0(parser);
							} else if (parser.getName().compareTo(WebMapContextTags.LAYER_LIST) == 0) {
								int layerListTag = parser.nextTag();
								boolean bLayerListEnd = false;
								layerList = new ArrayList();
								while (!bLayerListEnd) {
									switch(layerListTag) {
									case KXmlParser.START_TAG:
										if (parser.getName().compareTo(WebMapContextTags.LAYER) == 0) {
											FLyrWMS layer = parseLayer1_1_0(parser);
											// will use the mapcontext's bounding box as layer's fullextent
											IProjection proj =  CRSFactory.getCRS(srs);
											if (proj == null) {
												// non supported srs, and cannot continue
												String msg = PluginServices.getText(this, "unsupported_crs") +
															 " (" + srs + ")";
												throw new ImportMapContextException(msg, true);
											}
											String[] availableSRS = layer.getSRS().split(",");
											ICoordTrans ct = null;
											String mySRS = null;
											for (int i = 0; i < availableSRS.length; i++) {
												mySRS = availableSRS[i];
												IProjection dstProj = CRSFactory.getCRS(mySRS);
												if (dstProj != null) {
													try{
															ct = proj.getCT(dstProj);
														}catch(Exception e){
															NotificationManager.showMessageError(e.getLocalizedMessage(), e);
														}
													if (mySRS.equals(srs)) {
														// this is the most suitable SRS,
														break;
													}
												}
											}

											if (ct != null) {
												// I've found a supported projection
												layer.setFullExtent(ct.convert(bBox));
												layer.setSRS(mySRS);
											} else {
												// can't reproject
												errorMessages.append("[").
														      append(PluginServices.getText(this, "layer")).
														      append(" ").
														      append(layer.getName()).
														      append("] ").
															  append(PluginServices.getText(this, "cant_reproject_from_any_of")).
															  append(" [").append(layer.getSRS()).append("] ").
															  append(PluginServices.getText(this, "to")).
															  append(srs).
															  append("\n");

												/*
												 * will use view's projection and bounding box
												 * strictly this is incorrect but, at least it allows
												 * the user to recover by changin layer properties.
												 */
												layer.setFullExtent(bBox);
												layer.setSRS(srs);
											}

											layer.setWmsTransparency(true);
											layerList.add(layer);
										} else {
					                    	System.out.println("Unrecognized "+parser.getName());
										}
										break;
									case KXmlParser.END_TAG:
										if (parser.getName().compareTo(WebMapContextTags.LAYER_LIST) == 0) {
											bLayerListEnd = true;
										}
										break;
									case KXmlParser.TEXT:
										//System.out.println("[TEXT]["+kxmlParser.getText()+"]");
										break;
									}
									layerListTag = parser.next();
								}
							} else if (parser.getName().compareTo(WebMapContextTags.DIMENSION_LIST) == 0) {
								// TODO
								System.out.println("WebMapContext's Dimension not yet implemented");
							} else {
		                    	System.out.println("Unrecognized "+parser.getName());
							}
							break;
						case KXmlParser.END_TAG:
							break;
						case KXmlParser.TEXT:
							//System.out.println("[TEXT]["+kxmlParser.getText()+"]");
							break;
					}
					tag = parser.next();
				}
				parser.require(KXmlParser.END_DOCUMENT, null, null);
			}
		} catch (Exception e) {
			NotificationManager.addError(PluginServices.getText(this, "map_context_file_error"),e);
			e.printStackTrace();
		}

	}

	/**
	 * Parses the General tag of a web map context 1.1.0 (and 1.0.0) file
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void parseGeneral1_1_0(KXmlParser parser) throws XmlPullParserException, IOException {
		boolean end = false;
    	int tag = parser.next();
    	while (!end) {
    		switch(tag) {
        		case KXmlParser.START_TAG:
        			if (parser.getName().compareTo(WebMapContextTags.WINDOW) == 0) {
        				if (windowSize == null) {
							windowSize = new Dimension();
						}
						windowSize.setSize(Integer.parseInt(parser.getAttributeValue("", WebMapContextTags.WIDTH)),
										   Integer.parseInt(parser.getAttributeValue("", WebMapContextTags.HEIGHT)));
        			} else if (parser.getName().compareTo(WebMapContextTags.BOUNDING_BOX) == 0) {
        				srs = parser.getAttributeValue("", WebMapContextTags.SRS);
        				double minx = Double.parseDouble(parser.getAttributeValue("", WebMapContextTags.X_MIN));
        				double miny = Double.parseDouble(parser.getAttributeValue("", WebMapContextTags.Y_MIN));
        				double maxx = Double.parseDouble(parser.getAttributeValue("", WebMapContextTags.X_MAX));
        				double maxy = Double.parseDouble(parser.getAttributeValue("", WebMapContextTags.Y_MAX));
        				bBox = new Rectangle2D.Double(minx, miny, maxx-minx, maxy-miny);
            		} else if (parser.getName().compareTo(WebMapContextTags.TITLE) == 0) {
        				title = parser.nextText();
        			} else if (parser.getName().compareTo(WebMapContextTags.ABSTRACT) == 0) {
        				_abstract = parser.nextText();
        			} else if (parser.getName().compareTo(WebMapContextTags.KEYWORD_LIST) == 0) {
        				keywordList = new ArrayList();
        				boolean keywordEnd = false;
        		    	int keywordTag = parser.next();
        		    	while (!keywordEnd) {
        		    		switch(keywordTag) {
        		        		case KXmlParser.START_TAG:
        		        			if (parser.getName().compareTo(WebMapContextTags.KEYWORD) == 0) {
        		        				keywordList.add(parser.nextText());
        		        			} else {
        		                    	System.out.println("Unrecognized "+parser.getName());
        							}
        							break;
        		         		case KXmlParser.END_TAG:
        		        			if (parser.getName().compareTo(WebMapContextTags.KEYWORD_LIST) == 0) {
										keywordEnd = true;
									}
        		        			break;
        		        		case KXmlParser.TEXT:
        		        			if (parser.getName()!=null) {
										System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
									}
        		        			break;
        		    		}
        		    		keywordTag = parser.next();
        		    	}
        			} else if (parser.getName().compareTo(WebMapContextTags.LOGO_URL) == 0) {
        				logoURLSize = new Dimension(Integer.parseInt(parser.getAttributeValue("", WebMapContextTags.WIDTH)),
        											Integer.parseInt(parser.getAttributeValue("", WebMapContextTags.HEIGHT)));
        				logoURLFormat = parser.getAttributeValue("", WebMapContextTags.FORMAT.toLowerCase());
        				parser.nextTag();
        				if (parser.getName().compareTo(WebMapContextTags.ONLINE_RESOURCE) == 0) {
        					logoURL = parser.getAttributeValue("", WebMapContextTags.XLINK_HREF);
        				} else {
                        	System.out.println("Unrecognized "+parser.getName());
    					}
        			} else if (parser.getName().compareTo(WebMapContextTags.DESCRIPTION_URL) == 0) {
        				descriptionURLFormat = parser.getAttributeValue("", WebMapContextTags.FORMAT.toLowerCase());
        				parser.nextTag();
        				if (parser.getName().compareTo(WebMapContextTags.ONLINE_RESOURCE) == 0) {
        					descriptionURL = parser.getAttributeValue("", WebMapContextTags.XLINK_HREF);
        				} else {
                        	System.out.println("Unrecognized "+parser.getName());
    					}
        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_INFORMATION) == 0) {
        				boolean contactInfoEnd = false;
        		    	int contactInfoTag = parser.next();
        		    	while (!contactInfoEnd) {
        		    		switch(contactInfoTag) {
        		        		case KXmlParser.START_TAG:
        		        			if (parser.getName().compareTo(WebMapContextTags.CONTACT_PERSON) == 0) {
        		        				contactPerson = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_ORGANIZATION) == 0) {
        		        				contactOrganization = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_POSITION) == 0) {
        		        				contactPosition = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.ADDRESS) == 0) {
        		        				address = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CITY) == 0) {
        		        				city = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.STATE_OR_PROVINCE) == 0) {
        		        				stateOrProvince = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.POSTCODE) == 0) {
        		        				postCode = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.COUNTRY) == 0) {
        		        				country = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_VOICE_TELEPHONE) == 0) {
        		        				telephone = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_FACSIMILE_TELEPHONE) == 0) {
        		        				fax = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_ELECTRONIC_MAIL_ADDRESS) == 0) {
        		        				email = parser.nextText();
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_PERSON_PRIMARY) == 0) {
        		        				// DO NOTHING
        		        			} else if (parser.getName().compareTo(WebMapContextTags.CONTACT_ADDRESS) == 0) {
        		        				// DO NOTHING
        		        			} else {
        		                    	System.out.println("Unrecognized "+parser.getName());
        							}
        		        			break;
        		         		case KXmlParser.END_TAG:
        		        			if (parser.getName().compareTo(WebMapContextTags.CONTACT_INFORMATION) == 0) {
										contactInfoEnd = true;
									}
        		        			break;
        		        		case KXmlParser.TEXT:
        		        			if (parser.getName()!=null) {
										System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
									}
        		        			break;
        		    		}
        		    		contactInfoTag = parser.next();
        		    	}
        		    } else {
                    	System.out.println("Unrecognized "+parser.getName());
					}
					break;
         		case KXmlParser.END_TAG:
        			if (parser.getName().compareTo(WebMapContextTags.GENERAL) == 0) {
						end = true;
					}
        			break;
        		case KXmlParser.TEXT:
        			if (parser.getName()!=null) {
						System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
					}
        			break;
    		}
    		tag = parser.next();
    	}
	}

	private FLyrWMS parseLayer1_1_0(KXmlParser parser) throws XmlPullParserException, IOException {
		boolean end = false;
		FLyrWMS layer = new FLyrWMS();

		String queryable = parser.getAttributeValue("", WebMapContextTags.QUERYABLE);
		layer.setQueryable(queryable!=null && (queryable.equals("1") || queryable.toLowerCase().equals("true")));

		String hidden = parser.getAttributeValue("", WebMapContextTags.HIDDEN);
		layer.setVisible(hidden==null || !hidden.equals("1") || !hidden.toLowerCase().equals("true"));
    	int tag = parser.next();
    	while (!end) {
    		switch(tag) {
        		case KXmlParser.START_TAG:
        			if (parser.getName().compareTo(WebMapContextTags.SERVER) == 0) {
        				String serverTitle = parser.getAttributeValue("", WebMapContextTags.TITLE.toLowerCase());
        				parser.nextTag();
        				if (parser.getName().compareTo(WebMapContextTags.ONLINE_RESOURCE) == 0) {
        					layer.setHost(new URL(parser.getAttributeValue("", WebMapContextTags.XLINK_HREF)));
        					if (serverTitles == null) {
								serverTitles = new Hashtable();
							}
            				serverTitles.put(parser.getAttributeValue("", WebMapContextTags.XLINK_HREF), serverTitle);
        				}
        			} else if (parser.getName().compareTo(WebMapContextTags.TITLE) == 0) {
        				layer.setName(parser.nextText());
        			} else if (parser.getName().compareTo(WebMapContextTags.NAME) == 0) {
        				layer.setLayerQuery(parser.nextText());
        			/* //TODO This case would handle nested layer definitions.
        			 *
        			 } else if (parser.getName().compareTo(WebMapContextTags.LAYER) == 0) {
        				FLyrWMS sonLayer = parseLayer1_1_0(parser);
        				String q = layer.getLayerQuery();
        				if (q == null) q = "";
        				else q += ",";
        				layer.setLayerQuery( q + sonLayer.getLayerQuery());
        			 *
        			 */
        			} else if (parser.getName().compareTo(WebMapContextTags.ABSTRACT) == 0) {
        				if (layerAbstracts == null) {
							layerAbstracts = new Hashtable();
						}
        				layerAbstracts.put(layer, parser.nextText());
        			} else if (parser.getName().compareTo(WebMapContextTags.SRS) == 0) {
        				layer.setSRS(parser.nextText());
        			} else if (parser.getName().compareTo(WebMapContextTags.FORMAT_LIST) == 0) {
        				int formatsTag = parser.nextTag();
        				boolean bFormatsEnd = false;
        				ArrayList formats = new ArrayList();
        				while (!bFormatsEnd) {
        					switch (formatsTag) {
        					case KXmlParser.START_TAG:
        						if (parser.getName().compareTo(WebMapContextTags.FORMAT) == 0) {
        							String current = parser.getAttributeValue("", WebMapContextTags.CURRENT);
        							String format = parser.nextText();
        							if (current!=null && current.equals("1")) {
										layer.setFormat(format);
									}
        							formats.add(format);
        						} else {
        							System.out.println("Unrecognized "+parser.getName());
        						}
        						break;
        					case KXmlParser.END_TAG:
        						if (parser.getName().compareTo(WebMapContextTags.FORMAT_LIST) == 0) {
									bFormatsEnd = true;
								}
        						break;
        					case KXmlParser.TEXT:
        						if (parser.getName()!=null) {
									System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
								}
        						break;
        					}
        					formatsTag = parser.next();
        				}
        				if (layerFormats == null) {
							layerFormats = new Hashtable();
						}
        				layerFormats.put(layer, formats.toArray(new String[0]));

        			} else if (parser.getName().compareTo(WebMapContextTags.STYLE_LIST) == 0) {
        				int stylesTag = parser.nextTag();
        				boolean bStylesEnd = false;
        				ArrayList styles = new ArrayList();
        				while (!bStylesEnd) {
        					switch (stylesTag) {
        					case KXmlParser.START_TAG:
        						if (parser.getName().compareTo(WebMapContextTags.STYLE) == 0) {
        							String current = parser.getAttributeValue("", WebMapContextTags.CURRENT);
        							FMapWMSStyle style = parseStyle1_1_0(parser);

        							if (current!=null && current.equals("1") && !style.name.equals("default")) {
        								Vector v = new Vector();
        								v.add(style.name);
        								layer.setStyles(v);
        							}
        							styles.add(style);
        						} else {
        							System.out.println("Unrecognized "+parser.getName());
        						}
        						break;
        					case KXmlParser.END_TAG:
        						if (parser.getName().compareTo(WebMapContextTags.STYLE_LIST) == 0) {
									bStylesEnd = true;
								}
        						break;
        					case KXmlParser.TEXT:
        						if (parser.getName()!=null) {
									System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
								}
        						break;
        					}
        					stylesTag = parser.next();
        				}
        				if (layerStyles == null) {
							layerStyles = new Hashtable();
						}

        				layerStyles.put(layer, styles.toArray(new FMapWMSStyle[0]));
        			} else {
        				System.out.println("Unrecognized "+parser.getName());
        			}
        			break;
        		case KXmlParser.END_TAG:
        			if (parser.getName().compareTo(WebMapContextTags.LAYER) == 0) {
						end = true;
					}
        			break;
        		case KXmlParser.TEXT:
        			if (parser.getName()!=null) {
						System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
					}
        			break;
    		}
    		tag = parser.next();
    	}
    	return layer;
	}

	private FMapWMSStyle parseStyle1_1_0(KXmlParser parser) throws XmlPullParserException, IOException {
		boolean end = false;
		String styleName = null;
		String styleAbstract = null;
		String styleTitle = null;
		int legendWidth = -1;
		int legendHeight = -1;
		String legendType = null;
		String legendHref = null;

    	int tag = parser.next();
    	while (!end) {
    		switch(tag) {
        		case KXmlParser.START_TAG:
        			if (parser.getName().compareTo(WebMapContextTags.NAME) == 0) {
        				styleName = parser.nextText();
        			} else if (parser.getName().compareTo(WebMapContextTags.ABSTRACT) == 0) {
        				styleAbstract = parser.nextText();
        			} else if (parser.getName().compareTo(WebMapContextTags.TITLE) == 0) {
        				styleTitle = parser.nextText();
        			} else if (parser.getName().compareTo(WebMapContextTags.LEGEND_URL) == 0){
        				legendWidth = Integer.parseInt(parser.getAttributeValue("", WebMapContextTags.WIDTH));
        				legendHeight = Integer.parseInt(parser.getAttributeValue("", WebMapContextTags.HEIGHT));
        				parser.nextTag();
        				if (parser.getName().compareTo(WebMapContextTags.ONLINE_RESOURCE) == 0 ) {
							legendType = parser.getAttributeValue("", WebMapContextTags.XLINK_TYPE);
							legendHref = parser.getAttributeValue("", WebMapContextTags.XLINK_HREF);
        				}
        			} else {
                    	System.out.println("Unrecognized "+parser.getName());
					}
        			break;
         		case KXmlParser.END_TAG:
        			if (parser.getName().compareTo(WebMapContextTags.STYLE) == 0) {
						end = true;
					}
        			break;
        		case KXmlParser.TEXT:
        			if (parser.getName()!=null) {
						System.out.println("[TAG]["+parser.getName()+"]\n[TEXT]["+parser.getText().trim()+"]");
					}
        			break;
    		}
    		tag = parser.next();
    	}
    	WMSLayerNode n = new WMSLayerNode();
    	FMapWMSStyle sty = n.new FMapWMSStyle();
    	sty.name = styleName;
    	sty.title = styleTitle;
    	sty.styleAbstract = styleAbstract;
    	sty.legendWidth = legendWidth;
    	sty.legendHeight = legendHeight;
    	sty.type = legendType;
    	sty.href = legendHref;
    	sty.parent = null;
    	return sty;
	}

	/**
	 * Creates the Web Map Context (WMC) XML according on the version 1.1.0. Since Web
	 * WMC 1.0.0 is a subset of WMC 1.1.0 and this method does not produce tags for
	 * unset values, it can be used to produce WMC 1.0.0, or even to produce
	 * the deprecated (but still alive) WMC 0.1.4.
	 * @param v
	 * @return
	 */
	private String createMapContext1_1_0(ProjectView v) {
		ArrayList layersToExport = getExportableLayers(v);

		XmlBuilder xml = new XmlBuilder();
		HashMap xmlAttrs = new HashMap();
		xml.setEncoding("ISO-8859-1"); // TODO make it customizable???
		xml.writeHeader();

		// <ViewContext>
		String viewContextTag;
		if (fileVersion.equals("0.1.4")) {
			viewContextTag = WebMapContextTags.VIEW_CONTEXT_0_1_4;
		} else {
			viewContextTag = WebMapContextTags.VIEW_CONTEXT;
		}

		xml.writeRaw("<!-- "+PluginServices.getText(this, "created_with")+" gvSIG "+Project.VERSION+" -->");

		xmlAttrs.put(WebMapContextTags.VERSION, fileVersion);
		xmlAttrs.put(WebMapContextTags.ID, id);
		xmlAttrs.put(WebMapContextTags.XMLNS, WebMapContextTags.XMLNS_VALUE);
		xmlAttrs.put(WebMapContextTags.XMLNS_XLINK, WebMapContextTags.XMLNS_XLINK_VALUE);
		xmlAttrs.put(WebMapContextTags.XMLNS_XSI, WebMapContextTags.XMLNS_XSI_VALUE);
		xmlAttrs.put(WebMapContextTags.XSI_SCHEMA_LOCATION, WebMapContextTags.XSI_SCHEMA_LOCATION_VALUE);
		xml.openTag(viewContextTag,  xmlAttrs);
		xmlAttrs.clear();

			// <General>
			xml.openTag(WebMapContextTags.GENERAL);

				// <Window>
				if (windowSize!=null) {
					xmlAttrs.put(WebMapContextTags.WIDTH, ((int) windowSize.getWidth())+"");
					xmlAttrs.put(WebMapContextTags.HEIGHT, ((int) windowSize.getHeight())+"");
					xml.writeTag(WebMapContextTags.WINDOW, xmlAttrs);
					xmlAttrs.clear();
				}
				// </Window>

				// <BoundingBox>
				xmlAttrs.put(WebMapContextTags.SRS, v.getProjection().getAbrev());
				xmlAttrs.put(WebMapContextTags.X_MIN, bBox.getMinX()+"");
				xmlAttrs.put(WebMapContextTags.Y_MIN, bBox.getMinY()+"");
				xmlAttrs.put(WebMapContextTags.X_MAX, bBox.getMaxX()+"");
				xmlAttrs.put(WebMapContextTags.Y_MAX, bBox.getMaxY()+"");
				xml.writeTag(WebMapContextTags.BOUNDING_BOX, xmlAttrs);
				xmlAttrs.clear();
				// </BoundingBox>

				// <Title>
				xml.writeTag(WebMapContextTags.TITLE, title.trim());
				// </Title>

				// <Abstract>
				if (_abstract != null) {
					xml.writeTag(WebMapContextTags.ABSTRACT, _abstract.trim());
				// </Abstract>
				}

				// <LogoURL>
				if (logoURL != null) {
					xml.writeTag(WebMapContextTags.LOGO_URL, logoURL.trim());
				// </LogoURL>
				}

				// <DescriptionURL>
				if (descriptionURL != null) {
					xml.writeTag(WebMapContextTags.DESCRIPTION_URL, descriptionURL.trim());
				// </DescriptionURL>
				}

				if (contactInfo) {

					// <ContactInformation>
					xml.openTag(WebMapContextTags.CONTACT_INFORMATION);
					if (contactPerson != null || contactOrganization != null) {

						// <ContactPersonPrimary>
						xml.openTag(WebMapContextTags.CONTACT_PERSON_PRIMARY);

							// <ContactPerson>
							if (contactPerson != null) {
								xml.writeTag(WebMapContextTags.CONTACT_PERSON, contactPerson.trim());
							// </ContactPerson>
							}

							// <ContactOrganization>
							if (contactOrganization != null) {
								xml.writeTag(WebMapContextTags.CONTACT_ORGANIZATION, contactOrganization.trim());
							// </ContactOrganization>
							}

						xml.closeTag();
						// </ContactPersonPrimary>
					}
					xml.closeTag();
					// </ContactInformation>

					// <ContactPosition>
					if (contactPosition != null) {
						xml.writeTag(WebMapContextTags.CONTACT_POSITION, contactPosition.trim());
					// </ContactPosition>
					}

					// <ContactAddress>
					if (address != null || city != null || stateOrProvince != null || postCode != null || country != null) {
						xml.openTag(WebMapContextTags.CONTACT_ADDRESS);

							// <AddressType>
							xml.writeTag(WebMapContextTags.ADDRESS_TYPE, "Postal");
							// </AddressType>

							// <Address>
							if (address != null) {
								xml.writeTag(WebMapContextTags.ADDRESS, address.trim());
							// </Address>
							}

							// <City>
							if (city != null) {
								xml.writeTag(WebMapContextTags.CITY, city.trim());
							// </City>
							}

							// <StateOrProvince>
							if (stateOrProvince != null) {
								xml.writeTag(WebMapContextTags.STATE_OR_PROVINCE, stateOrProvince.trim());
							// </StateOrProvince>
							}

							// <PostCode>
							if (postCode != null) {
								xml.writeTag(WebMapContextTags.POSTCODE, postCode.trim());
							// </PostCode>
							}

							// <Country>
							if (country != null) {
								xml.writeTag(WebMapContextTags.COUNTRY, country.trim());
							}
							// </Country>
						xml.closeTag();
					}
					// </ContactAddress>

					// <ContactVoiceTelephone>
					if (telephone != null) {
						xml.writeTag(WebMapContextTags.CONTACT_VOICE_TELEPHONE, telephone.trim());
					// </ContactVoiceTelephone>
					}

					// <ContactFacsimileTelephone>
					if (fax != null) {
						xml.writeTag(WebMapContextTags.CONTACT_FACSIMILE_TELEPHONE, fax.trim());
					// </ContactFacsimileTelephone>
					}

					// <ContactElectronicMailAddress>
					if (email != null) {
						xml.writeTag(WebMapContextTags.CONTACT_ELECTRONIC_MAIL_ADDRESS, email.trim());
					// </ContactElectronicMailAddress>
					}
				}
				// <KeywordList>
				xml.openTag(WebMapContextTags.KEYWORD_LIST);
				if (keywordList != null) {
					for (int i = 0; i < keywordList.size(); i++) {
						xml.writeTag(WebMapContextTags.KEYWORD, ((String) keywordList.get(i)).trim());
					}
				} else {
					xml.writeTag(WebMapContextTags.KEYWORD, "");
				}
				xml.closeTag();

				// </KeywordList>
			xml.closeTag();
			// </General>

			// <LayerList>
			xml.openTag(WebMapContextTags.LAYER_LIST);
			for (int i = 0; i < layersToExport.size(); i++) {
				xml.writeRaw(((FLyrWMS) layersToExport.get(i)).toMapContext(fileVersion));
			}
			xml.closeTag();
			// </LayerList>
		xml.closeTag();
		// </ViewContext>

		return xml.getXML();
	}

	/**
	 * Exports the ProjectView passed as parameter to Web Map Context XML compliant
	 * with the defined specifications for the version set in the fileVersion field.
	 * @param ProjectView to be exported
	 * @return String containing the XML
	 */
	public String toXML(ProjectView v) {
		if (fileVersion.equals("1.1.0") ||
				fileVersion.equals("1.0.0") ||
				fileVersion.equals("0.1.4")) {
			return createMapContext1_1_0(v);
		}
		return null;
	}


	public static ArrayList getExportableLayers(ProjectView v) {
		ArrayList list = new ArrayList();
		FLayers lyrs = v.getMapContext().getLayers();
		list.addAll(_getExportableLayers(lyrs));
		return list;
	}

	private static ArrayList _getExportableLayers(FLayer lyr) {
		ArrayList list = new ArrayList();
		if (checkType(lyr)) {
			list.add(lyr);
		} else {
			if (lyr instanceof FLayers) {
				FLayers lyrs = (FLayers) lyr;
				for (int i = 0; i < lyrs.getLayersCount(); i++) {
					list.addAll(_getExportableLayers(lyrs.getLayer(i)));
				}
			}
		}
		return list;
	}

	/**
	 * Checks if the layer is supported by the WebMapContext
	 * @param lyr
	 * @return
	 */
	private static boolean checkType(FLayer lyr) {
		return supportedLayers.contains(lyr.getClass());
	}

	/**
	 * Searches in the layer tree of the TOC for an ocurrence of any
	 * exportable layer and returns true if so, or false otherwise.
	 * @param layer
	 * @return
	 */
	public static boolean containsExportableLayers(FLayer layer) {
		if (checkType(layer)) {
			return true;
		}

		if (layer instanceof FLayers) {
			FLayers layers = (FLayers) layer;
			for (int i = 0; i < layers.getLayersCount(); i++) {
				FLayer lyr = layers.getLayer(i);
				if (containsExportableLayers(lyr)) {
					return true;
				}

			}
		}
		return false;
	}
}
