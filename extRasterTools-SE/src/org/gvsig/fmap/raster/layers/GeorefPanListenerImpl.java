/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.raster.layers;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.MoveEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;


/**
 * Implementación de la interfaz PanListener como herramienta para realizar el
 * Pan.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GeorefPanListenerImpl implements PanListener {
	private static final GeometryManager 	geomManager	= GeometryLocator.getGeometryManager();
	private static final Logger 			logger 		= LoggerFactory.getLogger(GeorefPanListenerImpl.class);
//	private final Image ipan = new ImageIcon(MapControl.class.getResource(
//				"images/CruxCursor.png")).getImage();
	private final Image 					ipan 		= PluginServices.getIconTheme().get("crux-cursor").getImage();
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(ipan,
//			new Point(16, 16), "");
	private MapControl 						mapControl;
	private FLyrRasterSE 					lyrRaster 	= null;
	private String							pathToFile 	= null;


	/**
	 * Crea un nuevo RectangleListenerImpl.
	 *
	 * @param mapControl MapControl.
	 */
	public GeorefPanListenerImpl(MapControl mapControl) {
		this.mapControl = mapControl;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PanListener#move(java.awt.geom.Point2D,
	 * 		java.awt.geom.Point2D)
	 */
	public void move(MoveEvent event) {
		ViewPort vp = mapControl.getMapContext().getViewPort();

		Point2D from = vp.toMapPoint(event.getFrom());
		Point2D to = vp.toMapPoint(event.getTo());

		Rectangle2D.Double r = new Rectangle2D.Double();
		Rectangle2D extent = vp.getExtent();
		r.x = extent.getX() - (to.getX() - from.getX());
		r.y = extent.getY() - (to.getY() - from.getY());
		r.width = extent.getWidth();
		r.height = extent.getHeight();
		Envelope env;
		try {
			env = geomManager.createEnvelope(r.getMinX(), r.getMinY(), r
					.getMaxX(), r.getMaxY(), SUBTYPES.GEOM2D);
			vp.setEnvelope(env);
		} catch (CreateEnvelopeException e) {
			logger.error("Error creating the envelope", e);
		}
		


		// mapControl.drawMap();
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	/**
	 * @return Returns the lyrRaster.
	 */
	public FLyrRasterSE getLyrRaster() {
		return lyrRaster;
	}

	/**
	 * @param lyrRaster The lyrRaster to set.
	 */
	public void setLyrRaster(FLyrRasterSE lyrRaster) {
		this.lyrRaster = lyrRaster;
	}

	/**
	 * @return Returns the pathToFile.
	 */
	public String getPathToFile() {
		return pathToFile;
	}

	/**
	 * @param pathToFile The pathToFile to set.
	 */
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return ipan;
	}
}
