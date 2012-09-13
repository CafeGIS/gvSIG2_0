/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.rastertools.properties.control;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.NewStatusBar;
/**
 * Clase para poner la informacion de pixel RGB en la barra de estado
 * 
 * 22/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TranspByPixelStatusBarListener implements PointListener {
	private MapControl mapControl = null;

	/**
	 * Crea un nuevo StatusBarListener.
	 */
	public TranspByPixelStatusBarListener(MapControl mc) {
		mapControl = mc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) throws BehaviorException {
		if (PluginServices.getMainFrame() == null)
			return;

		PluginServices.getMainFrame().enableControls();

		Point2D point2D = event.getPoint();
		BufferedImage image = mapControl.getImage();
		int value = image.getRGB((int) point2D.getX(), (int) point2D.getY());
		int r = (value >> 16) & 0xff;
		int g = (value >> 8) & 0xff;
		int b = value & 0xff;

		NewStatusBar statusBar = PluginServices.getMainFrame().getStatusBar();
		statusBar.setMessage("x", r + ", " + g + ", " + b);
		statusBar.setMessage("y", "Hex: " +
				(r < 16 ? "0" : "")	+ Integer.toHexString(r) +
				(g < 16 ? "0" : "")	+ Integer.toHexString(g) +
				(b < 16 ? "0" : "")	+ Integer.toHexString(b));
	}

	public Cursor getCursor() {return null;}
	public boolean cancelDrawing() {return false;}
	public void pointDoubleClick(PointEvent event) throws BehaviorException {}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return null;
	}
}