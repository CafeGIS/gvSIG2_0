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
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MoveEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.view.MapOverview;



/**
 * <p>Listener for moving the extent of the associated {@link MapOverview MapOverview} object
 *  according the movement between the initial and final points of line determined by the movement
 *  dragging with the third button of the mouse.</p>
 *
 * <p>Updates the extent of its <code>ViewPort</code> with the new position.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class MapOverviewPanListener implements PanListener {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(MapOverviewPanListener.class);
	
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image icursor = PluginServices
		.getIconTheme().get("crux-cursor").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(icursor,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	protected MapControl mapControl;

	/**
 	 * <p>Creates a new listener for changing the position of the extent of the associated {@link MapOverview MapOverview} object.</p>
	 *
	 * @param mapControl the <code>MapControl</code> object which represents the <code>MapOverview</code>
	 */
	public MapOverviewPanListener(MapControl mapControl) {
		this.mapControl = mapControl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return icursor;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	/*
	public void move(MoveEvent event) throws BehaviorException {
		if (!checkModifiers(event.getEvent().getModifiers())){
			return;
		}

		ViewPort vp = mapControl.getMapContext().getViewPort();
		MapOverview mov=(MapOverview) this.mapControl;
		ViewPort vpView=mov.getAssociatedMapContext().getViewPort();
		if (vp.getExtent() != null) {
			Point2D p = vp.toMapPoint(event.getTo());
			Rectangle2D r = (Rectangle2D) vpView.getExtent().clone();
			r.setRect(p.getX() - (r.getWidth() / 2),
				p.getY() - (r.getHeight() / 2), r.getWidth(), r.getHeight());
			//vpView.setExtent(r);
			double scale;
			double escalaX;
			double escalaY;
			double newHeight;
			double newWidth;
			double xCenter = r.getCenterX();
			double yCenter = r.getCenterY();

			escalaX = mapControl.getWidth() / r.getWidth();
			escalaY = mapControl.getHeight() / r.getHeight();
			Rectangle2D adjustedExtent = new Rectangle2D.Double();

			if (escalaX < escalaY) {
				scale = escalaX;
				newHeight = mapControl.getHeight() / scale;
				adjustedExtent.setRect(xCenter - (r.getWidth() / 2.0),
					yCenter - (newHeight / 2.0), r.getWidth(), newHeight);
			} else {
				scale = escalaY;
				newWidth = mapControl.getWidth() / scale;
				adjustedExtent.setRect(xCenter - (newWidth / 2.0),
					yCenter - (r.getHeight() / 2.0), newWidth,
					r.getHeight());
			}
			mov.refreshOverView(adjustedExtent);
		}

	}
	*/

	/*
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PanListener#move(MoveEvent)
	 */
	public void move(MoveEvent event) throws BehaviorException {


		if (!checkModifiers(event.getEvent())){
			return;
		}
		System.out.println("mapOvervierPan");
		MapOverview mov=(MapOverview) this.mapControl;
		ViewPort vp = mov.getViewPort();
		ViewPort vpView=mov.getAssociatedMapContext().getViewPort();

		if (vp.getExtent() != null && vpView.getExtent() != null) {

			// recogemos la forma de la vista actual
			Envelope curEnvelope = vpView.getAdjustedExtent();
			// traducimos las coordenadas en px de la ultima posicion del raton
			// a coordenadas de la vista
			Point2D thePoint= vp.toMapPoint( event.getTo());

			double diffx =
				(curEnvelope.getMaximum(0) - curEnvelope.getMinimum(0)) / 2;
			double diffy =
				(curEnvelope.getMaximum(1) - curEnvelope.getMinimum(1)) / 2;
			// Creamos un envelope del mismo tama�o
			// con la coordenadas del punto del raton,
			// teniendo en cuenta que estas sera el
			// centro del recuadro

			Envelope envelope;
			try {
				envelope = geomManager.createEnvelope(
						thePoint.getX() - diffx,
						thePoint.getY() - diffy,
						thePoint.getX() + diffx,
						thePoint.getY()	+ diffy,
						SUBTYPES.GEOM2D
				);
				// cambiamos la posicion
				mov.refreshOverView(envelope);
				vpView.setEnvelope(envelope);
			} catch (CreateEnvelopeException e) {
				logger.error("Error creating the envelope", e);
			}			
			mov.getAssociatedMapContext().invalidate();
		}
	}

	/**
	 * Determines if has pressed the button 3 of the mouse.
	 */
	private boolean checkModifiers(MouseEvent event) {

		int modifiers = event.getModifiers();
		/*
		int keyPressedMask = InputEvent.BUTTON2_MASK;

		*** No se porque el boton derecho del raton devuelve
		*** un modificador 'Meta + BUTTON3'. Pensaba que deberia
		*** devolver 'BUTTON2' ????
		*/
		int keyPressedMask = InputEvent.BUTTON3_MASK;
		return ((modifiers & keyPressedMask) == keyPressedMask);
	}
}
