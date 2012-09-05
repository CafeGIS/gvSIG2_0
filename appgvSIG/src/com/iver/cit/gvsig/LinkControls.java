/*
 * Created on 01-jun-2004
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

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Extensión para gestionar los hiperlinks.
 *
 * @author Vicente Caballero Navarro
 */
public class LinkControls extends Extension {
    private static final Logger logger = LoggerFactory
            .getLogger(LinkControls.class);

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		//ProjectView model = vista.getModel();
		//FMap mapa = model.getMapContext();
		MapControl mapCtrl = vista.getMapControl();
		logger.debug("Comand : " + s);

		if (s.compareTo("LINK") == 0) {
			mapCtrl.setTool("link");
		}
	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
															 .getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof View) {

		  MapContext mapa = ((View) f).getModel().getMapContext();

		  return mapa.getLayers().getLayersCount() > 0;
		} else {
			return false;
		}
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		View f = (View) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof com.iver.cit.gvsig.project.documents.view.gui.View) {
			com.iver.cit.gvsig.project.documents.view.gui.View view = (com.iver.cit.gvsig.project.documents.view.gui.View) f;
			IProjectView model = view.getModel();
			Boolean p=null;


			FLayer[] activas = model.getMapContext().getLayers().getActives();

			//Utilizo esta comprobación para poner el botón de la herramienta desactivado
			//cuando se carga la ventana (comprobar que hay alguna capa activa)
			if(activas.length==0)
				return false;

			for (int i = 0; i < activas.length; i++) {
				if (!activas[i].isAvailable()) {
					return false;
				}
				if(!activas[i].allowLinks())return false;
				if(activas[i].getLinkProperties()==null || activas[i].getLinkProperties().getField()==null)
					return false;
			}
		}
		return true;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){

		PluginServices.getIconTheme().registerDefault(
				"view-query-link",
				this.getClass().getClassLoader().getResource("images/Link.png")
			);
	}
}
