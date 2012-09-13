package org.gvsig.gvsig3dgui.behavior;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.map3d.GraphicLayer3D;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.exceptions.OSGVPException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.SelectionRectangle;
import org.gvsig.osgvp.features.Polygon.PolygonType;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.Intersections;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Behavior.RectangleBehavior;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.RectangleListener;

public class RectangleBehavior3D extends RectangleBehavior {

	private Point2D m_FirstPoint;
	private Point2D m_LastPoint;
	private RectangleListener listener;
	private SelectionRectangle rectangle;
	private int height;
	private int width;

	private static Logger logger = Logger.getLogger(GraphicLayer3D.class
			.getName());

	public RectangleBehavior3D(RectangleListener zili) {
		super(zili);
		// TODO Auto-generated constructor stub
		listener = zili;
		try {
			rectangle = new SelectionRectangle();
		} catch (OSGVPException e) {
			logger.error("Command: "
					+ "Error creating new selection rectangle.", e);
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		// super.mouseDragged(e);
		// Painting
		rectangle.setMouseCoords((int) e.getPoint().getX(), height
				- (int) e.getPoint().getY());
		try {
			rectangle.update();
		} catch (OSGVPException e1) {
			logger.error("Command: " + "Error updating rectangle selection.",
					e1);
		}

		rectangle.setType(PolygonType.FILLED_POLYGON);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
		try {
			((PlanetViewer) mcontext.getCanvas3d().getOSGViewer())
					.addNodeToHUD(rectangle);
		} catch (NodeException e1) {
			logger.error("Command: "
					+ "Error adding the rectangle selecton to the hud node.",
					e1);
		}
		height = mcontext.getCanvas3d().getHeight();
		width = mcontext.getCanvas3d().getWidth();

		if (e.getButton() == MouseEvent.BUTTON1) {
			m_FirstPoint = e.getPoint();
			// Put here the code to star drawing the rectangle
			rectangle.setFirstPoint((int) m_FirstPoint.getX(), height
					- (int) m_FirstPoint.getY());
			rectangle.setMouseCoords((int) m_FirstPoint.getX(), height
					- (int) m_FirstPoint.getY());

			try {
				rectangle.update();
			} catch (OSGVPException e1) {
				logger.error("Command: "
						+ "Error updating rectangle selection.", e1);
			}
			rectangle.setType(PolygonType.EMPTY_POLYGON);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) throws BehaviorException {

		if (m_FirstPoint == null)
			return;
		Point2D p1 = new Point2D.Double();
		Point2D p2 = new Point2D.Double();
		Point pScreen = e.getPoint();

		// ViewPort vp = getMapControl().getMapContext().getViewPort();

		// p1 = vp.toMapPoint(m_FirstPoint);
		// p2 = vp.toMapPoint(pScreen);
		Vec3 p1Vec = coordinatesIntersection(m_FirstPoint);
		p1.setLocation(p1Vec.x(), p1Vec.y());
		Vec3 p2Vec = coordinatesIntersection(pScreen);
		p2.setLocation(p2Vec.x(), p2Vec.y());

		if (e.getButton() == MouseEvent.BUTTON1) {
			// Fijamos el nuevo extent
			Rectangle2D.Double r = new Rectangle2D.Double();
			r.setFrameFromDiagonal(p1, p2);

			Rectangle2D rectPixel = new Rectangle();
			rectPixel.setFrameFromDiagonal(m_FirstPoint, pScreen);

			RectangleEvent event = new RectangleEvent(r, e, rectPixel);
			listener.rectangle(event);

		}

		// Deleted rectangle
		rectangle.setMouseCoords((int) m_FirstPoint.getX(), height
				- (int) m_FirstPoint.getY());
		try {
			rectangle.update();
		} catch (OSGVPException e1) {
			logger.error("Command: " + "Error updating rectangle selection.",
					e1);
		}
		rectangle.setType(PolygonType.EMPTY_POLYGON);

		m_FirstPoint = null;
		m_LastPoint = null;
	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		// super.paintComponent(g);
		MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
		mcontext.getCanvas3d().repaint();
	}

	public Vec3 coordinatesIntersection(Point2D pScreen) {
		Vec3 intersection;

		// System.err.println("Coordenadas de pantalla " + pScreen.getX() + ","+
		// pScreen.getY());
		MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
		Intersections hits = mcontext.getCanvas3d().getOSGViewer().rayPick(
				(int) pScreen.getX(), (int) pScreen.getY());
		Point2D pWorld = new Point2D.Double();
		if (hits.containsIntersections()) {
			// get XYZ coordinates on planet
			Vec3 hit = hits.getFirstIntersection().getIntersectionPoint();
			// convert to geo coordinates

			// System.err.println("Interseccion de osg " + hit.x() + ","+
			// hit.y());
			if (mcontext.getProjection().getAbrev().compareToIgnoreCase(
					"EPSG:4326") == 0) {
				Vec3 geoPt = mcontext.getPlanet()
						.convertXYZToLatLongHeight(hit);
				// Swap the coordinates X and Y, because they are invert.
				intersection = new Vec3(geoPt.y(), geoPt.x(), geoPt.z());
			} else {
				intersection = hit;
			}
		} else {
			if (mcontext.getProjection().getAbrev().compareToIgnoreCase(
					"EPSG:4326") == 0) {
				pWorld.setLocation(360, 120);
				intersection = new Vec3(360, 120, 0);
			} else {
				intersection = new Vec3(1e100, 1e100, 0);
				// pWorld.setLocation(1e100, 1e100);
			}
		}
		NotificationManager.addInfo("Obteniendo punto de informacion "
				+ intersection.x() + "    ,   " + intersection.y() + "  ,  "
				+ intersection.z());
		return intersection;
	}

}
