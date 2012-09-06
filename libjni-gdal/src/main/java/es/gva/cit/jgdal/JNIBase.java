/**********************************************************************
 * $Id: JNIBase.java 15691 2007-10-31 10:49:53Z nbrodin $
 *
 * Name:     JNIBase.java
 * Project:  JGDAL. Interfaz java to gdal (Frank Warmerdam).
 * Purpose:  Base class for classes that use JNI.
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
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


/**
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

package es.gva.cit.jgdal;



public class JNIBase{
	
	protected long cPtr;
	
	private native int getRasterBandXSizeNat(long cPtr);
	private native int getRasterBandYSizeNat(long cPtr);
	private native int getOverviewCountNat(long cPtr);
	private native int getBlockXSizeNat(long cPtr);
	private native int getBlockYSizeNat(long cPtr);
	private native int getRasterXSizeNat(long cPtr);
	private native int getRasterYSizeNat(long cPtr);
	private native int getRasterCountNat(long cPtr);
	private native int getGCPCountNat(long cPtr);
	private native int getRasterDataTypeNat(long cPtr);
	private native int getDriverCountNat(long cPtr);
	private native int getLayerCountNat(long cPtr);
	
	
	 /**
	 * Función que sirve como base para funcionalidades de gdal que admiten como parámetro un entero y devuelven un entero.
	 * 
	 * @throws GdalException.
	 * @param msg1	Mensaje de error que se muestra cuando el puntero a objeto pasado es vacio.
	 * @param msg2	Mensaje de error que se muestra cuando el resultado de la llamada a la función de gdal es menor o igual que 0.
	 */
	 
	 
	protected int baseSimpleFunctions(int n,String msg1,String msg2)throws GdalException{
			
		int res = 0;
			
		switch(n){
			case 0: res = getRasterBandXSizeNat(cPtr);break;
			case 1: res = getRasterBandYSizeNat(cPtr);break;
			case 2: res = getOverviewCountNat(cPtr);break;
			case 3: res = getBlockXSizeNat(cPtr);break;
			case 4: res = getBlockYSizeNat(cPtr);break;
			case 5: res = getRasterXSizeNat(cPtr);break;
			case 6: res = getRasterYSizeNat(cPtr);break;
			case 7: res = getRasterCountNat(cPtr);break;
			case 8: res = getGCPCountNat(cPtr);break;
			case 9: res = getRasterDataTypeNat(cPtr);break;
		}

		if (res < 0)
			throw new GdalException(msg2);
		else
			return res;
	}
	
	static {
		System.loadLibrary("jgdal2.0.0");
	}
}
