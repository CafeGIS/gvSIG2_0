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
package org.gvsig.rastertools.reproject;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.reproject.ui.LayerReprojectPanel;

import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ReprojectTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem {
	static private ReprojectTocMenuEntry singleton  = null;
	FLayer lyr = null;

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "GeoRaster"; //FIXME
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 60;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 5;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "toc_reproject");
	}
	
	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public ReprojectTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new ReprojectTocMenuEntry();
		return singleton;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof FLyrRasterSE))
			return false;

		return ((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.REPROJECT);
	}

	/**
	 * Método que se ejecuta cuando se pulsa la entrada en el menú contextual del TOC 
	 * correspondiente a "Zoom a la resolución del raster". Aquí se creará el mapTool si 
	 * no se ha hecho antes y se cargará.
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		if (selectedItems.length == 1) {
			lyr = selectedItems[0];
			if (lyr instanceof FLyrRasterSE) {
				LayerReprojectPanel reprojectPanel = new LayerReprojectPanel((FLyrRasterSE) lyr, Boolean.TRUE);
				RasterToolsUtil.addWindow(reprojectPanel);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("reproj-icon");
	}
}