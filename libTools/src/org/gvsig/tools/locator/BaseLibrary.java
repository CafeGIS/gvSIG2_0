/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Gobernment (CIT)
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
* 2008 {DiSiD Technologies}   {Create a base Locator implementation}
*/
package org.gvsig.tools.locator;

/**
 * Base Library implementation, adding initialization logging.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public abstract class BaseLibrary implements Library {

    public void initialize() throws ReferenceNotRegisteredException {
        // TODO: log library initialization
        // Nothing to initialize
    }

    public void postInitialize() throws ReferenceNotRegisteredException {
        // TODO: log library postInitialization
        // Nothing to check
    }

}