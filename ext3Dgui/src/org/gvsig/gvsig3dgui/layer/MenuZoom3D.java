package org.gvsig.gvsig3dgui.layer;

import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;

import org.gvsig.gvsig3dgui.view.View3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class MenuZoom3D extends Extension {


	private boolean activa = true;

	public void execute(String actionCommand) {
		// System.out.println("EXECUTE");

		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;
		// Casting to View3D
		View3D vista3D = (View3D) view;
		IProjectView model = vista3D.getModel();
		MapContext mapa = model.getMapContext();

		// Action for ZOOM_SELECT
		if (actionCommand.equals("ZOOM_SELECT")) {
			// Getting all selected Extent
			FLayer[] selectedExtent = mapa.getLayers().getActives();

			// If there is not one and is available
			if (selectedExtent.length == 1 && selectedExtent[0].isAvailable()
					&& (selectedExtent[0] instanceof FLyrVect)) {
				// Getting selected layer
				FLyrVect lyr3D = (FLyrVect) selectedExtent[0];
				// Gettin extend
				Rectangle2D ex = mapa.getSelectionBounds();
				// ex = lyr3D.getFullExtent();
				if (ex != null)
					// Doing zoom
					mapa.zoomToExtent(ex);
			}

		}
		// Action for TRANSPARENCY
		if (actionCommand.equals("TRANSPARENCY")) {
			FLayer[] selectedExtent = mapa.getLayers().getActives();

			// If there is not one and is available
			if (selectedExtent.length == 1 && selectedExtent[0].isAvailable()) {
				// // && (selectedExtent[0] instanceof FLyrVect)) {
				 FLayer lyr3D = selectedExtent[0];
				Layer3DProps props = Layer3DProps.getLayer3DProps(lyr3D);
				if (props.getType() == Layer3DProps.layer3DImage) {
					// Generating transparency panel
					TransparencyPanel transparencyPanel = new TransparencyPanel(
							vista3D.getModel());
					// Showing it
					PluginServices.getMDIManager().addWindow(transparencyPanel);
				} else {
					JOptionPane.showMessageDialog(null, PluginServices.getText(
							this, "No_available"));
				}
			}

		}

	}

	public void initialize() {
		
		// Register new icons
		// zoom to selection
		PluginServices.getIconTheme().registerDefault(
				"zoom-selection-icon",
				this.getClass().getClassLoader().getResource(
				"images/ZoomSeleccion.png"));
		// transparency
		PluginServices.getIconTheme().registerDefault(
				"transparency-icon",
				this.getClass().getClassLoader().getResource(
				"images/Alpha.png"));
		
		
		this.setActiva(true);
	}

	public boolean isEnabled() {
		View3D f = (View3D) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}
		try {

			// If the selected layer is instance of FlyrVect isEnable = true
			FLayer[] selected = f.getModel().getMapContext().getLayers()
					.getActives();
			Layer3DProps props = Layer3DProps.getLayer3DProps(selected[0]);
			if ((selected.length == 1) && (selected[0].isAvailable())) {
				if ((props.getType() == Layer3DProps.layer3DImage)
						|| (props.getType() == Layer3DProps.layer3DVector))
					return true;
			}
		} catch (Exception e) {
			return false;
		}

		// if (selected.length == 1 && selected[0] instanceof FLyrVect
		// && selected[0].isAvailable()) {
		// return true;
		// }
		// if (selected.length == 1 && selected[0] instanceof FLyrWMS
		// && selected[0].isAvailable()) {
		// return true;
		// }
		return false;
		// return true;
	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		// Only isVisible = true, where the view3D have layers
		if (f instanceof View3D) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			return mapa.getLayers().getLayersCount() > 0;
		}
		return false;
	}

	public void terminate() {
		super.terminate();
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
