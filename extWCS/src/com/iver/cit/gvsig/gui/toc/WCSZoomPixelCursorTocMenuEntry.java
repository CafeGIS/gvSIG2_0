package com.iver.cit.gvsig.gui.toc;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.gui.toolListeners.WCSZoomPixelCursorListener;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * @author Nacho Brodin <brodin_ign@gva.es>
 *
 * Entrada de menú para la activación de la funcionalidad de zoom a un
 * pixel centrado en el cursor.
 */
public class WCSZoomPixelCursorTocMenuEntry extends
		AbstractTocContextMenuAction {

	public void execute(ITocItem item, FLayer[] selectedItems) {

		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		MapControl mapCtrl = vista.getMapControl();

		if (!mapCtrl.hasTool("zoom_pixel_cursor")) {
			StatusBarListener sbl = new StatusBarListener(mapCtrl);

			WCSZoomPixelCursorListener zp = new WCSZoomPixelCursorListener(
					mapCtrl);
			mapCtrl.addMapTool("zoom_pixel_cursor", new Behavior[] {
					new PointBehavior(zp), new MouseMovementBehavior(sbl) });

		}

		mapCtrl.setTool("zoom_pixel_cursor");
	}

	public String getText() {
		return PluginServices.getText(this, "Zoom_pixel");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return selectedItems.length == 1 && selectedItems[0].isAvailable();
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (selectedItems.length != 1) {
			return false;
		}
		return selectedItems[0] instanceof FLyrWCS;

	}

}