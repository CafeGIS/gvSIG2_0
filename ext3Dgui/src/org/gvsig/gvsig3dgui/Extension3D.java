package org.gvsig.gvsig3dgui;

import javax.swing.JPopupMenu;

import org.gvsig.gvsig3dgui.preferences.View3DPreferences;
import org.gvsig.gvsig3dgui.tocMenu.TocEditingLayer;
import org.gvsig.gvsig3dgui.tocMenu.TocRefreshLayer;
import org.gvsig.gvsig3dgui.tocMenu.TocTransparencyPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;


public class Extension3D extends Extension {

	public void execute(String actionCommand) {
	}

	public void initialize() {
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ProjectView3DFactory.register();
		
		// TODO: registrar aki las opciones del menu contestual del toc
		ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
	    extensionPoints.add("View_TocActions",PluginServices.getText(this, "Transparency"),new TocTransparencyPanel());
	    extensionPoints.add("View_TocActions",PluginServices.getText(this, "Layer_Refresh"),new TocRefreshLayer());
	    extensionPoints.add("View_TocActions",PluginServices.getText(this, "Editing_layer"),new TocEditingLayer());

	    // Registering preferences dialog
	    extensionPoints.add("AplicationPreferences","View3DPreferences", new View3DPreferences());
	}
	
	

	public void postInitialize() {
		super.postInitialize();
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return false;
	}

	public void terminate() {
		super.terminate();
	}
	
}
