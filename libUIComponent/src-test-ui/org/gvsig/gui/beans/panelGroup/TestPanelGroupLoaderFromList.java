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

package org.gvsig.gui.beans.panelGroup;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gui.beans.panelGroup.loaders.PanelGroupLoaderFromList;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.gui.beans.panelGroup.samples.Samples_Data;

/**
 * <p>Tests {@link PanelGroupLoaderUtilities PanelGroupLoaderUtilities} and {@link PanelGroupLoaderFromList PanelGroupLoaderFromList}.</p>
 * 
 * @version 16/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class TestPanelGroupLoaderFromList extends TestCase {

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
			PanelGroupLoaderFromList loader = new PanelGroupLoaderFromList(Samples_Data.TEST2_CLASSES);
			ArrayList<IPanel> panels = new ArrayList<IPanel>();

			loader.loadPanels(panels);

			// Check that has loaded all panels
			assertEquals(Samples_Data.TEST2_CLASSES.length, panels.size());
			
			int i = 0;

			while (i < Samples_Data.TEST2_CLASSES.length) {
				// Check order and class types
				assertTrue(panels.get(i).getClass() == (Samples_Data.TEST2_CLASSES[i++]));
			}
		} catch (BaseException e) {
			System.out.println(e.getLocalizedMessageStack());
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
