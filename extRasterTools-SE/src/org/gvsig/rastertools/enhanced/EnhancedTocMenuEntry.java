/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.enhanced;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.enhanced.ui.EnhancedDialog;
import org.gvsig.rastertools.statistics.StatisticsProcess;

import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * Punto de entrada para la funcionalidad de realce por expansión del contraste
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class EnhancedTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem, IProcessActions {
	static private EnhancedTocMenuEntry   singleton   = null;
	private FLyrRasterSE                  lyr         = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private EnhancedTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public EnhancedTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new EnhancedTocMenuEntry();
		return singleton;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "RasterProcess";
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
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "enhanced_rad");
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
		
		return ((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.ENHANCED);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1) || (!(selectedItems[0] instanceof FLyrRasterSE)))
			return;
				
		this.lyr = (FLyrRasterSE)selectedItems[0];
		if(!lyr.getDataSource().getStatistics().isCalculated()) 
			StatisticsProcess.launcher(lyr, this);
		else
			end(null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("brush-icon");
	}

	/**
	 * Lanzamos la ventana al final del proceso de calculo de estadísticas.
	 */
	public void end(Object param) {
		EnhancedDialog enhancedDialog = new EnhancedDialog(lyr, 760, 421);
		RasterToolsUtil.addWindow(enhancedDialog);
	}

	public void interrupted() {
	}
}