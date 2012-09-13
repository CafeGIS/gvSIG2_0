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
package org.gvsig.rastertools.clipping;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.fmap.raster.tools.behavior.TransformedRectangleBehavior;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.clipping.ui.ClippingDialog;
import org.gvsig.rastertools.clipping.ui.listener.ClippingMouseViewListener;
import org.gvsig.rastertools.clipping.ui.listener.ClippingPanelListener;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;
/**
 * <code>ClippingTocMenuEntry</code> es el punto de entrada del menu del
 * recorte.
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem {
	static private ClippingTocMenuEntry singleton = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private ClippingTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public ClippingTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new ClippingTocMenuEntry();
		return singleton;
	}

	/**
	 * Carga el listener de salvar a raster en el MapControl.
	 */
	private void loadClippingListener(MapControl m_MapControl, ClippingMouseViewListener clippingMouseViewListener, FLyrRasterSE lyr) {
		// Si no se ha cargado el listener aún lo cargamos.
		StatusBarListener sbl = new StatusBarListener(m_MapControl);
		TransformedRectangleBehavior trb = new TransformedRectangleBehavior(clippingMouseViewListener);
//		AffineTransform at = ((FLyrRasterSE)lyr).getAffineTransform();
//		trb.setAffineTransform(at);
		m_MapControl.addMapTool("clipRaster", new Behavior[] { trb, new MouseMovementBehavior(sbl) });
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
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "recorte");
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof ILayerState))
			return false;

		if (!((ILayerState) selectedItems[0]).isOpen())
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof FLyrRasterSE))
			return false;

		return ((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.FILTER);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayer fLayer = null;
		BaseView theView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		MapControl mapCtrl = theView.getMapControl();

		if (selectedItems.length != 1)
			return;

		fLayer = selectedItems[0];

		if (!(fLayer instanceof FLyrRasterSE))
			return;

		ClippingDialog clippingDialog = new ClippingDialog(420, 290, fLayer.getName());

		ClippingPanelListener clippingPanelListener = new ClippingPanelListener(clippingDialog.getClippingPanel());
		clippingPanelListener.setLayer((FLyrRasterSE)fLayer);
		clippingDialog.getClippingPanel().setClippingListener(clippingPanelListener);

		ClippingData clippingData = new ClippingData();
		clippingPanelListener.setData(clippingData);
		clippingData.addObserver(clippingDialog.getClippingPanel());

		ClippingMouseViewListener clippingMouseViewListener = new ClippingMouseViewListener(mapCtrl, clippingDialog.getClippingPanel(), clippingData, (FLyrRasterSE)fLayer);
		loadClippingListener(mapCtrl, clippingMouseViewListener, ((FLyrRasterSE)fLayer));

		RasterToolsUtil.addWindow(clippingDialog);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("clipping-icon");
	}
}