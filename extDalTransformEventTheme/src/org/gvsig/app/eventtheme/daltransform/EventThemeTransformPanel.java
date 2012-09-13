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
 
package org.gvsig.app.eventtheme.daltransform;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.app.daltransform.gui.components.impl.FeatureTypeCombo;
import org.gvsig.app.daltransform.gui.components.impl.NumericFeatureTypeAttributesCombo;
import org.gvsig.app.daltransform.gui.impl.AbstractDataTransformWizardPanel;

import com.iver.andami.PluginServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class EventThemeTransformPanel extends AbstractDataTransformWizardPanel implements ActionListener {
	private JLabel nameLabel;
	private JTextField nameText;
	private NumericFeatureTypeAttributesCombo xCombo;
	private JLabel xLabel;
	private NumericFeatureTypeAttributesCombo yCombo;
	private JLabel yLabel;
	private FeatureTypeCombo featureTypeCombo;
	private JLabel featureTypeLabel;
	private JPanel northPanel = null;

	public EventThemeTransformPanel() {
		super();
		initComponents();
		initLabels();
		featureTypeCombo.addActionListener(this);
	}

	private void initLabels() {
		featureTypeLabel.setText(PluginServices.getText(this, "events_feature_type_field"));
		xLabel.setText(PluginServices.getText(this, "events_x_field"));
		yLabel.setText(PluginServices.getText(this, "events_y_field"));
		nameLabel.setText(PluginServices.getText(this, "events_geom_field"));
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;
		
		featureTypeCombo = new FeatureTypeCombo();
		featureTypeLabel = new javax.swing.JLabel();
		xLabel = new javax.swing.JLabel();
		xCombo = new NumericFeatureTypeAttributesCombo();
		yLabel = new javax.swing.JLabel();
		yCombo = new NumericFeatureTypeAttributesCombo();
		nameLabel = new javax.swing.JLabel();
		nameText = new javax.swing.JTextField();
		northPanel= new JPanel();

		setLayout(new BorderLayout());
		northPanel.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,2,2,2);
		northPanel.add(featureTypeLabel, gridBagConstraints);

		featureTypeCombo.setModel(new javax.swing.DefaultComboBoxModel());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2,2,5,2);
		northPanel.add(featureTypeCombo, gridBagConstraints);
				
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,2,2,2);
		northPanel.add(xLabel, gridBagConstraints);

		xCombo.setModel(new javax.swing.DefaultComboBoxModel());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2,2,5,2);
		gridBagConstraints.weightx = 1.0;
		northPanel.add(xCombo, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,2,2,2);
		northPanel.add(yLabel, gridBagConstraints);

		yCombo.setModel(new javax.swing.DefaultComboBoxModel());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2,2,5,2);
		northPanel.add(yCombo, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5,2,2,2);
		northPanel.add(nameLabel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2,2,5,2);
		northPanel.add(nameText, gridBagConstraints);
		
		add(northPanel, BorderLayout.NORTH);
	}	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		Object obj = featureTypeCombo.getSelectedItem();
		if (obj != null){
			xCombo.addFeatureAttributes(featureTypeCombo.getSelectedFeatureType());
			yCombo.addFeatureAttributes(featureTypeCombo.getSelectedFeatureType());			
		}
	}
	
	public String getGeometryName(){
		return nameText.getText();
	}
	
	public String getXName(){
		return xCombo.getSelectedFeatureAttributeDescriptor().getName();
	}
	
	public String getYName(){
		return yCombo.getSelectedFeatureAttributeDescriptor().getName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.app.daltransform.impl.AbstractDataTransformWizardPanel#updatePanel()
	 */
	public void updatePanel() {
		featureTypeCombo.addFeatureStore(getFeatureStore());
		actionPerformed(null);			
	}
}