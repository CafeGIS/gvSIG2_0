package org.gvsig.gvsig3d.map3d;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.IProjection;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.Intersections;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.ExtentHistory;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;

public class ViewPort3D extends ViewPort implements MouseListener {

	private Planet planet;

	private IViewerContainer canvas3d;

	// private Rectangle2D _extent;

	private boolean dirty = false;

	public ViewPort3D(IProjection proj) {
		super(proj);
		// TODO Auto-generated constructor stub
	}

	public Planet getPlanet() {
		return planet;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	public void setViewer(IViewerContainer canvas) {
		canvas3d = canvas;
	}

	public void setDirty(boolean isDirty) {
		dirty = isDirty;
	}

	public boolean getDirty() {
		return dirty;
	}

	public Rectangle2D getAdjustedExtent() {
		return extent;
	}

	public void setExtent(Rectangle2D r) {
		extent = r;

		dirty = true;
	}

	/**
	 * Returns a point in geocoordinates from window coordinates
	 * 
	 * @param pScreen
	 *            Screen coordinates
	 * 
	 * @return point in geocoordinates
	 * 
	 * @throws RuntimeException
	 */
	@Override
	public Point2D toMapPoint(Point2D pScreen) {
		
		// getting layer information
		float heigth= 0;
		IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f instanceof BaseView) {
			BaseView baseView = (BaseView) f;
			FLayer[] lyrs = baseView.getMapControl().getMapContext().getLayers().getActives();
			if(lyrs != null && lyrs.length > 0) {
				FLayer layer = lyrs[lyrs.length-1];
				Layer3DProps props = Layer3DProps.getLayer3DProps(layer);
				if(props != null) 
					heigth = props.getHeigth();
			}
			
		}
		

//		System.err.println("Coordenadas de pantalla " + pScreen.getX() + ","+ pScreen.getY());
		Intersections hits = canvas3d.getOSGViewer().rayPick(
				(int) pScreen.getX(), (int) pScreen.getY());
		Point2D pWorld = new Point2D.Double();
		if (hits.containsIntersections()) {
			// get XYZ coordinates on planet
			Vec3 hit = hits.getFirstIntersection().getIntersectionPoint();
			// convert to geo coordinates

			// System.err.println("Interseccion de osg " + hit.x() + ","+
			// hit.y());
			if (getProjection().getAbrev().compareToIgnoreCase("EPSG:4326") == 0) {
				Vec3 geoPt = planet.convertXYZToLatLongHeight(hit);

				if (Math.abs(heigth - geoPt.z()) <= 1000) {
					pWorld.setLocation(geoPt.y(), geoPt.x());
				} else {
					pWorld.setLocation(360, 120);
				}
				NotificationManager.addInfo("Obteniendo punto de informacion "
						+ pWorld.getX() + "    ,   " + pWorld.getY());
			} else {
				if (Math.abs(heigth - hit.z()) <= 100) {
					pWorld.setLocation(hit.x(), hit.y());
				} else {
					pWorld.setLocation(360, 120);
				}
				NotificationManager.addInfo("Obteniendo punto de informacion "
						+ pWorld.getX() + "    ,   " + pWorld.getY());
			}
		} else {
			if (getProjection().getAbrev().compareToIgnoreCase("EPSG:4326") == 0) {
				pWorld.setLocation(360, 120);
			} else
				pWorld.setLocation(1e100, 1e100);
		}

		return pWorld;
	}

	/**
	 * Returns geographic distance from pixel distance
	 * 
	 * @param d
	 *            Pixel distance
	 * 
	 * @return geographic distance
	 */
	public double toMapDistance(int d) {
		// double dist = d / trans.getScaleX();

		double zoom = planet.getZoom(); // distance to center in meters

		return 0.1; // TEST
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		// Center point of locator
		Point2D center = null;
		// Scale factor
		double factor = 0;
		if (planet != null) {
			// Getting center point longitude and latitude
			center = new Point2D.Double((planet.getLongitude()), (planet
					.getLatitude()));
			// Getting distance
			double elevation = planet.getAltitude();
			factor = (elevation * 0.000005);

		}
		// View3D aspect radio
		double aspect = (double) canvas3d.getWidth() / canvas3d.getHeight();
		// Calculate new width & height
		double width = factor * aspect * 2;
		double height = (factor / aspect) * 2;

		height = height > 180 ? 180 : height;
		width = width > 360 ? 360 : height;
		// New extent
		Rectangle2D extend = new Rectangle2D.Double(center.getX() - width / 2,
				center.getY() - height / 2, width, height);
		extent = extend;
		// Locator Refresh
		refreshExtent();
	}

	public void refreshExtent() {

		// Calling extent changed to locator
		super.callExtentChanged(extent);
	}

	/**
	 * Crea un nuevo ViewPort a partir del XMLEntity.
	 * 
	 * @param xml
	 *            XMLEntity.
	 * 
	 * @return Nuevo ViewPort.
	 */
	public static ViewPort createFromXML(XMLEntity xml) {
		// A BETTER WAY TO DO THIS SHOULD BE TO SEPARATE CONSTRUCTOR CALL FROM
		// XML READING
		// SO SUPER'S XML READING COULD BE USED HERE

		ViewPort3D vp = new ViewPort3D(null);

		if (xml.contains("adjustedExtentX")) {
			vp.adjustedExtent = new Rectangle2D.Double(xml
					.getDoubleProperty("adjustedExtentX"), xml
					.getDoubleProperty("adjustedExtentY"), xml
					.getDoubleProperty("adjustedExtentW"), xml
					.getDoubleProperty("adjustedExtentH"));
		}

		if (xml.contains("backColor")) {
			vp.setBackColor(StringUtilities.string2Color(xml
					.getStringProperty("backColor")));
		} else {
			vp.setBackColor(Color.white);
		}

		if (xml.contains("clipX")) {
			vp.setClipRect( new Rectangle2D.Double(xml.getDoubleProperty("clipX"),
					xml.getDoubleProperty("clipY"), xml
							.getDoubleProperty("clipW"), xml
							.getDoubleProperty("clipH")));
		}

		vp.setDist1pixel(xml.getDoubleProperty("dist1pixel"));
		vp.setDist3pixel(xml.getDoubleProperty("dist3pixel"));
		vp.setDistanceUnits(xml.getIntProperty("distanceUnits"));
		vp.extents = ExtentHistory.createFromXML(xml.getChild(0));

		if (xml.contains("extentX")) {
			vp.setExtent(new Rectangle2D.Double(xml
					.getDoubleProperty("extentX"), xml
					.getDoubleProperty("extentY"), xml
					.getDoubleProperty("extentW"), xml
					.getDoubleProperty("extentH")));

			// Calcula la transformación afín
			// vp.calculateAffineTransform();

			// Lanzamos los eventos de extent cambiado
			// vp.callExtentListeners(vp.adjustedExtent);
		}

		vp.setMapUnits(xml.getIntProperty("mapUnits"));
		vp.setOffset(new Point2D.Double(xml.getDoubleProperty("offsetX"), xml
				.getDoubleProperty("offsetY")));

		if (xml.contains("proj")) {
			vp.setProjection(CRSFactory.getCRS(xml.getStringProperty("proj")));
		}

		// vp.setScale(xml.getDoubleProperty("scale"));
		vp.refreshExtent();
		return vp;
	}
}
