/*
 * Created on 05-may-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.LayoutKeyEvent;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Extensión para controlar las operaciones basicas de edición sobre el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutEditableControls extends Extension {
	private static LayoutKeyEvent lke=new LayoutKeyEvent();
	private static KeyStroke copyLayout = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK);
	private static KeyStroke cutLayout = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK);
	private static KeyStroke pasteLayout = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK);
	private static KeyStroke leftLayout = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0);
	private static KeyStroke rightLayout = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0);
	private static KeyStroke upLayout = KeyStroke.getKeyStroke(KeyEvent.VK_UP,0);
	private static KeyStroke downLayout = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0);
	private static KeyStroke undoLayout = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK);
	private static KeyStroke redoLayout = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK);
	private static KeyStroke del1Layout = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
	private static KeyStroke del2Layout = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		Layout layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		if (s.equals("PROPERTIES")) {
			if (layout.showFProperties()) {
				layout.getModel().setModified(true);
			}
		}
	}
	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
			return true;
		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerKeys();
	}
	private static void registerKeys() {
		PluginServices.registerKeyStroke(copyLayout, lke);
		PluginServices.registerKeyStroke(cutLayout, lke);
		PluginServices.registerKeyStroke(pasteLayout, lke);
		PluginServices.registerKeyStroke(leftLayout, lke);
		PluginServices.registerKeyStroke(rightLayout, lke);
		PluginServices.registerKeyStroke(upLayout, lke);
		PluginServices.registerKeyStroke(downLayout, lke);
		PluginServices.registerKeyStroke(undoLayout, lke);
		PluginServices.registerKeyStroke(redoLayout, lke);
		PluginServices.registerKeyStroke(del1Layout, lke);
		PluginServices.registerKeyStroke(del2Layout, lke);
	}
	private static void unregisterKeys() {
		PluginServices.unRegisterKeyStroke(copyLayout);
		PluginServices.unRegisterKeyStroke(cutLayout);
		PluginServices.unRegisterKeyStroke(pasteLayout);
		PluginServices.unRegisterKeyStroke(leftLayout);
		PluginServices.unRegisterKeyStroke(rightLayout);
		PluginServices.unRegisterKeyStroke(upLayout);
		PluginServices.unRegisterKeyStroke(downLayout);
		PluginServices.unRegisterKeyStroke(undoLayout);
		PluginServices.unRegisterKeyStroke(redoLayout);
		PluginServices.unRegisterKeyStroke(del1Layout);
		PluginServices.unRegisterKeyStroke(del2Layout);
	}
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}
}
