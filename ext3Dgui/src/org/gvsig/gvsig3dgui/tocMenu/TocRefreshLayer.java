package org.gvsig.gvsig3dgui.tocMenu;

import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3dgui.ProjectView3D;
import org.gvsig.gvsig3dgui.view.View3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;

public class TocRefreshLayer extends AbstractTocContextMenuAction {

	public String getGroup() {
		return "group6"; // FIXME
	}

	public int getGroupOrder() {
		return 60;
	}

	public int getOrder() {
		return 10;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		// return selectedItems.length == 1;

		if (item instanceof TocItemBranch){
			return true;
		}
		
		// return true;

		if (selectedItems.length == 1 && selectedItems[0] instanceof FLyrVect
				&& selectedItems[0].isAvailable() && !(item instanceof TocItemBranch)) {
			return true;
		}
		// TODO Un toggle this comment to use the WFS extension
		if (selectedItems.length == 1 && selectedItems[0] instanceof FLyrWMS
				&& selectedItems[0].isAvailable() && !(item instanceof TocItemBranch)) {
			return true;
		}/**/
		return false;

	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}
		
		if (!isTocItemBranch(item))
			return false;

		// Only isVisible = true, where the view3D have layers
		if (f instanceof View3D) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			return mapa.getLayers().getLayersCount() > 0;
		}
		return false;
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {

		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;
		// Casting to View3D
		View3D vista3D = (View3D) view;
		IProjectView model = vista3D.getModel();
		
		ProjectView3D pv = (ProjectView3D) model;
		
		for (int i = 0; i < selectedItems.length; i++) {
//			System.out.println("Recargando capa :" + i);
			((MapContext3D) pv.getMapContext()).refreshLayerInPlanet(Layer3DProps.getLayer3DProps(selectedItems[i]),true);
		}

	}

	public String getText() {
		// Name that appears in toc menu
		return "Refrescar Capa";
	}

}
