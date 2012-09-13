package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.command.CommandStackDialog;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableCommandStackExtension extends Extension {
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		PluginServices.getIconTheme().registerDefault(
				"commands-stack",
				this.getClass().getClassLoader().getResource("images/commandstack.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		.getActiveWindow();

		FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) f;
		FeatureTableDocument model = table.getModel();
		if (s.equals("COMMANDSTACK")) {
			try {
				FeatureStore featureStore=((FLyrVect)model.getAssociatedLayer()).getFeatureStore();
				featureStore.addObserver(table);

//				CommandsRecord cr=null;

//				if (model.getAssociatedTable()!=null){
//				try {
//				cr=((FLyrVect)model.getAssociatedTable()).getFeatureStore().getCommandsRecord();
//				} catch (ReadException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				} catch (DataException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				}
//				cr.addObserver(table);
//				}else{
//				try {
//				cr=model.getModel().getCommandsRecord();
//				} catch (DataException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				}
//				cr.addObserver(table);
//				}
				CommandStackDialog csd = new CommandStackDialog();

				csd.setModel(featureStore);

				PluginServices.getMDIManager().addWindow(csd);
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		.getActiveWindow();
		if (f instanceof FeatureTableDocumentPanel){
		FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) f;
		FeatureTableDocument model = table.getModel();
		if (model.getStore().isEditing())
			return true;
		}
			return false;

	}
}
