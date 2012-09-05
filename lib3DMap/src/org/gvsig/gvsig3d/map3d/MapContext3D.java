package org.gvsig.gvsig3d.map3d;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.cresques.cts.IProjection;
import org.gvsig.cacheservice.TileNum;
import org.gvsig.gvsig3d.cacheservices.FLayerCacheService;
import org.gvsig.gvsig3d.cacheservices.OSGCacheService;
import org.gvsig.gvsig3d.cacheservices.VectorCacheService;
import org.gvsig.gvsig3d.drivers.GvsigDriverOSG;
import org.gvsig.gvsig3d.map3d.layers.FLayers3D;
import org.gvsig.osgvp.core.osg.Image;
import org.gvsig.osgvp.core.osg.Matrix;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.exceptions.image.ImageConversionException;
import org.gvsig.osgvp.exceptions.node.ChildIndexOutOfBoundsExceptions;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.planets.RequestLayerEvent;
import org.gvsig.osgvp.planets.RequestLayerListener;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.PrintUtilities;

import com.hardcode.driverManager.Driver;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.fmap.layers.GraphicLayer;
import com.iver.cit.gvsig.fmap.layers.LayerEvent;
import com.iver.cit.gvsig.fmap.layers.LayerListener;
import com.iver.cit.gvsig.fmap.layers.LegendChangedEvent;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.SelectionEvent;
import com.iver.cit.gvsig.fmap.layers.SelectionListener;
import com.iver.cit.gvsig.fmap.layers.SelectionSupport;
import com.iver.cit.gvsig.fmap.layers.SingleLayerIterator;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable;
import com.iver.cit.gvsig.fmap.rendering.LegendListener;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.threads.Cancellable;

public class MapContext3D extends MapContext implements RequestLayerListener,
		LayerListener, LegendListener, SelectionListener {

	// JOSG library objects
	private Planet m_planet;

	private IViewerContainer m_canvas3d;

	private PlanetViewer planetViewer;

	// separate lists for image, elevation and vector layers
	private ArrayList m_layerLists = new ArrayList();

	private IProjection m_viewProjection;

	private float verticalExageration;

	private boolean m_bEmptyView = true;

	private boolean m_bListenToLegend = true;

	private boolean m_bLoading = false;

	private FLayers lyrs;

	private PlanetViewer canvasoff = null;

	private PlanetViewer canvasPrint;

	private boolean renewCanvasOff = false;

	private static Logger logger = Logger.getLogger(MapContext3D.class
			.getName());

	PlanetViewer printViewer = null;

	private boolean visibilityChange = false;

	@Override
	public void draw(BufferedImage image, Graphics2D g, Cancellable cancel,
			double scale) throws ReadDriverException {

		// Rectangle r = new Rectangle((int) getViewPort().getOffset().getX(),
		// (int) getViewPort().getOffset().getY(), image.getWidth(), image
		// .getHeight());

		double x = getViewPort().getOffset().getX();
		double y = getViewPort().getOffset().getY();
		double w = image.getWidth();
		double h = image.getHeight();

		// double x = r.getMinX();
		// double y = r.getMinY();
		// double w = r.getWidth();
		// double h = r.getHeight();

		// if ((canvasoff == null) || (isRenewCanvasOff())) {
		if (canvasoff == null) {
			try {
				canvasoff = new PlanetViewer();
				canvasoff.setUpViewerInBackground(0, 0, 512, 512);
				canvasoff.addPlanet(((PlanetViewer) m_canvas3d.getOSGViewer())
						.getPlanet(0));
				canvasoff.addSpecialNode((((PlanetViewer) m_canvas3d
						.getOSGViewer()).getSpecialNodes()));
				Vec4 color = ((PlanetViewer) m_canvas3d.getOSGViewer())
						.getClearColor();
				canvasoff.setClearColor(color.x(), color.y(), color.z(), color
						.w());
				this.setRenewCanvasOff(false);
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Camera refCam = m_canvas3d.getOSGViewer().getCamera();
		Matrix refView = refCam.getViewMatrix();
		Matrix refProy = refCam.getProjectionMatrix();
		Matrix.Perspective pers = refProy.getPerspective();
		Camera viewCam = new Camera();
		viewCam.setProjectionMatrixAsPerspective(pers.fovy, w / h, pers.zNear,
				pers.zFar);
		viewCam.setViewMatrix(refView);
		// canvasoff.setSceneData(m_canvas3d.getOSGViewer().getSceneData());
		canvasoff.setViewport(0, 0, (int) w, (int) h);
		canvasoff.setCamera(viewCam);
		// System.err.println("w: " +w);
		// System.err.println("h: " +h);
		canvasoff.takeScreenshotToMemory();
		canvasoff.frame();
		// canvasoff.setPolygonMode(OSGViewer.PolygonModeType.GL_LINE);

		Image OSGimage = canvasoff.getScreenshotImage();

		BufferedImage img = null;
		try {
			img = OSGimage.getBufferedImage();
		} catch (ImageConversionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		double scalex = w / img.getWidth(null);
		double scaley = h / img.getHeight(null);
		try {
			AffineTransform xform = AffineTransform.getScaleInstance(scalex,
					scaley);
			AffineTransform xpos = AffineTransform.getTranslateInstance(x, y);
			xpos.concatenate(xform);
			g.drawRenderedImage(img, xpos);
			// g.drawRenderedImage(img, new AffineTransform());
		} catch (ImagingOpException e) {
			NotificationManager.addError("Dibujando FFramePicture", e);
		}

	}

	public void print(Graphics2D g, double scale, PrintRequestAttributeSet arg2)
			throws ReadDriverException {

		int x = (int) getViewPort().getOffset().getX();
		int y = (int) getViewPort().getOffset().getY();
		int w = (int) g.getClipBounds().getWidth();
		int h = (int) g.getClipBounds().getHeight();
		System.err.println("x " + x + "; y " + y + "; w " + w + "; h" + h);

		Camera viewerCam = m_canvas3d.getOSGViewer().getCamera();
		// System.out.println("projmat"
		// + viewerCam.getProjectionMatrix().toString());
		BufferedImage s = null;

		try {
			int minw = (int) w / 10;
			int minh = (int) h / 10;
			System.out.println("minw" + minw + " minh" + minh);

			// if (printViewer == null) {
			printViewer = new PlanetViewer();

			printViewer.addPlanet(((PlanetViewer) m_canvas3d.getOSGViewer())
					.getPlanet(0));
			printViewer.addSpecialNode((((PlanetViewer) m_canvas3d
					.getOSGViewer()).getSpecialNodes()));
			Vec4 color = ((PlanetViewer) m_canvas3d.getOSGViewer())
					.getClearColor();
			printViewer.setClearColor(color.x(), color.y(), color.z(), color
					.w());
			// }
			printViewer.setUpViewerInBackground(0, 0, minw, minh);
			// printViewer.addPlanet(((PlanetViewer) m_canvas3d.getOSGViewer())
			// .getPlanet(0));

			// printViewer.setSceneData(((PlanetViewer)
			// m_canvas3d.getOSGViewer())
			// .getScene());

			// printViewer.setPolygonMode(OSGViewer.PolygonModeType.GL_LINE);

			PrintUtilities util = new PrintUtilities();
			util.setViewer(printViewer);
			s = util.getHighResolutionImage(viewerCam, w, h);

			RenderedImage render = s;
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		double scalex = w / s.getWidth(null);
		double scaley = h / s.getHeight(null);
		try {
			AffineTransform xform = AffineTransform.getScaleInstance(scalex,
					scaley);
			AffineTransform xpos = AffineTransform.getTranslateInstance(x, y);
			xpos.concatenate(xform);
			g.drawRenderedImage(s, xpos);
			// g.drawRenderedImage(img, new AffineTransform());
		} catch (ImagingOpException e) {
			NotificationManager.addError("Dibujando FFramePicture", e);
		}
		printViewer.releaseGLContext();
		printViewer.dispose();
		System.gc();
	}

	public MapContext3D(ViewPort vp) {
		super(vp);

		ArrayList textureLayers = new ArrayList();
		ArrayList elevationLayers = new ArrayList();
		ArrayList vectorLayers = new ArrayList();
		ArrayList OSGLayers = new ArrayList();
		m_layerLists.add(textureLayers);
		m_layerLists.add(elevationLayers);
		m_layerLists.add(vectorLayers);
		m_layerLists.add(OSGLayers);

		// extentAux = new Rectangle2D.Double(-180.0, 90.0, 180.0, -90.0);
	}

	public MapContext3D(FLayers fLayers, ViewPort vp) {
		super(fLayers, vp);
		ArrayList textureLayers = new ArrayList();
		ArrayList elevationLayers = new ArrayList();
		ArrayList vectorLayers = new ArrayList();
		ArrayList OSGLayers = new ArrayList();
		m_layerLists.add(textureLayers);
		m_layerLists.add(elevationLayers);
		m_layerLists.add(vectorLayers);
		m_layerLists.add(OSGLayers);

		// extentAux = new Rectangle2D.Double(-180.0, 90.0, 180.0, -90.0);
	}

	public void setPlanet(Planet planet) {
		if (planet == m_planet)
			return;

		m_planet = planet;

		// add layers to planet
		addCurrentLayersToPlanet(m_planet);
//		for (int iType = 0; iType < 4; iType++) {
//			ArrayList layerList = (ArrayList) m_layerLists.get(iType);
//			for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {
//
//				FLayer layer = (FLayer) layerList.get(iLayer);
//				Layer3DProps props = getLayer3DProps(layer);
//				props.setPlanetOrder(props.getTocOrder());
//				addLayerToPlanet(m_planet, layer, props.getPlanetOrder(), false);
//
//				// Layer active or not
//				if (props.getType() == Layer3DProps.layer3DImage) {
//					m_planet.setEnabledTextureLayer(props.getPlanetOrder(),
//							layer.isVisible());
//				}
//				if (props.getType() == Layer3DProps.layer3DElevation) {
//					m_planet.setEnabledHeightfieldLayer(0, layer.isVisible());
//				}
//			}
//			if (iType == Layer3DProps.layer3DElevation && layerList.size() > 0)
//				m_planet.invalidateHeightfieldLayer(0);
//		}
	}

	public Planet getPlanet() {
		return m_planet;
	}

	public void setViewer(IViewerContainer canvas) {
		m_canvas3d = canvas;
		this.planetViewer = (PlanetViewer) m_canvas3d.getOSGViewer();
	}

	public IProjection getViewProjection() {
		return m_viewProjection;
	}

	public void setViewProjection(IProjection projection) {
		m_viewProjection = projection;
	}

	public IViewerContainer getCanvas3d() {
		return m_canvas3d;
	}

	// public void setCanvas3d(IViewerContainer m_canvas3d) {
	// this.m_canvas3d = m_canvas3d;
	//		
	// }

	public float getVerticalExageration() {
		return verticalExageration;
	}

	public void setVerticalExageration(float verticalExageration) {
		this.verticalExageration = verticalExageration;
	}

	public void setLoading(boolean bLoading) {
		m_bLoading = bLoading;
	}

	public FLayers getNewGroupLayer(FLayers parent) {
		return new FLayers3D(this, parent, getViewPort());
	}

	/** * LAYER CHANGES called by FLayers3D ** */

	public void layerMoved(FLayers3D parent, FLayer layer, int oldPos,
			int newPos) {

		if (layer instanceof FLayers) {
			FLayers group = (FLayers) layer;
			if (newPos > oldPos) {
				for (int iChild = group.getLayersCount() - 1; iChild >= 0; iChild--)
					layerMoved((FLayers3D) group, group.getLayer(iChild),
							oldPos, newPos);
			} else {
				for (int iChild = 0; iChild < group.getLayersCount(); iChild++)
					layerMoved((FLayers3D) group, group.getLayer(iChild),
							oldPos, newPos);
			}
			return;
		}

		Layer3DProps props3D = getLayer3DProps(layer);
		if (props3D.getType() != Layer3DProps.layer3DImage)
			return;

		refreshLayerOrder();

		ArrayList layerList = (ArrayList) m_layerLists.get(props3D.getType());

		// This will be more complex when we have multiple planets
		int currentPlanetOrder = props3D.getPlanetOrder();
		int newPlanetOrder = props3D.getTocOrder();
		if (currentPlanetOrder != newPlanetOrder) {
			m_planet.reorderTextureLayer(currentPlanetOrder, newPlanetOrder);
			// update planet order for all layers
			for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {
				FLayer eachLayer = (FLayer) layerList.get(iLayer);
				Layer3DProps eachProps3D = getLayer3DProps(eachLayer);
				int currentPlanetOrderI = eachProps3D.getPlanetOrder();
				if (currentPlanetOrder < newPlanetOrder) { // layer moves 'up'
					if (currentPlanetOrderI > currentPlanetOrder
							&& currentPlanetOrderI <= newPlanetOrder)
						eachProps3D.setPlanetOrder(currentPlanetOrderI - 1);
				} else { // layer moves 'down'
					if (currentPlanetOrderI < currentPlanetOrder
							&& currentPlanetOrderI >= newPlanetOrder)
						eachProps3D.setPlanetOrder(currentPlanetOrderI + 1);
				}
			}
			props3D.setPlanetOrder(newPlanetOrder);
		}

		// reorder all which may have changed in internal layer list
		ArrayList sortedLayers = new ArrayList(layerList.size());
		for (int iLayer = 0; iLayer < layerList.size(); iLayer++)
			sortedLayers.add(null);

		for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {
			FLayer eachLayer = (FLayer) layerList.get(iLayer);
			Layer3DProps eachProps3D = getLayer3DProps(eachLayer);
			int newOrder = eachProps3D.getTocOrder();
			sortedLayers.set(newOrder, eachLayer);
		}
		m_layerLists.set(props3D.getType(), sortedLayers);

		PrintDebugLayers();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();
	}

	public void layerAdded(FLayers3D parent, FLayer layer) {

		// to add group layers to 3D, just add recursively child data layers
		if (layer instanceof FLayers) {
			FLayers group = (FLayers) layer;
			for (int iChild = 0; iChild < group.getLayersCount(); iChild++) {
				layerAdded((FLayers3D) group, group.getLayer(iChild));
			}
			getLayer3DProps(layer).setHooked(true);
			return;
		}

		if (layer instanceof Classifiable) {
			Classifiable legendLyr = (Classifiable) layer;
			// legendLyr.addLegendListener((LegendListener) this);
			this.addLayerListener(this);
		}
		layer.addLayerListener((LayerListener) this);

		// listener to manage the selection for the layers
		if (layer.getClass().equals(FLyrVect.class)) {
			FLyrVect lyr = (FLyrVect) layer;
			try {
				SelectableDataSource recordSet = lyr.getRecordset();
				if (recordSet != null) {
					SelectionSupport selectionSupport = recordSet
							.getSelectionSupport();
					selectionSupport.addSelectionListener(this);
				}
			} catch (ReadDriverException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// LINEAS COMETADAS PARA Q NO PREGUNTE DOS VECES QUE SI QUEREMOS
		// REPROYECTAR LA CAPA
		// SI SE COMENTA LAS LINEAS CUANDO SE COPIA Y PEGA UNA CAPA NO PREGUNTA
		// SI QUEREMOS REPROJECTAR
		// if (!m_bLoading)
		// checkProjection(layer, getViewPort().getProjection());

		int order = addLayerByType(layer);
		if (order == -1) {
			// an error has been generated
			parent.removeLayer(layer);
			return;
		}

		if (!m_bLoading)
			addLayerToPlanet(m_planet, layer, order, true);

		// Only do this the first time to add layer
		ArrayList layerListI = (ArrayList) m_layerLists
				.get(Layer3DProps.layer3DImage);
		ArrayList layerListV = (ArrayList) m_layerLists
				.get(Layer3DProps.layer3DVector);

		if (m_bEmptyView && !m_bLoading) {
			if (!((layerListI.size() == 1) && (layerListV.size() == 0))
					|| !((layerListI.size() == 0) && (layerListV.size() == 1))) {
				Layer3DProps prop = getLayer3DProps(layer);
				if(prop.getType() != Layer3DProps.layer3DOSG ) {
				
					try {
						zoomToExtent(layer.getFullExtent());
					} catch (ExpansionFileReadException e) {
						e.printStackTrace();
					} catch (ReadDriverException e) {
						e.printStackTrace();
					}
				}
				m_bEmptyView = false;
			}
		}
		if (m_canvas3d != null) {
			m_canvas3d.getOSGViewer().releaseGLContext();
			m_canvas3d.getOSGViewer().configureGLContext();
		}
		PrintDebugLayers();
	}

	public void layerRemoved(FLayers3D parent, FLayer layer) {

		// to remove group layers to 3D, just remove recursively child data
		// layers
		if (layer instanceof FLayers) {
			FLayers group = (FLayers) layer;
			for (int iChild = 0; iChild < group.getLayersCount(); iChild++) {
				layerRemoved((FLayers3D) group, group.getLayer(iChild));
			}
			getLayer3DProps(layer).setHooked(false);
			return;
		}

		if (layer instanceof Classifiable) {
			Classifiable legendLyr = (Classifiable) layer;
			legendLyr.removeLegendListener((LegendListener) this);
		}
		layer.removeLayerListener((LayerListener) this);

		int order = removeLayerByType(layer);
		if (order == -1)
			return;

		this.pepareLayerToRefresh(layer);

		
//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();

		ArrayList layerListI = (ArrayList) m_layerLists
				.get(Layer3DProps.layer3DImage);
		ArrayList layerListV = (ArrayList) m_layerLists
				.get(Layer3DProps.layer3DVector);
		// All layers are removed
		if ((layerListI.size() == 0) && (layerListV.size() == 0)) {
			m_bEmptyView = true;
		}

		PrintDebugLayers();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
	public void legendChanged(LegendChangedEvent e) {

		if (!m_bListenToLegend)
			return;
		if ((e == null) && (!visibilityChange)){

			// find layer whose legend changed
			FLayer found = null;
			SingleLayerIterator lyrIterator = new SingleLayerIterator(layers);
			while (lyrIterator.hasNext()) {
				FLayer lyr = lyrIterator.next();
				if (lyr instanceof FLyrVect) {
					FLyrVect lyrVect = (FLyrVect) lyr;
					long newDrawVersion = lyrVect.getDrawVersion();
					Layer3DProps props3D = getLayer3DProps(lyrVect);
//					if (true) {
					if (newDrawVersion != props3D.drawVersion) {
						props3D.drawVersion = lyrVect.getDrawVersion();
						// lyrVect.updateDrawVersion();
						refreshLayerInPlanet(props3D, true);
						m_bListenToLegend = false;
						props3D.VerifyLegend(m_planet.getPlanetName());
						m_bListenToLegend = true;
						
						// REPAINTING VIWER
						if (m_canvas3d != null)
							m_canvas3d.repaint();
					}
				}
			}

			IWindow f = PluginServices.getMDIManager().getActiveWindow();
			if (f instanceof BaseView) {
				BaseView view3D = (BaseView) f;
				view3D.getTOC().refresh();
			}
		}
		visibilityChange = false;
	}

	public void visibilityChanged(LayerEvent e) {
		FLayer lyr = e.getSource();

		Layer3DProps props3D = getLayer3DProps(lyr);

		if (props3D.getType() == Layer3DProps.layer3DVector) {
			refreshLayerVectorsVisibility(lyr);
		} else if (props3D.getType() == Layer3DProps.layer3DOSG) {
			refreshLayer3DOSGVisibility(lyr);
		}else {
			refreshLayerVisibility(lyr);
		}
		visibilityChange = true;
	}

	private void refreshLayer3DOSGVisibility(FLayer lyr) {
		if (m_planet == null || m_viewProjection == null)
			return;

		Layer3DProps props3D = getLayer3DProps(lyr);
		
		OSGCacheService cacheService = (OSGCacheService) props3D
				.getCacheService();
		if (cacheService != null) {
			// use VectorCacheService to add features to planet
			cacheService.refreshFeaturesToPlanet(lyr.isVisible());
		}
		
	}
	private void refreshLayerVisibility(FLayer layer) {

		Layer3DProps props3D = getLayer3DProps(layer);
		if (props3D.getType() == Layer3DProps.layer3DImage) {
			m_planet.setEnabledTextureLayer(props3D.getPlanetOrder(), layer
					.isVisible());
			System.out.println("Texture layer "+props3D.getPlanetOrder()+" is "+layer.isVisible());
		}
		if (props3D.getType() == Layer3DProps.layer3DElevation) {
			m_planet.setEnabledHeightfieldLayer(0, layer.isVisible());
		}
//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();
	}

	private void refreshLayerVectorsVisibility(FLayer lyr) {
		if (m_planet == null || m_viewProjection == null)
			return;

		Layer3DProps props3D = getLayer3DProps(lyr);

		VectorCacheService cacheService = (VectorCacheService) props3D
				.getCacheService();
		if (cacheService != null) {
			// use VectorCacheService to add features to planet
			cacheService.refreshFeaturesToPlanet(lyr.isVisible());
		}

	}

	public void activationChanged(LayerEvent e) {
		// TODO Implement this method
	}

	public void nameChanged(LayerEvent e) {
		// TODO Implement this method
	}

	public void editionChanged(LayerEvent e) {
		// TODO Implement this method

	}

	public void refreshLayerInPlanet(Layer3DProps props, boolean bRemoveCache) {
		if (props == null)
			return;

		if (m_planet == null)
			return; // view not opened yet

		// clear cache

		if (bRemoveCache) {
			String layerCacheDir = Layer3DProps.m_cacheDir + "/"
					+ m_planet.getPlanetName() + "/" + props.getCacheName();
			removeCache(layerCacheDir);
		}

		// refresh layer in planet

		int type = props.getType();
		if (type == Layer3DProps.layer3DImage) {
			int order = props.getPlanetOrder();
			// m_planet.refreshTextureInfo(order);
			m_planet.invalidateTextureLayer(order);
		} else if (type == Layer3DProps.layer3DElevation) {
			m_planet.invalidateHeightfieldLayer(0);
		} else if (type == Layer3DProps.layer3DVector) {
			invalidateVectorLayer(props);
		}

		if (m_canvas3d != null) {
			m_canvas3d.getOSGViewer().releaseGLContext();
			m_canvas3d.getOSGViewer().configureGLContext();
		}
	}

	private void invalidateVectorLayer(Layer3DProps props) {
		// TODO Auto-generated method stub
		if (m_planet == null || m_viewProjection == null)
			return;

		VectorCacheService cacheService = (VectorCacheService) props
				.getCacheService();
		if (cacheService != null) {
			// use VectorCacheService to add features to planet
			cacheService.RefreshFeaturesToPlanet();
		}
	}

	private boolean removeCache(String folder) {
		File dir = new File(folder);
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = removeCache(folder + "/" + children[i]);
				// Do not interrupt if it can't delete one file
				// if (!success) {
				// return false;
				// }
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	protected Layer3DProps getLayer3DProps(FLayer layer) {
		Layer3DProps props3D = Layer3DProps.getLayer3DProps(layer);
		if (props3D == null) {
			props3D = new Layer3DProps();
			if (layer instanceof FLyrVect) {
				FLyrVect nLayer = (FLyrVect) layer;
				Driver driver;
				try {
					driver = nLayer.getRecordset().getDriver();
					if (driver instanceof GvsigDriverOSG) {
						props3D.setChooseType(false);
					}
				} catch (ReadDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			props3D.setLayer(layer);

			props3D.initCacheName(m_planet.getCoordinateSystemType(),
					m_viewProjection, Planet.CoordinateSystemType.GEOCENTRIC);
			FLyrDefault baseLayer = (FLyrDefault) layer;
			baseLayer.setProperty("3DLayerExtension", props3D);
			props3D.setVerticalEx(this.getVerticalExageration());
		} else {
			if (m_bLoading)
				props3D.setChooseType(false);
			props3D.setLayer(layer);
		}

		return props3D;
	}
	
	public void addCurrentLayersToPlanet(Planet planet) {
		for (int iType = 0; iType < 4; iType++) {
			ArrayList layerList = (ArrayList) m_layerLists.get(iType);
			for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {

				FLayer layer = (FLayer) layerList.get(iLayer);
				Layer3DProps props = getLayer3DProps(layer);
				props.setPlanetOrder(props.getTocOrder());
				addLayerToPlanet(planet, layer, props.getPlanetOrder(), false);

				// Layer active or not
				if (props.getType() == Layer3DProps.layer3DImage) {
					planet.setEnabledTextureLayer(props.getPlanetOrder(),
							layer.isVisible());
					System.out.println("Texture layer "+props.getPlanetOrder()+" is "+layer.isVisible());
				}
				
				if (props.getType() == Layer3DProps.layer3DElevation) {
					planet.setEnabledHeightfieldLayer(0, layer.isVisible());
				}
			}
			if (iType == Layer3DProps.layer3DElevation && layerList.size() > 0)
				planet.invalidateHeightfieldLayer(0);
		}
	}

	private void addLayerToPlanet(Planet planet, FLayer layer, int order, boolean bVerifyLegend) {
		Layer3DProps props3D = getLayer3DProps(layer);
		
		
//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		
		
		if (props3D.getType() == Layer3DProps.layer3DVector) {
			CreateVectors(layer, props3D); // special case for now without
			// disk cache
		} else if (props3D.getType() == Layer3DProps.layer3DOSG) {
			CreateOSGLayer(layer, props3D);
		} else {
			Rectangle2D layerExtent = null;
			try {
				layerExtent = layer.getFullExtent();
			} catch (ExpansionFileReadException e) {
				e.printStackTrace();
			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
			if (layerExtent == null) { // hack for missing extents
				if (planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC)
					layerExtent = new Rectangle2D.Double(-180.0, -90.0, 360.0,
							180.0);
				else
					layerExtent = new Rectangle2D.Double(-20000000.0,
							-10000000.0, 40000000.0, 20000000.0);
				// TODO toggle comment because this code use WCS extension. When
				// WCS extension runs correctly uncoment!!!

				if (layer instanceof FLyrWCS)
					((FLyrWCS) layer).setFullExtent(layerExtent);
				/**/
			}
			if (props3D.getType() == Layer3DProps.layer3DElevation)
				planet.addHeightfieldLayer(layerExtent);
			else if (props3D.getType() == Layer3DProps.layer3DImage) {
				planet.addTextureLayer(layerExtent);
				ArrayList imageList = (ArrayList) m_layerLists
						.get(Layer3DProps.layer3DImage);
				int currentOrder = imageList.size() - 1;
				if (currentOrder != order) { // had to be added to non-last
					// position
					planet.reorderTextureLayer(currentOrder, order);
					for (int iLayer = 0; iLayer < imageList.size(); iLayer++) {
						FLayer eachLayer = (FLayer) imageList.get(iLayer);
						Layer3DProps eachProps3D = getLayer3DProps(eachLayer);
						if (eachProps3D.getPlanetOrder() >= order)
							eachProps3D.setPlanetOrder(eachProps3D
									.getPlanetOrder() + 1);
					}
				}
				props3D.setPlanetOrder(order);
			}
			
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

			if (bVerifyLegend) {
				m_bListenToLegend = false;
				props3D.VerifyLegend(planet.getPlanetName());
				m_bListenToLegend = true;
			}
		}
	}

	private void CreateOSGLayer(FLayer layer, Layer3DProps props3D) {
		if (m_planet == null || m_viewProjection == null)
			return;

		OSGCacheService cacheService = (OSGCacheService) props3D
				.getCacheService();
		if (cacheService == null) {
			cacheService = new OSGCacheService(m_canvas3d, m_planet, props3D
					.getCacheName(), layer, m_viewProjection);
			cacheService.setCacheRootDir(Layer3DProps.m_cacheDir);
			props3D.setCacheService(cacheService);
		}

		// use VectorCacheService to add features to planet
		cacheService.AddFeaturesToPlanet();

	}

	@Override
	public XMLEntity getXMLEntity() throws XMLException {
		// TODO Auto-generated method stub
		XMLEntity xml = super.getXMLEntity();
		
		SingleLayerIterator lyrIterator = new SingleLayerIterator(layers);
		while (lyrIterator.hasNext()) {
			FLayer lyr = lyrIterator.next();
			Layer3DProps props3D = getLayer3DProps(lyr);
			int type = props3D.getType();
			if (type == Layer3DProps.layer3DOSG){
				
				OSGCacheService cacheService = (OSGCacheService) props3D
				.getCacheService();
				if (cacheService != null) {
					// use VectorCacheService to add features to planet
					if (props3D.isEditing()){
						// TODO: PONER AKI EL CODIGO DE SALVAR
					}
				}
			}
		}
		
		
		return xml;
		
	}

	private void refreshLayerOrder() {
		int typeOrder[] = new int[4];
		typeOrder[Layer3DProps.layer3DImage] = 0;
		typeOrder[Layer3DProps.layer3DElevation] = 0;
		typeOrder[Layer3DProps.layer3DVector] = 0;
		typeOrder[Layer3DProps.layer3DOSG] = 0;

		SingleLayerIterator lyrIterator = new SingleLayerIterator(layers);
		while (lyrIterator.hasNext()) {
			FLayer lyr = lyrIterator.next();
			Layer3DProps props3D = getLayer3DProps(lyr);
			int type = props3D.getType();
			// stores new order in properties 3D, but doesn't reorder internal
			// layer lists
			props3D.setTocOrder(typeOrder[type]);
			typeOrder[type] += 1;
		}
	}

	private int addLayerByType(FLayer layer) {
		int resultOrder = -1;

		int typeOrder[] = new int[4];
		typeOrder[Layer3DProps.layer3DImage] = 0;
		typeOrder[Layer3DProps.layer3DElevation] = 0;
		typeOrder[Layer3DProps.layer3DVector] = 0;
		typeOrder[Layer3DProps.layer3DOSG] = 0;

		if (layer instanceof FLyrVect) {
			if (((FLyrVect) layer).getSource() != null) {
				if (((FLyrVect) layer).getSource().getDriver() instanceof GvsigDriverOSG) {
					Layer3DProps props3D = getLayer3DProps(layer);
					props3D.setType(Layer3DProps.layer3DOSG);
				}
			}
		}

		SingleLayerIterator lyrIterator = new SingleLayerIterator(layers);
		while (lyrIterator.hasNext()) {
			FLayer lyr = lyrIterator.next();
			Layer3DProps props3D = getLayer3DProps(lyr);

			int type = props3D.getType();
			// in pilot, limit to 1 the number of elevation layers
			if (type == Layer3DProps.layer3DElevation && typeOrder[type] == 1) {
				JOptionPane.showMessageDialog((Component) PluginServices
						.getMainFrame(), PluginServices.getText(this,
						"Only_one_elevation_messg"), PluginServices.getText(
						this, "Only_one_elevation_title"),
						JOptionPane.INFORMATION_MESSAGE);
				return -1; // error value
			}

			// stores new order in properties 3D, but doesn't reorder internal
			// layer lists
			props3D.setTocOrder(typeOrder[type]);
			typeOrder[type] += 1;

			if (layer == lyr) {
				resultOrder = props3D.getTocOrder();
				ArrayList layerList = (ArrayList) m_layerLists.get(type);
				if (resultOrder == layerList.size()
						|| layerList.get(resultOrder) != layer)
					layerList.add(resultOrder, layer);
			}
		}

		return resultOrder;
	}

	private int removeLayerByType(FLayer layer) {

		Layer3DProps props3D = getLayer3DProps(layer);
		ArrayList layerList = (ArrayList) m_layerLists.get(props3D.getType());
		int currentOrder = props3D.getTocOrder();
		if (currentOrder == -1)
			return -1;

		layerList.remove(currentOrder);

		for (int i = currentOrder; i < layerList.size(); i++) {
			FLayer lyrAux = (FLayer) layerList.get(i);
			Layer3DProps props3DAux = getLayer3DProps(lyrAux);
			props3DAux.setTocOrder(i);
		}
		return currentOrder;
	}

	private void CreateVectors(FLayer layer, Layer3DProps props3D) {
		if (m_planet == null || m_viewProjection == null)
			return;

		VectorCacheService cacheService = (VectorCacheService) props3D
				.getCacheService();
		if (cacheService == null) {
			cacheService = new VectorCacheService(m_canvas3d, m_planet, props3D
					.getCacheName(), layer, m_viewProjection);
			cacheService.setCacheRootDir(Layer3DProps.m_cacheDir);
			props3D.setCacheService(cacheService);
		}

		// use VectorCacheService to add features to planet
		cacheService.AddFeaturesToPlanet();
	}

	private void DeleteVectors(FLayer layer, Layer3DProps props3D) {
		if (m_planet == null || m_viewProjection == null)
			return;

		VectorCacheService cacheService = (VectorCacheService) props3D
				.getCacheService();
		if (cacheService != null) {
			// use VectorCacheService to delete features to planet
			cacheService.DeleteFeaturesToPlanet();
		}
	}

	/**
	 * Sets the given zoom extent to the view
	 * 
	 * @param extent
	 *            The extent to zoom to.
	 */
	public void zoomToExtent(Rectangle2D geoExtent) {
		double maxHeight = 0.0;

		// Getting extent positions
		double minLat = geoExtent.getMinY();
		double maxLat = geoExtent.getMaxY();
		double cenLon = geoExtent.getCenterX();
		double cenLat = geoExtent.getCenterY();

		double elevation = 0;

		// calculate altitude
		double avLat = 0;
		if (minLat > 0.0 || maxLat < 0.0)
			avLat = Math.min(Math.abs(minLat), Math.abs(maxLat));
		double avLon = Math.min(180.0, geoExtent.getWidth());

		double planetRadius = m_planet.getRadiusEquatorial();
		double radiusLat = planetRadius * Math.cos(Math.toRadians(avLat));
		double deltaLon = Math.sqrt(2 * radiusLat * radiusLat
				* (1 - Math.cos(Math.toRadians(avLon))));
		double deltaLat = Math.sqrt(2 * planetRadius * planetRadius
				* (1 - Math.cos(Math.toRadians(geoExtent.getHeight()))));

		double zoomFactor = 1.5;
		elevation = (Math.max(deltaLon, deltaLat) * zoomFactor) + maxHeight;

		// Calculate XYZ positions for camera.

		int planetType = this.m_planet.getCoordinateSystemType();

		Vec3 eye = new Vec3();
		Vec3 center = new Vec3();
		Vec3 up = new Vec3();
		// Calculate positions for PLAIN MODE.
		if (planetType == Planet.CoordinateSystemType.PROJECTED) {

			double difx = (geoExtent.getMaxX() - geoExtent.getX()) / 1.2d;
			double dify = (geoExtent.getMaxY() - geoExtent.getY()) / 1.2d;
			double height;

			height = Math.sqrt(difx * difx + dify * dify);
			double fullWindowFactor = 1.7;
			// EYE
			eye.setX(cenLon);
			eye.setY(cenLat);
			eye.setZ(height * fullWindowFactor);
		//	eye.setZ(5000000 * 4.6);
			// CENTER
			center.setX(cenLon);
			center.setY(cenLat);
			center.setZ(0.0);
			// UP
			up.setX(0.0);
			up.setY(1.0);
			up.setZ(0.0);
		} else
		// Calculate positions for SPHERICAL MODE.
		if (planetType == Planet.CoordinateSystemType.GEOCENTRIC) {
			// EYE
			Vec3 aux = new Vec3(cenLat, cenLon, elevation);
			eye = m_planet.convertLatLongHeightToXYZ(aux);
			// CENTER
			center.setX(0.0);
			center.setY(0.0);
			center.setZ(0.0);
			// UP
			up.setX(0.0);
			up.setY(0.0);
			up.setZ(1.0);
		}
		Camera cam = new Camera();
		cam.setViewByLookAt(eye, center, up);

		// UtilCoord.imprimeCamara(cam);

		planetViewer.setCamera(cam);
		
//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();

	}

	/** * CALLBACKS for Tiles ** */

	/*public void tileCreated(RequestLayerEvent evt) {
		int textureStage = 0;
		int MDTStage = 0;

		SingleLayerIterator lyrIterator = new SingleLayerIterator(layers);
		while (lyrIterator.hasNext()) {
			FLayer lyr = lyrIterator.next();

			Layer3DProps props3D = getLayer3DProps(lyr);
			int dataType = props3D.getType();

			if (lyr.isVisible()) { // if (true) {
				if (dataType == Layer3DProps.layer3DVector)
					continue; // get/create cache service
				FLayerCacheService cacheService = (FLayerCacheService) props3D
						.getCacheService();
				if (cacheService == null) {
					cacheService = new FLayerCacheService(m_planet, props3D
							.getCacheName(), lyr, m_viewProjection);
					cacheService.setCacheRootDir(Layer3DProps.m_cacheDir);
					props3D.setCacheService(cacheService);
				}

				Point tileIndices = new Point(evt.getX(), evt.getY());
				TileNum tileNum = new TileNum(evt.getLevel(), tileIndices);
				String tileFileName;

				double minX = evt.getMinX();
				double minY = evt.getMinY();
				double width = evt.getMaxX() - evt.getMinX();
				double height = evt.getMaxY() - evt.getMinY();

				Rectangle2D extent = new Rectangle2D.Double(minX, minY, width,
						height);
				if (cacheService.intersectsLayer(extent)) { // extent test
					// if (cacheService.intersectsLayer(extent)) { // extent
					// test
					// System.out.println("Extent del evento del tilenum "
					// + tileNum + minX + " " + minY + " " + width + " "
					// + height);
					try {
						// tileFileName = cacheService.getTileAsFName(tileNum);
						tileFileName = cacheService.getTileAsFName(tileNum,
								extent);
						// System.err.println("tile num " + tileNum);
						// System.err.println("nombre fichero " + tileFileName);
					} catch (Exception e) {
						return;
					}

					if (dataType == Layer3DProps.layer3DImage) {
						// // float opacity = 1.0f;
						// String fileExtension = "png";
						// m_planet.setTexture(evt.getTilePagedLod(),
						// tileFileName, textureStage);
						// m_planet.setTextureOpacityLayer(textureStage, props3D
						// .getOpacity());
						// textureStage++;
					} else if (dataType == Layer3DProps.layer3DElevation) {
						// String fileExtension = "tif";
						// m_planet.setHeightfield(evt.getTilePagedLod(),
						// tileFileName, MDTStage);
						// m_planet.setVerticalExaggerationLayer(MDTStage,
						// props3D
						// .getVerticalEx());
						// MDTStage++;
					}
				} else { // no intersection
					// if (dataType == Layer3DProps.layer3DImage) {
					// m_planet.setTexture(evt.getTilePagedLod(), "",
					// textureStage++);
					// }
					// if (dataType == Layer3DProps.layer3DElevation) {
					// m_planet.setHeightfield(evt.getTilePagedLod(), "",
					// MDTStage);
					// }
				}
			} else {
				if (dataType == Layer3DProps.layer3DImage) {
					textureStage++;
				}
				if (dataType == Layer3DProps.layer3DElevation) {
					MDTStage++;
				}
			} // REPAINTING VIEWER
			if (m_canvas3d != null)
				m_canvas3d.repaint();
		}
	}*/

	private void PrintDebugLayers() {
		System.out.println("===========================");
		for (int iList = 0; iList < 4; iList++) {
			ArrayList layerList = (ArrayList) m_layerLists.get(iList);
			System.out.println("===== List " + iList + "=====");
			for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {
				FLayer layer = (FLayer) layerList.get(iLayer);
				System.out.println("  Layer " + layer.getName());
				Layer3DProps props3D = getLayer3DProps(layer);
				System.out.println("    Type " + props3D.getType());
				System.out.println("    TOC Order " + props3D.getTocOrder());
				System.out.println("    Planet Order "
						+ props3D.getPlanetOrder());
			}
		}
	}

	/**
	 * Crea un nuevo MapContext(3D) a partir del XMLEntity.
	 * 
	 * @param xml
	 *            XMLEntity
	 * 
	 * @return Nuevo MapContext3D.
	 * 
	 * @throws XMLException
	 */
	public static MapContext createFromXML(XMLEntity xml) throws XMLException {
		ViewPort3D vp = (ViewPort3D) ViewPort3D.createFromXML(xml.getChild(0));

		FLayers layers = new FLayers3D(null, null, vp);
		layers.setXMLEntity(xml.getChild(1));

		MapContext fmap = layers.getMapContext();

		return fmap;
	}

	public static MapContext createFromXML03(XMLEntity xml) throws XMLException {
		ViewPort vp = ViewPort.createFromXML03(xml.getChild(0));

		FLayers layers = new FLayers3D(null, null, vp);
		layers.setXMLEntity03(xml.getChild(1));

		MapContext fmap = layers.getMapContext();

		return fmap;
	}

	public GraphicLayer getGraphicsLayer() {
		GraphicLayer3D gra3D = new GraphicLayer3D(this.planetViewer,
				getPlanet());
		return gra3D;
	}

	public Node getSpecialNode() {
		Node specialNode = null;
		try {
			specialNode = planetViewer.getSpecialNode(0);
		} catch (ChildIndexOutOfBoundsExceptions e) {
			logger.error("Command: " + "Error child index out of bound.", e);
		}
		return specialNode;
	}

	public ViewPort getViewPort() {

		ViewPort3D vp = (ViewPort3D) super.getViewPort();

		if (m_canvas3d != null)
			vp.setImageSize(new Dimension(planetViewer.getWidth(), planetViewer
					.getHeight()));
		vp.setPlanet(this.getPlanet());
		vp.setViewer(this.m_canvas3d);

		return vp;
	}

	/**
	 * M�todo de conveniencia que se usa provisionalmente para solicitar un
	 * refresco de todo lo que dependa del FMap (MapContext). Esto provocar�
	 * un evento de cambio de orden de capas que obligar� a redibujar todo lo
	 * que depende de FMap (TOC, MapControl, FFrameView, etc).
	 */
	public void invalidate() {
		ViewPort3D vp = (ViewPort3D) super.getViewPort();
		if (vp.getDirty()) {
			vp.setDirty(false);
			// extentAux = vport.getExtent();

			// Comento el zoom pq no kiero q me lo haga y me cambie la vista
			// cuando repinto

			// this.zoomToExtent(vp.getExtent());
			// System.out.println("extend seleccionado: \n Centro: "
			// + extentAux.getCenterX() + "\n Alto: " + extentAux.getHeight()
			// + "\n Ancho: " + extentAux.getWidth());

			// TEST

			// Updating all the layers. This operation synchronize the
			// transparency layer with layer transparency planet layer.
			// Getting all selected Extent

		}
		FLayer[] selectedExtent = getLayers().getActives();

		if (selectedExtent.length > 0) {
			for (int i = 0; i < selectedExtent.length; i++) {
				if (selectedExtent[0].isAvailable()) {

					FLayer lyr3D = selectedExtent[i];
					Layer3DProps layer3DProps = Layer3DProps
							.getLayer3DProps(lyr3D);

					int order = layer3DProps.getPlanetOrder();
					// float opacity = layer3DProps.getOpacity();
					float opacity = (((FLyrDefault) lyr3D).getTransparency())
							/ (float) 255;
					// Notify the opacity to planet
					this.m_planet.setTextureOpacityLayer(order, opacity);
				}
			}

		}
		// super.invalidate();

//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();
		//m_canvas3d.getOSGViewer().releaseGLContext();
		//m_canvas3d.getOSGViewer().configureGLContext();
	}

	public void selectionChanged(SelectionEvent e) {
		// TODO Auto-generated method stub
		for (int iType = 0; iType < 3; iType++) {
			ArrayList layerList = (ArrayList) m_layerLists.get(iType);
			for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {

				FLayer layer = (FLayer) layerList.get(iLayer);
				if (layer.getClass().equals(FLyrVect.class)) {
					FLyrVect lyr = (FLyrVect) layer;

					FBitSet selection = null;
					try {
						selection = lyr.getRecordset().getSelectionSupport()
								.getSelection();
					} catch (ReadDriverException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// if (!(selection.isEmpty())){
					if ((selection.cardinality() == 0)
							|| (!(selection.isEmpty()))) {
						Layer3DProps props = Layer3DProps
								.getLayer3DProps(layer);
						refreshLayerInPlanet(props, true);
						if (layer instanceof FLyrVect) {
							FLyrVect fvect = (FLyrVect) layer;
							props.drawVersion = fvect.getDrawVersion();
						}
					}
				}

			}
		}
//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();

	}

	public void requestElevationLayer(Planet planet, RequestLayerEvent rle) {

		// TODO : All this method are not tested
		// ignore for now

		/* UNCOMENT FOR TEST IT */
		if(rle.getTerrainNode() == 0) return;
		int order = rle.getOrder();
		ArrayList imageLayers = (ArrayList) m_layerLists
				.get(Layer3DProps.layer3DElevation);
		FLayer layer = (FLayer) imageLayers.get(order);
		if ((layer == null) || (!layer.isVisible()))
			return;

		Layer3DProps props3D = getLayer3DProps(layer);
		// get/create cache service
		FLayerCacheService cacheService = (FLayerCacheService) props3D
				.getCacheService();
		if (cacheService == null) {
			cacheService = new FLayerCacheService(m_planet, props3D
					.getCacheName(), layer, m_viewProjection);
			cacheService.setCacheRootDir(Layer3DProps.m_cacheDir);
			props3D.setCacheService(cacheService);
		}

		int dataType = props3D.getType();
		Point tileIndices = new Point(rle.getX(), rle.getY());
		TileNum tileNum = new TileNum(rle.getLevel(), tileIndices);

		String tileFileName;
		double minX = rle.getMinX();
		double minY = rle.getMinY();
		double width = rle.getMaxX() - rle.getMinX();
		double height = rle.getMaxY() - rle.getMinY();
		// Rectangle2D extent = new Rectangle2D.Double(Math.toDegrees(minX),
		// Math.toDegrees(minY), Math.toDegrees(width), Math.toDegrees(height));
		Rectangle2D extent = new Rectangle2D.Double(minX, minY, width, height);
		if (cacheService.intersectsLayer(extent)) { // extent test
			// if (cacheService.intersectsLayer(extent)) {
			// System.out.println("Extetn del evento " + minX + " " + minY + " "
			// + width + " " + height);
			try {
				// tileFileName = cacheService.getTileAsFName(tileNum,extent);
				tileFileName = cacheService.getTileAsFName(tileNum, extent);
			} catch (Exception e) {
				planet.setFailHeightField(rle.getTerrainNode(), order);
				return;
			}

			if (dataType == Layer3DProps.layer3DElevation) {
				String fileExtension = "tif";
				// evt.getTilePagedLod().setHeightField(tileFileName,
				// fileExtension, order);
				planet.setHeightfield(rle.getTerrainNode(), tileFileName,
						order);
					// m_planet.setVerticalExaggeration(order,
					// ((ProjectView3D) this.m_viewProjection)
					// .getVerticalExaggeration());
				planet.setVerticalExaggerationLayer(order, props3D
						.getVerticalEx());

			}
		} else { // no intersection for elevation layer
			// This code are not correctly for elevation layer
			if (dataType == Layer3DProps.layer3DImage) {
				planet.setHeightfield(rle.getTerrainNode(), "", order);
				// evt.getTilePagedLod().setHeightField("", "", order);
			}
		}

//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();

	}

	public void requestTextureLayer(Planet planet, RequestLayerEvent rle) {

		if(rle.getTerrainNode() == 0) return;
		int order = rle.getOrder();
		ArrayList imageLayers = (ArrayList) m_layerLists
				.get(Layer3DProps.layer3DImage);

		// if there are not one layer return
		if (imageLayers.size() == 0)
			return;
		FLayer layer = (FLayer) imageLayers.get(order);
		if ((layer == null) || (!layer.isVisible()))
			return;

		Layer3DProps props3D = getLayer3DProps(layer);
		// get/create cache service
		FLayerCacheService cacheService = (FLayerCacheService) props3D
				.getCacheService();
		if (cacheService == null) {
			cacheService = new FLayerCacheService(m_planet, props3D
					.getCacheName(), layer, m_viewProjection);
			cacheService.setCacheRootDir(Layer3DProps.m_cacheDir);
			props3D.setCacheService(cacheService);
		}

		int dataType = props3D.getType();
		Point tileIndices = new Point(rle.getX(), rle.getY());
		TileNum tileNum = new TileNum(rle.getLevel(), tileIndices);

		String tileFileName;
		double minX = rle.getMinX();
		double minY = rle.getMinY();
		double width = rle.getMaxX() - rle.getMinX();
		double height = rle.getMaxY() - rle.getMinY();
		// Rectangle2D extent = new Rectangle2D.Double(Math.toDegrees(minX),
		// Math.toDegrees(minY), Math.toDegrees(width), Math.toDegrees(height));
		Rectangle2D extent = new Rectangle2D.Double(minX, minY, width, height);
		if (cacheService.intersectsLayer(extent)) { // extent test
			// if (cacheService.intersectsLayer(extent)) {
			try {
				tileFileName = cacheService.getTileAsFName(tileNum, extent);
				// tileFileName = cacheService.getTileAsFName(tileNum);
				// System.err.println("tile num " + tileNum);
				// System.err.println("nombre fichero " + tileFileName);
			} catch (Exception e) {
				planet.setFailTexture(rle.getTerrainNode(), order);
				return;
			}

			if (dataType == Layer3DProps.layer3DImage) {
				// float opacity = 1.0f;
				String fileExtension = "png";
				// @TODO:aplicar texturas al planeta
				// evt.getTilePagedLod().setTexture(tileFileName, fileExtension,
				// order);
				planet.setTexture(rle.getTerrainNode(), tileFileName, order);
				planet.setTextureOpacityLayer(order, props3D.getOpacity());
			}
			/*
			 * else { String fileExtension = "tif";
			 * evt.getTilePagedLod().setHeightField(tileFileName,
			 * fileExtension); }
			 */
		} else { // no intersection
			if (dataType == Layer3DProps.layer3DImage) {
				planet.setTexture(rle.getTerrainNode(), "", order);
				// evt.getTilePagedLod().setTexture("", "", order);
			}
		}

//		try {
//			((PlanetViewer) m_canvas3d.getOSGViewer()).activePlanet(0);
//		} catch (ChildIndexOutOfBoundsExceptions e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		((PlanetViewer) m_canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath();
		// REPAINTING VIEWER
		if (m_canvas3d != null)
			m_canvas3d.repaint();

	}

	public void requestVectorLayer(Planet planet, RequestLayerEvent rle) {
		// TODO Auto-generated method stub
		if(rle.getTerrainNode() == 0) return;
	}

	public void pepareLayerToRefresh(FLayer layer) {
		// TODO Auto-generated method stub
		Layer3DProps props3D = getLayer3DProps(layer);

		if (props3D.getType() == Layer3DProps.layer3DVector) {
			DeleteVectors(layer, props3D);
		} else if (props3D.getType() == Layer3DProps.layer3DOSG) {
			DeleteOSGLayer(layer, props3D);
		} else if (props3D.getType() == Layer3DProps.layer3DImage) {
			m_planet.removeTextureLayer(props3D.getPlanetOrder());
			ArrayList layerList = (ArrayList) m_layerLists.get(props3D
					.getType());

			for (int iLayer = 0; iLayer < layerList.size(); iLayer++) {
				FLayer eachLayer = (FLayer) layerList.get(iLayer);
				Layer3DProps eachProps3D = getLayer3DProps(eachLayer);
				if (eachProps3D.getPlanetOrder() > props3D.getPlanetOrder())
					eachProps3D
							.setPlanetOrder(eachProps3D.getPlanetOrder() - 1);
			}
			props3D.setPlanetOrder(-1);
		} else if (props3D.getType() == Layer3DProps.layer3DElevation) {
			m_planet.removeHeightfieldLayer(props3D.getTocOrder());
		}

	}

	private void DeleteOSGLayer(FLayer layer, Layer3DProps props3D) {
		if (m_planet == null || m_viewProjection == null)
			return;

		OSGCacheService cacheService = (OSGCacheService) props3D
				.getCacheService();
		if (cacheService != null) {
			// use VectorCacheService to delete features to planet
			cacheService.DeleteFeaturesToPlanet();
		}
	}

	public void refreshLayer3DProps(FLayer layer) {
		// TODO Auto-generated method stub
		addLayerByType(layer);
		addLayerToPlanet(m_planet, layer, Layer3DProps.getLayer3DProps(layer)
				.getTocOrder(), true);

	}

	public boolean isRenewCanvasOff() {
		return renewCanvasOff;
	}

	public void setRenewCanvasOff(boolean renewCanvasOff) {
		this.renewCanvasOff = renewCanvasOff;
	}

	public void drawValueChanged(LayerEvent arg0) {
		// TODO Auto-generated method stub

	}

}
