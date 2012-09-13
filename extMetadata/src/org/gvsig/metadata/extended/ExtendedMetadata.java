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

import java.util.Date;
import java.util.Iterator;

import org.gvsig.metadata.Metadata;
import org.gvsig.metadata.exceptions.NamedMetadataNotFoundException;
import org.gvsig.metadata.extended.registry.objects.MDDefinition;


public interface ExtendedMetadata extends Metadata {
	
	/**
	 * Retrieves a MDElement from the ExtendedMetadata object
	 * @param	name	the name which identifies the object to return
	 * @return			the object referenced by 'name' in the ExtendedMetadata object
	 * @throws ClassCastException	thrown to indicate that the code has attempted to cast an object to a subclass of which it is not an instance
	 * @throws NamedMetadataNotFoundException	if not Metadata with the 'name' is found
	 */
	public MDElement getElement(String name) throws ClassCastException, NamedMetadataNotFoundException;
	
	/**
	 * Puts a MDElement in the ExtendedMetadata object
	 * @param	elem	the MDElement object to put in the ExtendedMetadata object 
	 */
	public void setElement(MDElement elem);
	
	/**
	 * Retrieves the identifier of the ExtendedMetadata object
	 * @return			the String which identifies the ExtendedMetadata object
	 */
	public String getId();
	
	/**
	 * Retrieves the description of the ExtendedMetadata object
	 * @return			a String containing the description of the ExtendedMetadata object
	 */
	public String getDescription();
	
	/**
	 * Set the description of the ExtendedMetadata object
	 */
	public void setDescription(String desc);
	
	/**
	 * Retrieves the date of creation of the object
	 * @return			a Date object containing the system date when the ExtendedMetadata object was created
	 */
	public Date getCreateDate();
	
	/**
	 * Retrieves the date of the last change in the object
	 * @return			a Date object containing the system date when the ExtendedMetadata object was modified for the last time
	 */
	public Date getChangeDate();
	
	/**
	 * Retrieves the ExtendedMetadata object type
	 * @return			a MDDefinition object containing the name and the description of the ExtendedMetadata object
	 */
	public MDDefinition getType();
	
	/**
	 * Retrieves the name of the creator of the ExtendedMetadata object
	 * @return			a String containing the name of the creator of the object
	 */
	public String getAuthor();
	
	/**
	 * Checks if the ExtendedMetadata object has a specific value
	 * @param 	name	the name which identifies the value to check
	 * @return			a boolean value which indicates if there is a value in the object referenced by 'name'
	 */
	public boolean hasValue(String name);
	
	/**
	 * Checks if the ExtendedMetadata object is empty
	 * @return			a boolean value which indicates if the object has any value
	 */
	public boolean isEmpty();
	
	/**
	 * Retrieves the father of the Metadata object, or null if it doesn't have.
	 * @return			a ExtendedMetadata object or null
	 */
	public ExtendedMetadata getFather();
	
	/**
	 * Changes father of the object
	 * @param 	father	the new father value of the ExtendedMetadata object
	 */
	public void setFather(ExtendedMetadata father); 
	
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
	 * Retrieves a Iterator to the Metadata elements.
	 * @return			an Iterator object.
	 */
	public Iterator iterator();
	
	/**
	 * Retrieves a String representing the Metadata object and its elements.
	 * @return			a String.
	 */
	public String toString();
}