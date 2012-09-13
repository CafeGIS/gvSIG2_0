package org.gvsig.app.join;

import org.gvsig.app.join.dal.feature.JoinTransform;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureStoreTransforms;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;

/**
 * @author Fernando González Cortés
 */
public class RemoveTableUnion extends Extension{

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		FeatureTableDocumentPanel t = (FeatureTableDocumentPanel) PluginServices.getMDIManager().getActiveWindow();
		FeatureTableDocument pt = t.getModel();
		FeatureStore fs = pt.getStore();
		this.removeJoinTransfor(fs);

		//		TODO
		//		if (fs instanceof JoinFeatureStore) {
		//			DataManager dm = DALLocator.getDataManager();
		//			DataStoreParameters originalParams = ((JoinFeatureStoreParameters) fs
		//					.getParameters()).getStorePrimary();
		//			FeatureStore original = null;
		//			try {
		//				original = (FeatureStore) dm.createStore(originalParams);
		//			} catch (InitializeException e) {
		//				NotificationManager.addError(e.getMessage(), e);
		//				return;
		//			}
		//
		//			pt.setStore(original);
		//			try {
		//				fs.dispose();
		//			} catch (CloseException e) {
		//				NotificationManager.addError(e);
		//			}
		//			t.setModel(pt);
		//
		//		}

		//		t.clearSelectedFields();
		t.getModel().setModified(true);
	}

	public void removeJoinTransfor(FeatureStore store) {
		FeatureStoreTransforms transforms = store.getTransforms();
		int size = transforms.size();
		if (size < 1) {
			return;
		}
		FeatureStoreTransform join = transforms.getTransform(size - 1);
		if (join instanceof JoinTransform) {
			transforms.remove(join);
		} else {
			return;
		}



	}

	public boolean hasJoinTransform(FeatureStore store) {

		FeatureStoreTransforms transforms = store.getTransforms();
		int size = transforms.size();
		if (size < 1) {
			return false;
		}
		return (transforms.getTransform(size - 1) instanceof JoinTransform);

	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();

		if (v == null) {
			return false;
		}

		if (v.getClass() == FeatureTableDocumentPanel.class) {
			FeatureTableDocumentPanel t = (FeatureTableDocumentPanel) v;
			// FIXME !!!! Asi se hacia antes
			//			if (t.getModel().getOriginal() != null){
			//				return true;
			//			}

			FeatureTableDocument pt = t.getModel();
			FeatureStore fs = pt.getStore();

			return this.hasJoinTransform(fs);
//			TODO
//			if (fs instanceof JoinFeatureStore) {
//				return true;
//			}

		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();

		if (v == null) {
			return false;
		}

		if (v instanceof FeatureTableDocumentPanel) {
			return true;
		} else {
			return false;
		}

	}

}
