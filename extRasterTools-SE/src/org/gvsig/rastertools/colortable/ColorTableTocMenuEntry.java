/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.rastertools.colortable;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.colortable.ui.ColorTableDialog;

import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * <code>ColorTableTocMenuEntry</code> es el punto de entrada del menu de la
 * tabla de color
 * 
 * @version 26/06/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorTableTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem {
	static private ColorTableTocMenuEntry singleton  = null;

	/**
	 * Nadie puede crear una instancia a esta clase �nica, hay que usar el
	 * getSingleton()
	 */
	private ColorTableTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public ColorTableTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new ColorTableTocMenuEntry();
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
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "tablas_color");
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
		
		if ((((FLyrRasterSE) selectedItems[0]).getBandCount() == 1) &&
				(((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.COLOR_TABLE)))
					return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		ColorTableDialog colorTableDialog = new ColorTableDialog(selectedItems[0], 660, 465);
		RasterToolsUtil.addWindow(colorTableDialog);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("colortable-icon");
	}
}