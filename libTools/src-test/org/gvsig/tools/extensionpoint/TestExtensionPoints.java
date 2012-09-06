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
 */
package org.gvsig.tools.extensionpoint;

import java.util.Iterator;

import junit.framework.TestCase;

import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;
import org.gvsig.tools.extensionpoint.impl.DefaultExtensionPointManager;

public class TestExtensionPoints extends TestCase {

	protected void setUp() throws Exception {
	    // Remove previous registered extension points, so other tests
        // don't affect the test validations.
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test() {
		ExtensionPointManager manager = new DefaultExtensionPointManager();

		manager.add("LayerWizars").append("WMS", null,
				UnaExtensionDePrueba1.class);
		manager.add("LayerWizars").append("WCS", null,
				UnaExtensionDePrueba2.class);
		manager.add("OtherWizars").append("uno", null,
				UnaExtensionDePrueba1.class);
		manager.add("OtherWizars").append("dos", null,
				UnaExtensionDePrueba2.class);

		// Comprobamos que el orden de las extensiones es el que hemos fijado.
		ExtensionPoint x = manager.get("LayerWizars");
		Iterator i = x.iterator();
		String[] nombres = {"WMS", "WCS" };
		int n = 0;
		while( i.hasNext() ) {
			String nombre = ((Extension) i.next()).getName();
			assertTrue(nombres[n].equals(nombre));
			n++;
		}


		ExtensionPoint extensionPointLayerWizars;
		extensionPointLayerWizars = manager.get("LayerWizars");
		assertTrue(extensionPointLayerWizars.has("WMS"));
		assertTrue(extensionPointLayerWizars.has("WCS"));

		assertEquals(2, manager.getCount());
		assertEquals(2, extensionPointLayerWizars.getCount());

		ExtensionPoint extensionPointLayerWizars2 = manager.create(
				"LayerWizars", null);


		extensionPointLayerWizars2.append("File", null,
				UnaExtensionDePrueba3.class);
		extensionPointLayerWizars2.append("JDBC", null,
				UnaExtensionDePrueba4.class);

		manager.add(extensionPointLayerWizars2);

		extensionPointLayerWizars = manager.get("LayerWizars");
		assertEquals(manager.getCount(), 2);
		assertEquals(4, extensionPointLayerWizars.getCount());
        assertEquals(2, manager.get("OtherWizars").getCount());

		assertTrue(extensionPointLayerWizars.has("WMS"));
		assertTrue(extensionPointLayerWizars.has("WCS"));
		assertTrue(extensionPointLayerWizars.has("File"));
		assertTrue(extensionPointLayerWizars.has("JDBC"));

		assertEquals(UnaExtensionDePrueba4.class, extensionPointLayerWizars
                .get("JDBC").getExtension());
        assertEquals(UnaExtensionDePrueba1.class, extensionPointLayerWizars
                .get("WMS").getExtension());

		assertEquals(UnaExtensionDePrueba1.class, manager.get("OtherWizars")
                .get("uno").getExtension());

		ExtensionPoint extensionPointOther2 = manager.create("OtherWizars",
				null);
		extensionPointOther2.append("tres", null, UnaExtensionDePrueba3.class);
		extensionPointOther2
				.append("cuatro", null, UnaExtensionDePrueba4.class);

		manager.add(extensionPointOther2);

		ExtensionPoint extensionPointOther = manager.get("OtherWizars");
		assertEquals(manager.getCount(), 2);
		assertEquals(extensionPointLayerWizars.getCount(), 4);
		assertEquals(extensionPointOther.getCount(), 4);

		assertTrue(extensionPointOther.has("uno"));
		assertTrue(extensionPointOther.has("dos"));
		assertTrue(extensionPointOther.has("tres"));
		assertTrue(extensionPointOther.has("cuatro"));

		assertEquals(UnaExtensionDePrueba3.class, extensionPointOther.get(
                "tres").getExtension());
        assertEquals(UnaExtensionDePrueba2.class, extensionPointOther
                .get("dos").getExtension());

		assertEquals(manager.get("Ninguno"), null);
	}

	public void testSingleton() {
		ExtensionPointManager manager1 = DefaultExtensionPointManager
				.getManager();

		manager1.add("LayerWizars").append("WMS", null,
				UnaExtensionDePrueba1.class);
		manager1.add("LayerWizars").append("WCS", null,
				UnaExtensionDePrueba2.class);
		manager1.add("OtherWizars").append("uno", null,
				UnaExtensionDePrueba1.class);
		manager1.add("OtherWizars").append("dos", null,
				UnaExtensionDePrueba2.class);

		ExtensionPointManager manager2 = DefaultExtensionPointManager
				.getManager();
		assertEquals(2, manager2.getCount());

		ExtensionPoint extensionPointLayerWizars;
		extensionPointLayerWizars = manager2.get("LayerWizars");
		assertTrue(extensionPointLayerWizars.has("WMS"));
		assertTrue(extensionPointLayerWizars.has("WCS"));
	}
}

class UnaExtensionDePrueba1 {
    public UnaExtensionDePrueba1() {
		;
	}
}
class UnaExtensionDePrueba2 {
	public UnaExtensionDePrueba2() {
	}
}

class UnaExtensionDePrueba3 {
	public UnaExtensionDePrueba3() {
	}
}

class UnaExtensionDePrueba4 {
	public UnaExtensionDePrueba4() {
	}
}