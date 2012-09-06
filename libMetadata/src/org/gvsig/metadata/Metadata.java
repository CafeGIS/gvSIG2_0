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

import java.util.Set;

import org.gvsig.tools.dynobject.DynObject;

/**
 * Metadata is information or data about data {@link http
 * ://en.wikipedia.org/wiki/Metadata}.
 * <p>
 * This interface extends DynObject to add anything needed over the DynObject
 * model to be able to be used as a Metadata model.
 * </p>
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface Metadata extends DynObject {

    /**
     * Returns the unique identifier of the Metadata.
     *
     * @return the Metadata identifier
     */
    Object getMetadataID();

    /**
     * Returns the name of the Metadata, which allows to identify the type in
     * the Metadata registry.
     *
     * @return the Metadata name
     */
    String getMetadataName();

    /**
     * Returns a set of child Metadata objects.
     *
     * @return a set of child Metadata objects
     */
    Set getMetadataChildren();

}