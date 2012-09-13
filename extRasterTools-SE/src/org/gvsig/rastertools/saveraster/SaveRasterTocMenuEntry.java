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
package org.gvsig.rastertools.saveraster;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.RectangleBehavior;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.saveraster.map.SaveRasterListener;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * Punto de entrada para la funcionalidad de salvar a raster
 * 
 * 22/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class SaveRasterTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem {
	static private SaveRasterTocMenuEntry singleton  = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private SaveRasterTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public SaveRasterTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new SaveRasterTocMenuEntry();
		return singleton;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "RasterExport";
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 50;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "salvar_raster_geo");
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null)
			return false;

		if (f instanceof BaseView) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			if (mapa.getLayers().getLayersCount() > 0) 
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		View theView = (View) PluginServices.getMDIManager().getActiveWindow();
		MapControl mapCtrl = theView.getMapControl();

		// Listener de eventos de movimiento que pone las coordenadas del ratón en
		// la barra de estado
		StatusBarListener sbl = new StatusBarListener(mapCtrl);

		RasterToolsUtil.messageBoxInfo("start_save", null);
		loadSaveRasterListener(mapCtrl, sbl);
		mapCtrl.setTool("saveRaster");
	}
	
	/**
	 * Carga el listener de salvar a raster en el MapControl.
	 */
	private void loadSaveRasterListener(MapControl m_MapControl, StatusBarListener sbl) {
		// Si no se ha cargado el listener aún lo cargamos.
		if (m_MapControl.getNamesMapTools().get("saveRaster") == null) {
			SaveRasterListener srl = new SaveRasterListener(m_MapControl);
			m_MapControl.addMapTool("saveRaster", new Behavior[] { new RectangleBehavior(srl), new MouseMovementBehavior(sbl) });
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return PluginServices.getIconTheme().get("save-raster");
	}
}