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
package org.gvsig.rastertools.enhanced.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.enhanced.graphics.HistogramGraphicBase;

import com.iver.utiles.swing.JComboBox;
/**
 * Panel con las selección de opciones.
 * 
 * 20/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class SelectorsPanel extends JPanel {
	private static final long serialVersionUID = 3453973982901626644L;
	private JComboBox            enhancedType  = null;
	private JComboBox            histogramType = null;
	private JComboBox            drawType      = null;
	private JComboBox            band          = null;
	private HistogramGraphicBase graphicBase   = null;
	// private JComboBox sourcesMatchingPanel = null;

	/**
	 * Crea una instancia del panel GraphicsPanel
	 * @param lyr
	 * @param list
	 * @param graphicBase Grafica donde estan los histogramas, para saber como
	 *          rellenar el combo de colores
	 */
	public SelectorsPanel(FLyrRasterSE lyr, HistogramGraphicBase graphicBase) {
		this.graphicBase = graphicBase;
		init(lyr);
	}
	
	/**
	 * Inicialización de los controles gráficos.
	 */
	private void init(FLyrRasterSE lyr) {
		GridBagConstraints gridBagConstraints = null;

		setBorder(BorderFactory.createTitledBorder(null, null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

		setLayout(new GridBagLayout());
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "operation")), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 2);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		add(getEnhancedType(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "band")), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		add(getBand(lyr), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "drawing_type")), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		add(getDrawType(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "histogram_type")), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		add(getHistogramType(), gridBagConstraints);
		
//		gridBagConstraints = new GridBagConstraints();
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy = 2;
//		gridBagConstraints.gridwidth = 4;
//		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.anchor = GridBagConstraints.NORTH;
//		gridBagConstraints.weighty = 1.0;
//		gridBagConstraints.insets = new Insets(2, 5, 5, 5);
//		add(getSourcesMatchingPanel(list), gridBagConstraints);
	}
	
//	/**
//	 * Obtiene la lista con las capas cargadas en el TOC para la selección de la 
//	 * que necesitemos en la funcionalidad de histogram matching
//	 * @return JPanel
//	 */
//	private JComboBox getSourcesMatchingPanel(ArrayList list) {
//		if (sourcesMatchingPanel == null) {
//			sourcesMatchingPanel = new JComboBox();
//			for (int i = 0; i < list.size(); i++)
//				sourcesMatchingPanel.addItem(((FLyrRasterSE) list.get(i)).getName());
//		}
//		return sourcesMatchingPanel;
//	}
	
	/**
	 * Obtiene el tipo de histograma
	 * @return JComboBox
	 */
	public JComboBox getHistogramType() {
		if (histogramType == null) {
			histogramType = new JComboBox();
			histogramType.addItem("Standard");
			histogramType.addItem("Cumulative");
			histogramType.addItem("Logarithmic");
			histogramType.addItem("Cumulative Logarithmic");
		}
		return histogramType;
	}
	
	/**
	 * Obtiene el tipo de dibujado de histograma (lineas, barras, ...)
	 * @return JComboBox
	 */
	public JComboBox getDrawType() {
		if (drawType == null) {
			drawType = new JComboBox();
			drawType.addItem("Line");
			drawType.addItem("Fill");
		}
		return drawType;
	}
	
	/**
	 * Obtiene la banda que representa el histograma
	 * @return JComboBox
	 */
	public JComboBox getBand(FLyrRasterSE lyr) {
		if (band == null) {
			band = new JComboBox();
			
			if (lyr.isRenderingAsGray()) {
				band.addItem(DatasetColorInterpretation.GRAY_BAND);
			} else {
				if (graphicBase.getHistogramStatus(HistogramGraphicBase.RED) != null)
					band.addItem(DatasetColorInterpretation.RED_BAND);
				if (graphicBase.getHistogramStatus(HistogramGraphicBase.GREEN) != null)
					band.addItem(DatasetColorInterpretation.GREEN_BAND);
				if (graphicBase.getHistogramStatus(HistogramGraphicBase.BLUE) != null)
					band.addItem(DatasetColorInterpretation.BLUE_BAND);
			}
		}
		return band;
	}

	/**
	 * Obtiene el tipo de función a aplicar (ecualización, realce lineal, ...)
	 * @return JComboBox
	 */
	public JComboBox getEnhancedType() {
		if (enhancedType == null) {
			enhancedType = new JComboBox();
			enhancedType.addItem("Lineal");
			enhancedType.addItem("Square-root");
			enhancedType.addItem("Logaritmic");
			enhancedType.addItem("Exponential");
			enhancedType.addItem("Level-slice");
//			enhancedType.addItem("Gaussian");
//			enhancedType.addItem("Equalization");
		}
		return enhancedType;
	}

	/**
	 * Selecciona un item del combo EnhancedType sin lanzar un evento
	 * @param type
	 */
	public void setSelectedEnhancedType(String type) {
		if (getEnhancedType().getSelectedItem() != type) {
			ActionListener[] copyActionListeners = getEnhancedType().getActionListeners();
			for (int i = 0; i < copyActionListeners.length; i++)
				getEnhancedType().removeActionListener(copyActionListeners[i]);
	
			getEnhancedType().setSelectedItem(type);

			for (int i = 0; i < copyActionListeners.length; i++)
				getEnhancedType().addActionListener(copyActionListeners[i]);
		}
	}
}