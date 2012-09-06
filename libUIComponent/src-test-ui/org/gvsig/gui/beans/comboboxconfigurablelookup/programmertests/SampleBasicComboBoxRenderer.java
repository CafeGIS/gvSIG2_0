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

package org.gvsig.gui.beans.comboboxconfigurablelookup.programmertests;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * <p>Sample of personalized combo box cell renderer.</p>
 * 
 * @version 08/02/2008
 * @author Pablo Piqueras Bartolom� (pablo.piqueras@iver.es) 
 */
public class SampleBasicComboBoxRenderer extends BasicComboBoxRenderer {
	private static final long serialVersionUID = -9044759678425798655L;

	/**
	 * <p>Creates a new instance of the <code>SampleBasicComboBoxRenderer</code> class.</p>
	 */
	public SampleBasicComboBoxRenderer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicComboBoxRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if (isSelected)
			component.setBackground(Color.GRAY);
		else
			component.setBackground(new Color(((17 * index) % 256), ((31 * index) % 256), ((7 * index) % 256)));
		
		return component;
	}
}
