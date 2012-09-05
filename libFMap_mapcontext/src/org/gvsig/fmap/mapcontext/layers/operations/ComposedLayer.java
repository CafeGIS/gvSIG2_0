package org.gvsig.fmap.mapcontext.layers.operations;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerDrawEvent;
import org.gvsig.tools.task.Cancellable;

/**
 * Abstract class for the classes that make able
 * a single draw for a layers group
 *
 * @see  org.gvsig.fmap.mapcontext.layers.FLayer#newComposedLayer()
 */
public abstract class ComposedLayer {

	ArrayList pendingLayersEvents= new ArrayList();
	MapContext mapContext = null;

	public ComposedLayer(){

	}

	public void setMapContext(MapContext mapContext) {
		this.mapContext = mapContext;
	}

	/**
	 * Checks if the layer can be added to this group
	 *
	 * @param layer
	 * @return layer can be added or not
	 */
	public abstract boolean canAdd(FLayer layer);

	/**
	 * Adds the layer to the draw group.
	 * You have to implements the doAdd(FLayer) method.
	 *
	 *
	 * @see org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer#canAdd(FLayer)
	 *
	 * @param layer
	 * @throws Exception
	 */
	public final void add(FLayer layer) throws Exception {
		if (this.mapContext == null){
			this.mapContext =layer.getMapContext();
		}
        doAdd(layer);
    	pendingLayersEvents.add(layer);
	}

	/**
	 * Draws all the group in one pass
	 * You have to implements the doDraw method.
	 *
	 * @see  org.gvsig.fmap.mapcontext.layers.FLayer#draw(BufferedImage, Graphics2D, ViewPort, Cancellable, double)
	 */
    public final void draw(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel,double scale) throws ReadException {
    	doDraw(image, g, viewPort, cancel, scale);
    	fireLayerDrawingEvents(g,viewPort);
		pendingLayersEvents.clear();
    }

	/**
	 * Prints all the group in one pass
	 * You have to implements the doDraw method.
	 *
	 * @see  org.gvsig.fmap.mapcontext.layers.FLayer#draw(BufferedImage, Graphics2D, ViewPort, Cancellable, double)
	 */
    public final void print(Graphics2D g, ViewPort viewPort,
			Cancellable cancel,double scale,PrintAttributes properties) throws ReadException {
    	doPrint(g, viewPort, cancel, scale,properties);
		pendingLayersEvents.clear();
    }

	/**
	 * Fires the event LayerDrawEvent.LAYER_AFTER_DRAW for every
	 * layers of the group.
	 *
	 *
	 *  @see org.gvsig.fmap.mapcontext.layers.LayerDrawEvent
	 *
	 */
	private void fireLayerDrawingEvents(Graphics2D g, ViewPort viewPort) {
		Iterator iter = pendingLayersEvents.iterator();
		LayerDrawEvent afterEvent;
		FLayer layer = null ;
		while (iter.hasNext()) {
			layer =(FLayer)iter.next();
			afterEvent = new LayerDrawEvent(layer, g, viewPort, LayerDrawEvent.LAYER_AFTER_DRAW);
			System.out.println("+++ evento " + afterEvent.getLayer().getName());
			mapContext.fireLayerDrawingEvent(afterEvent);
		}
	}


	/**
	 * Adds the layer to the draw group.
	 * Specific implementation.
	 *
	 * @see org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer#add(FLayer)
	 *
	 * @see org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer#canAdd(FLayer)
	 *
	 * @param layer
	 * @throws Exception
	 */
	protected abstract void doAdd(FLayer layer) throws Exception ;

	/**
	 * Draws all the group in one pass.
	 * Specific implementation.
	 *
	 * @see  org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer#draw(BufferedImage, Graphics2D, ViewPort, Cancellable, double)
	 *
	 * @see  org.gvsig.fmap.mapcontext.layers.FLayer#draw(BufferedImage, Graphics2D, ViewPort, Cancellable, double)
	 */
	protected abstract void doDraw(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel,double scale) throws ReadException ;

	/**
	 * Prints all the group in one pass.
	 * Specific implementation.
	 *
	 * @see  org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer#print(Graphics2D, ViewPort, Cancellable, double)
	 *
	 * @see  org.gvsig.fmap.mapcontext.layers.FLayer#print(Graphics2D, ViewPort, Cancellable, double)
	 */
	protected abstract void doPrint(Graphics2D g, ViewPort viewPort,
			Cancellable cancel,double scale,PrintAttributes properties) throws ReadException ;

}
