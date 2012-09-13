/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.rastertools.rasterresolution;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 * Entrada de menú para la activación de la funcionalidad de zoom a un
 * pixel centrado en el cursor.
 */
public class ZoomPixelCursorTocMenuEntry extends AbstractTocContextMenuAction {
	public static final int ZOOM_TO_IMAGE_CENTER = 0x1;
	public static final int ZOOM_TO_VIEW_CENTER = 0x2;
	FLayer lyr = null;
	public int zoomType = ZOOM_TO_VIEW_CENTER;

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "group2"; //FIXME
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 20;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 2;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return PluginServices.getText(this, "Zoom_pixel");
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if(getNodeLayer(item) instanceof ILayerState)
			if(!((ILayerState)getNodeLayer(item)).isOpen()) 
				return false;
		return true;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)) {
			if(selectedItems == null || selectedItems.length != 1)
				return false;
			if((getNodeLayer(item) instanceof FLyrRasterSE) && 
			   ((FLyrRasterSE)getNodeLayer(item)).isActionEnabled(IRasterLayerActions.ZOOM_PIXEL_RESOLUTION))
				return getNodeLayer(item) instanceof FLyrRasterSE;
		}
		return false;

	}

	/**
	 * Método que se ejecuta cuando se pulsa la entrada en el menú contextual del TOC 
	 * correspondiente a "Zoom a la resolución del raster". Aquí se creará el mapTool si 
	 * no se ha hecho antes y se cargará.
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		if(selectedItems.length == 1) {
	        lyr = getNodeLayer(item);
	        BaseView vista = (BaseView) PluginServices.getMDIManager().getActiveWindow();
	    	MapControl mapCtrl = vista.getMapControl();
	    	//Si no se ha cargado el listener aún lo cargamos.
	        if(mapCtrl.getNamesMapTools().get("zoom_pixel_cursor_SE") == null) {
		        //Crea el listener del zoom a la resolución del raster
	        	StatusBarListener sbl = new StatusBarListener(mapCtrl);
		        ZoomPixelCursorListener zp = new ZoomPixelCursorListener(mapCtrl);
		        mapCtrl.addMapTool("zoom_pixel_cursor_SE", new Behavior[]{
		        					new PointBehavior(zp), new MouseMovementBehavior(sbl)});
	        }
	    	mapCtrl.setTool("zoom_pixel_cursor_SE");
	    	Project project = ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
			project.setModified(true);

		}
	}
}
