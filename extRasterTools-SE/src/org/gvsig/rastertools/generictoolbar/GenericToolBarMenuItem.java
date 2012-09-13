/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.generictoolbar;

import java.util.Map;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.tools.extensionpoint.ExtensionBuilder;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * Clase que implementa un IGenericToolBarMenuItem para evitar tener que crear
 * clases para items de menu sencillas
 *
 * @version 06/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class GenericToolBarMenuItem implements IGenericToolBarMenuItem, ExtensionBuilder {
	private String text  = "";
	private int    order = 0;
	private int    groupOrder = 0;
	private Icon   icon  = null;
	private String group = "";

	public GenericToolBarMenuItem(String text, Icon icon) {
		this(text, icon, "", 0);
	}

	public GenericToolBarMenuItem(String text) {
		this(text, PluginServices.getIconTheme().get("blank-icon"), "", 0);
	}

	public GenericToolBarMenuItem(String text, Icon icon, String group, int order) {
		this.text = text;
		this.order = order;
		this.icon = icon;
		this.group = group;
	}

	public GenericToolBarMenuItem(String text, Icon icon, String group) {
		this(text, icon, group, 0);
	}

	public GenericToolBarMenuItem(String text, Icon icon, int order) {
		this(text, icon, "", 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getGroup()
	 */
	public String getGroup() {
		return group;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getOrder()
	 */
	public int getOrder() {
		return order;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getText()
	 */
	public String getText() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return icon;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getGroupOrder()
	 */
	public int getGroupOrder() {
		return groupOrder;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {}

	public Object create() {
		return this;
	}

	public Object create(Object[] args) {
		return this;
	}

	public Object create(Map args) {
		return this;
	}
}