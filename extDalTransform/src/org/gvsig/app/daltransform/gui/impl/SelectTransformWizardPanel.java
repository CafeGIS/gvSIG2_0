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

package org.gvsig.app.daltransform.gui.impl;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.app.daltransform.DataTransformLocator;
import org.gvsig.app.daltransform.DataTransformManager;
import org.gvsig.app.daltransform.gui.DataTransformGui;

import com.iver.andami.PluginServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class SelectTransformWizardPanel extends AbstractDataTransformWizardPanel implements ListSelectionListener{
	private JList transformList;
	private JScrollPane transformScrollPane;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionText;

	/**
	 * @param wizardComponents
	 */
	public SelectTransformWizardPanel() {
		super();	
		initComponents();		
		addTransforms();
		transformList.addListSelectionListener(this);		
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		transformScrollPane = new javax.swing.JScrollPane();
		transformList = new javax.swing.JList();
		descriptionScrollPane = new javax.swing.JScrollPane();
		descriptionText = new javax.swing.JTextArea();

		setLayout(new java.awt.GridBagLayout());

		transformScrollPane.setViewportView(transformList);

		transformList.setModel(new DefaultListModel());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 2);
		add(transformScrollPane, gridBagConstraints);

		descriptionText.setColumns(20);
		descriptionText.setEditable(false);
		descriptionText.setRows(5);
		descriptionText.setLineWrap(true);
		descriptionText.setBorder(null);
		descriptionScrollPane.setBorder(null);
		descriptionScrollPane.setViewportView(descriptionText);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 2);
		add(descriptionScrollPane, gridBagConstraints);
	}

	/**
	 * Adding the objects
	 */
	protected void addTransforms(){
		DataTransformManager featureTransformManager = 
			DataTransformLocator.getDataTransformManager();			
		ArrayList<DataTransformGui> featureTransformGui =
			featureTransformManager.getDataTransforms();
		for (int i=0 ; i<featureTransformGui.size() ; i++){
			((DefaultListModel)transformList.getModel()).addElement(new FeatureTransformGuiWrapper(featureTransformGui.get(i)));
		}	
		updatePanel();
	}

	/*
	 * 	(non-Javadoc)
	 * @see org.gvsig.app.daltransform.impl.AbstractDataTransformWizardPanel#updatePanel()
	 */
	@Override
	public void updatePanel() {
		if (transformList.getSelectedIndex() == -1){
			if (transformList.getModel().getSize() > 0){
				transformList.setSelectedIndex(0);
				valueChanged(null);
			}
		}else{
			if (transformList.getModel().getSize() == 0){
				getDataTransformWizard().setApplicable(false);
			}
		}
	}	

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		Object obj = transformList.getSelectedValue();
		if (obj != null){
			descriptionText.setText(((FeatureTransformGuiWrapper)obj).getFeatureTransformGui().getDescription());
		}
	}

	public DataTransformGui getFeatureTransformGui(){
		Object obj = transformList.getSelectedValue();
		if (obj != null){
			return ((FeatureTransformGuiWrapper)obj).getFeatureTransformGui();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.FeatureTransformWizard#getPanelTitle()
	 */
	public String getPanelTitle() {
		return PluginServices.getText(this, "transform_selection");
	}
	
	private class FeatureTransformGuiWrapper{
		private DataTransformGui featureTransformGui = null;

		/**
		 * @param featureTransformGui
		 */
		public FeatureTransformGuiWrapper(
				DataTransformGui featureTransformGui) {
			super();
			this.featureTransformGui = featureTransformGui;
		}

		/**
		 * @return the featureTransformGui
		 */
		public DataTransformGui getFeatureTransformGui() {
			return featureTransformGui;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {			
			return featureTransformGui.getName();
		}		
	}
}

