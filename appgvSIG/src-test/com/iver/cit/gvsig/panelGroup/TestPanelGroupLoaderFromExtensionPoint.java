/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */

package com.iver.cit.gvsig.panelGroup;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.gui.beans.panelGroup.panels.IPanel;

import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.panelGroup.samples.Samples_ExtensionPointsOfIPanels;

/**
 * <p>Tests {@link PanelGroupLoaderFromExtensionPoint PanelGroupLoaderFromExtensionPoint}.</p>
 * 
 * @version 16/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class TestPanelGroupLoaderFromExtensionPoint extends TestCase {

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void test() {
		try {
			Samples_ExtensionPointsOfIPanels.loadSample();
			
			PanelGroupLoaderFromExtensionPoint loader = new PanelGroupLoaderFromExtensionPoint(Samples_ExtensionPointsOfIPanels.EXTENSIONPOINT1_NAME);
			ArrayList<IPanel> panels = new ArrayList<IPanel>();

			loader.loadPanels(panels);

			// Check that has loaded all panels
			assertEquals(Samples_ExtensionPointsOfIPanels.EXTENSIONPOINTS1_CLASSES.length, panels.size());
			
			int i = 0;

			while (i < Samples_ExtensionPointsOfIPanels.EXTENSIONPOINTS2_CLASSES.length) {
				// Check order and class types
				assertTrue(panels.get(i).getClass() == (Samples_ExtensionPointsOfIPanels.EXTENSIONPOINTS1_CLASSES[i++]));
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
