package org.gvsig.fmap.mapcontrol.tools.snapping.snappers;

import java.awt.Color;

public abstract class AbstractSnapper implements ISnapper {

	// private Point2D snapPoint = null;
	private int sizePixels = 10;
	private Color color = Color.MAGENTA;
	private boolean enabled;
	private int priority=10;
//	public void setSnapPoint(Point2D snapPoint) {
//		this.snapPoint = snapPoint;
//
//	}


	public int getSizePixels() {
		return sizePixels;
	}

	public void setSizePixels(int sizePixels) {
		this.sizePixels = sizePixels;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Object getConfigurator(){
//		DefaultConfigurePanel configurePanel=new DefaultConfigurePanel();
//		return configurePanel;
		return null;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	 /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapper#getPriority()
     */
    public int getPriority() {
        return priority;
    }
	public void setPriority(int priority) {
		this.priority=priority;
	}
}
