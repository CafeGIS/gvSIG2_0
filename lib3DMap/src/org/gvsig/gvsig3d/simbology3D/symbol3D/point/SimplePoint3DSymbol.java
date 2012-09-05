package org.gvsig.gvsig3d.simbology3D.symbol3D.point;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.core.osgdb.osgDB;
import org.gvsig.osgvp.exceptions.node.LoadNodeException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.PixelPoint;

import com.iver.cit.gvsig.fmap.core.symbols.IMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class SimplePoint3DSymbol extends Abstract3DSymbol {

	// Los tipo puntos tienen q tener una unica instancia de pixel point.
	// Podrian tener un hasMap como en el caso del piloto
	private static Map<Double, PixelPoint> pointsMap;
	private Vec4 rgba;
	private double size;
	private Node node;

	public SimplePoint3DSymbol(ISymbol symbol) {
		super(symbol);
		IMarkerSymbol marketSymbol = (IMarkerSymbol) symbol;

		// Symbol color and alpha value
		Color color = marketSymbol.getColor();
		rgba = new Vec4(color.getRed() / 255f, color.getGreen() / 255f, color
				.getBlue() / 255f, color.getAlpha());

		size = marketSymbol.getSize();

	}

	@Override
	public Node generateSymbol(List<Vec3> position) {
		// TODO Auto-generated method stub
		// return FeatureFactory.generateQuadPoligon(position.get(0),new
		// Vec4(1.0,0.0,0.0,1.0),null,400);
		if (node == null)
			node = FeatureFactory.insertPointL(position, rgba, (int) size);
		Vec3 newPosition = new Vec3(0, 0, 0);
		FeatureFactory.addPointToNode(node, newPosition, rgba, (int) size);
		return node;
	}
	
	public Node generateSymbol(Vec3 position) {
		// TODO Auto-generated method stub
		// return FeatureFactory.generateQuadPoligon(position.get(0),new
		// Vec4(1.0,0.0,0.0,1.0),null,400);
//		if (node == null)
//			node = FeatureFactory.insertPointL(position, rgba, (int) size);
//		Vec3 newPosition = new Vec3(0, 0, 0);
//		FeatureFactory.addPointToNode(node, newPosition, rgba, (int) size);
//		return node;
//		Sphere sphere = null;
//		try {
//			sphere = new Sphere();
//			sphere.setRadius((float) size);
//			sphere.setColor(rgba);
//			sphere.setCenter(position);
//		} catch (NodeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Node n = null;
		try {
			n = osgDB.readNodeFile("D:\\modelos 3d\\axes.osg") ;
		} catch (LoadNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;
	}

	private Node getPointNode(double key) {
		PixelPoint pixelPoint = null;

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

}
