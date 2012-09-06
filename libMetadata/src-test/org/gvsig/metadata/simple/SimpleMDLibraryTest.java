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

package org.gvsig.metadata.simple;

import junit.framework.TestCase;

import org.gvsig.metadata.MDLibrary;
import org.gvsig.metadata.MDLocator;
import org.gvsig.tools.locator.Library;

public class SimpleMDLibraryTest extends TestCase {
	
	/**
     * Test method for {@link org.gvsig.metadata.simple.SimpleMDLibrary#initialize()}.
     */
	public void testInitialize() {
		Library api = new MDLibrary();
		Library impl = new SimpleMDLibrary();

		// 1: Initialize the Libraries
		api.initialize();
		impl.initialize();

		// 2: postInitialize them
		// if the library is not correctly initialized,
		// an Exception will be thrown
		api.postInitialize();
		impl.postInitialize();

		// Check if the correct implementation is returned
		assertEquals(SimpleMDManager.class, MDLocator.getMDManager()
				.getClass());
	}

}