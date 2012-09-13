/**
 *
 */
package com.iver.cit.gvsig.fmap.layers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer;
import org.gvsig.tools.task.Cancellable;


/**
 * Group WMS layers to make a single request to the
 * server for all layers.
 *
 * It is posible only if almost all params are the same. For this
 * comparasion, ComposedLayerWMS uses the method
 * {@link com.iver.cit.gvsig.fmap.layers.FLyrWMS#isComposedLayerCompatible(com.iver.cit.gvsig.fmap.layers.FLayer)}
 *
 *
 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.ComposedLayer
 * @see com.iver.cit.gvsig.fmap.layers.FLyrWMS
 */
public class ComposedLayerWMS extends ComposedLayer {
	private FLyrWMS layer=null;

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.ComposedLayer#canAdd(com.iver.cit.gvsig.fmap.layers.FLayer)
	 */
	public boolean canAdd(FLayer layer) {
		if (this.layer != null) {
			return this.layer.isComposedLayerCompatible(layer);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.ComposedLayer#doAdd(com.iver.cit.gvsig.fmap.layers.FLayer)
	 */
	public void doAdd(FLayer layer) throws Exception {
		FLyrWMS aLayer =(FLyrWMS)layer;
		if (this.layer == null) {
			this.layer = new FLyrWMS();
			this.layer.setXMLEntity(aLayer.getXMLEntity());
			return;
		}
		this.layer.setLayerQuery( this.layer.getLayerQuery() + ","+ aLayer.getLayerQuery());
		Vector aStyles = aLayer.getStyles();

		if (aStyles != null) {
			Vector myStyles = this.layer.getStyles();
			if (myStyles == null) {
				this.layer.setStyles(aStyles);
			} else {
				myStyles.addAll(aStyles);
				this.layer.setStyles(myStyles);
			}
		}

		//revisar el fullextend para ajustarlo a todas las capas
		this.layer.getFullEnvelope().add(aLayer.getFullEnvelope());

	}


	@Override
	protected void doDraw(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel, double scale)
			throws ReadException {
		this.layer.draw(image, g, viewPort, cancel, scale);

	}

	@Override
	protected void doPrint(Graphics2D g, ViewPort viewPort,
			Cancellable cancel,
			double scale,
			PrintAttributes properties) throws ReadException {
		this.layer.print(g, viewPort, cancel, scale, properties);

	}

}
