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
package org.gvsig.rastertools.properties.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.ConfigurationEvent;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Panel para la gestion del valor NoData en el panel de propiedades
 *
 * @version 18/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class NoDataPanel extends BasePanel implements ItemListener, ActionListener {
	private static final long   serialVersionUID = 7645641060812458944L;

	private JLabel              jLabelValue       = null;
	private JComboBox           jComboBoxSetup    = null;
	private JButton             buttonSaveDefault = null;
	private JFormattedTextField jTextFieldValue   = null;
	private INoDataPanel        noDataPanel       = null;
	private IRasterProperties   layer             = null;

	public NoDataPanel(INoDataPanel noDataPanel) {
		this.noDataPanel = noDataPanel;
		setEnabledComponents(false);
		init();
		translate();
	}

	private JComboBox getComboBoxSetup() {
		if (jComboBoxSetup == null) {
			jComboBoxSetup = new JComboBox();
			jComboBoxSetup.addItemListener(this);
		}
		return jComboBoxSetup;
	}

	protected void translate() {
		getButtonSaveDefault().setText(getText(this, "guardar_predeterminado"));
		getComboBoxSetup().setModel(new DefaultComboBoxModel(new String[] { getText(this, "desactivado"), getText(this, "capa"), getText(this, "personalizado") }));
		getLabelValue().setText(getText(this, "value") + ":");
	}

	private JButton getButtonSaveDefault() {
		if (buttonSaveDefault == null) {
			buttonSaveDefault = new JButton();
			buttonSaveDefault.addActionListener(this);
		}
		return buttonSaveDefault;
	}

	private JFormattedTextField getTextFieldValue() {
		if (jTextFieldValue == null) {
			NumberFormat integerFormat = NumberFormat.getNumberInstance();
			integerFormat.setParseIntegerOnly(false);
			integerFormat.setMinimumFractionDigits(0);
			integerFormat.setMaximumFractionDigits(3);
			jTextFieldValue = new JFormattedTextField(new DefaultFormatterFactory(
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat),
					new NumberFormatter(integerFormat)));
		}
		return jTextFieldValue;
	}

	private JLabel getLabelValue() {
		if (jLabelValue == null) {
			jLabelValue = new JLabel();
		}
		return jLabelValue;
	}

	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());

		int col = 0;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = col;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 2);
		add(getComboBoxSetup(), gridBagConstraints);

		col++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = col;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 2, 5, 2);
		add(getLabelValue(), gridBagConstraints);

		col++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = col;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 2, 5, 2);
		add(getTextFieldValue(), gridBagConstraints);

		col++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = col;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(5, 2, 5, 5);
		add(getButtonSaveDefault(), gridBagConstraints);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (e.getSource() == getComboBoxSetup()) {
				noDataPanel.sourceStateChanged(this, getComboBoxSetup().getSelectedIndex());
				return;
			}
		}
	}

	public void setComboValueSetup(int pos) {
		getComboBoxSetup().setSelectedIndex(pos);
		setEnabledComponents(pos == 2);
	}

	public void setEnabledComponents(boolean b) {
		getTextFieldValue().setEnabled(b);
		getLabelValue().setEnabled(b);
	}

	public void setNoDataValue(double value) {
		getTextFieldValue().setValue(Double.valueOf(value));
	}

	public double getNoDataValue() {
		return Double.valueOf(getTextFieldValue().getValue().toString()).doubleValue();
	}

	public int getComboSetupIndex() {
		return getComboBoxSetup().getSelectedIndex();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.util.ConfigurationListener#actionConfigurationChanged(org.gvsig.fmap.raster.util.ConfigurationEvent)
	 */
	public void actionConfigurationChanged(ConfigurationEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getButtonSaveDefault()) {
			saveToRmf();
			return;
		}
	}

	/**
	 * Aqui se definen los parametros iniciales para la transparencia del panel
	 * @param t
	 */
	public void setLayer(IRasterProperties layer) {
		this.layer = layer;

		setComboValueSetup(((FLyrRasterSE) layer).getNoDataType());
		setNoDataValue(((FLyrRasterSE) layer).getNoDataValue());
	}

	/**
	 * Salva el valor NoData al fichero rmf.
	 * @param fName
	 * @throws IOException
	 */
	private void saveToRmf() {
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, getText(this, "nodata_modificado_continuar"))) {
			IRasterDataSource datasource = ((FLyrRasterSE) layer).getDataSource();
			try {
				datasource.saveObjectToRmf(0, NoData.class, new NoData(getNoDataValue(), getComboSetupIndex(), layer.getDataType()[0]));
			} catch (RmfSerializerException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this,"error_salvando_rmf"), this, e);
			}
		}
	}
}