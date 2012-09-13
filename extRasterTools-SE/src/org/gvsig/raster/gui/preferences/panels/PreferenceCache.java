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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.BasePanel;
/**
 * PreferenceCache es la clase que se encarga de configurar en RasterPreferences
 * las opciones de cacheamiento de capas en Raster.
 *
 * @version 14/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceCache extends BasePanel {
	protected static final long serialVersionUID      = 1L;
	private JLabel              labelWarning          = null;
	private JLabel              labelBlockHeight      = null;
	private JComboBox           comboBoxBlockHeight   = null;
	private JLabel              labelCacheSize        = null;
	private JFormattedTextField textFieldCacheSize    = null;
	private JLabel              labelPagsPerGroup     = null;
	private JFormattedTextField textFieldPagsPerGroup = null;
	private JLabel              labelPageSize         = null;
	private JFormattedTextField textFieldPageSize     = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public PreferenceCache() {
		init();
		translate();
	}

	/**
	 * Define todas las traducciones de PreferenceCache
	 */
	protected void translate() {
		setBorder(BorderFactory.createTitledBorder(getText(this, "cache")));
		getLabelWarning().setText(getText(this, "preference_cache_warning"));
		getLabelCacheSize().setText(getText(this, "tamanyo") + ":");
		getLabelPagsPerGroup().setText(getText(this, "paginas_grupo") + ":");
		getLabelPageSize().setText(getText(this, "tamanyo_pagina") + ":");
		getLabelBlockHeight().setText(getText(this, "bloques_procesos") + ":");
	}

	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		add(getLabelWarning(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getLabelCacheSize(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getLabelPagsPerGroup(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getLabelPageSize(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 5, 2);
		add(getLabelBlockHeight(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getTextFieldCacheSize(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 5, 5);
		add(getComboBoxBlockHeight(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getTextFieldPageSize(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getTextFieldPagsPerGroup(), gridBagConstraints);
	}

	private JLabel getLabelWarning() {
		if (labelWarning == null) {
			labelWarning = new JLabel();
			labelWarning.setForeground(new Color(255, 0, 0));
			labelWarning.setHorizontalAlignment(SwingConstants.CENTER);
			labelWarning.setPreferredSize(new Dimension(0, 32));
		}
		return labelWarning;
	}

	private JFormattedTextField getTextFieldCacheSize() {
		if (textFieldCacheSize == null) {
			NumberFormat integerFormat = NumberFormat.getNumberInstance();
			integerFormat.setParseIntegerOnly(true);
			textFieldCacheSize = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat)));
		}
		return textFieldCacheSize;
	}

	private JFormattedTextField getTextFieldPagsPerGroup() {
		if (textFieldPagsPerGroup == null) {
			NumberFormat integerFormat = NumberFormat.getNumberInstance();
			integerFormat.setParseIntegerOnly(true);
			textFieldPagsPerGroup = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat)));
		}
		return textFieldPagsPerGroup;
	}

	private JFormattedTextField getTextFieldPageSize() {
		if (textFieldPageSize == null) {
			NumberFormat integerFormat = NumberFormat.getNumberInstance();
			integerFormat.setParseIntegerOnly(true);
			textFieldPageSize = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat)));
		}
		return textFieldPageSize;
	}

	private JLabel getLabelCacheSize() {
		if (labelCacheSize == null)
			labelCacheSize = new JLabel();
		return labelCacheSize;
	}

	private JLabel getLabelPagsPerGroup() {
		if (labelPagsPerGroup == null)
			labelPagsPerGroup = new JLabel();
		return labelPagsPerGroup;
	}

	private JLabel getLabelPageSize() {
		if (labelPageSize == null)
			labelPageSize = new JLabel();
		return labelPageSize;
	}

	private JLabel getLabelBlockHeight() {
		if (labelBlockHeight == null)
			labelBlockHeight = new JLabel();
		return labelBlockHeight;
	}

	private JComboBox getComboBoxBlockHeight() {
		if (comboBoxBlockHeight == null) {
			comboBoxBlockHeight = new JComboBox();
			comboBoxBlockHeight.setModel(new DefaultComboBoxModel(new String[] { "128", "256", "512", "1024", "2048", "4096" }));
		}
		return comboBoxBlockHeight;
	}

	/**
	 * Establece los valores por defecto de la Cache
	 */
	public void initializeDefaults() {
		getTextFieldCacheSize().setValue(Configuration.getDefaultValue("cache_size"));
		getTextFieldPageSize().setValue(Configuration.getDefaultValue("cache_pagesize"));
		getTextFieldPagsPerGroup().setValue(Configuration.getDefaultValue("cache_pagspergroup"));

		Integer blockHeight = (Integer) Configuration.getDefaultValue("cache_blockheight");
		for (int i = 0; i < getComboBoxBlockHeight().getItemCount(); i++)
			if (getComboBoxBlockHeight().getItemAt(i).toString().equals(blockHeight.toString())) {
				getComboBoxBlockHeight().setSelectedIndex(i);
				break;
			}
	}

	/**
	 * Establece los valores que ha definido el usuario de la Cache
	 */
	public void initializeValues() {
		getTextFieldCacheSize().setValue(Configuration.getValue("cache_size", Long.valueOf(RasterLibrary.cacheSize)));
		getTextFieldPageSize().setValue(Configuration.getValue("cache_pagesize", Double.valueOf(RasterLibrary.pageSize)));
		getTextFieldPagsPerGroup().setValue(Configuration.getValue("cache_pagspergroup", Integer.valueOf(RasterLibrary.pagsPerGroup)));

		Integer blockHeight = Configuration.getValue("cache_blockheight", Integer.valueOf(RasterLibrary.blockHeight));
		for (int i = 0; i < getComboBoxBlockHeight().getItemCount(); i++)
			if (getComboBoxBlockHeight().getItemAt(i).toString().equals(blockHeight.toString())) {
				getComboBoxBlockHeight().setSelectedIndex(i);
				break;
			}
	}

	/**
	 * Guarda los valores de la cache establecidos por el usuario
	 */
	public void storeValues() {
		Configuration.setValue("cache_size", getTextFieldCacheSize().getText());
		Configuration.setValue("cache_pagesize", getTextFieldPageSize()
				.getText());
		Configuration.setValue("cache_pagspergroup", getTextFieldPagsPerGroup().getText());
		Configuration.setValue("cache_blockheight", getComboBoxBlockHeight().getSelectedItem().toString());
	}
}