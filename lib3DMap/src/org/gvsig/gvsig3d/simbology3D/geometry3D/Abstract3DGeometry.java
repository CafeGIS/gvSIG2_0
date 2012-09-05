package org.gvsig.gvsig3d.simbology3D.geometry3D;


import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.simbology3D.symbol3D.I3DSymbol;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.planets.Planet;

import com.iver.cit.gvsig.fmap.core.FGeometry;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;

public abstract class Abstract3DGeometry implements I3DGeometry {

	private int geomType;
	private IGeometry geometry;
	private boolean zEnable = false;

	private float vertEx = 1;
	private float heigth;
	private Planet planet;
	
	private static Logger logger = Logger.getLogger(Abstract3DGeometry.class.getName());

	public Abstract3DGeometry(IGeometry geometry) {
		this.geometry = geometry;
	}

	public Group generateGeometry(I3DSymbol symbol3D) {
		Group groupAux = new Group();
		Node node = null;

		if (geometry == null || symbol3D == null)
			return null;

		geomType = geometry.getGeometryType();

		// Getting the Iterator
		PathIterator theIteratorL = geometry.getPathIterator(null);

		double[] dataLine = new double[6];
		List<Vec3> posi = new ArrayList<Vec3>();
		int contH = 0;
		while (!theIteratorL.isDone()) {
			int type = theIteratorL.currentSegment(dataLine);

			Vec3 position = getGeometryPosition(geometry, dataLine, heigth,
					contH);
			// Adding points
			switch (type) {
			case PathIterator.SEG_MOVETO:
				// System.out.println("SEG_MOVETO");
				node = symbol3D.generateSymbol(posi);
				if (node != null) {
					try {
						groupAux.addChild(node);
					} catch (NodeException e) {
						logger.error("Command:" + "Error setting new child.",e);
					}
					posi.clear();
					posi = new ArrayList<Vec3>();
				}
				posi.add(position);
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
		// System.out.println("Numero de puntos: " + contH);

		// Adding last symbol
		if (posi.size() > 0) {
			// if ((geomType & FShape.LINE) == FShape.LINE) {
			// }
			node = symbol3D.generateSymbol(posi);
			if (node != null)
				try {
					groupAux.addChild(node);
				} catch (NodeException e) {
					logger.error("Command:" + "Error adding new child.",e);
				}
		}
		return groupAux;

	}

	public Vec3 getGeometryPosition(IGeometry geometry, double[] dataLine,
			float heigth, int contH) {
		// TRANSFORMANDO A GEOMETRIA CON VALOR Z
		FGeometry g3D = null;
		if (geometry instanceof FGeometry) {
			g3D = (FGeometry) geometry;
		}
		Vec3 posGeo = null;
		Vec3 pos = null;
		double h = 0;
		if (isZEnable()) {
			if ((geomType & FShape.Z) == FShape.Z) {
				if (g3D != null) {
					h = (g3D.getZs()[contH]) * vertEx;
				}
			}
		} else {
			h = heigth;
		}

		if (planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC) {
			posGeo = new Vec3(dataLine[1], dataLine[0], h);
			pos = planet.convertLatLongHeightToXYZ(posGeo);
		} else {
			pos = new Vec3(dataLine[0], dataLine[1], h);
		}
		return pos;
	}

	public int getGeomType() {
		return geomType;
	}

	public void setGeomType(int geomType) {
		this.geomType = geomType;
	}

	public IGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(IGeometry geometry) {
		this.geometry = geometry;
	}

	public boolean isZEnable() {
		return zEnable;
	}

	public void setZEnable(boolean enable) {
		zEnable = enable;
	}

	public float getVertEx() {
		return vertEx;
	}

	public void setVertEx(float vertEx) {
		this.vertEx = vertEx;
	}

	public float getHeigth() {
		return heigth;
	}

	public void setHeigth(float heigth) {
		this.heigth = heigth;
	}

	public Planet getPlanet() {
		return planet;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

}
