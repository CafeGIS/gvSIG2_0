package es.unex.sextante.gvsig.extensions;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;
import com.iver.cit.gvsig.About;

public class SextanteAboutExtension extends Extension {
  /**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return false;
	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {

	}
	/**
	 * @see com.iver.andami.plugins.IExtension#postInitialize()
	 */
	public void postInitialize() {
		About about=(About)PluginServices.getExtension(About.class);
		FPanelAbout panelAbout=about.getAboutPanel();
		java.net.URL aboutURL = SextanteAboutExtension.class.getResource(
	        "/about.htm");
	        panelAbout.addAboutUrl("SEXTANTE",aboutURL);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
	}
}