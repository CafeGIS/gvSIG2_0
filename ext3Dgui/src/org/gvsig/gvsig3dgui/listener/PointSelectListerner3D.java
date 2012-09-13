package org.gvsig.gvsig3dgui.listener;

import java.awt.geom.Point2D;

import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.viewer.Intersections;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.project.documents.view.toolListeners.PointSelectListener;

public class PointSelectListerner3D extends PointSelectListener {

	public PointSelectListerner3D(MapControl mapCtrl) {
		super(mapCtrl);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void point(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub
		// super.point(event);
		// MapControl3D mapControl3D = (MapControl3D) this.mapCtrl;
		MapContext3D mapContext = (MapContext3D) mapCtrl.getMapContext();

		try {
			// mapCtrl.getMapContext().selectByPoint(event.getPoint(), 1);
			Point2D p = event.getPoint();
			Vec3 mapPoint = coordinatesIntersection(p);

			// Tolerancia de 3 pixels
			// double tol = mapCtrl.getViewPort().toMapDistance(3);
			double tol = 0.20;
			FLayer[] actives = mapCtrl.getMapContext().getLayers().getActives();
			for (int i = 0; i < actives.length; i++) {
				if (actives[i] instanceof FLyrVect) {
					FLyrVect lyrVect = (FLyrVect) actives[i];
					Layer3DProps props = Layer3DProps.getLayer3DProps(lyrVect);
					float heigth = props.getHeigth();
					if (Math.abs(heigth - mapPoint.z()) <= 3000){
						FBitSet oldBitSet = lyrVect.getSource().getRecordset()
						.getSelection();
						Point2D pointInter = new Point2D.Double();
						pointInter.setLocation(mapPoint.x(), mapPoint.y());
						FBitSet newBitSet = lyrVect.queryByPoint(pointInter, tol);
//						FBitSet newBitSet = lyrVect.queryByPoint(p, tol);
						if (event.getEvent().isControlDown())
							newBitSet.xor(oldBitSet);
						lyrVect.getRecordset().setSelection(newBitSet);
					}
				}
			}

		} catch (ReadDriverException e) {
			throw new BehaviorException("No se pudo hacer la selección");
		} catch (VisitorException e) {
			throw new BehaviorException("No se pudo hacer la selección");
		}
//		mapContext.selectionChanged(new SelectionEvent());
	}

	@Override
	public void pointDoubleClick(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub
		// super.pointDoubleClick(event);
	}

	public Vec3 coordinatesIntersection(Point2D pScreen) {
		Vec3 intersection;
	
		// System.err.println("Coordenadas de pantalla " + pScreen.getX() + ","+
		// pScreen.getY());
		MapContext3D mcontext = (MapContext3D) this.mapCtrl.getMapContext();
		int heigth = mcontext.getCanvas3d().getHeight();
		Intersections hits = mcontext.getCanvas3d().getOSGViewer().rayPick(
				(int) pScreen.getX(),(int) pScreen.getY());
		Point2D pWorld = new Point2D.Double();
		if (hits.containsIntersections()) {
			// get XYZ coordinates on planet
			Vec3 hit = hits.getFirstIntersection().getIntersectionPoint();
			// convert to geo coordinates

			// System.err.println("Interseccion de osg " + hit.x() + ","+
			// hit.y());
			if (mcontext.getProjection().getAbrev().compareToIgnoreCase("EPSG:4326") == 0) {
				Vec3 geoPt = mcontext.getPlanet().convertXYZToLatLongHeight(hit);
				// Swap the coordinates X and Y, because they are invert.
				intersection = new Vec3(geoPt.y(),geoPt.x(),geoPt.z());
//				intersection = geoPt;
			} else {
				intersection = hit;
			}
		} else {
			if (mcontext.getProjection().getAbrev().compareToIgnoreCase("EPSG:4326") == 0) {
				pWorld.setLocation(360, 120);
				intersection = new Vec3(360,120,0);
			} else{
				intersection = new Vec3(1e100,1e100,0);
//				pWorld.setLocation(1e100, 1e100);
			}
		}
		NotificationManager.addInfo("Obteniendo punto de informacion "+ intersection.x() + "    ,   " + intersection.y() +"  ,  "+ intersection.z());
		return intersection;
	}

}
