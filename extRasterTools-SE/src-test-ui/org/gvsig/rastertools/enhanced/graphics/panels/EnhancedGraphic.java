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
package org.gvsig.rastertools.enhanced.graphics.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.gvsig.raster.beans.canvas.DrawableElement;
import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.Border;
import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;
import org.gvsig.raster.beans.canvas.layers.functions.BaseFunction;
import org.gvsig.raster.beans.canvas.layers.functions.StraightLine;

/**
 * Gráfico con los elementos para la modificación de un histograma.
 * 
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancedGraphic  extends JPanel {
	private static final long         serialVersionUID = 5431466034535083594L;
	private JComboBox                  lineType = null;
	private JComboBox                  graphicType = null;
	private JComboBox                  histogramBand = null;
	private JSlider                    slider = null;
	private JTextField                 minValue = null;
	private JTextField                 maxValue = null;
	private GCanvas                    canvas = null;
	private JPanel                     north = null;
	private JPanel                     south = null;
	private BaseFunction               dElement = null;
	private double[]                   histogramR = new double[]{0, 0, 3, 4, 5, 8, 7, 18, 45, 36, 21, 36, 12, 23, 23, 40, 17, 10, 5, 1, 0, 0, 0};
	private double[]                   histogramG = new double[]{8, 7, 18, 45, 36, 21, 36, 12, 23, 23, 40, 17, 16, 15, 13, 10, 5, 6, 7, 0, 2, 1};
	private double[]                   histogramB = new double[]{0, 0, 3, 4, 5, 8, 7, 18, 45, 36, 21, 36, 12, 23, 23, 40, 17, 10, 5, 1, 0, 0, 0};
	private int                        min = 0, max = 255;
	
	/**
	 * Constructor. Se asignan los histogramas correspondientes a las bandas
	 * R, G y B
	 * @param hR Histograma de la banda R
	 * @param hG Histograma de la banda G
	 * @param hB Histograma de la banda B
	 */
	public EnhancedGraphic(double[] hR, double[] hG, double[] hB, int min, int max) {
		this.histogramR = hR;
		this.histogramG = hG;
		this.histogramB = hB;
		this.min = min;
		this.max = max;
		init();
	}
	
	/**
	 * Constructor para pruebas. Se toman los valores por defecto de los histogramas
	 */
	public EnhancedGraphic() {
		init();
	}
	
	/**
	 * Añade un elemento dibujable a la lista
	 * @param line Elemento dibujable
	 * @param name Nombre del elemento
	 */
	public void addDrawableElement(DrawableElement line, String name) {
		getLineType().addItem(name);
	}
	
	private void init() {
		createDrawableElements();
		EnhancedGraphicListener listener = new EnhancedGraphicListener(this);
		this.setLayout(new BorderLayout());
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(getCanvas(), BorderLayout.CENTER);
		this.add(getSouthPanel(), BorderLayout.SOUTH);
	}

	/**
	 * Crea la lista de objetos dibujables y la guarda en un array
	 */
	private void createDrawableElements() {		
		dElement = new StraightLine(Color.YELLOW);
		getLineType().addItem("Lineal");
	}
	
	/**
	 * Obtiene el panel con los combos de selección de gráfico y bandas
	 * @return JPanel
	 */
	public JPanel getNorthPanel() {
		if(north == null) {
			north = new JPanel();
			north.setLayout(new BorderLayout());
			
			JPanel combos = new JPanel();
			combos.setLayout(new GridBagLayout());
			combos.add(getLineType());
			combos.add(getHistogramBand());
			combos.add(getGraphicType());
			
			north.add(combos, BorderLayout.NORTH);
			north.add(getSlider(), BorderLayout.CENTER);
		}
		return north;
	}
	
	/**
	 * Obtiene el panel con las entradas de texto para los valores
	 * máximo y mínimo
	 * @return JPanel
	 */
	public JPanel getSouthPanel() {
		if(south == null) {
			south = new JPanel();
			south.setLayout(new GridBagLayout());
			GridBagConstraints gb = new GridBagConstraints();
			gb.weightx = 1;
			
			gb.anchor = GridBagConstraints.WEST;
			south.add(getMinValue(), gb);
			
			gb.anchor = GridBagConstraints.EAST;
			south.add(getMaxValue(), gb);
		}
		return south;
	}
	
	/**
	 * Obtiene el lienzo donde se dibujan las gráficas
	 * @return GCanvas
	 */
	public GCanvas getCanvas() {
		if(canvas == null) {
			GraphicHistogram gHistR = new GraphicHistogram(histogramR, Color.RED);
			gHistR.setType(GraphicHistogram.TYPE_LINE);
			
			canvas = new GCanvas(Color.BLACK);
			canvas.addDrawableElement(new Border(Color.WHITE));
			canvas.addDrawableElement(gHistR);
			canvas.addDrawableElement(dElement); 
		}
		return canvas;
	}

	/**
	 * Obtiene el combo con la banda seleccionada
	 * @return JComboBox
	 */
	public JComboBox getHistogramBand() {
		if(histogramBand == null) {
			histogramBand = new JComboBox();
			histogramBand.addItem("R");
			histogramBand.addItem("G");
			histogramBand.addItem("B");
		}
		return histogramBand;
	}

	/**
	 * Obtiene el JComboBox que selecciona el tipo de línea
	 * @return JComboBox
	 */
	public JComboBox getLineType() {
		if(lineType == null) {
			lineType = new JComboBox();
		}
		return lineType;
	}
	
	/**
	 * Obtiene el JComboBox que selecciona el tipo de gráfico
	 * @return JComboBox
	 */
	public JComboBox getGraphicType() {
		if(graphicType == null) {
			graphicType = new JComboBox();
			graphicType.addItem("Line");
			graphicType.addItem("Fill");
		}
		return graphicType;
	}

	/**
	 * Obtiene el campo de texto con el valor máximo
	 * @return JTextField
	 */
	public JTextField getMaxValue() {
		if(maxValue == null) {
			maxValue = new JTextField(String.valueOf(max));
			maxValue.setPreferredSize(new Dimension(34, 20));
		}
		return maxValue;
	}

	/**
	 * Obtiene el campo de texto con el valor mínimo
	 * @return JTextField
	 */
	public JTextField getMinValue() {
		if(minValue == null) {
			minValue = new JTextField(String.valueOf(min));
			minValue.setPreferredSize(new Dimension(34, 20));
		}
		return minValue;
	}

	/**
	 * Obtiene el slider 
	 * @return JSlider
	 */
	public JSlider getSlider() {
		if(slider == null) {
			slider = new JSlider();
		}
		return slider;
	}
	
	/**
	 * Obtiene el histograma correspondiente a una banda de color
	 * @param value Banda de la cual se quiere obtener el histograma
	 * @return histograma
	 */
	public double[] getHistogram(String value) {
		if(value.equals("R"))
			return histogramR;
		if(value.equals("G"))
			return histogramG;
		if(value.equals("B"))
			return histogramB;
		return null;
	}
}
