/**********************************************************************
 * $Id: OGRMultiLineString.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRMultiLineString.java
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

public class OGRMultiLineString extends JNIBase{
	
	private native void FreeOGRMultiLineStringNat(long cPtr);
	private native String getGeometryTypeNat(long cPtr);//return OGRwkbGeometryType
	private native OGRGeometry cloneNat(long cPtr);
	private native int importFromWktNat(long cPtr, String[] wkt );//Excepciones
	private native int exportToWktNat(long cPtr, String[] wkt );//Excepciones
	private native int addGeometryDirectlyNat(long cPtr, OGRGeometry geom );//Excepciones
	
	
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRMultiLineString de C. 
	 */
		
	public OGRMultiLineString(long cPtr){
		this.cPtr=cPtr;
	}		
		
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRMultiLineStringNat(cPtr);
	}
	 
//	Non standard (OGRGeometry).
	
	/**
	 * 
	 */
	
	public String getGeometryName()throws OGRException{
		return null;
	}

	/**
	 * 
	 */

	public String getGeometryType()throws OGRException{
		return null;
	}

	/**
	 * 
	 */

	public OGRGeometry cloneMultiPolygon()throws OGRException{
		return null;
	}

	/**
	 * 
	 */

	public void importFromWkt( String[] wkt )throws OGRException{//Excepciones
		
	}

	/**
	 * 
	 */

	public void exportToWkt( String[] wkt )throws OGRException{//Excepciones
		
	}

    // Non standard

	/**
	 * 
	 */

	public void addGeometryDirectly( OGRGeometry geom )throws OGRException{//Excepciones
		
	}
	
	
}