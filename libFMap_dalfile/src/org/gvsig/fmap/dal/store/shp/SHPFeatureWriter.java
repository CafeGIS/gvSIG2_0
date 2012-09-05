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
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal.store.shp;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.store.dbf.DBFFeatureWriter;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.shp.utils.SHPFileWrite;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHPFeatureWriter extends DBFFeatureWriter {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);

	private SHPFileWrite shpWrite;
	private Envelope envelope=null;
	private int[] supportedGeometryTypes;
	private Geometry defaultGeometry;
	private int fileSize;
	private FeatureType shpFeatureType;


	protected SHPFeatureWriter(String name) {
		super(name);
		try {
			this.defaultGeometry = geomManager.createNullGeometry(SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Creating a NullGeometry", e);
		}
	}

	public void begin(DBFStoreParameters dbfParameters,
			FeatureType featureType, FeatureType dbfFeatureType, long numRows)
			throws DataException {
		SHPStoreParameters shpParameters = (SHPStoreParameters) dbfParameters;
		File shpFile = shpParameters.getSHPFile();
		File shxFile = shpParameters.getSHXFile();

		FileChannel shpChannel = null;
		FileChannel shxChannel = null;

		try {
			shpChannel = (FileChannel) getWriteChannel(shpFile
					.getAbsolutePath());
			shxChannel = (FileChannel) getWriteChannel(shxFile
					.getAbsolutePath());
		} catch (IOException e) {
			throw new WriteException(this.name, e);
		}

		shpWrite = new SHPFileWrite(shpChannel, shxChannel);
		int gvSIG_geometryType =featureType.getAttributeDescriptor(
				featureType.getDefaultGeometryAttributeName())
				.getGeometryType();
		int gvSIG_geometrySubType = featureType.getAttributeDescriptor(
				featureType.getDefaultGeometryAttributeName())
				.getGeometrySubType();
		this.setSupportedGeometryTypes(gvSIG_geometryType);
		int shapeType = 0;
		shapeType = shpWrite
				.getShapeType(gvSIG_geometryType, gvSIG_geometrySubType);
		try {
			shpWrite.writeHeaders(geomManager.createEnvelope(SUBTYPES.GEOM2D),
						shapeType, 0, 0);
		} catch (CreateEnvelopeException e) {
			logger.error("Error creating the envelope", e);
		}

		this.shpFeatureType = featureType;
		super.begin(dbfParameters, dbfFeatureType, numRows);

	}

	public void dispose() {
		super.dispose();
		this.envelope = null;
		this.shpWrite = null;
	}

	public void end() throws DataException {
			if (envelope == null) {
				try {
					envelope = geomManager.createEnvelope(SUBTYPES.GEOM2D);
				} catch (CreateEnvelopeException e) {
					logger.error("Error creating the envelope", e);
				}
			}
			int gvSIG_geometryType = shpFeatureType.getAttributeDescriptor(
				shpFeatureType.getDefaultGeometryAttributeIndex())
					.getGeometryType();
			int gvSIG_geometrySubType = shpFeatureType.getAttributeDescriptor(
					shpFeatureType.getDefaultGeometryAttributeIndex())
						.getGeometryType();
			this.setSupportedGeometryTypes(gvSIG_geometryType);
			int shapeType = shpWrite
					.getShapeType(gvSIG_geometryType, gvSIG_geometrySubType);
			shpWrite.writeHeaders(envelope,
					shapeType, super.getRowCount(),
				fileSize);
			super.end();
			shpWrite.close();
	}

	public void append(Feature feature) throws DataException {

		Geometry theGeom = feature.getDefaultGeometry();
		if (theGeom==null){
			theGeom=this.defaultGeometry;
		}
		if (!canWriteGeometry(theGeom.getType())){
			throw new WriteException(this.name, // FIXME Excepcion correcta
					new RuntimeException("UnsupportedGeometryType: "+ theGeom.getGeometryType().getName()));
		}
//		numRows++;
		super.append(feature);

//		fileSize = shpWrite.writeIGeometry(theGeom);
		fileSize=shpWrite.writeIGeometry(theGeom);
		Envelope envelope = theGeom.getEnvelope();
		if (envelope!=null){
			if (this.envelope!=null) {
				this.envelope.add(envelope);
			} else {
				this.envelope=envelope;
			}
		}
	}

	private void setSupportedGeometryTypes(int gvSIG_geometryType) {
		switch (gvSIG_geometryType)
		{
		case Geometry.TYPES.POINT:
			supportedGeometryTypes = new int[] {Geometry.TYPES.POINT, Geometry.TYPES.NULL };
			break;
		case Geometry.TYPES.MULTIPOINT:
			supportedGeometryTypes = new int[] {Geometry.TYPES.MULTIPOINT, Geometry.TYPES.NULL };
			break;
		case Geometry.TYPES.MULTICURVE:
			supportedGeometryTypes = new int[] {Geometry.TYPES.CURVE, Geometry.TYPES.ELLIPSE,
				Geometry.TYPES.ARC,
					Geometry.TYPES.CIRCLE, Geometry.TYPES.MULTICURVE,
					Geometry.TYPES.NULL };
			break;
		case Geometry.TYPES.MULTISURFACE:
			supportedGeometryTypes = new int[] { Geometry.TYPES.SURFACE,
					Geometry.TYPES.MULTISURFACE, Geometry.TYPES.NULL };
			break;

		default:
			supportedGeometryTypes = new int[] {};
		}
	}
	public boolean canWriteGeometry(int gvSIGgeometryType) {
		for (int i=0; i < supportedGeometryTypes.length; i++)
		{
			if (gvSIGgeometryType == supportedGeometryTypes[i]) {
				return true;
			}
		}
		return false;
	}

	public void begin(DBFStoreParameters storeParameters,
			FeatureType featureType, long numRows) throws DataException {
		throw new UnsupportedOperationException();
	}

}
