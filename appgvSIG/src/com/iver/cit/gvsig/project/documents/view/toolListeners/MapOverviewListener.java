/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.project.documents.view.toolListeners;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.view.MapOverview;


/**
 * <p>Listener for changes of the zoom caused by selecting a point on the associated {@link MapOverview MapOverview} object
 *  by a single click of the button 1 of the mouse.</p>
 *
 * <p>Updates the extent of its <code>ViewPort</code> with a <i>zoom out</i> operation, and enables/disables controls for
 *  managing the data.</p>
 *
 * @see ViewPort
 *
 * @author Vicente Caballero Navarro
 */
public class MapOverviewListener implements PointListener {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(MapOverviewListener.class);
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image izoomout = PluginServices.getIconTheme().get("crux-cursor").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(izoomout,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	protected MapOverview mapControl;

	/**
 	 * <p>Creates a new listener for zooming out the extent of the associated {@link MapOverview MapOverview} object.</p>
	 *
	 * @param mapControl the <code>MapControl</code> object which represents the <code>MapOverview</code>
	 */
	public MapOverviewListener(MapOverview mapControl) {
		this.mapControl = mapControl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) {
		ViewPort vp = mapControl.getMapContext().getViewPort();

		if (vp.getExtent() != null &&
				mapControl.getAssociatedMapContext().getViewPort().getExtent()!=null) {
			Point2D p = vp.toMapPoint(event.getPoint());
			Rectangle2D r = (Rectangle2D) mapControl.getAssociatedMapContext()
										   .getViewPort().getExtent().clone();

			r.setRect(p.getX() - (r.getWidth() / 2),
				p.getY() - (r.getHeight() / 2), r.getWidth(), r.getHeight());

			if (event.getEvent().getButton()!=MouseEvent.BUTTON1) {
				try {
					(this.mapControl).getAssociatedMapContext()
						.getViewPort().setEnvelope(geomManager.createEnvelope(r.getX(),r.getY(),r.getMaxX(),r.getMaxY(), SUBTYPES.GEOM2D));
				} catch (CreateEnvelopeException e) {
					logger.error("Error creating the envelope", e);
				}
				PluginServices.getMainFrame().enableControls();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return izoomout;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#pointDoubleClick(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void pointDoubleClick(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub

	}
}
