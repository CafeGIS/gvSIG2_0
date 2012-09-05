package org.gvsig.xmlschema.utils;
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * $Id: SchemaTags.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.4  2007/07/02 09:57:35  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.3  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.2  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.3  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * This class contains the xml schema constants
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SchemaTags {
	public static final String SCHEMA_ROOT = "xs:schema";
	public static final String XLINK_NS_URI = "http://www.w3.org/1999/xlink";
	public static final String XMLNS_NS_URI = "http://www.w3.org/2000/xmlns/";
	public static final String XSD_NS_URI = "http://www.w3.org/2001/XMLSchema";
	public static final String XS_NS_URI = "http://www.w3.org/2001/XMLSchema";
	public static final String XSI_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String GML_NS_URI = "http://www.opengis.net/gml";
	public static final String GML_NS = "gml";
	public static final String XMLNS_NS = "xmlns";
	public static final String XSD_NS = "xs";
	public static final String XSI_NS = "xsi";
	public static final String SCHEMA_LOCATION_ATTR_NAME = "schemaLocation";
	public static final String XS_NS = "xs";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String TARGET_NAMESPACE = "targetNamespace";	
	public static final String SIMPLE_TYPE = "simpleType";
	public static final String COMPLEX_TYPE = "complexType";
	public static final String ELEMENT = "element";
	public static final String COMPLEX_CONTENT = "complexContent";
	public static final String SIMPLE_CONTENT = "simpleContent";
	public static final String EXTENSION = "extension";
	public static final String RESTRICTION = "restriction";
	public static final String CHOICE = "choice";
	public static final String GROUP = "group";
	public static final String ALL = "all";
	public static final String SEQUENCE = "sequence";
	public static final String NILLABLE = "nillable";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String MIN_OCCURS = "minOccurs";
	public static final String MAX_OCCURS = "maxOccurs";
	public static final String ELEMENTFORMDEFAULT = " elementFormDefault";
	public static final String UNBOUNDED = "unbounded";
	public static final String SUBSTITUTIONGROUP = "substitutionGroup";
}
