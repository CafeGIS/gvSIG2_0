package org.gvsig.gvsig3dgui.layout;

import org.gvsig.gvsig3dgui.layout.fframe.FFrameView3DFractory;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.gui.preferencespage.LayoutPage;
import com.iver.cit.gvsig.project.documents.layout.FLayoutZooms;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.behavior.LayoutRectangleBehavior;

public class layout3DExtension extends Extension {
	private Layout layout = null;
	private static LayoutPage layoutPropertiesPage = new LayoutPage();

	// private static Logger logger =
	// Logger.getLogger(LayoutExtension.class.getName());

	public void execute(String s) {
		// TODO Auto-generated method stub
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		
		LayoutAddView3DListenerImpl lavl = new LayoutAddView3DListenerImpl(layout);
		
		layout.getLayoutControl().addLayoutTool("view3Dinsert", new LayoutRectangleBehavior(lavl));
		
		FLayoutZooms zooms = new FLayoutZooms(layout);
		// logger.debug("Comand : " + s);

		if (s.equals("INSERT_3D_VIEW")) {
			layout.getLayoutControl().setTool("view3Dinsert");
		}
	}

	public void initialize() {
		FFrameView3DFractory.register();
		
		
		// Registering icons
		
		PluginServices.getIconTheme().registerDefault(
				"insert_3d_view_map",
				this.getClass().getClassLoader().getResource(
				"images/layout-insert-view3D.png"));
	}
	
	

	@Override
	public void postInitialize() {
		super.postInitialize();
		// Registering types
		
		
//		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
//		
//		LayoutAddViewListenerImpl lavl = new LayoutAddViewListenerImpl(layout);
//		
//		layout.getLayoutControl().addLayoutTool("view3Dinsert", new LayoutRectangleBehavior(lavl));
		// register the icons
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
			return true; // layout.m_Display.getMapControl().getMapContext().getLayers().layerCount()
							// > 0;
		}
		return false;
	}

}
