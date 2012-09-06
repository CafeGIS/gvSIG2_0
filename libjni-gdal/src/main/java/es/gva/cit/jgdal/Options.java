/**********************************************************************
 * $Id: Options.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     Options.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:   
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

package es.gva.cit.jgdal;


public class Options{
	
	String[] vars;
	String[] datos;
	int longitud;
	int nopc;
	
	/**
	 *
	 */
	
   	public Options(int nopciones){
   		vars=new String[nopciones];
   		datos=new String[nopciones];
   		longitud=nopciones;
   		nopc=0;
   	}
	
   	/**
   	 * 
   	 */
   	
   	public void addOption(String var, String dato){
   		vars[nopc]=var;
		datos[nopc]=dato;
		nopc++;
   	}
   	
   	/**
   	 * 
   	 */
   	public int getSize(){
   		return longitud;
   	}
 }