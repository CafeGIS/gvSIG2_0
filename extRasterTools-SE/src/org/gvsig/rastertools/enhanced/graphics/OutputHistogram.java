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
package org.gvsig.rastertools.enhanced.graphics;

import java.awt.Color;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.Border;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.datastruct.Histogram;
/**
 * Componente con el histograma de salida.
 * 
 * 20/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class OutputHistogram extends HistogramGraphicBase {
	private static final long serialVersionUID = -3827308936130572914L;

	/**
	 * Crea una nueva instancia de OutputHistogram.
	 */
	public OutputHistogram(Histogram hist, FLyrRasterSE lyr, double[] minList, double[] maxList) {
		super(hist, lyr, minList, maxList);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase#getCanvas()
	 */
	public GCanvas getCanvas() {
		if (canvas == null) {
			canvas = new GCanvas(Color.BLACK);
			canvas.addDrawableElement(new GraphicHistogram(Color.white));
			canvas.addDrawableElement(new Border(borderColor));
			InfoLayer infoLayer = new InfoLayer(Color.WHITE);
			infoLayer.setLimits(0.0, 255.0);
			canvas.addDrawableElement(infoLayer);
		}
		return canvas;
	}
}