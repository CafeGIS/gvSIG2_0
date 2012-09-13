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
package org.gvsig.rastertools.saveraster.map;


import java.awt.geom.Point2D;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.raster.tools.SaveRasterListenerImpl;
import org.gvsig.rastertools.saveraster.ui.SaveRasterDialog;
import org.gvsig.rastertools.saveraster.ui.SaveRasterPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;


/**
 * Extensión de la clase SaveRasterListenerImpl de FMap para poder llamar a métodos
 * de andami o de gvSIG.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class SaveRasterListener extends SaveRasterListenerImpl {

//	FLyrRaster 			layer = null;

	public SaveRasterListener(MapControl mapCtrl) {
		super(mapCtrl);
	}

	/**
	 * 
	 * @param flyr
	 */
/*
	public void  setRasterLayer(FLyrRaster flyr){
		layer = flyr;
	}
*/

	public void rectangle(EnvelopeEvent event) {
		super.rectangle(event);

		if (PluginServices.getMainFrame() != null)
				PluginServices.getMainFrame().enableControls();

		FLayers layers = mapCtrl.getMapContext().getLayers();

		SaveRasterDialog saveRaster = null;
		boolean open = false;
		IWindow[] win = PluginServices.getMDIManager().getAllWindows();
		for(int i = 0; i < win.length; i++) {
			if(win[i] instanceof SaveRasterDialog) {
				saveRaster = (SaveRasterDialog)win[i];
				open = true;
			}
		}
		if(saveRaster == null)
			saveRaster = new SaveRasterDialog(layers, mapCtrl);

		ViewPort vp = mapCtrl.getViewPort();
		Point2D ini = vp.fromMapPoint(rect.getMinimum(0), rect.getMinimum(1));
		Point2D fin = vp.fromMapPoint(rect.getMaximum(0), rect.getMaximum(1));
		if(Math.abs(fin.getY()-ini.getY())>10 && Math.abs(fin.getX()-ini.getX())>10){
			SaveRasterPanel dialog = saveRaster.getControlsPanel();
			dialog.getBProperties().setEnabled(false);
			dialog.setProjection(vp.getProjection());
			//dialog.setWidthInPixelsGeodesicas((int)Math.abs(fin.getX()-ini.getX()));
			//dialog.setHeightInPixelsGeodesicas((int)Math.abs(fin.getY()-ini.getY()));

			int dec = 2;
			if(!vp.getProjection().isProjected())
				dec = 6;

			int indexPoint = String.valueOf(rect.getMaximum(0)).indexOf('.');
			try{
				dialog.getTInfDerX().setValue(String.valueOf(rect.getMaximum(0)).substring(0,indexPoint + dec));
			}catch(IndexOutOfBoundsException ex){
				dialog.getTInfDerX().setValue(String.valueOf(rect.getMaximum(0)));
			}

			indexPoint = String.valueOf(rect.getMaximum(1)).indexOf('.');
			try{
				dialog.getTInfDerY().setValue(String.valueOf(rect.getMinimum(1)).substring(0,indexPoint + dec));
			}catch(IndexOutOfBoundsException ex){
				dialog.getTInfDerY().setValue(String.valueOf(rect.getMinimum(1)));
			}

			indexPoint = String.valueOf(rect.getMinimum(0)).indexOf('.');
			try{
				dialog.getTSupIzqX().setValue(String.valueOf(rect.getMinimum(0)).substring(0,indexPoint + dec));
			}catch(IndexOutOfBoundsException ex){
				dialog.getTSupIzqX().setValue(String.valueOf(rect.getMinimum(0)));
			}

			indexPoint = String.valueOf(rect.getMinimum(1)).indexOf('.');
			try{
				dialog.getTSupIzqY().setValue(String.valueOf(rect.getMaximum(1)).substring(0,indexPoint + dec));
			}catch(IndexOutOfBoundsException ex){
				dialog.getTSupIzqY().setValue(String.valueOf(rect.getMaximum(1)));
			}
			if(!open)
				PluginServices.getMDIManager().addWindow(saveRaster);
		}
	}
}
