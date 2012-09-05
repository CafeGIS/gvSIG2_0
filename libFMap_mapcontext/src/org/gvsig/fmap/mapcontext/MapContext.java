/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
 *   Av. Blasco Ibï¿½ï¿½ez, 50
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

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;
import org.gvsig.compat.CompatLocator;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.events.ErrorEvent;
import org.gvsig.fmap.mapcontext.events.listeners.AtomicEventListener;
import org.gvsig.fmap.mapcontext.events.listeners.ErrorListener;
import org.gvsig.fmap.mapcontext.events.listeners.EventBuffer;
import org.gvsig.fmap.mapcontext.impl.InvalidMapContextDrawerClassException;
import org.gvsig.fmap.mapcontext.layers.*;
import org.gvsig.fmap.mapcontext.layers.operations.Classifiable;
import org.gvsig.fmap.mapcontext.layers.operations.SingleLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;
import org.gvsig.fmap.mapcontext.rendering.strategies.SelectedZoomVisitor;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.task.Cancellable;
import org.gvsig.tools.visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * <p>The <code>MapContext</code> class represents the model and a part of the control and view around graphical layers
 * used by {@link MapControl MapControl}.</p>
 *
 * <p>An instance of <code>MapContext</code> is made up with:
 * <ul>
 * <li>a hierarchy of {@link FLayers FLayers} nodes
 * <li>a {@link GraphicLayer GraphicLayer} layer
 * <li>a {@link ViewPort ViewPort}
 * <li>an {@link EventBuffer EventButter}
 * <li>some {@link com.iver.cit.gvsig.fmap.layers.LegendListener LegendListener}s
 * <li>some {@link LayerDrawingListener LayerDrawingListener}s
 * <li>some {@link LayerEventListener LayerEventListener}s
 * <li>some {@link ErrorListener ErrorListener}s
 * </ul>
 * </p>
 *
 * @author Fernando González Cortés
 */
public class MapContext implements Projected,IPersistence, Observer {
	/**
	 * <p>Defines the value which a unit of a distance measurement must be divided to obtain its equivalent <b>in meters</b>.</p>
	 *
	 * <p><b><i>Conversion values of distance measurements:</i></b>
	 * <ul>
	 *  <li><code>MapContext.CHANGEM[0]</code>: kilometer
	 *  <li><code>MapContext.CHANGEM[1]</code>: meter
	 *  <li><code>MapContext.CHANGEM[2]</code>: centimeter
	 *  <li><code>MapContext.CHANGEM[3]</code>: millimeter
	 *  <li><code>MapContext.CHANGEM[4]</code>: international statute mile
	 *  <li><code>MapContext.CHANGEM[5]</code>: yard
	 *  <li><code>MapContext.CHANGEM[6]</code>: foot
	 *  <li><code>MapContext.CHANGEM[7]</code>: inch
	 *  <li><code>MapContext.CHANGEM[8]</code>: grade
	 * </ul>
	 *
	 * <p><h3>Examples:</h3>
	 * <pre>1 international statute mile / MapContext.CHANGEM[4] = X meters</pre>
	 * <pre>1 kilometer / MapContext.CHANGEM[0] = X meters</pre>
	 * <pre>1 grade / MapContext.CHANGEM[8] = X meters</pre>
	 * </p>
	 *
	 * <p><h3>Grade conversion value: <code>MapContext.CHANGEM[8]</code></h3>
	 * The value of <code>MapContext.CHANGEM[8]</code> represents the meters of a straight line between two
	 *  points on the Earth surface that are 1 grade far each other of the center of the Earth. This value has been calculated using
	 *  a radius approximated of R<sub>Earth</sub>=6.37846082678100774672e6 meters, according these equations:
	 * <pre>D = 2 * (sin (1)) * R<sub>Earth</sub></pre>
	 * <pre>MapContext.CHANGEM[8] = 1 / D</pre>
	 * <h4>Explanation:</h4>
	 * We get an isosceles triangle with the center of the Earth and the 2 points on the surface. This triangle can be divided into
	 * two rectangle triangles. We know two values, the angle of 1 grade, that will be 0.50 grades in each triangle, and the Earth radius that
	 * is the hypotenuse. Then we apply trigonometry and get the distance <i>D</i> between both points on the Earth surface.</p>
	 * <p>Now we only must invert that value to obtain <code>MapContext.CHANGEM[8]</code>.</p>
	 *@deprecated use getDistanceTrans2Meter()
	 */
	public static final double[] CHANGEM = { 1000, 1, 0.01, 0.001, 1609.344,
			0.9144, 0.3048, 0.0254, 1/8.983152841195214E-6 };


	public static ArrayList AREANAMES=new ArrayList();
	public static ArrayList AREAABBR=new ArrayList();
	public static ArrayList AREATRANS2METER=new ArrayList();

	public static ArrayList DISTANCENAMES=new ArrayList();
	public static ArrayList DISTANCEABBR=new ArrayList();
	public static ArrayList DISTANCETRANS2METER=new ArrayList();

	static{
		MapContext.addDistanceUnit("Kilometros","Km",1000);
    	MapContext.addDistanceUnit("Metros","m",1);
    	MapContext.addDistanceUnit("Centimetros","cm",0.01);
    	MapContext.addDistanceUnit("Milimetros","mm",0.001);
    	MapContext.addDistanceUnit("Millas","mi",1609.344);
    	MapContext.addDistanceUnit("Yardas","Ya",0.9144);
    	MapContext.addDistanceUnit("Pies","ft",0.3048);
    	MapContext.addDistanceUnit("Pulgadas","inche",0.0254);
    	MapContext.addDistanceUnit("Grados","º",1/8.983152841195214E-6);

    	MapContext.addAreaUnit("Kilometros","Km",true,1000);
    	MapContext.addAreaUnit("Metros","m",true,1);
    	MapContext.addAreaUnit("Centimetros","cm",true,0.01);
    	MapContext.addAreaUnit("Milimetros","mm",true,0.001);
    	MapContext.addAreaUnit("Millas","mi",true,1609.344);
    	MapContext.addAreaUnit("Yardas","Ya",true,0.9144);
    	MapContext.addAreaUnit("Pies","ft",true,0.3048);
    	MapContext.addAreaUnit("Pulgadas","inche",true,0.0254);
    	MapContext.addAreaUnit("Grados","º",true,1/8.983152841195214E-6);


    }
	
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	
	private static final MapContextManager mapContextManager = MapContextLocator
            .getMapContextManager(); 
	
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	/**
	 * <p>Determines the number of frames.</p>
	 *
	 * <p>Number of updates per second that the timer will invoke repaint this component.</p>
	 */
    private static int drawFrameRate = 3;
    /**
	 * <p>Returns the draw frame rate.</p>
	 *
	 * <p>Draw frame rate is the number of repaints of this <code>MapControl</code> instance that timer invokes per second.</p>
	 *
	 * @return number of repaints of this <code>MapControl</code> instance that timer invokes per second
	 *
	 * @see #applyFrameRate()
	 * @see #setDrawFrameRate(int)
	 */
	public static int getDrawFrameRate() {
		return drawFrameRate;
	}

	/**
	 * <p>Sets the draw frame rate.</p>
	 *
	 * <p>Draw frame rate is the number of repaints of this <code>MapControl</code> instance that timer invokes per second.</p>
	 *
	 * @param drawFrameRate number of repaints of this <code>MapControl</code> instance that timer invokes per second
	 *
	 * @see #applyFrameRate()
	 * @see #getDrawFrameRate()
	 */
	public static void setDrawFrameRate(int dFR) {
		drawFrameRate = dFR;
	}
	public static void addAreaUnit(String name, String abbr,boolean isLinear,double trans2meter){
		AREANAMES.add(name);
		String pow="";
		if (isLinear) {
			pow=String.valueOf((char)178);
		}
		AREAABBR.add(abbr+pow);
		AREATRANS2METER.add(new Double(trans2meter));
	}
	public static String[] getAreaNames(){
		return (String[])AREANAMES.toArray(new String[0]);
	}
	public static String[] getAreaAbbr(){
		return (String[])AREAABBR.toArray(new String[0]);
	}
	public static double[] getAreaTrans2Meter(){
		int size=AREATRANS2METER.size();
		double[] trans2meters=new double[size];
		for (int i = 0; i < size; i++) {
			trans2meters[i]=((Double)AREATRANS2METER.get(i)).doubleValue();
		}
		return trans2meters;
	}
	public static String getOfLinear(int i) {
		if (((String)AREAABBR.get(i)).toLowerCase().endsWith(String.valueOf((char)178))){
			return String.valueOf((char)178);
		}
		return "";
	}
	public static void addDistanceUnit(String name, String abbr,double trans2meter){
		DISTANCENAMES.add(name);
		DISTANCEABBR.add(abbr);
		DISTANCETRANS2METER.add(new Double(trans2meter));
	}
	public static String[] getDistanceNames(){
		return (String[])DISTANCENAMES.toArray(new String[0]);
	}
	public static String[] getDistanceAbbr(){
		return (String[])DISTANCEABBR.toArray(new String[0]);
	}
	public static double[] getDistanceTrans2Meter(){
		int size=DISTANCETRANS2METER.size();
		double[] trans2meters=new double[size];
		for (int i = 0; i < size; i++) {
			trans2meters[i]=((Double)DISTANCETRANS2METER.get(i)).doubleValue();
		}
		return trans2meters;
	}
	public static int getDistancePosition(String s){
		for (int i = 0; i < DISTANCENAMES.size(); i++) {
			if (DISTANCENAMES.get(i).equals(s)){
				return i;
			}
		}
		return 0;
	}

	/**
	 * <p>Defines the value which a unit of a distance measurement must be divided to obtain its equivalent <b>in centimeters</b>.</p>
	 *
	 * <p><b><i>Conversion values of distance measurements:</i></b>
	 * <ul>
	 *  <li><code>MapContext.CHANGE[0]</code>: kilometer
	 *  <li><code>MapContext.CHANGE[1]</code>: meter
	 *  <li><code>MapContext.CHANGE[2]</code>: centimeter
	 *  <li><code>MapContext.CHANGE[3]</code>: millimeter
	 *  <li><code>MapContext.CHANGE[4]</code>: international statute mile
	 *  <li><code>MapContext.CHANGE[5]</code>: yard
	 *  <li><code>MapContext.CHANGE[6]</code>: foot
	 *  <li><code>MapContext.CHANGE[7]</code>: inch
	 *  <li><code>MapContext.CHANGE[8]</code>: grade
	 * </ul>
	 *
	 * <p><h3>Examples:</h3>
	 * <pre>1 international statute mile / MapContext.CHANGE[4] = X centimeters</pre>
	 * <pre>1 kilometer / MapContext.CHANGE[0] = X centimeters</pre>
	 * <pre>1 grade / MapContext.CHANGE[8] = X centimeters</pre>
	 * </p>
	 *
	 * <p><h3>Grade conversion value: <code>MapContext.CHANGE[8]</code></h3>
	 * The value of <code>MapContext.CHANGE[8]</code> represents the centimeters of a straight line between two
	 *  points on the Earth surface that are 1 grade far each other of the center of the Earth. This value has been calculated using
	 *  a radius approximated of R<sub>Earth</sub>=6.37846082678100774672e6 meters, according these equations:
	 * <pre>D = 2 * (sin (1)) * R<sub>Earth</sub></pre>
	 * <pre>MapContext.CHANGE[8] = 1 / D</pre>
	 * <h4>Explanation:</h4>
	 * We get an isosceles triangle with the center of the Earth and the 2 points on the surface. This triangle can be divided into
	 * two rectangle triangles. We know two values, the angle of 1 grade, that will be 0.50 grades in each triangle, and the Earth radius that
	 * is the hypotenuse. Then we apply trigonometry and get the distance <i>D</i> between both points on the Earth surface.</p>
	 * <p>Now we only must invert that value to obtain <code>MapContext.CHANGE[8]</code>.</p>
	 * @deprecated use getDistanceTrans2Meter() * 100
	 */
	public static final double[] CHANGE = { 100000, 100, 1, 0.1, 160934.4,
			91.44, 30.48, 2.54, 1/8.983152841195214E-4 };

	/* Do not alter the order and the values of this array, if you need append values.*/
	/**
	 * <p>Gets the name of all distance measurements supported by <code>MapContext</code>.</p>
	 */
//	public static final String[] NAMES= {
//		Messages.getString("Kilometros"),
//		Messages.getString("Metros"),
//		Messages.getString("Centimetros"),
//		Messages.getString("Milimetros"),
//		Messages.getString("Millas"),
//		Messages.getString("Yardas"),
//		Messages.getString("Pies"),
//		Messages.getString("Pulgadas"),
//		Messages.getString("Grados"),
//	};

	public static final int EQUALS = 0;

	public static final int DISJOINT = 1;

	public static final int INTERSECTS = 2;

	public static final int TOUCHES = 3;

	public static final int CROSSES = 4;

	public static final int WITHIN = 5;

	public static final int CONTAINS = 6;

	public static final int OVERLAPS = 7;

	/**
	 * A hierarchy of {@link FLayers FLayers} nodes.
	 *
	 * @see #getLayers()
	 * @see #print(Graphics2D, double, PrintAttributes)
	 */
	protected FLayers layers;

	/**
	 * A layer with graphical items: geometries and symbols.
	 *
	 * @see #getGraphicsLayer()
	 * @see #setGraphicsLayer(GraphicLayer)
	 * @see #drawGraphics(BufferedImage, Graphics2D, Cancellable, double)
	 * @see #print(Graphics2D, double, PrintAttributes)
	 */
	private GraphicLayer tracLayer = new GraphicLayer();

	/**
	 * Information for draw layers in a view.
	 *
	 * @see #getViewPort()
	 * @see #setViewPort(ViewPort)
	 */
	private ViewPort viewPort;

	// private ArrayList invalidationListeners = new ArrayList();

	/**
	 * Array list with all {@link LegendListener LegendListener} registered to this map.
	 *
	 * @see #addLayerListener(LegendListener)
	 * @see #removeLayerListener(LegendListener)
	 * @see #callLegendChanged()
	 */
	private ArrayList legendListeners = new ArrayList();

	/**
	 * Array list with all {@link LayerDrawingListener LayerDrawingListener} registered to this map.
	 *
	 * @see #addLayerDrawingListener(LayerDrawingListener)
	 * @see #removeLayerDrawListener(LayerDrawingListener)
	 * @see #fireLayerDrawingEvent(LayerDrawEvent)
	 */
	private ArrayList layerDrawingListeners = new ArrayList();

	/**
	 * <p>Buffer that is used to store and eject events produced on this map:
	 * <ul>
	 *  <li>Layer collection events.
	 *  <li>View port events.
	 *  <li>Atomic events.
	 *  <li>Layer events.
	 *  <li>Legend events on a {@link Classificable Classificable} layer.
	 *  <li>Selection events on an {@link AlphanumericData AlphanumericData} data layer.
	 * </ul>
	 * </p>
	 *
	 * @see #addAtomicEventListener(AtomicEventListener)
	 * @see #removeAtomicEventListener(AtomicEventListener)
	 * @see #beginAtomicEvent()
	 * @see #endAtomicEvent()
	 */
	private EventBuffer eventBuffer = new EventBuffer();

	/**
	 * Event listener for the collection of layers of this map.
	 */
	private LayerEventListener layerEventListener = null;

	/**
	 * List with information of all errors produced on all layers.
	 *
	 * @see #addLayerError(String)
	 * @see #getLayersError()
	 * @see #clearErrors()
	 */
	private ArrayList layersError = new ArrayList();

	/**
	 * Array list with all {@link ErrorListener ErrorListener} registered to this map.
	 *
	 * @see #addErrorListener(ErrorListener)
	 * @see #removeErrorListener(LegendListener)
	 * @see #callNewErrorEvent(ErrorEvent)
	 * @see #reportDriverExceptions(String, List)
	 */
	private ArrayList errorListeners = new ArrayList();



	// public static ResourceBundle myResourceBundle =
	// ResourceBundle.getBundle("FMap");

	/**
	 * <p>Default <i>zoom in</i> factor.</p>
	 * <p>Doing a <i>zoom in</i> operation, decreases the focal distance and increases the eyesight angle to the surface. This allows view an smaller
	 * area but with the items bigger.</p>
	 */
	public static double ZOOMINFACTOR=2;

	/**
	 * <p>Default <i>zoom out</i> factor.</p>
	 * <p>Doing a <i>zoom out</i> operation, increases the focal distance and decreases the eyesight angle to the surface. This allows view a bigger
	 * area but with the items smaller.</p>
	 */
	public static double ZOOMOUTFACTOR=0.5;

	/**
	 * 	 * Draw version of the context. It's used for know when de componend has
	 * changed any visualization property
	 *
	 *  @see getDrawVersion
	 *  @see updateDrawVersion
	 */
	private long drawVersion= 0L;

	/**
	 * Object to Manage Draw of MapContext
	 */
	private MapContextDrawer mapContextDrawer= null;

	/**
	 * Object to Manage Draw of MapContext
	 */
	private Class mapContextDrawerClass = null;

	/**
	 * <p>Color used to represent the selections.</p>
	 */
	private static Color selectionColor = Color.YELLOW;
//	private Observable DefaultObservable defaultObservable=new DefaultObservable();

	private ArrayList snappers = new ArrayList();
	private ArrayList layersToSnap = new ArrayList();


	/**
	 * <p>Gets the color used to represent the selections.</p>
	 *
	 * @return color used to represent the selections
	 */
	public static Color getSelectionColor() {
		return selectionColor;
	}

	/**
	 * <p>Sets the color used to represent the selections.</p>
	 *
	 * @param selectionColor color used to represent the selections
	 */
	public static void setSelectionColor(Color selectionColor) {
		MapContext.selectionColor = selectionColor;
	}

	/**
	 * <p>Creates a new map context with the drawing information defined in the view port argument, and
	 *  without layers.</p>
	 *
	 * @param vp information for drawing the layers of this map in the available rectangular area according a projection
	 */
	public MapContext(ViewPort vp) {
		this.layers = new FLayers();//(this,null);
		this.layers.setMapContext(this);

		layerEventListener = new LayerEventListener();
		layers.addLayerCollectionListener(layerEventListener);
		layers.addLayerCollectionListener(eventBuffer);

		setViewPort(vp);

	}

	/**
	 * <p>Creates a new map context with the layers and the drawing information defined in the view port arguments.</p>
	 *
	 * @param fLayers the initial hierarchy of nodes of layers that this map will have
	 * @param vp information for drawing the layers of this map in the available rectangular area according a projection
	 */
	public MapContext(FLayers fLayers, ViewPort vp) {
		this.layers = fLayers;

		layerEventListener = new LayerEventListener();
		layers.addLayerCollectionListener(layerEventListener);
		layers.addLayerCollectionListener(eventBuffer);

		setViewPort(vp);
	}

	/**
	 * <p>Reports to all driver error listeners registered of a bundle of driver exceptions caused in the same map atomic transaction.</p>
	 *
	 * @param introductoryText introductory text specified by developer. If <code>null</code>, use ""
	 * @param driverExceptions list with a bundle of driver exceptions caught during an atomic event
	 *
	 * @see #addErrorListener(ErrorListener)
	 * @see #removeErrorListener(LegendListener)
	 * @see #callNewErrorEvent(ErrorEvent)
	 */
	public synchronized void reportDriverExceptions(String introductoryText,
													List driverExceptions){
		for (int i = 0; i < errorListeners.size(); i++) {
			((ErrorListener) errorListeners.get(i)).
				reportDriverExceptions(introductoryText, driverExceptions);
		}
	}

	/**
	 * <p>Adds the specified legend listener (if didn't exist) to receive legend events from this map.</p>
	 *
	 * @param listener the legend listener
	 *
	 * @see #removeLayerListener(LegendListener)
	 * @see #callLegendChanged()
	 */
	public void addLayerListener(LegendListener listener) {
		if (!legendListeners.contains(listener)){
			legendListeners.add(listener);
		}
	}
	// SUGERENCIA DE PABLO
	//	public void addLegendListener(LegendListener listener) {
	//		if (!legendListeners.contains(listener))
	//			legendListeners.add(listener);
	//	}

	/**
	 * <p>Adds the specified layer drawing listener to catch and handle drawing events from layers of this map.</p>
	 *
	 * @param listener the listener to add
	 *
	 * @see #removeLayerDrawListener(LayerDrawingListener)
	 * @see #fireLayerDrawingEvent(LayerDrawEvent)
	 */
	public void addLayerDrawingListener(LayerDrawingListener listener) {
		layerDrawingListeners.add(listener);
	}

	/**
	 * <p>Removes the specified layer drawing listener from this map.</p>
	 *
	 * @param listener the listener to remove
	 *
	 * @see #addLayerDrawingListener(LayerDrawingListener)
	 * @see #fireLayerDrawingEvent(LayerDrawEvent)
	 */
	public void removeLayerDrawListener(LayerDrawingListener listener) {
		layerDrawingListeners.remove(listener);
	}

	/**
	 * <p>Adds the specified error listener to receive error events from this map.</p>
	 *
	 * @param listener the listener to add
	 *
	 * @see #removeErrorListener(LegendListener)
	 * @see #callNewErrorEvent(ErrorEvent)
	 * @see #reportDriverExceptions(String, List)
	 */
	public void addErrorListener(ErrorListener listener) {
		errorListeners.add(listener);
	}

	/**
	 * <p>Removes the specified error listener from this map.</p>
	 *
	 * @param listener the listener to remove
	 *
	 * @see #addErrorListener(ErrorListener)
	 * @see #callNewErrorEvent(ErrorEvent)
	 * @see #reportDriverExceptions(String, List)
	 */
	public void removeErrorListener(LegendListener listener) {
		legendListeners.remove(listener);
	}

	// SUGERENCIA DE PABLO:
	//public void removeErrorListener(ErrorListener listener) {
	//	errorListeners.remove(listener);
	//}

	/**
	 * <p>Notifies to all legend listeners registered, that one legend has changed.</p>
	 * <p>This method must be called only if it's wanted to reflect a legend change.</p>
	 *
	 * @see #addLayerListener(LegendListener)
	 * @see #removeLayerListener(LegendListener)
	 */
	public synchronized void callLegendChanged() {
		for (int i = 0; i < legendListeners.size(); i++) {
			((LegendListener) legendListeners.get(i)).legendChanged(null);
		}
		// getLayers().moveTo(0,0);
	}

	/**
	 * <p>Fires a layer drawing event to all {@link LayerDrawingListener LayerDrawingListener} listeners registered,
	 *  distinguishing the kind of event.</p>
	 *
	 * @param e the event
	 *
	 * @see #addLayerDrawingListener(LayerDrawingListener)
	 * @see #removeLayerDrawListener(LayerDrawingListener)
	 */
	public synchronized void fireLayerDrawingEvent(LayerDrawEvent e) {
		for (int i = 0; i < layerDrawingListeners.size(); i++)
		{
			LayerDrawingListener listener = (LayerDrawingListener) layerDrawingListeners.get(i);
			switch (e.getEventType())
			{
				case LayerDrawEvent.LAYER_BEFORE_DRAW:
					listener.beforeLayerDraw(e);
					break;
				case LayerDrawEvent.LAYER_AFTER_DRAW:
					listener.afterLayerDraw(e);
					break;
				case LayerDrawEvent.GRAPHICLAYER_BEFORE_DRAW:
					listener.beforeGraphicLayerDraw(e);
					break;
				case LayerDrawEvent.GRAPHICLAYER_AFTER_DRAW:
					listener.afterLayerGraphicDraw(e);
					break;
			}
		}
		// getLayers().moveTo(0,0);
	}

	/**
	 * <p>Notifies to all error listeners registered, that one error has been produced.</p>
	 *
	 * @param e the event with information of the error
	 *
	 * @see #addErrorListener(ErrorListener)
	 * @see #removeErrorListener(LegendListener)
	 * @see #reportDriverExceptions(String, List)
	 */
	public synchronized void callNewErrorEvent(ErrorEvent e) {
		for (int i = 0; i < errorListeners.size(); i++) {
			((ErrorListener) errorListeners.get(i)).errorThrown(e);
		}
		errorListeners.clear();
		// getLayers().moveTo(0,0);
	}

	/**
	 * <p>Removes the specified layer listener from this map.</p>
	 *
	 * @param listener the listener to remove
	 *
	 * @see #addLayerListener(LegendListener)
	 * @see #callLegendChanged()
	 */
	public void removeLayerListener(LegendListener listener) {
		legendListeners.remove(listener);
	}

	// SUGERENCIA DE PABLO:
	// public void removeLegendListener(LegendListener listener) {
	// 	legendListeners.remove(listener);
	// }

	/**
	 * <p>Returns the hierarchy of {@link FLayers FLayers} nodes stored in this map.</p>
	 *
	 * @return the hierarchy of nodes of layers stored in this map
	 */
	public FLayers getLayers() {
		return layers;
	}

	/**
	 * <p>Draws the visible layers of this map according its view port, on the image parameter.</p>
	 *
	 * @param b image with an accessible buffer of image data
	 */
	public void drawLabels(BufferedImage b) {
	}

	/**
	 * @see #redraw()
	 */
	public void invalidate() {
		if (getLayers().getLayersCount() > 0) {
			getLayers().moveTo(0, 0);
		}
	}

    /**
     * <p>
     * Prints the layers of this map using the {@link Graphics2D Graphics2D}
     * argument, that usually is the {@link Graphics Graphics} of the printer.
     * </p>
     * 
     * @param g
     *            for rendering 2-dimensional shapes, text and images on the
     *            Java(tm) platform
     * @param scale
     *            the scale of the view. Must be between
     *            {@linkplain FLayer#getMinScale()} and
     *            {@linkplain FLayer#getMaxScale()}.
     * @param properties
     *            a set with the settings to be applied to a whole print job and
     *            to all the documents in the print job
     * @throws MapContextException
     *             if there is an error getting the instance of MapContextDrawer
     * 
     * @throws ReadDriverException
     *             if fails reading with driver.
     * 
     * @see FLayers#print(Graphics2D, ViewPort, Cancellable, double,
     *      PrintAttributes)
     * @see GraphicLayer#draw(BufferedImage, Graphics2D, ViewPort, Cancellable,
     *      double)
     */
	public void print(Graphics2D g, double scale,
			PrintAttributes properties) throws ReadException,
            MapContextException {
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHints(renderHints);

		Cancellable cancel = new Cancellable() {
			public boolean isCanceled() {
				return false;
			}

			public void setCanceled(boolean canceled) {
				// No queremos que se pueda cancelar la impresiï¿½n.

			}
		};
		this.getMapContextDrawer().print(this.layers, g, cancel, scale,properties);
		tracLayer.draw(null, g, viewPort, cancel, scale);
	}

	/**
	 * <p>Returns a new <code>MapContext</code> instance with the information of the <code>vp</code> argument, and the layers of this map.</p>
	 *
	 * @param vp information for drawing the layers of this map in the available rectangular area according a projection
	 *
	 * @return a new <code>MapContext</code> instance projected by <code>vp</code>
	 */
	public MapContext createNewFMap(ViewPort vp) {
		MapContext ret = new MapContext(vp);
		ret.layers = this.layers;

		return ret;
	}

	/**
	 * <p>Creates a new independent <code>MapContext</code> instance, that has a clone of the layers and the view port of this one.</p>
	 * <p>The new map will have the same data source drivers to avoid waste memory, and work faster.</p>
	 *
	 * @return the new <code>MapContext</code> instance
	 *
	 * @throws XMLException if fails cloning the view port or a layer
	 *
	 * @see FLayer#cloneLayer()
	 * @see ViewPort#cloneViewPort()
	 */
	public MapContext cloneFMap() {
		ViewPort vp = getViewPort().cloneViewPort();
		FLayers antLayers = getLayers();
		MapContext ret = new MapContext(vp);
		FLayers aux = new FLayers();//(ret,null);
		aux.setMapContext(ret);
		for (int i=0; i < antLayers.getLayersCount(); i++)
		{
			FLayer lyr = antLayers.getLayer(i);
			try {
				aux.addLayer(lyr.cloneLayer());
			} catch (Exception e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
				// FIXME
				throw new RuntimeException(e);
			}
		}
		ret.layers = aux;
		return ret;

//		return createFromXML(getXMLEntity());

	}

	/**
	 * Like {@linkplain #cloneFMap()}, but now doesn't clone the layers, rather copies them.
	 *
	 * @return the new map
	 */
	public MapContext cloneToDraw() {
		ViewPort vp = getViewPort().cloneViewPort();
		MapContext mapContext=new MapContext(getLayers(),vp);
		return mapContext;
	}

	/**
	 * Añade la capa que se pasa como parámetro al nodo que se pasa como
	 * parametro y lanza ProjectionMismatchException si no están todas las capas
	 * de este FMap en la misma proyección. Lanza un ChildNotAllowedException si
	 * la capa no es un FLayers y no permite hijos
	 *
	 * @param vectorial
	 *            DOCUMENT ME!
	 */

	/*
	 * public void addLayer(LayerPath parent, FLayer layer) throws
	 * ProjectionMismatchException, ChildrenNotAllowedException {
	 * layers.addLayer(parent, layer); } public void removeLayer(LayerPath
	 * parent)throws ChildrenNotAllowedException{ layers.removeLayer(parent); }
	 */

	/**
	 * <p>Adds a layer to the group of layers that are at a upper level in the tree.</p>
	 *
	 * @param vectorial the layer to add
	 */
	public void addToTrackLayer(FLayer vectorial) {
	}

	/**
	 * <p>Returns the scale of the view in the screen.</p>
	 *
	 * @return one of this values:
	 * <ul>
	 * <li>the scale of the adjusted extent scale of the view in the screen
	 * <li><code>-1</code> if there is no image
	 * <li><code>0</code> if there is no extent defined for the image
	 * </ul>
	 *
	 * @see #setScaleView(long)
	 * @see ViewPort#getAdjustedExtent()
	 * @see IProjection#getScale(double, double, double, double)
	 */
	public long getScaleView() {
		double dpi = getScreenDPI();
		IProjection proj = viewPort.getProjection();

		if (viewPort.getImageSize() == null) {
			return -1;
		}

		if (viewPort.getAdjustedExtent() == null) {
			return 0;
		}
		double[] trans2Meter=getDistanceTrans2Meter();
		if (proj == null) {
			double w = ((viewPort.getImageSize().getWidth() / dpi) * 2.54);
			return (long) (viewPort.getAdjustedExtent().getLength(0) / w * trans2Meter[getViewPort()
					.getMapUnits()]);
		}

		return Math.round(proj.getScale((viewPort.getAdjustedExtent().getMinimum(0)*trans2Meter[getViewPort().getMapUnits()]),
				(viewPort.getAdjustedExtent().getMaximum(0)*trans2Meter[getViewPort().getMapUnits()]), viewPort.getImageSize()
						.getWidth(), dpi));

	}

	/**
	 * <p>Sets the new extent of the view, calculated using the scale argument.</p>
	 * <p>Doesn't updates the scale if there isn't information about the dimension of the image or the
	 *  adjusted extent.</p>
	 *
	 * @param scale the new scale for the view
	 *
	 * @see ViewPort#setProjection(IProjection)
	 * @see #getScaleView()
	 */
	public void setScaleView(long scale) {
		double dpi = getScreenDPI();
		if (viewPort.getImageSize() == null) {
			return;
		}
		IProjection proj = viewPort.getProjection();
		if (viewPort.getAdjustedExtent() == null) {
			return;
		}
		double[] trans2Meter=getDistanceTrans2Meter();
		Envelope env=viewPort.getAdjustedExtent();
		Rectangle2D r=new Rectangle2D.Double(env.getMinimum(0),env.getMinimum(1),env.getLength(0),env.getLength(1));
		Rectangle2D rec=proj.getExtent(r,scale,viewPort.getImageWidth(),viewPort.getImageHeight(),100*getDistanceTrans2Meter()[getViewPort().getMapUnits()],trans2Meter[getViewPort().getDistanceUnits()],dpi);
		try {
			getViewPort().setEnvelope(geomManager.createEnvelope(rec.getX(),rec.getY(),rec.getMaxX(),rec.getMaxY(), SUBTYPES.GEOM2D));
		} catch (CreateEnvelopeException e) {
			logger.error("Error seting the bounding box");
		}
	}

	/**
	 * <p>Returns the screen resolution (Dots Per Inch) as it was defined by the user's preference, or
	 * by default as it is defined in the default Toolkit.</p>
	 *
	 * @return double with the screen's dpi
	 */
	public static double getScreenDPI() {
		return CompatLocator.getGraphicsUtils().getScreenDPI();
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#setVectorial(com.iver.cit.gvsig.fmap.VectorialAdapter)
	 */
//	public void setVectorial(VectorialAdapter v) {
//	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#process(com.iver.cit.gvsig.fmap.FeatureSelectorVisitor)
	 */
	public void process(Visitor visitor) {
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#processSelected(com.iver.cit.gvsig.fmap.FeatureVisitor)
	 */
	public void processSelected(Visitor visitor) {
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#select(com.iver.cit.gvsig.fmap.FeatureSelectorVisitor,
	 *      VectorialSubSet)
	 */
	public void select(Visitor visitor) {
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#selectFromSelection()
	 */
	public void selectFromSelection() {
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#createIndex()
	 */
	public void createIndex() {
	}

	/**
	 * @see org.cresques.geo.Projected#getProjection()
	 *
	 * @see ViewPort#getProjection()
	 * @see #setProjection(IProjection)
	 * @see #reProject(ICoordTrans)
	 */
	public IProjection getProjection() {
		return getViewPort().getProjection();
	}

	/**
	 * <p>Sets the new projection.</p>
	 *
	 * @param proj the new projection
	 *
	 * @see #getProjection()
	 * @see ViewPort#setProjection(IProjection)
	 * @see #reProject(ICoordTrans)
	 */
	public void setProjection(IProjection proj) {
		if (getViewPort() != null) {
			getViewPort().setProjection(proj);
		}
	}

	/**
	 * @see org.cresques.geo.Projected#reProject(org.cresques.cts.ICoordTrans)
	 */
	public void reProject(ICoordTrans arg0) {
		// TODO implementar reprojecciï¿½n (lo que sea eso)
	}

	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#getSelectionBounds()
	 *
	 * @see SelectedZoomVisitor#getSelectBound()
	 */
	public Envelope getSelectionBounds() throws BaseException {
		SelectedZoomVisitor visitor = new SelectedZoomVisitor();

		layers.accept(visitor);

		return visitor.getSelectBound();
	}

    /**
     * <p>
     * Draws this map if its {@link ViewPort ViewPort} has an extent defined:<br>
     * <ol>
     * <li>Selects only the layers that have to be drawn:
     * {@linkplain #prepareDrawing(BufferedImage, Graphics2D, double)}.
     * <li>Sets quality: antialiasing by text and images, and quality rendering.
     * <li>Draws the layers.
     * <li>Fires a <code>LayerDrawEvent.GRAPHICLAYER_BEFORE_DRAW</code>.
     * <li>Draws the graphic layer.
     * <li>Fires a <code>LayerDrawEvent.GRAPHICLAYER_AFTER_DRAW</code>.
     * <li>Invokes the garbage collector and memory clean.
     * </ol>
     * </p>
     * 
     * @param image
     *            buffer used sometimes instead <code>g</code> to accelerate the
     *            draw. For example, if two points are as closed that can't be
     *            distinguished, draws only one.
     * @param g
     *            for rendering 2-dimensional shapes, text and images on the
     *            Java(tm) platform
     * @param cancel
     *            shared object that determines if this layer can continue being
     *            drawn
     * @param scale
     *            the scale of the view. Must be between
     *            {@linkplain FLayer#getMinScale()} and
     *            {@linkplain FLayer#getMaxScale()}.
     * @throws MapContextException
     *             if there is an error getting the instance of MapContextDrawer
     * @throws ReadDriverException
     *             if fails reading with the driver.
     */
	public void draw(BufferedImage image, Graphics2D g, Cancellable cancel,
			double scale) throws ReadException, MapContextException {
		if (viewPort.getExtent() == null) {
			// System.err.println("viewPort.getExtent() = null");
			return;
		}
		System.out.println("Viewport despues: " + viewPort.toString());
		/*
		 * if ((viewPort.getImageWidth() <=0) || (viewPort.getImageHeight() <=
		 * 0)) { return; }
		 */

//		prepareDrawing(image, g, scale);

		// Mï¿½s cï¿½lidad al texto
		RenderingHints renderHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		renderHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHints(renderHints);

		long t1 = System.currentTimeMillis();
//		layers.draw(image, g, viewPort, cancel, scale);

		this.getMapContextDrawer().draw(this.layers, image, g, cancel, scale);

		LayerDrawEvent beforeTracLayerEvent = new LayerDrawEvent(tracLayer,
				g, viewPort, LayerDrawEvent.GRAPHICLAYER_BEFORE_DRAW);
		fireLayerDrawingEvent(beforeTracLayerEvent);
		tracLayer.draw(image, g, viewPort, cancel, scale);
		LayerDrawEvent afterTracLayerEvent = new LayerDrawEvent(tracLayer,
				g, viewPort, LayerDrawEvent.GRAPHICLAYER_AFTER_DRAW);
		fireLayerDrawingEvent(afterTracLayerEvent);

		//layers.setDirty(false);
		long t2 = System.currentTimeMillis();
		System.err.println("Tiempo de dibujado:" + (t2 - t1) +
				" mseg. Memoria libre:" + Runtime.getRuntime().freeMemory() / 1024  + " KB");
		/*
		 * g.setColor(Color.BLUE); GeneralPath shpR = new
		 * GeneralPath(viewPort.getExtent());
		 * shpR.transform(viewPort.getAffineTransform()); g.draw(shpR);
		 */
		System.gc();
	}

	/**
	 * <p>Draws only the internal graphic layer using the information of the {@link ViewPort ViewPort} of this map.</p>
	 *
	 * @param image image used to accelerate the screen draw
	 * @param g for rendering 2-dimensional shapes, text and images on the Java(tm) platform
	 * @param cancel shared object that determines if this layer can continue being drawn
	 * @param scale value that represents the scale
	 * @throws ReadDriverException if fails reading with the driver.
	 *
	 * @see GraphicLayer#draw(BufferedImage, Graphics2D, ViewPort, Cancellable, double)
	 */
	public void drawGraphics(BufferedImage image, Graphics2D g,
			Cancellable cancel, double scale) throws ReadException {
		if (viewPort == null) {
			return;
		}
		tracLayer.draw(image, g, viewPort, cancel, scale);
	}

    /**
     * <p>
     * Like
     * {@linkplain MapContext#draw(BufferedImage, Graphics2D, Cancellable, double)}
     * , but creating the task as cancellable.
     * </p>
     * 
     * @param image
     *            buffer used sometimes instead <code>g</code> to accelerate the
     *            draw. For example, if two points are as closed that can't be
     *            distinguished, draws only one.
     * @param g
     *            for rendering 2-dimensional shapes, text and images on the
     *            Java(tm) platform
     * @param scale
     *            the scale of the view. Must be between
     *            {@linkplain FLayer#getMinScale()} and
     *            {@linkplain FLayer#getMaxScale()}.
     * @throws MapContextException
     *             if there is an error getting the instance of MapContextDrawer
     * 
     * @throws ReadDriverException
     *             if the driver fails reading.
     * 
     * @see #draw(BufferedImage, Graphics2D, Cancellable, double)
     */
	public void draw(BufferedImage image, Graphics2D g, double scale)
			throws ReadException, MapContextException {
		//layers.setDirty(true);
		draw(image, g, new Cancellable() {
			/**
			 * @see com.iver.utiles.swing.threads.Cancellable#isCanceled()
			 */
			public boolean isCanceled() {
				return false;
			}

			public void setCanceled(boolean canceled) {
				// TODO Auto-generated method stub

			}
		}, scale);
	}

	/**
	 * <p>Gets the {@link ViewPort ViewPort} associated to this map.</p>
	 *
	 * @return the view port
	 *
	 * @see #setViewPort(ViewPort)
	 */
	public ViewPort getViewPort() {
		return viewPort;
	}

	/**
	 * <p>Sets a {@link ViewPort ViewPort} with the drawing information
	 *  of this map.</p>
	 * <p>If there was a previous view port, removes its {@link EventBuffer EventBuffer} and
	 *  adds the new one.</p>
	 *
	 * @param viewPort the viewPort
	 *
	 * @see #getViewPort()
	 */
	public void setViewPort(ViewPort viewPort) {
		if (this.viewPort != null) {
			this.viewPort.removeViewPortListener(eventBuffer);
		}

		if (this.mapContextDrawer != null){
			this.mapContextDrawer.setViewPort(viewPort);
		}

		this.viewPort = viewPort;
		if (viewPort != null) {
			viewPort.addViewPortListener(eventBuffer);
		}
	}

	/**
	 * <p>Sets the given extent to the {@link ViewPort ViewPort} and updates the view with the new zoom.</p>
	 *
	 * @param extent the extent of the new zoom
	 */
	public void zoomToEnvelope(Envelope extent) {
		if (extent!=null) {
			getViewPort().setEnvelope(extent);
		}
	}

	/**
	 * <p>Returns the union of all extents of all layers of this map.</p>
	 *
	 * @return full extent of layers of this map
	 * @throws ReadDriverException if the driver fails reading.
	 *
	 * @see FLayers#getFullEnvelope()
	 */
	public Envelope getFullEnvelope() throws ReadException {
		return layers.getFullEnvelope();
	}

	/**
	 * <p>Returns an XML entity with the name of this class as a property, and two children branches:<br>
	 * <ul>
	 * <li>XML entity of the internal {@link ViewPort ViewPort}.
	 * <li>XML entity of the internal {@link FLayers FLayers}.
	 * </ul>
	 *
	 * @return XMLEntity the XML entity
	 * @throws XMLException
	 * @throws XMLException if there is any error creating the XML from the map.
	 *
	 * @see #createFromXML(XMLEntity)
	 * @see ViewPort#getXMLEntity()
	 * @see FLayers#getXMLEntity()
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClass().getName());
		xml.addChild(viewPort.getXMLEntity());
		xml.addChild(layers.getXMLEntity());

		return xml;
	}

	/**
	 * <p>Creates a new <code>MapContext</code> instance from an XML entity, with
	 *  with the data of the {@link ViewPort ViewPort} and
	 *  {@link FLayers FLayers}.</p>
	 *
	 * @param xml an XML entity
	 *
	 * @return the new <code>MapContext</code> instance
	 *
	 * @throws XMLException if there is any error creating the map from the XML.
	 *
	 * @see #getXMLEntity()
	 * @see ViewPort#createFromXML(XMLEntity)
	 * @see FLayers#setXMLEntity(XMLEntity)
	 */
	public static MapContext createFromXML(XMLEntity xml) throws XMLException {
		ViewPort vp = ViewPort.createFromXML(xml.getChild(0));
		MapContext fmap = new MapContext(vp);
		fmap.layers.setXMLEntity(xml.getChild(1));
		fmap.layers.setName("root layer");
		return fmap;
	}

	/**
	 * <p>Adds a listener of atomic events to the internal {@link EventBuffer EventBuffer}.</p>
	 *
	 * @param listener the new listener
	 *
	 * @return <code>true</code> if has added the listener successfully
	 *
	 * @see #removeAtomicEventListener(AtomicEventListener)
	 * @see EventBuffer#addAtomicEventListener(AtomicEventListener)
	 */
	public boolean addAtomicEventListener(AtomicEventListener listener) {
		return eventBuffer.addAtomicEventListener(listener);
	}

	/**
	 * <p>Removes a listener of atomic events from the internal {@link EventBuffer EventBuffer}.</p>
	 *
	 * @param listener the listener to remove
	 *
     * @return <tt>true</tt> if the list contained the specified element
	 *
	 * @see #addAtomicEventListener(AtomicEventListener)
	 * @see EventBuffer#removeAtomicEventListener(AtomicEventListener)
	 */
	public boolean removeAtomicEventListener(AtomicEventListener listener) {
		return eventBuffer.removeAtomicEventListener(listener);
	}

	/**
	 * @see EventBuffer#beginAtomicEvent()
	 *
	 * @see #endAtomicEvent()
	 */
	public void beginAtomicEvent() {
		eventBuffer.beginAtomicEvent();
	}

	/**
	 * @see EventBuffer#endAtomicEvent()
	 *
	 * @see #beginAtomicEvent()
	 */
	public void endAtomicEvent() {
		eventBuffer.endAtomicEvent();
	}

	/**
	 * <p>The class <code>LayerEventListener</code> implements the methods of {@link LayerCollectionListener LayerCollectionListener}
	 *  that handles the "layer added" or "layer removed" events in a map.</p>
	 * <p>Is designed as a listener for all layers in a {@link MapContext MapContext}.</p>
	 *
	 * @author Fernando González Cortés
	 */
	public class LayerEventListener implements LayerCollectionListener {
		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdded(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
		 */
		public void layerAdded(LayerCollectionEvent e) {
			// Si es la primera capa, fijamos su extent al ViewPort
			// if (getLayers().getLayersCount() == 1) {
			if (getViewPort().getExtent() == null) {
				FLayer lyr = e.getAffectedLayer();
				if (lyr.isAvailable()) {
					try {
						getViewPort().setEnvelope(lyr.getFullEnvelope());
					} catch (ReadException e1) {
						e1.printStackTrace();
					}
				}
			}

			// Registramos al FMap como listener del legend de las capas
			FLayer lyr = e.getAffectedLayer();
			selectionListener(lyr);
			if (lyr instanceof SingleLayer){
				if (((SingleLayer) lyr).getDataStore() != null) {
					((SingleLayer) lyr).getDataStore().addObserver(
							MapContext.this);
				}
			}
		}

		/**
		 * <p>Registers an event buffer as a listener for all layers as argument.</p>
		 *
		 * <p>Each {@link FLayer FLayer} of this map must have an event buffer for all kind
		 * of specific listeners of that layer. This method distinguish between {@link Classifiable Classifiable},
		 * {@link AlphanumericData AlphanumericData}, and {@link FLayers FLayers} layers, and for each one,
		 * registers, for their specific listeners, the <code>eventBuffer</code> as a listener.</p>
		 *
		 * @param the layer or layers
		 */
		private void selectionListener(FLayer lyr){
			lyr.addLayerListener(eventBuffer);

			if (lyr instanceof Classifiable) {
				Classifiable c = (Classifiable) lyr;
				c.addLegendListener(eventBuffer);
			}

//			if (lyr instanceof AlphanumericData) {
//				Selectable s=null;
//				try {
//					s = ((AlphanumericData) lyr).getRecordset();
//					if (s!=null) {
//						s.addSelectionListener(eventBuffer);
//					}
//				} catch (ReadException e1) {
//					e1.printStackTrace();
//				}
//
//			}
			if (lyr instanceof FLayers){
				FLayers lyrs=(FLayers)lyr;
				for(int i=0;i<lyrs.getLayersCount();i++){
					selectionListener(lyrs.getLayer(i));
				}
			}

		}
		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoved(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
		 */
		public void layerMoved(LayerPositionEvent e) {
		}

		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoved(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
		 */
		public void layerRemoved(LayerCollectionEvent e) {
			FLayer lyr = e.getAffectedLayer();

			lyr.removeLayerListener(eventBuffer);

			if (lyr instanceof Classifiable) {
				Classifiable c = (Classifiable) lyr;
				c.removeLegendListener(eventBuffer);
			}

			if (lyr instanceof SingleLayer && ((SingleLayer) lyr).getDataStore()!=null) {
				((SingleLayer) lyr).getDataStore().deleteObserver(
						MapContext.this);
			}

//			if (lyr instanceof FLyrVect ) {
//				Selectable s = (Selectable) lyr;
//				s.addSelectionListener(eventBuffer);
//			}
		}

		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdding(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
		 */
		public void layerAdding(LayerCollectionEvent e)
				throws CancelationException {
		}

		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoving(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
		 */
		public void layerMoving(LayerPositionEvent e)
				throws CancelationException {
		}

		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoving(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
		 */
		public void layerRemoving(LayerCollectionEvent e)
				throws CancelationException {
		}


		/*
		 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#visibilityChanged(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
		 */
		public void visibilityChanged(LayerCollectionEvent e)
				throws CancelationException {
		}
	}

	/**
	 * <p>Adds the {@link LayerEventListener LayerEventListener} of this map to the
	 *  collection of layers argument.</p>
	 *
	 * @param a collection of layers
	 */
	public void addAsCollectionListener(FLayers layers2) {
		layers2.addLayerCollectionListener(layerEventListener);
	}

	/**
	 * <p>Returns the internal {@link GraphicLayer GraphicLayer}.</p>
	 *
	 * @return the graphic layer of this map
	 *
	 * @see #setGraphicsLayer(GraphicLayer)
	 */
	public GraphicLayer getGraphicsLayer() {
		return tracLayer;
	}

	/**
	 * <p>Sets a new {@link GraphicLayer GraphicLayer} to this map.</p>
	 *
	 * @param graphicLayer the new graphic layer
	 *
	 * @see #getGraphicsLayer()
	 */
	public void setGraphicsLayer(GraphicLayer graphicLayer) {
		tracLayer = graphicLayer;
	}

	/**
	 * <p>Indicates whether some other object is "equal to" this map.</p>
	 * <p>Returns <code>true</code> if success one of this options:
	 * <ul>
	 * <li>Both objects are equal according to {@linkplain Object#equals(Object)}.
	 * <li>Both maps have the same layers.
	 * <li>Both maps have the same number of layers and with the same name.
	 * </ul>
	 * </p>
	 *
	 * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the <code>arg0</code> argument;  otherwise <code>false</code>.
	 *
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof MapContext)) {
			return false;
		}
		MapContext map = (MapContext) arg0;
		if (super.equals(arg0)) {
			return true;
		}
		if (getLayers() == map.getLayers()) {
			return true;
		}
		boolean isEqual = true;
		if (map.getLayers().getLayersCount() == getLayers().getLayersCount()) {
			for (int i = 0; i < getLayers().getLayersCount(); i++) {

				if (!getLayers().getLayer(i).getName().equals(
						map.getLayers().getLayer(i).getName())) {
					isEqual = false;
				}

			}
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * <p>Registers the message of an error associated to this map.</p>
	 *
	 * @param stringProperty the error message
	 *
	 * @see #getLayersError()
	 * @see #clearErrors()
	 */
	public void addLayerError(String stringProperty) {
		layersError.add(stringProperty);
	}

	/**
	 * <p>Gets the list with all error messages registered to this map.</p>
	 *
	 * @return the list of errors registered to this map
	 *
	 * @see #addLayerError(String)
	 * @see #clearErrors()
	 */
	public ArrayList getLayersError() {
		return layersError;
	}

	/**
	 * <p>Removes all error messages associated to this map.</p>
	 *
	 * @see #addLayerError(String)
	 * @see #getLayersError()
	 */
	public void clearErrors() {
		layersError.clear();
	}

	/**
	 * <p>Creates and returns a new group of layers that belongs to this <code>MapContext</code>.</p>
	 *
	 * @param parent layer node in this <code>MapContexte</code> that will be the parent of the new node
	 * @return the new layer node
	 */
	public FLayers getNewGroupLayer(FLayers parent) {
		FLayers group1 = new FLayers();//(this,parent);
		group1.setMapContext(this);
		group1.setParentLayer(parent);
	    return group1;
	}

	public String getClassName() {
		return null;
	}

	public void setXMLEntity(XMLEntity arg0) {
		// TODO Auto-generated method stub

	}

	public ArrayList getSnappers() {
		return snappers;
	}
	public void setSnappers(ArrayList snappers){
		this.snappers=snappers;
	}


	public ArrayList getLayersToSnap() {
		return layersToSnap;
	}

	public void setLayersToSnap(ArrayList layersToSnap) {
		this.layersToSnap = layersToSnap;

	}

	public void update(Observable observable, Object notification) {
		// TODO REVISAR ESTO!!!
		String ntype=null;
		if (notification instanceof FeatureStoreNotification) {
			FeatureStoreNotification fsNotification = (FeatureStoreNotification) notification;
			ntype =fsNotification.getType();
			if (
					ntype.equals(FeatureStoreNotification.LOAD_FINISHED)||
					ntype.equals(FeatureStoreNotification.SELECTION_CHANGE)
			) {
				getLayers().moveTo(0, 0);
			}
		}
	}

	public long getDrawVersion() {
		return this.drawVersion;
	}

	protected void updateDrawVersion(){
		this.drawVersion++;
	}

	public MapContextDrawer getMapContextDrawer() throws ReadException,
            MapContextException {
		if (this.mapContextDrawer == null){
		    if (mapContextDrawerClass == null) {
                this.mapContextDrawer = mapContextManager
                        .createDefaultMapContextDrawerInstance();
            } else {
                this.mapContextDrawer = mapContextManager
                        .createMapContextDrawerInstance(mapContextDrawerClass);
            }
		    this.mapContextDrawer.setMapContext(this);
            this.mapContextDrawer.setViewPort(viewPort);
		}

		return this.mapContextDrawer;
	}
	
	public void setMapContextDrawerClass(Class mapContextDrawerClass)
            throws InvalidMapContextDrawerClassException {
		if (! MapContextDrawer.class.isAssignableFrom(mapContextDrawerClass)){
			throw new InvalidMapContextDrawerClassException(
                    mapContextDrawerClass);
		}
		this.mapContextDrawerClass = mapContextDrawerClass;
		if (this.mapContextDrawer != null){
			this.mapContextDrawer.dispose();
			this.mapContextDrawer = null;
		}
	}

	public void setMapContextDrawer(MapContextDrawer drawer){
		if (this.mapContextDrawer != null){
			this.mapContextDrawer.dispose();
			this.mapContextDrawer = null;
		}
		this.mapContextDrawer = drawer;
		if (this.mapContextDrawer != null){
			this.mapContextDrawer.setMapContext(this);
			this.mapContextDrawer.setViewPort(viewPort);
		}
	}

}
