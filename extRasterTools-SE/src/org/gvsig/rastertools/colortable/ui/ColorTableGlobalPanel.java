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
package org.gvsig.rastertools.colortable.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.rastertools.colortable.data.ColorTableData;
import org.gvsig.rastertools.statistics.StatisticsProcess;
/**
 * Panel que contiene las opciones sobre los maximos y minimos de la tabla de color,
 * checkbox de limites, si esta interpolado o si esta activa la tabla de color 
 * 
 * @version 07/01/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorTableGlobalPanel extends BasePanel implements ActionListener, PropertyChangeListener, IProcessActions {
	private static final long   serialVersionUID     = 1L;
	private JCheckBox           checkBoxInterpolated = null;
	private JCheckBox           checkBoxEnabled      = null;
	private ColorTableData      colorTableData       = null;

	private JCheckBox           checkBoxLimits       = null;
	private JFormattedTextField textFieldMinim       = null;
	private JFormattedTextField textFieldMaxim       = null;
	private JLabel              labelMinim           = null;
	private JLabel              labelMaxim           = null;
	private JButton             buttonStatistics     = null;

	private NumberFormat        doubleDisplayFormat  = null;
	private NumberFormat        doubleEditFormat     = null;
	private FLayer              fLayer               = null;
	
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public ColorTableGlobalPanel() {
		init();
		translate();
	}
	
	public ColorTableGlobalPanel(ColorTableData colorTableData) {
		doubleDisplayFormat = NumberFormat.getNumberInstance();
		doubleDisplayFormat.setMinimumFractionDigits(0);
		doubleEditFormat = NumberFormat.getNumberInstance();

		this.colorTableData = colorTableData;
		init();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 5);
		add(getButtonStatistics(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 2);
		add(getTextFieldMinim(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 2);
		add(getTextFieldMaxim(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 5, 2, 2);
		add(getLabelMinim(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 2, 2, 2);
		add(getLabelMaxim(), gridBagConstraints);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 5, 5, 2);
		panel.add(getCheckBoxEnabled(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 5, 5);
		panel.add(getCheckBoxInterpolated(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 5, 5);
		panel.add(getCheckBoxLimits(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
		add(panel, gridBagConstraints);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}

	/**
	 * Obtiene el jCBInterpolated
	 * @return
	 */
	private JCheckBox getCheckBoxInterpolated() {
		if (checkBoxInterpolated == null) {
			checkBoxInterpolated = new JCheckBox(getText(this, "interpolado"));
			checkBoxInterpolated.setSelected(true);
			checkBoxInterpolated.addActionListener(this);
		}
		return checkBoxInterpolated;
	}

	/**
	 * Obtiene el jCBInterpolated
	 * @return
	 */
	private JButton getButtonStatistics() {
		if (buttonStatistics == null) {
			buttonStatistics = new JButton(getText(this, "recalc_stats"));
			buttonStatistics.addActionListener(this);
		}
		return buttonStatistics;
	}

	/**
	 * Obtiene el jCBInterpolated
	 * @return
	 */
	private JCheckBox getCheckBoxEnabled() {
		if (checkBoxEnabled == null) {
			checkBoxEnabled = new JCheckBox(getText(this, "activar_tablas_color"));
			checkBoxEnabled.setSelected(true);
			checkBoxEnabled.addActionListener(this);
		}
		return checkBoxEnabled;
	}
	
	/**
	 * Activa el control de interpolacion
	 * @param b
	 */
	public void setCheckBoxInterpolated(boolean b) {
		getCheckBoxInterpolated().setSelected(b);
		colorTableData.setInterpolated(b);
	}
	
	/**
	 * Activa el uso de tablas de color para el panel
	 * @param b
	 */
	public void setCheckBoxEnabled(boolean b) {
		getCheckBoxEnabled().setSelected(b);
		colorTableData.setEnabled(b);
	}
	
	private boolean isInterpolated() {
		return getCheckBoxInterpolated().isSelected();
	}
	
	private boolean isCheckEnabled() {
		return getCheckBoxEnabled().isSelected();
	}
	
	/**
	 * Activa/Desactiva los componentes de las pestañas segun la pestaña selecionada
	 * @param enabled
	 */
	public void setEnabledPanel(boolean enabled) {
		getLabelMinim().setEnabled(enabled);
		getTextFieldMinim().setEnabled(enabled);
		getLabelMaxim().setEnabled(enabled);
		getTextFieldMaxim().setEnabled(enabled);
		getCheckBoxLimits().setEnabled(enabled);
		getCheckBoxInterpolated().setEnabled(enabled);
		getButtonStatistics().setEnabled(enabled);
		if (enabled)
			setLimitsEnabled(getCheckBoxLimits().isSelected());
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getCheckBoxInterpolated()) {
			colorTableData.setInterpolated(isInterpolated());
			return;
		}

		if (e.getSource() == getCheckBoxEnabled()) {
			colorTableData.setEnabled(isCheckEnabled());
			return;
		}

		if (e.getSource() == getCheckBoxLimits()) {
			setLimitsEnabled(getCheckBoxLimits().isSelected());
			return;
		}
		
		if ((e.getSource() == getButtonStatistics()) &&
				(fLayer != null)) {
			RasterProcess process = new StatisticsProcess();
			process.setActions(this);
			process.addParam("layer", fLayer);
			process.addParam("force", new Boolean(false));
			process.start();
			return;
		}
	}
	
	/**
	 * Indica si los limites estaran activos para la tabla de color activa
	 * @param enabled
	 */
	public void setLimitsEnabled(boolean enabled) {
		colorTableData.setLimitsEnabled(enabled);
		getCheckBoxLimits().setSelected(enabled);
		getLabelMaxim().setEnabled(enabled);
		getLabelMinim().setEnabled(enabled);
		getTextFieldMinim().setEnabled(enabled);
		getTextFieldMaxim().setEnabled(enabled);
		getButtonStatistics().setEnabled(enabled);
	}

	/**
	 * @return the checkBoxLimits
	 */
	private JCheckBox getCheckBoxLimits() {
		if (checkBoxLimits == null) {
			checkBoxLimits = new JCheckBox();
			checkBoxLimits.setText(getText(this, "ajustar_limites"));
			checkBoxLimits.addActionListener(this);
		}
		return checkBoxLimits;
	}

	/**
	 * @return the textFieldMinim
	 */
	private JFormattedTextField getTextFieldMinim() {
		if (textFieldMinim == null) {
			textFieldMinim = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(doubleDisplayFormat),
					new NumberFormatter(doubleDisplayFormat),
					new NumberFormatter(doubleEditFormat)));
			textFieldMinim.setText("0");
			textFieldMinim.setEnabled(false);
			textFieldMinim.addPropertyChangeListener("value", this);
		}
		return textFieldMinim;
	}

	/**
	 * @return the textFieldMaxim
	 */
	private JFormattedTextField getTextFieldMaxim() {
		if (textFieldMaxim == null) {
			textFieldMaxim = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(doubleDisplayFormat),
					new NumberFormatter(doubleDisplayFormat),
					new NumberFormatter(doubleEditFormat)));
			textFieldMaxim.setText("255");
			textFieldMaxim.setEnabled(false);
			textFieldMaxim.addPropertyChangeListener("value", this);
		}
		return textFieldMaxim;
	}

	/**
	 * @return the labelMinim
	 */
	private JLabel getLabelMinim() {
		if (labelMinim == null) {
			labelMinim = new JLabel();
			labelMinim.setText(getText(this, "minimo") + ":");
			labelMinim.setEnabled(false);
		}
		return labelMinim;
	}

	/**
	 * @return the labelMaxim
	 */
	private JLabel getLabelMaxim() {
		if (labelMaxim == null) {
			labelMaxim = new JLabel();
			labelMaxim.setText(getText(this, "maximo") + ":");
			labelMaxim.setEnabled(false);
		}
		return labelMaxim;
	}
	
	private void setLimitMaxim(double limit) {
		getTextFieldMaxim().setValue(Double.valueOf(limit));
		colorTableData.setMaxim(limit);
	}
	
	private void setLimitMinim(double limit) {
		getTextFieldMinim().setValue(Double.valueOf(limit));
		colorTableData.setMinim(limit);
	}

	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getTextFieldMaxim()) {
			double max = ((Number) getTextFieldMaxim().getValue()).doubleValue();
			colorTableData.setMaxim(max);
			return;
		}
		
		if (evt.getSource() == getTextFieldMinim()) {
			double min = ((Number) getTextFieldMinim().getValue()).doubleValue();
			colorTableData.setMinim(min);
			return;
		}
	}
	
	/**
	 * Establece los rangos de minimo y maximo obtenidos de las estadisticas
	 * de la capa.
	 */
	private void putStatistics() {
		DatasetListStatistics statistics = ((FLyrRasterSE) fLayer).getDataSource().getStatistics();
		if (statistics.isCalculated()) {
			// En caso de que al hacer la conversion a byte sea igual al valor double
			// podremos pensar que estamos en un rango RGB y por lo tanto definir
			// el limite segun los valores RGB, en caso contrario, no deberiamos usar
			// el valor RGB para mayor fiabilidad
			if (	((FLyrRasterSE) fLayer).getDataType()[0] == IBuffer.TYPE_BYTE &&
					((byte) statistics.getMinimun() == (double) statistics.getMinimun()) &&
					((byte) statistics.getMaximun() == (double) statistics.getMaximun())) {
				setLimitMaxim(statistics.getMaximunRGB());
				setLimitMinim(statistics.getMinimunRGB());
			} else {
				setLimitMaxim(statistics.getMaximun());
				setLimitMinim(statistics.getMinimun());
			}
		}
	}

	/**
	 * Establece que capa se va a usar en el panel para aplicarle la tabla de
	 * color
	 * @param fLayer
	 */
	public void setLayer(FLayer fLayer) {
		this.fLayer = fLayer;
		putStatistics();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		putStatistics();
	}

	public void interrupted() {}

}