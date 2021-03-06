/* Generated by Together */

package org.gvsig.gvsig3dgui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.cresques.cts.IProjection;
import org.gvsig.gvsig3d.listener.canvasListener;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3d.map3d.MapControl3D;
import org.gvsig.gvsig3d.map3d.ViewPort3D;
import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.gvsig3dgui.ProjectView3D;
import org.gvsig.gvsig3dgui.behavior.PolylineBehavior3D;
import org.gvsig.gvsig3dgui.behavior.RectangleBehavior3D;
import org.gvsig.gvsig3dgui.listener.PointSelectListerner3D;
import org.gvsig.gvsig3dgui.listener.PolylineDrawListener3D;
import org.gvsig.gvsig3dgui.listener.RectangleSelectListener3D;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.RequestLayerListener;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.DisplaySettings;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.OSGViewer;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.ColorEvent;
import com.iver.cit.gvsig.fmap.ExtentEvent;
import com.iver.cit.gvsig.fmap.ProjectionEvent;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.ViewPortListener;
import com.iver.cit.gvsig.fmap.MapControl.MapToolListener;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior;
import com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener;
import com.iver.cit.gvsig.project.documents.view.MapOverview;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.gui.TOC;
import com.iver.cit.gvsig.project.documents.view.toolListeners.InfoListener;

public class View3D extends BaseView {

	private static final long serialVersionUID = 1L;

	private IViewerContainer m_canvas3d;

	private Planet m_planet;

	private FLayers m_layers;

	private PolylineBehavior3D _polybehavior= null;
	
	private UpdateView3D updateViewThread;

	private NavigationMode navMode = null;

	private String buttonPath;

	public View3D() {
		this.setName("View3D");
		
	}

	public DisplaySettings getDisplaySettings() {

		return ((OSGViewer) getCanvas3d().getOSGViewer()).getDisplaySettings();

	}

	public void setModel(ProjectView3D model) {
		this.modelo = model;

		initComponents();

		// Se registra como listener de cambios en FMap
		MapContext3D fmap = (MapContext3D) modelo.getMapContext();

		m_layers = fmap.getLayers();
		/*
		 * for (int i=0;i<layers.getLayersCount();i++){ if
		 * (layers.getLayer(i).isEditing() && layers.getLayer(i) instanceof
		 * FLyrVect){ this.showConsole(); } }
		 */

		// Make layers listen to tile requests
		m_planet.setRequestLayerListener((RequestLayerListener) fmap);
		// .getTileListener();
		// tileManager.setTileCreatedListener((TileCreatedListener) fmap);
		// tileManager.setRequestLayerListener((RequestLayerListener) fmap);

		// set projection for 3D view
		IProjection projection = null;
		ViewPort vp = null;
		Rectangle2D extend;
		if (m_planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC) {
			projection = CRSFactory.getCRS("EPSG:4326");
			extend = new Rectangle2D.Double(-180.0, 90.0, 180.0, -90.0);
		} else {
			if ((fmap != null) && (fmap.getProjection() != null))
				projection = fmap.getProjection();
			else
				projection = CRSFactory.getCRS("EPSG:23030"); // test
			// projection = CRSFactory.getCRS("EPSG:4326"); // test
			// extend = new Rectangle2D.Double(-20000000.0, 10000000.0,
			// 40000000.0, 20000000.0);
			extend = new Rectangle2D.Double(20000000.0, 10000000.0,
					-20000000.0, -10000000.0);
		}
		vp = new ViewPort3D(projection);
		vp.setExtent(extend);
		fmap.setViewPort(vp);
		((ViewPort3D) vp).setPlanet(this.m_planet);

		// Add ViewPort3D listener to canvas3D. to refresh locator.
		m_canvas3d.addMouseListener((ViewPort3D) vp);

		// Pass JOSG objects to MapContext3D
		fmap.setViewer(m_canvas3d);
		fmap.setViewProjection(projection);
		fmap.setPlanet(m_planet);

		// Se configura el mapControl

		canvasListener lis = new canvasListener();
		lis.setPlanet(m_planet);
		lis.setCanvas(m_canvas3d);
		m_canvas3d.addKeyListener(lis);
		m_canvas3d.addMouseListener(lis);

		MapToolListener mapToolListener = m_MapControl.getMapToolListener();
		m_canvas3d.addMouseListener(mapToolListener);
		m_canvas3d.addMouseMotionListener(mapToolListener);
		m_canvas3d.addMouseWheelListener(mapToolListener);

		m_MapControl.setMapContext(fmap);
		m_TOC.setMapContext(fmap);
		m_MapControl.setBackground(new Color(255, 255, 255));
		if (modelo.getMapOverViewContext() != null) {
			m_MapLoc.setModel(modelo.getMapOverViewContext());
		}
		model.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("name")) {
					PluginServices.getMDIManager().getWindowInfo(View3D.this)
							.setTitle(
									PluginServices.getText(this, "Vista3D")
											+ " : "
											+ (String) evt.getNewValue());
				}
			}
		});
		if (m_MapControl.getViewPort() != null) {
			m_MapControl.getViewPort().addViewPortListener(
					new ViewPortListener() {
						public void extentChanged(ExtentEvent e) {
							/*
							 * if (PluginServices.getMainFrame() != null){
							 * PluginServices.getMainFrame().getStatusBar().setControlValue("scale",String.valueOf(m_MapControl.getMapContext().getScaleView()));
							 * PluginServices.getMainFrame().getStatusBar().setMessage("projection",
							 * getMapControl().getViewPort().getProjection().getAbrev()); }
							 */
						}

						public void backColorChanged(ColorEvent e) {
						}

						public void projectionChanged(ProjectionEvent e) {
							m_MapLoc.setProjection(e.getNewProjection());
						}
					});
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	// FALTA CONVERTIR QUE CANVAS3D HAGA EL PAPEL DE m_MapControl
	protected void initComponents() { // GEN-BEGIN:initComponents
		m_MapControl = new MapControl3D(this); // Default is paintEnabled =
												// false.
		// Remember to activate it
		m_MapControl.addExceptionListener(mapControlExceptionListener);
		// modelo.setMapContext(m_MapControl.getMapContext());

		// m_MapControl.setAcceptEvents(true); Lo comento porque ya se hace en
		// el setModel
		m_TOC = new TOC();

		// Ponemos el localizador
		m_MapLoc = new MapOverview(m_MapControl);
		m_MapLoc.setPreferredSize(new Dimension(150, 200));
		removeAll();
		tempMainSplit = new ViewSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		tempMainSplit.setPreferredSize(new Dimension(500, 300));
		if (!isPalette()) {
			JSplitPane tempSplitToc = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			tempSplitToc.setTopComponent(m_TOC);
			tempSplitToc.setBottomComponent(m_MapLoc);
			tempSplitToc.setResizeWeight(0.7);
			tempMainSplit.setLeftComponent(tempSplitToc);
		} else {
			tempMainSplit.setLeftComponent(m_TOC);
		}
		m_TOC.setVisible(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add((Component) m_canvas3d /* m_MapControl */);
		tempMainSplit.setRightComponent(panel);
		panel.setMinimumSize(new Dimension(100, 100));
		// split.setBottomComponent(getConsolePanel());
		// split.setTopComponent(tempMainSplit);
		// split.setResizeWeight(0.9);
		this.setLayout(new BorderLayout());
		this.add(tempMainSplit, BorderLayout.CENTER);

		// default cursor

		String oldPath = ResourcesFactory.getExtPath();// Save the path.
		ResourcesFactory
				.setExtPath("/gvSIG/extensiones/com.iver.ai2.gvsig3dgui/images/");// my
																					// new
																					// path
		buttonPath = ResourcesFactory.getResourcesPath();
		ResourcesFactory.setExtPath(oldPath);// Restore the old path.
		System.out.println(oldPath);
		System.out.println(buttonPath);

		URL path;
		path = this.getClass().getClassLoader().getResource("images/");
		buttonPath = path.getPath();

		Image navCursorImage = new ImageIcon(buttonPath
				+ "/NavigationCursor.gif").getImage();

		// Image navCursorImage = new ImageIcon(this.getClass().getResource(
		// "images/NavigationCursor.gif")).getImage();
		Cursor navCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				navCursorImage, new Point(16, 16), "");
		((Component) m_canvas3d).setCursor(navCursor);

		// hideConsole();

		// //Listener de eventos de movimiento que pone las coordenadas del
		// rat�n en la barra de estado
		// StatusBarListener sbl = new StatusBarListener(m_MapControl);
		//
		// // Zoom out (pinchas y el mapa se centra y te muestra m�s).
		// // No es dibujando un rect�ngulo, es solo pinchando.
		//
		// ZoomOutListener zol = new ZoomOutListener(m_MapControl);
		// m_MapControl.addMapTool("zoomOut", new Behavior[]{new
		// PointBehavior(zol), new MouseMovementBehavior(sbl)});
		//
		// // pan
		//
		// PanListener pl = new PanListener(m_MapControl);
		// m_MapControl.addMapTool("pan", new Behavior[]{new MoveBehavior(pl),
		// new MouseMovementBehavior(sbl)});
		//
		// // Medir
		//
		// MeasureListener mli = new MeasureListener(m_MapControl);
		// m_MapControl.addMapTool("medicion", new Behavior[]{new
		// PolylineBehavior(mli), new MouseMovementBehavior(sbl)});
		//
		// // Area
		//
		// AreaListener ali = new AreaListener(m_MapControl);
		// m_MapControl.addMapTool("area", new Behavior[]{new
		// PolygonBehavior(ali), new MouseMovementBehavior(sbl)});
		//
		// Seleccion por punto

		PointSelectListerner3D psl = new PointSelectListerner3D(m_MapControl);
		m_MapControl.addMapTool("pointSelection",
				new Behavior[] { new PointBehavior(psl) });
		//
		// Info por punto
		InfoListener il = new InfoListener(m_MapControl);
		m_MapControl.addMapTool("info",
				new Behavior[] { new PointBehavior(il) });
		// new MouseMovementBehavior(sbl)});
		//
		// // Link por punto
		// LinkListener ll = new LinkListener(m_MapControl);
		// m_MapControl.addMapTool("link", new Behavior[]{new PointBehavior(ll),
		// new MouseMovementBehavior(sbl)});
		//
		// Selecci�n por rect�ngulo
		RectangleSelectListener3D rsl = new RectangleSelectListener3D(
				m_MapControl);
		m_MapControl.addMapTool("rectSelection",
				new Behavior[] { new RectangleBehavior3D(rsl) });
		//
		// //Selecci�n por pol�gono
		PolylineDrawListener3D polylis = new PolylineDrawListener3D(
				m_MapControl);
		
		_polybehavior= new PolylineBehavior3D(polylis);
		
		m_MapControl.addMapTool("interactivePolyline",
				new Behavior[] {_polybehavior });
		
		
		
		// PolygonSelectListener poligSel = new
		// PolygonSelectListener(m_MapControl);
		// m_MapControl.addMapTool("polSelection", new Behavior[]{new
		// PolygonBehavior(poligSel), new MouseMovementBehavior(sbl)});
		//
		// // Zoom por rect�ngulo
		// ZoomOutRightButtonListener zoil = new
		// ZoomOutRightButtonListener(m_MapControl);
		// ZoomInListener zil = new ZoomInListener(m_MapControl);
		// m_MapControl.addMapTool("zoomIn", new Behavior[]{new
		// RectangleBehavior(zil),
		// new PointBehavior(zoil), new MouseMovementBehavior(sbl)});
		//
		// SelectImageListener sil = new SelectImageListener(m_MapControl);
		// m_MapControl.addMapTool("selectImage", new Behavior[]{
		// new PointBehavior(sil), new MouseMovementBehavior(sbl)});
		//
		// ZoomPixelCursorListener zp = new
		// ZoomPixelCursorListener(m_MapControl);
		// m_MapControl.addMapTool("zoom_pixel_cursor", new Behavior[]{new
		// PointBehavior(zp), new MouseMovementBehavior(sbl)});
		//
		// m_MapControl.setTool("zoomIn"); // Por defecto
		// m_MapControl.setPaintEnabled(true);
	}

	public WindowInfo getWindowInfo() {
		if (m_viewInfo == null) {
			m_viewInfo = new WindowInfo(WindowInfo.ICONIFIABLE
					| WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);

			m_viewInfo.setWidth(500);
			m_viewInfo.setHeight(300);
			m_viewInfo.setTitle(PluginServices.getText(this, "Vista3D") + " : "
					+ modelo.getName());
		}
		return m_viewInfo;
	}

	public IViewerContainer getCanvas3d() {
		return m_canvas3d;
	}

	public void setCanvas3d(IViewerContainer canvas3d) {
		m_canvas3d = canvas3d;

		// Launching slider update
		// Create the object with the run() method
		updateViewThread = new UpdateView3D(30000);

		// Create the thread supplying it with the runnable object
		Thread thread = new Thread(updateViewThread);

		// Start the thread
		thread.start();
	}

	public Planet getPlanet() {
		return m_planet;
	}
	public PolylineBehavior3D getPolylineBehavior(){
		return _polybehavior;
	}
	public void setPlanet(Planet planet) {
		if (m_canvas3d == null)
			return;
		m_planet = planet;

		// Preguntar a rafa si cuando se hace un setSceneData se cuelga un nodo
		// nuevo o se destruye el grafo entero y se crea de nuevo
		// m_canvas3d.setSceneData(m_planet);
	}

	public void windowActivated() {
		m_canvas3d.repaint();

	}

	public void windowClosed() {
		super.windowClosed();
	}

	public void repaint() {
		if (m_canvas3d != null)
			m_canvas3d.repaint();
		super.repaint();
	}

	public Camera getCamera() {
		return getCanvas3d().getOSGViewer().getCamera();
	}

	public void setCamera(Camera camera) {
		getCanvas3d().getOSGViewer().setCamera(camera);
	}

	/**
	 * Class to repaint canvas 3D. This class implements runnable and contructor
	 * with time parameter.
	 * 
	 * @author julio
	 * 
	 */
	public class UpdateView3D implements Runnable {

		private boolean finish = false;

		private long time;

		public UpdateView3D(long time) {
			this.time = time;
		}

		// This method is called when the thread runs
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
					synchronized (this) {
						if (finish) {
							break;
						}
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				// Repainting canvas
				m_canvas3d.repaint();
			}
		}

		public synchronized void end() {
			finish = true;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

	}

	public NavigationMode getNavMode() {
		return navMode;
	}

	public void setNavMode(NavigationMode navMode) {
		this.navMode = navMode;
	}

	public Object getWindowProfile() {
		return WindowInfo.EDITOR_PROFILE;
	}
}
