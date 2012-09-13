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

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.gui.IView;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class SelectDataStoreWizardPanel extends AbstractDataTransformWizardPanel{
	private static final long serialVersionUID = -1841990357325903449L;
	private JList dataStoreList;
	private JScrollPane dataStoreScrollPane;

	/**
	 * @param wizardComponents
	 */
	public SelectDataStoreWizardPanel() {
		super();
		initComponents();
		addDataStores();		
	}	

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		dataStoreScrollPane = new javax.swing.JScrollPane();
		dataStoreList = new javax.swing.JList();

		setLayout(new java.awt.GridBagLayout());

		dataStoreScrollPane.setViewportView(dataStoreList);

		dataStoreList.setModel(new DefaultListModel());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		add(dataStoreScrollPane, gridBagConstraints);
	}

	public void removeFeatureStore(FeatureStore featureStore){
		DefaultListModel model =( DefaultListModel)dataStoreList.getModel();
		for (int i=model.getSize()-1 ; i>=0 ; i--){
			if (((FeatureStoreCombo)model.get(i)).getFeatureStore().equals(featureStore)){
				model.remove(i);
				break;
			}
		}		
	}

	/**
	 * Adding the objects
	 */
	private void addDataStores(){
		//Add all the tables
		ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
		ArrayList<ProjectDocument> tables = ext.getProject().getDocumentsByType(FeatureTableDocumentFactory.registerName);
		for (int i=0 ; i<tables.size() ; i++){
			FeatureTableDocument table = (FeatureTableDocument)tables.get(i);
			((DefaultListModel)dataStoreList.getModel()).addElement(					
					new FeatureStoreCombo(table.getName(),
							table.getStore(),
							false));
		}
		//Add the layers from the current view
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof IView){
			IView view = (IView)window;
			LayersIterator it = new LayersIterator(
					view.getMapControl().getMapContext().getLayers());
			while(it.hasNext()){
				FLayer layer = it.nextLayer();
				if (layer instanceof FLyrVect){
					try {
						FLyrVect layerVect = (FLyrVect)layer;
						FeatureStore featureStore = layerVect.getFeatureStore();
						boolean found = false;
						for (int i=0 ; i<tables.size() ; i++){
							FeatureTableDocument table = (FeatureTableDocument)tables.get(i);
							if (table.getStore().equals(featureStore)) {
								found = true;
							}							
						}
						if (!found){
							((DefaultListModel)dataStoreList.getModel()).addElement(
									new FeatureStoreCombo(layerVect.getName(),
											featureStore,
											true));
						}
					} catch (ReadException e) {
						logger.error("It is not possible to read the FeatureStore", e);
					}
				}
			}
		}	
	}


	/**
	 * @return the selected feature store
	 */
	public FeatureStore getSelectedFeatureStore(){
		Object obj = dataStoreList.getSelectedValue();
		if (obj != null){
			return ((FeatureStoreCombo)obj).getFeatureStore();
		}
		return null;
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.impl.AbstractDataTransformWizardPanel#getFeatureStore()
	 */
	@Override
	public FeatureStore getFeatureStore() {
		return getSelectedFeatureStore();
	}

	/**
	 * @return the selected feature store
	 */
	public boolean isSelectedFeatureStoreLoaded(){
		Object obj = dataStoreList.getSelectedValue();
		if (obj != null){
			return ((FeatureStoreCombo)obj).isLoaded();
		}
		return false;
	}

	/**
	 * Used to fill the combo
	 * @author jpiera
	 */
	private class FeatureStoreCombo{
		private FeatureStore featureStore = null;
		private String name = null;
		private boolean isLoaded = false;

		public FeatureStoreCombo(String name, FeatureStore featureStore, boolean isLoaded) {
			super();
			this.name = name;
			this.featureStore = featureStore;
			this.isLoaded = isLoaded;
		}

		/**
		 * @return the isLoaded
		 */
		public boolean isLoaded() {
			return isLoaded;
		}

		/**
		 * @return the featureStore
		 */
		public FeatureStore getFeatureStore() {
			return featureStore;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */		
		public String toString() {			
			return name;
		}		
	}

	/*
	 * 	(non-Javadoc)
	 * @see org.gvsig.app.daltransform.gui.FeatureTransformWizard#getPanelTitle()
	 */
	public String getPanelTitle() {
		return PluginServices.getText(this, "transform_datastore_selection");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.DataTransformWizard#updatePanel()
	 */
	public void updatePanel() {
		if (dataStoreList.getSelectedIndex() == -1){
			if (dataStoreList.getModel().getSize() > 0){
				dataStoreList.setSelectedIndex(0);
				getDataTransformWizard().setApplicable(true);
			}else{
				getDataTransformWizard().setApplicable(false);
			}
		}		
	}

	/**
	 * @return
	 */
	public boolean isFeatureStoreLayer() {
		Object obj = dataStoreList.getSelectedValue();
		if (obj != null){
			return ((FeatureStoreCombo)obj).isLoaded;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.app.daltransform.impl.AbstractDataTransformWizardPanel#nextPanel()
	 */
	@Override
	public void nextPanel() {
		getDataTransformWizard().updateGui();
	}
	
	
}

