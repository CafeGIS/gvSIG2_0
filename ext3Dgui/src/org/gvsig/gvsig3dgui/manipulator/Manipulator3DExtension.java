package org.gvsig.gvsig3dgui.manipulator;

import java.awt.Component;

import org.gvsig.gvsig3d.cacheservices.OSGCacheService;
import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.manipulator.EditionManager;
import org.gvsig.osgvp.manipulator.Manipulator.DraggerType;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.IProjectView;

public class Manipulator3DExtension extends Extension {

	private NavigationMode navMode;
	private EditionManager em;

	public void execute(String actionCommand) {

		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;
		// Casting to View3D
		View3D vista3D = (View3D) view;
		Component viewer = (Component) vista3D.getCanvas3d();

		// remove active tool in MapControl
		boolean resetCursor = true;

//		navMode = vista3D.getNavMode();
//		navMode.removeAllModes();

		IProjectView model = vista3D.getModel();
		MapContext mapa = model.getMapContext();
		FLayer[] activeLayers = mapa.getLayers().getActives();
		if (activeLayers.length > 0) {
			FLayer layer = activeLayers[0];

			Layer3DProps props3D = Layer3DProps.getLayer3DProps(layer);
			OSGCacheService osgCacheService = (OSGCacheService) props3D
					.getCacheService();

			em = osgCacheService.getEditionManager();
			if (actionCommand.equals("ROTATION_MANIPULATOR")) {
//				em.changeDragger(DraggerType.ROTATE_SPHERE_DRAGGER);
				em.changeDragger(DraggerType.TABBOX_DRAGGER);
			} else if (actionCommand.equals("TABBOX_MANIPULATOR")) {
				em.changeDragger(DraggerType.TRACKBALL_DRAGGER);
			} 
		}

	}

	public void initialize() {
		// TODO Auto-generated method stub
		
		//Registering icons.
		PluginServices.getIconTheme().registerDefault(
				"tabbox_manipulator",
				this.getClass().getClassLoader().getResource(
				"images/rotation_manipulator.png"));
		
		PluginServices.getIconTheme().registerDefault(
				"rotation_manipulator",
				this.getClass().getClassLoader().getResource(
				"images/tabbox_manipulator.gif"));

	}

	public boolean isEnabled() {
		boolean result = false;
		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
		.getMDIManager().getActiveWindow();
		if ((view instanceof View3D)){
			// Casting to View3D
			View3D vista3D = (View3D) view;
			
			IProjectView model = vista3D.getModel();
			MapContext mapa = model.getMapContext();
			FLayer[] activeLayers = mapa.getLayers().getActives();
			if (activeLayers.length > 0) {
				FLayer layer = activeLayers[0];
				Layer3DProps props3D = Layer3DProps.getLayer3DProps(layer);
				if (props3D.getType() == Layer3DProps.layer3DOSG){
					result = true;
				}
			}
		}
		return result;
	}

	public boolean isVisible() {
		// Getting view3
		boolean result = false;
		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
		.getMDIManager().getActiveWindow();
		if ((view instanceof View3D)){
			// Casting to View3D
			View3D vista3D = (View3D) view;
			
			IProjectView model = vista3D.getModel();
			MapContext mapa = model.getMapContext();
			FLayer[] activeLayers = mapa.getLayers().getActives();
			if (activeLayers.length > 0) {
				FLayer layer = activeLayers[0];
				Layer3DProps props3D = Layer3DProps.getLayer3DProps(layer);
				if (props3D.getType() == Layer3DProps.layer3DOSG){
					result = true;
				}
			}
		}
		return result;
	}

}
