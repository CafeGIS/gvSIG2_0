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
package org.gvsig.rastertools.clipping.panels;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.buffer.BufferInterpolation;

import com.iver.andami.PluginServices;
/**
 * <code>ResolutionPanel</code> sirve para controlar las redimensiones por
 * interpolación
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingResolutionPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2570616093263451091L;
	private JPanel             pButtons       = null;
	private DataInputContainer cCellSize      = null;
	private DataInputContainer cHeight        = null;
	private DataInputContainer cWidth         = null;
	private JRadioButton       rSize          = null;
	private JRadioButton       rWidthH        = null;
	private JComboBox          cInterpolation = null;
	private JButton            buttonRestore  = null;

	/**
	 * This is the default constructor
	 */
	public ClippingResolutionPanel() {
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints;

		setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipadx = 200;
		gridBagConstraints.ipady = 25;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		add(getPButtons(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 5);
		add(getJComboBox(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(getCCellSize(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(getCWidth(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(getCHeight(), gridBagConstraints);

		JLabel jLabel = new JLabel();
		jLabel.setText(PluginServices.getText(this, "interpolacion") + ":");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 2);
		add(jLabel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
		add(getButtonRestore(), gridBagConstraints);

		getRSize().addActionListener(this);
		getRWidthH().addActionListener(this);
		getRSize().setSelected(true);
		getCWidth().setControlEnabled(false);
		getCHeight().setControlEnabled(false);
	}

	/**
	 * This method initializes jPanel1
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getCCellSize() {
		if (cCellSize == null) {
			cCellSize = new DataInputContainer();
			cCellSize.setLabelText(PluginServices.getText(this, "celda"));
		}
		return cCellSize;
	}

	/**
	 * This method initializes jPanel2
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getCHeight() {
		if (cHeight == null) {
			cHeight = new DataInputContainer();
			cHeight.setLabelText(PluginServices.getText(this, "alto"));
		}
		return cHeight;
	}
	
	public JButton getButtonRestore() {
		if (buttonRestore == null) {
			buttonRestore = new JButton(PluginServices.getText(this, "restablecer"));
		}
		return buttonRestore;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getCWidth() {
		if (cWidth == null) {
			cWidth = new DataInputContainer();
			cWidth.setLabelText(PluginServices.getText(this, "ancho"));
		}
		return cWidth;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getPButtons() {
		if (pButtons == null) {
			pButtons = new JPanel();
			pButtons.setLayout(new java.awt.GridBagLayout());
			pButtons.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));

			GridBagConstraints gridBagConstraints;
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(0, 10, 0, 0);
			pButtons.add(getRSize(), gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			pButtons.add(getRWidthH(), gridBagConstraints);
		}
		return pButtons;
	}

	/**
	 * This method initializes jRadioButton
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRSize() {
		if (rSize == null) {
			rSize = new JRadioButton(PluginServices.getText(this, "tamanyo_celda"));
		}
		return rSize;
	}

	/**
	 * This method initializes jRadioButton1
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRWidthH() {
		if (rWidthH == null) {
			rWidthH = new JRadioButton(PluginServices.getText(this, "ancho_x_alto"));
		}
		return rWidthH;
	}

	/**
	 * This method initializes jComboBox
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (cInterpolation == null) {
			cInterpolation = new JComboBox();
			cInterpolation.addItem(PluginServices.getText(this, "vecino_+_proximo"));
			cInterpolation.addItem(PluginServices.getText(this, "bilinear"));
			cInterpolation.addItem(PluginServices.getText(this, "distancia_inversa"));
			cInterpolation.addItem(PluginServices.getText(this, "b_splines"));
			// cInterpolation.addItem(PluginServices.getText(this, "bicubico"));
		}
		return cInterpolation;
	}

	/**
	 * Obtiene el valor correspondiente al método de interpolación seleccionado en
	 * el JComboBox
	 * @return Método de interpolación interpretable con las variables definidas
	 *         en BufferInterpolation.
	 */
	public int getSelectedInterpolationMethod() {
		switch (getJComboBox().getSelectedIndex()) {
			case 0:
				return BufferInterpolation.INTERPOLATION_NearestNeighbour;
			case 1:
				return BufferInterpolation.INTERPOLATION_Bilinear;
			case 2:
				return BufferInterpolation.INTERPOLATION_InverseDistance;
			case 3:
				return BufferInterpolation.INTERPOLATION_BSpline;
		}
		return BufferInterpolation.INTERPOLATION_Undefined;
	}

	/**
	 * Asigna el valor del campo "tamaño de celda" a partir del double que lo
	 * representa y con el número de decimales que se especifica en el parámetro
	 * dec
	 * @param cellSize Tamaño de celda en double
	 * @param dec Número de decimales
	 */
	public void setCellSizeText(double cellSize, int dec) {
		int indexPoint = String.valueOf(cellSize).indexOf('.');
		try {
			cCellSize.setValue(String.valueOf(cellSize).substring(0, indexPoint + dec));
		} catch (StringIndexOutOfBoundsException ex) {
			cCellSize.setValue(String.valueOf(cellSize));
		} catch (NumberFormatException ex) {
			cCellSize.setValue(String.valueOf(cellSize));
		}
	}

	/**
	 * Asigna el valor del campo "Ancho" a partir del double que lo representa y
	 * con el número de decimales que se especifica en el parámetro dec
	 * @param width Ancho
	 * @param dec Número de decimales
	 */
	public void setWidthText(double width, int dec) {
		int indexPoint = String.valueOf(width).indexOf('.');
		try {
			cWidth.setValue(String.valueOf(width).substring(0, indexPoint + dec));
		} catch (StringIndexOutOfBoundsException ex) {
			cWidth.setValue(String.valueOf(width));
		} catch (NumberFormatException ex) {
			cWidth.setValue(String.valueOf(width));
		}
	}

	/**
	 * Asigna el valor del campo "Alto" a partir del double que lo representa y
	 * con el número de decimales que se especifica en el parámetro dec
	 * @param height Alto
	 * @param dec Número de decimales
	 */
	public void setHeightText(double height, int dec) {
		int indexPoint = String.valueOf(height).indexOf('.');
		try {
			cHeight.setValue(String.valueOf(height).substring(0, indexPoint + dec));
		} catch (StringIndexOutOfBoundsException ex) {
			cHeight.setValue(String.valueOf(height));
		} catch (NumberFormatException ex) {
			cHeight.setValue(String.valueOf(height));
		}
	}

	/**
	 * Devuelve el valor del campo "tamaño de celda"
	 * @return
	 */
	public double getCellSizeText() {
		return Double.parseDouble(cCellSize.getValue());
	}

	/**
	 * Devuelve el valor del campo "tamaño de celda"
	 * @return
	 */
	public double getWidthText() {
		return Double.parseDouble(cWidth.getValue());
	}

	/**
	 * Devuelve el valor del campo "tamaño de celda"
	 * @return
	 */
	public double getHeightText() {
		return Double.parseDouble(cHeight.getValue());
	}

	/**
	 * Devuelve los valores de Alto x Acho de la resolución espacial
	 * @return
	 */
	public String[] getWidthHeight() {
		String[] text = { cWidth.getValue(), cHeight.getValue() };
		return text;
	}

	/**
	 * Devuelve el metodo de interpolacion.
	 * @return
	 */
	public String getInterpolation() {
		return (String) getJComboBox().getSelectedItem();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getRSize()) {
			getRSize().setSelected(true);
			getRWidthH().setSelected(false);
			cCellSize.setControlEnabled(true);
			cHeight.setControlEnabled(false);
			cWidth.setControlEnabled(false);
		}
		if (e.getSource() == getRWidthH()) {
			getRWidthH().setSelected(true);
			getRSize().setSelected(false);
			cCellSize.setControlEnabled(false);
			cHeight.setControlEnabled(true);
			cWidth.setControlEnabled(true);
		}
	}
}
