/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.fmap.geom.operation.towkb;

import java.awt.geom.PathIterator;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.Aggregate;
import org.gvsig.fmap.geom.aggregate.MultiCurve;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.aggregate.MultiSurface;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;



public class WKBEncoder {

	public interface wkbGeometryType {
		public final static int wkbPoint = 1;
		public final static int wkbLineString = 2;
		public final static int wkbPolygon = 3;
		public final static int wkbTriangle = 17;
		public final static int wkbMultiPoint = 4;
		public final static int wkbMultiLineString = 5;
		public final static int wkbMultiPolygon = 6;
		public final static int wkbGeometryCollection = 7;
		public final static int wkbPolyhedralSurface = 15;
		public final static int wkbTIN = 16;

		public final static int wkbPointZ = 1001;
		public final static int wkbLineStringZ = 1002;
		public final static int wkbPolygonZ = 1003;
		public final static int wkbTrianglez = 1017;
		public final static int wkbMultiPointZ = 1004;
		public final static int wkbMultiLineStringZ = 1005;
		public final static int wkbMultiPolygonZ = 1006;
		public final static int wkbGeometryCollectionZ = 1007;
		public final static int wkbPolyhedralSurfaceZ = 1015;
		public final static int wkbTINZ = 1016;

		public final static int wkbPointM = 2001;
		public final static int wkbLineStringM = 2002;
		public final static int wkbPolygonM = 2003;
		public final static int wkbTriangleM = 2017;
		public final static int wkbMultiPointM = 2004;
		public final static int wkbMultiLineStringM = 2005;
		public final static int wkbMultiPolygonM = 2006;
		public final static int wkbGeometryCollectionM = 2007;
		public final static int wkbPolyhedralSurfaceM = 2015;
		public final static int wkbTINM = 2016;

		public final static int wkbPointZM = 3001;
		public final static int wkbLineStringZM = 3002;
		public final static int wkbPolygonZM = 3003;
		public final static int wkbTriangleZM = 3017;
		public final static int wkbMultiPointZM = 3004;
		public final static int wkbMultiLineStringZM = 3005;
		public final static int wkbMultiPolygonZM = 3006;
		public final static int wkbGeometryCollectionZM = 3007;
		public final static int wkbPolyhedralSurfaceZM = 3015;
		public final static int wkbTinZM = 3016;

		public final static int wkb_baseToZ = wkbPointZ - wkbPoint;
		public final static int wkb_baseToM = wkbPointM - wkbPoint;
		public final static int wkb_baseToZM = wkbPointZM - wkbPoint;
	}

	public interface wkbByteOrder {
		public final static byte wkbXDR = 0;                // Big Endian
		public final static byte wkbNDR = 1;                // Little Endian
	}


	public byte[] encode(Geometry geometry)
			throws GeometryTypeNotSupportedException,
			WKBEncodingException,
			IOException {
		ByteArrayOutputStream stream;

		if (geometry.getType() == TYPES.POINT) {
			stream = new ByteArrayOutputStream(50);
		} else {
			stream = new ByteArrayOutputStream(512);
		}

		encode(geometry, stream);
		return stream.toByteArray();

	}
	public void encode(Geometry geometry, ByteArrayOutputStream stream)
			throws GeometryTypeNotSupportedException, WKBEncodingException,
			IOException {
		encode(geometry, new DataOutputStream(stream));
	}

	public void encode(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException,
			WKBEncodingException,
			IOException {
		switch (geometry.getType()) {
			case TYPES.POINT:
				encodePoint(geometry, stream);
				break;
			case TYPES.MULTIPOINT:
				encodeMultiPoint(geometry, stream);
				break;

			case TYPES.ARC:
			case TYPES.CURVE:
			case TYPES.CIRCLE:
			case TYPES.ELLIPSE:
			case TYPES.ELLIPTICARC:
				encodeLineString(geometry, stream);
				break;

			case TYPES.MULTICURVE:
				encodeMultiLineString(geometry, stream);
				break;

			case TYPES.SURFACE:
				encodePolygon(geometry, stream);
				break;

			case TYPES.MULTISURFACE:
				encodeMultiPolygon(geometry, stream);
				break;

			case TYPES.AGGREGATE:
				encodeCollection(geometry, stream);
				break;

			default:
				throw new GeometryTypeNotSupportedException(geometry.getType(),
					geometry.getGeometryType().getSubType());
		}

		// FIXME geom.subtype != SUBTYPES.GEOM2D NOT SUPPORTED !!!!
		if (geometry.getGeometryType().getSubType() != SUBTYPES.GEOM2D) {
			throw new GeometryTypeNotSupportedException(geometry.getType(),
					geometry.getGeometryType().getSubType());
		}
	}


	private void encodeCollection(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException,
			WKBEncodingException,
			IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbGeometryCollection, geometryType,
				stream);

		Aggregate collection = (Aggregate) geometry;

		int nGeometries = collection.getPrimitivesNumber();
		stream.writeInt(nGeometries);
		for (int i = 0; i < nGeometries; i++) {
			encode(collection.getPrimitiveAt(i), stream);
		}
	}

	private void encodeMultiPolygon(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException,
			WKBPolygonNotClosedException, IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbMultiPolygon, geometryType, stream);

		MultiSurface surfaces = (MultiSurface) geometry;

		int nGeometries = surfaces.getPrimitivesNumber();
		stream.writeInt(nGeometries);
		for (int i = 0; i < nGeometries; i++) {
			encodePolygon(surfaces.getPrimitiveAt(i), stream);
		}
	}

	private void checkLinearRingIsClosed(Geometry geom, List linearRing)
			throws WKBPolygonNotClosedException {
		double[] first = (double[]) linearRing.get(0);
		double[] last = (double[]) linearRing.get(linearRing.size() - 1);

		for (int i = 0; i < first.length; i++) {
			if (Math.abs(first[i] - last[i]) > 0.000001) {
				throw new WKBPolygonNotClosedException(geom);
			}
		}

	}

	private List getLinearRings(Surface surface)
			throws WKBPolygonNotClosedException {
		PathIterator theIterator = surface.getPathIterator(null);
		List linearRings = new ArrayList();
		List curlinearRing = new ArrayList();
		int theType;
		double[] theData = new double[6];

		while (!theIterator.isDone()) {
			// while not done
			theType = theIterator.currentSegment(theData);

			// Populate a segment of the new
			// GeneralPathX object.
			// Process the current segment to populate a new
			// segment of the new GeneralPathX object.
			switch (theType) {
			case PathIterator.SEG_MOVETO:
				if (curlinearRing.size() != 0) {
					if (curlinearRing.size() < 4) {
						// FIXME exception
						throw new WKBPolygonNotClosedException(surface);
					}
					checkLinearRingIsClosed(surface, curlinearRing);
					linearRings.add(curlinearRing);
					curlinearRing = new ArrayList();

				}
				curlinearRing.add(new double[] { theData[0], theData[1] });
				break;

			case PathIterator.SEG_LINETO:
				curlinearRing.add(new double[] { theData[0], theData[1] });
				break;

			case PathIterator.SEG_QUADTO:
				// TODO transform to linear segments
				throw new IllegalArgumentException("SEG_QUADTO unsupported");

				// shape.quadTo(theData[0], theData[1], theData[2], theData[3]);

				// break;

			case PathIterator.SEG_CUBICTO:
				// TODO transform to linear segments
				throw new IllegalArgumentException("SEG_QUADTO unsupported");
				// shape.curveTo(theData[0], theData[1], theData[2], theData[3],
				// theData[4], theData[5]);

				// break;

			case PathIterator.SEG_CLOSE:
				curlinearRing.add(curlinearRing.get(0));
				linearRings.add(curlinearRing);
				curlinearRing = new ArrayList();
				break;
			} // end switch

			theIterator.next();
		}

		if (curlinearRing.size() != 0) {
			if (curlinearRing.size() < 4) {
				// FIXME exception
				throw new WKBPolygonNotClosedException(surface);
			}
			checkLinearRingIsClosed(surface, curlinearRing);
			linearRings.add(curlinearRing);
		}
		return linearRings;
	}

	private List getLines(Curve curve) throws WKBEncodingException {
		List lines = new ArrayList();
		PathIterator theIterator = curve.getPathIterator(null);
		List curlinearRing = new ArrayList();
		int theType;
		double[] theData = new double[6];

		while (!theIterator.isDone()) {
			// while not done
			theType = theIterator.currentSegment(theData);

			// Populate a segment of the new
			// GeneralPathX object.
			// Process the current segment to populate a new
			// segment of the new GeneralPathX object.
			switch (theType) {
			case PathIterator.SEG_MOVETO:
				if (curlinearRing.size() != 0) {
					if (curlinearRing.size() < 2) {
						throw new WKBOnePointLineException(curve);
					}
					lines.add(curlinearRing);
					curlinearRing = new ArrayList();
				}
				curlinearRing.add(new double[] { theData[0], theData[1] });

				break;

			case PathIterator.SEG_LINETO:
				curlinearRing.add(new double[] { theData[0], theData[1] });
				break;

			case PathIterator.SEG_QUADTO:
				// TODO transform to linear segments
				throw new IllegalArgumentException("SEG_QUADTO unsupported");

				// shape.quadTo(theData[0], theData[1], theData[2], theData[3]);

				// break;

			case PathIterator.SEG_CUBICTO:
				// TODO transform to linear segments
				throw new IllegalArgumentException("SEG_QUADTO unsupported");
				// shape.curveTo(theData[0], theData[1], theData[2], theData[3],
				// theData[4], theData[5]);

				// break;

			case PathIterator.SEG_CLOSE:
				curlinearRing.add(curlinearRing.get(0));
				break;
			} // end switch

			theIterator.next();
		}

		if (curlinearRing.size() != 0) {
			if (curlinearRing.size() < 2) {
				throw new WKBOnePointLineException(curve);
			}
			lines.add(curlinearRing);
		}
		return lines;
	}


	private void encodePolygon(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException,
			WKBPolygonNotClosedException, IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbPolygon, geometryType, stream);

		Surface surface = (Surface) geometry;

		List linearRings = getLinearRings(surface);
		stream.writeInt(linearRings.size());
		for (int i = 0; i < linearRings.size(); i++) {
			encodeLinearRing((List) linearRings.get(i), stream);
		}

	}

	private void encodeLinearRing(List linearRing, DataOutputStream stream)
			throws IOException {
		stream.writeInt(linearRing.size());
		for (int i = 0; i < linearRing.size(); i++) {
			encodeCoordinates((double[]) linearRing.get(i), stream);
		}
	}

	private void encodeMultiLineString(Geometry geometry,
			DataOutputStream stream)
			throws GeometryTypeNotSupportedException,
			WKBEncodingException, IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbMultiLineString, geometryType,
				stream);

		MultiCurve curves = (MultiCurve) geometry;

		int nGeometries = curves.getPrimitivesNumber();
		stream.writeInt(nGeometries);
		for (int i = 0; i < nGeometries; i++) {
			encodeLineString(curves.getPrimitiveAt(i), stream);
		}
	}

	private void encodeLineString(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException, WKBEncodingException,
			IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbLineString, geometryType,
				 stream);

		Curve curve = (Curve) geometry;

		List lines = getLines(curve);
		if (lines.size() != 1) {
			throw new WKBMultyLineInLineDefinitionException(curve);
		}

		List line = (List) lines.get(0);
		int nVertices = line.size();
		stream.writeInt(nVertices);
		for (int i = 0; i < nVertices; i++) {
			encodeCoordinates((double[]) line.get(i), stream);
		}

	}


	private void encodeCoordinates(double[] coordinates, DataOutputStream stream)
			throws IOException {
		for (int i = 0; i < coordinates.length; i++) {
			stream.writeDouble(coordinates[i]);
		}
	}

	private void encodeWKBGeomHead(int wkbBaseType, GeometryType geometryType,
			DataOutputStream stream) throws GeometryTypeNotSupportedException,
			IOException {

		stream.writeByte(wkbByteOrder.wkbXDR);

		int finalType = wkbBaseType % wkbGeometryType.wkb_baseToZ;

		switch (geometryType.getSubType()) {
		case SUBTYPES.GEOM2D:
			break;
		case SUBTYPES.GEOM2DM:
			finalType = finalType + wkbGeometryType.wkb_baseToM;
			break;

		case SUBTYPES.GEOM2DZ:
		case SUBTYPES.GEOM3D:
			finalType = finalType + wkbGeometryType.wkb_baseToZ;
			break;

		case SUBTYPES.GEOM3DM:
			finalType = finalType + wkbGeometryType.wkb_baseToZM;
			break;


		default:
			throw new GeometryTypeNotSupportedException(geometryType.getType(),
					geometryType.getSubType());

		}

		stream.writeInt(finalType);


	}


	private void encodePoint(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException, IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbPoint, geometryType,
				stream);

		Point point = (Point) geometry;
		double[] coords = point.getCoordinates();
		for (int i = 0; i < coords.length; i++) {
			stream.writeDouble(coords[i]);
		}

	}

	private void encodeMultiPoint(Geometry geometry, DataOutputStream stream)
			throws GeometryTypeNotSupportedException, IOException {
		GeometryType geometryType = geometry.getGeometryType();
		encodeWKBGeomHead(wkbGeometryType.wkbMultiPoint, geometryType, stream);

		MultiPoint points = (MultiPoint) geometry;

		int nGeometries = points.getPrimitivesNumber();
		stream.writeInt(nGeometries);
		for (int i=0;i<nGeometries;i++) {
			encodePoint(points.getPrimitiveAt(i), stream);
		}
	}



}
