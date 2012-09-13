package com.iver.cit.gvsig;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.legend.gui.LayerProperties;

public class LayerPropertiesExtension extends Extension{

	public void initialize() {
		// TODO Auto-generated method stub
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().register(
				"images-selected-icon",
				this.getClass().getClassLoader().getResource("images/selected.png")
		);
		PluginServices.getIconTheme().register(
				"images-notselected-icon",
				this.getClass().getClassLoader().getResource("images/notSelected.png")
		);
		PluginServices.getIconTheme().register(
				"images-blocked-icon",
				this.getClass().getClassLoader().getResource("images/blocked.png")
		);
		PluginServices.getIconTheme().register(
				"images-unblocked-icon",
				this.getClass().getClassLoader().getResource("images/unblocked.png")
		);
		PluginServices.getIconTheme().register(
				"images-defuse-icon",
				this.getClass().getClassLoader().getResource("images/defuse.png")
		);
		PluginServices.getIconTheme().register(
				"images-active-icon",
				this.getClass().getClassLoader().getResource("images/active.png")
		);
		PluginServices.getIconTheme().register(
				"images-disabled-icon",
				this.getClass().getClassLoader().getResource("images/disabled.png")
		);
		PluginServices.getIconTheme().register(
				"images-notdisabled-icon",
				this.getClass().getClassLoader().getResource("images/notdisabled.png")
		);
		PluginServices.getIconTheme().register(
				"images-fill-icon",
				this.getClass().getClassLoader().getResource("images/fill.png")
		);
		PluginServices.getIconTheme().register(
				"images-notfill-icon",
				this.getClass().getClassLoader().getResource("images/notfill.png")
		);
	}

	public void execute(String actionCommand) {
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices.getMDIManager().getActiveWindow();
		if (!(view instanceof View))
			return;
		View vista=(View)view;
		IProjectView model = vista.getModel();
		MapContext mapa = model.getMapContext();
		MapControl mapCtrl = vista.getMapControl();
		FLayer[] layers=mapa.getLayers().getActives();
		//layers[0].
		LayerProperties layerProperties=new LayerProperties(layers[0],((FLyrVect)layers[0]).getLegend());
		PluginServices.getMDIManager().addWindow(layerProperties);
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return true;
	}

}
