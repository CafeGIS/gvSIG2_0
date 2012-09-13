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
package org.gvsig.rastertools.properties;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.panelGroup.PanelGroupDialog;
import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * Entrada en del menú contextual del TOC correspondiente al cuadro de
 * propiedades del raster
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterPropertiesTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem {
	static private RasterPropertiesTocMenuEntry singleton  = null;
	private PanelGroupDialog                    properties = null;
	private FLayer                              lyr        = null;

	/**
	 * Variable para controlar si los eventos de los paneles se deben interpretar.
	 * En la carga inicial se deben desactivar todos los eventos
	 */
	public static boolean                     enableEvents = false;
	
	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private RasterPropertiesTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public RasterPropertiesTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new RasterPropertiesTocMenuEntry();
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
		return 60;
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
		return RasterToolsUtil.getText(this, "propiedades_raster");
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;
		if (selectedItems[0] instanceof ILayerState) {
			if (!((ILayerState) selectedItems[0]).isOpen())
				return false;
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;
		if (selectedItems[0] instanceof FLyrRasterSE)
			return true;
		return false;
	}

	/**
	 * Gestiona la apertura del dialogo de propiedades de raster cuando se pulsa
	 * la opción asignando a este las propiedades iniciales.
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return;

		lyr = selectedItems[0];

		try {
			enableEvents = false;

			PanelGroupManager manager = PanelGroupManager.getManager();

			manager.registerPanelGroup(TabbedPanel.class);
			manager.setDefaultType(TabbedPanel.class);

			TabbedPanel panelGroup = (TabbedPanel) manager.getPanelGroup(lyr);
			PanelGroupLoaderFromExtensionPoint loader = new PanelGroupLoaderFromExtensionPoint("RasterSEPropertiesDialog");

			properties = new PanelGroupDialog(lyr.getName() ,RasterToolsUtil.getText(this, "propiedades_raster"), 550, 450, (byte) (WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE), panelGroup);
			properties.loadPanels(loader);
			enableEvents = true;
			RasterToolsUtil.addWindow(properties);
		} catch (Exception e) {
			RasterToolsUtil.messageBoxInfo("error_props_tabs", this, e);
		} finally  {
			enableEvents = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("properties-icon");
	}
}