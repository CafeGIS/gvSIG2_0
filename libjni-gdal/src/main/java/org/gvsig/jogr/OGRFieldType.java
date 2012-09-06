/**********************************************************************
 * $Id: OGRFieldType.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRFieldType.java
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

public class OGRFieldType{
	
	private int tipo;
	
	/**
	 * Asigna el tipo
	 * @param tipo	tipo
	 */
	
	public void setType(int tipo){this.tipo=tipo;}
	
	/**
	 * Devuelve el tipo
	 * @return tipo
	 */
	
	public int getType(){return tipo;}
	
	/**
	 * Devuelve el tipo en formato cadena
	 * @return tipo
	 */
	
	public String getStringType(){
		switch(tipo){
		 case 0:return new String("Integer");
		 case 1:return new String("IntegerList");
		 case 2:return new String("Real");
		 case 3:return new String("RealList");
		 case 4:return new String("String");
		 case 5:return new String("StringList");
		 case 6:return new String("WideString");
		 case 7:return new String("WideStringList");
		 case 8:return new String("Binary");
		}
		return null;
	}

}