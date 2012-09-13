package org.gvsig.gvsig3dgui;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.gvsig.cacheservice.CacheService;
import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.gui.Hud;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3d.map3d.layers.FLayers3D;
import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.gvsig3dgui.view.ViewProperties3D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.planets.exceptions.PlanetExtentException;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.CanvasViewer;
import org.gvsig.osgvp.viewer.DisplaySettings;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.ViewerFactory;
import org.gvsig.osgvp.viewer.ViewerStateListener;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.SingleLayerIterator;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.cit.gvsig.fmap.layers.layerOperations.AlphanumericData;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.table.ProjectTable;
import com.iver.cit.gvsig.project.documents.table.ProjectTableFactory;
import com.iver.cit.gvsig.project.documents.view.ProjectViewBase;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.utiles.IPersistence;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;

/**
 * Clase que representa una vista del proyecto
 * 
 * @author Fernando Gonz�lez Cort�s
 */
public class ProjectView3D extends ProjectViewBase {
	// public static int numViews = 0;

	private IViewerContainer m_canvas3d = null;

	private PlanetViewer m_planetViewer = null;

	private Planet m_planet = null;

	private ProjectCamera projectCamera = null;

	private int m_planetType = Planet.CoordinateSystemType.PROJECTED; // spheric
																		// or
																		// plane

	private Color backgroundColor;

	private float verticalExaggeration = 5;

	private FeatureFactory g;

	private View3D m_view = null;

	private static Preferences prefs = Preferences.userRoot().node(
			"gvsig.configuration.3D");

	private boolean prespectiveMode = true;

	private static Logger logger = Logger.getLogger(ProjectView3D.class
			.getName());

	// private Satelite runnable;

	public ProjectView3D() {
		super();
		backgroundColor = new Color(0, 0, 0, 255);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * @throws XMLException
	 * @throws SaveException
	 */
	public XMLEntity getXMLEntity() throws SaveException {
		XMLEntity xml = super.getXMLEntity();
		// xml.putProperty("nameClass", this.getClass().getName());
		try {
			// Persistiendo la posicion actual de la camara
			// TODO: remove this line where planet viewer will be test
			// if (m_canvas3d != null) {
			// projectCamera = new ProjectCamera();
			// projectCamera.setDescription("Camera");
			// projectCamera.setCamera(m_canvas3d.getOSGViewer().getCamera());
			//
			// xml.addChild(projectCamera.getXMLEntity());
			// }

			if (this.m_planetViewer != null) {
				projectCamera = new ProjectCamera();
				projectCamera.setDescription("Camera");
				projectCamera.setCamera(this.m_planetViewer.getCamera());

				xml.addChild(projectCamera.getXMLEntity());
			}

			int numViews = ((Integer) ProjectDocument.NUMS
					.get(ProjectView3DFactory.registerName)).intValue();
			xml.putProperty("numViews", numViews);
			xml.putProperty("m_selectedField", m_selectedField);
			xml.putProperty("m_typeLink", m_typeLink);
			xml.putProperty("m_extLink", m_extLink);

			// 3D properties
			xml.putProperty("planetType", m_planetType);
			xml.putProperty("verticalExaggeration", verticalExaggeration);

			xml.putProperty("backgroundColor", StringUtilities
					.color2String(backgroundColor));

			xml.addChild(mapContext.getXMLEntity());

			if (mapOverViewContext != null) {
				if (mapOverViewContext.getViewPort() != null) {
					xml.putProperty("mapOverView", true);
					xml.addChild(mapOverViewContext.getXMLEntity());
				} else {
					xml.putProperty("mapOverView", false);
				}
			} else {
				xml.putProperty("mapOverView", false);
			}
		} catch (Exception e) {
			throw new SaveException(e, this.getClass().getName());
		}

		return xml;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param xml
	 *            DOCUMENT ME!
	 * @param p
	 *            DOCUMENT ME!
	 * @throws XMLException
	 * @throws DriverException
	 * @see com.iver.cit.gvsig.project.documents.ProjectDocument#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity03(XMLEntity xml, Project p) throws XMLException {
		try {
			super.setXMLEntity03(xml);
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int numViews = xml.getIntProperty("numViews");
		ProjectDocument.NUMS.put(ProjectView3DFactory.registerName,
				new Integer(numViews));

		m_selectedField = xml.getStringProperty("m_selectedField");
		m_typeLink = xml.getIntProperty("m_typeLink");
		m_extLink = xml.getStringProperty("m_extLink");
		setMapContext(MapContext3D.createFromXML03(xml.getChild(0)));

		if (xml.getBooleanProperty("mapOverView")) {
			setMapOverViewContext(MapContext.createFromXML03(xml.getChild(1)));
		}

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param xml
	 *            DOCUMENT ME!
	 * @param p
	 *            DOCUMENT ME!
	 * @throws XMLException
	 * @throws OpenException
	 * 
	 * @see com.iver.cit.gvsig.project.documents.ProjectDocument#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException, OpenException {
		try {
			super.setXMLEntity(xml);
			int currentChild = 0;

			// Read last child
			int childNumber = currentChild;

			// Get camera
			XMLEntity xmlProp = xml.getChild(childNumber);
			if (xmlProp.contains("eyeX")) {
				try {
					String className = xmlProp.getStringProperty("className");
					Class classProp = Class.forName(className);
					Object obj = classProp.newInstance();
					IPersistence objPersist = (IPersistence) obj;
					objPersist.setXMLEntity(xmlProp);
					projectCamera = (ProjectCamera) obj;
				} catch (Exception e) {

				}
				currentChild++;
			}

			int numViews = xml.getIntProperty("numViews");
			ProjectDocument.NUMS.put(ProjectView3DFactory.registerName,
					new Integer(numViews));

			m_selectedField = xml.getStringProperty("m_selectedField");
			m_typeLink = xml.getIntProperty("m_typeLink");
			m_extLink = xml.getStringProperty("m_extLink");

			// 3D properties
			m_planetType = xml.getIntProperty("planetType");
			verticalExaggeration = xml.getFloatProperty("verticalExaggeration");

			backgroundColor = StringUtilities.string2Color((xml
					.getStringProperty("backgroundColor")));

			setMapContext(MapContext3D
					.createFromXML(xml.getChild(currentChild)));
			currentChild++;
			if (xml.getBooleanProperty("mapOverView")) {
				setMapOverViewContext(MapContext.createFromXML(xml
						.getChild(currentChild)));
				currentChild++;
			}
			getOrCreateView();
			showErrors();
		} catch (Exception e) {
			throw new OpenException(e, this.getClass().getName());
		}

	}

	public String getFrameName() {
		return PluginServices.getText(this, "Vista3D");
	}
	
	public IViewerContainer getOrCreateCanvas3D() {
		// create libJOSG objects
		if (m_canvas3d == null) {
			// AI2 objects
			try {
				m_planetViewer = new PlanetViewer();
			} catch (NodeException e1) {
			logger.error("Command: " + "Error creating planer viewer.", e1);
			}

			// Getting 3D preferences
			boolean compatibilitySwing = false;
			compatibilitySwing = prefs.getBoolean("compatibilitySwing",
					compatibilitySwing);

			if (compatibilitySwing) {
				m_canvas3d = ViewerFactory.getInstance()
						.createViewer(ViewerFactory.VIEWER_TYPE.JPANEL_VIEWER,
								m_planetViewer);
			} else {
				m_canvas3d = ViewerFactory.getInstance()
						.createViewer(ViewerFactory.VIEWER_TYPE.CANVAS_VIEWER,
								m_planetViewer);
			}

			this.configureProjectionViewMode();
			// m_canvas3d = ViewerFactory.getInstance().createViewer(
			// ViewerFactory.VIEWER_TYPE.CANVAS_VIEWER, m_planetViewer);
			m_canvas3d.addKeyListener(new ViewerStateListener(m_canvas3d
					.getOSGViewer()));

			// ViewerFactory.getInstance().startAnimator();
			
			//
			m_canvas3d.getOSGViewer().setDisplaySettings(new DisplaySettings());
			
			//
			m_planetViewer.setEnabledLighting(false);
			ViewerFactory.getInstance().startAnimator();

//			try {
//				((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//			} catch (ChildIndexOutOfBoundsExceptions e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
			
			// REPAINTING VIEWER
			if (m_canvas3d != null)
				m_canvas3d.repaint();
			
			if (getBackGroundColor() != null) {
				float r, g, b, a;
				r = ((float) backgroundColor.getRed()) / 255.0f;
				g = ((float) backgroundColor.getGreen()) / 255.0f;
				b = ((float) backgroundColor.getBlue()) / 255.0f;
				a = ((float) backgroundColor.getAlpha()) / 255.0f;
				m_planetViewer.setClearColor(r, g, b, a);
			}
			try {
				m_planet = new Planet();
			m_planet.setCoordinateSystemType(m_planetType);
			if (m_planetType == Planet.CoordinateSystemType.PROJECTED) {
				m_planet.setCoordinateSystemName("EPSG:23030");
				double extent[] = new double[4];
				extent[0] = CacheService.planeGlobalBounds.getMinX();
				extent[1] = CacheService.planeGlobalBounds.getMinY();
				extent[2] = CacheService.planeGlobalBounds.getMaxX();
				extent[3] = CacheService.planeGlobalBounds.getMaxY();
				m_planet.setExtent(extent);
			}
			} catch (NodeException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (PlanetExtentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// canvasListener lis = new canvasListener();
			// lis.setCanvas(m_canvas3d);
			// m_canvas3d.addKeyListener(lis);
			/*
			 * CODIGO DE PRUEBA PARA LA INCLUSION DE TEXTOS, POLIGONOS, LINEAS,
			 * ETC...
			 */

			double radiusEarth = m_planet.getRadiusEquatorial();
			g = new FeatureFactory(radiusEarth, m_planet);

			// g.setNodeName("VECTOR");
			// Group root = new Group();

			// root.addChild(g);
			// root.addChild(m_planet);
			// PositionAttitudeTransform nt = new PositionAttitudeTransform();
			// nt.addChild(m_canvas3d.readNodeFile("c:/modelos3d/spaceship.osg"));
			// nt.addChild(m_canvas3d.readNodeFile("c:/modelos3d/axes.osg"));
			// nt.addChild(m_canvas3d.readNodeFile("c:/modelos3d/cow.osg"));
			// nt.addChild(m_canvas3d.readNodeFile("c:/modelos3d/cessna.osg"));
			// nt.setPosition(new Vec3 (0,0,0) );
			// nt.setScale(new Vec3 (1,1,1));
			// m_canvas3d.addSpecialNode(nt);
			// m_canvas3d.addSpecialNode(m_canvas3d.readNodeFile("c:/modelos3d/axes.osg"));
			// m_canvas3d.addSpecialNode(m_canvas3d.readNodeFile("c:/modelos3d/spaceship.osg"));
			// m_canvas3d.addSpecialNode(m_canvas3d.readNodeFile("c:/modelos3d/cow.osg"));
			// root.addChild(m_canvas3d.readNodeFile("c:/modelos3d/cow.osg"));
			// root.addChild(m_canvas3d.readNodeFile("c:/modelos3d/cessna.osg"));
			// m_canvas3d.setSceneData(root);

			// float cenLat = (float) ((-1) *(40 * 2 * Math.PI)/360),
			// cenLon= (float) ( (-1) *(3 * 2* Math.PI)/360);
			//			 
			// PositionAttitudeTransform rot1 = new PositionAttitudeTransform();
			// PositionAttitudeTransform rot2 = new PositionAttitudeTransform();
			// // cargando la bandera
			// String name = ResourcesFactory.getResourcePath("flag.ive");
			// rot1.addChild(this.m_canvas3d.getOSGViewer().readNodeFile(name));
			//
			// rot1.setAttitude(cenLon, new Vec3(0.0, 1.0, 0.0));
			// rot1.setScale(new Vec3(600, 600, 600));
			//		
			//
			// rot2.addChild(rot1);
			// rot2.setAttitude(cenLat, new Vec3(1.0, 0.0, 0.0));
			// rot2.setPosition( new Vec3(0.0, 0.0, 0.0));
			//
			// root.addChild(rot2);
			/* ************************************************************************* */

			// m_planetViewer.addSpecialNode(g);
			// List posi = new ArrayList();
			// for (int i = 0; i < 10; i++) {
			// Vec3 pos1 = m_planet.convertLatLongHeightToXYZ(new Vec3(0, Math
			// .toRadians(i * 10), 0));
			//
			// // Vec3 pos1 = new Vec3(0, i * 10, 0);
			// posi.add(pos1);
			// }
			// Vec4 rgba = new Vec4(1.0, 1.0, 1.0, 1.0);
			// float lineWidth = 8;
			// g.addChild(FeatureFactory.insertLine(posi, rgba, lineWidth));
			// m_planetViewer.addSpecialNode(FeatureFactory.insertLine(posi,
			// rgba,
			// lineWidth));
			// m_planetViewer.addSpecialNode(osgDB.readNodeFile(ResourcesFactory
			// .getResourcesPath()
			// + "../images/flag.ive"));
			// m_canvas3d.getOSGViewer().addSpecialNode(root);
			// Cargando el satelite
			// m_canvas3d.addSpecialNode(sat);
			try {
				m_planetViewer.addPlanet(m_planet);
			} catch (NodeException e1) {
				logger.error("Command: " + "Error adding new child node.", e1);
			}

			Camera cam;
			cam = new Camera();
			if (this.projectCamera == null) {

				if (m_planetType == Planet.CoordinateSystemType.PROJECTED) {
					Rectangle2D e = m_planet.getExtent();
					// double difx = (e[1] - e[0]) / 2.0d;
					// double dify = (e[3] - e[2]) / 2.0d;
					// double posx = e[0] + difx;
					// double posy = e[2] + dify;

					double height;
					height = Math.sqrt(e.getWidth() * e.getWidth()
							+ e.getHeight() * e.getHeight()) * 10f;
					cam.setViewByLookAt((float) e.getCenterX(), (float) e
							.getCenterY(), (float) height, (float) e
							.getCenterX(), (float) e.getCenterY(), 0f, 0f, 1f,
							0f);
					cam.setViewByLookAt(0, 0, 5000000 * 4.6, 0, 0, 0, 0, 1, 0);
					m_planetViewer.getCustomTerrainManipulator()
							.setMinCameraDistance(10);
					m_planetViewer.getCustomTerrainManipulator()
							.setMaxCameraDistance(5000000 * 10);
					// m_planet.setSubdivisionFactor((float) 0.45);
				} else {
					// cam.setViewByLookAt(m_planet.getRadiusEquatorial() * 3.0,
					// 0, 0, 0, 0, 0, 0, 0, 1);
					cam.setViewByLookAt(m_planet.getRadiusEquatorial() * 8.0,
							0, 0, 0, 0, 0, 0, 0, 1);
					m_planetViewer.getCustomTerrainManipulator()
							.setMinCameraDistance(100);
					// m_planetViewer.getCustomTerrainManipulator()
					// .setMaxCameraDistance(
					// m_planet.getRadiusEquatorial() * 3.0);
					m_planetViewer.getCustomTerrainManipulator()
							.setMaxCameraDistance(
									m_planet.getRadiusEquatorial() * 10.0);
					// cam.setViewByLookAt(100 * 2.0,
					// 0, 0, 0, 0, 0, 0, 0, 1);
					// m_planet.setSubdivisionFactor((float) 0.65);
				}

			} else {
				cam = projectCamera.getCamera();
			}

			// m_canvas3d.getOSGViewer().setCameraManipulator(new
			// TrackballManipulator());

			m_planetViewer.setCamera(cam);

			Hud hud = new Hud(m_canvas3d, m_planet);
			try {
				m_planetViewer.addNodeToHUD(hud);
			} catch (NodeException e) {
				logger.error("Command: " + "Error adding node to hud.", e);
			}

			m_planetViewer.setEnabledLighting(true);
			
		}
		m_canvas3d.addKeyListener(new Key3DListener(this.m_planet));
		
		return m_canvas3d;
	}
	
	public View3D getOrCreateView() {
		if(m_view == null) {
			m_view = new View3D();

			m_view.setCanvas3d(getOrCreateCanvas3D());

			m_view.setPlanet(m_planet);
			m_view.setModel(this);	
			m_view.setNavMode(new NavigationMode(((PlanetViewer) m_planetViewer)
					.getCustomTerrainManipulator()));

			((MapContext3D) getMapContext())
					.setVerticalExageration(verticalExaggeration);
			setVerticalEx(verticalExaggeration);
		}
		return m_view;
	}

	public IWindow createWindow() {

		// View allready exits. Return it.
		if (m_view != null)
			return m_view;
		
		getOrCreateView();

		callCreateWindow(m_view);
		return m_view;
	}

	protected void finalize() throws Throwable {
		if (m_canvas3d != null) {
			// runnable.end();
			ViewerFactory.getInstance().stopAnimator();
			m_planet.dispose();
			m_planetViewer.dispose();
		}
	}

	public IWindow getProperties() {
		return new ViewProperties3D(this, false); // new ViewProperties(this);
		// TODO
	}

	public void exportToXML(XMLEntity root, Project project)
			throws SaveException {
		XMLEntity viewsRoot = project.getExportXMLTypeRootNode(root,
				ProjectViewFactory.registerName);
		viewsRoot.addChild(this.getXMLEntity());
		this.exportToXMLLayerDependencies(this.getMapContext().getLayers(),
				root, project);
		if (this.getMapOverViewContext() != null) {
			this.exportToXMLLayerDependencies(this.getMapOverViewContext()
					.getLayers(), root, project);
		}
	}

	private void exportToXMLLayerDependencies(FLayer layer, XMLEntity root,
			Project project) throws SaveException {

		if (layer instanceof FLayers) {
			FLayers layers = (FLayers) layer;
			for (int i = 0; i < layers.getLayersCount(); i++) {
				this.exportToXMLLayerDependencies(layers.getLayer(i), root,
						project);
			}
		} else {
			if (layer instanceof AlphanumericData) {

				try {
					project
							.exportToXMLDataSource(root,
									((AlphanumericData) layer).getRecordset()
											.getName());
				} catch (ReadDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ProjectTable pt = project.getTable((AlphanumericData) layer);
				if (pt != null) {
					pt.exportToXML(root, project);
				}
			}
		}
	}

	public void importFromXML(XMLEntity root, XMLEntity typeRoot,
			int elementIndex, Project project, boolean removeDocumentsFromRoot)
			throws XMLException, OpenException {
		XMLEntity element = typeRoot.getChild(elementIndex);

		this.setXMLEntity(element);
		project.addDocument(this);
		if (removeDocumentsFromRoot) {
			typeRoot.removeChild(elementIndex);
		}

		// Cargamos las tables vinculadas:

		// Recuperamos todos los nombres
		XMLEntity tablesRoot = project.getExportXMLTypeRootNode(root,
				ProjectTableFactory.registerName);
		int childIndex;
		XMLEntity child;
		// Lo hacemos en un map por si una vista se usa varias veces
		HashMap tablesName = new HashMap();
		Iterator iterTables = tablesRoot.findChildren("viewName", this
				.getName());
		while (iterTables.hasNext()) {
			child = (XMLEntity) iterTables.next();
			tablesName.put(child.getStringProperty("name"), child
					.getStringProperty("name"));
		}

		XMLEntity tableXML;

		// Construimos un diccionario ordenado inversamente por el indice
		// del elemento (por si se van eliminando elementos al importar) y
		// como valor el nombre de la vista
		TreeMap tablesToImport = new TreeMap(new Comparator() {

			public int compare(Object o1, Object o2) {

				if (((Integer) o1).intValue() > ((Integer) o2).intValue()) {
					return -1; // o1 first
				} else if (((Integer) o1).intValue() < ((Integer) o2)
						.intValue()) {
					return 1; // o1 second
				}
				return 0;
			}

		});
		Iterator iterTablesName = tablesName.keySet().iterator();
		int tableIndex;
		String tableName;
		while (iterTablesName.hasNext()) {
			tableName = (String) iterTablesName.next();
			tableIndex = tablesRoot.firstIndexOfChild("name", tableName);
			tablesToImport.put(new Integer(tableIndex), tableName);
		}

		ProjectTable table;
		ProjectDocumentFactory tableFactory = project
				.getProjectDocumentFactory(ProjectTableFactory.registerName);

		Iterator iterTablesToImport = tablesToImport.entrySet().iterator();
		Entry entry;
		// Nos recorremos las vistas a importar
		while (iterTablesToImport.hasNext()) {
			entry = (Entry) iterTablesToImport.next();
			tableName = (String) entry.getValue();
			tableIndex = ((Integer) entry.getKey()).intValue();
			table = (ProjectTable) tableFactory.create(project);
			try {
				table.importFromXML(root, tablesRoot, tableIndex, project,
						removeDocumentsFromRoot);
			} catch (ReadDriverException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int getPlanetType() {
		return m_planetType;
	}

	public void setPlanetType(int type) {
		m_planetType = type;
	}

	public float getVerticalExaggeration() {
		return verticalExaggeration;
	}

	public void setVerticalExaggeration(float exaggeration) {
		verticalExaggeration = exaggeration;
	}

	public Color getBackGroundColor() {
		return backgroundColor;
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.backgroundColor = backGroundColor;
		if ((this.backgroundColor != null) && (m_planetViewer != null)) {
			float r, g, b, a;
			r = ((float) backgroundColor.getRed()) / 255.0f;
			g = ((float) backgroundColor.getGreen()) / 255.0f;
			b = ((float) backgroundColor.getBlue()) / 255.0f;
			a = ((float) backgroundColor.getAlpha()) / 255.0f;
			
			m_planetViewer.setClearColor(r, g, b, a);
		}
	}

	/**
	 * Method to insert text points in a position with a especific text
	 * 
	 * @param nombre
	 *            The text
	 * @param posicion
	 *            the position
	 */
	public void insertaText(String nombre, Vec3 posicion) {
		// Group root = (Group) m_canvas3d.getSceneData();
		//		
		// Group vector = (Group) root.getChild(0);
		//
		// if (vector.getNodeName(null).equals("VECTOR")) {
		// VectorTest vaux = (VectorTest) vector;
		// System.out.println("Numero de hijos " + vaux.getNumChildren());
		// vaux.insertText(nombre, posicion);
		// // vaux.insertPoint(posicion, new Vec4(1.0,1.0,1.0,1.0));
		// System.out.println("Numero de hijos despues "
		// + vaux.getNumChildren());
		// }

		// g.insertText(nombre, posicion);

		// System.out.println("Nombre del nodo leido " +
		// vector.getNodeName(null)) ;

	}

	public Planet getPlanet() {
		return m_planet;
	}

	public void setPlanet(Planet m_planet) {
		this.m_planet = m_planet;
	}

	public IViewerContainer getCanvas3d() {
		return m_canvas3d;
	}

	public void setCanvas3d(CanvasViewer m_canvas3d) {
		this.m_canvas3d = m_canvas3d;
	}

	private void setVerticalEx(float exa) {

		SingleLayerIterator lyrIterator = new SingleLayerIterator(
				(FLayers3D) getMapContext().getLayers());
		while (lyrIterator.hasNext()) {
			FLayer lyr = lyrIterator.next();

			Layer3DProps props = Layer3DProps.getLayer3DProps(lyr);
			int type = props.getType();
			if (exa != props.getVerticalEx()) {
				if (type == Layer3DProps.layer3DElevation) {
					int order = props.getPlanetOrder();
					props.setVerticalEx(exa);
					m_planet.setVerticalExaggerationLayer(order, props
							.getVerticalEx());
				} else if (type == Layer3DProps.layer3DVector) {
					int order = props.getPlanetOrder();
					props.setVerticalEx(exa);
				}
			}

		}
	}

	public boolean isPrespectiveModeActive() {
		return prespectiveMode;
	}

	/**
	 * Active the prespective mode. If we disable the prespective mode, the
	 * ortho mode will be active
	 * 
	 * @param prespectiveMode
	 *            enable/disable
	 */
	public void setActivePrespectiveMode(boolean prespectiveMode) {
		this.prespectiveMode = prespectiveMode;
	}

	private void configureProjectionViewMode() {
		if (prespectiveMode) {
			System.out.println("Setting up prespective projection");
			// this.m_canvas3d.getOSGViewer().setProjectionMatrixAsPerspective(
			// arg0, arg1, arg2, arg3);
		} else {
			System.out.println("Setting up prespective projection");
			this.m_canvas3d.getOSGViewer().setProjectionMatrixAsOrtho(-10, 10,
					-10, 10, 1, 1000);
		}
	}

	public View3D getView() {
		return m_view;
	}

	public void setView(View3D m_view) {
		this.m_view = m_view;
	}

}
