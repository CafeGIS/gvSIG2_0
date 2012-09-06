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
 * 2008 {{Company}}   {{Task}}
 */
package org.gvsig.tools.locator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit tests for the AbstractLocator class.
 * 
 * @author <a href="mailto:cordin@disid.com">César Ordiñana</a>
 */
public class AbstractLocatorTest extends TestCase {

    private TestLocator locator;

    protected void setUp() throws Exception {
        super.setUp();
        locator = new TestLocator();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.gvsig.tools.locator.AbstractLocator#getNames()}.
     */
    public void testGetNames() {
        assertNull("Empty locator must not return any names", locator
                .getNames());

        String name1 = "test1";
        String name2 = "test2";

        locator.register(name1, String.class);
        locator.register(name2, String.class);

        String[] names = locator.getNames();

        assertEquals("Number of registered names incorrect, must be 2", 2,
                names.length);

        Collection namesColl = Arrays.asList(names);
        assertTrue("The list of names does not contain the registered name: "
                + name1, namesColl.contains(name1));
        assertTrue("The list of names does not contain the registered name: "
                + name2, namesColl.contains(name2));
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.locator.AbstractLocator#get(java.lang.String)} and
     * {@link org.gvsig.tools.locator.AbstractLocator#register(java.lang.String, java.lang.Class)}
     */
    public void testGetAndRegisterClass() {
        Class clazz = String.class;
        String name = "test";

        locator.register(name, clazz);

        Object ref = locator.get(name);

        assertEquals(clazz, ref.getClass());
    }

    /**
     * Test method for
     * {@link org.gvsig.tools.locator.AbstractLocator#get(java.lang.String)} and
     * {@link org.gvsig.tools.locator.AbstractLocator#register(String, LocatorObjectFactory)

     */
    public void testGetAndRegisterFactory() {
        final Object ref = new Object();
        LocatorObjectFactory factory = new LocatorObjectFactory() {

            public Object create() {
                return ref;
            }

            public Object create(Object[] args) {
                return ref;
            }

            public Object create(Map args) {
                return ref;
            }
        };

        String name = "test";

        locator.register(name, factory);

        Object locatorRef = locator.get(name);

        assertEquals(ref, locatorRef);
    }

    public class TestLocator extends AbstractLocator {
        public String getLocatorName() {
            return "TestLocator";
        }
    }

}