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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gvsig.app.daltransform.gui.DataTransformGui;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.ProjectViewBase;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.IView;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class LoadLayerWizardPanel extends AbstractDataTransformWizardPanel implements ItemListener{
	private static final Logger logger = LoggerFactory.getLogger(LoadLayerWizardPanel.class);
	private JCheckBox loadLayerCb = null;
	private JScrollPane messageScroll = null;
	private JTextArea messageTextArea = null;
	private JPanel northPanel = null;
	private JLabel selectViewLabel;
	private JList selectViewList;
	private JScrollPane selectViewScroll;
    private JPanel centerPanel;
	private FeatureStoreTransform transform = null;
	private boolean hasViews = false;

	/**
	 * @param wizardComponents
	 */
	public LoadLayerWizardPanel() {
		super();	
		initComponents();
		initLabels();	
		addViews();
		loadLayerCb.addItemListener(this);
		itemStateChanged(null);		
	}

	private void initLabels(){
		loadLayerCb.setText(PluginServices.getText(this, "transform_load_layer_query"));
		selectViewLabel.setText(PluginServices.getText(this, "transform_view_to_load"));
	}

	private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        northPanel = new javax.swing.JPanel();
        messageScroll = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        loadLayerCb = new javax.swing.JCheckBox();
        selectViewLabel = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        selectViewScroll = new javax.swing.JScrollPane();
        selectViewList = new javax.swing.JList();
        selectViewList.setModel(new DefaultListModel());

        setLayout(new java.awt.BorderLayout());

        northPanel.setLayout(new java.awt.GridBagLayout());

        messageScroll.setBorder(null);

        messageTextArea.setColumns(20);
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageTextArea.setBorder(null);
        messageScroll.setViewportView(messageTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 2);
        northPanel.add(messageScroll, gridBagConstraints);

        loadLayerCb.setText("jCheckBox1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 2);
        northPanel.add(loadLayerCb, gridBagConstraints);

        selectViewLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        northPanel.add(selectViewLabel, gridBagConstraints);

        add(northPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setLayout(new java.awt.GridBagLayout());

        selectViewScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        selectViewScroll.setPreferredSize(null);

        selectViewScroll.setViewportView(selectViewList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 2);
        centerPanel.add(selectViewScroll, gridBagConstraints);

        add(centerPanel, java.awt.BorderLayout.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.app.daltransform.impl.AbstractDataTransformWizardPanel#updatePanel()
	 */
	public void updatePanel() {
		//Gets the selected transformation
		DataTransformGui featureTransformGui = getDataTransformWizard().getDataTransformGui();
		
		//Gets the selected FeatureStore
		FeatureStore featureStore = getFeatureStore();
		
		//Apply the transformation
		try {
			transform = featureTransformGui.createFeatureStoreTransform(featureStore);

			if (isLayerLoaded()){
				updateGuiForLayer(transform);
			}else{
				updateGuiForTable(transform);
			}
		} catch (DataException e) {
			logger.error("Error creating the transformation", e);
		}		
	}

	/**
	 * Update the form when the transform has been applied in
	 * to a table
	 * @param transform
	 * @throws DataException
	 */
	private void updateGuiForTable(FeatureStoreTransform transform) throws DataException{
		FeatureType featureType = transform.getDefaultFeatureType();
		FeatureAttributeDescriptor[] featureAttributeDescriptors = featureType.getAttributeDescriptors();
		boolean hasGeometry = false;
		for (int i=0 ; i<featureAttributeDescriptors.length ; i++){
			if (featureAttributeDescriptors[i].getDataType() == DataTypes.GEOMETRY){
				hasGeometry = true;
			}
		}
		if (hasGeometry){
			if (selectViewList.getModel().getSize() == 0){
				messageTextArea.setText(PluginServices.getText(this, "transform_layout_not_view_to_load"));
				setLoadLayerVisible(false);
			}else{
				messageTextArea.setText(PluginServices.getText(this, "transform_layout_geometry"));
				setLoadLayerVisible(true);
			}
		}else{
			messageTextArea.setText(PluginServices.getText(this, "transform_layout_no_geometry"));
			setLoadLayerVisible(false);
		}		
	}

	/**
	 * Set if it is possible or not lo load a layer
	 * from the transformation
	 * @param isVisible
	 */
	private void setLoadLayerVisible(boolean isVisible){
		loadLayerCb.setVisible(isVisible);
		selectViewLabel.setVisible(isVisible);
		selectViewScroll.setVisible(isVisible);
		selectViewList.setVisible(isVisible);
	}

	/**
	 * Add the project views
	 */
	private void addViews(){
		selectViewList.removeAll();
		ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
		ArrayList<ProjectDocument> projects = ext.getProject().getDocumentsByType(ProjectViewFactory.registerName);
		for (int i=0 ; i<projects.size() ; i++){
			ProjectViewBase view = (ProjectViewBase)projects.get(i);
			((DefaultListModel)selectViewList.getModel()).addElement(view);
		}
		if (selectViewList.getModel().getSize() > 0){
			hasViews = true;
		}
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof IView){
			selectViewList.setSelectedValue(window, true);
		}else{
			selectViewList.setSelectedIndex(0);
		}
	}

	/**
	 * Update the form when the transform has been applied in
	 * to a layer
	 * @param transform
	 * @throws DataException
	 */
	private void updateGuiForLayer(FeatureStoreTransform transform){
		messageTextArea.setText(PluginServices.getText(this, "transform_layer"));
		setLoadLayerVisible(false);
	}

	/**
	 * @return if the layer has to be loaded 
	 */
	public boolean isLayerLoaded(){
		if (!loadLayerCb.isVisible()){
			return false;
		}
		return loadLayerCb.isSelected();
	}	

	/**
	 * @return the transform
	 */
	public FeatureStoreTransform getTransform() {
		return transform;
	}
	
	/**
	 * @return The mapcontext
	 */
	public MapContext getMapContext(){
		Object obj = selectViewList.getSelectedValue();
		if (obj != null){
			return ((ProjectViewBase)obj).getMapContext();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent arg0) {
		boolean isSelected = loadLayerCb.isSelected();
		selectViewLabel.setEnabled(isSelected);
		selectViewList.setEnabled(isSelected);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.FeatureTransformWizard#getPanelTitle()
	 */
	public String getPanelTitle() {
		return PluginServices.getText(this, "transform_apply");
	}
}

