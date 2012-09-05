/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.mapcontext;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.GeoCalc;
import org.cresques.cts.IProjection;
import org.cresques.cts.UTM;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.events.ColorEvent;
import org.gvsig.fmap.mapcontext.events.ExtentEvent;
import org.gvsig.fmap.mapcontext.events.ProjectionEvent;
import org.gvsig.fmap.mapcontext.events.listeners.ViewPortListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;


/**
 * <p><code>ViewPort</code> class represents the logic needed to transform a rectangular area of a map
 *  to the available area in screen to display it.</p>
 *
 * <p>Includes an affine transformation, between the rectangular area selected of the external map, in its own
 *  <i>map coordinates</i>, to the rectangular area available of a view in <i>screen coordinates</i>.</p>
 *
 * <p>Elements:
 * <ul>
 * <li><i>extent</i>: the area selected of the map, in <i>map coordinates</i>.
 * <li><i>imageSize</i>: width and height in pixels (<i>screen coordinates</i>) of the area available
 *  in screen to display the area selected of the map.
 * <li><i>adjustedExtent</i>: the area selected must be an scale of <i>imageSize</i>.<br>This implies adapt the
 *  extent, preserving and centering it, and adding around the needed area to fill all the image size. That
 *  added area will be extracted from the original map, wherever exists, and filled with the background color
 *  wherever not.
 * <li><i>scale</i>: the scale between the adjusted extent and the image size.
 * <li><i>backColor</i>: the default background color in the view, if there is no map.
 * <li><i>trans</i>: the affine transformation.
 * <li><i>proj</i>: map projection used in this view.
 * <li><i>distanceUnits</i>: distance measurement units, of data in screen.
 * <li><i>mapUnits</i>: measurement units, of data in map.
 * <li><i>extents</i>: an {@link ExtentHistory ExtentHistory} with the last previous extents.
 * <li><i>offset</i>: position in pixels of the available rectangular area, where start drawing the map.
 * <li><i>dist1pixel</i>: the distance in <i>world coordinates</i> equivalent to 1 pixel in the view with the
 *  current extent.
 * <li><i>dist3pixel</i>: the distance in <i>world coordinates</i> equivalent to 3 pixels in the view with the
 *  current extent.
 * <li><i>listeners</i>: list with the {@link ViewPortListener ViewPortListener} registered.
 * </ul>
 * </p>
 *
 * @author Vicente Caballero Navarro
 */
public class ViewPort {
//	/**
//	 * <p>Metric unit or length equal to 1000 meters.</p>
//	 */
//	public static int KILOMETROS = 0;
//
//	/**
//	 * <p>The base unit of length in the International System of Units that is equal to the distance
//	 *  traveled by light in a vacuum in {frac;1;299,792,458} second or to about 39.37 inches.</p>
//	 */
//	public static int METROS = 1;
//
//	/**
//	 * <p>Metric unit or length equal to 0'01 meters.</p>
//	 */
//	public static int CENTIMETRO = 2;
//
//	/**
//	 * <p>Metric unit or length equal to 0'001 meters.</p>
//	 */
//	public static int MILIMETRO = 3;
//
//	/**
//	 * <p>The international statute mile by international agreement. It is defined to be precisely
//	 *  1,760 international yards (by definition, 0.9144 m each) and is therefore exactly 1,609.344
//	 *  metres (1.609344 km).</p>
//	 */
//	public static int MILLAS = 4;
//
//	/**
//	 * <p>Unit of length equal in the United States to 0.9144 meter.</p>
//	 */
//	public static int YARDAS = 5;
//
//	/**
//	 * <p>Any of various units of length based on the length of the human foot; especially :
//	 *  a unit equal to 1/3 yard and comprising 12 inches.</p>
//	 */
//	public static int PIES = 6;
//
//	/**
//	 * <p>Unit of length equal to 1/36 yard.</p>
//	 */
//	public static int PULGADAS = 7;
//
//	/**
//	 * <p>Grades according the current projection.</p>
//	 */
//	public static int GRADOS = 8;
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);
	
	/**
	 * <p>Screen resolution in <i>dots-per-inch</i>. Useful to calculate the geographic scale of the view.</p>
	 *
	 * @see Toolkit#getScreenResolution()
	 * @see #getScale()
	 */
	private static int dpi = java.awt.Toolkit.getDefaultToolkit()
											 .getScreenResolution();

	/**
	 * <p>Area selected by user using some tool.</p>
	 *
	 * <p>When the zoom changes (for instance when using the zoom in or zoom out tools,
	 *  but also zooming to a selected feature or shape) the extent that covers that
	 *  area is the value returned by this method. It is not the actual area shown
	 *  in the view because it does not care about the aspect ratio of the available
	 *  area. However, any part of the real world contained in this extent is shown
	 *  in the view.
	 * </p>
	 * <p>
	 * Probably this is not what you are looking for. If you are looking for
	 * the complete extent currently shown, you must use {@linkplain #getAdjustedExtent()} method
	 * which returns the extent that contains this one but regarding the current
	 * view's aspect ratio.
	 * </p>
	 *
	 * @see #getExtent()
	 * @see #setEnvelope(Envelope)
	 */
	protected Rectangle2D extent;

	/**
	 * <p>Location and dimensions of the extent adjusted to the image size.</p>
	 *
	 * @see #getAdjustedExtent()
	 */
	protected Rectangle2D adjustedExtent;

	/**
	 * Draw version of the context. It's used for know when de componend has
	 * changed any visualization property
	 *
	 *  @see getDrawVersion
	 *  @see updateDrawVersion
	 */
	private long drawVersion= 0L;

	/**
	 * <p>History with the last extents of the view.</p>
	 *
	 * @see #setPreviousExtent()
	 * @see #getExtents()
	 */
	protected ExtentHistory extents = new ExtentHistory();

	/**
	 * <p>Size in <i>screen coordinates</i> of the rectangle where the image is displayed.</p>
	 * <p>Used by {@linkplain #calculateAffineTransform()} to calculate:<br>
  	 *
	 * <ul>
	 * <li>The new {@link #scale scale} .
	 * <li>The new {@link #adjustedExtent adjustableExtent} .
	 * <li>The new {@link #trans trans} .
	 * <li>The new real world coordinates equivalent to 1 pixel ({@link #dist1pixel dist1pixel}) .
	 * <li>The new real world coordinates equivalent to 3 pixels ({@link #dist3pixel dist3pixel}) .
	 * </ul>
	 * </p>
	 *
	 * @see #getImageSize()
	 * @see #getImageHeight()
	 * @see #getImageWidth()
	 * @see #setImageSize(Dimension)
	 */
	private Dimension imageSize;

	/**
	 * <p>the affine transformation between the {@link #extent extent} in <i>map 2D coordinates</i> to
	 *  the image area in the screen, in <i>screen 2D coordinates</i> (pixels).</p>
	 *
	 * @see AffineTransform
	 *
	 * @see #getAffineTransform()
	 * @see #setAffineTransform(AffineTransform)
	 * @see #calculateAffineTransform()
	 */
	private AffineTransform trans = new AffineTransform();

	/**
	 * <p>Measurement unit used for measuring distances and displaying information.</p>
	 *
	 * @see #getDistanceUnits()
	 * @see #setDistanceUnits(int)
	 */
	private int distanceUnits = 1;
	/**
	 * <p>Measurement unit used for measuring areas and displaying information.</p>
	 *
	 * @see #getDistanceArea()
	 * @see #setDistanceArea(int)
	 */
	private int distanceArea = 1;
	/**
	 * <p>Measurement unit used by this view port for the map.</p>
	 *
	 * @see #getMapUnits()
	 * @see #setMapUnits(int)
	 */
	private int mapUnits = 1;

	/**
	 * <p>Array with the {@link ViewPortListener ViewPortListener}s registered to this view port.</p>
	 *
	 * @see #addViewPortListener(ViewPortListener)
	 * @see #removeViewPortListener(ViewPortListener)
	 */
	private ArrayList listeners = new ArrayList();

	/**
	 * <p>The offset is the position where start drawing the map.</p>
	 * <p>The offset of a <a href="http://www.gvsig.gva.es/">gvSIG</a>'s <i>View</i> is
	 * always (0, 0) because the drawing area fits with the full window area. But in
	 * a <a href="http://www.gvsig.gva.es/">gvSIG</a>'s <i>Layout</i> it's up to the place where
	 * the <code>FFrameView</code> is located.</p>
	 *
	 * @see #getOffset()
	 * @see #setOffset(Point2D)
	 */
	private Point2D offset = new Point2D.Double(0, 0);

	/**
	 * <p>Clipping area.</p>
	 */
	private Rectangle2D clip;

	/**
	 * <p>Background color of this view.</p>
	 *
	 * @see #getBackColor()
	 * @see #setBackColor(Color)
	 */
	private Color backColor = null; //Color.WHITE;

	/**
	 * <p>Information about the map projection used in this view.</p>
	 *
	 * @see #getProjection()
	 * @see #setProjection(IProjection)
	 */
	private IProjection proj;

	/**
	 * <p>Represents the distance in <i>world coordinates</i> equivalent to 1 pixel in the view with the current extent.</p>
	 *
	 * @see #getDist1pixel()
	 * @see #setDist1pixel(double)
	 */
	private double dist1pixel;

	/**
	 * <p>Represents the distance in <i>world coordinates</i> equivalent to 3 pixels in the view with the current extent.</p>
	 *
	 * @see #getDist3pixel()
	 * @see #setDist3pixel(double)
	 */
	private double dist3pixel;

	/**
	 * <p>Ratio between the size of <code>imageSize</code> and <code>extent</code>:<br> <i><pre>min{(imageSize.getHeight()/extent.getHeight(), imageSize.getWidth()/extent.getWidth())}</pre></i></p>
	 */
	private double scale;

	/**
	 * <p>Clipping area.</p>
	 *
	 * @see #setClipRect(Rectangle2D)
	 */
	private Rectangle2D cliprect;

	/**
	 * <p>Enables or disables the <i>"adjustable extent"</i> mode.</p>
	 *
	 * <p>
	 * When calculates the affine transform, if
	 * <ul>
	 * <li><i>enabled</i>: the new <code>adjustedExtent</code> will have the (X, Y) coordinates of the <code>extent</code> and
	 *  an area that will be an scale of the image size. That area will have different
	 *  height or width (not both) of the extent according the least ratio (height or width) in <pre>image.size/extent.size"</pre>.
	 * <li><i>disabled</i>: the new <code>adjustedExtent</code> will be like <code>extent</code>.
	 * </ul>
	 * </p>
	 *
	 * @see #setAdjustable(boolean)
	 */
	private boolean adjustableExtent=true;

	/**
	 * <p>Creates a new view port with the information of the projection in <code>proj</code> argument, and
	 *  default configuration:</p>
	 * <p>
	 * <ul>
	 *  <li><i><code>distanceUnits</code></i> = meters
	 *  <li><i><code>mapUnits</code></i> = meters
	 *  <li><i><code>backColor</code></i> = <i>undefined</i>
	 *  <li><i><code>offset</code></i> = <code>new Point2D.Double(0, 0);</code>
	 * </ul>
	 * </p>
	 *
	 * @param proj information of the projection for this view port
	 */
	public ViewPort(IProjection proj) {
		// Por defecto
		this.proj = proj;
	}

	/**
	 * <p>Changes the status of the <i>"adjustable extent"</i> option to enabled or disabled.</p>
	 *
	 * <p>If view port isn't adjustable, won't bear in mind the aspect ratio of the available rectangular area to
	 *  calculate the affine transform from the original map in real coordinates. (Won't scale the image to adapt
	 *  it to the available rectangular area).</p>
	 *
	 * @param boolean the boolean to be set
	 */
	public void setAdjustable(boolean adjustable) {
		if (adjustable == adjustableExtent){
			return;
		}
		adjustableExtent = adjustable;
		this.updateDrawVersion();
	}

	/**
	 * <p>Appends the specified {@link ViewPortListener ViewPortListener} listener if weren't.</p>
	 *
	 * @param arg0 the listener to add
	 *
	 * @return <code>true</code> if has been added successfully
	 *
	 * @see #removeViewPortListener(ViewPortListener)
	 */
	public boolean addViewPortListener(ViewPortListener arg0) {
		if (!listeners.contains(arg0)) {
			return listeners.add(arg0);
		}
		return false;
	}

	/**
 	 * <p>Removes the specified {@link ViewPortListener ViewPortListener} listener, if existed.</p>
	 *
	 * @param arg0 the listener to remove
	 *
	 * @return <code>true</code> if the contained the specified listener.
	 *
	 * @see #addViewPortListener(ViewPortListener)
	 */
	public boolean removeViewPortListener(ViewPortListener arg0) {
		return listeners.remove(arg0);
	}

	/**
	 * <p>Converts and returns the distance <code>d</code>, that is in <i>map
	 *  coordinates</i> to <i>screen coordinates</i> using a <i>delta transform</i> with
	 *  the transformation affine information in the {@link #trans #trans} attribute.</p>
	 *
	 * @param d distance in <i>map coordinates</i>
	 *
	 * @return distance equivalent in <i>screen coordinates</i>
	 *
	 * @see #toMapDistance(int)
	 * @see AffineTransform#deltaTransform(Point2D, Point2D)S
	 */
	public int fromMapDistance(double d) {
		Point2D.Double pWorld = new Point2D.Double(1, 1);
		Point2D.Double pScreen = new Point2D.Double();

		try {
			trans.deltaTransform(pWorld, pScreen);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

		return (int) (d * pScreen.x);
	}

	/**
	 * <p>Converts and returns the 2D point <code>(x,y)</code>, that is in <i>map
	 *  coordinates</i> to <i>screen coordinates</i> (pixels) using
	 *  the affine transformation in the {@link #trans #trans} attribute.</p>
	 *
	 * @param x the <code>x</code> <i>map coordinate</i> of a 2D point
	 * @param y the <code>y</code> <i>map coordinate</i> of a 2D point
	 *
	 * @return 2D point equivalent in <i>screen coordinates</i> (pixels)
	 *
	 * @see #fromMapPoint(Point2D)
	 * @see AffineTransform#transform(Point2D, Point2D)
	 */
	public Point2D fromMapPoint(double x, double y) {
		Point2D.Double pWorld = new Point2D.Double(x, y);
		Point2D.Double pScreen = new Point2D.Double();

		try {
			trans.transform(pWorld, pScreen);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

		return pScreen;
	}

	/**
	 * <p>Converts and returns the 2D point argument, that is in <i>map
	 *  coordinates</i> to <i>screen coordinates</i> (pixels) using
	 *  the affine transformation in the {@link #trans #trans} attribute.</p>
	 *
	 * @param point the 2D point in <i>map coordinates</i>
	 *
	 * @return 2D point equivalent in <i>screen coordinates</i> (pixels)
	 *
	 * @see #toMapPoint(Point2D)
	 * @see #fromMapPoint(double, double)
	 */
	public Point2D fromMapPoint(Point2D point) {
		return fromMapPoint(point.getX(), point.getY());
	}

	/**
	 * <p>Converts and returns the 2D point <code>(x,y)</code>, that is in <i>screen coordinates</i>
	 *  (pixels) to <i>map coordinates</i> using
	 *  the affine transformation in the {@link #trans #trans} attribute.</p>
	 *
	 * @param x the <code>x</code> <i>screen coordinate</i> of a 2D point
	 * @param y the <code>y</code> <i>screen coordinate</i> of a 2D point
	 *
	 * @return 2D point equivalent in <i>map coordinates</i>
	 *
	 * @see #toMapPoint(Point2D)
	 * @see #fromMapPoint(double, double)
	 */
	public Point2D toMapPoint(int x, int y) {
		Point2D pScreen = new Point2D.Double(x, y);

		return toMapPoint(pScreen);
	}

	/**
	 * <p>Converts and returns the {@link Rectangle2D Rectangle2D}, that is in <i>screen
	 *  coordinates</i> (pixels) to <i>map coordinates</i> using {@linkplain #toMapDistance(int)},
	 *  and {@linkplain #toMapPoint(int, int)}.</p>
	 *
	 * @param r the 2D rectangle in <i>screen coordinates</i> (pixels)
	 * @return 2D rectangle equivalent in <i>map coordinates</i>
	 *
	 * @see #fromMapRectangle(Rectangle2D)
	 * @see #toMapDistance(int)
	 * @see #toMapPoint(int, int)
	 */
	public Rectangle2D toMapRectangle(Rectangle2D r){
		Rectangle2D rect=new Rectangle2D.Double();
		Point2D p1=toMapPoint((int)r.getX(),(int)r.getY());
		Point2D p2=toMapPoint((int)r.getMaxX(),(int)r.getMaxY());
		rect.setFrameFromDiagonal(p1,p2);
		return rect;
	}

	/**
	 * <p>Converts and returns the distance <code>d</code>, that is in <i>screen
	 *  coordinates</i> to <i>map coordinates</i> using the transformation affine information
	 *  in the {@link #trans #trans} attribute.</p>
	 *
	 * @param d distance in pixels
	 *
	 * @return distance equivalent in <i>map coordinates</i>
	 *
	 * @see #fromMapDistance(double)
	 * @see AffineTransform
	 */
	public double toMapDistance(int d) {
		double dist = d / trans.getScaleX();

		return dist;
	}

	/**
	 * <p>Converts and returns the 2D point argument, that is in <i>screen coordinates</i>
	 *  (pixels) to <i>map coordinates</i> using the
	 *  inverse affine transformation of the {@link #trans #trans} attribute.</p>
	 *
	 * @param pScreen the 2D point in <i>screen coordinates</i> (pixels)
	 *
	 * @return 2D point equivalent in <i>map coordinates</i>
	 *
	 * @see #toMapPoint(int, int)
	 * @see AffineTransform#createInverse()
	 * @see AffineTransform#transform(Point2D, Point2D)
	 */
	public Point2D toMapPoint(Point2D pScreen) {
		Point2D.Double pWorld = new Point2D.Double();
		AffineTransform at;

		try {
			at = trans.createInverse();
			at.transform(pScreen, pWorld);
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException("Non invertible transform Exception",e);
		}

		return pWorld;
	}

	/**
	 * <p>Returns the real distance (in <i>world coordinates</i>) at the graphic layers of two 2D points
	 *  (in <i>map coordinates</i>) of the plane where is selected the <i>extent</i>.</p>
	 * <p>If the projection of this view is UTM, considers the Earth curvature.</p>
	 *
	 * @param pt1 a 2D point in <i>map coordinates</i>
	 * @param pt2 another 2D point in <i>map coordinates</i>
	 *
	 * @return the distance in meters between the two points 2D
	 *
	 * @see GeoCalcImpl#distanceVincenty(Point2D, Point2D)
	 */
	public double distanceWorld(Point2D pt1, Point2D pt2) {
		double dist = -1;
		dist = pt1.distance(pt2);

		if ((proj != null) && !(proj instanceof UTM)) {
			dist = new GeoCalc(proj).distanceVincenty(proj.toGeo(pt1),
					proj.toGeo(pt2));
			return dist;
		}
		return (dist*MapContext.getDistanceTrans2Meter()[getMapUnits()]);
	}

	/**
	 * <p>Sets as extent and adjusted extent of this view port, the previous. Recalculating
	 *  its parameters.</p>
	 *
	 * @see #getExtents()
	 * @see #calculateAffineTransform()
	 */
	public void setPreviousExtent() {
		this.updateDrawVersion();
		extent = extents.removePrev();

		//Calcula la transformación afín
		calculateAffineTransform();

		// Lanzamos los eventos de extent cambiado
		callExtentChanged(getAdjustedExtent());
	}

	/**
	 * <p>Gets the area selected by user using some tool.</p>
	 *
	 * <p>When the zoom changes (for instance using the <i>zoom in</i> or <i>zoom out</i> tools,
	 *  but also zooming to a selected feature or shape) the extent that covers that
	 *  area is the value returned by this method. It is not the actual area shown
	 *  because it doesn't care about the aspect ratio of the image size of the view. However, any
	 *  part of the real world contained in this extent is shown in the view.</p>
	 *
	 * <p>If you are looking for the complete extent currently shown, you must use the
	 *  {@linkplain #getAdjustedExtent()} method.</p>
	 *
	 * @return the current extent
	 *
	 * @see #setEnvelope(Envelope)
	 * @see #getAdjustedExtent()
	 * @see #setPreviousExtent()
	 * @see #getExtents()
	 */
	public Rectangle2D getExtent() {
		return extent;
	}

	/**
	 * <p>Changes the <i>extent</i> and <i>adjusted extent</i> of this view port:<br>
	 * <ul>
	 * <li>Stores the previous extent.
	 * <li>Calculates the new extent using <code>r</code>:
	 * <pre>extent = new Rectangle2D.Double(r.getMinX() - 0.1, r.getMinY() - 0.1, r.getWidth() + 0.2, r.getHeight() + 0.2);</pre>
	 * <li>Executes {@linkplain #calculateAffineTransform()}: getting the new scale, adjusted extent, affine transformation between
	 *  map and screen coordinates, the real world coordinates equivalent to 1 pixel, and the real world coordinates equivalent to 3 pixels.
	 * <li>Notifies all {@link ViewPortListener ViewPortListener} registered that the extent has changed.
	 * </ul>
	 * </p>
	 *
	 * @param r the new extent
	 *
	 * @see #getExtent()
	 * @see #getExtents()
	 * @see #calculateAffineTransform()
	 * @see #setPreviousExtent()
	 */
	public void setEnvelope(Envelope r) {
		Rectangle2D newExtent=null;
		//Esto comprueba que el extent no es de anchura o altura = "0"
		//y si es así lo redimensiona.
		if (r!=null) {
			if ((r.getMaximum(0) - r.getMinimum(0) == 0)
					|| (r.getMaximum(1) - r.getMinimum(1) == 0)) {
				newExtent = new Rectangle2D.Double(r.getMinimum(0) - 0.1,
					r.getMinimum(1) - 0.1, r.getMaximum(0)-r.getMinimum(0) + 0.2, r.getMaximum(1)-r.getMinimum(1) + 0.2);
			} else {
				newExtent = new Rectangle2D.Double(r.getMinimum(0),r.getMinimum(1),Math.abs(r.getMaximum(0)-r.getMinimum(0)),Math.abs(r.getMaximum(1)-r.getMinimum(1)));
			}
		}

		if (this.extent != null && this.extent.equals(newExtent)){
			return;
		}
		if (extent != null) {
			extents.put(extent);
		}
		this.updateDrawVersion();
		this.extent = newExtent;

		//Calcula la transformación afín
		calculateAffineTransform();

		// Lanzamos los eventos de extent cambiado
		callExtentChanged(getAdjustedExtent());
	}

	/**
	 * <p>Changes the <i>extent</i> and <i>adjusted extent</i> of this view port:<br>
	 * <ul>
	 * <li>Executes {@linkplain #calculateAffineTransform()}: getting the new scale, adjusted extent, affine transformation between
	 *  map and screen coordinates, the real world coordinates equivalent to 1 pixel, and the real world coordinates equivalent to 3 pixels.
	 * <li>Notifies to all {@link ViewPortListener ViewPortListener} registered that the extent has changed.
	 * </ul>
	 * </p>
	 *
	 * @see #setEnvelope(Envelope)
	 * @see #calculateAffineTransform()
	 */
	public void refreshExtent() {
		//this.scale = scale;

		//Calcula la transformación afín
		calculateAffineTransform();

		// Lanzamos los eventos de extent cambiado
		callExtentChanged(getAdjustedExtent());
	}

	/**
	 * <p>Calculates and returns using the current projection of this view port, the scale that
	 *  is the extent in <i>screen coordinates</i> from the image in <i>map coordinates</i>.</p>
	 *
	 * @return the scale <i>extent / image size</i> projected by this view port
	 *
	 * @deprecated since 07/09/07, use {@linkplain MapContext#getScaleView()}
	 */
	public double getScale() {
		return proj.getScale(extent.getMinX(), extent.getMaxX(),
			imageSize.width, dpi);
	}

	/**
	 * <p>Affine transformation between <i>map 2D coordinates</i> to <i>screen 2D coordinates</i> (pixels),
	 * preserving the "straightness" and "parallelism" of the lines.</p>
	 *
	 * @return the affine transformation
	 *
	 * @see #setAffineTransform(AffineTransform)
	 * @see #calculateAffineTransform()
	 */
	public AffineTransform getAffineTransform() {
		return trans;
	}

	/**
	 * <p>Returns the size of the image projected.</p>
	 *
	 * @return the image size
	 *
	 * @see #setImageSize(Dimension)
	 * @see #getImageHeight()
	 * @see #getImageWidth()
	 */
	public Dimension getImageSize() {
		return imageSize;
	}

	/**
	 * <p>Sets the size of the image projected, recalculating the parameters of this view port.</p>
	 *
	 * @param imageSize the image size
	 *
	 * @see #getImageSize()
	 * @see #calculateAffineTransform()
	 */
	public void setImageSize(Dimension imageSize) {

		if (this.imageSize == null  || (!this.imageSize.equals(imageSize))){
				this.updateDrawVersion();
				this.imageSize = imageSize;
				calculateAffineTransform();
		}
	}

	/**
	 * <p>Notifies to all view port listeners registered, that the adjusted extent of this view port
	 *  has changed.</p>
	 *
	 * @param newRect the new adjusted extend
	 *
	 * @see #refreshExtent()
	 * @see #setEnvelope(Envelope)
	 * @see #setPreviousExtent()
	 * @see ExtentEvent
	 * @see ViewPortListener
	 */
	protected void callExtentChanged(Envelope newRect) {
		ExtentEvent ev = ExtentEvent.createExtentEvent(newRect);

		for (int i = 0; i < listeners.size(); i++) {
			ViewPortListener listener = (ViewPortListener) listeners.get(i);
			listener.extentChanged(ev);
		}
	}

	/**
	 * <p>Notifies to all view port listeners registered, that the background color of this view port
	 *  has changed.</p>
	 *
	 * @param c the new background color
	 *
	 * @see #setBackColor(Color)
	 * @see ColorEvent
	 * @see ViewPortListener
	 */
	private void callColorChanged(Color c) {
		ColorEvent ce = ColorEvent.createColorEvent(c);

		for (int i = 0; i < listeners.size(); i++) {
			ViewPortListener listener = (ViewPortListener) listeners.get(i);
			listener.backColorChanged(ce);
		}
	}

	/**
	 * <p>Notifies to all view port listeners registered, that the projection of this view port
	 *  has changed.</p>
	 *
	 * @param projection the new projection
	 *
	 * @see #setProjection(IProjection)
	 * @see ProjectionEvent
	 * @see ViewPortListener
	 */
	private void callProjectionChanged(IProjection projection) {
		ProjectionEvent ev = ProjectionEvent.createProjectionEvent(projection);

		for (int i = 0; i < listeners.size(); i++) {
			ViewPortListener listener = (ViewPortListener) listeners.get(i);
			listener.projectionChanged(ev);
		}
	}

	/**
	 * <p>Calculates the affine transformation between the {@link #extent extent} in <i>map 2D coordinates</i> to
	 *  the image area in the screen, in <i>screen 2D coordinates</i> (pixels).</p>
	 *
	 * <p>This process recalculates some parameters of this view port:<br>
	 *
	 * <ul>
	 * <li>The new {@link #scale scale} .
	 * <li>The new {@link #adjustedExtent adjustedExtent} .
	 * <li>The new {@link #trans trans} .
	 * <li>The new real world coordinates equivalent to 1 pixel ({@link #dist1pixel dist1pixel}) .
	 * <li>The new real world coordinates equivalent to 3 pixels ({@link #dist3pixel dist3pixel}) .
	 * </ul>
	 * </p>
	 *
	 * @see #getAffineTransform()
	 * @see #setAffineTransform(AffineTransform)
	 * @see #refreshExtent()
	 * @see #setEnvelope(Envelope)
	 * @see #setImageSize(Dimension)
	 * @see #setPreviousExtent()
	 * @see #createFromXML(XMLEntity)
	 * @see AffineTransform
	 */
	private void calculateAffineTransform() {
		if ((imageSize == null) || (extent == null) ||
				(imageSize.width <= 0) || (imageSize.height <= 0)) {
			return;
		}

		AffineTransform escalado = new AffineTransform();
		AffineTransform translacion = new AffineTransform();

		double escalaX;
		double escalaY;

		escalaX = imageSize.width / extent.getWidth();
		escalaY = imageSize.height / extent.getHeight();

		double xCenter = extent.getCenterX();
		double yCenter = extent.getCenterY();
		double newHeight;
		double newWidth;

		adjustedExtent = new Rectangle2D.Double();

		if (adjustableExtent) {
			if (escalaX < escalaY) {
				scale = escalaX;
				newHeight = imageSize.height / scale;
				adjustedExtent.setRect(xCenter - (extent.getWidth() / 2.0),
					yCenter - (newHeight / 2.0), extent.getWidth(), newHeight);
			} else {
				scale = escalaY;
				newWidth = imageSize.width / scale;
				adjustedExtent.setRect(xCenter - (newWidth / 2.0),
					yCenter - (extent.getHeight() / 2.0), newWidth,
					extent.getHeight());
			}
			escalado.setToScale(scale, -scale);
		}
		else { // adjusted is same as extent
			scale = escalaX;
			adjustedExtent.setFrame(extent);
			escalado.setToScale(escalaX, -escalaY);
		}
		Envelope env=getAdjustedExtent();
		if (env == null) {
			return;
		}
		translacion.setToTranslation(-env.getMinimum(0),
			-env.getMinimum(1) - getAdjustedExtent().getLength(1));

		AffineTransform offsetTrans = new AffineTransform();
		offsetTrans.setToTranslation(offset.getX(), offset.getY());

		trans.setToIdentity();
		trans.concatenate(offsetTrans);
		trans.concatenate(escalado);

		trans.concatenate(translacion);

		// Calculamos las distancias de 1 pixel y 3 pixel con esa transformación
		// de coordenadas, de forma que estén precalculadas para cuando las necesitemos
		AffineTransform at;

		try {
			at = trans.createInverse();

			java.awt.Point pPixel = new java.awt.Point(1, 1);
			Point2D.Float pProv = new Point2D.Float();
			at.deltaTransform(pPixel, pProv);

			dist1pixel = pProv.x;
			dist3pixel = 3 * pProv.x;
		} catch (NoninvertibleTransformException e) {
			System.err.println("transformada afin = " + trans.toString());
			System.err.println("extent = " + extent.toString() +
				" imageSize= " + imageSize.toString());
			throw new RuntimeException("Non invertible transform Exception",e);
		}
	}

	/**
	 * <p>Sets the offset.</p>
	 * <p>The offset is the position where start drawing the map.</p>
	 *
	 * @param p 2D point that represents the offset in pixels
	 *
	 * @see #getOffset()
	 */
	public void setOffset(Point2D p) {
		if (!offset.equals(p)){
			this.updateDrawVersion();
			offset = p;
		}
	}

	/**
	 * <p>Gets the offset.</p>
	 * <p>The offset is the position where start drawing the map.</p>
	 *
	 * @return 2D point that represents the offset in pixels
	 *
	 * @see #setOffset(Point2D)
	 */
	public Point2D getOffset() {
		return offset;
	}

	/**
	 * <p>Sets the background color.</p>
	 *
	 * @param c the new background color
	 *
	 * @see #getBackColor()
	 */
	public void setBackColor(Color c) {
		if (!c.equals(this.backColor)){
			this.updateDrawVersion();
			backColor = c;
			callColorChanged(backColor);
		}
	}

	/**
	 * <p>Gets the background color.</p>
	 *
	 * @return the background color of the view
	 *
	 * @see #setBackColor(Color)
	 */
	public Color getBackColor() {
		return backColor;
	}

	/**
	 * <p>Returns the extent currently covered by the view adjusted (scaled) to the image size aspect.</p>
	 *
	 * @return extent of the view adjusted to the image size aspect
	 *
	 * @see #setAdjustable(boolean)
	 */
	public Envelope getAdjustedExtent() {
		if (cliprect!=null){
			Rectangle2D r= adjustedExtent.createIntersection(cliprect);
			try {
				return geomManager.createEnvelope(r.getX(),r.getY(),r.getMaxX(),r.getMaxY(), SUBTYPES.GEOM2D);
			} catch (CreateEnvelopeException e) {
				e.printStackTrace();
				logger.error("Error adjusting the extent", e);
			}
		}
		if (adjustedExtent!=null) {
			try {
				return geomManager.createEnvelope(adjustedExtent.getX(),adjustedExtent.getY(),adjustedExtent.getMaxX(),adjustedExtent.getMaxY(), SUBTYPES.GEOM2D);
			} catch (CreateEnvelopeException e) {
				e.printStackTrace();
				logger.error("Error adjusting the extent", e);
			}
		}
		return null;
	}

	/**
	 * <p>Returns the measurement unit of this view port used for measuring distances and displaying information.</p>
	 *
	 * @return the measurement unit of this view used for measuring distances and displaying information
	 *
	 * @see #setDistanceUnits(int)
	 */
	public int getDistanceUnits() {
		return distanceUnits;
	}
	/**
	 * <p>Returns the measurement unit of this view port used for measuring areas and displaying information.</p>
	 *
	 * @return the measurement unit of this view used for measuring areas and displaying information
	 *
	 * @see #setDistanceUnits(int)
	 */
	public int getDistanceArea() {
		return distanceArea;
	}
	/**
	 * <p>Sets the measurement unit of this view port used for measuring distances and displaying information.</p>
	 *
	 * @param distanceUnits the measurement unit of this view used for measuring distances and displaying information
	 *
	 * @see #getDistanceUnits()
	 */
	public void setDistanceUnits(int distanceUnits) {
		this.distanceUnits = distanceUnits;
	}
	/**
	 * <p>Sets the measurement unit of this view port used for measuring areas and displaying information.</p>
	 *
	 * @param distanceUnits the measurement unit of this view used for measuring areas and displaying information
	 *
	 * @see #getDistanceUnits()
	 */
	public void setDistanceArea(int distanceArea) {
		this.distanceArea = distanceArea;
	}
	/**
	 * <p>Gets the measurement unit used by this view port for the map.</p>
	 *
	 * @return Returns the current map measure unit
	 *
	 * @see #setMapUnits(int)
	 */
	public int getMapUnits() {
		return mapUnits;
	}

	/**
	 * <p>Sets the measurement unit used by this view port for the map.</p>
	 *
	 * @param mapUnits the new map measure unit
	 *
	 * @see #getMapUnits()
	 */
	public void setMapUnits(int mapUnits) {
		this.mapUnits = mapUnits;
	}

	/**
	 * <p>Gets the width in <i>screen coordinates</i> of the rectangle where the image is displayed.</p>
	 * <p>Used by {@linkplain #calculateAffineTransform()} to calculate:<br>
  	 *
	 * <ul>
	 * <li>The new {@link #scale scale} .
	 * <li>The new {@link #adjustedExtent adjustableExtent} .
	 * <li>The new {@link #trans trans} .
	 * <li>The new real world coordinates equivalent to 1 pixel ({@link #dist1pixel dist1pixel}) .
	 * <li>The new real world coordinates equivalent to 3 pixels ({@link #dist3pixel dist3pixel}) .
	 * </ul>
	 * </p>
	 *
	 * @see #getImageHeight()
	 * @see #getImageSize()
	 * @see #setImageSize(Dimension)
	 */
	public int getImageWidth() {
		return imageSize.width;
	}

	/**
	 * <p>Gets the height in <i>screen coordinates</i> of the rectangle where the image is displayed.</p>
	 * <p>Used by {@linkplain #calculateAffineTransform()} to calculate:<br>
  	 *
	 * <ul>
	 * <li>The new {@link #scale scale} .
	 * <li>The new {@link #adjustedExtent adjustableExtent} .
	 * <li>The new {@link #trans trans} .
	 * <li>The new real world coordinates equivalent to 1 pixel ({@link #dist1pixel dist1pixel}) .
	 * <li>The new real world coordinates equivalent to 3 pixels ({@link #dist3pixel dist3pixel}) .
	 * </ul>
	 * </p>
	 *
	 * @see #getImageWidth()
	 * @see #getImageSize()
	 * @see #setImageSize(Dimension)
	 */
	public int getImageHeight() {
		return imageSize.height;
	}

	/**
	 * <p>Gets the distance in <i>world coordinates</i> equivalent to 1 pixel in the view with the current extent.</p>
	 *
	 * @return the distance
	 *
	 * @see #setDist1pixel(double)
	 */
	public double getDist1pixel() {
		return dist1pixel;
	}

	/**
	 * <p>Sets the distance in <i>world coordinates</i> equivalent to 1 pixel in the view with the current extent.</p>
	 *
	 * @param dist1pixel the distance
	 *
	 * @see #getDist1pixel()
	 */
	public void setDist1pixel(double dist1pixel) {
		if (dist1pixel == this.dist1pixel){
			return;
		}
			this.updateDrawVersion();
		this.dist1pixel = dist1pixel;
	}

	/**
	 * <p>Gets the distance in <i>world coordinates</i> equivalent to 3 pixels in the view with the current extent.</p>
	 *
	 * @return the distance
	 *
	 * @see #setDist3pixel(double)
	 */
	public double getDist3pixel() {
		return dist3pixel;
	}

	/**
	 * <p>Sets the distance in <i>world coordinates</i> equivalent to 3 pixels in the view with the current extent.</p>
	 *
	 * @param dist3pixel the distance
	 *
	 * @see #getDist3pixel()
	 */
	public void setDist3pixel(double dist3pixel) {
		if (this.dist3pixel == dist3pixel){
			return;
		}
		this.updateDrawVersion();
		this.dist3pixel = dist3pixel;
	}

	/**
	 * <p>Returns the last previous extents of this view port.</p>
	 *
	 * @return the last previous extents of this view port
	 *
	 * @see #setPreviousExtent()
	 */
	public ExtentHistory getExtents() {
		return extents;
	}

	/**
	 * <p>Gets the projection used in this view port.</p>
	 *
	 * @return projection used in this view port
	 *
	 * @see #setProjection(IProjection)
	 */
	public IProjection getProjection() {
		return proj;
	}

	/**
	 * <p>Sets the projection to this view port.</p>
	 *
	 * @param proj the new projection
	 *
	 * @see #getProjection()
	 */
	public void setProjection(IProjection proj) {
		if(this.proj == null || !this.proj.getAbrev().equals(proj.getAbrev())) {
			this.updateDrawVersion();
			this.proj = proj;
			callProjectionChanged(proj);
		}
	}

//  -----------------------------------------------------------------------------------------------------------
//  NOTA PARA DESARROLLADORES SOBRE EL MÉTODO "public void setAffineTransform(AffineTransform at)"
//  ==============================================================================================
//	  Only used for print, should be removed, redefining the {@link RasterAdapter RasterAdapter} interface,
//	   allowing it to receive a {@link ViewPortData ViewPortData} .
//  -----------------------------------------------------------------------------------------------------------

	/**
	 * <p>Sets only the affine transform to this view port, without updating dependent attributes.</p>
	 * <p><b><i>This method could be problematic!</i></b></p>
	 *
	 * @param at the affine transform to set
	 *
	 * @see #getAffineTransform()
	 * @see #calculateAffineTransform()
	 */
	public void setAffineTransform(AffineTransform at)
	{
	    this.trans = at;
	}

	/**
	 * <p>Returns an XML entity that represents this view port instance:<br>
	 * <ul>
	 * <li>Properties:
	 *  <ul>
	 *  <li><i>className</i>: name of this class.
	 *  <li>If defined, the adjusted extent:
	 *   <ul>
	 *   <li><i>adjustedExtentX</i>: X coordinate of the adjusted extent.
	 *   <li><i>adjustedExtentY</i>: Y coordinate of the adjusted extent.
	 *   <li><i>adjustedExtentW</i>: width of the adjusted extent.
	 *   <li><i>adjustedExtentH</i>: height of the adjusted extent.
	 *   </ul>
	 *  <li>If defined, the background color:
	 *   <ul>
	 *   <li><i>backColor</i>: background color.
	 *   </ul>
	 *  <li>If defined, the clip:
	 *   <ul>
	 *   <li><i>clipX</i>: X coordinate of the clip.
	 *   <li><i>clipY</i>: Y coordinate of clip.
	 *   <li><i>clipW</i>: width of the clip.
	 *   <li><i>clipH</i>: height of the clip.
	 *   </ul>
	 *  <li><i>dist1pixel</i>: the distance in world coordinates equivalent to 1 pixel in the view.
	 *  <li><i>dist3pixel</i>: the distance in world coordinates equivalent to 3 pixels in the view.
	 *  <li><i>distanceUnits</i>: the distance measurement unit.
	 *  <li>If defined, the extent:
	 *   <ul>
	 *   <li><i>extentX</i>: X coordinate of the extent.
	 *   <li><i>extentY</i>: Y coordinate of the extent.
	 *   <li><i>extentW</i>: width of the extent.
	 *   <li><i>extentH</i>: height of the extent.
	 *   </ul>
	 *  <li><i>mapUnits</i>: the map measurement unit.
	 *  <li><i>offsetX</i>: X coordinate of the offset.
	 *  <li><i>offsetY</i>: Y coordinate of the offset.
	 *  <li>If defined, the projection:
	 *   <ul>
	 *   <li>If its defined, the projection:
	 *    <ul>
	 *     <li><i>proj</i>: the projection.</li>
	 *    </ul>
	 *   </ul>
	 *  <li><i>scale</i>: ratio between the size of <code>imageSize</code> and <code>extent</code>.
	 *  </ul>
	 * <li>Child branches:
	 *  <ul>
	 *  <li>XML entity of the internal {@link ExtentHistory ExtentHistory} .
	 *  </ul>
	 * </ul>
	 *
	 * @return the XML entity
	 *
	 * @see #createFromXML(XMLEntity)
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className",this.getClass().getName());

		if (adjustedExtent != null) {
			xml.putProperty("adjustedExtentX", adjustedExtent.getX());
			xml.putProperty("adjustedExtentY", adjustedExtent.getY());
			xml.putProperty("adjustedExtentW", adjustedExtent.getWidth());
			xml.putProperty("adjustedExtentH", adjustedExtent.getHeight());
		}

		if (backColor != null) {
			xml.putProperty("backColor", StringUtilities.color2String(backColor));
		}

		if (clip != null) {
			xml.putProperty("clipX", clip.getX());
			xml.putProperty("clipY", clip.getY());
			xml.putProperty("clipW", clip.getWidth());
			xml.putProperty("clipH", clip.getHeight());
		}

		xml.putProperty("dist1pixel", dist1pixel);
		xml.putProperty("dist3pixel", dist3pixel);
		xml.putProperty("distanceUnits", distanceUnits);

		if (extent != null) {
			xml.putProperty("extentX", extent.getX());
			xml.putProperty("extentY", extent.getY());
			xml.putProperty("extentW", extent.getWidth());
			xml.putProperty("extentH", extent.getHeight());
		}

		xml.addChild(extents.getXMLEntity());
		xml.putProperty("mapUnits", mapUnits);
		xml.putProperty("offsetX", offset.getX());
		xml.putProperty("offsetY", offset.getY());

		if (proj != null) {
			xml.putProperty("proj", proj.getAbrev());
		}

		xml.putProperty("scale", scale);

		return xml;
	}


	/**
	 * <p>Creates a new <code>ViewPort</code> from an XML entity.</p>
	 *
	 * @param xml an XML entity
	 *
	 * @return the new <code>ViewPort</code>
	 *
	 * @see #getXMLEntity()
	 */
	public static ViewPort createFromXML(XMLEntity xml) {
		ViewPort vp = new ViewPort(null);

		if (xml.contains("adjustedExtentX")) {
			vp.adjustedExtent = new Rectangle2D.Double(xml.getDoubleProperty(
						"adjustedExtentX"),
					xml.getDoubleProperty("adjustedExtentY"),
					xml.getDoubleProperty("adjustedExtentW"),
					xml.getDoubleProperty("adjustedExtentH"));
		}

		if (xml.contains("backColor")) {
			vp.setBackColor(StringUtilities.string2Color(xml.getStringProperty(
						"backColor")));
		}else {
			vp.setBackColor(Color.white);
		}

		if (xml.contains("clipX")) {
			vp.clip = new Rectangle2D.Double(xml.getDoubleProperty("clipX"),
					xml.getDoubleProperty("clipY"),
					xml.getDoubleProperty("clipW"),
					xml.getDoubleProperty("clipH"));
		}

		vp.setDist1pixel(xml.getDoubleProperty("dist1pixel"));
		vp.setDist3pixel(xml.getDoubleProperty("dist3pixel"));
		vp.setDistanceUnits(xml.getIntProperty("distanceUnits"));
		if (xml.contains("distanceArea")){
			vp.setDistanceArea(xml.getIntProperty("distanceArea"));
		}else{
			vp.setDistanceArea(xml.getIntProperty("distanceUnits"));
		}
		vp.extents = ExtentHistory.createFromXML(xml.getChild(0));

		if (xml.contains("extentX")) {
			double x=xml.getDoubleProperty("extentX");
			double y=xml.getDoubleProperty("extentY");
			try {
				vp.setEnvelope(geomManager.createEnvelope(x,y,
						x+xml.getDoubleProperty("extentW"),y+xml.getDoubleProperty("extentH"), SUBTYPES.GEOM2D));
			} catch (CreateEnvelopeException e) {
				e.printStackTrace();
				logger.error("Error setting the extent", e);
			}

			//Calcula la transformación afín
			vp.calculateAffineTransform();

			// Lanzamos los eventos de extent cambiado
			// vp.callExtentListeners(vp.adjustedExtent);
		}

		vp.setMapUnits(xml.getIntProperty("mapUnits"));
		vp.setOffset(new Point2D.Double(xml.getDoubleProperty("offsetX"),
				xml.getDoubleProperty("offsetY")));

		if (xml.contains("proj")) {
			vp.proj = CRSFactory.getCRS(xml.getStringProperty("proj"));
		}

		//vp.setScale(xml.getDoubleProperty("scale"));
		vp.refreshExtent();
		return vp;
	}

	/**
	 * <p>Fast clone implementation: creates and returns a clone of this view port using XML entities.</p>
	 * <p>Isn't a <i>deepclone</i> to avoid unnecessary memory consumption.</p>
	 *
	 * @return the new view port
	 *
	 * @see #createFromXML(XMLEntity)
	 */
	public ViewPort cloneViewPort() {
		return createFromXML(getXMLEntity());
	}

	/**
	 * <p>Returns a <code>String</code> representation of the main values of this view port: <code>{@linkplain #extent}</code>,
	 *  <code>{@linkplain #adjustedExtent}</code>, <code>{@linkplain #imageSize}</code>, <code>{@linkplain #scale}</code>, and <code>{@linkplain #trans}</code>.</p>
	 *
	 * @return a <code>string</code> representation of the main values of this view port
	 */
	public String toString() {

		String str;
		str = "Datos del viewPort:\nExtent=" + extent + "\nadjustedExtent=" +
			adjustedExtent + "\nimageSize=" + imageSize + "\nescale=" + scale +
			"\ntrans=" + trans;

		return str;
	}

	/**
	 * <p>Sets the position and size of the clipping rectangle.</p>
	 *
	 * @param rectView the clipping rectangle to set
	 */
	public void setClipRect(Rectangle2D rectView) {
		this.updateDrawVersion();
		cliprect=rectView;
	}

	/**
	 * <p>Converts and returns the {@link Rectangle2D Rectangle2D}, that is in <i>map
	 *  coordinates</i> to <i>screen coordinates</i> (pixels) using an <i>inverse transform</i> with
	 *  the transformation affine information in the {@link #trans #trans} attribute.</p>
	 *
	 * @param r the 2D rectangle in <i>map coordinates</i>
	 * @return 2D rectangle equivalent in <i>screen coordinates</i> (pixels)
	 *
	 * @see #toMapRectangle(Rectangle2D)
	 * @see #fromMapDistance(double)
	 * @see #fromMapPoint(Point2D)
	 */
	public Rectangle2D fromMapRectangle(Rectangle2D r) {
		Rectangle2D rect=new Rectangle2D.Double();
		Point2D p1=fromMapPoint((int)r.getX(),(int)r.getY());
		Point2D p2=fromMapPoint((int)r.getMaxX(),(int)r.getMaxY());
		rect.setFrameFromDiagonal(p1,p2);
		return rect;
	}

	/**
	 * <p>Recalculates the current <code>{@linkplain #extent}</code> using an scale. It's necessary execute {@linkplain #refreshExtent()} after.</p>
	 *
	 * @param s the scale to set
	 *
	 * @deprecated since 07/09/07, use {@linkplain MapContext#setScaleView(long)}
	 */
	public void setScale(long s){
		double x=extent.getX();
		double y=extent.getY();
		double escalaX = imageSize.width / extent.getWidth();
		double w=imageSize.width / s;
		double h=imageSize.height / s;
		double difw = escalaX/s;

		double x1 = (-x * difw) -
            x+
            extent.getWidth()/2;
        double y1 = (-y * difw) -
            y +
            extent.getHeight()/2;
        double w1=extent.getWidth()*difw;
        double h1=extent.getHeight()*difw;
		extent.setRect(-x1,-y1,w1,h1);
	}


	public long getDrawVersion() {
		return this.drawVersion;
	}

	protected void updateDrawVersion(){
		this.drawVersion++;
	}

}
