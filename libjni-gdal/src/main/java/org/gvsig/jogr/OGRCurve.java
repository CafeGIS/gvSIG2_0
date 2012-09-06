/**********************************************************************
 * $Id: OGRCurve.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRCurve.java
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

public class OGRCurve extends JNIBase{
	
	private native double getLengthNat( long cPtr );
	private native void startPointNat( long cPtr, OGRPoint point);
	private native void endPointNat( long cPtr, OGRPoint point);
	private native int  getIsClosedNat( long cPtr );
	private native void valueNat( long cPtr,  double d, OGRPoint point );
	private native void FreeOGRCurveNat(long cPtr);
		
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRCurve de C. 
	 */
		
	public OGRCurve(long cPtr){
		this.cPtr=cPtr;
	}		
		
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if((cPtr == 0) || (cPtr == -1))
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRCurveNat(cPtr);
	}
	 
	/**
	 * 
	 */
	
	public double getLength()throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public void startPoint(OGRPoint point)throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void endPoint(OGRPoint point)throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public int  getIsClosed()throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public void value( double d, OGRPoint point )throws OGRException{
		
	}
	
	
	
	
}