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
package org.gvsig.gui.beans.doubleslider;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestDoubleSlider implements DoubleSliderListener {
	private JFrame jFrame = null;
	private DoubleSlider doubleSlider = null;
	private JPanel jPanel = null;

	public TestDoubleSlider() {
		jFrame = new JFrame();
		jFrame.setSize(new Dimension(498, 267));
		jFrame.setContentPane(getJPanel());
		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}

	/**
	 * This method initializes multiSlider	
	 * 	
	 * @return borsanza.src.multislider.MultiSlider	
	 */
	private DoubleSlider getMultiSlider() {
		if (doubleSlider == null) {
			doubleSlider = new DoubleSlider();
			doubleSlider.setMinimum(0);
			doubleSlider.setMaximum(255);
			doubleSlider.addValueChangedListener(this);
		}
		return doubleSlider;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getMultiSlider(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	public static void main(String[] args) {
		new TestDoubleSlider();
	}

	public void actionValueChanged(DoubleSliderEvent e) {
		System.out.println("Changed: " + getMultiSlider().getValue());
	}

	public void actionValueDragged(DoubleSliderEvent e) {
		System.out.println("Dragged: " + getMultiSlider().getValue());
	}
}