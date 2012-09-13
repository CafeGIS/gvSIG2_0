package org.gvsig.gvsig3dgui.display;

import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.viewer.DisplaySettings;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public class DisplayMenu extends Extension {

	public void execute(String actionCommand) {

		// Getting view3D
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;

		if (actionCommand.equals("DISPLAY")) {

			DisplayPanel displayPanel = new DisplayPanel();
			// Showing it
			PluginServices.getMDIManager().addWindow(displayPanel);

		}

	}

	public void initialize() {
		// Register new icons
		// zoom to selection
		PluginServices.getIconTheme().registerDefault(
				"display-icon",
				this.getClass().getClassLoader().getResource(
						"images/display.png"));

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
