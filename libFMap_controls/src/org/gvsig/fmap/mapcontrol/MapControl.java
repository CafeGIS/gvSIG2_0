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
package org.gvsig.fmap.mapcontrol;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.events.AtomicEvent;
import org.gvsig.fmap.mapcontext.events.listeners.AtomicEventListener;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionEvent;
import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.SpatialCache;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.CompoundBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;
import org.gvsig.fmap.mapcontrol.tools.grid.Grid;
import org.gvsig.fmap.mapcontrol.tools.snapping.GeometriesSnappingVisitor;
import org.gvsig.fmap.mapcontrol.tools.snapping.SnappingVisitor;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapper;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperGeometriesVectorial;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperRaster;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperVectorial;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.exceptionHandling.ExceptionHandlingSupport;
import com.iver.utiles.exceptionHandling.ExceptionListener;


/**
 * <p>A component that includes a {@link MapContext MapContext} with support for use it as a particular {@link Behavior Behavior}.</p>
 *
 * <p>A developer can register a set of <code>Behavior</code>, but only one (that can be a composition of several) of them can be active. The active one
 *  defines the way to work and access with its <code>MapContext</code>'s layers. The active behavior, in combination with the appropriate
 *  {@link ToolListener ToolListener} will allow user work with a particular <i>tool</i>.</p>
 *
 * <p>All mouse events produced on this component will be delegated to the current active behavior, the <i>currentMapTool</i>.</p>
 *
 * <p><b>Drawing process:</b></p>
 *
 * <p>Uses a double buffer for the drawing process of <code>MapContext</code>'s information.</p>
 *
 * <p>If the double buffer wasn't created, creates a new one.</p>
 *
 * <p>Paints the component according the following algorithm:
 * <br>
 *  &nbsp If <i>status</i> is <i>UPDATED</i>:<br>
 *  &nbsp &nbsp If there is a <i>double buffer</i>:<br>
 *  &nbsp &nbsp &nbsp If there is a <i>behavior</i> for managing the <code>MapControl</code> instance, delegates
 *   the drawing process to that behavior, calling: <code><i>behavior_instance</i>.paintComponent(g)</code>.<br>
 *  &nbsp &nbsp &nbsp Else, repaints the current graphical information quickly calling: <code>g.drawImage(image,0,0,null)</code>.<br>
 *  &nbsp Else, (<i>status</i> is <i>OUTDATED</i>, or <i>ONLY_GRAPHICS</i>): executes a quickly repaint of the previous information calling <code>g.drawImage(image,0,0,null)</code>, and creates
 *   a <i>painting request</i> to delegate the heavy drawing process to the {@link Drawer2 Drawer2}'s worker thread, according the <i>SingleWorketThread</i> pattern, starting a timer to update
 *   (invoking <code>repaint()</code>) the view every delay of <code>1000 / drawFrameRate</code> ms. during that heavy drawing process, and if its enabled <code>drawAnimationEnabled</code>. The <i>painting request</i> once is being attended, invokes <code>MapContext</code> to
 *   draw the layers: <code>mapContext.draw(image, g, cancel,mapContext.getScaleView());</code>
 * <br>
 * <p>Some notes:
 *  <ul>
 *   <li>The painting process can be cancelled calling {@link #cancelDrawing() #cancelDrawing()}.</li>
 *   <li>At last resort, the particular implementation of each layer in a <code>MapControl</code>'s <code>MapContrext</code>
 *    will be that one which will draw the graphical information, and, if supports, which could cancel its drawing subprocess.</li>
 *   <li>It's possible to force repaint all layers, calling {@link #drawMap(boolean doClear) #drawMap(boolean)}.</li>
 *   <li>It's possible repaint only the dirty layers, calling {@link #rePaintDirtyLayers() #rePaintDirtyLayers()}.</li>
 *   <li>It's possible repaint only the {@link GraphicLayer GraphicLayer}, calling {@link #drawGraphics() #drawGraphics()}.</li>
 *  </ul>
 * </p>
 *
 * <p><b>Tools:</b></p>
 *
 * <p>A developer can:
 *   <ul>
 *    <li>Register each tool as:
 *     <ul>
 *      <li>A single behavior: {@link #addMapTool(String, Behavior) #addMapTool(String, Behavior)}.</li>
 *      <li>Or, a compound behavior: {@link #addMapTool(String, Behavior) #addMapTool(String, Behavior)}.</li>
 *     </ul>
 *    </li>
 *    <li>Get the current active tool: {@link #getCurrentMapTool() #getCurrentMapTool()}.</li>
 *    <li>Get the current active tool name: {@link #getCurrentTool() #getCurrentTool()}.</li>
 *    <li>Get a registered tool: {@link #getMapTool(String) #getMapTool(String)}.</li>
 *    <li>Get the name of all tools registered: {@link #getMapToolsKeySet() #getMapToolsKeySet()}.</li>
 *    <li>Get all tools registered, including the name they were registered: {@link #getNamesMapTools() #getNamesMapTools()}.</li>
 *    <li>Determine if has a tool registered: {@link #hasTool(String) #hasTool(String)}.</li>
 *    <li>Set as an active tool, one of the registered: {@link #setTool(String) #setTool(String)}.</li>
 *    <li>Set as active tool, the previous used: {@link #setPrevTool() #setPrevTool()}.</li>
 *    <li>Set the current tool: {@link #setCurrentMapTool(Behavior) #setCurrentMapTool(Behavior)}.</li>
 *    <li>Change the draw frame rate: {@link #setDrawFrameRate(int) #setDrawFrameRate(int)} and {@link #applyFrameRate() #applyFrameRate()}.</li>
 *    <li>Get the draw frame rate: {@link #getDrawFrameRate() #getDrawFrameRate()}.</li>
 *    <li>Determine if will repaint this component each time timer finishes: {@link #isDrawAnimationEnabled() #isDrawAnimationEnabled()}.</li>
 *    <li>Change if will repaint this component each time timer finishes: {@link #setDrawAnimationEnabled(boolean) #setDrawAnimationEnabled(boolean)}.</li>
 *    <li>Get the shared object that determines if a drawing process must be cancelled or can continue: {@link #getCanceldraw() #getCanceldraw()}.</li>
 *    <li>Get the combined tool: {@link #getCombinedTool() #getCombinedTool()}.</li>
 *    <li>Set a combined tool: {@link #setCombinedTool(Behavior) #setCombinedTool(Behavior)}.</li>
 *    <li>Remove the combined tool: {@link #removeCombinedTool() #removeCombinedTool()}.</li>
 *   </ul>
 * </p>
 *
 * <p><b>Exception listener:</b></p>
 *
 * <p> Adding an <code>ExceptionListener</code>, can get notification about any exception produced:
 *  <ul>
 *   <li>Attending a <i>painting request</i>.</li>
 *   <li>Working with the active tool.</li>
 *   <li>Applying a <i>zoom in</i> or <i>zoom out</i> operation.</li>
 *  </ul>
 * </p>
 *
 * <p><b>Other:</b></p>
 *
 * <p>Other useful capabilities of <code>MapControl</code>:
 *   <ul>
 *    <li>Cancel the current drawing process (notifying it also to the inner
 *     <code>MapContext</code> instance and its layers): {@link #cancelDrawing() #cancelDrawing()}.</li>
 *    <li>Applying a <i>zoom in</i> operation centered at mouse position (without a <code>ToolListener</code>): {@link #zoomIn() #zoomIn()}.</li>
 *    <li>Applying a <i>zoom out</i> operation centered at mouse position (without a <code>ToolListener</code>): {@link #zoomOut() #zoomOut()}.</li>
 *   </ul>
 * </p>
 *
 * @see CancelDraw
 * @see Drawer2
 * @see MapContextListener
 * @see MapToolListener
 *
 * @author Fernando González Cortés
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class MapControl extends JComponent implements ComponentListener, Observer {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	/**
	 * <p>One of the possible status of <code>MapControl</code>. Determines that all visible information has been
	 * drawn and its updated.</p>
	 */
	public static final int ACTUALIZADO = 0;

	/**
	 * <p>One of the possible status of <code>MapControl</code>. Determines that not all visible information has been
	 * drawn or isn't updated.</p>
	 */
	public static final int DESACTUALIZADO = 1;

	/**
	 * <p>One of the possible status of <code>MapControl</code>. Determines that only the graphical layer must
	 * be drawn / updated.</p>
	 */
	public static final int ONLY_GRAPHICS = 2;



    /**
     * <p>Determines if the drawer can update this <code>MapControl</code> instance when the timer launches an event.</p>
     */
    private static boolean drawAnimationEnabled = true;

    // public static final int FAST_PAINT = 3;
	//private static Logger logger = Logger.getLogger(MapControl.class.getName());

    /**
     * <p>Inner model with the layers, event support for drawing them, and the <code>ViewPort</code>
     *  with information to adapt to the bounds available in <i>image coordinates</i>.</p>
     *
     * @see #getMapContext()
     * @see #setMapContext(MapContext)
     */
    private MapContext mapContext = null;

    //private boolean drawerAlive = false;


	/**
	 * <p>All registered <code>Behavior</code> that can define a way to work with this <code>MapControl</code>.</p>
	 *
	 * <p>Only one of them can be active at a given moment.</p>
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 * @see #getMapTool(String)
	 * @see #getMapToolsKeySet()
	 * @see #getNamesMapTools()
	 */
    protected HashMap namesMapTools = new HashMap();

	/**
	 * <p>Active {@link Behavior Behavior} that will generate events according a criterion, and then, with a {@link ToolListener ToolListener}
	 *  associated, will simulate to user that works with this component as a particular tool.</p>
	 *
	 * @see #getCurrentMapTool()
	 * @see #getCurrentTool()
	 * @see #setTool(String)
	 */
    protected Behavior currentMapTool = null;

	/**
	 * <p>Determines which's the current drawn status of this component:
	 * <ul>
	 *  <li><b>OUTDATED</b>: all visible information has been drawn or isn't updated.</li>
	 *  <li><b>UTDATED</b>: all visible information has been drawn and its updated.</li>
	 *  <li><b>ONLY_GRAPHICS</b>: only the graphical layer must be drawn / updated.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>The <code>MapControl</code> drawing process will consider the value of this parameter to decide which elements will
	 *  be updated or drawn.</p>
	 */
    private int status = DESACTUALIZADO;

	/**
	 * <p>Image with a buffer to accelerate the draw the changes of the graphical items in this component.</p>
	 *
	 * <p>Firstly, information will be drawn in the buffer, and, when is outright drawn, that information will be displayed.
	 * Meanwhile, the previous image can be kept showed.</p>
	 *
	 * @see BufferedImage
	 *
	 * @see #getImage()
	 */
    private BufferedImage image = null;

	/**
	 * <p>Name of the tool used currently to interact with this component.</p>
	 *
	 * @see #getCurrentTool()
	 * @see #setTool(String)
	 */
    protected String currentTool;

	/**
	 * <p>Object to store the flag that notifies a drawing thread task and <code>MapContext</code>'s layers,
	 * that must be canceled or can continue with the process.</p>
	 *
	 * @see #cancelDrawing()
	 */
    private CancelDraw canceldraw;

    //private boolean isCancelled = true;

	/**
	 * <p>Fires an action events after a specified delay.</p>
	 *
	 * <p><code>MapControl</code> will use the timer to update its visible graphical information during
	 *  a drawing process, or allowing to cancel that process.</p>
	 *
	 * <p>This is very useful to pretend faster interactivity to user when <code>MapControl</code> has
	 *  lots of layers, and / or layers with heavy graphical elements, that need a long time to finish
	 *  drawing all its data.</p>
	 */
    private Timer timer;

	/**
	 * <p>Reference to the {@link ViewPort ViewPort} of the {@link MapContext MapContext} of this component.</p>
	 *
	 * <p>The view port once is created an instance of <code>MapControl</code>,
	 *  is obtained from the <i>EPSG:23030</i> projection, that's the default projection for this component.</p>
	 *
	 * <p>After, the view port will change adapting itself according the current projection and the extent.</p>
	 *
	 * @see #getViewPort()
	 *
	 * @see ViewPort
	 */
    protected ViewPort vp;

    //private Drawer drawer;

	/**
	 * <p>Manager of all <code>MapControl</code> painting requests.</p>
	 */
    private Drawer2 drawer2;

    // private boolean firstDraw = true;

	/**
	 * <p>Listener of all kind of mouse events produced in this component.</p>
	 *
	 * <p>Delegates each mouse event to the current map tool.</p>
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 * @see #getMapTool(String)
	 * @see #getMapToolsKeySet()
	 * @see #getNamesMapTools()
	 * @see #setTool(String)
	 */
    protected MapToolListener mapToolListener = new MapToolListener();

	/**
	 * <p>Listener of all events produced in a this component's <code>MapContext</code>
	 * object during an atomic period of time.</p>
	 */
    private MapContextListener mapContextListener = new MapContextListener();

	/**
	 * <p>Group of <code>ExceptionListener</code> that, in whatever moment could be notified a Throwable Java error or exception.</p>
	 *
	 * @see #addExceptionListener(ExceptionListener)
	 * @see #removeExceptionListener(ExceptionListener)
	 */
    private ExceptionHandlingSupport exceptionHandlingSupport = new ExceptionHandlingSupport();

    /**
     * <p>Name of the previous tool used.</p>
     */
	protected String prevTool;

	/**
	 * <p>Tool that will be used combined with the current tool of this <code>MapControl</code>.</p>
	 */
	private Behavior combinedTool = null;

	/**
     * We need this to avoid not wanted refresh. REMEMBER TO SET TO TRUE!!
     */
    // private boolean paintEnabled = false;
	/**
	 * Edition preferences.
	 */
	private static Preferences prefs = Preferences.userRoot().node( "cadtooladapter" );
	/**
	 * Optional grid that could be applied on the <code>MapControl</code>'s view port.
	 *
	 * @see #getGrid()
	 * @see #setAdjustGrid(boolean)
	 */
	private Grid cadgrid = new Grid();
	/**
	 * Represents the cursor's point selected in <i>screen coordinates</i>.
	 *
	 * @see ViewPort#fromMapPoint(Point2D)
	 */
	private Point2D adjustedPoint;
	/**
	 * <p>Determines if the position of the snap of the mouse's cursor on the <code>MapControl</code>
	 * is within the area around a control point of a geometry.</p>
	 *
	 * <p>The area is calculated as a circle centered at the control point and with radius the pixels tolerance
	 *  defined in the preferences.</p>
	 */
	private boolean bForceCoord = false;
	/**
	 * Kind of geometry drawn to identify the kind of control point selected by the cursor's mouse.
	 */
	private ISnapper usedSnap = null;
	/**
	 * A light yellow color for the tool tip text box associated to the point indicated by the mouse's cursor.
	 */
	private Color theTipColor = new Color(255, 255, 155);

	/**
	 * Determines if the snap tools are enabled or disabled.
	 *
	 * @see #isRefentEnabled()
	 * @see #setRefentEnabled(boolean)
	 */
	private boolean bRefent = true;

	/**
	 * Stores the 2D map coordinates of the last point added.
	 */
	private double[] previousPoint = null;

	private static HashMap selected = new HashMap();

	public static int tolerance=4;
	/**
	 * Represents the cursor's point selected in <i>map coordinates</i>.
	 *
	 * @see MapControl#toMapPoint
	 */
	private Point2D mapAdjustedPoint;

	/**
	 * <p>Creates a new <code>MapControl</code> instance with the following characteristics:
	 * <ul>
	 *  <li><i>Name</i>: MapControl .</li>
	 *  <li>Disables the double buffer of <code>JComponent</code> .</li>
	 *  <li>Sets opaque <i>(see {@link JComponent#setOpaque(boolean)} )</i>. </li>
	 *  <li>Sets its status to <code>OUTDATED</code> .</li>
	 *  <li>Creates a new {@link CancelDraw CancelDraw} object to notify <code>MapContext</code>'s layers if can continue processing the drawn or must cancel it.</li>
	 *  <li>Creates a new {@link MapContext MapContext} with a new {@link ViewPort ViewPort} in the projection <i>"EPSG:23030"</i> .</li>
	 *  <li>Creates a new {@link CommandListener CommandListener} for edition operations.</li>
	 *  <li>Creates a new {@link MapToolListener MapToolListener}, and associates it as a listener of whatever kind of mouse events produced in this component.</li>
	 *  <li>Creates a new {@link Drawer2 Drawer2} for managing the painting requests.</li>
	 *  <li>Creates a new timer that will invoke refresh this component <code>drawFrameRate</code> per second, when is running a drawing process, and its enabled
	 *   <code>drawAnimationEnabled</code>.</li>
	 * </ul>
	 * </p>
	 */
	public MapControl() {
		this.setName("MapControl");
		setDoubleBuffered(false);
		setOpaque(true);
		status = DESACTUALIZADO;

		//Clase usada para cancelar el dibujado
		canceldraw = new CancelDraw();

		//Modelo de datos y ventana del mismo
		// TODO: Cuando creamos un mapControl, deberíamos asignar
		// la projección por defecto con la que vayamos a trabajar.
		// 23030 es el código EPSG del UTM30 elipsoide ED50
		vp = new ViewPort(CRSFactory.getCRS("EPSG:23030"));
		setMapContext(new MapContext(vp));

		//eventos
		this.addComponentListener(this);
		this.addMouseListener(mapToolListener);
		this.addMouseMotionListener(mapToolListener);
		this.addMouseWheelListener(mapToolListener);

        this.drawer2 = new Drawer2();
		//Timer para mostrar el redibujado mientras se dibuja
		timer = new Timer(1000/MapContext.getDrawFrameRate(),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						if (drawAnimationEnabled) {
							MapControl.this.repaint();
						}
					}
				});
		initializeGrid();
	}

	/**
	 * <p>Sets a <code>MapContext</code> to this component.</p>
	 *
	 * <p>The <code>MapContext</code> has the <i>model</i>, and most of the <i>view</i>,
	 * and <i>control</i> logic of the layers of this component, including a {@link ViewPort ViewPort} to adapt the
	 * information to the projection, and to display it in the available area.</p>
	 *
	 * <p>If <code>model</code> hadn't a <code>ViewPort</code>, assigns the current one to it, otherwise, use its <code>ViewPort</code>.</p>
	 *
	 * <p>After assigning the <code>MapContext</code> and <code>ViewPort</code>, sets the same {@link MapContextListener MapContextListener}
	 *  that was using, and changes the <i>status</i> to <code>OUTDATED</code>.</p>
	 *
	 * @param model this component's <code>MapContext</code>, that includes the <code>ViewPort</code>.
	 *
	 * @see MapContext
	 *
	 * @see #getMapContext()
	 */
	public void setMapContext(MapContext model) {
		if (mapContext != null) {
			mapContext.removeAtomicEventListener(mapContextListener);
		}

		mapContext = model;

		if (mapContext.getViewPort() == null) {
			mapContext.setViewPort(vp);
		} else {
			vp = mapContext.getViewPort();
			cadgrid.setViewPort(vp);
			// vp.setImageSize(new Dimension(getWidth(), getHeight()));
			//System.err.println("Viewport en setMapContext:" + vp);
		}

		mapContext.addAtomicEventListener(mapContextListener);

		status = DESACTUALIZADO;
	}

	/**
	 * <p>Gets this component's {@link MapContext MapContext} projection.</p>
	 *
	 * @return this component's {@link MapContext MapContext} projection
	 *
	 * @see MapContext#getProjection()
	 * @see MapControl#setProjection(IProjection)
	 */
	public IProjection getProjection() {
		return getMapContext().getProjection();
	}

	/**
	 * <p>Sets the projection to this component's {@link MapContext MapContext}.</p>
	 *
	 * @param proj the kind of projection to this component's {@link MapContext MapContext}
	 *
	 * @see MapContext#setProjection(IProjection)
	 * @see MapControl#getProjection()
	 */
	public void setProjection(IProjection proj) {
		getMapContext().setProjection(proj);
	}

	/**
	 * <p>Gets this component's <code>MapContext</code>, with the <i>model</i>, and most of the <i>view</i>,
	 * and <i>control</i> logic of the layers of this component, including a {@link ViewPort ViewPort} to adapt the
	 * information to the projection, and display it in the available area.</p>
	 *
	 * @return this component's <code>MapContext</code>, that includes the <code>ViewPort</code> used to project the
	 * graphical information, and display it in the available area
	 *
	 * @see MapContext
	 *
	 * @see MapControl#setMapContext(MapContext)
	 */
	public MapContext getMapContext() {
		return mapContext;
	}

	/**
	 * <p>Registers a new behavior to this component.</p>
	 *
	 * <p>According the nature of the {@link Behavior Behavior}, different events will be generated. Those
	 *  events can be caught by a particular {@link ToolListener ToolListener}, allowing user to interact with this
	 *  <code>MapControl</code> object as a <i>tool</i>.</p>
	 *
	 * @param name name to identify the behavior to add
	 * @param tool the behavior to add
	 *
	 * @see #addMapTool(String, Behavior[])
	 * @see #getNamesMapTools()
	 * @see #getMapToolsKeySet()
	 * @see #hasTool(String)
	 */
	public void addMapTool(String name, Behavior tool) {
		namesMapTools.put(name, tool);
		tool.setMapControl(this);
	}

	/**
	 * <p>Registers a new behavior to this component as a {@link CompoundBehavior CompoundBehavior} made up of <code>tools</code>.</p>
	 *
	 * <p>According the nature of the behaviors registered, different events will be generated. Those
	 *  events can be caught by a particular {@link ToolListener ToolListener}, allowing user to interact with this
	 *  <code>MapControl</code> object as a <i>tool</i>.</p>
	 *
	 * @param name name to identify the compound behavior to add
	 * @param tools the compound behavior to add
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #getNamesMapTools()
	 * @see #getMapToolsKeySet()
	 * @see #hasTool(String)
	 */
	public void addMapTool(String name, Behavior[] tools){
		CompoundBehavior tool = new CompoundBehavior(tools);
		addMapTool(name, tool);
	}

	/**
	 * <p>Gets the <code>Behavior</code> registered in this component, identified
	 *  by <code>name</code>.</p>
	 *
	 * @param name name of a registered behavior
	 *
	 * @return tool the registered behavior in this component as <code>name</code>, or <code>null</code> if
	 *  no one has that identifier
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 * @see #hasTool(String)
	 */
	public Behavior getMapTool(String name) {
		return (Behavior)namesMapTools.get(name);
	}

	/**
	 * <p>Returns a set view of the keys that identified the tools
	 *  registered.</p>
	 *
	 * @return a set view of the keys that identified the tools registered
	 *
	 * @see HashMap#keySet()
	 *
	 * @see #getNamesMapTools()
 	 * @see #addMapTool(String, Behavior)
 	 * @see #addMapTool(String, Behavior[])
	 */
	public Set getMapToolsKeySet() {
		return namesMapTools.keySet();
	}

	/**
	 * <p>Returns <code>true</code> if this component contains a tool identified by <code>toolName</code>.</p>
	 *
	 * @param toolName identifier of the tool
	 *
	 * @return <code>true</code> if this component contains a tool identified by <code>toolName</code>; otherwise <code>false</code>
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 */
	public boolean hasTool(String toolName) {
		return namesMapTools.containsKey(toolName);
	}

	/**
	 * <p>Sets as current active <code>Behavior</code> associated to this component, that one which
	 *  is registered and identified by <code>toolName</code>.</p>
	 *
	 * <p>Changing the current active behavior for this <code>MapControl</code>, implies also updating the
	 *  previous <i>behavior</i> tool, and the current cursor.</p>
	 *
	 * @param toolName name of a registered behavior
	 *
	 * @see #getCurrentMapTool()
	 * @see #getCurrentTool()
	 */
	public void setTool(String toolName) {
		prevTool=getCurrentTool();
		Behavior mapTool = (Behavior) namesMapTools.get(toolName);
		currentMapTool = mapTool;
		currentTool = toolName;

		if (combinedTool != null) {
			if (mapTool instanceof CompoundBehavior) {
				((CompoundBehavior)mapTool).addMapBehavior(combinedTool, true);
			}
			else {
				currentMapTool = new CompoundBehavior(new Behavior[] {currentMapTool});
				((CompoundBehavior)currentMapTool).addMapBehavior(combinedTool, true);
			}
		}

//		this.setCursor(mapTool.getCursor());
	}

	/**
	 * <p>Gets as current active <code>Behavior</code> associated to this component, that one which
	 *  is registered and identified by <code>toolName</code>.</p>
	 *
	 * <p>Changing the current active behavior for this <code>MapControl</code>, implies also updating the
	 *  previous <i>behavior</i> tool, and the current cursor.</p>
	 *
	 * @param toolName name of a registered behavior
	 *
	 * @see #getCurrentTool()
	 * @see #setTool(String)
	 */
	public Behavior getCurrentMapTool(){
		return currentMapTool;
	}

	/**
	 * <p>Returns the name of the current selected tool on this MapControl</p>
	 *
	 * @return the name of the current's behavior tool associated to this component
	 *
	 * @see #getCurrentMapTool()
	 * @see #setTool(String)
	 */
	public String getCurrentTool() {
		return currentTool;
	}

	/**
	 * <p>Determines that current drawing process of <code>MapControl</code>'s <code>MapContext</code>'s data must be canceled.</p>
	 *
	 * <p>It has no effects if now isn't drawing that graphical information.</p>
	 *
	 * <p>At last resort, the particular implementation of each layer in this <code>MapControl</code>'s <code>MapContrext</code>
     *   will be that one which will draw the graphical information, and, if supports, which could cancel its drawing subprocess.</p>
	 */
	public void cancelDrawing() {
		/* if (drawer != null) {
			if (!drawer.isAlive()) {
				return;
			}
		}
		*/
		canceldraw.setCanceled(true);

		/* while (!isCancelled) {
			if (!drawer.isAlive()) {
			    // Si hemos llegado aquí con un thread vivo, seguramente
			    // no estamos actualizados.

				break;
			}

		}
		canceldraw.setCancel(false);
		isCancelled = false;
        drawerAlive = false; */
	}

	/**
	 * <p>Creates a {@link BufferedImage BufferedImage} image if there was no buffered image, or if
	 *  its viewport's image height or width is different from this component's size. Once has created
	 *  a double-buffer, fills it with the vieport's background color, or with <i>white</i> if it had no background color.</p>
	 *
	 * <p>If no double-buffered existed, creates a {@link BufferedImage BufferedImage} with the size of this component,
	 * and as an image with 8-bit RGBA color components packed into integer pixels. That image has a <code>DirectColorModel</code> with alpha.
	 * The color data in that image is considered not to be premultiplied with alpha.</p>
	 *
	 * <p>Once has created and filled the new inner <code>MapControl</code>'s double-buffer, changes the status to
	 * <code>OUTDATED</code>.</p>
	 *
	 * @return <code>true</code> if has created and filled a new double-buffer for this <code>MapControl</code> instance; otherwise <code>false</code>
	 */
    private boolean adaptToImageSize()
    {
        if ((image == null) || (vp.getImageWidth() != this.getWidth()) || (vp.getImageHeight() != this.getHeight()))
        {
            image = new BufferedImage(this.getWidth(), this.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            // ESTILO MAC
//        	image = GraphicsEnvironment.getLocalGraphicsEnvironment()
//        			.getDefaultScreenDevice().getDefaultConfiguration()
//        			.createCompatibleImage(this.getWidth(), this.getHeight());
            vp.setImageSize(new Dimension(getWidth(), getHeight()));
            getMapContext().getViewPort().refreshExtent();


            Graphics gTemp = image.createGraphics();
            Color theBackColor = vp.getBackColor();
            if (theBackColor == null) {
				gTemp.setColor(Color.WHITE);
			} else {
				gTemp.setColor(theBackColor);
			}

            gTemp.fillRect(0,0,getWidth(), getHeight());
            gTemp.dispose();
            status = DESACTUALIZADO;
            // g.drawImage(image,0,0,null);
            return true;
        }
        return false;
    }

	/**
	 * <p>Paints the graphical information of this component using a double buffer.</p>
	 *
	 * <p>If the double buffer wasn't created, creates a new one.</p>
	 *
	 * <p>Paints the component according the following algorithm:
	 * <br>
	 *  &nbsp If <i>status</i> is <i>UPDATED</i>:<br>
	 *  &nbsp &nbsp If there is no <i>double buffer</i>:<br>
	 *  &nbsp &nbsp &nbsp If there is a <i>behavior</i> for managing the <code>MapControl</code> instance, delegates
	 *   the drawing process to that behavior, calling: <code><i>behavior_instance</i>.paintComponent(g)</code> &nbsp .<br>
	 *  &nbsp &nbsp &nbsp Else, repaints the current graphical information quickly calling: <code>g.drawImage(image,0,0,null)</code> &nbsp .<br>
	 *  &nbsp Else, (<i>status</i> is <i>OUTDATED</i>, or <i>ONLY_GRAPHICS</i>): executes a quickly repaint of the previous information calling <code>g.drawImage(image,0,0,null)</code>, and creates
	 *   a <i>painting request</i> to delegate the heavy drawing process to the {@link Drawer2 Drawer2}'s worker thread, according the <i>SingleWorketThread</i> pattern, starting a timer to update
	 *   (invoking <code>repaint()</code> that comprises invoke this method) the view every delay of 360 ms. during the the process drawing.</p>
	 *
  	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
  	 * @see Drawer2
	 */
	protected void paintComponent(Graphics g) {
        adaptToImageSize();
        /* if (status == FAST_PAINT) {
            System.out.println("FAST_PAINT");
            g.drawImage(image,0,0,null);
            status = ACTUALIZADO;
            return;
        } */
        // System.out.println("PINTANDO MAPCONTROL" + this);
		if (status == ACTUALIZADO) {
			// LWS logger.debug("Dibujando la imagen obtenida");

			/*
			 * Si hay un behaviour y la imagen es distinta de null se delega el dibujado
			 * en dicho behaviour
			 */
            if (image != null)
            {
                if (currentMapTool != null) {
					currentMapTool.paintComponent(g);
				} else {
					g.drawImage(image,0,0,null);
				}

				// System.out.println("Pinto ACTUALIZADO");
			}
		} else if ((status == DESACTUALIZADO)
                || (status == ONLY_GRAPHICS)) {
			// LWS System.out.println("DESACTUALIZADO: Obteniendo la imagen de la cartografía");
			/* if (isOpaque())
			{
			    if (image==null)
			    {
			        g.setColor(vp.getBackColor());
			        g.fillRect(0,0,getWidth(), getHeight());
			    }
			    // else g.drawImage(image,0,0,null);
			} */
            // cancelDrawing();
			//Se crea la imagen con el color de fonde deseado
			/* if (image == null)
			{
				image = new BufferedImage(this.getWidth(), this.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
				vp.setImageSize(new Dimension(getWidth(), getHeight()));
				Graphics gTemp = image.createGraphics();
			    Color theBackColor = vp.getBackColor();
			    if (theBackColor == null)
			        gTemp.setColor(Color.WHITE);
			    else
			        gTemp.setColor(theBackColor);

				gTemp.fillRect(0,0,getWidth(), getHeight());
				gTemp.dispose();
				// g.drawImage(image,0,0,null);
				System.out.println("Imagen con null en DESACTUALIZADO. Width = " + this.getWidth());
			} */
            // else
            // {


            // if (image != null)
            //  {
                g.drawImage(image,0,0,null);

                drawer2.put(new PaintingRequest());
                timer.start();
            /* }
            else
                return; */
            // }

            /* if (drawerAlive == false)
            {
                drawer = new Drawer(image, canceldraw);
                drawer.start();
			//Se lanza el tread de dibujado
            } */

			// status = ACTUALIZADO;
		}
		cadgrid.drawGrid(g);
		drawCursor(g);
	}

	/**
	 * <p>Gets the {@link BufferedImage BufferedImage} used to accelerate the draw of new ''frames'' with changes,
	 * or new graphical items in this component.</p>
	 *
	 * @return double buffered image used by this component to accelerate the draw of its graphical information, or
	 * <code>null</code> if isn't already created
	 *
	 * @see BufferedImage
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * <p>Forces repaint all visible graphical information in this component.</p>
	 *
	 * <p>If <code>doClear == true</code>, before repainting, clears the background color, with the
	 *  inner viewport's background color.</p>
	 *
	 * @param doClear <code>true</code> if needs clearing the background color before drawing the map
	 *
	 * @see #cancelDrawing()
	 * @see FLayers#setDirty(boolean)
	 */
	public void drawMap(boolean doClear) {
		cancelDrawing();
		//System.out.println("drawMap con doClear=" + doClear);
        status = DESACTUALIZADO;
		if (doClear)
        {
            // image = null; // Se usa para el PAN
            if (image != null)
            {
                Graphics2D g = image.createGraphics();
                Color theBackColor = vp.getBackColor();
                if (theBackColor == null) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(theBackColor);
				}
                g.fillRect(0, 0, vp.getImageWidth(), vp.getImageHeight());
                g.dispose();
            }
        }
		repaint();
	}


	/**
	 * <p>Cancels any current drawing process, changing the status to <code>OUTDATED</code>, and forcing
	 * repaint only the layers dirty.</p>
	 *
	 * @see #cancelDrawing()
	 */
	public void rePaintDirtyLayers()
	{
		cancelDrawing();
        status = DESACTUALIZADO;
        repaint();
	}

	/**
	 * <p>Cancels any current drawing process, changing the status to <code>ONLY_GRAPHICS</code>, and forcing
	 * repaint only the graphical layer of the <code>MapContext</code>.</p>
	 */
    public void drawGraphics() {
        status = ONLY_GRAPHICS;
        repaint();
    }

	/**
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
	}

	/**
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		/* image = new BufferedImage(this.getWidth(), this.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics gTemp = image.createGraphics();
		gTemp.setColor(vp.getBackColor());
		gTemp.fillRect(0,0,getWidth(), getHeight());
        System.out.println("MapControl resized");
	    // image = null;
	    vp.setImageSize(new Dimension(getWidth(), getHeight()));
		getMapContext().getViewPort().setScale(); */
		// drawMap(true);
	}

	/**
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
	}

	/**
	 * @see ExceptionHandlingSupport#addExceptionListener(ExceptionListener)
	 */
	public void addExceptionListener(ExceptionListener o) {
		exceptionHandlingSupport.addExceptionListener(o);
	}

	/**
	 * @see ExceptionHandlingSupport#removeExceptionListener(ExceptionListener)
	 */
	public boolean removeExceptionListener(ExceptionListener o) {
		return exceptionHandlingSupport.removeExceptionListener(o);
	}

	/**
	 * @see ExceptionHandlingSupport#throwException(Throwable)
	 */
	protected void throwException(Throwable t) {
		exceptionHandlingSupport.throwException(t);
	}

	/**
	 * <p>Represents each <code>MapControl</code>'s data painting request.</p>
	 *
	 * <p>The request will be attended by a <code>Drawer2</code>, which will hold it since the <code>Drawer2</code>'s worker
	 *  takes it, or arrives a new painting request, which will replace it.</p>
	 */
    private class PaintingRequest
    {
    	/**
    	 * <p>Creates a new <code>PaintingRequest</p> instance.</p>
    	 */
        public PaintingRequest()
        {
        }

        /**
         * <p><code>MapControl</code> paint process:</p>
         *
         * <p>
         *  <ul>
         *   <li><i>1.- </i>Cancels all previous <code>MapControl</code>'s drawing processes.</li>
         *   <li><i>2.- </i>If <i>status</i> was OUTDATED:
         *    <ul>
         *     <li><i>2.1.- </i>Fills the background color with viewport's background color, or <i>white</i> if it was undefined.</li>
         *     <li><i>2.2.- </i>Notifies <i>MapContext</i> to be drawn invoking: <code>mapContext.draw(double-buffer, double-buffer's buffer, shared cancel-draw object, mapContext.getScaleView());</code>.</li>
         *     <li><i>2.3.- </i>If <code>canceldraw.isCanceled()</code>
         *      <ul>
         *       <li><i>2.3.1.- </i>Sets <i>status</i> to OUTDATED.</li>
         *       <li><i>2.3.2.- </i>Sets <i>dirty</i> all layers stored in <i>MapContext</i>.</li>
         *      </ul>
         *     </li>
         *     <li><i>2.4.- </i>Else, sets <i>status</i> to UPDATED.</li>
         *    </ul>
         *   </li>
         *   <li><i>3.- </i>Else, if <i>status</i> was ONLY_GRAPHICS:
         *    <ul>
         *     <li><i>3.1.- </i>Sets <i>status</i> to UPDATED.</li>
         *     <li><i>3.2.- </i>Notifies <i>MapContext</i> to be drawn invoking: <code>mapContext.drawGraphics(double-buffer, double-buffer's buffer, shared cancel-draw object, mapContext.getScaleView());</code>.</li>
         *    </ul>
         *   </li>
         *   <li><i>4.- </i>Stops the <i>timer</i>.</li>
         *   <li><i>5.- </i>Repaints this component invoking: <code>repaint();</code></li>
         *  </ul>
         * </p>
         *
         * @see #cancelDrawing()
         * @see MapContext#draw(BufferedImage, Graphics2D, Cancellable, double)
         * @see MapContext#drawGraphics(BufferedImage, Graphics2D, Cancellable, double)
         *
         * @see ViewPort
         */
        public void paint()
        {
            try
            {
            	canceldraw.setCanceled(false);
                /* if (image == null)
                {
                    image = new BufferedImage(vp.getImageWidth(), vp.getImageHeight(),
                            BufferedImage.TYPE_INT_ARGB);
                    Graphics gTemp = image.createGraphics();
                    Color theBackColor = vp.getBackColor();
                    if (theBackColor == null)
                        gTemp.setColor(Color.WHITE);
                    else
                        gTemp.setColor(theBackColor);

                    gTemp.fillRect(0,0,getWidth(), getHeight());
                    gTemp.dispose();
                    // g.drawImage(image,0,0,null);
                    System.out.println("Imagen con null en DESACTUALIZADO. Width = " + this.getWidth());
                } */
                Graphics2D g = image.createGraphics();

                ViewPort viewPort = mapContext.getViewPort();

                if (status == DESACTUALIZADO)
                {
                	Graphics2D gTemp = image.createGraphics();
                    Color theBackColor = viewPort.getBackColor();
                    if (theBackColor == null) {
						gTemp.setColor(Color.WHITE);
					} else {
						gTemp.setColor(theBackColor);
					}
                    gTemp.fillRect(0, 0, viewPort.getImageWidth(), viewPort.getImageHeight());
                    // ESTILO MAC
//                  BufferedImage imgMac = new BufferedImage(vp.getImageWidth(), vp.getImageHeight(),
//                          BufferedImage.TYPE_INT_ARGB);
          //
//                  mapContext.draw(imgMac, g, canceldraw, mapContext.getScaleView());
//                  g.drawImage(imgMac, 0, 0, null);
                  // FIN ESTILO MAC
                  // SIN MAC:

                  mapContext.draw(image, g, canceldraw, mapContext.getScaleView());
                  if (!canceldraw.isCanceled()){
                  	status=ACTUALIZADO;
                 }else{
                  	status=DESACTUALIZADO;
//                  	getMapContext().getLayers().setDirty(true);
                  }

                }
                else if (status == ONLY_GRAPHICS)
                {
                    status = ACTUALIZADO;
                    mapContext.drawGraphics(image, g, canceldraw,mapContext.getScaleView());

                }



                // status = FAST_PAINT;
              //  drawerAlive = false;
                timer.stop();
                repaint();


            } catch (Throwable e) {
                timer.stop();
              //  isCancelled = true;
                e.printStackTrace();
                throwException(e);
            } finally {
            }
        }
    }

    /**
     * <p>An instance of <code>Drawer2</code> could manage all <code>MapControl</code> painting requests.</p>
     *
     * <p>Based on the <i>WorkerThread</i> software pattern, creates a worker thread that will attend sequentially
     *  the current waiting painting request, after finishing the previous (that could be by a cancel action).</p>
     *
     * <p>All new {@link PaintingRequest PaintingRequest} generated will be stored as <i>waiting requests</i> since the worker
     * attends it.</p>
     *
     * <p>If a worker finished and there was no <i>painting request</i>, the worker would be set to wait until any
     *  <i>painting request</i> would be put.</p>
     *
     * @author fjp
     */
    public class Drawer2
    {
        // Una mini cola de 2. No acumulamos peticiones de dibujado
        // dibujamos solo lo último que nos han pedido.


    	/**
    	 * <p>Painting request that's been attended by the <code>Drawer2</code>'s worker.</p>
    	 *
    	 * @see #put(org.gvsig.fmap.mapcontrol.MapControl.PaintingRequest)
    	 * @see #take()
    	 */
    	private PaintingRequest paintingRequest;

    	/**
    	 * <p>Painting request waiting to be attended by the <code>Drawer2</code>'s worker.</p>
    	 *
    	 * @see #put(org.gvsig.fmap.mapcontrol.MapControl.PaintingRequest)
    	 * @see #take()
    	 */
    	private PaintingRequest waitingRequest;

    	/**
    	 * <p>Determines that the <code>Drawer2</code>'s worker is busy attending a painting request.</p>
     	 *
    	 * @see #put(org.gvsig.fmap.mapcontrol.MapControl.PaintingRequest)
    	 * @see #take()
    	 */
        private boolean waiting;

    	/**
    	 * <p>Notifies the <code>Drawer2</code>'s worker to finish or continue with its process.</p>
    	 *
    	 * @see #setShutdown(boolean)
    	 */
        private boolean shutdown;

        /**
         * <p>Sets this <code>Drawer2</code>'s worker to finish or continue with its process.</p>
         *
         * @param isShutdown a boolean value
         */
        public void setShutdown(boolean isShutdown)
        {
            shutdown = isShutdown;
        }

        /**
         * <p>Creates a new drawer for managing all data painting requests in <code>MapControl</code>.</p>
         *
         * <p>Includes the following steps:
         *  <ul>
         *   <li>By default, there is no <i>current painting request</i>.</li>
         *   <li>By default, there is no <i>waiting painting request</i>.</li>
         *   <li>By default, the worker thread is waiting no <i>painting request</i>.</li>
         *   <li>By default, the worker thread is running.</li>
         *   <li>Creates and starts a worker thread for attending the <i>painting requests</i>.</li>
         *  </ul>
         * </p>
         */
        public Drawer2()
        {
            paintingRequest = null;
            waitingRequest = null;
            waiting = false;
            shutdown = false;
            new Thread(new Worker()).start();
        }

        /**
         * <p>Sets a <code>PaintingRequest</code> to be attended by the worker thread of this object. If
         *  this one was waiting, wakes up.</p>
         *
         * <p>All waiting threads will be notified synchronized.</p>
         *
         * @param newPaintRequest
         *
         * @see #take()
         */
        public void put(PaintingRequest newPaintRequest)
        {
            waitingRequest = newPaintRequest;
            if (waiting)
            {
                synchronized (this) {
                    notifyAll();
                }
            }
        }

        /**
         * <p>Used by this object's worker, returns the current waiting drawing request, causing current thread
         *  to wait until another thread invokes {@link #put(org.gvsig.fmap.mapcontrol.MapControl.PaintingRequest) #put(com.iver.cit.gvsig.fmap.MapControl.PaintingRequest)},
         *  if there was no waiting request.</p>
         *
         * <p>All threads will access synchronized to the waiting request.</p>
         *
         * @return <code>PaintingRequest</code> that was waiting to be attended
         *
         * @see #put(org.gvsig.fmap.mapcontrol.MapControl.PaintingRequest)
         */
        public PaintingRequest take()
        {
            if (waitingRequest == null)
            {
                synchronized (this) {
                    waiting = true;
                    try {
                        wait();
                    }
                    catch (InterruptedException ie)
                    {
                        waiting = false;
                    }
                }
            }
            paintingRequest = waitingRequest;
            waitingRequest = null;
            return paintingRequest;
        }

        /**
         * <p>Thread for attending painting requests.</p>
         *
         * <p>If there was no double buffer, sets the status to <code>OUTDATED</code> and finishes, otherwise
         *  takes the painting request (it's probably that would wait some time), cancel the previous drawing
         *  process, and starts processing the request.</p>
         *
         * @see Thread
         */
        private class Worker implements Runnable
        {
        	/*
        	 * (non-Javadoc)
        	 * @see java.lang.Runnable#run()
        	 */
        	public void run()
            {
                while (!shutdown)
                {
                    PaintingRequest p = take();
                    //System.out.println("Pintando");
                    if (image != null){
                    	cancelDrawing();
                        p.paint();
                    } else{
                        status = DESACTUALIZADO;
                    }
                }
            }
        }
    }

	/**
	 * <p><code>Drawer</code> is implemented for drawing the layers of a <code>MapControl</code>'s <i>MapContext</i> instance
	 *  as a <i>thread</i> of execution.</p>
	 *
	 * <p>Draws <code>MapControl</code> according its <i>status</i>:
	 *  <ul>
	 *   <li><code><i>ONLY_GRAPHICS</i></code>: refreshes only the graphical layer, changing the status to <code><i>UPDATED</i></code>, via
	 *    {@linkplain MapContext#drawGraphics(BufferedImage, Graphics2D, Cancellable, double) MapContext#drawGraphics(BufferedImage, Graphics2D, Cancellable, double)}.</li>
	 *   <li><code><i>OUTDATED</i></code>: refreshes all layers, changing the status to <code><i>UPDATED</i></code>, via
	 *    {@linkplain MapContext#draw(BufferedImage, Graphics2D, Cancellable, double) MapContext#draw(BufferedImage, Graphics2D, Cancellable, double)}.</li>
	 *  <ul>
	 * </p>
	 *
	 * <p>This drawing process is accelerated by a <code>BufferedImage</code>, and can be canceled.</p>
	 *
	 * <p>Once the drawing process has finished, the timer stops and this component gets repainted.</p>
	 *
	 * @deprecated
	 * @author Vicente Caballero Navarro
	 */
	public class Drawer extends Thread {
		//private Graphics g;

		/**
		 * <p>Image with a buffer to accelerate the draw the changes of the graphical items in this component.</p>
		 *
		 * <p>Firstly, information will be drawn in the buffer, and, when is outright drawn, that information will be displayed.
		 * Meanwhile, the previous image can be kept showed.</p>
		 *
		 * @see BufferedImage
		 */
		private BufferedImage image = null;

		/**
		 * <p>Object to store the flag that notifies the drawing must be canceled or can continue with the process.</p>
		 *
		 * <p>At last resort, the particular implementation of each layer in a <code>MapControl</code>'s <code>MapContrext</code>
         *   will be which will draw the graphical information, and, if supports, which could cancel its drawing subprocess.</p>
		 */
		private CancelDraw cancel;
		//private boolean threadCancel = false;

		/**
		 * <p>Creates a new <code>Drawer</code> instance.</p>
		 */
		public Drawer(BufferedImage image, CancelDraw cancel)
        {
			this.image = image;
			this.cancel = cancel;
         //   drawerAlive = true;
		}

		/**
		 * @see java.lang.Runnable#run()
		 * @see MapContext#draw(BufferedImage, Graphics2D, Cancellable, double)
		 * @see MapContext#drawGraphics(BufferedImage, Graphics2D, Cancellable, double)
		 */
		public void run() {
			try {
				// synchronized (Drawer.class) {
				    Graphics2D g = image.createGraphics();

				    ViewPort viewPort = mapContext.getViewPort();
                    if (status == DESACTUALIZADO)
                    {
    				    Color theBackColor = viewPort.getBackColor();
    				    if (theBackColor == null) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(theBackColor);
						}
    					g.fillRect(0, 0, viewPort.getImageWidth(), viewPort.getImageHeight());
                        status = ACTUALIZADO;
                        mapContext.draw(image, g, cancel,mapContext.getScaleView());
                    }
                    else if (status == ONLY_GRAPHICS)
                    {
                        status = ACTUALIZADO;
                        mapContext.drawGraphics(image, g, cancel,mapContext.getScaleView());
                    }

					timer.stop();
                    // status = FAST_PAINT;
                  //  drawerAlive = false;
					repaint();



				// }
			} catch (Throwable e) {
			    timer.stop();
				//isCancelled = true;
                e.printStackTrace();
				throwException(e);
			} finally {
			}
		}
	}

	/**
	 * <p>An instance of <code>CancelDraw</code> will be shared by all this <code>MapControl</code>'s <code>MapContext</code> layers,
	 *  allowing receive a notification that, when they're been drawn, to be cancelled.</p>
	 *
	 * @see Cancellable
	 *
	 * @author Fernando González Cortés
	 */
	public class CancelDraw implements Cancellable {
		/**
		 * <p>Determines if the drawing task must be canceled or not.</p>
		 *
		 * @see #isCanceled()
		 * @see #setCanceled(boolean)
		 */
		private boolean cancel = false;

		/**
		 * Creates a new <code>CancelDraw</code> object.
		 */
		public CancelDraw() {
		}

		/*
		 * (non-Javadoc)
		 * @see com.iver.utiles.swing.threads.Cancellable#setCanceled(boolean)
		 */
		public void setCanceled(boolean b) {
			cancel = b;
		}

		/*
		 * (non-Javadoc)
		 * @see com.iver.utiles.swing.threads.Cancellable#isCanceled()
		 */
		public boolean isCanceled() {
			return cancel;
		}
	}

	/**
	 * <p>Listens all kind of mouse events produced in {@link MapControl MapControl}, and invokes its current
	 *  map tool <i>({@link MapControl#getCurrentMapTool() MapControl#getCurrentMapTool()}</i> to simulate a behavior.</p>
	 *
	 * <p>Mouse wheel moved events produce a <i>zoom in</i> operation if wheel rotation is negative, or a <i>zoom out</i>
	 *  if its positive. Both will be centered in the position of the mouse, but, meanwhile <i>zoom in</i> operation
	 *  applies a factor of 0.9, <i>zoom out</i> operation applies a factor of 1.2</p>
	 *
	 * <p>Mouse wheel moved events can be produced as much frequently, that between each one, the drawing process could
	 *  hadn't finished. This is the reason that, in this situation, cancels always the previous drawing process before
	 *  applying a <i>zoom</i> operation, and ignores all new mouse positions that are produced before 1 second.</p>
	 *
	 * @author Fernando González Cortés
	 */
	public class MapToolListener implements MouseListener, MouseWheelListener,
		MouseMotionListener {

		/**
		 * <p>Used to avoid mouse wheel move events closed.</p>
		 *
		 * <p>If a mouse wheel move event is produced
		 */
		long t1;

		/**
		 * <p>Position of the mouse, in map coordinates.</p>
		 *
		 * <p>This point coordinates will be used as center of the <i>zoom</i> operation.</p>
		 */
		Point2D pReal;

		/**
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 * @see Behavior#mouseClicked(MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			try {
				if (currentMapTool != null) {
					currentMapTool.mouseClicked(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
		}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 * @see Behavior#mouseEntered(MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			clearMouseImage();
			try {
				if (currentMapTool != null) {
					currentMapTool.mouseEntered(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
		}

		/**
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 * @see Behavior#mouseExited(MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
			try {
				if (currentMapTool != null) {
					currentMapTool.mouseExited(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
		}

		/**
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 * @see Behavior#mousePressed(MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			try {
				if (currentMapTool != null) {
					currentMapTool.mousePressed(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
		}

		/**
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 * @see Behavior#mouseReleased(MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			try {
				if (currentMapTool != null) {
					currentMapTool.mouseReleased(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
		}

		/**
		 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
		 * @see Behavior#mouseWheelMoved(MouseWheelEvent)
		 */
		public void mouseWheelMoved(MouseWheelEvent e) {
			try {
				if (currentMapTool == null) {
					return;
				}

				currentMapTool.mouseWheelMoved(e);

				// Si el tool actual no ha consumido el evento
				// entendemos que quiere el comportamiento por defecto.
				if (!e.isConsumed())
				{
					// Para usar el primer punto sobre el que queremos centrar
					// el mapa, dejamos pasar un segundo para considerar el siguiente
					// punto como válido.
					if (t1 == 0)
					{
						t1= System.currentTimeMillis();
						pReal = vp.toMapPoint(e.getPoint());
					}
					else
					{
						long t2 = System.currentTimeMillis();
						if ((t2-t1) > 1000) {
							t1=0;
						}
					}
					cancelDrawing();
					ViewPort vp = getViewPort();


					/* Point2D pReal = new Point2D.Double(vp.getAdjustedExtent().getCenterX(),
							vp.getAdjustedExtent().getCenterY()); */
					int amount = e.getWheelRotation();
					double nuevoX;
					double nuevoY;
					double factor;

					if (amount < 0) // nos acercamos
					{
						factor = 0.9;
					} else // nos alejamos
					{
						factor = 1.2;
					}
					if (vp.getExtent() != null) {
						nuevoX = pReal.getX()
								- ((vp.getExtent().getWidth() * factor) / 2.0);
						nuevoY = pReal.getY()
								- ((vp.getExtent().getHeight() * factor) / 2.0);
						double x = nuevoX;
						double y = nuevoY;
						double width = vp.getExtent().getWidth() * factor;
						double height = vp.getExtent().getHeight() * factor;

						try {
							vp.setEnvelope(geomManager.createEnvelope(x,y,x+width,y+height, SUBTYPES.GEOM2D));
						} catch (CreateEnvelopeException e1) {
							logger.error("Error creating the envelope", e);
						}
					}


				}
			} catch (BehaviorException t) {
				throwException(t);
			}
		}

		/**
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 * @see Behavior#mouseDragged(MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {
			calculateSnapPoint(e.getPoint());
			try {
				if (currentMapTool != null) {
					currentMapTool.mouseDragged(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
			repaint();
		}

		/**
		 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 * @see Behavior#mouseMoved(MouseEvent)
		 */
		public void mouseMoved(MouseEvent e) {
			calculateSnapPoint(e.getPoint());
			try {
				if (currentMapTool != null) {
					currentMapTool.mouseMoved(e);
				}
			} catch (BehaviorException t) {
				throwException(t);
			}
			repaint();
		}
	}

	/**
	 * <p<code>MapContextListener</code> listens all events produced in a <code>MapControl</code>'s <code>MapContext</code>
	 * object during an atomic period of time, and sets it to dirty, <i>executing <code>drawMap(false)</code>, if any of the
	 * following conditions is accomplished</i>:
	 * <ul>
	 *  <li>Any of the <code>LayerEvent</code> in the <code>AtomicEvent</code> parameter notifies a <i>visibility change</i>.</li>
	 *  <li>There is at least one <code>ColorEvent</code> in the <code>AtomicEvent</code> parameter.</li>
	 *  <li>There is at least one <code>ExtentEvent</code> in the <code>AtomicEvent</code> parameter.</li>
	 *  <li>Any of the <code>LayerCollectionEvent</code> in the <code>AtomicEvent</code> parameter notifies that a driver's layer has reloaded it successfully.</li>
	 *  <li>There is at least one <code>LegendEvent</code> in the <code>AtomicEvent</code> parameter.</li>
	 *  <li>There is at least one <code>SelectionEvent</code> in the <code>AtomicEvent</code> parameter.</li>
	 * </ul>
	 * </p>
	 *
	 * @author Fernando González Cortés
	 */
	public class MapContextListener implements AtomicEventListener {
		/**
		 * @see org.gvsig.fmap.mapcontext.events.listeners.AtomicEventListener#atomicEvent(org.gvsig.fmap.mapcontext.events.AtomicEvent)
		 */
		public void atomicEvent(AtomicEvent e) {
			LayerEvent[] layerEvents = e.getLayerEvents();

			int eType;
			for (int i = 0; i < layerEvents.length; i++) {
				eType=layerEvents[i].getEventType();
//				if (eType == LayerEvent.DRAW_VALUES_CHANGED || eType == LayerEvent.VISIBILITY_CHANGED) {
//					MapControl.this.drawMap(false);
//					return;
//				}
				if (layerEvents[i].getProperty().equals("visible")) {
					MapControl.this.drawMap(false);
					return;
				} else if (layerEvents[i].getEventType() == LayerEvent.EDITION_CHANGED){
					MapControl.this.drawMap(false);
					return;
				}
			}

			if (e.getColorEvents().length > 0) {
				MapControl.this.drawMap(false);
				return;
			}

			if (e.getExtentEvents().length > 0) {
				MapControl.this.drawMap(false);
				return;
			}

			if (e.getProjectionEvents().length > 0) {
				//redraw = true;
			}


			LayerCollectionEvent[] aux = e.getLayerCollectionEvents();
			if (aux.length > 0) {
				for (int i=0; i < aux.length; i++)
				{
					if (aux[i].getAffectedLayer().getFLayerStatus().isDriverLoaded())
					{
						MapControl.this.drawMap(false);
						return;
					}
				}

			}

			if (e.getLegendEvents().length > 0) {
				MapControl.this.drawMap(false);
				return;
			}

			if (e.getSelectionEvents().length > 0) {
				MapControl.this.drawMap(false);
				return;
			}
		}
	}

	/**
	 * <p>Gets the <code>ViewPort</code> of this component's {@link MapContext MapContext} .</p>
	 *
	 * @see MapContext#getViewPort()
	 */
	public ViewPort getViewPort() {
		return vp;
	}

	/**
	 * <p>Returns all registered <code>Behavior</code> that can define a way to work with this <code>MapControl</code>.</p>
	 *
	 * @return registered <code>Behavior</code> to this <code>MapControl</code>
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 * @see #getMapToolsKeySet()
	 * @see #hasTool(String)
	 */
	public HashMap getNamesMapTools() {
		return namesMapTools;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.edition.commands.CommandListener#commandRepaint()
	 */
	public void commandRepaint() {
		drawMap(false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.edition.commands.CommandListener#commandRefresh()
	 */
	public void commandRefresh() {
		// TODO Auto-generated method stub
	}

	/**
	 * <p>Equivalent operation to <i>undo</i>.</p>
	 *
	 * <p>Exchanges the previous tool with the current one.</p>
	 *
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 * @see #setTool(String)
	 */
	public void setPrevTool() {
		setTool(prevTool);
	}

	/**
	 * <p>Executes a <i>zoom in</i> operation centered at the center of the extent.</p>
	 *
	 * <p>This implementation is designed for being invoked outside a <code>Behavior</code>, for example
	 *  by an action of pressing a button; and simulates that the event has been produced by
	 *  releasing the <i>button 1</i> of the mouse using the registered <code>Behavior</code> in this
	 *  <code>MapControl</code> object that's responsible for the <i>zoom in</i> operation.</p>
	 *
	 * @see #zoomOut()
	 */
	public void zoomIn() {
		Behavior mapTool = (Behavior) namesMapTools.get("zoomIn");
		ViewPort vp=getViewPort();
		Envelope r=getViewPort().getAdjustedExtent();
		Point2D pCenter=vp.fromMapPoint(r.getCenter(0),r.getCenter(1));
		MouseEvent e=new MouseEvent(this,MouseEvent.MOUSE_RELEASED,MouseEvent.ACTION_EVENT_MASK,MouseEvent.BUTTON1,(int)pCenter.getX(),(int)pCenter.getY(),1,true,MouseEvent.BUTTON1);
		try {
			mapTool.mousePressed(e);
			mapTool.mouseReleased(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
	}

	/**
	 * <p>Executes a <i>zoom out</i> operation centered at the center of the extent.</p>
	 *
	 * <p>This implementation is thought for being invoked outside a <code>Behavior</code>, for example
	 *  by an action of pressing a button, and simulates that the event has been produced by
	 *  releasing the <i>button 1</i> of the mouse using the registered <code>Behavior</code> in this
	 *  <code>MapControl</code> object that's responsible for the <i>zoom out</i> operation.</p>
	 *
	 * @see #zoomIn()
	 */
	public void zoomOut() {
		Behavior mapTool = (Behavior) namesMapTools.get("zoomOut");
		ViewPort vp=getViewPort();
		Envelope r=getViewPort().getAdjustedExtent();
		Point2D pCenter=vp.fromMapPoint(r.getCenter(0),r.getCenter(1));
		MouseEvent e=new MouseEvent(this,MouseEvent.MOUSE_RELEASED,MouseEvent.ACTION_EVENT_MASK,MouseEvent.BUTTON1,(int)pCenter.getX(),(int)pCenter.getY(),1,true,MouseEvent.BUTTON1);
		try {
			mapTool.mousePressed(e);
			mapTool.mouseReleased(e);
		} catch (BehaviorException t) {
			throwException(t);
		}
	}

	/**
	 * <p>Returns the listener used to catch all mouse events produced in this <code>MapControl</code> instance
	 *  and that redirects the calls to the current map tool.</p>
	 *
	 * @return the map tool listener used
	 */
	public MapToolListener getMapToolListener() {
		return mapToolListener;
	}

//	 mapTool can be null, for instance, in 3D's navigation tools
	/**
	 * <p>Sets <code>mapTool</code> as this <code>MapControl</code>'s current map tool.
	 *
	 * @param mapTool a map tool, or <code>null</code> to disable the interaction with the user
	 *
	 * @see #getCurrentMapTool()
	 * @see #getCurrentTool()
	 * @see #setTool(String)
	 * @see #setPrevTool()
	 * @see #addMapTool(String, Behavior)
	 * @see #addMapTool(String, Behavior[])
	 */
	public void setCurrentMapTool(Behavior mapTool ){
		currentMapTool = mapTool;
	}

	/**
	 * <p>Sets the delay to the timer that refreshes this <code>MapControl</code> instance.</p>
	 *
	 * <p><code>Delay (in ms) = 1000 / getDrawFrameRate()</code></p>
	 *
	 * @see #getDrawFrameRate()
	 * @see #setDrawFrameRate(int)
	 */
	public void applyFrameRate() {
		if (MapContext.getDrawFrameRate()>0) {
			timer.setDelay(1000/MapContext.getDrawFrameRate());
		}
	}



	/**
	 * <p>Determines if its enabled the repaint that invokes the timer according to {@link #getDrawFrameRate() #getDrawFrameRate()}.</p>
	 *
	 * @return <code>true</code> if its enabled; otherwise <code>false</code>
	 */
	public static boolean isDrawAnimationEnabled() {
		return drawAnimationEnabled;
	}

	/**
	 * <p>Sets if its enabled the repaint that invokes the timer according to {@link #getDrawFrameRate() #getDrawFrameRate()}.</p>
	 *
	 * @param drawAnimationEnabled <code>true</code> to enable the mode; otherwise <code>false</code>
	 */
	public static void setDrawAnimationEnabled(boolean drawAnimationEnabled) {
		MapControl.drawAnimationEnabled = drawAnimationEnabled;
	}

	/**
	 * <p>Gets the shared object that determines if a drawing process must be cancelled or can continue.</p>
	 *
	 * @return the shared object that determines if a drawing process must be cancelled or can continue
	 */
	public CancelDraw getCanceldraw() {
		return canceldraw;
	}

	/**
	 * <p>Gets the tool used in combination with the current tool of this <code>MapControl</code>.</p>
	 *
	 * @return the tool used in combination with the <code>currentMapTool</code>; <code>null</code> if there is
	 *  no combined tool
	 */
	public Behavior getCombinedTool() {
		return combinedTool;
	}

	/**
	 * <p>Sets a tool to be used in combination with the current tool of this <code>MapControl</code>.</p>
	 *
	 * @param combinedTool a tool to be used in combination with the current tool of <code>MapControl</code>
	 */
	public void setCombinedTool(Behavior combinedTool) {
		this.combinedTool = combinedTool;

		if (currentMapTool == null) {
			return;
		}

		if (currentMapTool instanceof CompoundBehavior) {
			((CompoundBehavior)currentMapTool).addMapBehavior(combinedTool, true);
		}
		else {
			currentMapTool = new CompoundBehavior(new Behavior[] {currentMapTool});
			((CompoundBehavior)currentMapTool).addMapBehavior(combinedTool, true);
		}

	}
	/**
	 * <p>Adds a new tool as combined tool.</p>
	 * <p>The new tool will be stored with the previous combined tools, and will be combined with
	 *  the current tool.</p>
	 * <p>If <code>tool</code> was already stored as a combined tool, doesn't adds it.</p>
	 *
	 * @param tool a new tool to be used combined with the current tool
	 */
	public void addCombinedTool(Behavior tool) {
		if (combinedTool == null) {
			combinedTool = tool;
		}
		else {
			if (combinedTool instanceof CompoundBehavior) {
				if (((CompoundBehavior)combinedTool).containsBehavior(tool)) {
					return;
				}

				((CompoundBehavior)combinedTool).addMapBehavior(tool, true);
			}
			else {
				if (combinedTool.equals(tool)) {
					return;
				}

				combinedTool = new CompoundBehavior(new Behavior[] {combinedTool});
				((CompoundBehavior)combinedTool).addMapBehavior(tool, true);
			}
		}

		if (currentMapTool == null) {
			return;
		}

		if (currentMapTool instanceof CompoundBehavior) {
			((CompoundBehavior)currentMapTool).addMapBehavior(tool, true);
		}
		else {
			currentMapTool = new CompoundBehavior(new Behavior[] {currentMapTool});
			((CompoundBehavior)currentMapTool).addMapBehavior(tool, true);
		}
	}
	/**
	 * <p>Removes the tool <code>tool</code> used in combination with the current tool of this <code>MapControl</code>.</p>
	 */
	public void removeCombinedTool(Behavior tool) {
		if ((currentMapTool != null) && (currentMapTool instanceof CompoundBehavior)) {
				((CompoundBehavior)currentMapTool).removeMapBehavior(tool);
		}

		if (combinedTool == null) {
			return;
		}

		if (combinedTool instanceof CompoundBehavior) {
			((CompoundBehavior)combinedTool).removeMapBehavior(tool);
		}
		else {
			combinedTool = null;
		}
	}


	/**
	 * <p>Updates the grid on the <code>ViewPort</code> of the associated <code>MapControl</code>
	 *  object according the values in the {@link com.iver.cit.gvsig.gui.cad.CADToolAdapter.prefs.Preferences com.iver.cit.gvsig.gui.cad.CADToolAdapter.prefs.Preferences}.</p>
	 *
	 * <p>The preferences are:
	 *  <ul>
	 *   <li>Show/hide the grid.</li>
	 *   <li>Adjust or not the grid.</li>
	 *   <li>Horizontal ( X ) line separation.</li>
	 *   <li>Vertical ( Y ) line separation.</li>
	 *  </ul>
	 * </p>
	 */
	public void initializeGrid(){
		boolean showGrid = prefs.getBoolean("grid.showgrid",cadgrid.isShowGrid());
		boolean adjustGrid = prefs.getBoolean("grid.adjustgrid",cadgrid.isAdjustGrid());

		double dx = prefs.getDouble("grid.distancex",cadgrid.getGridSizeX());
		double dy = prefs.getDouble("grid.distancey",cadgrid.getGridSizeY());

		setGridVisibility(showGrid);
		setAdjustGrid(adjustGrid);
		cadgrid.setGridSizeX(dx);
		cadgrid.setGridSizeY(dy);
	}

	public void setAdjustGrid(boolean adjustGrid) {
		cadgrid.setAdjustGrid(adjustGrid);
	}

	public void setGridVisibility(boolean showGrid) {
		cadgrid.setShowGrid(showGrid);
		cadgrid.setViewPort(getViewPort());
		this.repaint();
	}
	/**
	 * <p>Draws a 31x31 pixels cross round the mouse's cursor with an small geometry centered:
	 *  <ul>
	 *   <li><i>an square centered</i>: if isn't over a <i>control point</i>.
	 *   <li><i>an small geometry centered according to the kind of control point</i>: if it's over a control
	 *    point. In this case, the small geometry is drawn by a {@link ISnapper ISnapper} type object.<br>
	 *    On the other hand, a light-yellowed background tool tip text with the type of <i>control point</i> will
	 *     be displayed.</li>
	 * </p>
	 *
	 * @param g <code>MapControl</code>'s graphics where the data will be drawn
	 */
	private void drawCursor(Graphics g) {
		Image cursor=getCurrentMapTool().getImageCursor();



		g.setColor(Color.black);
		Point2D p = adjustedPoint;

		if (p == null) {
			cadgrid.setViewPort(vp);
			return;
		}
		g.drawImage(cursor, (int)p.getX()-16, (int)p.getY()-16, this);
//		int size1 = 15;
//		int size2 = 3;
//		g.drawLine((int) (p.getX() - size1), (int) (p.getY()),
//				(int) (p.getX() + size1), (int) (p.getY()));
//		g.drawLine((int) (p.getX()), (int) (p.getY() - size1),
//				(int) (p.getX()), (int) (p.getY() + size1));
//
//		// getMapControl().setToolTipText(null);
//		if (adjustedPoint != null) {
//			if (bForceCoord) {
//				/* g.setColor(Color.ORANGE);
//				g.drawRect((int) (adjustedPoint.getX() - 6),
//						(int) (adjustedPoint.getY() - 6), 12, 12);
//				g.drawRect((int) (adjustedPoint.getX() - 3),
//						(int) (adjustedPoint.getY() - 3), 6, 6);
//				g.setColor(Color.MAGENTA);
//				g.drawRect((int) (adjustedPoint.getX() - 4),
//						(int) (adjustedPoint.getY() - 4), 8, 8); */
//				if (usedSnap != null)
//				{
//					usedSnap.draw(g, adjustedPoint);
//
//					Graphics2D g2 = (Graphics2D) g;
//			        FontMetrics metrics = g2.getFontMetrics();
//			        int w = metrics.stringWidth(usedSnap.getToolTipText()) + 5;
//			        int h = metrics.getMaxAscent() + 5;
//			        int x = (int)p.getX()+9;
//			        int y = (int)p.getY()- 7;
//
//			        g2.setColor(theTipColor );
//			        g2.fillRect(x, y-h, w, h);
//			        g2.setColor(Color.BLACK);
//			        g2.drawRect(x, y-h, w, h);
//					g2.drawString(usedSnap.getToolTipText(), x+3, y-3);
//
//
//					// getMapControl().setToolTipText(usedSnap.getToolTipText());
//				}
//
//				bForceCoord = false;
//			} else {
//				g.drawRect((int) (p.getX() - size2), (int) (p.getY() - size2),
//						(int) (size2 * 2), (int) (size2 * 2));
//			}
//		}
//		repaint();
	}
	/**
	 * Hides the mouse's cursor.
	 */
	private void clearMouseImage() {
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(image, new Point(0, 0), "invisiblecursor");

		setCursor(transparentCursor);
	}
	/**
	 * <p>Adjusts the <code>point</code> to the grid if its enabled, and
	 *  sets <code>mapHandlerAdjustedPoint</code> with that new value.</p>
	 *
	 * <p>The value returned is the distance between those points: the original and
	 *  the adjusted one.</p>
	 *
	 * @param point point to adjust
	 * @param mapHandlerAdjustedPoint <code>point</code> adjusted
	 *
	 * @return distance from <code>point</code> to the adjusted one. If there is no
	 *  adjustment, returns <code>Double.MAX_VALUE</code>.
	 */


	private double adjustToHandler(Point2D point,
            Point2D mapHandlerAdjustedPoint) {

        if (!isRefentEnabled())
            return Double.MAX_VALUE;

        ArrayList snappers=new ArrayList(selected.keySet());
        ArrayList layersToSnap = getMapContext().getLayersToSnap();

        double mapTolerance = vp.toMapDistance(MapControl.tolerance);
        double minDist = mapTolerance;
        double middleTol=mapTolerance * 0.5;
        Point2D mapPoint = point;
        org.gvsig.fmap.geom.primitive.Envelope r;
		com.vividsolutions.jts.geom.Envelope e = null;
		try {
			r = geomManager.createEnvelope(mapPoint.getX() - middleTol,
					mapPoint.getY() - middleTol,
					mapPoint.getX() + middleTol,
					mapPoint.getY() + middleTol,
					SUBTYPES.GEOM2D);

			e = Converter.convertEnvelopeToJTS(r);
		} catch (CreateEnvelopeException e1) {
			logger.error("Error creating the envelope", e1);
		}

        usedSnap = null;
        Point2D lastPoint = null;
        if (previousPoint != null)
        {
            lastPoint = new Point2D.Double(previousPoint[0], previousPoint[1]);
        }
        for (int j = 0; j < layersToSnap.size(); j++)
        {
            FLyrVect lyrVect = (FLyrVect) layersToSnap.get(j);
            SpatialCache cache = lyrVect.getSpatialCache();
            if (lyrVect.isVisible())
            {
                // La lista de snappers está siempre ordenada por prioridad. Los de mayor
                // prioridad están primero.
                java.util.List geoms = cache.query(e);
                for (int n=0; n < geoms.size(); n++) {
                    Geometry geom = (Geometry) geoms.get(n);
                    for (int i = 0; i < snappers.size(); i++)
                    {
                        ISnapper theSnapper = (ISnapper) snappers.get(i);

                        if (usedSnap != null)
                        {
                            // Si ya tenemos un snap y es de alta prioridad, cogemos ese. (A no ser que en otra capa encontremos un snapper mejor)
                            if (theSnapper.getPriority() > usedSnap.getPriority())
                                break;
                        }
//                        SnappingVisitor snapVisitor = null;
                        Point2D theSnappedPoint = null;
                        if (theSnapper instanceof ISnapperVectorial)
                        {
                            theSnappedPoint = ((ISnapperVectorial) theSnapper).getSnapPoint(point, geom, mapTolerance, lastPoint);
                        }
                        if (theSnapper instanceof ISnapperRaster)
                        {
                            ISnapperRaster snapRaster = (ISnapperRaster) theSnapper;
                            theSnappedPoint = snapRaster.getSnapPoint(this, point, mapTolerance, lastPoint);
                        }


                        if (theSnappedPoint != null) {
                            double distAux = theSnappedPoint.distance(point);
                            if (minDist > distAux)
                            {
                                minDist = distAux;
                                usedSnap = theSnapper;
                                mapHandlerAdjustedPoint.setLocation(theSnappedPoint);
                            }
                        }
                    }
                } // for n
            } // visible
        }
        if (usedSnap != null)
            return minDist;
        return Double.MAX_VALUE;

    }
	/**
	 * Determines if snap tools are enabled or disabled.
	 *
	 * @return <code>true</code> to enable the snap tools; <code>false</code> to disable them
	 *
	 * @see #setRefentEnabled(boolean)
	 */
	public boolean isRefentEnabled()
	{
		return bRefent;
	}
	public void initializeSnapping() {
		ArrayList snappers=getMapContext().getSnappers();
        for (int n = 0; n < snappers.size(); n++) {
            ISnapper snp = (ISnapper) snappers.get(n);
            String nameClass=snp.getClass().getName();
            nameClass=nameClass.substring(nameClass.lastIndexOf('.'));
            boolean select = prefs.getBoolean("snapper_activated" + nameClass, false);
            if (select) {
				selected.put(snp, new Boolean(select));
			}
            int priority = prefs.getInt("snapper_priority" + nameClass,3);
            snp.setPriority(priority);
        }
//        applySnappers = prefs.getBoolean("apply-snappers",true);
//        snapConfig.setApplySnappers(applySnappers);
//        snapConfig.selectSnappers(selected);

    }
	/**
	 * <p>Tries to find the nearest geometry or grid control point by the position of the current snap tool.</p>
	 *
	 * <p>Prioritizes the grid control points than the geometries ones.</p>
	 *
	 * <p>If finds any near, stores the <i>map</i> and <i>pixel</i> coordinates for the snap, and enables
	 *  the <code>bForceCoord</code> attribute for the next draw of the mouse's cursor.</p>
	 *
	 * @param point current mouse 2D position
	 */
	public Point2D calculateSnapPoint(Point point) {
		// Se comprueba el ajuste a rejilla

		Point2D gridAdjustedPoint = vp.toMapPoint(
				point);
		double minDistance = Double.MAX_VALUE;
//		CADTool ct = (CADTool) cadToolStack.peek();
//		if (ct instanceof SelectionCADTool
//				&& ((SelectionCADTool) ct).getStatus().equals(
//						"Selection.FirstPoint")) {
//			mapAdjustedPoint = gridAdjustedPoint;
//			adjustedPoint = (Point2D) point.clone();
//		} else {

			minDistance = cadgrid.adjustToGrid(gridAdjustedPoint);
			if (minDistance < Double.MAX_VALUE) {
				adjustedPoint = vp.fromMapPoint(
						gridAdjustedPoint);
				mapAdjustedPoint = gridAdjustedPoint;
			} else {
				mapAdjustedPoint = null;
			}
//		}
		Point2D handlerAdjustedPoint = null;

		// Se comprueba el ajuste a los handlers
		if (mapAdjustedPoint != null) {
			handlerAdjustedPoint = (Point2D) mapAdjustedPoint.clone(); // getMapControl().getViewPort().toMapPoint(point);
		} else {
			handlerAdjustedPoint = vp.toMapPoint(
					point);
		}

		Point2D mapPoint = new Point2D.Double();
		double distance = adjustToHandler(handlerAdjustedPoint, mapPoint);

		if (distance < minDistance) {
			bForceCoord = true;
			adjustedPoint = vp.fromMapPoint(mapPoint);
			mapAdjustedPoint = mapPoint;
			minDistance = distance;
		}

		// Si no hay ajuste
		if (minDistance == Double.MAX_VALUE) {
			adjustedPoint = point;
			mapAdjustedPoint = null;
		}
		return mapAdjustedPoint;
	}
	/**
	 * Sets the snap tools enabled or disabled.
	 *
	 * @param activated <code>true</code> to enable the snap tools; <code>false</code> to disable them
	 *
	 * @see #isRefentEnabled()
	 */
	public void setRefentEnabled(boolean activated) {
		bRefent = activated;
	}

	public Grid getGrid() {
		return cadgrid;
	}

	public static HashMap getSelectedSnapppers() {
		return selected;
	}

	public void update(Observable observable, Object notification) {
		DataStoreNotification ddsn=(DataStoreNotification)notification;
		String type=ddsn.getType();
		if (type.equals(FeatureStoreNotification.AFTER_UNDO) ||
				type.equals(FeatureStoreNotification.AFTER_REDO)){
			repaint();
		}
	}
}
