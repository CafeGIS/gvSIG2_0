/*
 * Created on 31-may-2004
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

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;


/**
 * Extensión encargada de limpiar la selección.
 *
 * @author Vicente Caballero Navarro
 */
public class ClearSelectionExtension extends Extension {
	/**
	 * @see com.iver.mdiApp.plugins.IExtension#updateUI(java.lang.String)
	 */
	public void execute(String s) {
		if (s.compareTo("DEL_SELECTION") == 0) {
			boolean refresh = false;
			com.iver.andami.ui.mdiManager.IWindow view =PluginServices.getMDIManager().getActiveWindow();

			if (view instanceof BaseView){
				BaseView vista = (BaseView) view;
				IProjectView model = vista.getModel();
				MapContext mapa = model.getMapContext();
				MapControl mapCtrl = vista.getMapControl();
				FLayers layers = mapa.getLayers();
				refresh = clearSelectionOfView(layers);

				if (refresh) {
					mapCtrl.drawMap(false);
				}
				((ProjectDocument)vista.getModel()).setModified(true);
			}else if (view instanceof FeatureTableDocumentPanel){
				FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) view;
				FeatureTableDocument model = table.getModel();
				FeatureStore featureStore=null;
//				try {
					featureStore = model.getStore();
//				} catch (ReadException e) {
//					e.printStackTrace();
//				}
				try {
				if (featureStore.getFeatureSelection().getSize() != 0) {
					refresh = true;
				}
			    featureStore.getFeatureSelection().deselectAll();
				} catch (DataException e) {
					NotificationManager.addError(e);
				}
			    if (refresh) {
			    	table.repaint();
				}
			    table.getModel().setModified(true);
			}
		 else {
				}
			}
    }


	private boolean clearSelectionOfView(FLayers layers){
		boolean refresh=false;

		for (int i = 0; i < layers.getLayersCount(); i++) {
			FLayer lyr =layers.getLayer(i);
			if (lyr instanceof FLayers){
				refresh = refresh || clearSelectionOfView((FLayers) lyr);
			} else if (lyr instanceof FLyrVect) {
				FLyrVect lyrVect = (FLyrVect) lyr;
				if (lyrVect.isActive()) {
					try {
						FeatureStore featureStore;

						featureStore = ((FLyrVect)lyr).getFeatureStore();
						if (featureStore.getFeatureSelection().getSize() != 0) {
							refresh = true;
						}
				        featureStore.getFeatureSelection().deselectAll();
					} catch (ReadException e) {
						e.printStackTrace();
					} catch (DataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return refresh;
	}
	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices.getMDIManager().getActiveWindow();

		if (view == null) {
			return false;
		}
		if (view instanceof BaseView) {
			MapContext mapa = ((BaseView) view).getModel().getMapContext();
			return mapa.getLayers().getLayersCount() > 0;
		}
		if (view instanceof FeatureTableDocumentPanel) {
			return true;
		}

		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices.getMDIManager().getActiveWindow();
		if (view == null) {
			return false;
		}
		if (view instanceof BaseView){
			MapContext mapa = ((BaseView) view).getMapControl().getMapContext();
			return hasVectorLayersWithSelection(mapa.getLayers());
		}
		if (view instanceof FeatureTableDocumentPanel){
			try {
				return !((FeatureSelection)((FeatureTableDocumentPanel)view).getModel().getStore().getSelection()).isEmpty();
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean hasVectorLayersWithSelection(FLayers layers) {
		for (int i = 0; i < layers.getLayersCount(); i++) {
			FLayer lyr =layers.getLayer(i);
			if (lyr instanceof FLayers){
				if (hasVectorLayersWithSelection((FLayers) lyr)){
					return true;
				}
			} else if (lyr instanceof FLyrVect) {
				FLyrVect lyrVect = (FLyrVect) lyr;
				if (lyrVect.isActive()) {
					if (lyrVect.isAvailable()){
						try {
							if (lyrVect.getFeatureStore().getFeatureSelection()
									.getSize() > 0) {
								return true;
							}
						} catch (DataException e) {
							e.printStackTrace();
							NotificationManager.addWarning("Capa " + lyrVect.getName() + " sin recordset correcto",e);
						}
					}
				}
			}
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
				"view-clear-selection",
				this.getClass().getClassLoader().getResource("images/delselection.png")
			);
	}
}
