package org.gvsig.gvsig3dgui.layer;

import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3d.map3d.MapControl3D;
import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.gvsig3dgui.view.View3D;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.IProjectView;

public class ViewSelectionControls3D extends Extension {

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		View3D vista = (View3D) PluginServices.getMDIManager().getActiveWindow();
		IProjectView model = vista.getModel();
		MapContext3D mapa = (MapContext3D) model.getMapContext();
		MapControl3D mapCtrl = (MapControl3D) vista.getMapControl();
		NavigationMode navigator = vista.getNavMode();
		if (actionCommand.equals("SELRECT")) {
			mapCtrl.setTool("rectSelection");
//			((ProjectDocument)vista.getModel()).setModified(true);
		} else if (actionCommand.equals("SELPOINT")) {
			mapCtrl.setTool("pointSelection");
//			((ProjectDocument)vista.getModel()).setModified(true);
		} else if (actionCommand.equals("SELPOL")) {
			mapCtrl.setTool("polSelection");
//			((ProjectDocument)vista.getModel()).setModified(true);
		} else if (actionCommand.equals("SELECTIONBYSHAPE")) {
//			SelectionByTheme dlg = new SelectionByTheme();
//			// FLayer[] layers = mapa.getLayers().getActives();
//			// int count = 0;
//			dlg.setModel(new DefaultSelectionByThemeModel());
//			dlg.addSelectionListener(new MySelectionByThemeListener());
//			PluginServices.getMDIManager().addWindow(dlg);
//			((ProjectDocument)vista.getModel()).setModified(true);
		} else if (actionCommand.equals("INVERT_SELECTION")) {
			for (int i = 0; i < mapa.getLayers().getActives().length; i++) {
				FLayer lyr = mapa.getLayers().getActives()[i];
				if (lyr.isAvailable() && lyr instanceof FLyrVect) {
					FLyrVect lyrVect = (FLyrVect) lyr;
					SelectableDataSource sds;
					try {
						sds = lyrVect.getRecordset();
						FBitSet selectedRows = sds.getSelection();
						selectedRows.flip(0, (int)sds.getRowCount());
						sds.setSelection(selectedRows);
					} catch (ReadDriverException e) {
						e.printStackTrace();
						NotificationManager.addError(e);
					}

				}
			}
			((ProjectDocument)vista.getModel()).setModified(true);
			// Disabling All Navigation modes
		}
		navigator.removeAllModes();
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f instanceof View3D) {

			View3D vista = (View3D) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			for (int i = 0; i < mapa.getLayers().getActives().length; i++) {
				FLayer lyr = mapa.getLayers().getActives()[i];
				if (lyr.isAvailable() && lyr instanceof FLyrVect) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f != null && f instanceof View3D) {
			View3D vista = (View3D) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			return hasVectorLayers(mapa.getLayers());

		}
		return false;
	}
	
	private boolean hasVectorLayers(FLayers layers) {
		for (int i = 0; i < layers.getLayersCount(); i++) {
			FLayer lyr =layers.getLayer(i);
			if (lyr instanceof FLayers){
				if (hasVectorLayers((FLayers) lyr)){
					return true;
				}
			} else if (lyr instanceof FLyrVect) {
				return true;
			}
		}
		return false;
	}

}
