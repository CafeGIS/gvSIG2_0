package org.gvsig.remoteClient.wfs.schema;
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
 * $Id: GMLTags.java 9723 2007-01-14 13:12:11Z jaume $
 * $Log$
 * Revision 1.4  2007-01-14 13:12:11  jaume
 * objects to strings
 *
 * Revision 1.3  2006/12/22 11:25:44  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 * Revision 1.2  2006/08/10 12:37:05  jorpiell
 * Añadido el tag description al GML parser
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 *
 */
/**
 * Class containing a description for all the TAGS defined in 
 * the GML File
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class GMLTags {
	public static final String VERSION = "version";
	public static final String XML_TARGET_NAMESPACE = "targetNamespace";
	public static final String XML_ELEMENT_FORM_DEFAULT = "elementFormDefault";
	public static final String XML_ATTRIBUTE_FORM_DEFAULT = "attributeFormDefault";
	public static final String XML_NAMESPACE = "xmlns";
	public static final String XML_SCHEMA_LOCATION = "schemaLocation";
	public static final String GML_NAME = "name";
	public static final String GML_DESCRIPTION = "description";
	public static final String GML_BOUNDEDBY = "boundedBy";
	public static final String GML_BOX = "Box";
	public static final String GML_COORDINATES = "coordinates";
	public static final String GML_COORD = "coord";
	public static final String GML_X = "X";
	public static final String GML_Y = "Y";
	public static final String GML_COORDINATES_DECIMAL = "decimal";
	public static final String GML_COORDINATES_CS = "cs";
	public static final String GML_COORDINATES_TS = "ts";
	public static final String GML_SRS_NAME = "srsName";
	
	//GML types
	public static final String GML_POINT = "Point";
	public static final String GML_MUNTILINE = "MultiLineString";
}
