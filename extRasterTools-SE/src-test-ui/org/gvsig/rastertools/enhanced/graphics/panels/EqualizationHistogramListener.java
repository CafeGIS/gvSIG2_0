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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.raster.beans.canvas.layers.GraphicHistogram;

/**
 * Listener para el panel EnhancedGraphic. Gestiona los eventos de todos los
 * componentes de este panel.
 * 
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EqualizationHistogramListener  implements ActionListener, ChangeListener {

	private EqualizationHistogram             panel = null;
	
	/**
	 * Constructor
	 * @param panel
	 */
	public EqualizationHistogramListener(EqualizationHistogram panel) {
		this.panel = panel;
		panel.getLineType().addActionListener(this);
		panel.getHistogramBand().addActionListener(this);
		panel.getGraphicType().addActionListener(this);
		panel.getSlider().addChangeListener(this);
		panel.getMinValue().addActionListener(this);
		panel.getMaxValue().addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panel.getMinValue()) {
			try {
//				panel.getMinMaxLines().setMin(Double.valueOf(panel.getMinValue().getText()).doubleValue());
			}catch (NumberFormatException ex) {
				
			}
		}
		
		if(e.getSource() == panel.getMaxValue()) {
			try {
//				panel.getMinMaxLines().setMax(Double.valueOf(panel.getMaxValue().getText()).doubleValue());
			}catch (NumberFormatException ex) {
				
			}
		}
		
		if(e.getSource() == panel.getLineType()) {
			
		}
		
		if (e.getSource() == panel.getHistogramBand()) {
			// Eliminamos el histograma que hay y añadimos el nuevo
			ArrayList elements = panel.getCanvas().getDrawableElements(GraphicHistogram.class);
			for (int i = 0; i < elements.size(); i++) {
				Object value = panel.getHistogramBand().getSelectedItem();
				if (value instanceof String) {
					double[] hist = panel.getHistogram((String) value);
					Color colorH = Color.RED;
					if (value.equals("G"))
						colorH = Color.GREEN;
					if (value.equals("B"))
						colorH = Color.BLUE;
					GraphicHistogram gHist = new GraphicHistogram(hist, colorH);
					gHist.setCanvas(panel.getCanvas());
					int lineType = GraphicHistogram.TYPE_LINE;
					if (panel.getGraphicType().getSelectedItem().equals("Fill"))
						lineType = GraphicHistogram.TYPE_FILL;
					gHist.setType(lineType);
					panel.getCanvas().replaceDrawableElement(gHist);
					panel.getCanvas().repaint();
				}
			}
		}

		if (e.getSource() == panel.getGraphicType()) {
			ArrayList elements = panel.getCanvas().getDrawableElements(GraphicHistogram.class);
			for (int i = 0; i < elements.size(); i++) {
				Object value = panel.getGraphicType().getSelectedItem();
				if (value instanceof String) {
					if (value.equals("Line"))
						((GraphicHistogram) elements.get(i)).setType(GraphicHistogram.TYPE_LINE);
					if (value.equals("Fill"))
						((GraphicHistogram) elements.get(i)).setType(GraphicHistogram.TYPE_FILL);
					panel.getCanvas().repaint();
				}
			}
		}
	}

	/**
	 * Acciones que se ejecutan al variar el slider de la gráfica
	 */
	public void stateChanged(ChangeEvent e) {
	}
}