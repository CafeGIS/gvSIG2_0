/**********************************************************************
 * $Id: OGRLinearRing.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRLinearRing.java
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

public class OGRLinearRing extends OGRLineString{
	
	private native void FreeOGRLinearRingNat(long cPtr);
	private native String getGeometryNameNat(long cPtr);
    private native OGRGeometry cloneLinearRingNat(long cPtr);
	private native void closeRingsNat(long cPtr);
	private native double get_AreaNat(long cPtr);
	private native int importFromWkbNat( long cPtr, String wkt, int i ); //return OGRErr
	private native int exportToWkbNat( long cPtr, boolean wktborder, String wkt ); //return OGRErr. boolean=OGRwkbByteOrder
																
	
	public OGRLinearRing(){}
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRLinearRing de C. 
	 */
		
	public OGRLinearRing(long cPtr){
		super(cPtr);
	}		
		
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRLinearRingNat(cPtr);
	}
	 
	/**
	 * 
	 */
	
	public String getGeometryName()throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
    public OGRGeometry cloneLinearRing()throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
	public int isClockwise()throws OGRException{
		return 0;
	}

	/**
	 * 
	 */
	
	public void closeRings()throws OGRException{
		
	}

	/**
	 * 
	 */
	
	public double get_Area()throws OGRException{
		return 0;
	}

	/**
	 * 
	 */
	
	public int WkbSize() throws OGRException{
		return 0;
	}

	/**
	 * 
	 */
	
	public void importFromWkb( String wkt, int i )throws OGRException{ 
		//return OGRErr
	}

	/**
	 * 
	 */
	
	public void exportToWkb( boolean wktborder, String wkt )throws OGRException{ //return OGRErr. boolean=OGRwkbByteOrder
		
	}
	
	
}