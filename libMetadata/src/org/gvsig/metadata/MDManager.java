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

package org.gvsig.metadata;

/**
 * Manages the load and storage of Metadata objects.
 * 
 * @author <a href="mailto:arturo.beltran@alumail.uji.es">Arturo Beltrán</a>
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface MDManager {

    /**
     * Loads a Metadata object data from persistence.
     * 
     * @param metadata
     *            the Metadata whose data will be loaded
     */
	void loadMD(Metadata metadata);

    /**
     * Stores the data of a Metadata. Only attributes which are registered into
     * the Metadata registry will be stored, the other ones will be ignored.
     * 
     * @param metadata
     *            object to store
     * @param storeChildren
     *            if the data of the children Metadata objects should also be
     *            stored
     */
    void storeMD(Metadata metadata, boolean storeChildren);
}