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
package org.gvsig.gui.beans.colorchooser;

import java.awt.Dimension;
import java.util.EventObject;

import org.gvsig.gui.beans.TestUI;
/**
* Test del ColorChooser
*
* @version 08/05/2007
* @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
*/
public class TestColorChooser implements ColorChooserListener {
	private TestUI       jFrame       = new TestUI("TestColorChooser");
	private ColorChooser colorChooser = null;

	public TestColorChooser() {
		initialize();
	}

	private void initialize() {
		colorChooser = new ColorChooser();
		colorChooser.addValueChangedListener(this);
		colorChooser.setEnabled(true);

		jFrame.setSize(new Dimension(258, 167));
		jFrame.setContentPane(colorChooser);
		jFrame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestColorChooser();
	}

	public void actionValueChanged(EventObject e) {
		System.out.println("Changed: " + colorChooser.getColor() + ", " + colorChooser.getColor().getAlpha());
	}

	public void actionValueDragged(EventObject e) {
		System.out.println("Dragged: " + colorChooser.getColor() + ", " + colorChooser.getColor().getAlpha());
	}
}