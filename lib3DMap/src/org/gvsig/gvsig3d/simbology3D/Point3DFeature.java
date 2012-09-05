package org.gvsig.gvsig3d.simbology3D;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.simbology3D.geometry3D.Point3DGeometry;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.point.CharacterPoint3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.point.ExtrusionPoint3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.point.MultiLayerPoint3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.point.ObjectPoint3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.point.PicturePoint3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.point.SimplePoint3DSymbol;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.symbology.fmap.symbols.CharacterMarkerSymbol;
import org.gvsig.symbology.fmap.symbols.PictureMarkerSymbol;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.ai2.gvsig3d.legend.symbols.Object3DMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.core.symbols.MultiLayerMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleMarkerSymbol;

public class Point3DFeature extends AbstractFeature3D {

	private Point3DGeometry p3DGeometry;
	//private I3DSymbol p3DSymbol;
	private Abstract3DSymbol p3DSymbol;

	private static Logger logger = Logger.getLogger(Point3DFeature.class
			.getName());

	public Point3DFeature(ISymbol symbol, IGeometry geometry) {
		super(symbol, geometry);

		p3DGeometry = new Point3DGeometry(geometry);
		// p3DSymbol = new SimplePolygon3DSymbol(symbol);
		
		if (this.getSymbol().getClass().equals(BaseExtrusionSymbol.class)) {
			p3DSymbol = new ExtrusionPoint3DSymbol(symbol);
		}
		else if (this.getSymbol().getClass().equals(SimpleMarkerSymbol.class)) {
			p3DSymbol = new SimplePoint3DSymbol(symbol);
		} else if (this.getSymbol().getClass().equals(PictureMarkerSymbol.class)) {
			p3DSymbol = new PicturePoint3DSymbol(symbol);
		} else if (this.getSymbol().getClass().equals(CharacterMarkerSymbol.class)) {
			p3DSymbol = new CharacterPoint3DSymbol(symbol);
		} else if (this.getSymbol().getClass().equals(MultiLayerMarkerSymbol.class)) {
			p3DSymbol = new MultiLayerPoint3DSymbol(symbol);
		} else if (this.getSymbol().getClass().equals(Object3DMarkerSymbol.class)) {
			p3DSymbol = new ObjectPoint3DSymbol(symbol);
		} 

	}

	public Group Draw(Group group) {
		try {
			group.addChild(p3DGeometry.generateGeometry(p3DSymbol));
		} catch (NodeException e) {
			logger.error("Command: " + "Error adding new child.", e);
		}
		return group;
	}

	public void setPlanet(Planet planet) {
		if (this.p3DGeometry != null) {
			this.p3DGeometry.setPlanet(planet);
		}
	}

	public void setHeigth(float heigth) {
		if (this.p3DGeometry != null) {
			this.p3DGeometry.setHeigth(heigth);
		}
	}

	// public Group generateGeometry(Group group) {
	// Node node = null;
	//
	// if (geometry == null || symbol == null)
	// return null;
	//
	// geomType = geometry.getGeometryType();
	//		
	//		
	// // Setup point hash map
	// List pointListAux = null;
	// if (pointsMap==null)
	// pointsMap = new HashMap<Double, List>();
	// if ((geomType & FShape.POINT) == FShape.POINT) {
	// if (pointsMap.containsKey(new Integer(pointSize))) {
	// // System.out.println("ya contiene esa key");
	// pointListAux = (List) pointsMap.get(new Double(pointSize));
	// } else {
	// pointListAux = new ArrayList();
	// pointsMap.put(new Double(pointSize), pointListAux);
	// }
	//
	// }
	//
	//
	// // Getting the Iterator
	// PathIterator theIteratorL = geometry.getPathIterator(null);
	//
	// double[] dataLine = new double[6];
	// List<Vec3> posi = new ArrayList<Vec3>();
	// int contH = 0;
	// // System.out.println("ENTRO");
	// while (!theIteratorL.isDone()) {
	// // System.out.println("ITERO");
	// int type = theIteratorL.currentSegment(dataLine);
	//			
	// Vec3 position = getGeometryPosition(geometry,dataLine,heigth,contH);
	// // Adding points
	// switch (type) {
	// case PathIterator.SEG_MOVETO:
	// // System.out.println("SEG_MOVETO");
	// // FeatureFactory.addPointToNode(node, pos, rgba, pointSize);
	// // posi.add(position);
	// pointListAux.add(new Punto3D(position, rgba, pointSize));
	// break;
	//
	// case PathIterator.SEG_LINETO:
	// // System.out.println("SEG_LINETO");
	// posi.add(position);
	// break;
	//
	// case PathIterator.SEG_QUADTO:
	// // System.out.println("SEG_QUADTO");
	// break;
	//
	// case PathIterator.SEG_CUBICTO:
	// // System.out.println("SEG_CUBICTO");
	// break;
	//
	// case PathIterator.SEG_CLOSE:
	// // System.out.println("SEG_CLOSE");
	// break;
	// }
	// contH++;
	// theIteratorL.next();
	// }
	// // System.out.println("Numero de puntos: " + contH);
	//
	// // Adding last symbol
	// // System.err.println("tamaño de posi " + posi.size());
	// // node = FeatureFactory.insertPoints(posi, rgba, pointSize);
	// // group.addChild(node);
	// // posi.clear();
	// return group;
	// return null;
	// }

}
