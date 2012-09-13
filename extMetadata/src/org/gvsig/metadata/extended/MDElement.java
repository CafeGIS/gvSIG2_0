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

package org.gvsig.metadata.extended;

import java.util.Locale;

import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;

public interface MDElement {
	
	/**
	 * Retrieves the identifier of the MDElement object
	 * @return	a String which identifies the object 
	 */
	public String getId();
	
	/**
	 * Retrieves the value of the MDElement object
	 * @return	an Object containing the value of the object
	 */
	public Object getValue();
	
	/**
	 * Retrieves the type of the MDElement object
	 * @return	a MDElementDefinition object containing data about the object
	 */
	public MDElementDefinition getType();
	
	/**
	 * Retrieves the Locale value of the MDElement object
	 * @return	a Locale object which represents a specific geographical, political or cultural region
	 */
	public Locale getLocale();
	
	/**
	 * Retrieves the MDElement name using its MDElementDefinition
	 * @return	the name of the MDElement object
	 */
	public String getName();
	
	/**
	 * Retrieves the father of the Metadata object, or null if it doesn't have.
	 * @return			a ExtendedMetadata object or null
	 */
	public Object getFather();
	
	/**
	 * Changes the identifier of the object
	 * @param 	id	the new identifier of the MDElement object
	 */
	public void setId(String id);
	
	/**
	 * Changes the value of the object
	 * @param 	value	the new value of the MDElement object
	 */
	public void setValue(Object value);
	
	/**
	 * Changes the type of the object
	 * @param 	type	the new type of the MDElement object
	 */
	public void setType(MDElementDefinition type);
	
	/**
	 * Changes the Locale value of the object
	 * @param 	locale	the new Locale value of the MDElement object
	 */
	public void setLocale(Locale locale); 
	
	/**
	 * Changes father of the object
	 * @param 	father	the new father value of the MDElement object
	 */
	public void setFather(Object father); 
	
	/**
	 * Checks if the ExtendedMetadata has father
	 * @return			a boolean value which indicates if the object has father
	 */
	public boolean hasFather();
	
	/**
	 * Retrieves the deep level in the Metadata tree, 0 for the root.
	 * @return			an integer value which indicates the level.
	 */
	public int getLevel();
	
	/**
	 * Retrieves a String representing the Element object and its value.
	 * @return			a String.
	 */
	public String toString();
	
}
