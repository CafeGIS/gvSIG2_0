package org.gvsig.iconThemeBase;

import java.io.File;

import com.iver.andami.PluginServices;
import com.iver.andami.iconthemes.AbstractIconTheme;
import com.iver.andami.iconthemes.IconThemeDir;
import com.iver.andami.iconthemes.IconThemeManager;
import com.iver.andami.plugins.Extension;

public class ExtensionIcon22Theme extends Extension {


	public void execute(String actionCommand) {
		// TODO Auto-generated method stub

	}

	public void initialize() {
		// TODO Auto-generated method stub

		AbstractIconTheme iconTheme = new IconThemeDir(PluginServices.getIconThemeManager().getDefault());

		iconTheme.setName("IconsTheme22");
		iconTheme.setResource(new File (ExtensionIcon22Theme.getExtensionPath()+File.separator+ "images"+File.separator+"22x22"));
		PluginServices.getIconThemeManager().register(iconTheme);
		PluginServices.getIconThemeManager().setCurrent(iconTheme);
		iconTheme.load();


	}

	public static String getExtensionPath() {
		String pluginName = "org.gvsig.iconThemeBase";
		PluginServices ps = PluginServices.getPluginServices(pluginName);
		return ps.getPluginDirectory().getAbsolutePath();
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}
