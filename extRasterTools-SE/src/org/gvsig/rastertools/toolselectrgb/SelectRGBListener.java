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
package org.gvsig.rastertools.toolselectrgb;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.rastertools.selectrasterlayer.SelectImageListImpl;

import com.iver.andami.PluginServices;
/**
 * Extensión de la clase SelectImageListenerImple de FMap. Esta clase permite
 * capturar el evento de la selección de un punto RGB sobre la vista
 * 
 * 22/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class SelectRGBListener extends SelectImageListImpl {
	private ISelectRGB selectRGB;

	/**
	 * Contructor
	 * @param mapCtrl
	 */
	public SelectRGBListener(MapControl mapCtrl, ISelectRGB selectRGB) {
		super(mapCtrl);
		this.selectRGB = selectRGB;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) {
		super.point(event);

		Point2D point2D = event.getPoint();

		if (PluginServices.getMainFrame() != null)
			PluginServices.getMainFrame().enableControls();

		BufferedImage image = mapCtrl.getImage();
		int value = image.getRGB((int) point2D.getX(), (int) point2D.getY());
		int r = (value >> 16) & 0xff;
		int g = (value >> 8) & 0xff;
		int b = value & 0xff;
		
		selectRGB.actionRGBSelected(r, g, b);
	}
}