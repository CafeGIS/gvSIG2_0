package org.gvsig.fmap.mapcontext.layers;

import java.awt.Graphics2D;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.events.FMapEvent;


/**
 * <p>The <code>LayerDrawEvent</code> class represents an event produced on a layer about a drawing change.</p>
 * <p>This event can be a notification before or after a {@link FLayer FLayer}, {@link FLayers FLayers}, or
 *  {@link GraphicLayer GraphicLayer} is drawn.</p>
 */
public class LayerDrawEvent extends FMapEvent {
	/**
	 * <p>The view port the information for drawing the layer in which this event has been produced.</p>
	 */
	ViewPort vp;
	
	/**
	 * <p>Reference to an object for rendering 2D on the Java(tm) platform.</p>
	 */
	Graphics2D g;
	
	/**
	 * <p>Reference to the layer where this event has been produced.</p>
	 */
	FLayer lyr;
	
	/**
	 * Public constant that identifies the kind of <code>LayerDrawEvent</code> as an event produced
	 *  before drawing on a {@link FLayer FLayer} or {@link FLayers FLayers}.
	 */
	public static final int LAYER_BEFORE_DRAW = 101;
	
	/**
	 * Public constant that identifies the kind of <code>LayerDrawEvent</code> as an event produced
	 *  after drawing on a {@link FLayer FLayer} or {@link FLayers FLayers}.
	 */
	public static final int LAYER_AFTER_DRAW = 102;
	
	/**
	 * Public constant that identifies the kind of <code>LayerDrawEvent</code> as an event produced
	 *  before drawing on a {@link GraphicLayer GraphicLayer}.
	 */
	public static final int GRAPHICLAYER_BEFORE_DRAW = 103;
	
	/**
	 * Public constant that identifies the kind of <code>LayerDrawEvent</code> as an event produced
	 *  after drawing on a {@link GraphicLayer GraphicLayer}.
	 */
	public static final int GRAPHICLAYER_AFTER_DRAW = 104;

	/**
	 * <p>Creates a new layer draw event with all necessary information.</p>
	 * 
	 * @param lyr layer in which the event has been produced
	 * @param g object for rendering 2D on the Java(tm) platform
	 * @param vp object with information for drawing the layer in which this event has been produced
	 * @param eventType identifies the kind of this event: {@linkplain #LAYER_BEFORE_DRAW}, {@linkplain #LAYER_AFTER_DRAW},
	 *  {@linkplain #GRAPHICLAYER_BEFORE_DRAW} or {@linkplain #GRAPHICLAYER_AFTER_DRAW}.
	 */
	public LayerDrawEvent(FLayer lyr, Graphics2D g, ViewPort vp, int eventType) {
		this.lyr = lyr;
		this.g = g;
		this.vp = vp;
		setEventType(eventType);
	}
	
	/**
	 * <p>Returns an object for rendering 2-dimensional shapes, text and images on the Java(tm) platform.</p>
	 * 
	 * @return object for rendering 2D on the Java(tm) platform
	 */
	public Graphics2D getGraphics() {
		return g;
	}
	
	/**
	 * <p>Returns the view port the information for drawing the layer in which this event has been produced.</p>
	 * 
	 * @return object with information for drawing the layer in which this event has been produced
	 */
	public ViewPort getViewPort() {
		return vp;
	}

	/**
	 * <p>Returns a reference to the layer where this event has been produced.</p> 
	 * 
	 * @return layer in which this event has been produced
	 */
	public FLayer getLayer() {
		return lyr;
	}
}
