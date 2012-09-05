package org.gvsig.gpe.gml.utils;

import java.util.Hashtable;

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
 * $Id: GMLGeometries.java 202 2007-11-27 12:00:11Z jpiera $
 * $Log$
 * Revision 1.1  2007/05/18 10:41:01  csanchez
 * ActualizaciÃ³n libGPE-GML eliminaciÃ³n de clases inecesarias
 *
 * Revision 1.2  2007/05/14 09:31:33  jorpiell
 * Add a new method to compare geometries
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2006/12/22 11:25:04  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 *
 */

/************************************************************
 * class < Geometries >										*
 * It contains the standard tags specified in GML 2.x		*
 * Also, it has functions to parse geometry tags.			*
 * This class help us with the "gml" namespace.				*
 * 															*
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)	*
 ************************************************************/
public class GMLGeometries{
	//It has all the drawable geometries
	private static Hashtable gmlTags = new Hashtable();
	//It has all the tags of GML that it specifies a feature
	//private static Hashtable features = new Hashtable();
	
	static{
		//complex geometry elements
		gmlTags.put("_Geometry","");
		gmlTags.put("_GeometryCollection","");
		gmlTags.put("geometryMember","");
		gmlTags.put("pointMember","");
		gmlTags.put("lineStringMember","");
		gmlTags.put("polygonMember","");
		gmlTags.put("outerBoundaryIs","");
		gmlTags.put("innerBoundaryIs","");
		//primitive geometry elements
		gmlTags.put("Point","");
		gmlTags.put("LineString","");
		gmlTags.put("LinearRing","");
		gmlTags.put("Polygon","");
		gmlTags.put("Box","");
		//aggregate geometry elements
		gmlTags.put("MultiGeometry","");
		gmlTags.put("MultiPoint","");
		gmlTags.put("MultiLineString","");
		gmlTags.put("MultiPolygon","");
		//coordinate elements
		gmlTags.put("coord","");
		gmlTags.put("X","");
		gmlTags.put("Y","");
		gmlTags.put("Z","");
		gmlTags.put("coordinates","");
		//this attribute gives the location where an element is defined
		gmlTags.put("remoteSchema","");
		//features
		gmlTags.put("_Feature","");
		gmlTags.put("_FeatureCollection","");
		gmlTags.put("featureMember","");
		//some basic geometric properties of features
		gmlTags.put("_geometryProperty","");
		gmlTags.put("geometryProperty","");
		gmlTags.put("boundedBy","");
		gmlTags.put("pointProperty","");
		gmlTags.put("polygonProperty","");
		gmlTags.put("lineStringProperty","");
		gmlTags.put("multiPointProperty","");
		gmlTags.put("multiLineStringProperty","");
		gmlTags.put("multiPolygonProperty","");
		gmlTags.put("multiGeometryProperty","");
		//common aliases for geometry properties
		gmlTags.put("location","");
		gmlTags.put("centerOf","");
		gmlTags.put("position","");
		gmlTags.put("extentOf","");
		gmlTags.put("coverage","");
		gmlTags.put("edgeOf","");
		gmlTags.put("centerLineOf","");
		gmlTags.put("multiLocation","");
		gmlTags.put("multiCenterOf","");
		gmlTags.put("multiPosition","");
		gmlTags.put("multiCenterLineOf","");
		gmlTags.put("multiEdgeOf","");
		gmlTags.put("multiCoverage","");
		gmlTags.put("multiExtentOf","");
		//common feature descriptors
		gmlTags.put("description","");
		gmlTags.put("name","");
	
		/**********************************************************
		 * Added geometries for GML 3 SFP(Simple Feature Profile) *
		 **********************************************************/
		gmlTags.put("MultiPoint","");
		gmlTags.put("MultiCurve","");
		gmlTags.put("MultiSurface","");
		gmlTags.put("pointMember","");
		gmlTags.put("surfaceMember","");
		gmlTags.put("Curve","");
		gmlTags.put("segments","");
		gmlTags.put("curveMember","");
		gmlTags.put("LineStringSegment","");
		gmlTags.put("patches","");
		gmlTags.put("PolygonPatch","");
		gmlTags.put("Surface","");
		gmlTags.put("Polygon","");
		gmlTags.put("exterior","");
		gmlTags.put("interior","");
		gmlTags.put("LinearRing","");
		gmlTags.put("Point","");
		gmlTags.put("LineString","");
		gmlTags.put("pos","");
		gmlTags.put("posList","");
		gmlTags.put("Envelope","");
		gmlTags.put("boundedBy","");
		gmlTags.put("description","");
		gmlTags.put("name","");
		gmlTags.put("curveProperty","");
		gmlTags.put("surfaceProperty","");
		gmlTags.put("multiCurveProperty","");
		gmlTags.put("multiSurfaceProperty","");
	
	}

	private QName tag;
	
	/**
	 * It search a tag in the geometry hashtable
	 * 	-if it isn't, then returns false.
	 * 	-else it is a GML 2.x stardard geometry and return true
	 * @return 
	 * 
	 * @return boolean
	 **/
	public static boolean isGeometryGML(QName tag){
		return (gmlTags.get(tag.getLocalPart())!=null);
	}
	
	/**
	 * Return if the tag is a geometry
	 * @param tag
	 * XML tag to compare
	 * @return 
	 * true if the tag is a geometry
	 */
	public static boolean isGML(QName tag){
		if (tag.getNamespaceURI() != null){
				 if(tag.getNamespaceURI().equals(GMLTags.GML_NAMESPACE_URI))
					return isGeometryGML(tag);
		}		
		return false;
	}
}
