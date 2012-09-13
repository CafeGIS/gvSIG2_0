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
package org.gvsig.raster.gui.preferences.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.BasePanel;
/**
 * Panel para la gestión del valor NoData en el panel de preferencias de gvSIG.
 *
 * @version 10/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceNoData extends BasePanel {
	private static final long   serialVersionUID = -8964531984609056094L;
	private JCheckBox           jCheckBoxNoDataEnabled = null;
	private JLabel              jLabelGeneralValue     = null;
	private JFormattedTextField textFieldGeneralValue  = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public PreferenceNoData() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		setBorder(BorderFactory.createTitledBorder(getText(this, "nodata")));
		getCheckBoxNoDataEnabled().setText(getText(this, "tratar_nodata_transparente"));
		getCheckBoxNoDataEnabled().setToolTipText(getCheckBoxNoDataEnabled().getText());
		getLabelGeneralValue().setText(getText(this, "valor_general") + ":");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		setLayout(new GridBagLayout());

		int posy = 0;
		GridBagConstraints gridBagConstraints;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = posy;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		add(getCheckBoxNoDataEnabled(), gridBagConstraints);

		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getLabelGeneralValue(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getTextFieldGeneralValue(), gridBagConstraints);
	}

	private JFormattedTextField getTextFieldGeneralValue() {
		if (textFieldGeneralValue == null) {
			NumberFormat integerFormat = NumberFormat.getNumberInstance();
			integerFormat.setParseIntegerOnly(false);
			integerFormat.setMinimumFractionDigits(0);
			integerFormat.setMaximumFractionDigits(3);
			textFieldGeneralValue = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat)));
		}
		return textFieldGeneralValue;
	}

	private JCheckBox getCheckBoxNoDataEnabled() {
		if (jCheckBoxNoDataEnabled == null) {
			jCheckBoxNoDataEnabled = new JCheckBox();
			jCheckBoxNoDataEnabled.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jCheckBoxNoDataEnabled.setMargin(new Insets(0, 0, 0, 0));
		}
		return jCheckBoxNoDataEnabled;
	}

	private JLabel getLabelGeneralValue() {
		if (jLabelGeneralValue == null) {
			jLabelGeneralValue = new JLabel();
		}
		return jLabelGeneralValue;
	}

	/**
	 * Establece los valores por defecto
	 */
	public void initializeDefaults() {
		Boolean enabled = (Boolean) Configuration.getDefaultValue("nodata_transparency_enabled");
		getCheckBoxNoDataEnabled().setSelected(enabled.booleanValue());
		getTextFieldGeneralValue().setValue((Double) Configuration.getDefaultValue("nodata_value"));
	}

	/**
	 * Carga los valores definidos por el usuario
	 */
	public void initializeValues() {
		Boolean enabled = Configuration.getValue("nodata_transparency_enabled", Boolean.FALSE);
		getCheckBoxNoDataEnabled().setSelected(enabled.booleanValue());
		getTextFieldGeneralValue().setValue(Configuration.getValue("nodata_value", Double.valueOf(RasterLibrary.defaultNoDataValue)));
	}

	/**
	 * Guarda los valores definidos por el usuario
	 */
	public void storeValues() {
		Configuration.setValue("nodata_transparency_enabled", Boolean.valueOf(getCheckBoxNoDataEnabled().isSelected()));
		try {
			if(getTextFieldGeneralValue().getValue() instanceof Double)
				Configuration.setValue("nodata_value", (Double) getTextFieldGeneralValue().getValue());
			if(getTextFieldGeneralValue().getValue() instanceof String)
				Configuration.setValue("nodata_value", new Double((String)getTextFieldGeneralValue().getValue()));
			if(getTextFieldGeneralValue().getValue() instanceof Long)
				Configuration.setValue("nodata_value", new Double(((Long)getTextFieldGeneralValue().getValue()).longValue()));
		} catch (NumberFormatException e) {
		}
	}
}