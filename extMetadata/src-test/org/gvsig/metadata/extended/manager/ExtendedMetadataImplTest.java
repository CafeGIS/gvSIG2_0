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

import java.util.Date;

import junit.framework.TestCase;

import org.gvsig.metadata.exceptions.NamedMetadataNotFoundException;
import org.gvsig.metadata.extended.ExtendedMetadata;
import org.gvsig.metadata.extended.MDElement;
import org.gvsig.metadata.extended.manager.ExtendedMetadataImpl;
import org.gvsig.metadata.extended.manager.MDElementImpl;
import org.gvsig.metadata.extended.registry.objects.MDDefinition;
import org.gvsig.metadata.extended.registry.objects.MDElementDefinition;


public class ExtendedMetadataImplTest extends TestCase {

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

	public void testGet() throws NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		md.set("Author", "Arturo");
		assertEquals("Arturo", md.get("Author"));
	}
	
	public void testNamedMetadataNotFoundException() {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		try {
			md.get("Topic");
			fail("get() should have thrown a NamedMetadataNotFoundException!");
		}
		catch (NamedMetadataNotFoundException ex) {
		}
	}
	
	public void testGetBoolean() throws ClassCastException, NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		md.setBoolean("Available", true);
		assertEquals(true, md.getBoolean("Available"));
	}
	
	public void testGetDouble() throws ClassCastException, NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		md.setDouble("Area", (double) 500);
		assertEquals((double) 500, md.getDouble("Area"), 0);
	}
	
	public void testGetInt() throws ClassCastException, NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		md.setInt("Number", 5);
		assertEquals(5, md.getInt("Number"));
	}
	
	public void testGetString() throws ClassCastException, NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		md.setString("Author", "Alejandro Llaves");
		assertEquals("Alejandro Llaves", md.getString("Author"));
	}
	
	public void testGetDate() throws ClassCastException, NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		md.setDate("Date", new Date(0L));
		assertEquals(new Date(0l), md.getDate("Date"));
	}

	public void testHasValue() {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		assertEquals(md.hasValue("Author"), false);
		md.set("Author", "Alejandro Llaves");
		assertEquals(md.hasValue("Author"), true);
	}

	public void testIsEmpty() {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		assertEquals(md.isEmpty(), true);
		md.set("Author", "Alejandro Llaves");
		assertEquals(md.isEmpty(), false);
	}
	
	public void testGetElement() throws ClassCastException, NamedMetadataNotFoundException {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		MDElementDefinition mded = new MDElementDefinition("elemento de prueba", null, null, null, null);
		MDElement mde = new MDElementImpl("elem", mded, null);
		md.setElement(mde);
		assertEquals(mde, md.getElement("elemento de prueba"));
	}
	
	public void testGetName() {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		assertEquals("prueba", md.getName());
		((ExtendedMetadataImpl) md).setName("2a_prueba");
		assertEquals("2a_prueba", md.getName());
	}
	
	public void testGetDescription() {
		ExtendedMetadata md = new ExtendedMetadataImpl("prueba");
		((ExtendedMetadataImpl) md).setDescription("Objeto Metadata de prueba");
		assertEquals("Objeto Metadata de prueba", md.getDescription());
	}
	
	public void testGetType() {
		MDDefinition mdd = new MDDefinition("prueba", "Objeto Metadata de prueba");
		ExtendedMetadata md = new ExtendedMetadataImpl(mdd, null);
		assertEquals(mdd, md.getType());
	}
}
