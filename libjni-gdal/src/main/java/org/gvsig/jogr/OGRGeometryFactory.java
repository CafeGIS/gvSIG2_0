/**********************************************************************
 * $Id: OGRGeometryFactory.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRGeometryFactory.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:   
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
/*Copyright (C) 2004  Nacho Brodin <brodin_ign@gva.es>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gvsig.jogr;


/** 
 * Esta clase representa a una fuente de datos
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRGeometryFactory extends JNIBase{
	
	private native  static int createFromWkbNat( String wkt, OGRSpatialReference spatialref,
            OGRGeometry geom, int i );//Excepciones
	private native static int createFromWktNat( String[] wkt, OGRSpatialReference spatialref,
	            OGRGeometry geom );//Excepciones
	private native static OGRGeometry createFromGMLNat( String gml );
	//private native static OGRGeometry createFromGEOSNat( geos.Geometry * );
	private native static void destroyGeometryNat( OGRGeometry geom );
	private native static OGRGeometry createGeometryNat( String wkb );//param 1=OGRwkbGeometryType
	private native static OGRGeometry forceToPolygonNat( OGRGeometry geom );
	private native static OGRGeometry forceToMultiPolygonNat( OGRGeometry geom );
	private native static OGRGeometry forceToMultiPointNat( OGRGeometry geom );
	private native static OGRGeometry forceToMultiLineStringNat( OGRGeometry geom );
	//private native static geos::GeometryFactory *getGEOSGeometryFactoryNat();
	
	/**
	 * 
	 */
	
	public static void createFromWkb( String wkt, OGRSpatialReference spatialref,
            OGRGeometry geom, int i )throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public static void createFromWkt( String[] wkt, OGRSpatialReference spatialref,
	            OGRGeometry geom )throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public static OGRGeometry createFromGML( String gml )throws OGRException{
		return null;
	}
	//public static OGRGeometry createFromGEOS( geos.Geometry * )throws OGRException{
	
	/**
	 * 
	 */
	
	public static void destroyGeometry( OGRGeometry geom )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public static OGRGeometry createGeometry( String wkb )throws OGRException{//param 1=OGRwkbGeometryType
		return null;
	}
	
	/**
	 * 
	 */
	
	public static OGRGeometry forceToPolygon( OGRGeometry geom )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public static OGRGeometry forceToMultiPolygon( OGRGeometry geom )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public static OGRGeometry forceToMultiPoint( OGRGeometry geom )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public static OGRGeometry forceToMultiLineString( OGRGeometry geom )throws OGRException{
		return null;
	}
	//public static geos::GeometryFactory *getGEOSGeometryFactory()throws OGRException{
	
}