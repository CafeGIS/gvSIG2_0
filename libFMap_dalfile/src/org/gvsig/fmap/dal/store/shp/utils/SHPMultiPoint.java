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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elemento shape de tipo multipunto.
 *
 * @author Vicente Caballero Navarro
 */
public class SHPMultiPoint implements SHPShape {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SHPMultiPoint.class);
	private int m_type;
	private int numpoints;
	private Point2D[] points;
	private double[] zs;

	/**
	 * Crea un nuevo SHPMultiPoint.
	 */
	public SHPMultiPoint() {
		m_type = SHP.MULTIPOINT2D;
	}

	/**
	 * Crea un nuevo SHPMultiPoint.
	 *
	 * @param type Tipo de multipunto.
	 *
	 * @throws ShapefileException
	 */
	public SHPMultiPoint(int type) {
		if ((type != SHP.MULTIPOINT2D) &&
				(type != SHP.MULTIPOINTM) &&
				(type != SHP.MULTIPOINT3D)) {
//			throw new ShapefileException("No es de tipo 8, 18, o 28");
		}

		m_type = type;
	}

	/**
	 * Devuelve el tipo de multipoint en concreto.
	 *
	 * @return Tipo de multipoint.
	 */
	public int getShapeType() {
		return m_type;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#read(MappedByteBuffer, int)
	 */
	public Geometry read(MappedByteBuffer buffer, int type) {
		double minX = buffer.getDouble();
		double minY = buffer.getDouble();
		double maxX = buffer.getDouble();
		double maxY = buffer.getDouble();
		Rectangle2D rec = new Rectangle2D.Double(minX, minY, maxX - minX,
				maxY - maxY);
		int numpoints = buffer.getInt();
		org.gvsig.fmap.geom.primitive.Point[] p = new org.gvsig.fmap.geom.primitive.Point[numpoints];

		for (int t = 0; t < numpoints; t++) {
			double x = buffer.getDouble();
			double y = buffer.getDouble();
			try {
				p[t] = geomManager.createPoint(x, y, SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				logger.error("Error creating a point", e);
			}
		}

		/*   if (m_type == FConstant.SHAPE_TYPE_MULTIPOINTZ) {
		   buffer.position(buffer.position() + (2 * 8));
		   for (int t = 0; t < numpoints; t++) {
		       p[t].z = buffer.getDouble(); //z
		   }
		   }
		 */
		MultiPoint multipoint = null;
		try {
			multipoint = (MultiPoint)geomManager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
			for (int i=0 ; i<p.length ; i++){
				multipoint.addPoint(p[i]);
			}			
		} catch (CreateGeometryException e) {
			logger.error("Error creating the multipoint", e);
		}
		return multipoint;		
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#write(ByteBuffer, IGeometry)
	 */
	public void write(ByteBuffer buffer, Geometry geometry) {
		// FMultiPoint2D mp = (FMultiPoint2D) geometry.getShape();
//		int p = buffer.position();

		Envelope env = geometry.getEnvelope();

		buffer.putDouble(env.getMinimum(0));
		buffer.putDouble(env.getMinimum(1));
		buffer.putDouble(env.getMaximum(0));
		buffer.putDouble(env.getMaximum(1));
		///obtainsPoints(geometry.getGeneralPathXIterator());
		buffer.putInt(numpoints);

		for (int t = 0, tt = numpoints; t < tt; t++) {
			Point2D point = points[t];
			buffer.putDouble(point.getX());
			buffer.putDouble(point.getY());
		}

		  if (m_type == SHP.MULTIPOINT3D) {
		   double[] zExtreame = SHP.getZMinMax(zs);
		   if (Double.isNaN(zExtreame[0])) {
		       buffer.putDouble(0.0);
		       buffer.putDouble(0.0);
		   } else {
		       buffer.putDouble(zExtreame[0]);
		       buffer.putDouble(zExtreame[1]);
		   }
		   for (int t = 0; t < numpoints; t++) {
		       double z = zs[t];
		       if (Double.isNaN(z)) {
		           buffer.putDouble(0.0);
		       } else {
		           buffer.putDouble(z);
		       }
		   }
		   }
		   if ((m_type == SHP.MULTIPOINTM) ||
		           (m_type == SHP.MULTIPOINT3D)) {
		       buffer.putDouble(-10E40);
		       buffer.putDouble(-10E40);
		       for (int t = 0; t < numpoints; t++) {
		           buffer.putDouble(-10E40);
		       }
		   }

	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#getLength(com.iver.cit.gvsig.core.BasicShape.FGeometry)
	 */
	public int getLength(Geometry fgeometry) {
		//FMultiPoint2D mp = (FMultiPoint2D) fgeometry.getShape();
		///obtainsPoints(fgeometry.getGeneralPathXIterator());

		int length;

		if (m_type == SHP.MULTIPOINT2D) {
			// two doubles per coord (16 * numgeoms) + 40 for header
			length = (numpoints * 16) + 40;
		} else if (m_type == SHP.MULTIPOINTM) {
			// add the additional MMin, MMax for 16, then 8 per measure
			length = (numpoints * 16) + 40 + 16 + (8 * numpoints);
		} else if (m_type == SHP.MULTIPOINT3D) {
			// add the additional ZMin,ZMax, plus 8 per Z
			length = (numpoints * 16) + 40 + 16 + (8 * numpoints);
		} else {
			throw new IllegalStateException("Expected ShapeType of Arc, got " +
				m_type);
		}

		return length;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.drivers.shp.write.SHPShape#obtainsPoints(com.iver.cit.gvsig.fmap.core.GeneralPathXIterator)
	 */
	public void obtainsPoints(Geometry g) {
		if (SHP.MULTIPOINT3D == m_type){
			MultiPoint multipoint = (MultiPoint)g;
			zs = new double[multipoint.getPrimitivesNumber()];
			for (int i=0 ; i<zs.length ; i++){
				zs[i] = multipoint.getPointAt(i).getCoordinateAt(2);
			}		
		}
		PathIterator theIterator = g.getPathIterator(null); //polyLine.getPathIterator(null, flatness);
		double[] theData = new double[6];
		ArrayList ps=new ArrayList();
		while (!theIterator.isDone()) {
			//while not done
//			int theType = theIterator.currentSegment(theData);
			theIterator.currentSegment(theData);

			ps.add(new Point2D.Double(theData[0], theData[1]));
			theIterator.next();
		} //end while loop
		points=(Point2D[])ps.toArray(new Point2D.Double[0]);
		numpoints=points.length;
	}
//	public void setFlatness(double flatness) {
//		//this.flatness=flatness;
//	}
}
