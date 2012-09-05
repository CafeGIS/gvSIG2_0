package org.gvsig.gpe.kml.utils;

import javax.xml.namespace.QName;

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
 * $Id: KmlTags.java 136 2007-05-16 15:54:36Z jorpiell $
 * $Log$
 * Revision 1.10  2007/05/16 15:54:20  jorpiell
 * Add elements support
 *
 * Revision 1.9  2007/05/15 12:37:45  jorpiell
 * Add multyGeometries
 *
 * Revision 1.8  2007/05/09 08:36:24  jorpiell
 * Add the bbox to the layer
 *
 * Revision 1.7  2007/05/08 09:28:17  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.6  2007/05/08 07:53:08  jorpiell
 * Add comments to the writers
 *
 * Revision 1.5  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 * Revision 1.4  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.3  2007/04/14 16:08:07  jorpiell
 * Kml writing support added
 *
 * Revision 1.2  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * Añadido el driver de KML
 *
 *
 */
/**
 * Just some KML tags retreived from its specification
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class Kml2_1_Tags {
	public static final String NAMESPACE_21 = "http://earth.google.com/kml/2.1";
	
	public static final QName ROOT = new QName(NAMESPACE_21, "kml");
	
	public static final QName PLACEMARK = new QName(NAMESPACE_21, "Placemark");
	public static final QName OPEN = new QName(NAMESPACE_21, "open");
	public static final QName GROUNDOVERLAY = new QName(NAMESPACE_21, "GroundOverlay");
		
	public static final QName POINT = new QName(NAMESPACE_21, "Point");
	public static final QName COORDINATES = new QName(NAMESPACE_21, "coordinates");
	public static final QName LINESTRING = new QName(NAMESPACE_21, "LineString");
	public static final QName LINEARRING = new QName(NAMESPACE_21, "LinearRing");
	public static final QName POLYGON = new QName(NAMESPACE_21, "Polygon");	
	public static final QName OUTERBOUNDARYIS = new QName(NAMESPACE_21, "outerBoundaryIs");	
	public static final QName INNERBOUNDARYIS = new QName(NAMESPACE_21, "innerBoundaryIs");	
	public static final QName MULTIGEOMETRY = new QName(NAMESPACE_21, "MultiGeometry");
	
	public static final QName DOCUMENT = new QName(NAMESPACE_21, "Document");
	public static final QName FOLDER = new QName(NAMESPACE_21, "Folder");
	public static final QName STYLE = new QName(NAMESPACE_21, "Style");
		
	//Separator between coordinates
	public static final String COORDINATES_SEPARATOR = ",";
	public static final String TUPLES_SEPARATOR = "\n";
		
	//PlaceMark params
	public static final QName NAME = new QName(NAMESPACE_21, "name");
	public static final QName DESCRIPTION = new QName(NAMESPACE_21, "description");
	public static final QName VISIBILITY = new QName(NAMESPACE_21, "visibility");
	public static final QName LOOKAT = new QName(NAMESPACE_21, "LookAt");
	public static final QName STYLEURL = new QName(NAMESPACE_21, "styleUrl");
	
	//Param added to each feature to create the legend
	public static final String FOLDER_NAME = "Folder Name";
	
	//Name for the default legend
	public static final String DEFAULT_LEGEND = "Default";
	
	//LookAt params
	public static final QName LONGITUDE = new QName(NAMESPACE_21, "longitude");
	public static final QName LATITUDE = new QName(NAMESPACE_21, "latitude");
	public static final QName ALTITUDE = new QName(NAMESPACE_21, "altitude");
	public static final QName RANGE = new QName(NAMESPACE_21, "range");
	public static final QName TILT = new QName(NAMESPACE_21, "tilt");
	public static final QName HEADING = new QName(NAMESPACE_21, "heading");
	
	//LatLonBox params
	public static final QName LATLONALTBOX = new QName(NAMESPACE_21, "LatLonAltBox");
	public static final QName LATLONBOX = new QName(NAMESPACE_21, "LatLonBox");
	public static final QName REGION = new QName(NAMESPACE_21, "Region");
	public static final QName NORTH = new QName(NAMESPACE_21, "north");
	public static final QName SOUTH = new QName(NAMESPACE_21, "south");
	public static final QName EAST = new QName(NAMESPACE_21, "east");
	public static final QName WEST = new QName(NAMESPACE_21, "west");
	public static final QName MINALTITUDE = new QName(NAMESPACE_21, "minAltitude");
	public static final QName MAXALTITUDE = new QName(NAMESPACE_21, "maxAltitude");
	public static final QName ALTITUDEMODE = new QName(NAMESPACE_21, "altitudeMode");
	public static final QName ROTATION = new QName(NAMESPACE_21, "rotation");
		
	//Others
	public static final String UNKNOWN_VERSION = "UnknownVersion";
	public static final String DEFAULT_SRS = "EPSG:4326";
	public static final QName ID = new QName(NAMESPACE_21,"id"); 
	public static final QName GEOMETRY_ID = new QName(NAMESPACE_21,"id"); 
	public static final QName METADATA = new QName(NAMESPACE_21, "Metadata");
	
	//To be used in the elements with blanc spaces
	public static final String DEFAULT_BLANC_SPACE = "_";
}
