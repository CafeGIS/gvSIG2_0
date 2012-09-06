/**********************************************************************
 * $Id: OGRException.java 7792 2006-10-03 08:33:34Z nacho $
 *
 * Name:     OGRException.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  Class for exceptions produced into OGR. 
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
 * Es generada cuando los códigos de retorno de las funciones de OGR significan que algo ha ido mal.
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */


public class OGRException extends Exception{

	OGRException(String msg){
		super(msg);
	}
	
	public OGRException(int n, String msg, OGRSpatialReference ORGSpace) {
		if(n == 1)
			System.out.println(msg+"Insuficientes parametros para construir el dato");
		else if(n == 2)
			System.out.println(msg+"Insuficiente memoria para construir el dato");
		else if(n == 3)
			System.out.println(msg+"Geometria no soportada");
		else if(n == 4)
			System.out.println(msg+"Operacion no soportada");
		else if(n == 5)
			System.out.println(msg+"Algun dato esta corrupto");
		else if(n == 6)
			System.out.println(msg+"Fallo");
		else if(n == 7)
			System.out.println(msg+"CRS no soportado");		
	}
}