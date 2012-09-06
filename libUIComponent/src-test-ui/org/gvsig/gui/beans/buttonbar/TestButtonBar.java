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
package org.gvsig.gui.beans.buttonbar;

import org.gvsig.gui.beans.TestUI;

public class TestButtonBar {
	private TestUI frame = new TestUI("TestButtonBar");

	public TestButtonBar() {
		ButtonBarContainer cont = new ButtonBarContainer();
		frame.setSize(410, 45);
		cont.setButtonAlignment("right");
		cont.addButton("down.png", "hola",0);
		cont.addButton(null, "que",1);
		cont.addButton(null, "tal",2);
		cont.addButton(null, "estas?",3);
		cont.addButton(null, "adios", 4);
		cont.getButton(2).setEnabled(false);
		cont.disableAllControls();
		cont.restoreControlsValue();
		cont.setComponentBorder(true);
		cont.delButton(3);
		frame.getContentPane().add(cont);
		frame.setVisible(true);
	}

	public static void main(String[] args){
		new TestButtonBar();
	}
}