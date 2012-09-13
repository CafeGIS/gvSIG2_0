package org.gvsig.help;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.help.Help;
import com.iver.andami.plugins.Extension;

public class HelpExtension  extends Extension {


	private Logger log() {
		return LoggerFactory.getLogger("org.gvsig");
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public void execute(String actionCommand) {

		// If the option pressed is help control the help panel is created.
		if(actionCommand.equalsIgnoreCase("Help")){

			Help help = Help.getHelp();	//My constructor.
			help.show();//Launch help panel.

			return;
		}
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return true;
	}

	public static String getExtensionPath() {
		String pluginName = "org.gvsig.help";
		PluginServices ps = PluginServices.getPluginServices(pluginName);
		return ps.getPluginDirectory().getAbsolutePath();
	}

	@Override
	public void postInitialize() {
		super.postInitialize();
		Help help = Help.getHelp();	//My constructor.
		help.addResource(HelpExtension.getExtensionPath()+File.separator+ "gvSIG"+File.separator+"manual-de-usuario.zip");//Documentation path.
		help.addHelp("manual-de-usuario");

	}

}
