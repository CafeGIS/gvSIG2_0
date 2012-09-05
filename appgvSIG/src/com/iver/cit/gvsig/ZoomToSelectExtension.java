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
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;
import org.gvsig.tools.exception.BaseException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Extensión de zoom a lo seleccionado teniendo como ventana activa una vista o una tabla.
 *
 * @author Vicente Caballero Navarro
 */
public class ZoomToSelectExtension extends Extension {
	/**
	 * @see com.iver.mdiApp.plugins.IExtension#updateUI(java.lang.String)
	 */
	public void execute(String s) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		 	.getActiveWindow();
		MapContext mapa = null;
		Envelope selectedExtent = null;
		if (f instanceof View) {
			View vista = (View)f;
			mapa = vista.getModel().getMapContext();
		}else if (f instanceof FeatureTableDocumentPanel) {
			FeatureTableDocumentPanel table=(FeatureTableDocumentPanel)f;
			mapa = (table.getModel().getAssociatedLayer()).getMapContext();
		}

		try {
			selectedExtent = mapa.getSelectionBounds();
			mapa.getViewPort().setEnvelope(selectedExtent);
		} catch (BaseException e) {
			NotificationManager.addError(e);
		}

		//if (selectedExtent != null) {
			//Envelope env=new DefaultEnvelope(2,new double[]{selectedExtent.getX(),selectedExtent.getY()},new double[]{selectedExtent.getMaxX(),selectedExtent.getMaxY()});
			//((ProjectDocument) vista.getModel()).setModified(true);

		//}

	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
															 .getActiveWindow();
		if (f != null && f instanceof View) {
			MapContext mapa = ((View) f).getModel().getMapContext();
			return mapa.getLayers().getLayersCount() > 0;
//		}else if (f instanceof Table) {
//			Table t=(Table)f;
//			IWindow[] windows=PluginServices.getMDIManager().getAllWindows();
//			for (int i=0;i<windows.length;i++) {
//				if (windows[i] instanceof View) {
//					if (t.getModel().getAssociatedTable()!=null)
//					if (((View)windows[i]).getMapControl().getMapContext().equals(((FLyrVect)t.getModel().getAssociatedTable()).getMapContext())){
//						return true;
//					}
//				}
//			}
		}

		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		 	.getActiveWindow();
		if (f == null) {
			return false;
		}
		if (f instanceof View) {
			View view = (View) f;
			FLayer[] selected = view.getModel().getMapContext().getLayers().getActives();
			if (selected.length == 1 && selected[0] instanceof FLyrVect && selected[0].isAvailable()){
				try {
					if (!((FLyrVect) selected[0]).getFeatureStore()
							.getFeatureSelection().isEmpty()) {
						return true;
					}
				} catch (DataException e) {
					NotificationManager.addError(e);
					return false;
				}
			}
		}else if (f instanceof FeatureTableDocumentPanel) {
			FeatureTableDocumentPanel t=(FeatureTableDocumentPanel)f;
			IWindow[] windows=PluginServices.getMDIManager().getAllWindows();
			for (int i=0;i<windows.length;i++) {
				if (windows[i] instanceof View) {
					if (((View)windows[i]).getMapControl().getMapContext().equals((t.getModel().getAssociatedLayer()).getMapContext())){
						try {
							if (!t.getModel().getStore().getFeatureSelection()
									.isEmpty()) {
								return true;
							}
						} catch (DataException e) {
							NotificationManager.addError(e);
							return false;
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
				"view-zoom-to-seleccion",
				this.getClass().getClassLoader().getResource("images/ZoomSeleccion.png")
			);
	}
}
