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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elemento shape de tipo Polígono.
 *
 * @author Vicente Caballero Navarro
 */
public class SHPPolygon extends SHPMultiLine {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SHPPolygon.class);

	/**
	 * Crea un nuevo SHPPolygon.
	 */
	public SHPPolygon() {
		m_type = SHP.POLYGON2D;
	}

	/**
	 * Crea un nuevo SHPPolygon.
	 *
	 * @param type Tipo de shape.
	 *
	 * @throws ShapefileException
	 */
	public SHPPolygon(int type) {
		if ((type != SHP.POLYGON2D) &&
				(type != SHP.POLYGONM) &&
				(type != SHP.POLYGON3D)) {
//			throw new ShapefileException("No es de tipo 5, 15, o 25");
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
	public synchronized Geometry read(MappedByteBuffer buffer, int type) {
		double minX = buffer.getDouble();
		double minY = buffer.getDouble();
		double maxX = buffer.getDouble();
		double maxY = buffer.getDouble();
		Rectangle2D rec = new Rectangle2D.Double(minX, minY, maxX - minX,
				maxY - maxY);
		int numParts = buffer.getInt();
		int numPoints = buffer.getInt();

		int[] partOffsets = new int[numParts];

		for (int i = 0; i < numParts; i++) {
			partOffsets[i] = buffer.getInt();
		}

		Point[] points = readPoints(buffer, numPoints);

		/* if (m_type == FConstant.SHAPE_TYPE_POLYGONZ) {
		   //z
		   buffer.position(buffer.position() + (2 * 8));
		   for (int t = 0; t < numPoints; t++) {
		       points[t].z = buffer.getDouble();
		   }
		   }
		 */
		int offset = 0;
		int start;
		int finish;
		int length;

		for (int part = 0; part < numParts; part++) {
			start = partOffsets[part];

			if (part == (numParts - 1)) {
				finish = numPoints;
			} else {
				finish = partOffsets[part + 1];
			}

			length = finish - start;

			Point[] pointsPart = new Point[length];

			for (int i = 0; i < length; i++) {
				pointsPart[i] = points[offset++];
			}
		}

		try {
			return geomManager.createSurface(getGeneralPathX(points, partOffsets), SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a surface", e);
			return null;
		}
	}

	/**
	 * Lee los puntos del buffer.
	 *
	 * @param buffer
	 * @param numPoints Número de puntos.
	 *
	 * @return Vector de Puntos.
	 */
	private synchronized Point[] readPoints(final MappedByteBuffer buffer,
		final int numPoints) {
		Point[] points = new Point[numPoints];

		for (int t = 0; t < numPoints; t++) {
			try {
				points[t] = geomManager.createPoint(buffer.getDouble(), buffer.getDouble(), SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				logger.error("Error creating a point", e);
			}
		}

		return points;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#write(ByteBuffer, IGeometry)
	 */
	public synchronized void write(ByteBuffer buffer, Geometry geometry) {
		//FPolygon2D polyLine;
		//polyLine = (FPolygon2D) geometry.getShape();
		Envelope env = geometry.getEnvelope();

		buffer.putDouble(env.getMinimum(0));
		buffer.putDouble(env.getMinimum(1));
		buffer.putDouble(env.getMaximum(0));
		buffer.putDouble(env.getMaximum(1));
		//////
		///obtainsPoints(geometry.getGeneralPathXIterator());

		//int[] parts=polyLine.getParts();
		//FPoint2D[] points=polyLine.getPoints();
		int nparts = parts.length;
		int npoints = points.length;

		//////
		///int npoints = polyLine.getNumPoints();
		///int nparts = polyLine.getNumParts();
		buffer.putInt(nparts);
		buffer.putInt(npoints);

//		int count = 0;

		for (int t = 0; t < nparts; t++) {
			///buffer.putInt(polyLine.getPart(t));
			buffer.putInt(parts[t]);
		}

		///FPoint[] points = polyLine.getPoints();
		for (int t = 0; t < points.length; t++) {
			///buffer.putDouble(points[t].x);
			///buffer.putDouble(points[t].y);
			buffer.putDouble(points[t].getX());
			buffer.putDouble(points[t].getY());
		}

		if (m_type == SHP.POLYGON3D) {
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

		if ((m_type == SHP.POLYGONM) ||
				(m_type == SHP.POLYGON3D)) {
			buffer.putDouble(-10E40);
			buffer.putDouble(-10E40);

			for (int t = 0; t < npoints; t++) {
				buffer.putDouble(-10E40);
			}
		}
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#getLength(com.iver.cit.gvsig.core.BasicShape.FGeometry)
	 */
	public synchronized int getLength(Geometry fgeometry) {
		// FPolygon2D multi;
		//multi = (FPolygon2D) fgeometry.getShape();
		///int nrings = 0;
		///obtainsPoints(fgeometry.getGeneralPathXIterator());

		//int[] parts=multi.getParts();
		//FPoint2D[] points;
		/////////
		//points = multi.getPoints();
		int npoints = points.length;

		///////////
		///nrings = multi.getNumParts();
		///int npoints = multi.getNumPoints();
		int length;

		if (m_type == SHP.POLYGON3D) {
			length = 44 + (4 * parts.length) + (16 * npoints) + (8 * npoints) +
				16;
		} else if (m_type == SHP.POLYGONM) {
			length = 44 + (4 * parts.length) + (16 * npoints) + (8 * npoints) +
				16;
		} else if (m_type == SHP.POLYGON2D) {
			length = 44 + (4 * parts.length) + (16 * npoints);
		} else {
			throw new IllegalStateException(
				"Expected ShapeType of Polygon, got " + m_type);
		}

		return length;
	}
}
