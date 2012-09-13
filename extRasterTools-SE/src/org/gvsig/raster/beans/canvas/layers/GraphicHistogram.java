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
package org.gvsig.raster.beans.canvas.layers;

import java.awt.Color;
import java.awt.Graphics;

import org.gvsig.raster.beans.canvas.DrawableElement;
/**
 * Gráfica que representa un histograma con los valores pasados en el constructor 
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GraphicHistogram extends DrawableElement {
	public static final int TYPE_LINE            = 1;
	public static final int TYPE_FILL            = 2;

	public static final int VIEW_LINEAL          = 0;
	public static final int VIEW_ACUMMULATED     = 1;
	public static final int VIEW_LOGARITHMIC     = 2;
	public static final int VIEW_ACUMMULATEDLOG  = 3;
	
	public static final int FUNCTION_NONE        = -1;
	public static final int FUNCTION_LINEAL      = 0;
	public static final int FUNCTION_GAUSS       = 1;
	public static final int FUNCTION_EXPONENT    = 2;
	public static final int FUNCTION_LOGARIT     = 3;
	public static final int FUNCTION_SQUARE_ROOT = 4;
	public static final int EQUALIZATION         = 5;
	public static final int FUNCTION_DENSITY     = 6;

	private int             border               = 2;

	// Valores de la función original
	private double[]        histogramValues      = null;

	// Valores que se pintan en la gráfica
	private double[]        valuesLineal         = null;
	private double[]        valuesAcummulated    = null;
	private double[]        valuesLogarithmic    = null;
	private double[]        valuesAcummulatedLog = null;

	private int             typeViewed           = 0;

	private int             type                 = TYPE_LINE;
	
	/**
	 * Constructor. Asigna el color
	 * @param c
	 */
	public GraphicHistogram(Color c) {
		setColor(c);
	}

	/**
	 * Constructor. Asigna el color y los valores
	 * @param c
	 */
	public GraphicHistogram(double[] values, Color c) {
		setColor(c);
		setHistogramDrawed(values);
	}

	private int valueToPixelY(double value) {
		value = 1.0D - value;
		return (int) Math.round(canvas.getCanvasMinY() + border + ((canvas.getCanvasMaxY() - canvas.getCanvasMinY() - (border * 2)) * value));
	}

	/**
	 * Dibujado de la línea de incremento exponencial sobre el canvas.
	 */
	protected void paint(Graphics g) {
		double[] valuesToDraw = recalcHistogram(typeViewed);
		
		if (valuesToDraw == null)
			return;

		g.setColor(color);

		double width;
		switch (type) {
			case TYPE_FILL:
				width = canvas.getCanvasMaxX() - canvas.getCanvasMinX();
				for (int i = 0; i < valuesToDraw.length; i++) {
					int x1 = (int) Math.round(((double) i - 0.5D) * (width / (valuesToDraw.length - 1.0D)));
					int x2 = (int) Math.round(((double) i + 0.5D) * (width / (valuesToDraw.length - 1.0D)));
					x1 += canvas.getCanvasMinX();
					x2 += canvas.getCanvasMinX();
					int y1 = valueToPixelY(valuesToDraw[i]);
					if (x1 < (canvas.getCanvasMinX() + border))
						x1 = canvas.getCanvasMinX() + border;
					if (x2 > (canvas.getCanvasMaxX() - border))
						x2 = canvas.getCanvasMaxX() - border;
					g.setColor(color);
					g.fillRect(x1, y1, x2 - x1, canvas.getCanvasMaxY() - border - y1);
				}
				break;
			default:
				width = canvas.getCanvasMaxX() - canvas.getCanvasMinX();
				for (int i = 1; i < valuesToDraw.length; i++) {
					int x1 = (int) Math.round(canvas.getCanvasMinX() + ((i - 1.0D) * (width / (valuesToDraw.length - 1.0D))));
					int x2 = (int) Math.round(canvas.getCanvasMinX() + (i * (width / (valuesToDraw.length - 1.0D))));
					g.drawLine(x1, valueToPixelY(valuesToDraw[i - 1]), x2, valueToPixelY(valuesToDraw[i]));
				}
				break;
		}
	}

	/**
	 * Asigna el tipo de gráfica a mostrar. El tipo de gráfica está definida por las constantes TYPE 
	 * definidas en esta clase. Por defecto siempre se muestra el tipo LINE
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Asigna el tipo de vista para el histograma a mostrar. No hace un repintado
	 * @param type
	 */
	public void setTypeViewed(int type) {
		this.typeViewed = type;
	}
	
	/**
	 * Convertimos los valores del histograma a visualizar a una escala de
	 * porcentajes que es lo que representa visualmente el componente
	 * @param values
	 */
	private void convertToPercent(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < values.length; i++)
			if (values[i] > max)
				max = values[i];
		
		for (int i = 0; i < values.length; i++)
			values[i] = values[i] / max;
	}
	
	/**
	 * Calculamos el histograma a dibujar en cuestion. Los tipos son:<p>
	 * 	VIEW_LINEAL<br>
	 * 	VIEW_ACUMMULATED<br>
	 * 	VIEW_LOGARITHMIC
	 * 
	 * @param type
	 * @return
	 */
	private double[] recalcHistogram(int type) {
		double min;
		if (histogramValues == null)
			histogramValues = new double[0];
		switch (type) {
			// Calcular la grafica acumulada
			case VIEW_ACUMMULATED:
				// Si ya estan calculados, no los vuelve a calcular
				if (valuesAcummulated != null) return valuesAcummulated;

				valuesAcummulated = new double[histogramValues.length];
				if (valuesAcummulated.length >= 1) {
					valuesAcummulated[0] = histogramValues[0];
					for (int i = 1; i < histogramValues.length; i++)
						valuesAcummulated[i] = histogramValues[i] + valuesAcummulated[i - 1];
				}
				
				convertToPercent(valuesAcummulated);

				return valuesAcummulated;

			// Calcular la grafica logaritmica
			case VIEW_LOGARITHMIC:
				// Si ya estan calculados, no los vuelve a calcular
				if (valuesLogarithmic != null) return valuesLogarithmic;
				
				min = Double.MAX_VALUE;
				for (int i = 0; i < histogramValues.length; i++)
					if (histogramValues[i] < min)
						min = histogramValues[i];
				
				valuesLogarithmic = new double[histogramValues.length];
				for (int i = 0; i < histogramValues.length; i++)
					valuesLogarithmic[i] = java.lang.Math.log(histogramValues[i] - min + 1.0);
				
				convertToPercent(valuesLogarithmic);
				
				return valuesLogarithmic;
				
				
				// Calcular la grafica acumulada
			case VIEW_ACUMMULATEDLOG:
				// Si ya estan calculados, no los vuelve a calcular
				if (valuesAcummulatedLog != null) return valuesAcummulatedLog;

				valuesAcummulatedLog = new double[histogramValues.length];
				if (valuesAcummulatedLog.length >= 1) {
					valuesAcummulatedLog[0] = histogramValues[0];
					for (int i = 1; i < histogramValues.length; i++)
						valuesAcummulatedLog[i] = histogramValues[i] + valuesAcummulatedLog[i - 1];
				}
				
				min = Double.MAX_VALUE;
				for (int i = 0; i < valuesAcummulatedLog.length; i++)
					if (valuesAcummulatedLog[i] < min)
						min = valuesAcummulatedLog[i];
				
				for (int i = 0; i < valuesAcummulatedLog.length; i++)
					valuesAcummulatedLog[i] = java.lang.Math.log(valuesAcummulatedLog[i] - min + 1.0);
				
				convertToPercent(valuesAcummulatedLog);

				return valuesAcummulatedLog;

			// Si no es logaritmica ni acumulada, lo tratamos como si fuera lineal
			default:
				// Si ya estan calculados, no los vuelve a calcular
				if (valuesLineal != null) return valuesLineal;

				valuesLineal = new double[histogramValues.length];
				for (int i = 0; i < histogramValues.length; i++)
					valuesLineal[i] = histogramValues[i];

				convertToPercent(valuesLineal);

				return valuesLineal;
		}
	}
	
	/**
	 * Asigna el histograma a visualizar
	 * @param histogramDrawed
	 */
	public void setHistogramDrawed(double[] histogramDrawed) {
		histogramValues = histogramDrawed;

		valuesLineal = null;
		valuesAcummulated = null;
		valuesLogarithmic = null;
		valuesAcummulatedLog = null;
		
		if (canvas != null)
			canvas.repaint();
	}

	/**
	 * Devuelve la forma en la que se dibujara el histograma. Posibilidades:<br>
	 * TYPE_LINE<br>
	 * TYPE_FILL
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Devuelve la forma que tendrá la grafica del histograma. Posibilidades:<br>
	 * VIEW_LINEAL<br>
	 * VIEW_ACUMMULATED<br>
	 * VIEW_LOGARITHMIC<br>
	 * VIEW_ACUMMULATEDLOG<br>
	 * 
	 * @return the typeViewed
	 */
	public int getTypeViewed() {
		return typeViewed;
	}
	
	/**
	 * @return the histogramValues
	 */
	public double[] getHistogramValues() {
		return histogramValues;
	}
	
	public void firstActions() {}
	public void firstDrawActions() {}
}