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

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.panelGroup.loaders.PanelGroupLoaderFromList;
import org.gvsig.gui.beans.panelGroup.samples.Samples_Data;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;

/**
 * <p>Tests the loading of layers using a {@link TabbedPanel TabbedPanel} object and a {@link PanelGroupLoaderFromList PanelGroupLoaderFromList} loader
 * Tests also the managing of the different kind of exceptions which could be launched during this process.</p>
 * 
 * @version 29/11/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class Test1ExceptionsUsingTabbedPanel extends TestCase {
	private TabbedPanel panelGroup;
	private PanelGroupManager manager;
	private PanelGroupLoaderFromList loader;
	private String validationText;
	private String localizedMessage;

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		manager = PanelGroupManager.getManager();
		manager.registerPanelGroup(TabbedPanel.class);
		manager.setDefaultType(TabbedPanel.class);
		panelGroup = (TabbedPanel) manager.getPanelGroup(Samples_Data.REFERENCE3);
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		manager.deregisterPanelGroup(TabbedPanel.class);
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test1() {
		validationText = Messages.getText("couldnt_add_some_panel_exception") + "\n  " + Messages.getText("couldnt_load_panels_from_list_exception") + "\n  Error cargando un panel: / by zero.";
		
		try {
			System.out.println("----- Test 1 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST4_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test2() {
		validationText = Messages.getText("couldnt_add_some_panel_exception") + "\n  Panel de etiqueta Information_LABEL sin tamaño inicial definido.";
		
		try {
			System.out.println("----- Test 2 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST5_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test3() {
		validationText = Messages.getText("empty_panel_group_exception");

		try {
			System.out.println("----- Test 3 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST6_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test4() {
		validationText = Messages.getText("empty_panel_group_gui_exception");

		try {
			System.out.println("----- Test 4 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST7_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test5() {
		validationText = Messages.getText("couldnt_add_some_panel_exception") + "\n  " + Messages.getText("couldnt_load_panels_from_list_exception") + "\n  Error cargando un panel: / by zero.\n  Panel de etiqueta Information_LABEL sin tamaño inicial definido.\n  " + Messages.getText("empty_panel_group_exception");

		try {
			System.out.println("----- Test 5 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST8_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test6() {
		validationText = Messages.getText("couldnt_add_some_panel_exception") + "\n  " + Messages.getText("couldnt_load_panels_from_list_exception") + "\n  Error cargando un panel: / by zero.\n  Panel de etiqueta Information_LABEL sin tamaño inicial definido.\n  " + Messages.getText("empty_panel_group_gui_exception");

		try {
			System.out.println("----- Test 6 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST9_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
	
	/**
	 * <p>Test the 'PanelGroup' exceptions </p>
	 */
	public void test7() {
		validationText = Messages.getText("couldnt_add_some_panel_exception") + "\n  " + Messages.getText("couldnt_load_panels_from_list_exception") + "\n  Error cargando un panel: / by zero.\n  Panel de etiqueta Information_LABEL sin tamaño inicial definido.";

		try {
			System.out.println("----- Test 7 -----");
			loader = new PanelGroupLoaderFromList(Samples_Data.TEST10_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
		} catch (BaseException bE) {
			localizedMessage = bE.getLocalizedMessageStack();
			System.out.println(localizedMessage);
			System.out.println("------------------");
			assertEquals(localizedMessage, validationText);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("------------------");
			fail();
			return;
		}

		System.out.println("------------------");
		fail();
	}
}
