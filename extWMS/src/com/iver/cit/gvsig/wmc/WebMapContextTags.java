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
* $Id: WebMapContextTags.java 5409 2006-05-24 16:37:35Z jaume $
* $Log$
* Revision 1.3  2006-05-24 16:36:28  jaume
* *** empty log message ***
*
* Revision 1.2  2006/05/12 07:47:39  jaume
* removed unnecessary imports
*
* Revision 1.1  2006/05/03 07:51:21  jaume
* *** empty log message ***
*
* Revision 1.5  2006/05/02 15:58:20  jaume
* Few better javadoc
*
* Revision 1.4  2006/05/02 15:57:44  jaume
* Few better javadoc
*
* Revision 1.3  2006/04/21 10:27:32  jaume
* exporting now supported
*
* Revision 1.2  2006/04/19 16:34:29  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/19 07:57:29  jaume
* *** empty log message ***
*
* Revision 1.2  2006/04/12 17:10:53  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/04 14:22:22  jaume
* Now exports MapContext (not yet tested)
*
*
*/
package com.iver.cit.gvsig.wmc;

/**
 * WebMapContextTags is a class with only static public fields containing the
 * String constants of the WebMapContext XML document.
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class WebMapContextTags {
	private WebMapContextTags() {};
	
	public static final String VIEW_CONTEXT = "ViewContext";
	public static final String VIEW_CONTEXT_0_1_4 = "WMS_Viewer_Context";
	public static final String VERSION = "version";
	public static final String ID = "id";
	public static final String XMLNS = "xmlns";
	public static final String XMLNS_XLINK = "xmlns:xlink";
	public static final String XMLNS_XLINK_VALUE = "http://www.w3.org/1999/xlink";
	public static final String XMLNS_VALUE = "http://www.opengis.net/context"; // "http://www.opengeospatial.net/context"
	public static final String XMLNS_XSI = "xmlns:xsi"; 
	public static final String XMLNS_XSI_VALUE = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSI_SCHEMA_LOCATION = "xsi:schemaLocation";
	public static final String XSI_SCHEMA_LOCATION_VALUE = "http://www.opengeospatial.net/context context.xsd";
	public static final String GENERAL = "General";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String WINDOW = "Window";
	public static final String SRS = "SRS";
	public static final String X_MIN = "minx";
	public static final String Y_MIN = "miny";
	public static final String X_MAX = "maxx";
	public static final String Y_MAX = "maxy";
	public static final String BOUNDING_BOX = "BoundingBox";
	public static final String TITLE = "Title";
	public static final String LAYER_LIST = "LayerList";
	public static final String LAYER = "Layer";
	public static final String QUERYABLE = "queryable";
	public static final String HIDDEN = "hidden";
	public static final String SERVICE = "service";
	public static final String WMS = "WMS";
	public static final String SERVER_TITLE = "title";
	public static final String SERVER = "Server";
	public static final String XLINK_TYPE = "xlink:type";
	public static final String XLINK_HREF = "xlink:href";
	public static final String ONLINE_RESOURCE = "OnlineResource";
	public static final String NAME = "Name";
	public static final String ABSTRACT = "Abstract";
	public static final String FORMAT_LIST = "FormatList";
	public static final String CURRENT = "current";
	public static final String FORMAT = "Format";
	public static final String STYLE_LIST = "StyleList";
	public static final String STYLE = "Style";
	public static final String KEYWORD_LIST = "KeywordList";
	public static final String KEYWORD = "Keyword";
	public static final String LOGO_URL = "LogoURL";
	public static final String DESCRIPTION_URL = "DescriptionURL";
	public static final String CONTACT_INFORMATION = "ContactInformation";
	public static final String CONTACT_PERSON_PRIMARY = "ContactPersonPrimary";
	public static final String CONTACT_PERSON = "ContactPerson";
	public static final String CONTACT_ORGANIZATION = "ContactOrganization";
	public static final String CONTACT_POSITION = "ContactPosition";
	public static final String CONTACT_ADDRESS = "ContactAddress";
	public static final String ADDRESS_TYPE = "AddressType";
	public static final String ADDRESS = "Address";
	public static final String CITY = "City";
	public static final String STATE_OR_PROVINCE = "StateOrProvince";
	public static final String POSTCODE = "PostCode";
	public static final String COUNTRY = "Country";
	public static final String CONTACT_VOICE_TELEPHONE = "ContactVoiceTelephone";
	public static final String CONTACT_FACSIMILE_TELEPHONE = "ContactFacsimileTelephone";
	public static final String CONTACT_ELECTRONIC_MAIL_ADDRESS = "ContactElectronicMailAddress";
	public static final String DIMENSION_LIST = "DimensionList";
	public static final String LEGEND_URL = "LegendURL";
	
}
