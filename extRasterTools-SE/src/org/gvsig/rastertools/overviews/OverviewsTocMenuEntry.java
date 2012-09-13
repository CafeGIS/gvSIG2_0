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
package org.gvsig.rastertools.overviews;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.exceptions.ReloadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.dataset.BandAccessException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * Herramienta del menú contextual para la generación de overviews.
 * 
 * 10-dic-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class OverviewsTocMenuEntry extends AbstractTocContextMenuAction implements PropertyChangeListener, IGenericToolBarMenuItem, IProcessActions {
	static private OverviewsTocMenuEntry singleton  = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private OverviewsTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public OverviewsTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new OverviewsTocMenuEntry();
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
		return PluginServices.getText(this, "generar_overviews");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof FLyrRasterSE))
			return false;

		if (!((ILayerState) selectedItems[0]).isOpen())
			return false;

		if (!((FLyrRasterSE) selectedItems[0]).overviewsSupport())
			return false;
		
		if (!((FLyrRasterSE) selectedItems[0]).getDataSource().overviewsSupport())
			return false;

		return ((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.CREATEOVERVIEWS);							
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
		FLayer lyr = selectedItems[0];
		if (lyr instanceof FLyrRasterSE) {
			if (RasterToolsUtil.messageBoxYesOrNot("sobreescribir_datos_overview", this)) {

				try {
					if (((FLyrRasterSE) lyr).getDataSource().getOverviewCount(0) > 0) {
						if (!RasterToolsUtil.messageBoxYesOrNot("sobreescribir_overviews", this))
							return;
					}
				} catch (BandAccessException e) {
					// Actuamos como si no tubiera overviews pero salvamos un Log
					RasterToolsUtil.debug("Error accediendo a la banda en getOverViewCount", this, e);

				} catch (RasterDriverException e) {
					// Actuamos como si no tubiera overviews pero salvamos un Log
					RasterToolsUtil.debug("Error en getOverViewCount", this, e);
				}

				RasterProcess process = new OverviewsProcess();
				process.addParam("layer", (FLyrRasterSE) lyr);
				process.setCancelable(false);
				process.setActions(this);
				process.start();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return PluginServices.getIconTheme().get("overviews-icon");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		try {
			((FLyrRasterSE) param).reload();
		} catch (ReloadLayerException e) {
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {}
	public void interrupted() {}
}