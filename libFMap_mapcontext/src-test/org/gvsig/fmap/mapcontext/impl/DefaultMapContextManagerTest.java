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
* 2009 {DiSiD Technologies}  {{Task}}
*/
package org.gvsig.fmap.mapcontext.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.*;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.tools.task.Cancellable;

/**
 * @author <a href="mailto:cordinyana@gvsig.org">Cèsar Ordiñana</a>
 */
public class DefaultMapContextManagerTest extends TestCase {
    
    private DefaultMapContextManager manager;

    protected void setUp() throws Exception {
        super.setUp();
        manager = new DefaultMapContextManager();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.gvsig.fmap.mapcontext.impl.DefaultMapContextManager#createDefaultMapContextDrawerInstance()}.
     */
    public void testGetDefaultMapContextDrawerInstance() throws Exception {
        manager.setDefaultMapContextDrawer(DummyMapContextDrawer.class);

        MapContextDrawer drawer = manager.createDefaultMapContextDrawerInstance();
        
        assertNotNull("The created MapContextDrawer instance can't be null",
                drawer);
        assertTrue(
                "The created MapContextDrawer is not instance of the registered class"
                        + DummyMapContextDrawer.class,
                drawer instanceof DummyMapContextDrawer);
    }

    /**
     * Test method for {@link org.gvsig.fmap.mapcontext.impl.DefaultMapContextManager#setDefaultMapContextDrawer(java.lang.Class)}.
     */
    public void testSetDefaultMapContextDrawer() throws Exception {

        // First, try to register an invalid class
        try {
            manager.setDefaultMapContextDrawer(Object.class);
            fail("Error, a class that does not implement the MapContextDrawer "
                    + "interface has been accepted");
        } catch (MapContextException e) {
            // OK
        }

        // Next, try to register a valid class
        manager.setDefaultMapContextDrawer(DummyMapContextDrawer.class);
    }

    public static class DummyMapContextDrawer implements MapContextDrawer {

        public void dispose() {
        }

        public void draw(FLayers root, BufferedImage image, Graphics2D g,
                Cancellable cancel, double scale) throws ReadException {
        }

        public void print(FLayers root, Graphics2D g, Cancellable cancel,
                double scale, PrintAttributes properties)
                throws ReadException {
        }

        public void setMapContext(MapContext mapContext) {
        }

        public void setViewPort(ViewPort viewPort) {
        }

    }
}
