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

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.beans.canvas.layers.MinMaxLines;
import org.gvsig.raster.beans.canvas.layers.functions.BaseFunction;
import org.gvsig.raster.beans.canvas.layers.functions.DensitySlicingLine;
import org.gvsig.raster.beans.canvas.layers.functions.StraightLine;
import org.gvsig.raster.datastruct.Histogram;
/**
 * Clase base para los gráficos de histogramas de entrada y salida.
 * 
 * 20/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public abstract class HistogramGraphicBase extends JPanel  {
	private static final long serialVersionUID = 68166939596605313L;
	protected Color minMaxLineColor = Color.WHITE;
	protected Color borderColor     = Color.WHITE;
	protected Color functionColor   = Color.YELLOW;
	
	/**
	 * Clase para tener guardados los valores de estado de una banda del histograma
	 * 
	 * @version 04/03/2008
	 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
	 */
	public class HistogramStatus {
		private double[]         histogram        = new double[] { 0, 0, 3, 4, 5, 8, 7, 18, 45, 36, 21, 36, 12, 23, 23, 40, 17, 10, 5, 1, 0, 0, 0 };
		private double           min              = 0.0D;
		private double           max              = 1.0D;
		private BaseFunction     baseFunction     = null;
		private GraphicHistogram graphicHistogram = null;
		private MinMaxLines      minMaxLines      = null;
		private InfoLayer        infoLayer        = null;

		public HistogramStatus(Color color) {
			graphicHistogram = new GraphicHistogram(color);
			graphicHistogram.setType(GraphicHistogram.TYPE_LINE);
			baseFunction = new StraightLine(functionColor);
			minMaxLines = new MinMaxLines(minMaxLineColor);
			infoLayer = new InfoLayer(Color.WHITE);
		}

		/**
		 * Establece los valores minimo y maximo
		 * @param min
		 * @param max
		 */
		public void setLimits(double min, double max) {
			this.min = min;
			this.max = max;
			infoLayer.setLimits(min, max);
		}
		
		/**
		 * @return the histogram
		 */
		public double[] getHistogram() {
			return histogram;
		}

		/**
		 * @param histogram the histogram to set
		 */
		public void setHistogram(double[] histogram) {
			graphicHistogram.setHistogramDrawed(histogram);
			this.histogram = histogram;
		}

		/**
		 * @return the graphicHistogram
		 */
		public GraphicHistogram getGraphicHistogram() {
			return graphicHistogram;
		}

		/**
		 * @return the minMaxLines
		 */
		public MinMaxLines getMinMaxLines() {
			return minMaxLines;
		}

		/**
		 * @return the straightLine
		 */
		public BaseFunction getBaseFunction() {
			return baseFunction;
		}
		
		/**
		 * Asigna la función dibujada. Por defecto se crea con StraightLine
		 * pero puede ser modificada.
		 * @param baseFunction BaseFunction
		 */
		public void setBaseFunction(BaseFunction baseFunction) {
			this.baseFunction = baseFunction;
		}
		
		/**
		 * Número de cortes cuando es una función level slice
		 * @param level
		 */
		public void setLevel(int level) {
			if(baseFunction instanceof DensitySlicingLine)
				((DensitySlicingLine)baseFunction).setShape(level);
		}

		/**
		 * @return the min
		 */
		public double getMin() {
			return min;
		}

		/**
		 * @return the max
		 */
		public double getMax() {
			return max;
		}

		/**
		 * @return the infoLayer
		 */
		public InfoLayer getInfoLayer() {
			return infoLayer;
		}
	}

	/**
	 * Constantes que identifican un histograma con una banda
	 */
	public final static int   RED             = 0;
	public final static int   GREEN           = 1;
	public final static int   BLUE            = 2;
	public final static int   GRAY            = 3;

	/**
	 * Constante para poder coger el histograma visualizado en ese momento
	 */
	public final static int   DRAWED          = 4;

	protected GCanvas         canvas          = null;

	protected HistogramStatus histogramRed    = null;
	protected HistogramStatus histogramGreen  = null;
	protected HistogramStatus histogramBlue   = null;
	protected HistogramStatus histogramGray   = null;
	protected HistogramStatus histogramDrawed = null;
	private FLyrRasterSE      lyr             = null;

	public HistogramGraphicBase(Histogram hist, FLyrRasterSE lyr, double[] minList, double[] maxList) {
		this.lyr = lyr;
		setHistogram(hist, minList, maxList);
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getCanvas(), BorderLayout.CENTER);
	}

	/**
	 * Crea la lista de objetos dibujables y la guarda en un array
	 */
	public void setHistogram(Histogram hist, double[] minList, double[] maxList) {
		long[][] table = hist.getTable();
		if (lyr.isRenderingAsGray()) {
			setHistogramBand(GRAY, minList, maxList, table);
			setHistogramBand(RED, minList, maxList, table);
			setHistogramBand(GREEN, minList, maxList, table);
			setHistogramBand(BLUE, minList, maxList, table);
		} else {
			setHistogramBand(RED, minList, maxList, table);
			setHistogramDrawed(RED);
			setHistogramBand(GREEN, minList, maxList, table);
			setHistogramBand(BLUE, minList, maxList, table);
		}
	}
	
	/**
	 * Define el histograma para una banda de color
	 * @param COLOR Banda al que se le aplicara el histograma
	 * @param minList Lista de minimos
	 * @param maxList Lista de maximos
	 * @param table Tabla de valores de un histograma
	 */
	private void setHistogramBand(int COLOR, double[] minList, double[] maxList, long[][] table) {
		int position = 0;
		switch (COLOR) {
			case GREEN:
				position = 1;
				break;
			case BLUE:
				position = 2;
				break;
		}

		int[] renderBands = lyr.getRenderBands();

		if (position >= renderBands.length)
			return;

		int band = renderBands[position];

		if ((band < 0) || (band >= table.length))
			return;

		double histogram[] = new double[table[band].length];
		for (int i = 0; i < histogram.length; i++)
			histogram[i] = (double) table[band][i];
		setHistogram(histogram, COLOR);

		HistogramStatus histogramStatus = getHistogramStatus(COLOR);

		if (histogramStatus == null)
			return;

		if ((band >= minList.length) || (band >= maxList.length))
			return;

		histogramStatus.setLimits(minList[band], maxList[band]);
	}

	/**
	 * Asigna el histograma marcado con la interpretación de color indicada.
	 * @param hist Histograma a asignar
	 * @param colorInterp Band a la que corresponde el histograma
	 */
	public void setHistogram(double[] hist, int colorInterp) {
		switch (colorInterp) {
			case GREEN:
				if (histogramGreen == null)
					histogramGreen = new HistogramStatus(Color.green);
				histogramGreen.setHistogram(hist);
				break;
			case BLUE:
				if (histogramBlue == null)
					histogramBlue = new HistogramStatus(Color.blue);
				histogramBlue.setHistogram(hist);
				break;
			case GRAY:
				if (histogramGray == null)
					histogramGray = new HistogramStatus(Color.gray);
				histogramGray.setHistogram(hist);
				break;
			default: // Banda roja
				if (histogramRed == null)
					histogramRed = new HistogramStatus(Color.red);
				histogramRed.setHistogram(hist);
				break;
		}

		if (histogramDrawed == null)
			setHistogramDrawed(colorInterp);
	}
	
	/**
	 * Devuelve el estado del histograma seleccionado
	 * @param colorInterp
	 * @return
	 */
	public HistogramStatus getHistogramStatus(int colorInterp) {
		switch (colorInterp) {
			case RED:
				return histogramRed;
			case GREEN:
				return histogramGreen;
			case BLUE:
				return histogramBlue;
			case GRAY:
				return histogramGray;
			case DRAWED:
				return histogramDrawed;
		}
		return null;
	}
	
	/**
	 * Asigna el histograma dibujado 
	 * @param colorInterp
	 */
	public void setHistogramDrawed(int colorInterp) {
		switch (colorInterp) {
			case RED:
				histogramDrawed = histogramRed;
				break;
			case GREEN:
				histogramDrawed = histogramGreen;
				break;
			case BLUE:
				histogramDrawed = histogramBlue;
				break;
			case GRAY:
				histogramDrawed = histogramGray;
				break;
		}


		if (histogramDrawed != null) {
			getCanvas().replaceDrawableElement(histogramDrawed.getGraphicHistogram());
			getCanvas().replaceDrawableElement(histogramDrawed.getMinMaxLines());
			getCanvas().replaceDrawableElement(histogramDrawed.getBaseFunction(), BaseFunction.class);
			getCanvas().replaceDrawableElement(histogramDrawed.getInfoLayer());
			getCanvas().repaint();
		}
	}
	
	/**
	 * Asigna el tipo de histograma Standard/Cumulative
	 * @param type Tipo de histograma. El valor está definido en las constantes de GraphicHistogram
	 */
	public void setHistogramType(int type) {
		if (histogramDrawed != null)
			histogramDrawed.getGraphicHistogram().setTypeViewed(type);
	}
		
	/**
	 * Asigna el tipo
	 * @param type
	 */
	public void setType(int type) {
		ArrayList elements = getCanvas().getDrawableElements(GraphicHistogram.class);
		for (int i = 0; i < elements.size(); i++)
			((GraphicHistogram) elements.get(i)).setType(type);
	}

	/**
	 * Obtiene el lienzo donde se dibujan las gráficas
	 * @return GCanvas
	 */
	public abstract GCanvas getCanvas();
}