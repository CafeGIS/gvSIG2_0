package org.gvsig.gvsig3dgui.display;

import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.exceptions.node.ChildIndexOutOfBoundsExceptions;
import org.gvsig.osgvp.planets.PlanetViewer;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public class EnableCompass extends Extension {

	boolean value = true;
	
	public void execute(String actionCommand) {

		// Getting view3D
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;

		if (actionCommand.equals("COMPASS")) {

			try {
				System.out.println("Node is going to be" + value);
				((Group) ((PlanetViewer) ((View3D) view).getCanvas3d().getOSGViewer()).getNodeFromHUD(0)).getChild(1).setEnabledNode(!value);
				value = !value;
			} catch (ChildIndexOutOfBoundsExceptions e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void initialize() {
		// Register new icons
		// zoom to selection
		PluginServices.getIconTheme().registerDefault(
				"compass-icon",
				this.getClass().getClassLoader().getResource(
						"images/compass-icon.png"));

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
