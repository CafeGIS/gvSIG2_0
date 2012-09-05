/**
 *
 */
package com.iver.cit.gvsig.project.documents.view.toolListeners;

import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
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
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.mapcontrol.tools.Events.MoveEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.view.MapOverview;

/**
 * <p>Listener for changes of the zoom caused by selecting a rectangular area on the associated
 *  {@link MapOverview MapOverview} object with the first button of the mouse.</p>
 *
 * <p>If the kind of action was a movement on the associated object,
 *  updates the <i>extent</i> of its rectangular area.</p>
 *
 * <p>If the kind of action is the selection of a rectangular area, and is bigger than 3x3 pixels,
 *  applies a <i>zoom in</i> operation centering its <code>ViewPort</code> according the equivalent <i>extent</i>
 *  in map coordinates.</p>
 *
 * @see ViewPort
 *
 * @author jmvivo
 */
public class MapOverviewChangeZoomListener implements RectangleListener, PanListener {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(MapOverviewChangeZoomListener.class);
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image izoomin = PluginServices.getIconTheme()
		.get("cursor-zoom-in").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(izoomin,new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	protected MapControl mapControl;

	/**
	 * <p>Creates a new listener for changes of zoom at the associated {@link MapOverview MapOverview} object.</p>
	 *
	 * @param mapControl the <code>MapControl</code> object which represents the <code>MapOverview</code>
	 */
	public MapOverviewChangeZoomListener(MapControl mapControl) {
		this.mapControl=mapControl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.RectangleListener#rectangle(com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent)
	 */
	public void rectangle(EnvelopeEvent event) throws BehaviorException {

		if (!checkModifiers(event.getEvent())){
			return;
		}
		MapOverview mov=(MapOverview) this.mapControl;
		ViewPort vpView=mov.getAssociatedMapContext().getViewPort();
		ViewPort vp = mov.getViewPort();

		if (vp.getExtent() != null && vpView.getExtent() != null) {

			// Recuperamos las coordenadas del evento en px
			Rectangle2D pxRectangle = event.getPixelCoordRect();
			// Recuperamos las coordenadas del evento en coordenadas de la vista de localizador
			Envelope realRectangle = event.getWorldCoordRect();

			if ((pxRectangle.getWidth() < 3) && (pxRectangle.getHeight() < 3))
			{
				// rectangulo < 3 px no hacemos nada
				return;

			} else {
				// Cambiamos la extension de la vista asociada al localizador
				vpView.setEnvelope(realRectangle);
				mov.getAssociatedMapContext().invalidate();
				vpView.setEnvelope(realRectangle);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return izoomin;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true; //???
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PanListener#move(com.iver.cit.gvsig.fmap.tools.Events.MoveEvent)
	 */
	public void move(MoveEvent event) throws BehaviorException {


		if (!checkModifiers(event.getEvent())){
			return;
		}
		System.out.println("mapOvervierChangeZoom");
		MapOverview mov=(MapOverview) this.mapControl;
		ViewPort vpView=mov.getAssociatedMapContext().getViewPort();
		ViewPort vp = mov.getViewPort();

		if (vp.getExtent() != null && vpView.getExtent() != null) {

			// Creamos un recuadro con las coordenadas del raton
			// traducidas a la del mapa
//			Rectangle2D realRectangle = new Rectangle2D.Double();
			Point2D p1=vp.toMapPoint(event.getFrom());
			Point2D p2=vp.toMapPoint(event.getTo());
			Envelope realRectangle = null;
			double x=0;
			double y=0;
			double xMax=0;
			double yMax=0;
			if (p1.getX()<p2.getX()){
				x=p1.getX();
				xMax=p2.getX();
			}else{
				x=p2.getX();
				xMax=p1.getX();
			}
			if (p1.getY()<p2.getY()){
				y=p1.getY();
				yMax=p2.getY();
			}else{
				y=p2.getY();
				yMax=p1.getY();
			}
				try {
					realRectangle = geomManager.createEnvelope(x,y,xMax,yMax, SUBTYPES.GEOM2D);
				} catch (CreateEnvelopeException e) {
					logger.error("Error creating the envelope", e);
				}

			//			realRectangle.setFrameFromDiagonal(vp.toMapPoint(event.getFrom()),vp.toMapPoint(event.getTo()));

			// Establecemos la forma
			mov.refreshOverView(realRectangle);
		}



	}

	/**
	 * Determines if has pressed the button 1 of the mouse.
	 */
	private boolean checkModifiers(MouseEvent event) {
		int modifiers = event.getModifiers();
		int keyPressedMask = InputEvent.BUTTON1_MASK;
		return ((modifiers & keyPressedMask) == keyPressedMask);
	}
}
