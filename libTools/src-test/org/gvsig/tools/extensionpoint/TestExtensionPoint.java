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

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;
import org.gvsig.tools.extensionpoint.impl.DefaultExtensionPointManager;

public class TestExtensionPoint extends TestCase {


	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private ExtensionPointManager getManager() {
		return DefaultExtensionPointManager.getManager();
	}

	public void testCreacion() {
		String name = "LayerWizars";
		String description = "Punto de extension que registra los distintos Wizars para añadir capas.";
		ExtensionPoint ep = null;

		ep = getManager().create(name, description);
		TestExtensionPoint.assertEquals(ep.getName(),name);
		TestExtensionPoint.assertEquals(ep.getDescription(),description);

		ep.append("WMSLayer", null, ExtensionDePrueba2.class);
		ep.append("WCSLayer", null, ExtensionDePrueba1.class);
		ep.append("WFSLayer", null, ExtensionDePrueba1.class);

		TestExtensionPoint.assertEquals(ep.getCount(), 3);

		// Comprobamos que la lista de extensiones
		// mantiene el orden de insercion.
		Iterator iter = ep.iterator();
		TestExtensionPoint.assertEquals("WMSLayer", ((Extension) iter.next())
                .getName());
		TestExtensionPoint.assertEquals("WCSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WFSLayer", ((Extension) iter.next())
                .getName());

		try {
			/*Object extension =*/ ep.create("WCSLayer");
		} catch (InstantiationException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WCSLayer con InstantiationException");
		} catch (IllegalAccessException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WCSLayer con IllegalAccessException");
		}

		ExtensionDePrueba2 extension = null;
		try {
			Object args[] = {"pepe",new Integer(5)};
			extension =(ExtensionDePrueba2)ep.create("WMSLayer",args);
		} catch (InstantiationException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WMSLayer con InstantiationException");
		} catch (IllegalAccessException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WMSLayer con IllegalAccessException");
		} catch (SecurityException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WMSLayer con SecurityException");
		} catch (IllegalArgumentException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WMSLayer con IllegalArgumentException");
		} catch (NoSuchMethodException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WMSLayer con NoSuchMethodException");
		} catch (InvocationTargetException e) {
			TestExtensionPoint.fail("Ha petado la creacion de WMSLayer con InvocationTargetException");
		}

		TestExtensionPoint.assertEquals("pepe",extension.nombre);
		TestExtensionPoint.assertEquals(5,extension.ancho);

	}

	public void testInsert() {
		String name = "LayerWizars";
		String description = "Punto de extension que registra los distintos Wizars para añadir capas.";
		ExtensionPoint ep = null;

		ep = getManager().create(name, description);

		ep.append("WMSLayer", null, ExtensionDePrueba2.class);
		ep.append("WCSLayer", null, ExtensionDePrueba1.class);
		ep.append("WFSLayer", null, ExtensionDePrueba1.class);
		Iterator iter = ep.iterator();
        TestExtensionPoint.assertEquals("WMSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WCSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WFSLayer", ((Extension) iter.next())
                .getName());

		ep.insert("WCSLayer", "testA", null, ExtensionDePrueba1.class);

		iter = ep.iterator();
        TestExtensionPoint.assertEquals("WMSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testA", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WCSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WFSLayer", ((Extension) iter.next())
                .getName());

		ep.insert("XXXX", "testB", null, ExtensionDePrueba1.class);
		iter = ep.iterator();
        TestExtensionPoint.assertEquals("WMSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testA", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WCSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WFSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testB", ((Extension) iter.next())
                .getName());

		ep.insert("testB", "testC", null, ExtensionDePrueba1.class);
		iter = ep.iterator();
        TestExtensionPoint.assertEquals("WMSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testA", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WCSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WFSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testC", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testB", ((Extension) iter.next())
                .getName());

		ep.insert("WMSLayer", "testD", null, ExtensionDePrueba1.class);
		iter = ep.iterator();
        TestExtensionPoint.assertEquals("testD", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WMSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testA", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WCSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("WFSLayer", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testC", ((Extension) iter.next())
                .getName());
        TestExtensionPoint.assertEquals("testB", ((Extension) iter.next())
                .getName());
	}

	public static class ExtensionDePrueba1 {
        public ExtensionDePrueba1() {
            ;
        }
    }

	public static class ExtensionDePrueba2 {
        public int ancho;
        public String nombre;

        public ExtensionDePrueba2(String nombre, Integer ancho) {
            this.ancho = ancho.intValue();
            this.nombre = nombre;
        }
	}
}
