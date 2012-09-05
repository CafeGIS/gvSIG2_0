package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;

public interface IPrintable {
	public void print(Graphics2D g,
					  AffineTransform at,
					  Geometry shape,
					  PrintAttributes properties);
}
