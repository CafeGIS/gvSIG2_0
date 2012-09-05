package org.gvsig.gvsig3d.simbology3D.geometry3D;

import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.simbology3D.symbol3D.I3DSymbol;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.PixelPoint;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class Point3DGeometry extends Abstract3DGeometry {

	protected Map<Double, PixelPoint> pointsMap;
	
	private static Logger logger = Logger.getLogger(Point3DGeometry.class.getName());

	public Point3DGeometry(IGeometry geometry) {
		super(geometry);

	}
	
	private Node getPointNode(double key) {
		PixelPoint pixelPoint= null;

		if (pointsMap.containsKey(new Double(key))) {
			// System.out.println("ya contiene esa key");
			pixelPoint = pointsMap.get(new Double(key));
		} else {
			try {
				pixelPoint = new PixelPoint();
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pointsMap.put(new Double(key), pixelPoint);
		}

		return pixelPoint;
	}


	public Group generateGeometry(I3DSymbol symbol3D) {
		Group groupAux = new Group();
		Node node = null;

		if (getGeometry() == null || symbol3D == null)
			return null;

		this.setGeomType(getGeometry().getGeometryType());
		// TRANSFORMANDO A GEOMETRIA CON VALOR Z

		// Getting the Iterator
		PathIterator theIteratorL = getGeometry().getPathIterator(null);

		double[] dataLine = new double[6];
		List<Vec3> posi = new ArrayList<Vec3>();
		int contH = 0;
		// System.out.println("ENTRO");
		while (!theIteratorL.isDone()) {
			// System.out.println("ITERO");
			int type = theIteratorL.currentSegment(dataLine);
			Vec3 position = getGeometryPosition(getGeometry(), dataLine,
					getHeigth(), contH);

			// Adding points
			switch (type) {
			case PathIterator.SEG_MOVETO:
				// System.out.println("SEG_MOVETO");
				// FeatureFactory.addPointToNode(node, pos, rgba, pointSize);
				// posi.add(pos);
				// INSERT HERE THE CODE TO MAKE TETURED QUAD POINT
//				List<Vec3> posit = new ArrayList<Vec3>();
				posi.add(position);
				node = symbol3D.generateSymbol(posi);
				try {
						groupAux.addChild(node);
					} catch (NodeException e) {
						logger.equals("command: " + "Error adding new child" + e);
					}
				// group.addChild(generateFeature(posit,new
				// Vec4(1.0,0.0,0.0,1.0),sym));
				// group.addChild();
				// pointListAux.add(new Punto3D(pos, rgba, pointSize));
				break;

			case PathIterator.SEG_LINETO:
				// System.out.println("SEG_LINETO");
				posi.add(position);
				break;

			case PathIterator.SEG_QUADTO:
				// System.out.println("SEG_QUADTO");
				break;

			case PathIterator.SEG_CUBICTO:
				// System.out.println("SEG_CUBICTO");
				break;

			case PathIterator.SEG_CLOSE:
				// System.out.println("SEG_CLOSE");
				break;
			}
			contH++;
			theIteratorL.next();
		}
		return groupAux;

	}

}
