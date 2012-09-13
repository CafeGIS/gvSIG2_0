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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.enhanced.graphics.InputHistogram;
import org.gvsig.rastertools.enhanced.graphics.OutputHistogram;
/**
 * Panel que contiene las gráficas para la modificación del realce.
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GraphicsPanel extends JPanel implements IUserPanelInterface, KeyListener {
	private static final long   serialVersionUID = 1L;
	private InputHistogram      input            = null;
	private OutputHistogram     output           = null;
	private JPanel              controls         = null;

	private JLabel              llevels          = null;
	private JFormattedTextField tlevels          = null;
	private JCheckBox           cbRGB            = null;

	private Histogram           hist             = null;
	private Histogram           histRGB          = null;

	private double[]            minList          = null;
	private double[]            maxList          = null;
	private FLyrRasterSE        lyr              = null;
	

	/**
	 * Crea una instancia del panel GraphicsPanel
	 */
	public GraphicsPanel(FLyrRasterSE lyr) {
		try {
			this.lyr = lyr;
			hist = lyr.getDataSource().getHistogram();
			histRGB = Histogram.convertHistogramToRGB(hist);
			minList = lyr.getDataSource().getStatistics().getMin();
			maxList = lyr.getDataSource().getStatistics().getMax();
			if (minList == null)
				minList = new double[] { 0 };
			if (maxList == null)
				maxList = new double[] { 255 };
		} catch (HistogramException e) {
			hist = null;
		} catch (InterruptedException e) {
			hist = null;
		}

		init();

		if (lyr.getDataType()[0] == IBuffer.TYPE_BYTE)
			getRGB().setSelected(true);
		else
			getRGB().setEnabled(false);
	}
	
	/**
	 * Inicialización de los controles gráficos.
	 */
	private void init() {
		setLayout(new GridBagLayout());
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "histograms"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		GridBagConstraints gridBagConstraints = null;

		JLabel label1 = new JLabel(RasterToolsUtil.getText(null, "input_hist"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label1.setPreferredSize(new Dimension(150, 14));
		add(label1, gridBagConstraints);
		
		JLabel label2 = new JLabel(RasterToolsUtil.getText(null, "output_hist"));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
		label2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		label2.setPreferredSize(new Dimension(150, 14));
		add(label2, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		add(getInputHistogram(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		add(getControls(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		add(getOutputHistogram(), gridBagConstraints);		
		
		setLevelsEnabled(false);
	}
	
	/**
	 * Obtiene el histograma de entrada
	 * @return Histogram
	 */
	public Histogram getHistogram() {
		return hist;
	}
	
	/**
	 * Obtiene el gráfico con el histograma de entrada
	 * @return InputHistogram
	 */
	public InputHistogram getInputHistogram() {
		if (input == null) {
			input = new InputHistogram(hist, lyr, minList, maxList);
			input.setHistogram(hist, minList, maxList);
		}
		return input;
	}
	
	/**
	 * Actualiza el histograma a visualizar por si se ha cambiado de RGB a normal.
	 */
	public void updateHistogram() {
		if (getRGB().isSelected()) {
			double[] min = new double[minList.length];
			double[] max = new double[maxList.length];
			for (int i = 0; i < minList.length; i++) {
				min[i] = 0.0;
				max[i] = 255.0;
			}
			input.setHistogram(histRGB, min, max);
		} else {
			input.setHistogram(hist, minList, maxList);
		}
	}
	
	/**
	 * Obtiene el gráfico con el histograma de salida
	 * @return OutputHistogram
	 */
	public OutputHistogram getOutputHistogram() {
		if (output == null) {
			output = new OutputHistogram(hist, lyr, minList, maxList);
		}
		return output;
	}
	
	/**
	 * Obtiene el panel con los controles
	 * @return JPanel
	 */
	public JPanel getControls() {
		if(controls == null) {
			controls = new JPanel();
			controls.setLayout(new GridBagLayout());
			
			llevels = new JLabel(RasterToolsUtil.getText(null, "levels"));
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 5, 0, 5);
			gbc.fill = GridBagConstraints.BOTH;
			controls.add(llevels, gbc);
			gbc.gridy = 1;
			controls.add(getLevels(), gbc);
			
			gbc.gridy = 2;
			controls.add(getRGB(), gbc);
		}
		return controls;
	}
	
	/**
	 * Activa o desactiva el control de niveles de posterización en level-slice
	 * de recorte.
	 * @param enabled
	 */
	public void setLevelsEnabled(boolean enabled) {
		getLevels().setEnabled(enabled);
		if(enabled)
			getLevels().setBackground(Color.WHITE);
		else
			getLevels().setBackground(getControls().getBackground());
	}
	
	/**
	 * Obtiene el número de niveles.
	 * @return
	 */
	public JFormattedTextField getLevels() {
		if (tlevels == null) {
			NumberFormat doubleDisplayFormat = NumberFormat.getNumberInstance();
			doubleDisplayFormat.setMinimumFractionDigits(0);
			NumberFormat doubleEditFormat = NumberFormat.getNumberInstance();

			tlevels = new JFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(doubleDisplayFormat), new NumberFormatter(doubleDisplayFormat), new NumberFormatter(doubleEditFormat)));

			tlevels.addKeyListener(this);
		}
		return tlevels;
	}
	
	/**
	 * Obtiene el checkbox que indica si se va a tratar la imagen como un checkbox
	 * @return
	 */
	public JCheckBox getRGB() {
		if (cbRGB == null) {
			cbRGB = new JCheckBox("RGB");
		}
		return cbRGB;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return "";
	}

	/**
	 * Asigna el listener para gestionar el evento de movimiento de gráficos
	 * @param listener
	 */
	public void setListener(EnhancedListener listener) {
		getInputHistogram().setListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		Long lValue = (Long) getLevels().getValue();
		int value = lValue.intValue();

		boolean change = false;
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				value++;
				change = true;
				break;
			case KeyEvent.VK_DOWN:
				value--;
				change = true;
				break;
		}

		if (change) {
			if (value > 30)
				value = 30;
	
			if (value < 2)
				value = 2;
	
			if (!getLevels().getValue().equals(new Long(value))) {
				getLevels().setValue(new Long(value));
				getLevels().postActionEvent();
			}
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}