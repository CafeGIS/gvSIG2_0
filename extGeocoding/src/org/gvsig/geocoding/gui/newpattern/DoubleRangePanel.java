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
 * Double Range panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class DoubleRangePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox jComboLeftFromNum;
	private JComboBox jComboLeftToNum;
	private JComboBox jComboRightFromNum;
	private JComboBox jComboRightToNum;
	private JLabel jLabLeftFromNum;
	private JLabel jLabLeftToNum;
	private JLabel jLabRightFromNum;
	private JLabel jLabRightToNum;
	private JPanel jPanRight;
	private JPanel jPanLeft;

	/**
	 * Constructor
	 */
	public DoubleRangePanel(GeocodingController control,
			List<String> fields) {
		initComponents();
		setMessages();
		// fill Combos
		if (fields != null) {
			fillCombos(fields);
		}
	}

	/**
	 * Initialize panel components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jPanLeft = new JPanel();
		jLabLeftFromNum = new JLabel();
		jComboLeftFromNum = new JComboBox();
		jLabLeftToNum = new JLabel();
		jComboLeftToNum = new JComboBox();
		jPanRight = new JPanel();
		jLabRightFromNum = new JLabel();
		jComboRightFromNum = new JComboBox();
		jLabRightToNum = new JLabel();
		jComboRightToNum = new JComboBox();

		setMinimumSize(new java.awt.Dimension(500, 70));
		setPreferredSize(new java.awt.Dimension(500, 70));
		setLayout(new java.awt.GridBagLayout());

		jPanLeft
				.setBorder(javax.swing.BorderFactory.createTitledBorder("Left"));
		jPanLeft.setLayout(new java.awt.GridBagLayout());

		jLabLeftFromNum
				.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabLeftFromNum.setText("LeftFromNum");
		jLabLeftFromNum.setMaximumSize(new java.awt.Dimension(200, 14));
		jLabLeftFromNum.setMinimumSize(new java.awt.Dimension(70, 14));
		jLabLeftFromNum.setPreferredSize(new java.awt.Dimension(70, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
		jPanLeft.add(jLabLeftFromNum, gridBagConstraints);

		jComboLeftFromNum.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboLeftFromNum.setMinimumSize(new java.awt.Dimension(150, 20));
		jComboLeftFromNum.setPreferredSize(new java.awt.Dimension(150, 20));
		jComboLeftFromNum
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						evLeftFromNum(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
		jPanLeft.add(jComboLeftFromNum, gridBagConstraints);

		jLabLeftToNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabLeftToNum.setText("LeftToNum");
		jLabLeftToNum
				.setMaximumSize(new java.awt.Dimension(1000000000, 1400000));
		jLabLeftToNum.setMinimumSize(new java.awt.Dimension(70, 14));
		jLabLeftToNum.setPreferredSize(new java.awt.Dimension(70, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jPanLeft.add(jLabLeftToNum, gridBagConstraints);

		jComboLeftToNum.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboLeftToNum.setMinimumSize(new java.awt.Dimension(150, 20));
		jComboLeftToNum.setPreferredSize(new java.awt.Dimension(150, 20));
		jComboLeftToNum.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evLeftToNum(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanLeft.add(jComboLeftToNum, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(jPanLeft, gridBagConstraints);

		jPanRight.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Right"));
		jPanRight.setLayout(new java.awt.GridBagLayout());

		jLabRightFromNum
				.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabRightFromNum.setText("RightFromNum");
		jLabRightFromNum
				.setMaximumSize(new java.awt.Dimension(3400000, 140000));
		jLabRightFromNum.setMinimumSize(new java.awt.Dimension(70, 14));
		jLabRightFromNum.setPreferredSize(new java.awt.Dimension(70, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 5);
		jPanRight.add(jLabRightFromNum, gridBagConstraints);

		jComboRightFromNum.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboRightFromNum.setMinimumSize(new java.awt.Dimension(150, 20));
		jComboRightFromNum.setPreferredSize(new java.awt.Dimension(150, 20));
		jComboRightFromNum
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						evRightFromNum(evt);
					}
				});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
		jPanRight.add(jComboRightFromNum, gridBagConstraints);

		jLabRightToNum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabRightToNum.setText("RightToNum");
		jLabRightToNum
				.setMaximumSize(new java.awt.Dimension(3400000, 140000000));
		jLabRightToNum.setMinimumSize(new java.awt.Dimension(70, 14));
		jLabRightToNum.setPreferredSize(new java.awt.Dimension(70, 14));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jPanRight.add(jLabRightToNum, gridBagConstraints);

		jComboRightToNum.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "" }));
		jComboRightToNum.setMinimumSize(new java.awt.Dimension(150, 20));
		jComboRightToNum.setPreferredSize(new java.awt.Dimension(150, 20));
		jComboRightToNum.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evRightToNum(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanRight.add(jComboRightToNum, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(jPanRight, gridBagConstraints);
	}

	/**
	 * Change value of LeftFromNum
	 * 
	 * @param evt
	 */
	private void evLeftFromNum(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Change value of LeftToNum
	 * 
	 * @param evt
	 */
	private void evLeftToNum(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Change value of RightFromNum
	 * 
	 * @param evt
	 */
	private void evRightFromNum(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	/**
	 * Change value of RightToNum
	 * 
	 * @param evt
	 */
	private void evRightToNum(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_evRightToNum
		// TODO add your handling code here:
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
			jComboRightFromNum.setModel(model);

			DefaultComboBoxModel model2 = new DefaultComboBoxModel();
			for (String field2 : fields) {
				model2.addElement(field2);
			}
			jComboRightToNum.setModel(model2);

			DefaultComboBoxModel model3 = new DefaultComboBoxModel();
			for (String field3 : fields) {
				model3.addElement(field3);
			}
			jComboLeftFromNum.setModel(model3);

			DefaultComboBoxModel model4 = new DefaultComboBoxModel();
			for (String field4 : fields) {
				model4.addElement(field4);
			}
			jComboLeftToNum.setModel(model4);
		}
	}

	/**
	 * Get description RightFromMun Combo
	 * 
	 * @return
	 */
	public String getDescriptorRightFronMun() {

		String cDesc = (String) jComboRightFromNum.getSelectedItem();
		if (cDesc != null) {
			return cDesc;
		}
		return null;
	}

	/**
	 * Get description RightToMun Combo
	 * 
	 * @return
	 */
	public String getDescriptorRightToMun() {

		String cDesc = (String) jComboRightToNum.getSelectedItem();
		if (cDesc != null) {
			return cDesc;
		}
		return null;
	}

	/**
	 * Get description LeftFromMun Combo
	 * 
	 * @return
	 */
	public String getDescriptorLeftFronMun() {

		String cDesc = (String) jComboLeftFromNum.getSelectedItem();
		if (cDesc != null) {
			return cDesc;
		}
		return null;
	}

	/**
	 * Get description LeftToMun Combo
	 * 
	 * @return
	 */
	public String getDescriptorLeftToMun() {

		String cDesc = (String) jComboLeftToNum.getSelectedItem();
		if (cDesc != null) {
			return cDesc;
		}
		return null;
	}

	/**
	 * set description RightFromMun Combo
	 * 
	 * @return
	 */
	public void setDescriptorRightFronMun(String desc) {		
		jComboRightFromNum.getModel().setSelectedItem(desc);
	}

	/**
	 * set description RightToMun Combo
	 * 
	 * @return
	 */
	public void setDescriptorRightToMun(String desc) {
		jComboRightToNum.getModel().setSelectedItem(desc);
	}

	/**
	 * set description LeftFromMun Combo
	 * 
	 * @return
	 */
	public void setDescriptorLeftFronMun(String desc) {
		jComboLeftFromNum.getModel().setSelectedItem(desc);
	}

	/**
	 * set description LeftToMun Combo
	 * 
	 * @return
	 */
	public void setDescriptorLeftToMun(String desc) {
		jComboLeftToNum.getModel().setSelectedItem(desc);
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

		jPanRight.setBorder(GeocodingUtils.getTitledBorder(ps.getText("right")));
		jPanLeft.setBorder(GeocodingUtils.getTitledBorder(ps.getText("left")));

		jLabRightFromNum.setText(ps.getText("rightfromnum"));
		jLabRightToNum.setText(ps.getText("righttonum"));
		jLabLeftFromNum.setText(ps.getText("leftfromnum"));
		jLabLeftToNum.setText(ps.getText("lefttomnum"));

	}
}
