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

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class SHPPoint implements SHPShape {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SHPPoint.class);
	private int m_type;
	private Point point;
	private double z;

	/**
	 * Crea un nuevo SHPPoint.
	 *
	 * @param type DOCUMENT ME!
	 *
	 * @throws ShapefileException DOCUMENT ME!
	 */
	public SHPPoint(int type)  {
		if ((type != SHP.POINT2D) &&
				(type != SHP.POINTM) &&
				(type != SHP.POINT3D)) { // 2d, 2d+m, 3d+m
//			throw new ShapefileException("No es un punto 1,11 ni 21");
		}

		m_type = type;
	}

	/**
	 * Crea un nuevo SHPPoint.
	 */
	public SHPPoint() {
		m_type = SHP.POINT2D; //2d
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
		double x = buffer.getDouble();
		double y = buffer.getDouble();
		double z = Double.NaN;

		if (m_type == SHP.POINTM) {
			buffer.getDouble();
		}

		if (m_type == SHP.POINT3D) {
			z = buffer.getDouble();
			Point point;
			try {
				point = geomManager.createPoint(x, y, SUBTYPES.GEOM2DZ);
				point.setCoordinateAt(2, z);
				return point;
			     //FIXME que hacems con esto
			} catch (CreateGeometryException e) {
				logger.error("Error creating a point", e);
			}			
		}
		try {
			return geomManager.createPoint(x, y, SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a point", e);
		}
		return null;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#write(ByteBuffer, IGeometry)
	 */
	public void write(ByteBuffer buffer, Geometry geometry) {
		//FPoint2D p2d = ((FPoint2D) geometry.getShape());
		///obtainsPoints(geometry.getGeneralPathXIterator());
		buffer.putDouble(point.getX());
		buffer.putDouble(point.getY());

		  if (m_type == SHP.POINT3D) {
		   if (Double.isNaN(z)) { // nan means not defined
		       buffer.putDouble(0.0);
		   } else {
		       buffer.putDouble(z);
		   }
		   }
		   if ((m_type == SHP.POINT3D) ||
		           (m_type == SHP.POINTM)) {
		       buffer.putDouble(-10E40); //M
		   }

	}

	/**
	 * @see com.iver.cit.gvsig.fmap.shp.SHPShape#getLength(int)
	 */
	public int getLength(Geometry fgeometry) {
		int length;

		if (m_type == SHP.POINT2D) {
			length = 20;
		} else if (m_type == SHP.POINTM || m_type == SHP.POINT3D) {
			length = 28;
		} else {
			throw new IllegalStateException("Expected ShapeType of Point, got" +
				m_type);
		}

		return length;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.drivers.shp.write.SHPShape#obtainsPoints(com.iver.cit.gvsig.fmap.core.GeneralPathXIterator)
	 */
	public void obtainsPoints(Geometry g) {
		if (SHP.POINT3D == m_type){
			z=((Point)g).getCoordinateAt(2);
		}
		PathIterator theIterator = g.getPathIterator(null); //polyLine.getPathIterator(null, flatness);
		double[] theData = new double[6];

		while (!theIterator.isDone()) {
			//while not done
//			int theType = theIterator.currentSegment(theData);
			theIterator.currentSegment(theData);

			try {
				point = geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				logger.error("Error creating a point", e);
			}

			theIterator.next();
		} //end while loop
	}
//	public void setFlatness(double flatness) {
//	//	this.flatness=flatness;
//	}
}
