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
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.store.shp.utils;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.bigfile.BigByteBuffer2;

/**
 * @author jmvivo
 *
 */
public class SHPFile {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SHPFile.class);
	private Envelope extent;
	private int type;
	private String srsParameters;

	private FileInputStream fin;
	private FileChannel channel;
	private BigByteBuffer2 bb;
	private FileInputStream finShx;
	private FileChannel channelShx;
	private BigByteBuffer2 bbShx;

	private SHPStoreParameters params;

	private int[] supportedGeometryTypes;
	private GeometryManager gManager = GeometryLocator.getGeometryManager();

	public SHPFile(SHPStoreParameters params) {
		this.params = params;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#doClose()
	 */
	public void close() throws CloseException {
		CloseException ret = null;

		//FIXME: Arreglar esto para que se acumulen los errores
		try {
			channel.close();
			channelShx.close();
		} catch (IOException e) {
			ret = new CloseException("SHPFile.close", e);
		} finally {
			try {
				fin.close();
				finShx.close();
			} catch (IOException e1) {
				ret = new CloseException("SHPFile.close", e1);
			}
		}

		if (ret != null) {
			throw ret;
		}
		bb = null;
		bbShx = null;
		fin = null;
		finShx = null;
		channel = null;
		channelShx = null;
		srsParameters = null;
	}

	public boolean isOpen() {
		return this.fin != null;
	}

	public void open() throws DataException {
		try {
			fin = new FileInputStream(this.params.getSHPFile());
		} catch (java.io.FileNotFoundException e) {
			throw new FileNotFoundException(this.params.getSHPFileName());
		}


		// Open the file and then get a channel from the stream
		channel = fin.getChannel();

		// long size = channel.size();

		// Get the file's size and then map it into memory
		// bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
		try {
			bb = new BigByteBuffer2(channel, FileChannel.MapMode.READ_ONLY);
		} catch (IOException e) {
			throw new ReadException(this.params.getSHPFileName(), e);

		}
		try {
			finShx = new FileInputStream(this.params.getSHXFile());
		} catch (java.io.FileNotFoundException e) {
			throw new FileNotFoundException(this.params.getSHXFileName());
		}


		// Open the file and then get a channel from the stream
		channelShx = finShx.getChannel();

		//			long sizeShx = channelShx.size();

		// Get the file's size and then map it into memory
		// bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
		// bbShx = channelShx.map(FileChannel.MapMode.READ_ONLY, 0, sizeShx);
		try {
			bbShx = new BigByteBuffer2(channelShx,
					FileChannel.MapMode.READ_ONLY);
		} catch (IOException e) {
			throw new ReadException(this.params.getSHXFileName(), e);

		}
		bbShx.order(ByteOrder.BIG_ENDIAN);


		//			create a new header.
		ShapeFileHeader2 myHeader = new ShapeFileHeader2();

		bb.position(0);

		// read the header
		myHeader.readHeader(bb);

		double[] min = new double[2];
		min[0] = myHeader.myXmin;
		min[1] = myHeader.myYmin;
		double[] max = new double[2];
		max[0] = myHeader.myXmax;
		max[1] = myHeader.myYmax;

		try {
			extent = geomManager.createEnvelope(min[0], min[1], max[0], max[1], SUBTYPES.GEOM2D);
		} catch (CreateEnvelopeException e1) {
			logger.error("Error creating the envelope", e1);
		}
		//			extent = new Rectangle2D.Double(myHeader.myXmin, myHeader.myYmin,
		//			myHeader.myXmax - myHeader.myXmin,
		//			myHeader.myYmax - myHeader.myYmin);

		type = myHeader.myShapeType;

		this.initSupportedGeometryTypes();

		double x = myHeader.myXmin;
		double y = myHeader.myYmin;
		double w = myHeader.myXmax - myHeader.myXmin;
		double h = myHeader.myYmax - myHeader.myYmin;

		if (w == 0) {
			x -= 0.1;
			w = 0.2;
		}

		if (h == 0) {
			y -= 0.1;
			h = 0.2;
		}

		//TODO: SRS
		File prjFile = SHP.getPrjFile(this.params.getSHPFile());
		if (prjFile.exists()) {
			BufferedReader input=null;
			try {
				input =  new BufferedReader(new FileReader(prjFile));
			} catch (java.io.FileNotFoundException e) {
				throw new FileNotFoundException(prjFile.getAbsolutePath());
			}

			try {
				this.srsParameters = input.readLine();
			} catch (IOException e) {
				throw new ReadException("SHPFile.open prj", e);
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					// TODO ???
				}
			}



		} else {
			this.srsParameters = null;
		}
	}

	public Envelope getFullExtent() throws ReadException {
		return this.extent;
	}

	public boolean isEditable(){
		return this.params.getSHPFile().canWrite()
				&& this.params.getSHXFile().canWrite();
	}

	public int getGeometryType() throws ReadException {
		int auxType = 0;

		switch (type) {
		case (SHP.POINT2D):
		case (SHP.POINT3D):
			auxType = auxType | Geometry.TYPES.POINT;

		break;

		case (SHP.POLYLINE2D):
		case (SHP.POLYLINE3D):
			auxType = auxType | Geometry.TYPES.MULTICURVE;

		break;

		case (SHP.POLYGON2D):
		case (SHP.POLYGON3D):
			auxType = auxType | Geometry.TYPES.MULTISURFACE;

		break;
		case (SHP.MULTIPOINT2D):
		case (SHP.MULTIPOINT3D):
			auxType = auxType | Geometry.TYPES.MULTIPOINT;

		break;
		}

		return auxType;
	}
	public int getGeometrySubType() throws ReadException {
		switch (type) {
		case (SHP.POINT2D):
		case (SHP.POLYLINE2D):
		case (SHP.POLYGON2D):
		case (SHP.MULTIPOINT2D):
			return SUBTYPES.GEOM2D;
		case (SHP.POINT3D):
		case (SHP.POLYLINE3D):
		case (SHP.POLYGON3D):
		case (SHP.MULTIPOINT3D):
			return SUBTYPES.GEOM2DZ;
		case (SHP.POINTM):
		case (SHP.POLYLINEM):
		case (SHP.POLYGONM):
		case (SHP.MULTIPOINTM):
			return SUBTYPES.GEOM2DM;
		}

		return SUBTYPES.UNKNOWN;
	}
	public synchronized Geometry getGeometry(long position)
			throws ReadException, CreateGeometryException {
		Point2D p = new Point2D.Double();
		int numParts;
		int numPoints;
		int i;
		int j;
		int shapeType;

		bb.position(getPositionForRecord(position));
		bb.order(ByteOrder.LITTLE_ENDIAN);

		shapeType = bb.getInt();
		//el shape tal con tema tal y númro tal es null
		if (shapeType==SHP.NULL){
			return geomManager.createNullGeometry(SUBTYPES.GEOM2D);
		}

		// retrieve that shape.
		// tempRecord.setShape(readShape(tempShapeType, tempContentLength, in));
		switch (type) {
		case (SHP.POINT2D):
			p = readPoint(bb);

		Point point = (Point)gManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
		point.setX(p.getX());
		point.setY(p.getY());
		return point;

		case (SHP.POLYLINE2D):

			bb.position(bb.position() + 32);
		numParts = bb.getInt();
		numPoints = bb.getInt();

		// part indexes.
		// Geometry geom = GeometryFactory.toGeometryArray();
		GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
				numPoints);

		int[] tempParts = new int[numParts];

		for (i = 0; i < numParts; i++) {
			tempParts[i] = bb.getInt();
		}

		j = 0;

		for (i = 0; i < numPoints; i++) {
			p = readPoint(bb);

			if (i == tempParts[j]) {
				elShape.moveTo(p.getX(), p.getY());

				if (j < (numParts - 1)) {
					j++;
				}
			} else {
				elShape.lineTo(p.getX(), p.getY());
			}
		}

		Curve curve = (Curve)gManager.create(TYPES.CURVE , SUBTYPES.GEOM2D);
		curve.setGeneralPath(elShape);
		return curve;

		case (SHP.POLYGON2D):

			//	            	BoundingBox = readRectangle(bb);
//			bb.getDouble();
//			bb.getDouble();
//			bb.getDouble();
//			bb.getDouble();
			bb.position(bb.position() + 32);
		numParts = bb.getInt();

		numPoints = bb.getInt();

		// part indexes.
		// Geometry geom = GeometryFactory.toGeometryArray();
		elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, numPoints);

		tempParts = new int[numParts];

		for (i = 0; i < numParts; i++) {
			tempParts[i] = bb.getInt();
		}

		j = 0;

		for (i = 0; i < numPoints; i++) {
			p = readPoint(bb);

			if (i == tempParts[j]) {
				elShape.moveTo(p.getX(), p.getY());

				if (j < (numParts - 1)) {
					j++;
				}
			} else {
				if (i==numPoints-1){
					elShape.closePath();
				}else{
					elShape.lineTo(p.getX(), p.getY());
				}
			}
		}

		Surface surface = (Surface)gManager.create(TYPES.SURFACE , SUBTYPES.GEOM2D);
		surface.setGeneralPath(elShape);
		return surface;

		case (SHP.POINT3D):

			double x = bb.getDouble();
		double y = bb.getDouble();
		double z = bb.getDouble();

		Point point3D = (Point)gManager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
		point3D.setX(p.getX());
		point3D.setY(p.getY());
		point3D.setCoordinateAt(2, z);
		return point3D;

		case (SHP.POLYLINE3D):
			bb.position(bb.position() + 32);
		numParts = bb.getInt();
		numPoints = bb.getInt();
		elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, numPoints);
		tempParts = new int[numParts];

		for (i = 0; i < numParts; i++) {
			tempParts[i] = bb.getInt();
		}

		j = 0;

		for (i = 0; i < numPoints; i++) {
			p = readPoint(bb);

			if (i == tempParts[j]) {
				elShape.moveTo(p.getX(), p.getY());

				if (j < (numParts - 1)) {
					j++;
				}
			} else {
				elShape.lineTo(p.getX(), p.getY());
			}
		}

		double[] boxZ = new double[2];
		boxZ[0] = bb.getDouble();
		boxZ[1] = bb.getDouble();

		double[] pZ = new double[numPoints];

		Curve curve3D = (Curve)gManager.create(TYPES.CURVE , SUBTYPES.GEOM2DZ);
		curve3D.setGeneralPath(elShape);

		for (i = 0; i < numPoints; i++) {
			curve3D.setCoordinateAt(i, 2, bb.getDouble());
		}

		return curve3D;

		case (SHP.POLYGON3D):
			bb.position(bb.position() + 32);
		numParts = bb.getInt();
		numPoints = bb.getInt();
		elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, numPoints);
		tempParts = new int[numParts];

		for (i = 0; i < numParts; i++) {
			tempParts[i] = bb.getInt();
		}

		j = 0;

		for (i = 0; i < numPoints; i++) {
			p = readPoint(bb);

			if (i == tempParts[j]) {
				elShape.moveTo(p.getX(), p.getY());

				if (j < (numParts - 1)) {
					j++;
				}
			} else {
				if (i==numPoints-1){
					elShape.closePath();
				}else{
					elShape.lineTo(p.getX(), p.getY());
				}
			}
		}

		double[] boxpoZ = new double[2];
		boxpoZ[0] = bb.getDouble();
		boxpoZ[1] = bb.getDouble();

		double[] poZ = new double[numPoints];

		Surface surface3D = (Surface)gManager.create(TYPES.SURFACE, SUBTYPES.GEOM2DZ);
		surface3D.setGeneralPath(elShape);

		for (i = 0; i < numPoints; i++) {
			surface3D.setCoordinateAt(i, 2, bb.getDouble());
		}

		return surface3D;

		case (SHP.MULTIPOINT2D):
			bb.position(bb.position() + 32);
		numPoints = bb.getInt();


		Point[] points = new Point[numPoints];

		for (i = 0; i < numPoints; i++) {
			points[i] = (Point)gManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			points[i].setX(bb.getDouble());
			points[i].setY(bb.getDouble());
		}

		MultiPoint multipoint = (MultiPoint)GeometryLocator.getGeometryManager().create(TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
		for (int k=0 ; k<points.length ; k++){
			multipoint.addPoint(points[k]);
		}

		return multipoint;

		case (SHP.MULTIPOINT3D):
			bb.position(bb.position() + 32);
		numPoints = bb.getInt();

		double[] temX = new double[numPoints];
		double[] temY = new double[numPoints];
		double[] temZ = new double[numPoints];

		for (i = 0; i < numPoints; i++) {
			temX[i] = bb.getDouble();
			temY[i] = bb.getDouble();
			//temZ[i] = bb.getDouble();
		}

		for (i = 0; i < numPoints; i++) {
			temZ[i] = bb.getDouble();
		}

		MultiPoint multipoint3D = (MultiPoint)gManager.create(TYPES.MULTIPOINT, SUBTYPES.GEOM2DZ);
		for (int k=0 ; k<temX.length ; k++){
			Point pointAux = (Point)gManager.create(TYPES.POINT ,SUBTYPES.GEOM2DZ);
			pointAux.setX(temX[k]);
			pointAux.setY(temY[k]);
			pointAux.setCoordinateAt(2, temZ[k]);
			multipoint3D.addPoint(pointAux);
		}
		return multipoint3D;
		}

		return null;


	}

	private long getPositionForRecord(long numRec)
	{
		// shx file has a 100 bytes header, then, records
		// 8 bytes length, one for each entity.
		// first 4 bytes are the offset
		// next 4 bytes are length

		int posIndex = 100 + ((int)numRec * 8);
		// bbShx.position(posIndex);
		long pos = 8 + 2* bbShx.getInt(posIndex);

		return pos;
	}
	/**
	 * Reads the Point from the shape file.
	 *
	 * @param in ByteBuffer.
	 *
	 * @return Point2D.
	 */
	private synchronized Point2D readPoint(BigByteBuffer2 in){
		// create a new point
		Point2D.Double tempPoint = new Point2D.Double();

		// bytes 1 to 4 are the type and have already been read.
		// bytes 4 to 12 are the X coordinate
		in.order(ByteOrder.LITTLE_ENDIAN);
		tempPoint.setLocation(in.getDouble(), in.getDouble());

		return tempPoint;
	}

	/**
	 * Lee un rectángulo del fichero.
	 *
	 * @param in ByteBuffer.
	 *
	 * @return Rectángulo.
	 * @throws CreateEnvelopeException
	 *
	 * @throws IOException
	 */
	private synchronized Envelope readRectangle(BigByteBuffer2 in) throws CreateEnvelopeException{
		in.order(ByteOrder.LITTLE_ENDIAN);
		double x = in.getDouble();
		double y = in.getDouble();

		double x2 = in.getDouble();

		if (x2-x == 0) {
			x2 = 0.2;
			x -= 0.1;
		}

		double y2 = in.getDouble();

		if (y2-y == 0) {
			y2 = 0.2;
			y -= 0.1;
		}
		Envelope tempEnvelope = geomManager.createEnvelope(x,y,x2,y2, SUBTYPES.GEOM2D);
		return tempEnvelope;
	}

	public Envelope getBoundingBox(long featureIndex) throws ReadException, CreateEnvelopeException {
		Point2D p = new Point2D.Double();
		Envelope BoundingBox = null;
		try {
			bb.position(getPositionForRecord(featureIndex));
		}catch (Exception e) {
			throw new ReadException("getBondingBox (" + featureIndex + ")", e);
//			logger.error(" Shapefile is corrupted. Drawing aborted. ="+e+ "  "+"index = "+index);
		}
		bb.order(ByteOrder.LITTLE_ENDIAN);

		int tipoShape = bb.getInt();

		//AZABALA: si tipoShape viene con valores erroneos deja de funcionar
		//el metodo getShape(i)
//		if (tipoShape != SHP.NULL) {
//		type = tipoShape;

//		}

		// retrieve that shape.
		// tempRecord.setShape(readShape(tempShapeType, tempContentLength, in));
		switch (tipoShape) {
		case (SHP.POINT2D):
		case (SHP.POINT3D):
			p = readPoint(bb);
		BoundingBox = geomManager.createEnvelope(p.getX() - 0.1, p.getY() - 0.1,p.getX()+0.2,p.getY()+0.2, SUBTYPES.GEOM2D);
//		new Rectangle2D.Double(p.getX() - 0.1,
//		p.getY() - 0.1, 0.2, 0.2);

		break;

		case (SHP.POLYLINE2D):
		case (SHP.POLYGON2D):
		case (SHP.MULTIPOINT2D):
		case (SHP.POLYLINE3D):
		case (SHP.POLYGON3D):
		case (SHP.MULTIPOINT3D):

			// BoundingBox
			BoundingBox = readRectangle(bb);

		break;
		}

		return BoundingBox;
	}

	/**
	 * @return
	 */
	public String getSRSParameters() {
		return this.srsParameters;
	}

	private void initSupportedGeometryTypes() throws ReadException {
		switch (this.getGeometryType()) {
		case Geometry.TYPES.POINT:
			supportedGeometryTypes = new int[] { Geometry.TYPES.POINT,
					Geometry.TYPES.NULL };
			break;
		case Geometry.TYPES.MULTIPOINT:
			supportedGeometryTypes = new int[] { Geometry.TYPES.MULTIPOINT,
					Geometry.TYPES.NULL };
			break;
		case Geometry.TYPES.CURVE:
			supportedGeometryTypes = new int[] { Geometry.TYPES.CURVE,
					Geometry.TYPES.ELLIPSE, Geometry.TYPES.ARC,
					Geometry.TYPES.CIRCLE, Geometry.TYPES.SURFACE,
					Geometry.TYPES.NULL };
			break;
		case Geometry.TYPES.SURFACE:
			supportedGeometryTypes = new int[] { Geometry.TYPES.ELLIPSE,
					Geometry.TYPES.CIRCLE, Geometry.TYPES.SURFACE,
					Geometry.TYPES.NULL };
			break;

		default:
			supportedGeometryTypes = new int[] {};
		}
	}

	public boolean canWriteGeometry(int gvSIGgeometryType) {
		for (int i = 0; i < supportedGeometryTypes.length; i++) {
			if (gvSIGgeometryType == supportedGeometryTypes[i]) {
				return true;
			}
		}
		return false;
	}
}

