package org.gvsig.fmap.geom.operation;

import java.awt.Graphics2D;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.tools.task.Cancellable;


public class DrawOperationContext extends GeometryOperationContext{
	private ISymbol symbol=null;
	private Graphics2D graphics=null;
	private Cancellable cancellable;
	private double dpi;
	private double scale;
	private boolean hasDPI=false;
	private ViewPort viewPort;
	public ViewPort getViewPort() {
		return viewPort;
	}
	public void setViewPort(ViewPort viewPort) {
		this.viewPort = viewPort;
	}
	public Graphics2D getGraphics() {
		return graphics;
	}
	public void setGraphics(Graphics2D graphics) {
		this.graphics = graphics;
	}
	public ISymbol getSymbol() {
		return symbol;
	}
	public void setSymbol(ISymbol symbol) {
		this.symbol = symbol;
	}
	public void setCancellable(Cancellable cancel) {
		this.cancellable=cancel;

	}
	public Cancellable getCancellable() {
		return cancellable;
	}
	public void setDPI(double dpi) {
		this.hasDPI=true;
		this.dpi=dpi;
	}
	public double getDPI(){
		return dpi;
	}
	public boolean hasDPI() {
		return hasDPI;
	}
	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

}
