package es.unex.sextante.gvsig.gui;

import com.iver.andami.PluginServices;

import es.unex.sextante.gui.core.DefaultGUIFactory;

public class gvGUIFactory extends DefaultGUIFactory {

	public void showToolBoxDialog() {

		ToolboxDialog toolbox = new ToolboxDialog();
		m_Toolbox = toolbox.getToolboxPanel();
		PluginServices.getMDIManager().addWindow(toolbox);

	}

	public void showModelerDialog(){

		ModelerDialog modeler = new ModelerDialog();
		PluginServices.getMDIManager().addWindow(modeler);

	}

}
