/**********************************************************************
 * $Id: OGRTools.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRTools.java
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
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRTools extends JNIBase{
	
	private native static long OGRCreateCoordinateTransformationNat( long poSource, long poTarget);
	
	/**
	 * 
	 */
	
	public static OGRCoordinateTransformation OGRCreateCoordinateTransformation( OGRSpatialReference poSource,
            																OGRSpatialReference poTarget )throws OGRException{
		if(poSource.getPtro() ==0  || poTarget.getPtro() ==0)
			throw new OGRException("Error en OGRCreateCoordinateTransformation(). Los parámetros no tienen una referencia correcta.");
		
		long ptr_cct = OGRCreateCoordinateTransformationNat(poSource.getPtro(), poTarget.getPtro());
		
		if(ptr_cct == 0)
			throw new OGRException("Error en OGRCreateCoordinateTransformation(). No se ha podido obtener una referencia valida al objeto OGRCoordinateTransformation.");
		
		return new OGRCoordinateTransformation(ptr_cct);
		
	}
}