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

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.gvsig.addo.Jaddo;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.util.BasePanel;
/**
 * PreferenceOverviews es una clase para la configuracion de las overviews de
 * Raster en gvSIG
 * 
 * @version 14/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceOverviews extends BasePanel {
	private static final long  serialVersionUID  = 1L;
	//	private JCheckBox jCheckBoxAsk           = null;
	private JComboBox          comboBoxRate      = null;
	private JComboBox          comboBoxNumber    = null;
	private JComboBox          comboBoxAlgorithm = null;
	private JLabel             labelNumber       = null;
	private JLabel             labelRate         = null;
	private JLabel             labelAlgorithm    = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public PreferenceOverviews() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		setBorder(BorderFactory.createTitledBorder(getText(this, "overviews")));
		getLabelNumber().setText(getText(this, "num_overviews") + ":");
		getLabelRate().setText(getText(this, "proporcion_overviews") + ":");
		getLabelAlgorithm().setText(getText(this, "algorithm") + ":");
//		getCheckBoxAsk().setText(getText(this, "generacion_overviews"));
//		getCheckBoxAsk().setToolTipText(getCheckBoxAsk().getText());
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());

		int posy = 0;
		
//		gridBagConstraints = new GridBagConstraints();
//		gridBagConstraints.gridx = 0;
//		gridBagConstraints.gridy = posy;
//		gridBagConstraints.gridwidth = 2;
//		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//		gridBagConstraints.weightx = 1.0;
//		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
//		add(getCheckBoxAsk(), gridBagConstraints);

//		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getLabelNumber(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getComboNumber(), gridBagConstraints);

		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(getLabelRate(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getComboRate(), gridBagConstraints);

		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 5, 2);
		add(getLabelAlgorithm(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 2, 5, 5);
		add(getComboAlgorithm(), gridBagConstraints);
	}

	private JLabel getLabelAlgorithm() {
		if (labelAlgorithm == null)
			labelAlgorithm = new JLabel();
		return labelAlgorithm;
	}

	private JLabel getLabelRate() {
		if (labelRate == null)
			labelRate = new JLabel();
		return labelRate;
	}

	private JLabel getLabelNumber() {
		if (labelNumber == null)
			labelNumber = new JLabel();
		return labelNumber;
	}

//	private JCheckBox getCheckBoxAsk() {
//		if (jCheckBoxAsk == null) {
//			jCheckBoxAsk = new JCheckBox();
//			jCheckBoxAsk.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//			jCheckBoxAsk.setMargin(new Insets(0, 0, 0, 0));
//		}
//		return jCheckBoxAsk;
//	}

	private JComboBox getComboRate() {
		if (comboBoxRate == null) {
			comboBoxRate = new JComboBox();
			comboBoxRate.setModel(new DefaultComboBoxModel(new String[] { "/2", "/3" }));
		}
		return comboBoxRate;
	}

	private JComboBox getComboNumber() {
		if (comboBoxNumber == null) {
			comboBoxNumber = new JComboBox();
			comboBoxNumber.setModel(new DefaultComboBoxModel(new String[] { "2", "3", "4", "5", "6" }));
		}
		return comboBoxNumber;
	}

	private JComboBox getComboAlgorithm() {
		if (comboBoxAlgorithm == null) {
			comboBoxAlgorithm = new JComboBox();
			comboBoxAlgorithm.setModel(new DefaultComboBoxModel(new String[] { getText(this, "vecino_cercano"), getText(this, "media"), getText(this, "media_phase") }));
		}
		return comboBoxAlgorithm;
	}

	/**
	 * Carga los valores por defecto del componente
	 */
	public void initializeDefaults() {
		int pos = 0;
//		getCheckBoxAsk().setSelected(((Boolean) Configuration.getDefaultValue("overviews_ask_before_loading")).booleanValue());

		pos = ((Integer) Configuration.getDefaultValue("overviews_number")).intValue() - 2;
		if ((pos < 0) || (pos >= getComboNumber().getItemCount()))
			pos = 2;
		getComboNumber().setSelectedIndex(pos);

		pos = ((Integer) Configuration.getDefaultValue("overviews_rate")).intValue() - 2;
		if ((pos < 0) || (pos >= getComboRate().getItemCount()))
			pos = 0;
		getComboRate().setSelectedIndex(pos);

		pos = ((Integer) Configuration.getDefaultValue("overviews_resampling_algorithm")).intValue();
		int type = 1;
		switch (pos) {
			case Jaddo.NEAREST:
				type = 0;
				break;
			case Jaddo.AVERAGE_MAGPHASE:
				type = 2;
				break;
		}
		getComboAlgorithm().setSelectedIndex(type);
	}

	/**
	 * Carga los valores definidos previamente por el usuario 
	 */
	public void initializeValues() {
		int pos = 0;
//		getCheckBoxAsk().setSelected(Configuration.getValue("overviews_ask_before_loading", Boolean.FALSE).booleanValue());

		pos = Configuration.getValue("overviews_number", Integer.valueOf(4)).intValue() - 2;
		if ((pos < 0) || (pos >= getComboNumber().getItemCount()))
			pos = 2;
		getComboNumber().setSelectedIndex(pos);

		pos = Configuration.getValue("overviews_rate", Integer.valueOf(2)).intValue() - 2;
		if ((pos < 0) || (pos >= getComboRate().getItemCount()))
			pos = 0;
		getComboRate().setSelectedIndex(pos);

		pos = Configuration.getValue("overviews_resampling_algorithm", Integer.valueOf(Jaddo.AVERAGE)).intValue();
		int type = 1;
		switch (pos) {
			case Jaddo.NEAREST:
				type = 0;
				break;
			case Jaddo.AVERAGE_MAGPHASE:
				type = 2;
				break;
		}
		getComboAlgorithm().setSelectedIndex(type);
	}

	/**
	 * Guarda los valores definidos por el usuario
	 */
	public void storeValues() {
//		Configuration.setValue("overviews_ask_before_loading", Boolean.valueOf(getCheckBoxAsk().isSelected()));
		if (getComboRate().getSelectedIndex() > -1)
			Configuration.setValue("overviews_rate", Integer.valueOf(getComboRate().getSelectedIndex() + 2));
		if (getComboNumber().getSelectedIndex() > -1)
			Configuration.setValue("overviews_number", Integer.valueOf(getComboNumber().getSelectedIndex() + 2));

		int type = Jaddo.AVERAGE;
		switch (getComboAlgorithm().getSelectedIndex()) {
			case 0:
				type = Jaddo.NEAREST;
				break;
			case 2:
				type = Jaddo.AVERAGE_MAGPHASE;
				break;
		}
		Configuration.setValue("overviews_resampling_algorithm", Integer.valueOf(type));
	}

}