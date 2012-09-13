/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Informáticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/

package org.gvsig.metadata.extended.exchanger;

import org.gvsig.metadata.Metadata;



public interface MDExchanger {
	
	/**
	 * Creates a Metadata object from the data of a file
	 * @param 	file		the path of the file where the metadata is located
	 * @return				an object which implements the Metadata interface 
	 */
	public Metadata importMD(Metadata md, String file);
	
	/**
	 * Transforms the metadata passed as a parameter from its current format to another one, also passed as a parameter
	 * @param 	md		the metadata is obtained from this Metadata object
	 * @param 	format	the format to which the metadata transformation is done  
	 * @return			a String containing the metadata of 'md' in the format chosen  
	 */
	public String exportMD(Metadata md, String format);
}