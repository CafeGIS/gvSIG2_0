package org.gvsig.gvsig3d.map3d;

import java.awt.Component;

import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.osgvp.planets.CustomTerrainManipulator;

import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class MapControl3D extends MapControl {

	BaseView view3D;
	

	public MapControl3D(BaseView view) {
		super();
		view3D = view;
	}

	public void setTool(String toolName) {
		prevTool = getCurrentTool();
		Behavior mapTool = (Behavior) namesMapTools.get(toolName);
		currentMapTool = mapTool;
		currentTool = toolName;

		// Getting viewer component
		Component viewer = (Component) ((MapContext3D) this.getMapContext())
				.getCanvas3d();
		// Setting cursor
		viewer.setCursor(mapTool.getCursor());

		// Getting the custom terrain manipulator
		CustomTerrainManipulator ctm = (CustomTerrainManipulator) ((MapContext3D)this.getMapContext()).getCanvas3d().getOSGViewer().getCameraManipulator();
		// Disabling All Navigation modes
		NavigationMode.removeAllNavigationModes(ctm);
	}

//	public void selectionChanged(SelectionEvent e) {
//		// TODO Auto-generated method stub
//		e.getEventType();
//		
//	}

	
}
