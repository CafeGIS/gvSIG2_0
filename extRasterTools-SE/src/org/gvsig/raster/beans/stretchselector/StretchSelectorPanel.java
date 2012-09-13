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
package org.gvsig.raster.beans.stretchselector;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.datainput.DataInputField;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.GenericBasePanel;

import com.iver.andami.PluginServices;

/**
 * Clase que contiene el panel con los elementos para añadir una serie
 * de intervalos. La selección de intervalos puede ser seleccionando el número de
 * estos para una distancia definida entre un máximo y un mínimo. En este caso se
 * calculará automáticamente la distancia entre los tramos. También puede ser calculada
 * a partir de una distancia dada. En este caso calculará automáticamenet el número de 
 * tramos para la longitud total.
 * 
 * El controlador está metido dentro del mismo panel por lo que tendrá también una referencia
 * al modelo de datos.
 * 
 * 06/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchSelectorPanel extends BasePanel implements ActionListener, DataInputContainerListener, Observer {
	private static final long    serialVersionUID     = 1638303379491319120L;

	private boolean              selectedCount        = false;
	private ButtonGroup          buttonGroup1         = null;

	private JLabel               jLabelMinim          = null;
	private JLabel               jLabelMaxim          = null;
	private BasePanel            jPanel1              = null;
	private JRadioButton         jRadioIntervalSize   = null;
	private JRadioButton         jRadioIntervalNumber = null;

	private DataInputField       jTextMinim           = null;
	private DataInputField       jTextMaxim           = null;
	private DataInputField       jTextValue           = null;
	private StretchSelectorData  data                 = null;
		
	/**
	 * Constructor. Llama al inicializador de elementos gráficos
	 */
	public StretchSelectorPanel() {
		init();
		translate();
	}
	
	/**
	 * Constructor. Llama al inicializador de elementos gráficos
	 */
	public StretchSelectorPanel(StretchSelectorData data) {
		this.data = data;
		this.data.addObserver(this);
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		jLabelMinim.setText(getText(this, "minimo") + ":");
		jLabelMaxim.setText(getText(this, "maximo") + ":");
		getIntervalSize().setText(getText(this, "tamano_intervalo"));
		getIntervalNumber().setText(getText(this, "n_intervalos"));
	}
	
	/**
	 * Inicializa los elementos gráficos
	 */
	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());
		jPanel1 = new GenericBasePanel();
		jPanel1.setLayout(new GridBagLayout());
		jPanel1.setBorder(BorderFactory.createTitledBorder(""));

		int y = 1;

	    jLabelMinim = new JLabel();
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = y;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(jLabelMinim, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = y;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getMinimText(), gridBagConstraints);

		y++;
	    jLabelMaxim = new JLabel();
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = y;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		add(jLabelMaxim, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = y;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		add(getMaximunText(), gridBagConstraints);

		y++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 5, 0, 5);
		add(jPanel1, gridBagConstraints);

		// Panel vacio
		y++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		JPanel panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(0, 0));
		add(panel2, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		jPanel1.add(getIntervalSize(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		jPanel1.add(getIntervalNumber(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 5, 5, 5);
		jPanel1.add(getIntervalInputField(), gridBagConstraints);

		buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(getIntervalSize());
		buttonGroup1.add(getIntervalNumber());
	}
	
	/**
	 * Obtiene el modelo de datos o crea uno si no encuentra ninguno
	 * @return StretchSelectorData
	 */
	public StretchSelectorData getData() {
		if(data == null) {
			data = new StretchSelectorData();
			data.addObserver(this);
		}
		return data;
	}
	
	/**
	 * Asigna el modelo de datos y añade el panel como observador
	 * @param data
	 */
	public void setData(StretchSelectorData data) {
		this.data = data;
		data.addObserver(this);
	}
	
	/**
	 * Obtiene el componente con la entrada de texto para el valor del mínimo
	 * @return DataInputField
	 */
	private DataInputField getIntervalInputField() {
		if(jTextValue == null) {
			jTextValue = new DataInputField();
			jTextValue.setValue(Double.valueOf(255).toString());
			jTextValue.addValueChangedListener(this);
		}
		return jTextValue;			
	}
	
	/**
	 * Obtiene el componente con la entrada de texto para el valor del mínimo
	 * @return DataInputField
	 */
	private DataInputField getMinimText() {
		if(jTextMinim == null) {
			jTextMinim = new DataInputField();
			jTextMinim.setValue(Double.valueOf("0").toString());
			jTextMinim.addValueChangedListener(this);
		}
		return jTextMinim;			
	}
	
	/**
	 * Obtiene el componente con la entrada de texto para el valor del mínimo
	 * @return DataInputField
	 */
	private DataInputField getMaximunText() {
		if(jTextMaxim == null) {
			jTextMaxim = new DataInputField();
			jTextMaxim.setValue(Double.valueOf("255").toString());
			jTextMaxim.addValueChangedListener(this);
		}
		return jTextMaxim;			
	}
	
	/**
	 * Obtiene el componente botón de selección de creación de tramos por valor del intervalo
	 * @return JRadioButton
	 */
	public JRadioButton getIntervalSize() {
		if(jRadioIntervalSize == null) {
			jRadioIntervalSize = new JRadioButton(PluginServices.getText(this, "tamano_intervalo"), true);
			jRadioIntervalSize.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jRadioIntervalSize.setMargin(new Insets(0, 0, 0, 0));
			jRadioIntervalSize.addActionListener(this);
		}
		return jRadioIntervalSize;			
	}
	
	/**
	 * Obtiene el componente botón de selección de creación de tramos por número de intervalos
	 * @return JRadioButton
	 */
	public JRadioButton getIntervalNumber() {
		if(jRadioIntervalNumber == null) {
			jRadioIntervalNumber = new JRadioButton(PluginServices.getText(this, "n_intervalos"));
			jRadioIntervalNumber.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jRadioIntervalNumber.setMargin(new Insets(0, 0, 0, 0));
			jRadioIntervalNumber.addActionListener(this);
		}
		return jRadioIntervalNumber;			
	}
		
	/************************************
	 *           FUNCIONALIDAD
	 ************************************/

	/**
	 * Actualiza el modelo de datos
	 */
	private void updateData() {
		if (getIntervalNumber().isSelected()) {
			if (!selectedCount) {
				getData().setIntervalSize(getIntervalInputField().getValue());
				selectedCount = true;
			}
		} else {
			if (selectedCount) {
				getData().setNumber(getIntervalInputField().getValue());
				selectedCount = false;
			}
		}
		getData().setMin(getMinimText().getValue());
		getData().setMax(getMaximunText().getValue());	
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(!isEnableValueChangedEvent())
			return;
		updateData();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.datainput.DataInputContainerListener#actionValueChanged(java.util.EventObject)
	 */
	public void actionValueChanged(EventObject e) {
		if(!isEnableValueChangedEvent())
			return;
		getInterval();
	}

	/**
	 * Devuelve un intervalo valido
	 * @return
	 */
	public double getInterval() {
		getData().setMin(getMinimText().getValue());
		getData().setMax(getMaximunText().getValue());	
		if (getIntervalNumber().isSelected())
			getData().setNumber(getIntervalInputField().getValue());
		else
			getData().setIntervalSize(getIntervalInputField().getValue());
		double value = Double.parseDouble(getIntervalInputField().getValue());
		double value2 = value;
		if (getIntervalSize().isSelected()) {
			if (value > Math.abs(getData().getMaximum() - getData().getMinimum()))
				value = Math.abs(getData().getMaximum() - getData().getMinimum());
			if (value <= 0)
				value = 1;
		} else {
			if (value < 1)
				value = 1;
		}
		value = Math.abs(value);
		
		if (value != value2) {
			if (getIntervalNumber().isSelected())
				getData().setNumber(Double.valueOf(value).toString());
			else
				getData().setIntervalSize(Double.valueOf(value).toString());
		}
		return value;
	}
	
	/**
	 * Actualiza los valores de los paneles cuando los datos de ClippingData varian
	 * @param o 
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if(!(o instanceof StretchSelectorData))
			return;
		StretchSelectorData data = (StretchSelectorData)o;
		
		setEnableValueChangedEvent(false);
		if(getIntervalNumber().isSelected())
			getIntervalInputField().setValue(data.getNumber());
		if(getIntervalSize().isSelected())
			getIntervalInputField().setValue(data.getIntervalSize());
		getMinimText().setValue(data.getMin());
		getMaximunText().setValue(data.getMax());
		setEnableValueChangedEvent(true);
	}
}
