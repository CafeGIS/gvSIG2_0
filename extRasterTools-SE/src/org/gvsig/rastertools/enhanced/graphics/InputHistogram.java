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
import java.util.ArrayList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.Border;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.beans.canvas.layers.MinMaxLines;
import org.gvsig.raster.beans.canvas.layers.functions.BaseFunction;
import org.gvsig.raster.beans.canvas.layers.functions.DensitySlicingLine;
import org.gvsig.raster.beans.canvas.layers.functions.LogaritmicExponentialLine;
import org.gvsig.raster.beans.canvas.layers.functions.SquareRootPowLine;
import org.gvsig.raster.beans.canvas.layers.functions.StraightLine;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.rastertools.enhanced.ui.EnhancedListener;
/**
 * Componente con el histograma de entrada.
 * 
 * 20/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class InputHistogram extends HistogramGraphicBase {
	private static final long serialVersionUID = 681848373747974757L;
	
	/**
	 * Crea una nueva instancia de InputHistogram.
	 */
	public InputHistogram(Histogram hist, FLyrRasterSE lyr, double[] minList, double[] maxList) {
		super(hist, lyr, minList, maxList);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase#getCanvas()
	 */
	public GCanvas getCanvas() {
		if(canvas == null) {
			canvas = new GCanvas(Color.BLACK);
			canvas.addDrawableElement(new GraphicHistogram(Color.white));
			canvas.addDrawableElement(new MinMaxLines(minMaxLineColor));
			canvas.addDrawableElement(new StraightLine(functionColor));
			canvas.addDrawableElement(new Border(borderColor));
			canvas.addDrawableElement(new InfoLayer(Color.WHITE));
		}
		return canvas;
	}
	
	/**
	 * Asigna el listener para gestionar el evento de movimiento de gráficos
	 * @param listener
	 */
	public void setListener(EnhancedListener listener) {
		getCanvas().addValueChangedListener(listener);
	}
	
	/**
	 * Asigna el nivel de la función si esta es density slicing.
	 * @param level
	 */
	public void setLevel(int level) {
		if (histogramDrawed != null)
			histogramDrawed.setLevel(level);
		getCanvas().repaint();
	}
	
	/**
	 * Asigna el tipo de función Lineal, Exponencial, logaritmica, density slicing, campana de gauss, 
	 * raiz cuadrada o ecualización.
	 * 
	 * @param type Tipo de histograma. El valor está definido en las constantes de GraphicHistogram
	 */
	public void setFunction(int function) {
		if (histogramDrawed == null)
			return;

		BaseFunction baseFunction = histogramDrawed.getBaseFunction();
		
		ArrayList listBaseFunc = canvas.getDrawableElements(BaseFunction.class);
		if(listBaseFunc != null && listBaseFunc.get(0) instanceof BaseFunction)
			((BaseFunction)listBaseFunc.get(0)).setDrawing(true);
		
		boolean regen = false;
		switch (function) {
			case GraphicHistogram.FUNCTION_DENSITY:
				if (!baseFunction.getClass().equals(DensitySlicingLine.class))
					baseFunction = new DensitySlicingLine(histogramDrawed.getBaseFunction().getColor());
				break;
			case GraphicHistogram.FUNCTION_LINEAL:
				if (!baseFunction.getClass().equals(StraightLine.class))
					baseFunction = new StraightLine(histogramDrawed.getBaseFunction().getColor());
				break;
			case GraphicHistogram.FUNCTION_LOGARIT:
				regen = false;
				if (!baseFunction.getClass().equals(LogaritmicExponentialLine.class)) {
					regen = true;
				} else {
					if (!((LogaritmicExponentialLine) baseFunction).isLogaritmical())
						regen = true;
				}
				if (regen)
					baseFunction = new LogaritmicExponentialLine(histogramDrawed.getBaseFunction().getColor(), 1.0);
				break;
			case GraphicHistogram.FUNCTION_EXPONENT:
				regen = false;
				if (!baseFunction.getClass().equals(LogaritmicExponentialLine.class)) {
					regen = true;
				} else {
					if (!((LogaritmicExponentialLine) baseFunction).isExponencial())
						regen = true;
				}
				if (regen)
					baseFunction = new LogaritmicExponentialLine(histogramDrawed.getBaseFunction().getColor(), -1.0);
				break;
			case GraphicHistogram.FUNCTION_SQUARE_ROOT:
				if (!baseFunction.getClass().equals(SquareRootPowLine.class))
					baseFunction = new SquareRootPowLine(histogramDrawed.getBaseFunction().getColor(), 1.0);
				break;
			case GraphicHistogram.FUNCTION_NONE:
				((BaseFunction)listBaseFunc.get(0)).setDrawing(false);		
				break;
		}
					
		histogramDrawed.setBaseFunction(baseFunction);
		getCanvas().replaceDrawableElement(histogramDrawed.getBaseFunction(), BaseFunction.class);
		
		// Fuerza un repintado inmediato para actualizar los valores
		getCanvas().paint(getCanvas().getGraphics());
	}
}