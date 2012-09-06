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
package org.gvsig.gui.beans.colorbutton;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.gvsig.gui.beans.TestUI;
/**
* Test del ColorButton
*
* @version 08/05/2007
* @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
*/
public class TestColorButton implements ColorButtonListener {
	private TestUI       jFrame       = null;
	private ColorButton colorButton = null;

	public TestColorButton() {
		initialize();
	}

	private void initialize() {
		colorButton = new ColorButton();
		colorButton.setEnabled(true);
		colorButton.addValueChangedListener(this);
		jFrame = new TestUI("TestColorButton");
		JPanel jpanel = new JPanel();
		jpanel.add(colorButton);
		jFrame.setSize(new Dimension(598, 167));
		jFrame.setContentPane(jpanel);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestColorButton();
	}

	public void actionValueChanged(ColorButtonEvent e) {
		System.out.println("actionValueChanged");
	}

	public void actionValueDragged(ColorButtonEvent e) {
		System.out.println("actionValueDragged");
	}
}