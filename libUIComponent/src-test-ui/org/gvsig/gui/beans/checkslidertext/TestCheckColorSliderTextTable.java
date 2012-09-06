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

import java.awt.Color;

import org.gvsig.gui.beans.TestUI;
import org.gvsig.gui.beans.doubleslider.DoubleSliderEvent;
import org.gvsig.gui.beans.doubleslider.DoubleSliderListener;

public class TestCheckColorSliderTextTable implements DoubleSliderListener {
	private int                           w      = 375, h = 150;
	private TestUI                        frame  = new TestUI("TestCheckColorSliderTextTable");
	private CheckColorSliderTextContainer sliderRed = null;

	public TestCheckColorSliderTextTable() {
		sliderRed = new CheckColorSliderTextContainer(0, 255, 0, "R", false);
		sliderRed.setColor1(Color.BLACK, false);
		sliderRed.setColor2(Color.RED, true);
		sliderRed.setCheckboxVisible(true);

		frame.getContentPane().add(sliderRed);
		sliderRed.setEnabled(true);
		frame.setSize(w, h);
		frame.setVisible(true);
		sliderRed.addValueChangedListener(this);
	}

	public static void main(String[] args) {
		new TestCheckColorSliderTextTable();
	}

	public void actionValueChanged(DoubleSliderEvent e) {
		System.out.println("Changed: " + sliderRed.getValue());
	}

	public void actionValueDragged(DoubleSliderEvent e) {
		System.out.println("Dragged: " + sliderRed.getValue());
	}
}