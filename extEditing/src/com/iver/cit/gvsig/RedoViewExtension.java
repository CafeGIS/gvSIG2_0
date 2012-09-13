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
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.tools.undo.RedoException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Extensión encargada de gestionar el rehacer un comando anteriormente
 * deshecho.
 *
 * @author Vicente Caballero Navarro
 */
public class RedoViewExtension extends Extension {
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		PluginServices.getIconTheme().registerDefault(
				"view-redo",
				this.getClass().getClassLoader().getResource("images/Redo.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		MapControl mapControl = vista.getMapControl();

		if (s.compareTo("REDO") == 0) {
			try {
					FLayers layers=mapControl.getMapContext().getLayers();
					for (int i=0;i<layers.getLayersCount();i++){
						if (layers.getLayer(i) instanceof FLyrVect && layers.getLayer(i).isEditing() && layers.getLayer(i).isActive()){
							((FLyrVect)layers.getLayer(i)).getFeatureStore().redo();
//							CommandsRecord commandsRecord=((FLyrVect)layers.getLayer(i)).getFeatureStore().getCommandsRecord();
//							commandsRecord.redo();
							mapControl.drawMap(false);
//							vea.getCommandRecord().fireCommandsRepaint(null);
//							vea.getSelection().clear();
//							CADExtension.getCADTool().clearSelection();
						}

					}
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(),e);
			} catch (DataException e) {
				NotificationManager.addError(e.getMessage(),e);
			} catch (RedoException e) {
				NotificationManager.addError(e.getMessage(),e);
			}

			//vista.getMapControl().drawMap(false);
		}
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		MapControl mapControl = vista.getMapControl();
		FLayers layers=mapControl.getMapContext().getLayers();
		for (int i=0;i<layers.getLayersCount();i++){
			try {
				if (layers.getLayer(i) instanceof FLyrVect && ((FLyrVect)layers.getLayer(i)).getFeatureStore().isEditing() && layers.getLayer(i).isActive()){
					return ((FLyrVect)layers.getLayer(i)).getFeatureStore().canRedo();
				}
			} catch (ReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
															 .getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof View) {
			MapControl mapControl = ((View)f).getMapControl();
			FLayer[] layers=mapControl.getMapContext().getLayers().getActives();
			FLayer layer;
			for (int i=0;i<layers.length;i++){
				layer = layers[i];
				if (!layer.isAvailable()){
					continue;
				}
				try {
					if (layer instanceof FLyrVect && ((FLyrVect)layer).getFeatureStore().isEditing()){
						return true;
					}
				} catch (ReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
