package org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems;

import java.io.Serializable;

import javax.swing.ImageIcon;

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



/**
 * This class represents the necessary information about each image icon item of 'JPanelWithJComboBoxImageIconItems'
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class ImageIconItemInfo implements Serializable {
	private static final long serialVersionUID = -7087121873703006781L;

	private ImageIcon imageIcon;
	private String toolTipText;
	private Object itemValue; // An object value of the item like a item value of a JComboBox
	
	/* This other attribute it's necessary because it's possible that the image icon couldn't be
	     loaded and then in this case could be impossible to know the path after */
	private String path;

	/**
	 * Default constructor without parameters
	 */
	public ImageIconItemInfo() {
		path = null;
		imageIcon = null;
		toolTipText = null;
		itemValue = null;
	}
	
	/**
	 * Default constructor with two parameters
	 * 
	 * @param path Path to the image icon
	 * @param image_Icon The image icon
	 * @param text The tool tip text
	 * @param item_Value A value associated to this item
	 */
	public ImageIconItemInfo(String image_Icon_Path, ImageIcon image_Icon, String text, Object item_Value) {
		path = image_Icon_Path;
		imageIcon = image_Icon;
		toolTipText = text;
		itemValue = item_Value;
	}

	/**
	 * Gets the image icon object reference
	 * 
	 * @return the imageIcon
	 */
	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	/**
	 * Sets the image icon object reference
	 * 
	 * @param imageIcon the imageIcon to set
	 */
	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	/**
	 * Gets the path
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path
	 * 
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Gets the tool tip text
	 * 
	 * @return text the tool tip text to this icon image
	 */
	public String getToolTipText() {
		return this.toolTipText;
	}
	
	/**
	 * Sets the tool tip text
	 * 
	 * @param text the tool tip text to this icon image
	 */
	public void setToolTipText(String text) {
		this.toolTipText = text;
	}
	
	/**
	 * Gets the associated item value
	 * 
	 * @return a value associated to this item
	 */
	public Object getItemValue() {
		return this.itemValue;
	}
	
	/**
	 * Sets the associated item value
	 * 
	 * @param item_Value a value associated to this item
	 */
	public void setItemValue(Object item_Value) {
		this.itemValue = item_Value;
	}
}