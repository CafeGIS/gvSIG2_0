package org.gvsig.fmap.mapcontext;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.tools.task.Cancellable;

public interface MapContextDrawer {

	public void setMapContext(MapContext mapContext);
	public void setViewPort(ViewPort viewPort);
	public void draw(FLayers root, BufferedImage image, Graphics2D g, Cancellable cancel,
			double scale) throws ReadException;
	public void print(FLayers root, Graphics2D g, Cancellable cancel,
			double scale, PrintAttributes properties) throws ReadException;
	public void dispose();
}
