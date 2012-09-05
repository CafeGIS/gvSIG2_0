/*
 * Created on 02-mar-2004
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

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.gui.FeatureTypeEditingPanel;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;


/**
 * Extensión que abre la ventana para cambiar la configuración de la estructura de la tabla.
 *
 * @author Vicente Caballero Navarro
 */
public class TableEditAttributes extends Extension {
	private FeatureTableDocumentPanel table = null;

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#updateUI(java.lang.String)
	 */
	public void execute(String s) {
		FeatureTableDocument pt = table.getModel();
	    FeatureStore fs = pt.getStore();
   		FeatureTypeEditingPanel dlg = new FeatureTypeEditingPanel(fs);
   		PluginServices.getMDIManager().addWindow(dlg);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

    public boolean isEnabled() {
		FeatureStore fs = table.getModel().getStore();
	    return fs.isEditing();
	}

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();
		if (v !=null && v instanceof FeatureTableDocumentPanel) {
		    table=(FeatureTableDocumentPanel)v;
			return true;
		}
		return false;
    }

}
