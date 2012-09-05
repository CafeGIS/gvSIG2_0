package org.gvsig.fmap.mapcontrol.tools.snapping.snappers;

import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * @author fjp
 *
 */
public interface ISnapper {


	// void setSnapPoint(Point2D snapPoint);

	void draw(Graphics g, Point2D pPixels);

	String getToolTipText();

	/**
	 * Implement this if you need a Snapper more important than the others.
	 * Default value is 0 (no prority).
	 * @return
	 */
	int getPriority();

	boolean isEnabled();
	public void setEnabled(boolean enabled);
	public void setPriority(int priority);
	Object getConfigurator();

}
