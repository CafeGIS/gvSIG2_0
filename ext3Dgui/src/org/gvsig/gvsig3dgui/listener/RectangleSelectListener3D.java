package org.gvsig.gvsig3dgui.listener;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.viewer.Intersections;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.RectangleSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;

public class RectangleSelectListener3D extends RectangleSelectionListener {

	private MapControl mapControl;

	public RectangleSelectListener3D(MapControl mc) {
		super(mc);
		// TODO Auto-generated constructor stub
		mapControl = mc;
	}

	@Override
	public void rectangle(RectangleEvent event) throws BehaviorException {
		try {
			// mapCtrl.getMapContext().selectByRect(event.getWorldCoordRect());
			Rectangle2D rect = event.getWorldCoordRect();
			Point2D up = new Point2D.Double();
			up.setLocation(rect.getX(), rect.getY());
			Vec3 upLeft = coordinatesIntersection(up);
//			Point2D down = new Point2D.Double();
//			down.setLocation(rect.getMaxX(), rect.getMaxY());
//			Vec3 downRight = coordinatesIntersection(down);

			Rectangle2D newRect = new Rectangle2D.Double(upLeft.x(),
					upLeft.y(), rect.getHeight(), rect.getWidth());

			FLayer[] actives = mapControl.getMapContext().getLayers()
					.getActives();
			for (int i = 0; i < actives.length; i++) {
				if (actives[i] instanceof FLyrVect) {
					FLyrVect lyrVect = (FLyrVect) actives[i];
					FBitSet oldBitSet = lyrVect.getSource().getRecordset()
							.getSelection();
//					FBitSet newBitSet = lyrVect.queryByRect(newRect);
					FBitSet newBitSet = lyrVect.queryByRect(rect);
					if (event.getEvent().isControlDown())
						newBitSet.xor(oldBitSet);
					lyrVect.getRecordset().setSelection(newBitSet);
				}
			}

		} catch (ReadDriverException e) {
			throw new BehaviorException("No se pudo hacer la selección");
		} catch (VisitorException e) {
			throw new BehaviorException("No se pudo hacer la selección");
		}
	}

	public Vec3 coordinatesIntersection(Point2D pScreen) {
		Vec3 intersection;

		// System.err.println("Coordenadas de pantalla " + pScreen.getX() + ","+
		// pScreen.getY());
		MapContext3D mcontext = (MapContext3D) this.mapControl.getMapContext();
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
				intersection = geoPt;
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
