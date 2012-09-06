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
package org.gvsig.gui.beans.listview;

import java.awt.Rectangle;

/**
 * <code>ListViewItem</code> representa un item para ser usado desde
 * ListViewComponent
 *
 * @version 28/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ListViewItem {
	IIconPaint icon          = null;
	String     name          = null;
	Rectangle  nameRectangle = null;
	Rectangle  itemRectangle = new Rectangle();
	boolean    selected      = false;
	boolean    showTooltip   = true;
	Object     tag           = null;

	/**
	 * Construye un ListViewItem con un icono y su nombre.
	 * @param icon
	 * @param name
	 */
	public ListViewItem(IIconPaint icon, String name) {
		this.icon = icon;
		this.name = name;
	}

	/**
	 * Obtener el nombre del item
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Definir el nombre del item
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtener el icono del item
	 * @return
	 */
	public IIconPaint getIcon() {
		return icon;
	}

	/**
	 * Especificar el icono del item
	 * @param icon
	 */
	public void setIcon(IIconPaint icon) {
		this.icon = icon;
	}

	/**
	 * Comprobar si el item esta seleccionado
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Definir si el item esta seleccionado
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Definir algun campo extra necesario por el programador
	 * @return
	 */
	public Object getTag() {
		return tag;
	}

	/**
	 * Obtiene el campo extra
	 * @return
	 */
	public void setTag(Object tag) {
		this.tag = tag;
	}

	public boolean isShowTooltip() {
		return showTooltip;
	}

	public void setShowTooltip(boolean showTooltip) {
		this.showTooltip = showTooltip;
	}

	public Rectangle getNameRectangle() {
		return nameRectangle;
	}

	public void setNameRectangle(Rectangle nameRectangle) {
		this.nameRectangle = nameRectangle;
	}

	public Rectangle getItemRectangle() {
		return itemRectangle;
	}

	public void setItemRectangle(Rectangle itemRectangle) {
		this.itemRectangle = itemRectangle;
	}
}