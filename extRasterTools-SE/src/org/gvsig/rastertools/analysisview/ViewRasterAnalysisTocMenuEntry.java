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
package org.gvsig.rastertools.analysisview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.pixelincrease.PixelIncreaseDialog;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.MapOverview;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * Herramienta del menú contextual que carga el raster en el localizador para tener una visión general de
 * esta y carga el zoom del cursor para tener una selección de precisión.
 *
 * 16-jun-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ViewRasterAnalysisTocMenuEntry extends AbstractTocContextMenuAction implements PropertyChangeListener, IGenericToolBarMenuItem {
	private boolean onView = false;
	private String rasterNameInLoc = "";
	static private ViewRasterAnalysisTocMenuEntry singleton = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private ViewRasterAnalysisTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public ViewRasterAnalysisTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new ViewRasterAnalysisTocMenuEntry();

		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "RasterLayer";
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 55;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 4;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		if (!onView)
			return RasterToolsUtil.getText(this, "analysisview");
		else
			return RasterToolsUtil.getText(this, "closeanalysisview");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof ILayerState))
			return false;

		if (!((ILayerState) selectedItems[0]).isOpen())
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof FLyrRasterSE))
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {		
		MapControl mcCurrentLoc = null;
		ArrayList mapControlListLoc = new ArrayList();
		IWindow[] w = PluginServices.getMDIManager().getAllWindows();
		IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
		FLayer lyr = selectedItems[0];

		// Cerramos cualquier inspector de pixeles anterior que esté abierto y
		// obtenemos el mapcontrol del localizador
		for (int i = 0; i < w.length; i++) {
			if (w[i] instanceof PixelIncreaseDialog)
				RasterToolsUtil.closeWindow(w[i]);
			if (w[i] instanceof View && w[i].equals(activeWindow))
				mcCurrentLoc = ((BaseView) w[i]).getMapOverview();
			if (w[i] instanceof View)
				mapControlListLoc.add(((BaseView) w[i]).getMapOverview());
		}

		if (!onView) {
			// Abrimos el inspector de pixeles
			PixelIncreaseDialog pIncrease = new PixelIncreaseDialog();
			RasterToolsUtil.addWindow(pIncrease);

			// Cargamos el raster en el localizador
			mcCurrentLoc.getMapContext().beginAtomicEvent();

			Object path = null;
			if (lyr instanceof FLyrRasterSE)
				path = ((FLyrRasterSE) lyr).getLoadParams();

			try {
				lyr = FLyrRasterSE.createLayer(lyr.getName(), path, lyr.getProjection());
				rasterNameInLoc = lyr.getName();
				mcCurrentLoc.getMapContext().getLayers().addLayer(lyr);
			} catch (LoadLayerException e) {
				JOptionPane.showMessageDialog(null, RasterToolsUtil.getText(this, "coordenadas_erroneas"));
			}
			mcCurrentLoc.getMapContext().endAtomicEvent();
		} else {
			for (int nViews = 0; nViews < mapControlListLoc.size(); nViews++) {
				MapControl mc = ((MapControl) mapControlListLoc.get(nViews));
				FLayers lyrs = mc.getMapContext().getLayers();
				for (int i = 0; i < lyrs.getLayersCount(); i++) {
					if (lyrs.getLayer(i).getName().compareTo(rasterNameInLoc) == 0)
						lyrs.removeLayer(i);
				}
				if (mc instanceof MapOverview)
					((MapOverview) mc).refreshExtent();
			}
		}
		onView = !onView;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("analisis-icon");
	}

	public void propertyChange(PropertyChangeEvent evt) {}
}