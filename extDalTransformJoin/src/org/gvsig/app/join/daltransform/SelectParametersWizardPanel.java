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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.app.join.daltransform;

import org.gvsig.app.daltransform.gui.DataTransformWizardPanel;
import org.gvsig.app.daltransform.gui.components.impl.FeatureTypeAttributesCombo;
import org.gvsig.app.daltransform.gui.components.impl.FeatureTypeAttributesList;
import org.gvsig.app.daltransform.gui.impl.AbstractDataTransformWizardPanel;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;

import com.iver.andami.PluginServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class SelectParametersWizardPanel extends AbstractDataTransformWizardPanel implements DataTransformWizardPanel{
	private javax.swing.JLabel attributesLabel;
	private FeatureTypeAttributesList attributesList;
	private javax.swing.JScrollPane attributesScroll;
	private FeatureTypeAttributesCombo key1Combo;
	private javax.swing.JLabel key1Label;
	private FeatureTypeAttributesCombo key2Combo;
	private javax.swing.JLabel key2Label;
	private javax.swing.JLabel prefix1Label;
	private javax.swing.JTextField prefix1Text;
	private javax.swing.JLabel prefix2Label;
	private javax.swing.JTextField prefix2Text; 

	/**
	 * @param featureTransformWizardModel
	 */
	public SelectParametersWizardPanel() {
		super();	
		initComponents();
		initLabels();
	}
	
	private void initLabels(){
		key1Label.setText(PluginServices.getText(this,"join_first_key"));
		key2Label.setText(PluginServices.getText(this,"join_second_key"));
		prefix1Label.setText(PluginServices.getText(this,"join_first_prefix"));
		prefix2Label.setText(PluginServices.getText(this,"join_second_prefix"));
		attributesLabel.setText(PluginServices.getText(this,"join_select_attributes"));
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		prefix2Text = new javax.swing.JTextField();
		key2Combo = new FeatureTypeAttributesCombo();
		prefix1Text = new javax.swing.JTextField();
		key1Label = new javax.swing.JLabel();
		key1Combo = new FeatureTypeAttributesCombo();
		key2Label = new javax.swing.JLabel();
		prefix1Label = new javax.swing.JLabel();
		prefix2Label = new javax.swing.JLabel();
		attributesLabel = new javax.swing.JLabel();
		attributesScroll = new javax.swing.JScrollPane();
		attributesList = new FeatureTypeAttributesList();

		setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
		add(prefix2Text, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
		add(key2Combo, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
		add(prefix1Text, gridBagConstraints);

		key1Label.setText("jLabel1");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(key1Label, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
		add(key1Combo, gridBagConstraints);

		key2Label.setText("jLabel2");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 2);
		add(key2Label, gridBagConstraints);

		prefix1Label.setText("jLabel1");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 2);
		add(prefix1Label, gridBagConstraints);

		prefix2Label.setText("jLabel2");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 2);
		add(prefix2Label, gridBagConstraints);

		attributesLabel.setText("attributesLabel");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 2);
		add(attributesLabel, gridBagConstraints);

		attributesScroll.setViewportView(attributesList);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(attributesScroll, gridBagConstraints);
	}

	/**
	 * @return
	 */
	public String getKeyAttr1() {
		return key1Combo.getSelectedFeatureAttributeDescriptor().getName();
	}

	/**
	 * @return
	 */
	public String getkeyAtrr2() {
		return key2Combo.getSelectedFeatureAttributeDescriptor().getName();
	}

	/**
	 * @return
	 */
	public String getPrefix1() {
		return prefix1Text.getText();
	}

	/**
	 * @return
	 */
	public String getPrefix2() {
		return prefix2Text.getText();
	}

	/**
	 * @return
	 */
	public String[] getAttributes() {
		return attributesList.getAttributesName();
	}
	
	/**
	 * @param selectedFeatureStore
	 * @throws DataException 
	 */
	public void updateFeatureStores(FeatureStore selectedFeatureStore) throws DataException {
		key1Combo.addFeatureAttributes(getFeatureStore().getDefaultFeatureType());
		key2Combo.addFeatureAttributes(selectedFeatureStore.getDefaultFeatureType());
		attributesList.addFeatureAttributes(selectedFeatureStore.getDefaultFeatureType());
	}
}

