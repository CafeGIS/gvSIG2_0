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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.AttrInTableLabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.ILabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.JComboBoxUnits;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.styling.JComboBoxUnitsReferenceSystem;
import com.iver.cit.gvsig.gui.utils.FontChooser;
import com.iver.cit.gvsig.project.Project;
import com.iver.utiles.swing.JComboBox;

public class AttrInTableLabeling extends JPanel implements  ILabelingStrategyPanel{
	private static final long serialVersionUID = 8229927418031917075L;
	private static final String NO_FIELD_ITEM = "-- " +
			PluginServices.getText(LabelingManager.class, "none") + " --";
	private String[] fieldNames;
	private String[] numericFieldNames;
	private String[] integerFieldNames;

	private JRadioButton rdBtnFixedHeight;
	private JRadioButton rdBtnHeightField;
	private JRadioButton rdBtnFixedColor;
	private JRadioButton rdBtnColorField;
	private JComboBox cmbTextField;
	private JComboBox cmbHeightField;
	private JComboBox cmbRotationField;
	private JComboBoxUnits cmbUnits;
	private JComboBoxUnitsReferenceSystem cmbReferenceSystem;
	private JTextField txtHeightField;
	private FLyrVect layer;

	private ColorChooserPanel colorChooser;
	private JComboBox cmbColorField;
	private JButton chooseFontBut;
	private Font labelFont = SymbologyFactory.DefaultTextFont;

	public AttrInTableLabeling() {
		initialize();
	}

	private void initialize() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		GridBagLayoutPanel panel = new GridBagLayoutPanel();

		GridBagLayoutPanel aux = new GridBagLayoutPanel();
		aux.addComponent(PluginServices.getText(this, "field_to_be_labeled") + ":", getCmbTextField());
		aux.addComponent(getRdBtnHeightField(), getCmbHeightField());
		aux.addComponent(getRdBtnFixedHeight(), getTxtHeightField());
		aux.addComponent(PluginServices.getText(this, "rotation_height") + ":", getCmbRotationField());
		aux.addComponent(PluginServices.getText(this, "units") + ":", getCmbUnits());
		aux.addComponent(PluginServices.getText(this,""),getCmbReferenceSystem());
		panel.add(aux);

		aux = new GridBagLayoutPanel();
		aux.addComponent(getChooseFontBut(),new JBlank(20,20));
		GridBagLayoutPanel aux2 = new GridBagLayoutPanel();
		aux2.setBorder(BorderFactory.createTitledBorder(null,PluginServices.getText(this,"color")));
		aux2.addComponent(getRdBtnFixedColor(),colorChooser = new ColorChooserPanel(true));
		aux2.addComponent(getRdBtnColorField(),getCmbColorField());
		aux.addComponent(aux2);

		panel.add(new JBlank(20,20));
		panel.add(aux);


		add(panel);


		ButtonGroup group = new ButtonGroup();
		group.add(getRdBtnFixedHeight());
		group.add(getRdBtnHeightField());

		ButtonGroup colorGroup = new ButtonGroup();
		colorGroup.add(getRdBtnFixedColor());
		colorGroup.add(getRdBtnColorField());

//		getRdBtnHeightField().setEnabled(true);
	}

	private JButton getChooseFontBut() {
		if(chooseFontBut == null){
			chooseFontBut = new JButton(PluginServices.getText(this,"font"));
			chooseFontBut.setPreferredSize(new Dimension(80,10));
			chooseFontBut.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					Font newFont;

					newFont = FontChooser.showDialog("Choose Font", labelFont);
					if (newFont == null) {
						return;
					}

					labelFont = newFont;
				}

			});
		}
		return chooseFontBut;
	}

	private JRadioButton getRdBtnFixedHeight() {
		if (rdBtnFixedHeight == null) {
			rdBtnFixedHeight = new JRadioButton(PluginServices.getText(this, "fixed_height") + ":");
			rdBtnFixedHeight.setSelected(false);
			rdBtnFixedHeight.setName("RDFIXEDHEIGHT");
		}

		return rdBtnFixedHeight;
	}

	private JRadioButton getRdBtnHeightField() {
		if (rdBtnHeightField == null) {
			rdBtnHeightField = new JRadioButton(PluginServices.getText(this, "text_height_field") + ":");
			rdBtnHeightField.setSelected(true);
			rdBtnHeightField.setName("RDHEIGHTFIELD");
		}

		return rdBtnHeightField;
	}

	private JRadioButton getRdBtnFixedColor() {
		if (rdBtnFixedColor == null) {
			rdBtnFixedColor = new JRadioButton(PluginServices.getText(this, "fixed_color") + ":");
			rdBtnFixedColor.setSelected(false);
			rdBtnFixedColor.setName("RDFIXEDCOLOR");
		}

		return rdBtnFixedColor;
	}

	private JRadioButton getRdBtnColorField() {
		if (rdBtnColorField == null) {
			rdBtnColorField = new JRadioButton(PluginServices.getText(this, "color_field") + ":");
			rdBtnColorField.setSelected(true);
			rdBtnColorField.setName("RDCOLORFIELD");
		}

		return rdBtnColorField;
	}

	private JComboBoxUnits getCmbUnits() {
		if (cmbUnits == null) {
			cmbUnits = new JComboBoxUnits();
			cmbUnits.setSelectedIndex(Project.getDefaultDistanceUnits());
			cmbUnits.setName("CMBUNITS");
		}

		return cmbUnits;
	}

	private JComboBoxUnitsReferenceSystem getCmbReferenceSystem(){
		if(cmbReferenceSystem == null){
			cmbReferenceSystem = new JComboBoxUnitsReferenceSystem();
			cmbReferenceSystem.setName("CMBREFSYST");
		}
		return cmbReferenceSystem;
	}

	private JComboBox getCmbColorField() {
		if (cmbColorField == null) {
			cmbColorField = new JComboBox();
			cmbColorField.setName("CMBCOLOR");
		}

		return cmbColorField;
	}

	private void refreshControls() {
		// When the attributes are in the table -----
		//      field with the text
		refreshCmbTextField();

		//      field with the rotation
		refreshCmbRotationField();

		//      field with the text height or the text size
		refreshTextHeight();

		//		the text size unit name
		refreshCmbUnits();

		refreshCmbRefSystem();
		//		the font for the text
		refreshFont();
		//		the color for the font
		refreshColorFont();
	}

	private JComboBox getCmbRotationField() {
		if (cmbRotationField == null) {
			cmbRotationField = new JComboBox();
			cmbRotationField.setPreferredSize(new Dimension(200, 20));
			cmbRotationField.setName("CMBROTATIONFIELD");
		}
		return cmbRotationField;
	}

	private JComboBox getCmbHeightField() {
		if (cmbHeightField == null) {
			cmbHeightField = new JComboBox();
			cmbHeightField.setPreferredSize(new Dimension(200, 20));
			cmbHeightField.setName("CMBHEIGHTFIELD");
		}
		return cmbHeightField;
	}

	private JComboBox getCmbTextField() {
		if (cmbTextField == null) {
			cmbTextField = new JComboBox();
			cmbTextField.setPreferredSize(new Dimension(200, 20));
			cmbTextField.setName("CMBTEXTFIELD");
		}
		return cmbTextField;
	}


	private JTextField getTxtHeightField() {
		if (txtHeightField == null) {
			txtHeightField = new JTextField(10);
			txtHeightField.setText("10");
			txtHeightField.setName("TXTHEIGHTFIELD");
		}

		return txtHeightField;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		throw new Error("Not yet implemented!");

	}
	private ColorChooserPanel getColorChooser() {
		if (colorChooser == null){
			colorChooser = new ColorChooserPanel(true);
		}
		return colorChooser;
	}

	public ILabelingStrategy getLabelingStrategy() {
		// user selected to define each label attributes from values
		// contained in the table for each feature row.

		double fixedSize;
		try {
			fixedSize = Double.parseDouble(getTxtHeightField().getText());
		} catch (Exception e) {
			fixedSize = 10;
		}
		AttrInTableLabelingStrategy strategy = new AttrInTableLabelingStrategy();
		try {
			strategy.setLayer(layer);
		} catch (ReadException e) {
			// FIXME Exception
			throw new RuntimeException(e);
		}

		if(getCmbHeightField().getItemCount() > 0 && !rdBtnFixedHeight.isSelected()) {
			strategy.setHeightField(
				(String) getCmbHeightField().getSelectedItem());
		}
		if(getCmbRotationField().getItemCount() > 0) {
			if(!getCmbRotationField().getSelectedItem().equals(NO_FIELD_ITEM)) {
				strategy.setRotationField(
						(String) getCmbRotationField().getSelectedItem());
			} else {
				strategy.setRotationField(null);
			}
		}

		if(getCmbTextField().getItemCount() > 0) {
			strategy.setTextField(
				(String) getCmbTextField().getSelectedItem());
		}

		strategy.setUsesFixedSize(getRdBtnFixedHeight().isSelected());
		strategy.setFixedSize(fixedSize);

		if(getCmbUnits().getItemCount() > 0) {
			strategy.setUnit(getCmbUnits().getSelectedUnitIndex());
		}
		if(getCmbReferenceSystem().getItemCount() > 0) {
			strategy.setReferenceSystem(getCmbReferenceSystem().getSelectedIndex());
		}

		strategy.setUsesFixedColor(getRdBtnFixedColor().isSelected());
		strategy.setFixedColor(getColorChooser().getColor());

		if(getCmbColorField().getItemCount() > 0 && !rdBtnFixedColor.isSelected()) {
			strategy.setColorField((String) getCmbColorField().getSelectedItem());
		}


		strategy.setFont(labelFont);
		return strategy;
	}

	public void setModel(FLayer layer, ILabelingStrategy str) {
		this.layer = (FLyrVect) layer;
		// to allow the labeling of non-FLyrVect layers
		if (layer instanceof FLyrVect) {
			FLyrVect lv = (FLyrVect) layer;
			try {
				FeatureType featureType = lv.getFeatureStore()
						.getDefaultFeatureType();
				fieldNames = new String[featureType.size()];
				Iterator<FeatureAttributeDescriptor> iterator = featureType
						.iterator();
				ArrayList<String> l = new ArrayList<String>();
				ArrayList<String> lColors = new ArrayList<String>();
				String name;
				FeatureAttributeDescriptor descriptor;
				while (iterator.hasNext()) {
					descriptor = iterator.next();

					name = descriptor.getName();
					fieldNames[descriptor.getIndex()] = name;
					switch (descriptor.getDataType()) {
//					case DataTypes.DECIMAL:
//					case DataTypes.NUMERIC:
					case DataTypes.FLOAT:
//					case DataTypes.REAL:
					case DataTypes.DOUBLE:
						l.add(name);
						break;
					case DataTypes.INT:
//					case DataTypes.SMALLINT:
//					case DataTypes.TINYINT:
					case DataTypes.LONG:
						lColors.add(name);
						l.add(name);
						break;
					}
				}
				numericFieldNames = l.toArray(new String[l.size()]);
				integerFieldNames = lColors.toArray(new String[lColors.size()]);
			} catch (DataException e) {
				NotificationManager.addError(PluginServices.getText(this, "accessing_file_structure"), e);
			}
			refreshControls();
		}
	}

	private void refreshColorFont(){

		getCmbColorField().removeAllItems();

		boolean enabled = integerFieldNames.length>0;
		getCmbColorField().setEnabled(enabled);
		getRdBtnColorField().setEnabled(enabled);

		if (!enabled) {
			getRdBtnFixedColor().setSelected(true);
		}

		for (int i = 0; i < integerFieldNames.length; i++) {
			getCmbColorField().addItem(integerFieldNames[i]);
		}

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			try {

				getRdBtnFixedColor().setSelected(aux.usesFixedColor());
				getRdBtnColorField().setSelected(!aux.usesFixedColor());

				String item = aux.getColorField();
				getCmbColorField().setSelectedItem(item);
				getColorChooser().setColor(aux.getFixedColor());

			} catch (DataException e) {
				// should never happen
				NotificationManager.addWarning(PluginServices.getText(this, "could_not_restore_color_field"), e);
			}
		}
	}

	private void refreshFont(){

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			labelFont = aux.getFont();
		}
	}

	private void refreshCmbUnits() {

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			getCmbUnits().setSelectedUnitIndex(aux.getUnit());
		}
	}

	private void refreshCmbRefSystem() {

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			getCmbReferenceSystem().setSelectedIndex(aux.getReferenceSystem());
		}
	}

	private void refreshTextHeight() {
		getCmbHeightField().removeAllItems();

		boolean enabled = numericFieldNames.length>0;
//		getCmbHeightField().setEnabled(enabled);
//		getRdBtnHeightField().setEnabled(enabled);

		if (!enabled) {
			getRdBtnFixedHeight().setSelected(true);
		}

		for (int i = 0; i < numericFieldNames.length; i++) {
			getCmbHeightField().addItem(numericFieldNames[i]);
		}

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			try {
				getTxtHeightField().setText(String.valueOf(aux.getFixedSize()));
				getRdBtnFixedHeight().setSelected(aux.usesFixedSize());
				getRdBtnHeightField().setSelected(!aux.usesFixedSize());

				String item = aux.getHeightField();
				getCmbHeightField().setSelectedItem(item);

			} catch (DataException e) {
				// should never happen
				NotificationManager.addWarning(PluginServices.getText(this, "could_not_restore_text_height_field"), e);
			}
		}

	}

	private void refreshCmbRotationField() {
		getCmbRotationField().removeAllItems();
		getCmbRotationField().addItem(NO_FIELD_ITEM);
		for (int i = 0; i < numericFieldNames.length; i++) {
			getCmbRotationField().addItem(numericFieldNames[i]);
		}

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			try {
				String item = aux.getRotationField();
				getCmbRotationField().setSelectedItem(item != null? item : NO_FIELD_ITEM);
			} catch (DataException e) {
				// should never happen
				NotificationManager.addWarning(PluginServices.getText(this, "could_not_restore_rotation_field"), e);
			}
		}
	}

	private void refreshCmbTextField() {
		getCmbTextField().removeAllItems();
		for (int i = 0; i < fieldNames.length; i++) {
			getCmbTextField().addItem(fieldNames[i]);
		}

		if (layer.getLabelingStrategy() instanceof AttrInTableLabelingStrategy) {
			AttrInTableLabelingStrategy aux = (AttrInTableLabelingStrategy) layer.getLabelingStrategy();
			try {
				String item = aux.getTextField();
				getCmbTextField().setSelectedItem(item != null? item : NO_FIELD_ITEM);
			} catch (DataException e) {
				// should never happen
				NotificationManager.addWarning(PluginServices.getText(this, "could_not_restore_text_field"), e);
			}
		}
	}

	public String getLabelingStrategyName() {
		return PluginServices.getText(this, "label_attributes_defined_in_table");
	}

	public Class getLabelingStrategyClass() {
		return AttrInTableLabelingStrategy.class;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getChooseFontBut().setEnabled(enabled);
		getCmbColorField().setEnabled(enabled);
		getCmbHeightField().setEnabled(enabled);
		getCmbReferenceSystem().setEnabled(enabled);
		getCmbRotationField().setEnabled(enabled);
		getCmbTextField().setEnabled(enabled);
		getCmbUnits().setEnabled(enabled);
		getColorChooser().setEnabled(enabled);
		getRdBtnColorField().setEnabled(enabled);
		getRdBtnFixedColor().setEnabled(enabled);
		getRdBtnFixedHeight().setEnabled(enabled);
		getRdBtnHeightField().setEnabled(enabled);
		getTxtHeightField().setEnabled(enabled);

	}
}
