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
package org.gvsig.fmap.mapcontrol.tools;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

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



/**
 * <p>Listener for moving the extent of the associated {@link MapControl MapControl} object
 *  according the movement between the initial and final points of line determined by the movement
 *  dragging with the third button of the mouse.</p>
 *
 * <p>Updates the extent of its <code>ViewPort</code> with the new position.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class PanListenerImpl implements PanListener {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(PanListenerImpl.class);
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image ipan = new ImageIcon(MapControl.class.getResource(
				"images/Hand.gif")).getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(ipan,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	private MapControl mapControl;

	/**
  	 * <p>Creates a new listener for changing the position of the extent of the associated {@link MapControl MapControl} object.</p>
	 *
	 * @param mapControl the <code>MapControl</code> where will be applied the changes
	 */
	public PanListenerImpl(MapControl mapControl) {
		this.mapControl = mapControl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PanListener#move(com.iver.cit.gvsig.fmap.tools.Events.MoveEvent)
	 */
	public void move(MoveEvent event) {
		ViewPort vp = mapControl.getMapContext().getViewPort();

		// System.out.println(vp);
		Point2D from = vp.toMapPoint(event.getFrom());
		Point2D to = vp.toMapPoint(event.getTo());


		Rectangle2D extent = vp.getExtent();
		double x = extent.getX() - (to.getX() - from.getX());
		double y = extent.getY() - (to.getY() - from.getY());
		double width = extent.getWidth();
		double height = extent.getHeight();
		Envelope r;
		try {
			r = geomManager.createEnvelope(x, y, x+width, y+height, SUBTYPES.GEOM2D);
			vp.setEnvelope(r);
		} catch (CreateEnvelopeException e) {
			logger.error("Error creating the envelope", e);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return ipan;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}
}
