package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.command.CommandStackDialog;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class ViewCommandStackExtension extends Extension {

	public static CommandStackDialog csd=null;
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

		View vista = (View) f;
		IProjectView model = vista.getModel();
		MapContext mapa = model.getMapContext();
		FLayers layers = mapa.getLayers();
		if (s.equals("COMMANDSTACK")) {
			for (int i =0;i<layers.getLayersCount();i++){
				if (layers.getLayer(i) instanceof FLyrVect){
					FLyrVect lyrVect=(FLyrVect)layers.getLayer(i);
					if (lyrVect.isEditing() && lyrVect.isActive()){
						try{
						FeatureStore featureStore=lyrVect.getFeatureStore();
//					CommandsRecord commandsRecord=null;
//						try {
//							commandsRecord = lyrVect.getFeatureStore().getCommandsRecord();
//						} catch (ReadException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (DataException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						commandsRecord.addObserver(this);
						csd=new CommandStackDialog();
						csd.setModel(featureStore);
						PluginServices.getMDIManager().addWindow(csd);
						} catch (DataException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			}
		}

		//PluginServices.getMainFrame().enableControls();

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
		if (EditionUtilities.getEditionStatus() == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE)
			return true;
		return false;

	}

//	public void commandRepaint() {
//		try {
//			CADExtension.getCADTool().clearSelection();
//		} catch (ReadException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		}
//
//	}
//
//	public void commandRefresh() {
//		try {
//			CADExtension.getCADTool().clearSelection();
//		} catch (ReadException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		}
//	}

}
