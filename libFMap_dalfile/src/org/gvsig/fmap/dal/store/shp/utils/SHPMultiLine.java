/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.dal.store.shp.utils;

import java.awt.geom.PathIterator;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Elemento shape de tipo multilínea.
 *
 * @author Vicente Caballero Navarro
 */
public class SHPMultiLine implements SHPShape {
	protected int m_type;
	protected int[] parts;
	protected Point[] points;
	protected double[] zs;
	//double flatness = 0.8; // Por ejemplo. Cuanto más pequeño, más segmentos necesitará la curva
	private GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SHPShape.class);

	/**
	 * Crea un nuevo SHPMultiLine.
	 */
	public SHPMultiLine() {
		m_type = SHP.POLYLINE2D;
	}

	/**
	 * Crea un nuevo SHPMultiLine.
	 *
	 * @param type Tipo de multilínea.
	 *
	 * @throws ShapefileException
	 */
	public SHPMultiLine(int type) {
		if ((type != SHP.POLYLINE2D) &&
				(type != SHP.POLYLINEM) &&
				(type != SHP.POLYLINE3D)) {
//			throw new ShapefileException("No es de tipo 3,13 ni 23");
		}

		m_type = type;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#getShapeType()
	 */
	public int getShapeType() {
		return m_type;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#read(MappedByteBuffer, int)
	 */
	public Geometry read(MappedByteBuffer buffer, int type) {
//		double minX = buffer.getDouble();
//		double minY = buffer.getDouble();
//		double maxX = buffer.getDouble();
//		double maxY = buffer.getDouble();
//		Rectangle2D rec = new Rectangle2D.Double(minX, minY, maxX - minX, maxY
//				- maxY);

		int numParts = buffer.getInt();
		int numPoints = buffer.getInt(); //total number of points

		int[] partOffsets = new int[numParts];

		for (int i = 0; i < numParts; i++) {
			partOffsets[i] = buffer.getInt();
		}

		Point[] points = new Point[numPoints];

		for (int t = 0; t < numPoints; t++) {
			try {
				points[t] = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
				points[t] = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
				points[t].setX(buffer.getDouble());
				points[t].setY(buffer.getDouble());
			} catch (CreateGeometryException e) {
				logger.error("Error creating a point",e);
			}
		}

		/*   if (type == FConstant.SHAPE_TYPE_POLYLINEZ) {
		   //z min, max
		   buffer.position(buffer.position() + (2 * 8));
		   for (int t = 0; t < numPoints; t++) {
		       points[t].z = buffer.getDouble(); //z value
		   }
		   }
		 */
		Curve curve = null;
		try {
			curve = (Curve)geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
			curve.setGeneralPath(getGeneralPathX(points, partOffsets));
		} catch (CreateGeometryException e) {
			logger.error("Error creating the curve",e);
		}
		return curve;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#write(ByteBuffer, IGeometry)
	 */
	public void write(ByteBuffer buffer, Geometry geometry) {
		Envelope env = geometry.getEnvelope();

		buffer.putDouble(env.getMinimum(0));
		buffer.putDouble(env.getMinimum(1));
		buffer.putDouble(env.getMaximum(0));
		buffer.putDouble(env.getMaximum(1));
		int numParts = parts.length;
		int npoints = points.length;
		buffer.putInt(numParts);
		buffer.putInt(npoints);

		for (int i = 0; i < numParts; i++) {
			buffer.putInt(parts[i]);
		}

		for (int t = 0; t < npoints; t++) {
			buffer.putDouble(points[t].getX());
			buffer.putDouble(points[t].getY());
		}

		  if (m_type == SHP.POLYLINE3D) {
		   double[] zExtreame = SHP.getZMinMax(zs);
		   if (Double.isNaN(zExtreame[0])) {
		       buffer.putDouble(0.0);
		       buffer.putDouble(0.0);
		   } else {
		       buffer.putDouble(zExtreame[0]);
		       buffer.putDouble(zExtreame[1]);
		   }
		   for (int t = 0; t < npoints; t++) {
		       double z = zs[t];
		       if (Double.isNaN(z)) {
		           buffer.putDouble(0.0);
		       } else {
		           buffer.putDouble(z);
		       }
		   }

		   }
		   if (m_type == SHP.POLYLINEM) {
		       buffer.putDouble(-10E40);
		       buffer.putDouble(-10E40);
		       for (int t = 0; t < npoints; t++) {
		           buffer.putDouble(-10E40);
		       }
		   }

	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#getLength(int)
	 */
	public int getLength(Geometry fgeometry) {
		int numlines;
		int numpoints;
		int length;

		numlines = parts.length;
		numpoints = points.length;
		if (m_type == SHP.POLYLINE2D) {
			length = 44 + (4 * numlines) + (numpoints * 16);
		} else if (m_type == SHP.POLYLINEM) {
			length = 44 + (4 * numlines) + (numpoints * 16) +
				(8 * numpoints) + 16;
		} else if (m_type == SHP.POLYLINE3D) {
			length = 44 + (4 * numlines) + (numpoints * 16) +
				(8 * numpoints) + 16;
		} else {
			throw new IllegalStateException("Expected ShapeType of Arc, got " +
				m_type);
		}

		return length;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param po DOCUMENT ME!
	 * @param pa DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected GeneralPathX getGeneralPathX(Point[] po, int[] pa) {
		GeneralPathX gPX = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
				po.length);
		int j = 0;

		for (int i = 0; i < po.length; i++) {
			if (i == pa[j]) {
				gPX.moveTo(po[i].getX(), po[i].getY());

				if (j < (pa.length - 1)) {
					j++;
				}
			} else {
				gPX.lineTo(po[i].getX(), po[i].getY());
			}
		}

		return gPX;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#obtainsPoints(com.iver.cit.gvsig.fmap.core.GeneralPathXIterator)
	 */
	public void obtainsPoints(Geometry g) {
		boolean is3D=false;
		if (SHP.POLYLINE3D == m_type){
			Curve curve = (Curve)g;
			zs = new double[curve.getNumVertices()];
			for (int i=0 ; i<zs.length ; i++){
				zs[i] = curve.getCoordinateAt(i, 2);
			}
			is3D=true;
		}else if (SHP.POLYGON3D == m_type){
			Surface surface = (Surface)g;
			zs = new double[surface.getNumVertices()];
			for (int i=0 ; i<zs.length ; i++){
				zs[i] = surface.getCoordinateAt(i, 2);
			}
			is3D=true;
		}
		ArrayList arrayPoints = null;
		ArrayList arrayParts = new ArrayList();
		PathIterator theIterator = g.getPathIterator(null, Converter.FLATNESS); //polyLine.getPathIterator(null, flatness);
		double[] theData = new double[6];
		int numParts = 0;
		java.awt.geom.Point2D pFirst=null;
		int pos=0;
		ArrayList arrayZs=new ArrayList();
		Double firstZ = null;
		while (!theIterator.isDone()) {
			int theType = theIterator.currentSegment(theData);
			switch (theType) {
				case PathIterator.SEG_MOVETO:
					if (arrayPoints == null) {
						arrayPoints = new ArrayList();
						arrayParts.add(new Integer(0));
					} else {
						if (m_type==SHP.POLYGON2D ||
								m_type==SHP.POLYGON3D ||
								m_type==SHP.POLYGONM){
							try {
								Point point=geomManager.createPoint(pFirst.getX(), pFirst.getY(), SUBTYPES.GEOM2D);
								if (!arrayPoints.get(arrayPoints.size()-1).equals(point)){
									arrayPoints.add(point);
									if (is3D){
										arrayZs.add(firstZ);
									}
								}
							} catch (CreateGeometryException e) {
								logger.error("Error creating a point", e);
							}
						}
						arrayParts.add(new Integer(arrayPoints.size()));
					}

					numParts++;
					pFirst=new java.awt.geom.Point2D.Double(theData[0], theData[1]);
					try {
						arrayPoints.add(geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D));
					} catch (CreateGeometryException e1) {
						logger.error("Error creating a point", e1);
					}
					if (is3D){
						firstZ=new Double(zs[pos]);
						arrayZs.add(new Double(zs[pos]));
						pos++;
					}
					break;

				case PathIterator.SEG_LINETO:
					try {
						arrayPoints.add(geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D));
					} catch (CreateGeometryException e) {
						logger.error("Error creating a point", e);
					}
					if (is3D){
						arrayZs.add(new Double(zs[pos]));
						pos++;
					}
					break;

				case PathIterator.SEG_QUADTO:
					System.out.println("Not supported here");

					break;

				case PathIterator.SEG_CUBICTO:
					System.out.println("Not supported here");

					break;

				case PathIterator.SEG_CLOSE:
					try{
						Point point=geomManager.createPoint(pFirst.getX(), pFirst.getY(), SUBTYPES.GEOM2D);
						if (!arrayPoints.get(arrayPoints.size()-1).equals(point)){
							arrayPoints.add(point);
							if (is3D){
								arrayZs.add(firstZ);
							}
						}
					} catch (CreateGeometryException e) {
						logger.error("Error creating a point", e);
					}
					break;
			} //end switch
			theIterator.next();
		}
		Integer[] integers = (Integer[]) arrayParts.toArray(new Integer[0]);
		parts = new int[integers.length];
		for (int i = 0; i < integers.length; i++) {
			parts[i] = integers[i].intValue();
		}
		if (arrayPoints==null){
			points=new Point[0];
			return;
		}
		points = (Point[]) arrayPoints.toArray(new Point[0]);
		if (is3D){
			Double[] doubleZs=(Double[])arrayZs.toArray(new Double[0]);
			zs=new double[doubleZs.length];
			for (int i=0;i<doubleZs.length;i++){
				zs[i]=doubleZs[i].doubleValue();
			}
		}
	}
}
