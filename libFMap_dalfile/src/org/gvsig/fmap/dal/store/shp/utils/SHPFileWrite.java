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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class SHPFileWrite {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SHPFileWrite.class);
	private SHPShape m_shape = null;
	private ByteBuffer m_bb = null;
	private ByteBuffer m_indexBuffer = null;
	private int m_pos = 0;
	private int m_offset;
	//	private int m_type;
	private int m_cnt;
	private FileChannel shpChannel;
	private FileChannel shxChannel;
//	private double flatness;

	/**
	 * Crea un nuevo SHPFileWrite.
	 *
	 * @param shpChannel DOCUMENT ME!
	 * @param shxChannel DOCUMENT ME!
	 */
	public SHPFileWrite(FileChannel shpChannel, FileChannel shxChannel) {
		this.shpChannel = shpChannel;
		this.shxChannel = shxChannel;
	}

	/**
	 * Make sure our buffer is of size.
	 *
	 * @param size DOCUMENT ME!
	 */
	private void checkShapeBuffer(int size) {
		if (m_bb.capacity() < size) {
			m_bb = ByteBuffer.allocateDirect(size);
		}
	}

	/**
	 * Drain internal buffers into underlying channels.
	 * @throws WriteException
	 *
	 * @throws IOException DOCUMENT ME!
	 */
	private void drain() throws WriteException {
		m_bb.flip();
		m_indexBuffer.flip();
		try{
		while (m_bb.remaining() > 0) {
			shpChannel.write(m_bb);
		}

		while (m_indexBuffer.remaining() > 0) {
			shxChannel.write(m_indexBuffer);
		}
		}catch (IOException e) {
			throw new WriteException("SHP File Write Drain", e);
		}
		m_bb.flip().limit(m_bb.capacity());
		m_indexBuffer.flip().limit(m_indexBuffer.capacity());
	}

	/**
	 * DOCUMENT ME!
	 */
	private void allocateBuffers() {
		m_bb = ByteBuffer.allocateDirect(16 * 1024);
		m_indexBuffer = ByteBuffer.allocateDirect(100);
	}

	/**
	 * Close the underlying Channels.
	 */
	public void close() throws WriteException {
		try {
			shpChannel.close();
			shxChannel.close();
		} catch (IOException e) {
			throw new WriteException("SHP File Write Close", e);
		}
		shpChannel = null;
		shxChannel = null;
		m_shape = null;

		if (m_indexBuffer instanceof ByteBuffer) {
			if (m_indexBuffer != null) {
				///NIOUtilities.clean(m_indexBuffer);
			}
		}

		if (m_indexBuffer instanceof ByteBuffer) {
			if (m_indexBuffer != null) {
				///NIOUtilities.clean(m_bb);
			}
		}

		m_indexBuffer = null;
		m_bb = null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param geometries DOCUMENT ME!
	 * @param type DOCUMENT ME!
	 *
	 */
	public void write(Geometry[] geometries, int type)
			throws WriteException {
		m_shape = SHP.create(type);
//		m_shape.setFlatness(flatness);
		writeHeaders(geometries, type);

		m_pos = m_bb.position();

		for (int i = 0, ii = geometries.length; i < ii; i++) {
			writeGeometry(geometries[i]);
		}

		close();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param geometries DOCUMENT ME!
	 * @param type DOCUMENT ME!
	 * @throws WriteException
	 *
	 */
	private void writeHeaders(Geometry[] geometries, int type)
			throws WriteException {
		int fileLength = 100;
		Envelope extent = null;

		for (int i = geometries.length - 1; i >= 0; i--) {
			Geometry fgeometry = geometries[i];
			m_shape.obtainsPoints(fgeometry);
			int size = m_shape.getLength(fgeometry) + 8;
			fileLength += size;

			if (extent == null) {
				try {
					extent = geomManager.createEnvelope(SUBTYPES.GEOM2D);
				} catch (CreateEnvelopeException e) {
					logger.error("Error creating the envelope", e);
				}
			} else {
				extent.add(fgeometry.getEnvelope());
			}
		}

		writeHeaders(extent, type, geometries.length, fileLength);
	}

	/**
	 * Writes shape header (100 bytes)
	 *
	 * @param bounds DOCUMENT ME!
	 * @param type DOCUMENT ME!
	 * @param numberOfGeometries DOCUMENT ME!
	 * @param fileLength DOCUMENT ME!
	 */
	public void writeHeaders(Envelope bounds, int type,
		int numberOfGeometries,
			int fileLength) throws WriteException {
		/*try {
		   handler = type.getShapeHandler();
		   } catch (ShapefileException se) {
		     throw new RuntimeException("unexpected Exception",se);
		   }
		 */
		if (m_bb == null) {
			allocateBuffers();
		}
		// Posicionamos al principio.
		m_bb.position(0);
		m_indexBuffer.position(0);

		ShapeFileHeader2 header = new ShapeFileHeader2();

		header.write(m_bb, type, numberOfGeometries, fileLength / 2,
			bounds
				.getMinimum(0), bounds.getMinimum(1), bounds.getMaximum(0),
			bounds.getMaximum(1), 0, 0, 0, 0);

		header.write(m_indexBuffer, type, numberOfGeometries,
			50 + (4 * numberOfGeometries), bounds.getMinimum(0), bounds.getMinimum(1),
			bounds.getMaximum(0), bounds.getMaximum(1), 0, 0, 0, 0);

		m_offset = 50;
//		m_type = type;
		m_cnt = 0;

		try {
			shpChannel.position(0);
			shxChannel.position(0);
		} catch (IOException e) {
			throw new WriteException("SHP File Write Headers", e);
		}
		drain();
	}
	public int writeIGeometry(Geometry g) throws WriteException {
		int shapeType = getShapeType(g.getType(), g.getGeometryType().getSubType());
		m_shape = SHP.create(shapeType);
//		m_shape.setFlatness(flatness);
		// System.out.println("writeIGeometry: type="+ g.getType());
		return writeGeometry(g);
	}

	/**
	 * Writes a single Geometry.
	 *
	 * @param g
	 * @return the position of buffer (after the last geometry, it will allow you to
	 * write the file size in the header.
	 */
	public synchronized int writeGeometry(Geometry g)
			throws WriteException {
		if (m_bb == null) {
			allocateBuffers();
			m_offset = 50;
			m_cnt = 0;

			try {
				shpChannel.position(0);
				shxChannel.position(0);
			} catch (IOException e) {
				throw new WriteException("SHP File Write", e);
			}
			// throw new IOException("Must write headers first");
		}

		m_pos = m_bb.position();
		m_shape.obtainsPoints(g);
		int length = m_shape.getLength(g);

		// must allocate enough for shape + header (2 ints)
		checkShapeBuffer(length + 8);

		length /= 2;

		m_bb.order(ByteOrder.BIG_ENDIAN);
		m_bb.putInt(++m_cnt);
		m_bb.putInt(length);
		m_bb.order(ByteOrder.LITTLE_ENDIAN);
		m_bb.putInt(m_shape.getShapeType());
		m_shape.write(m_bb, g);

		///assert (length * 2 == (m_bb.position() - m_pos) - 8);
		m_pos = m_bb.position();

		// write to the shx
		m_indexBuffer.putInt(m_offset);
		m_indexBuffer.putInt(length);
		m_offset += (length + 4);
		drain();

		///assert(m_bb.position() == 0);
		return m_pos; // Devolvemos hasta donde hemos escrito
	}

	/**
	 * Returns a shapeType compatible with shapeFile constants from a gvSIG's IGeometry type
	 * @param geometryType
	 * @return a shapeType compatible with shapeFile constants from a gvSIG's IGeometry type
	 */
	public int getShapeType(int geometryType, int geometrySubType) {

		if (geometrySubType == Geometry.SUBTYPES.GEOM2DZ){
			switch (geometryType) {
			case Geometry.TYPES.POINT:
				return SHP.POINT3D;

			case Geometry.TYPES.CURVE:
			case Geometry.TYPES.MULTICURVE:
			case Geometry.TYPES.ELLIPSE:
			case Geometry.TYPES.CIRCLE:
			case Geometry.TYPES.ARC:
				return SHP.POLYLINE3D;

			case Geometry.TYPES.SURFACE:
			case Geometry.TYPES.MULTISURFACE:
				return SHP.POLYGON3D;

			case Geometry.TYPES.MULTIPOINT:
				return SHP.MULTIPOINT3D; //TODO falta aclarar cosas aquí.
		}

		}else{
			switch (geometryType) {
				case Geometry.TYPES.POINT:
					return SHP.POINT2D;

				case Geometry.TYPES.CURVE:
				case Geometry.TYPES.MULTICURVE:
				case Geometry.TYPES.ELLIPSE:
				case Geometry.TYPES.CIRCLE:
				case Geometry.TYPES.ARC:
					return SHP.POLYLINE2D;

				case Geometry.TYPES.SURFACE:
				case Geometry.TYPES.MULTISURFACE:
					return SHP.POLYGON2D;

				case Geometry.TYPES.MULTIPOINT:
					return SHP.MULTIPOINT2D; //TODO falta aclarar cosas aquí.
			}
		}
			return SHP.NULL;
		}

//	public void setFlatness(double flatness) {
//		this.flatness=flatness;
//	}

}
