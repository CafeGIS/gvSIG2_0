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
 
package org.gvsig.metadata.extended.manager;

import java.util.Locale;

import org.gvsig.metadata.extended.MDElement;
import org.gvsig.metadata.extended.manager.MDElementImpl;
import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;

import junit.framework.TestCase;



public class MDElementImplTest extends TestCase {

	//@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	//@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	//@Before
	public void setUp() throws Exception {
	}

	//@After
	public void tearDown() throws Exception {
	}
	
	public void testGetId() {
		MDElement mde = new MDElementImpl();
		mde.setId("123");
		assertEquals("123", mde.getId());
	}
	
	public void testGetValue() {
		MDElement mde = new MDElementImpl();
		mde.setValue("Default value");
		assertEquals("Default value", mde.getValue());
	}
	
	public void testGetType() {
		MDElementDefinition mded = new MDElementDefinition("prueba", null, null, null, null);
		MDElement mde = new MDElementImpl();
		mde.setType(mded);
		assertEquals(mded, mde.getType());
	}
	
	public void testGetLocale() {
		MDElement mde = new MDElementImpl();
		mde.setLocale(Locale.ENGLISH);
		assertEquals(Locale.ENGLISH, mde.getLocale());
	}

}
