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
package org.gvsig.raster.grid.filter;

import javax.swing.JFrame;

import org.gvsig.raster.grid.filter.convolution.ConvolutionUI;
/**
 * Clase para poder ver la ventana de las Tablas de color
 *
 * @version 28/09/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class TestUIConvolution {
	private JFrame 			frame = new JFrame();

	public TestUIConvolution() {
		super();
		initialize();
	}

	public static void main(String[] args){
		new TestUIConvolution();
	}

	private void initialize() {
		ConvolutionUI ui = new ConvolutionUI(null);
		frame.setContentPane(ui);
		frame.setSize(new java.awt.Dimension(220, 190));
		frame.setResizable(true);
		frame.setTitle("Convolution");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
}