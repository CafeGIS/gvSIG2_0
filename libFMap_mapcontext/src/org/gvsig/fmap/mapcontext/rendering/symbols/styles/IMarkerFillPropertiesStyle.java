package org.gvsig.fmap.mapcontext.rendering.symbols.styles;

import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
/**
 * IMarkerFillPropertiesStyle is an interface used by classes that are trying to fill a
 * polygon using marker symbols for that. In order to permit a big mount of possibilities for
 * the filling, this interface has the options to modify the rotation of the markers that
 * compose the padding, the x and y offset, the x and y separation,and the style of the fill.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IMarkerFillPropertiesStyle extends IStyle {
	public static final int RANDOM_FILL = 3;
	public static final int GRID_FILL = 1;
	public static final int SINGLE_CENTERED_SYMBOL = 2;

	/**
	 * <p>
	 * Defines an utility symbol to show up a thumbnail
	 * by default, this symbol is a SimpleMarkerSymbol.
	 * Thus, the drawInsideRectangle will always work. But
	 * it can be changed with setSampleSymbol(IMakerSymbol).<br>
	 * </p>
	 * <p>
	 * If <b>marker</b> is null, it does nothing
	 * </p>
	 */
	public void setSampleSymbol(IMarkerSymbol marker) ;

	/**
	 * Obtains the rotation of the marker symbol that compose the padding.
	 * @return the rotation of a marker
	 */
	public double getRotation();
	/**
	 * Sets the rotation of the marker symbol that compose the padding.
	 * @param rotation
	 */
	public void setRotation(double rotation) ;
	/**
	 * Returns the x offset of the markers that compose the padding
	 * @return xoffset of the marker
	 */
	public double getXOffset();
	/**
	 * Establishes de x offset of the markers that compose the padding
	 * @param offset
	 */
	public void setXOffset(double offset) ;
	/**
	 * Return the separation (x axis) between the markers that compose the padding
	 * @return x separation
	 */
	public double getXSeparation() ;
	/**
	 * Sets the separation (x axis) between the markers that compose the padding
	 * @param separation
	 */
	public void setXSeparation(double separation);

	/**
	 * Returns the y offset of the markers that compose the padding
	 * @return yoffset of the marker
	 */
	public double getYOffset();
	/**
	 * Establishes the y offset of the markers that compose the padding
	 * @param offset
	 */
	public void setYOffset(double offset) ;
	/**
	 * Return the separation (y axis) between the markers that compose the padding
	 * @return y separation
	 */
	public double getYSeparation() ;
	/**
	 * Sets the separation (y axis) between the markers that compose the padding
	 * @param separation
	 */
	public void setYSeparation(double separation);
	/**
	 * Sets the style for the fill that use marker symbols to do it(the filled).
	 * @param fillStyle
	 */
	public void setFillStyle(int fillStyle);
	/**
	 * Returns the style of the fill that use marker symbols to do it(the filled).
	 * @return int, fill style
	 */
	public int getFillStyle();
}
