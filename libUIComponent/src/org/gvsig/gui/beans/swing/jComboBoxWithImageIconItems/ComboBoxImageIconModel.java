package org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems;

import java.io.Serializable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

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
 * Model for 'JPanelWithJComboBoxImageIconItems', stores the attribute 'showImageIconAndText'
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class ComboBoxImageIconModel extends DefaultComboBoxModel implements Serializable {
	private static final long serialVersionUID = -775510419199115805L;

	public static final boolean DEFAULT_SHOW_IMAGE_ICON_AND_TEXT_PROPERTY = false;
	
	private boolean showImageIconAndText;

	/**
	 * @see DefaultComboBoxModel#DefaultComboBoxModel()
	 */
	public ComboBoxImageIconModel() {
		super();
		initialize();
	}

	/**
	 * @see DefaultComboBoxModel#DefaultComboBoxModel(Object[])
	 */
	public ComboBoxImageIconModel(Object[] items) {
		super(items);
		initialize();
	}

	/**
	 * @see DefaultComboBoxModel#DefaultComboBoxModel(Vector)
	 */
	public ComboBoxImageIconModel(Vector v) {
		super(v);
		initialize();
	}

	/**
	 * Initializes the new attribute of this model
	 */
	private void initialize() {
		showImageIconAndText = DEFAULT_SHOW_IMAGE_ICON_AND_TEXT_PROPERTY;
	}

	/**
	 * Sets the value of the inner attribute 'imageIconAndText'
	 * 
	 * @param b A boolean value
	 */
	public void setShowImageIconAndTextProperty(boolean b) {
		showImageIconAndText = b;
	}

	/**
	 * Returns the value of the inner attribute 'imageIconAndText'
	 * 
	 * @return A boolean value
	 */
	public boolean getShowImageIconAndTextProperty() {
		return showImageIconAndText;
	}
}
