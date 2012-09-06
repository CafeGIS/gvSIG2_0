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
package org.gvsig.gui.beans.checkslidertext;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;

public class TestCheckSliderTextTable implements SliderListener {
	private TestUI                   frame  = new TestUI("TestCheckSliderTextTable");
	private CheckSliderTextContainer slider = null;

	public TestCheckSliderTextTable() {
		slider = new CheckSliderTextContainer(0, 10, 5, false, "Activar", false);
		slider.setDecimal(false);
		slider.setBorder("Cabecera");
		slider.addValueChangedListener(this);

		frame.getContentPane().add(slider);
		frame.setSize(375, 150);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestCheckSliderTextTable();
	}

	public void actionValueChanged(SliderEvent e) {
		System.out.println("Changed: " + slider.getValue());
	}

	public void actionValueDragged(SliderEvent e) {
		System.out.println("Dragged: " + slider.getValue());
	}
}