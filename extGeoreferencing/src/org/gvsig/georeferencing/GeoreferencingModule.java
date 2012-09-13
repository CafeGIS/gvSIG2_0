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
package org.gvsig.georeferencing;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.georeferencing.main.Georeferencing;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * Extensión para georreferenciación
 *
 * 05/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GeoreferencingModule  extends Extension implements IGenericToolBarMenuItem {
	private ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		if (actionCommand.equals("GEOREFERENCING")) {
			Georeferencing georef = new Georeferencing();
			georef.initialize();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		RasterLibrary.wakeUp();
		ExtensionPoint ep = this.extensionPoints.add("GenericToolBarMenu");

//		ExtensionPoint extensionPoints = ExtensionPoint.getExtensionPoint("GenericToolBarMenu");
		ep.append("Georreferencing", "", this.getClass());

		PluginServices.getIconTheme().registerDefault(
				"save-icon",
				this.getClass().getClassLoader().getResource("images/save.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"reset-icon",
				this.getClass().getClassLoader().getResource("images/reset.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"tfwload-icon",
				this.getClass().getClassLoader().getResource("images/load.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"selectzoomarea-icon",
				this.getClass().getClassLoader().getResource("images/view-zoom-to-seleccion.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"prevzoom-icon",
				this.getClass().getClassLoader().getResource("images/view-zoom-back.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"fullview-icon",
				this.getClass().getClassLoader().getResource("images/view-zoom-map-contents.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"increase-icon",
				this.getClass().getClassLoader().getResource("images/aumentar.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"decrease-icon",
				this.getClass().getClassLoader().getResource("images/disminuir.png")
			);
		PluginServices.getIconTheme().register(
				"georeferencing-icon",
				this.getClass().getClassLoader().getResource("images/georef.gif")
			);
		PluginServices.getIconTheme().register(
				"exporttoascii-icon",
				this.getClass().getClassLoader().getResource("images/exportToAscii.png")
			);
		PluginServices.getIconTheme().register(
				"exporttocsv-icon",
				this.getClass().getClassLoader().getResource("images/exportToCSV.png")
			);
		PluginServices.getIconTheme().register(
				"importfromcsv-icon",
				this.getClass().getClassLoader().getResource("images/importFromCSV.png")
			);
		PluginServices.getIconTheme().register(
				"exit-icon",
				this.getClass().getClassLoader().getResource("images/ico_exit.gif")
			);
		PluginServices.getIconTheme().register(
				"process-icon",
				this.getClass().getClassLoader().getResource("images/icon_process.gif")
			);
		PluginServices.getIconTheme().register(
				"endprocess-icon",
				this.getClass().getClassLoader().getResource("images/icon_endprocess.gif")
			);
		PluginServices.getIconTheme().register(
				"options-icon",
				this.getClass().getClassLoader().getResource("images/ico_options.gif")
			);
		PluginServices.getIconTheme().register(
				"add-icon",
				this.getClass().getClassLoader().getResource("images/add-ico.gif")
			);
		PluginServices.getIconTheme().register(
				"centerpoint-icon",
				this.getClass().getClassLoader().getResource("images/icon_centerpoint.gif")
			);
		PluginServices.getIconTheme().register(
				"hand-icon",
				this.getClass().getClassLoader().getResource("images/Hand.gif")
			);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		this.execute("GEOREFERENCING");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getGroup()
	 */
	public String getGroup() {
		return "GeoRaster";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return PluginServices.getIconTheme().get("georeferencing-icon");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getOrder()
	 */
	public int getOrder() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "georreferenciacion");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return this.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return this.isVisible();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 0;
	}
}