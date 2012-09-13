package org.gvsig.gvsig3dgui.display;

import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.planets.CustomTerrainManipulator;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.OSGViewer;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public class ProjectionMode extends Extension {

	private boolean _perspective = true;
	private double _fovy = 30.0;

	public void execute(String actionCommand) {
		// Getting view3D
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;

		OSGViewer viewer = (OSGViewer) ((View3D) view).getCanvas3d()
		.getOSGViewer();
		if (actionCommand.equals("PROJECTION")) {

			NavigationMode nm = ((View3D) view).getNavMode();

			if (!_perspective) {
				((View3D) view).getCanvas3d().getOSGViewer()
						.setProjectionMatrixAsPerspective(
								_fovy,
								((double) viewer.getWidth())
										/ ((double) viewer
												.getHeight()), 1, 10000);
				_perspective = true;
				nm.removeAllNavigationModes((CustomTerrainManipulator) viewer.getCameraManipulator());
				

			} else {

				_fovy = viewer.getCamera().getProjectionMatrixAsPerspective().fovy;
				
				((View3D) view)
						.getCanvas3d()
						.getOSGViewer()
						.setProjectionMatrixAsOrtho(
								20 * (((double) viewer.getCamera()
										.getProjectionMatrixAsFrustum().left)),
								20 * (((double) viewer.getCamera()
										.getProjectionMatrixAsFrustum().right)),
								20 * (((double)viewer.getCamera()
										.getProjectionMatrixAsFrustum().bottom)),
								20 * (((double) viewer.getCamera()
										.getProjectionMatrixAsFrustum().top)),
								1, 1000);

				_perspective = false;
				nm.SetAzimutRollMode();

			}
		}

	}

	public void initialize() {
		// Register new icons
		// zoom to selection
		PluginServices.getIconTheme().registerDefault(
				"projection-icon",
				this.getClass().getClassLoader().getResource(
						"images/projection.png"));

	}

	public boolean isEnabled() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof View3D)
			return true;
		else
			return false;
	}

	public boolean isVisible() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof View3D)
			return true;
		else
			return false;
	}

}
