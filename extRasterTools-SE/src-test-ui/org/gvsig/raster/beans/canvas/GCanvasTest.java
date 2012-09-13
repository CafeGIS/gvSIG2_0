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
package org.gvsig.raster.beans.canvas;

import java.awt.Color;
import java.awt.Dimension;

import org.gvsig.raster.beans.canvas.layers.Border;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.beans.canvas.layers.MinMaxLines;
import org.gvsig.raster.beans.canvas.layers.functions.SquareRootPowLine;
import org.gvsig.rastertools.TestUI;
/**
 * Test para comprobar el funcionamiento basico de un GCanvas
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GCanvasTest implements IGCanvasListener {
	private TestUI       jFrame = null;
	private GCanvas  component = null;
	private SquareRootPowLine line = null;
	private MinMaxLines  minMaxLines = null;
	private InfoLayer infoLayer = null;

	public GCanvasTest() {
		initialize();
	}

	private void initialize() {
		jFrame = new TestUI("GCanvasTest");
		double[] histR = new double[]{0, 0, 3, 4, 5, 8, 7, 18, 45, 36, 21, 36, 12, 23, 23, 40, 17, 10, 5, 1, 0, 0, 0};
		GraphicHistogram gHistR = new GraphicHistogram(histR, Color.RED);
		gHistR.setType(GraphicHistogram.TYPE_FILL);
		
		line = new SquareRootPowLine(Color.YELLOW, 1.0);
		//line = new ExponentialLine(Color.YELLOW);
		//line = new LogaritmicLine(Color.YELLOW);
		DrawableElement border = new Border(Color.WHITE);
		minMaxLines = new MinMaxLines(Color.white);
		infoLayer = new InfoLayer(Color.white);
		
		component = new GCanvas(Color.BLACK);
		component.addDrawableElement(border);
		component.addDrawableElement(gHistR);
		component.addDrawableElement(minMaxLines);
		component.addDrawableElement(line);
		component.addDrawableElement(infoLayer);
		
		component.addValueChangedListener(this);
		
		jFrame.setSize(new Dimension(222, 248));
		jFrame.setContentPane(component);

		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new GCanvasTest();
	}

	public void actionDataDragged(GCanvasEvent e) {}
	public void actionDataChanged(GCanvasEvent e) {}
}