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
package org.gvsig.gui.beans.slidertext;

import javax.swing.JFrame;

import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;

public class TestSliderTextTable implements SliderListener {
	private int 				w = 350, h = 150;
	private JFrame 				frame=new JFrame();
	private SliderTextContainer	slider = null;

	public TestSliderTextTable() {
		slider = new SliderTextContainer(-255, 255, 0);
		slider.addValueChangedListener(this);

		frame.getContentPane().add(slider);
		frame.setSize(w, h);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new TestSliderTextTable();
	}

	public void actionValueChanged(SliderEvent e) {
		System.out.println("- Changed: " + slider.getValue());
	}

	public void actionValueDragged(SliderEvent e) {
//		System.out.println("+ Dragged: " + slider.getValue());
	}
}