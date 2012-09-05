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

import org.gvsig.fmap.dal.exception.ReadException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;



/**
 * Extensión preparada para controlar las opciones que se pueden realizar sobre
 * una vista añadida en el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameViewExtension extends Extension {
	private Layout layout = null;

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"view-zoom-in",
				this.getClass().getClassLoader().getResource("images/ZoomIn.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"view-zoom-out",
				this.getClass().getClassLoader().getResource("images/ZoomOut.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"view-zoom-map-contents",
				this.getClass().getClassLoader().getResource("images/MapContents.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"view-pan",
				this.getClass().getClassLoader().getResource("images/Pan.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();

		if (s.compareTo("VIEW_ZOOMIN") == 0) {
			layout.getLayoutControl().setTool("layoutviewzoomin");
		} else if (s.compareTo("VIEW_ZOOMOUT") == 0) {
			layout.getLayoutControl().setTool("layoutviewzoomout");
		} else if (s.compareTo("VIEW_FULL") == 0) {
			try {
				layout.getLayoutControl().viewFull();
			} catch (ReadException e) {
				NotificationManager.addError("Error de Driver", e);
			}
		} else if (s.compareTo("VIEW_PAN") == 0) {
			layout.getLayoutControl().setTool("layoutviewpan");
		}
		layout.getModel().setModified(true);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		Layout l = (Layout)PluginServices.getMDIManager().getActiveWindow();
		IFFrame[] fframes = l.getLayoutContext().getFFrameSelected();
		if (!l.getLayoutContext().isEditable()) {
			return false;
		}
		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof FFrameView) {
				if (((FFrameView)fframes[i]).getView()!=null) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
		//	Layout layout = (Layout) f;

			return true; //layout.m_Display.getMapControl().getMapContext().getLayers().layerCount() > 0;
		} else {
			return false;
		}
	}
}
