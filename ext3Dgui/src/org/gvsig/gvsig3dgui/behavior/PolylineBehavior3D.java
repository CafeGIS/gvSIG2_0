package org.gvsig.gvsig3dgui.behavior;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.ext.awt.geom.Polyline2D;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.map3d.GraphicLayer3D;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.exceptions.OSGVPException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.InteractivePolyline;
import org.gvsig.osgvp.features.SelectionRectangle;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.util.Util;
import org.gvsig.osgvp.viewer.Intersections;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.PolylineBehavior;
import com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;
import com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener;
import com.iver.cit.gvsig.fmap.tools.Listeners.RectangleListener;
import com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener;

public class PolylineBehavior3D extends Behavior{
	private Point2D m_FirstPoint;
	private Point2D m_LastPoint;
	private boolean _clicked=false;
	private PolylineListener listener;
	private InteractivePolyline ipol;
	private int height;
	private int width;
	private float _factor=0.1f;
	private static Logger logger = Logger.getLogger(GraphicLayer3D.class
			.getName());
	
	public PolylineBehavior3D(PolylineListener mli) {
		//super(mli);
		listener = mli;
		try {
			ipol = new InteractivePolyline();
		} catch (OSGVPException e) {
			logger.error("Command: "
					+ "Error creating new interactive polyline", e);
		}
	}
	public PolylineBehavior3D() {
		
		try {
			ipol = new InteractivePolyline();
		} catch (OSGVPException e) {
			logger.error("Command: "
					+ "Error creating new interactive polyline", e);
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		// super.paintComponent(g);
		
//		mcontext.getCanvas3d().repaint();
	}
	public void restart(){
		MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
		try {
			((PlanetViewer) mcontext.getCanvas3d().getOSGViewer())
			.removeSpecialNode(ipol);
			
		} catch (OSGVPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ipol = new InteractivePolyline();
		
		} catch (OSGVPException e) {
			logger.error("Command: "
					+ "Error creating new interactive polyline", e);
		}
		try {
			((PlanetViewer) mcontext.getCanvas3d().getOSGViewer())
			.addSpecialNode(ipol);
			
		} catch (OSGVPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void mouseMoved(MouseEvent arg0) {

		
	
		if (_clicked) {
			MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
			Intersections hits =  mcontext.getCanvas3d().getOSGViewer().rayPick(arg0.getX(),
					arg0.getY());
			if (hits!=null){
			Vec3 normal =hits.getFirstIntersection().getIntersectionNormal();
			
			normal.normalize();
			ipol.setMouseCoords(hits.getFirstIntersection().getIntersectionPoint().sum(normal.escalarProduct(_factor)));
			}
			try {
				ipol.update();
			} catch (OSGVPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

		}
	}
	
	
	
	
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	
		
		MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
		Intersections hits = mcontext.getCanvas3d().getOSGViewer().rayPick(arg0.getX(),
				arg0.getY());
		if (!_clicked){
			try {
				((PlanetViewer) mcontext.getCanvas3d().getOSGViewer())
				.addSpecialNode(ipol);
				
			} catch (OSGVPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			_clicked = true;
			Vec3 normal =hits.getFirstIntersection()
			.getIntersectionNormal();
			normal.normalize();
			ipol.setMouseCoords(hits.getFirstIntersection().getIntersectionPoint().sum(normal.escalarProduct(_factor)));
		}
	
		
		try {
			ipol.update();
		} catch (OSGVPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ipol.setEnabledNode(true);
		}
	
	}
	
	public void mouseReleased(MouseEvent arg0) {
		MapContext3D mcontext = (MapContext3D) getMapControl().getMapContext();
		Intersections hits = mcontext.getCanvas3d().getOSGViewer().rayPick(arg0.getX(),
				arg0.getY());
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			Vec3 normal =hits.getFirstIntersection()
			.getIntersectionNormal();
			normal.normalize();
			ipol.setMouseCoords(hits.getFirstIntersection().getIntersectionPoint().sum(normal.escalarProduct(_factor)));
			ipol.addVertex(hits.getFirstIntersection().getIntersectionPoint().sum(normal.escalarProduct(_factor)));
			try {
				ipol.update();
			} catch (OSGVPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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
//		NotificationManager.addInfo("Obteniendo punto de informacion "
//				+ intersection.x() + "    ,   " + intersection.y() + "  ,  "
//				+ intersection.z());
		return intersection;
	}
	@Override
	public ToolListener getListener() {
		// TODO Auto-generated method stub
		return null;
	}


}
