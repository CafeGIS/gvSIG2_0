/*
 * Created on 15-jul-2004
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.FLayoutGraphics;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Extensión que actua sobre el Layout para controlas las diferentes
 * operaciones sobre los gráficos.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutGraphicControls extends Extension {
    private static final Logger logger = LoggerFactory
            .getLogger(LayoutGraphicControls.class);
	private Layout layout =null;
	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		FLayoutGraphics lg = new FLayoutGraphics(layout);
		logger.debug("Comand : " + s);

		if (s.compareTo("AGRUPAR") == 0) {
			layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this,"group"));
			lg.grouping();
			layout.getLayoutContext().getFrameCommandsRecord().endComplex();
			layout.getModel().setModified(true);
		} else if (s.compareTo("DESAGRUPAR") == 0) {
			lg.ungrouping();
			layout.getModel().setModified(true);
		} else if (s.compareTo("PROPIEDADES") == 0) {
			if (lg.openFFrameDialog()) {
				layout.getModel().setModified(true);
			}
		} else if (s.compareTo("ALINEAR") == 0) {
			lg.aligning();
			layout.getModel().setModified(true);
		} else if (s.compareTo("DETRAS") == 0) {
			lg.behind();
			layout.getModel().setModified(true);
		} else if (s.compareTo("DELANTE") == 0) {
			lg.before();
			layout.getModel().setModified(true);
		} else if (s.compareTo("BORDEAR") == 0) {
			if (lg.border()) {
				layout.getModel().setModified(true);
			}
		} else if (s.compareTo("POSICIONAR") == 0) {
			lg.position();
			layout.getModel().setModified(true);
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
			return true; //layout.m_Display.getMapControl().getMapContext().getLayers().layerCount() > 0;
		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"layout-group",
				this.getClass().getClassLoader().getResource("images/agrupar.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"layout-ungroup",
				this.getClass().getClassLoader().getResource("images/desagrupar.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"layout-bring-to-front",
				this.getClass().getClassLoader().getResource("images/delante.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"layout-send-to-back",
				this.getClass().getClassLoader().getResource("images/detras.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"layout-set-size-position",
				this.getClass().getClassLoader().getResource("images/posicionar.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"layout-add-border",
				this.getClass().getClassLoader().getResource("images/bordear.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		if (!layout.getLayoutContext().isEditable())
			return false;
		return true;
	}
}
