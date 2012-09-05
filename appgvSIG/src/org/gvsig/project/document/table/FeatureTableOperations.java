package org.gvsig.project.document.table;

import java.awt.Component;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.data.feature.swing.FeatureTable;
import org.gvsig.project.document.table.gui.CreateNewAttributePanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
/**
 * Feature Table Operations.
 *
 * @author Vicente Caballero Navarro
 *
 */
public class FeatureTableOperations {
	public static final int MAX_FIELD_LENGTH = 254;
	private static FeatureTableOperations fto=null;
	private FeatureStore featureStore;
	private ArrayList<Feature> selectedFeatures=new ArrayList<Feature>();
	private boolean cutting=false;

	public static FeatureTableOperations getInstance(){
		if (fto==null){
			fto=new FeatureTableOperations();
		}
		return fto;
	}

	public void setStore(FeatureStore store) {
		featureStore=store;
	}

	public void copyFeatures() throws DataException {
		cutting=false;
		copy();
	}

	public boolean hasSelection() {
		return !selectedFeatures.isEmpty();
	}

	public void pasteFeatures() throws DataException {
		if (cutting){
			delete();
			cutting=false;
		}
		Iterator<Feature> features=selectedFeatures.iterator();
		while (features.hasNext()) {
			Feature feature = features.next();
			featureStore.insert(feature.getEditable());
		}
	}

	public void cutFeatures() throws DataException {
		cutting=true;
		copy();
	}
	private void copy() throws DataException{
		DisposableIterator features = null;
		try {
			features = ((FeatureSelection) featureStore.getSelection())
					.iterator();
			selectedFeatures.clear();
			while (features.hasNext()) {
				Feature feature = (Feature) features.next();
				selectedFeatures.add(feature);
			}
		} finally {
			if (features != null) {
				features.dispose();
			}
		}
	}
	private void delete() throws DataException{
		Iterator<Feature> features=selectedFeatures.iterator();
		while (features.hasNext()) {
			Feature feature = features.next();
			featureStore.delete(feature);
		}
	}
	public void deleteFeatures() throws DataException{
		DisposableIterator features = null;
		try {
			features = ((FeatureSelection) featureStore.getSelection())
					.iterator();
			while (features.hasNext()) {
				Feature feature = (Feature) features.next();
				featureStore.delete(feature);
			}
		} finally {
			if (features != null) {
				features.dispose();
			}
		}
	}
	public void insertNewFeature() throws DataException {
//		if (getModel().getAssociatedTable()!=null){
//			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),"No se puede añadir una fila a una tabla asociada a una capa.");
//			return;
//		}
		EditableFeature feature = featureStore.createNewFeature();
		featureStore.insert(feature);
	}
	public void deleteAttributes(FeatureTable table) throws DataException {
		EditableFeatureType eft = featureStore.getDefaultFeatureType().getEditable();
		FeatureAttributeDescriptor[] selecteds =table.getSelectedColumnsAttributeDescriptor();
		for (int i = 0; i < selecteds.length; i++) {
			eft.remove(selecteds[i].getName());
		}
		featureStore.update(eft);
	}

	public void insertAttributes(FeatureTable table) throws DataException {
		EditableFeatureType eft = featureStore.getDefaultFeatureType().getEditable();

		try {
			CreateNewAttributePanel panelNewField = new CreateNewAttributePanel();

			EditableFeatureAttributeDescriptor ead = panelNewField
			.loadFieldDescription(eft);
			if (ead == null) {
				return;
			}
			if (ead.getDataType() == DataTypes.STRING
					&& ead.getSize() > MAX_FIELD_LENGTH) {
				NotificationManager.showMessageInfo( PluginServices.getText(this,
				"max_length_is")
				+ ":" + MAX_FIELD_LENGTH, null);
				ead.setSize(MAX_FIELD_LENGTH);
			}
			PluginServices.getMDIManager().closeWindow(
					panelNewField);
		} catch (ParseException e2) {
			NotificationManager.addError(e2);
		}
		featureStore.update(eft);

	}

	public void renameAttributes(FeatureTable table) throws DataException {
		EditableFeatureType eft = featureStore.getDefaultFeatureType().getEditable();
		FeatureAttributeDescriptor[] selecteds =table.getSelectedColumnsAttributeDescriptor();

		for (int i = selecteds.length - 1; i >= 0; i--) {
			String newName = JOptionPane
			.showInputDialog(
					(Component) PluginServices
					.getMDIManager()
					.getActiveWindow(),
					PluginServices
					.getText(this,
							"please_insert_new_field_name"),
							selecteds[i].getName());
			if (newName == null) {
				return;
			}
			if (eft.getIndex(newName) != -1) {
				NotificationManager.showMessageInfo(
						PluginServices.getText(this,
						"field_already_exists"),
						null);
				return;
			}
			FeatureAttributeDescriptor ad = (FeatureAttributeDescriptor) eft
			.get(selecteds[i].getName());
			eft.remove(ad.getName());
			EditableFeatureAttributeDescriptor ead = eft
			.add(newName, ad.getDataType(), ad
					.getSize());
			ead.setPrecision(ad.getPrecision());
		}

		featureStore.update(eft);
	}


}
