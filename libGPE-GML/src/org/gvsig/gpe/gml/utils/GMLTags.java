package org.gvsig.gpe.gml.utils;

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
 * $Id: GMLTags.java 397 2008-01-24 09:10:48Z jpiera $
 * $Log$
 * Revision 1.13  2007/05/24 07:29:47  csanchez
 * AÃ±adidos Alias GML2
 *
 * Revision 1.12  2007/05/15 10:39:39  jorpiell
 * Add the number of decimals property
 *
 * Revision 1.11  2007/05/15 09:35:09  jorpiell
 * the tag names cant have blanc spaces
 *
 * Revision 1.10  2007/05/15 07:31:06  jorpiell
 * Add the geometryProperties tags
 *
 * Revision 1.9  2007/05/14 11:20:25  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.8  2007/05/14 09:52:26  jorpiell
 * Tupes separator tag updated
 *
 * Revision 1.7  2007/05/14 09:30:08  jorpiell
 * Add the fid tag
 *
 * Revision 1.6  2007/05/07 12:58:42  jorpiell
 * Add some methods to manage the multigeometries
 *
 * Revision 1.5  2007/04/13 13:16:00  jorpiell
 * Add the multiple geometries
 *
 * Revision 1.4  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 * Revision 1.3  2007/04/12 10:35:04  jorpiell
 * Add the innerboundaryIs
 *
 * Revision 1.2  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.4  2007/01/14 13:12:11  jaume
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
	public static final String GML_NAMESPACE_URI = "http://www.opengis.net/gml";
	public static final String GML_DEAFULT_BLANC_SPACE = "_";	
	public static final int GML_DEAFULT_DECIMAL_DIGITS = 3;
	public static final QName VERSION = new QName(GML_NAMESPACE_URI, "version");
	public static final QName GML_NAME = new QName(GML_NAMESPACE_URI, "name");
	public static final QName GML_DESCRIPTION = new QName(GML_NAMESPACE_URI,"description");
	public static final QName GML_BOUNDEDBY = new QName(GML_NAMESPACE_URI, "boundedBy");
	public static final QName GML_BOX = new QName(GML_NAMESPACE_URI, "Box");
	public static final QName GML_COORDINATES = new QName(GML_NAMESPACE_URI, "coordinates");
	public static final QName GML_COORD = new QName(GML_NAMESPACE_URI, "coord");
	public static final QName GML_X = new QName(GML_NAMESPACE_URI, "X");
	public static final QName GML_Y = new QName(GML_NAMESPACE_URI, "Y");
	public static final QName GML_Z = new QName(GML_NAMESPACE_URI, "Z");
	public static final QName GML_COORDINATES_DECIMAL = new QName(GML_NAMESPACE_URI, "decimal");
	public static final QName GML_COORDINATES_CS = new QName(GML_NAMESPACE_URI, "cs");
	public static final QName GML_COORDINATES_TS = new QName(GML_NAMESPACE_URI, "ts");
	public static final QName GML_SRS_NAME = new QName(GML_NAMESPACE_URI, "srsName");
	public static final QName GML_GID = new QName(GML_NAMESPACE_URI, "gid");
	public static final QName GML_FID = new QName(GML_NAMESPACE_URI, "fid");
	public static final QName GML_ID = new QName(GML_NAMESPACE_URI, "id");
	public static final String GML_NAMESPACE_PREFIX = "gml";
	public static final QName GML_FEATUREMEMBER = new QName(GML_NAMESPACE_URI, "featureMember");
	public static final QName GML_FEATUREMEMBERS = new QName(GML_NAMESPACE_URI, "featureMembers");
	public static final QName GML_FEATURCOLLECTION = new QName(GML_NAMESPACE_URI, "FeatureCollection");
	public static final QName GML_ABSTRACT_FEATURECOLLECTION = new QName(GML_NAMESPACE_URI, "_FeatureCollection");
	
	//Geometries
	public static final QName GML_POINT = new QName(GML_NAMESPACE_URI, "Point");
	public static final QName GML_LINESTRING = new QName(GML_NAMESPACE_URI, "LineString");
	public static final QName GML_LINEARRING = new QName(GML_NAMESPACE_URI, "LinearRing");
	public static final QName GML_POLYGON = new QName(GML_NAMESPACE_URI, "Polygon");
	public static final QName GML_MULTIPOINT = new QName(GML_NAMESPACE_URI, "MultiPoint");
	public static final QName GML_MULTILINESTRING = new QName(GML_NAMESPACE_URI, "MultiLineString");
	public static final QName GML_MULTIPOLYGON = new QName(GML_NAMESPACE_URI, "MultiPolygon");
	public static final QName GML_MULTIGEOMETRY = new QName(GML_NAMESPACE_URI, "MultiGeometry");
	public static final QName GML_POINTPROPERTY = new QName(GML_NAMESPACE_URI, "pointProperty");
	public static final QName GML_LINESTRINGPROPERTY = new QName(GML_NAMESPACE_URI, "lineStringProperty");
	public static final QName GML_POLYGONPROPERTY = new QName(GML_NAMESPACE_URI, "polygonProperty");
	public static final QName GML_GEOMETRYPROPERTY = new QName(GML_NAMESPACE_URI, "geometryProperty");
	public static final QName GML_MULTIPOINTPROPERTY = new QName(GML_NAMESPACE_URI, "multiPointProperty");
	public static final QName GML_MULTILINESTRINGPROPERTY = new QName(GML_NAMESPACE_URI, "multiLineStringProperty");
	public static final QName GML_MULTIPOLYGONPROPERTY = new QName(GML_NAMESPACE_URI, "multiPolygonProperty");
	public static final QName GML_MULTIGEOMETRYPROPERTY = new QName(GML_NAMESPACE_URI, "multiGeometryProperty");
	
	public static final String GML_DEFAULT_TUPLES_SEPARATOR  = "[\n\\s\r\t ]+";
	public static final String GML_DEFAULT_COORDINATES_SEPARATOR = ",";
	public static final String GML_DEFAULT_COORDINATES_DECIMAL = ".";	
	
	public static final QName GML_OUTERBOUNDARYIS = new QName(GML_NAMESPACE_URI, "outerBoundaryIs");
	public static final QName GML_INNERBOUNDARYIS = new QName(GML_NAMESPACE_URI, "innerBoundaryIs");
	public static final QName GML_POINTMEMBER = new QName(GML_NAMESPACE_URI, "pointMember");
	public static final QName GML_LINESTRINGMEMBER = new QName(GML_NAMESPACE_URI, "lineStringMember");
	public static final QName GML_POLYGONMEMBER = new QName(GML_NAMESPACE_URI, "polygonMember");
	public static final QName GML_GEOMETRYMEMBER = new QName(GML_NAMESPACE_URI, "geometryMember");
	
	/*************************************************************
	 * New GML 3 definitions									 *
	 *************************************************************/
	//Atributos de Interpolación
	public static final QName GML_INTERPOLATION = new QName(GML_NAMESPACE_URI, "interpolation");
	public static final String GML_DEFAULT_INTERPOLATION = "linear";
	
	//Atributos de Dimensión (x,y,z) 
	public static final int GML_DEFAULT_DIMENSION = 2;
	public static final QName GML_DIMENSION = new QName(GML_NAMESPACE_URI, "dimension");
	public static final QName GML_SRSDIMENSION = new QName(GML_NAMESPACE_URI, "srsDimension");
	
	//Curve
	public static final QName GML_MULTICURVE = new QName(GML_NAMESPACE_URI, "MultiCurve");
	public static final QName GML_CURVEMEMBER = new QName(GML_NAMESPACE_URI, "curveMember");
	public static final QName GML_CURVE = new QName(GML_NAMESPACE_URI, "Curve");
	public static final QName GML_SEGMENTS = new QName(GML_NAMESPACE_URI, "segments");
	public static final QName GML_MULTICURVEPROPERTY = new QName(GML_NAMESPACE_URI, "multiCurveProperty");
	public static final QName GML_CURVEPROPERTY = new QName(GML_NAMESPACE_URI, "curveProperty");
	public static final QName GML_LINESTRINGSEGMENT = new QName(GML_NAMESPACE_URI, "LineStringSegment");
	
	//Surface
	public static final QName GML_MULTISURFACE = new QName(GML_NAMESPACE_URI, "MultiSurface");
	public static final QName GML_SURFACEMEMBER = new QName(GML_NAMESPACE_URI, "surfaceMember");
	public static final QName GML_PATCHES = new QName(GML_NAMESPACE_URI, "patches");
	public static final QName GML_POLYGONPATCH = new QName(GML_NAMESPACE_URI, "PolygonPatch");
	public static final QName GML_EXTERIOR = new QName(GML_NAMESPACE_URI, "exterior");
	public static final QName GML_INTERIOR = new QName(GML_NAMESPACE_URI, "interior");
	public static final QName GML_SURFACE = new QName(GML_NAMESPACE_URI, "Surface");
	
	//Coords
	//Sustituye al coordinates
	public static final QName GML_POSLIST = new QName(GML_NAMESPACE_URI, "posList");
	public static final QName GML_POS = new QName(GML_NAMESPACE_URI, "pos");
	
	//Envelope (contenido en el BoundedBy)
	public static final QName GML_ENVELOPE = new QName(GML_NAMESPACE_URI, "Envelope");
	public static final QName GML_LOWERCORNER = new QName(GML_NAMESPACE_URI, "lowerCorner");
	public static final QName GML_UPPERCORNER = new QName(GML_NAMESPACE_URI, "upperCorner");
	
	
	/***************************************************************/
	
	//Point Alias
	public static final QName GML_CENTEROF = new QName(GML_NAMESPACE_URI, "centerOf");
	public static final QName GML_LOCATION = new QName(GML_NAMESPACE_URI, "location");
	public static final QName GML_POSITION = new QName(GML_NAMESPACE_URI, "position");
	
	//Polygon Alias
	public static final QName GML_EXTENTOF = new QName(GML_NAMESPACE_URI, "extentOf");
	public static final QName GML_COVERAGE = new QName(GML_NAMESPACE_URI, "coverage");
	
	//LineString Alias
	public static final QName GML_EDGEOF = new QName(GML_NAMESPACE_URI, "edgeOf");
	public static final QName GML_CENTERLINEOF = new QName(GML_NAMESPACE_URI, "centerLineOf");
	
	//MultiPoint
	public static final QName GML_MULTICENTEROF = new QName(GML_NAMESPACE_URI, "multiCenterOf");
	public static final QName GML_MULTILOCATION = new QName(GML_NAMESPACE_URI, "multiLocation");
	public static final QName GML_MULTIPOSITION = new QName(GML_NAMESPACE_URI, "multiPosition");
	
	//MultiLine
	public static final QName GML_MULTIEDGEOF = new QName(GML_NAMESPACE_URI, "multiEdgeOf");
	public static final QName GML_MULTICENTERLINEOF = new QName(GML_NAMESPACE_URI, "multiCenterLineOf");
	
	//MultiPolygon
	public static final QName GML_MULTIEXTENTOF = new QName(GML_NAMESPACE_URI, "multiExtentOf");
	public static final QName GML_MULTICOVERAGE = new QName(GML_NAMESPACE_URI, "multiCoverage");
	
	//srs
	public static final String SRS = "http://www.opengis.net/gml/srs/";
	public static final String SRS_EPSG_NAME = "EPSG";
	public static final String SRS_EPSG = "epsg.xml";
	public static final String SRS_UNKNOWN = "unknown";
	
}
