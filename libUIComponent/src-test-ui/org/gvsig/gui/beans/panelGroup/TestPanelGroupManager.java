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

import junit.framework.TestCase;

import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;
import org.gvsig.gui.beans.panelGroup.treePanel.TreePanel;

/**
 * <p>Tests the class {@link PanelGroupManager PanelGroupManager}.</p>
 * 
 * @version 06/11/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class TestPanelGroupManager extends TestCase {
	private PanelGroupManager manager;
	private Object reference; 

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		manager = PanelGroupManager.getManager();
		manager.registerPanelGroup(TabbedPanel.class);
		manager.setDefaultType(TabbedPanel.class);

		reference = new Object();
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
	public void testGetManager() {
		try {
			assertSame(manager, PanelGroupManager.getManager());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void testDefaultType() {
		try {
			assertSame(manager.getPanelGroup(reference).getClass(), TabbedPanel.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void testTabbedPanelType() {
		try {
			manager.setDefaultType(TabbedPanel.class);
			assertSame(manager.getPanelGroup(reference).getClass(), TabbedPanel.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void testTreePanelType() {
		try {
			manager.registerPanelGroup(TreePanel.class);
			manager.setDefaultType(TreePanel.class);
			assertSame(manager.getPanelGroup(reference).getClass(), TreePanel.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void testCreateAPanelGroupManager() {
		try {
			manager.setDefaultType(TreePanel.class);
			assertSame(manager.getPanelGroup(reference).getClass(), TreePanel.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void test1RegistegerAndDeregisterTypes() {
		try {
			manager.setDefaultType(TabbedPanel.class);
			manager.deregisterPanelGroup(TabbedPanel.class);

			AbstractPanelGroup panelGroup = manager.getPanelGroup(reference);
			
			assertEquals(panelGroup, null);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * <p>Test, results must be valid.</p>
	 */
	public void test2RegistegerAndDeregisterTypes() {
		try {
			manager.deregisterPanelGroup(TabbedPanel.class);
			manager.deregisterPanelGroup(TreePanel.class);
			manager.registerPanelGroup(TabbedPanel.class);
			manager.registerPanelGroup(TreePanel.class);
			manager.setDefaultType(TabbedPanel.class);
			manager.registerPanelGroup(TabbedPanel.class);
			manager.registerPanelGroup(TreePanel.class);
			manager.deregisterPanelGroup(TreePanel.class);
			manager.deregisterPanelGroup(TabbedPanel.class);

			AbstractPanelGroup panelGroup = manager.getPanelGroup(reference);
			
			assertEquals(panelGroup, null);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
}
