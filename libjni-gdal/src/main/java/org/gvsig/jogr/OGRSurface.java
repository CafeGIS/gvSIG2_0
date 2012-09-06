/**********************************************************************
 * $Id: OGRSurface.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRSurface.java
 * Project:  JGDAL. Interface java to OGR (Frank Warmerdam).
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
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRSurface extends OGRGeometry{
	
	private native void FreeOGRSurface( long cPtr );
	
	public OGRSurface(){}
	
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRSurface de C. 
	 */
	
	public OGRSurface(long cPtr){
		super(cPtr);
	}
	
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
			
		FreeOGRSurface(cPtr);
	}
}