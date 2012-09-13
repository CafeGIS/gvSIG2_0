/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 Prodevelop S.L. main developer
 */

package org.gvsig.geocoding.gui.newpattern;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.utils.GeocodingUtils;

import com.iver.andami.PluginServices;

/**
 * Simple Range panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class SimpleRangePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox jComboFromNum;
	private JComboBox jComboToNum;
	private JLabel jLabFromNum;
	private JLabel jLabToNum;

	/**
	 * Constructor
	 */
	public SimpleRangePanel(GeocodingController control,
			List<String> fields) {
		initComponents();
		setMessages();
		// Fill two combos with the fields of datasource
		if (fields != null) {
			fillCombos(fields);
		}

	}

	/**
	 * Initialize panel components
	 */
	private void initComponents() {

		this.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Numeric range"));
		java.awt.GridBagConstraints gridBagConstraints;

		jLabFromNum = new javax.swing.JLabel();
		jComboFromNum = new javax.swing.JComboBox();
		jLabToNum = new javax.swing.JLabel();
		jComboToNum = new javax.swing.JComboBox();

		setMinimumSize(new java.awt.Dimension(500, 50));
		setPreferredSize(new java.awt.Dimension(500, 50));
		setLayout(new java.awt.GridBagLayout());

		jLabFromNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabFromNum.setText("FromNumber");
		jLabFromNum.setFocusable(false);
		jLabFromNum.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		jLabFromNum.setMinimumSize(new java.awt.Dimension(70, 14));
		jLabFromNum.setPreferredSize(new java.awt.Dimension(70, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
		add(jLabFromNum, gridBagConstraints);

		jComboFromNum.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboFromNum.setMinimumSize(new java.awt.Dimension(150, 20));
		jComboFromNum.setOpaque(false);
		jComboFromNum.setPreferredSize(new java.awt.Dimension(150, 20));
		jComboFromNum.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evFromNum(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 15);
		add(jComboFromNum, gridBagConstraints);

		jLabToNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabToNum.setText("ToNumber");
		jLabToNum.setFocusable(false);
		jLabToNum.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		jLabToNum.setMinimumSize(new java.awt.Dimension(70, 14));
		jLabToNum.setPreferredSize(new java.awt.Dimension(70, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jLabToNum, gridBagConstraints);

		jComboToNum.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboToNum.setMinimumSize(new java.awt.Dimension(150, 20));
		jComboToNum.setPreferredSize(new java.awt.Dimension(150, 20));
		jComboToNum.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evToNum(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		add(jComboToNum, gridBagConstraints);
	}

	/**
	 * Change value of FromNum
	 * 
	 * @param evt
	 */
	private void evFromNum(java.awt.event.ActionEvent evt) {
		// TODO nothing to do
	}

	/**
	 * Change Value of ToNum
	 * 
	 * @param evt
	 */
	private void evToNum(java.awt.event.ActionEvent evt) {
		// nothing to do
	}

	/**
	 * Fill panel combos
	 * 
	 * @param fields
	 */
	private void fillCombos(List<String> fields) {
		if (fields != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			for (String field : fields) {
				model.addElement(field);
			}
			jComboFromNum.setModel(model);
			DefaultComboBoxModel model2 = new DefaultComboBoxModel();
			for (String field2 : fields) {
				model2.addElement(field2);
			}
			jComboToNum.setModel(model2);
		}
	}

	/**
	 * Get description FromMun Combo
	 * 
	 * @return
	 */
	public String getDescriptorFronMun() {

		String cDesc = (String) jComboFromNum.getSelectedItem();
		if (cDesc != null) {
			return cDesc;
		}
		return null;
	}

	/**
	 * Get description ToMun Combo
	 * 
	 * @return
	 */
	public String getDescriptorToMun() {

		String cDesc = (String) jComboToNum.getSelectedItem();
		if (cDesc != null) {
			return cDesc;
		}
		return null;
	}

	/**
	 * set description FromMun Combo
	 * 
	 * @param desc
	 */
	public void setDescriptorFronMun(String desc) {
		jComboFromNum.getModel().setSelectedItem(desc);
	}

	/**
	 * Get description ToMun Combo
	 * 
	 * @return
	 */
	public void setDescriptorToMun(String desc) {
		jComboToNum.getModel().setSelectedItem(desc);
	}

	/**
	 * 
	 * @param desc
	 */
	public void setComboValues(List<String> desc) {
		fillCombos(desc);
		this.validate();
	}

	/**
	 * This method updates all labels of the panel from properties file
	 */
	private void setMessages() {

		PluginServices ps = PluginServices.getPluginServices(this);

		this.setBorder(GeocodingUtils.getTitledBorder(ps.getText("rangenum")));

		jLabFromNum.setText(ps.getText("fromnum"));
		jLabToNum.setText(ps.getText("tonum"));

	}

}
