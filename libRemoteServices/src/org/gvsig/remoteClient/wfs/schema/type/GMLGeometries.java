package org.gvsig.remoteClient.wfs.schema.type;

import java.util.Hashtable;

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
 * $Id: GMLGeometries.java 9451 2006-12-22 11:25:44Z csanchez $
 * $Log$
 * Revision 1.1  2006-12-22 11:25:04  csanchez
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
	private static Hashtable geometries = new Hashtable();
	//It has all the tags of GML that it specifies a feature
	private static Hashtable features = new Hashtable();
	
	static{
		//complex geometry elements
		geometries.put("_Geometry","");
		geometries.put("_GeometryCollection","");
		geometries.put("geometryMember","");
		geometries.put("pointMember","");
		geometries.put("lineStringMember","");
		geometries.put("polygonMember","");
		geometries.put("outerBoundaryIs","");
		geometries.put("innerBoundaryIs","");
		//primitive geometry elements
		geometries.put("Point","");
		geometries.put("LineString","");
		geometries.put("LinearRing","");
		geometries.put("Polygon","");
		geometries.put("Box","");
		//aggregate geometry elements
		geometries.put("MultiGeometry","");
		geometries.put("MultiPoint","");
		geometries.put("MultiLineString","");
		geometries.put("MultiPolygon","");
		//coordinate elements
		geometries.put("coord","");
		geometries.put("X","");
		geometries.put("Y","");
		geometries.put("Z","");
		geometries.put("coordinates","");
		//this attribute gives the location where an element is defined
		geometries.put("remoteSchema","");
	}
	static{
		//features
		features.put("_Feature","");
		features.put("_FeatureCollection","");
		features.put("featureMember","");
		//some basic geometric properties of features
		features.put("_geometryProperty","");
		features.put("geometryProperty","");
		features.put("boundedBy","");
		features.put("pointProperty","");
		features.put("polygonProperty","");
		features.put("lineStringProperty","");
		features.put("multiPointProperty","");
		features.put("multiLineStringProperty","");
		features.put("multiPolygonProperty","");
		features.put("multiGeometryProperty","");
		//common aliases for geometry properties
		features.put("location","");
		features.put("centerOf","");
		features.put("position","");
		features.put("extentOf","");
		features.put("coverage","");
		features.put("edgeOf","");
		features.put("centerLineOf","");
		features.put("multiLocation","");
		features.put("multiCenterOf","");
		features.put("multiPosition","");
		features.put("multiCenterLineOf","");
		features.put("multiEdgeOf","");
		features.put("multiCoverage","");
		features.put("multiExtentOf","");
		//common feature descriptors
		features.put("description","");
		features.put("name","");
	}

	private String tag;
	
	/**
	 * Class constructor
	 **/
	public GMLGeometries(String actual){
		this.tag = actual;
	}
	
	/**
	 * It search a tag in the both of GML hashtables
	 * 	-if it isn't, then returns false.
	 * 	-else it is a GML 2.x stardard tag and return true
	 * 
	 * @return boolean
	 **/
	public boolean isGML(){
		boolean ok;
		if (isGeometryGML()==true){
			ok=true;
		}
		else if (isFeatureGML()==true){
			ok=true;
		}
		else{
			ok=false;
		}
		return (ok);
	}
	
	/**
	 * It search a tag in the geometry hashtable
	 * 	-if it isn't, then returns false.
	 * 	-else it is a GML 2.x stardard geometry and return true
	 * 
	 * @return boolean
	 **/
	public boolean isGeometryGML(){
		return (geometries.get(tag)!=null);
	}
	
	/**
	 * It search a tag in the feature hashtable
	 * 	-if it isn't, then returns false.
	 * 	-else it is a GML 2.x stardard feature tag and return true
	 * 
	 * @return boolean
	 **/
	public boolean isFeatureGML(){
		return (features.get(tag)!=null);
	}
}
