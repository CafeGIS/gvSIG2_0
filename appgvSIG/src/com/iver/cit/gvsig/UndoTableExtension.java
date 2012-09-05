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

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;
import org.gvsig.tools.undo.UndoException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;


/**
 * Extensión encargada de gestionar el rehacer un comando anteriormente
 * deshecho.
 *
 * @author Vicente Caballero Navarro
 */
public class UndoTableExtension extends Extension {
	private FeatureTableDocumentPanel table;

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"table-undo",
				this.getClass().getClassLoader().getResource("images/Undo.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {

		if (s.compareTo("UNDO") == 0) {
			if (table.getModel().getStore().isEditing()){
				FeatureStore fs=table.getModel().getStore();
				try {
					fs.undo();
					fs.getFeatureSelection().deselectAll();
				} catch (DataException e) {
					NotificationManager.addError(e);
				} catch (UndoException e) {
					NotificationManager.addError(e);
				}

			}
			table.getModel().setModified(true);
		}
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		//MapControl mapControl = (MapControl) vista.getMapControl();
		//FLayers layers=mapControl.getMapContext().getLayers();
		//for (int i=0;i<layers.getLayersCount();i++){
		FeatureStore fs=table.getModel().getStore();
			if (fs.isEditing()){
				if (fs==null) {
					return false;
				}
				return fs.canUndo();
			}

		//}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
															 .getActiveWindow();
		if (f!=null && f instanceof FeatureTableDocumentPanel) {
			table=(FeatureTableDocumentPanel)f;
			return true;
		}
		return false;
	}
}
