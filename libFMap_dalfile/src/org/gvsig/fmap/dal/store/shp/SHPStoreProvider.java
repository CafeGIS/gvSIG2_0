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

import java.io.IOException;
import java.util.Iterator;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.PerformEditingException;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyChangesException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyCloseException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.dal.store.shp.utils.SHPFile;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHPStoreProvider extends DBFStoreProvider {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryManager.class);
	public static String NAME = "SHP";
	public static String DESCRIPTION = "SHP file";
	private SHPFile shpFile;
	private ResourceProvider shpResource;
	private ResourceProvider shxResource;


	protected static final String GEOMETRY_ATTIBUTE_NAME = "GEOMETRY";
	protected static final String DYNFIELD_SRSORIGINALPARAMETERS_NAME = "SRSOriginalParameters";
	private static final String DYNCLASS_NAME = "SHPStore";
	protected static DynClass DYNCLASS = null;

	private SHPFeatureWriter writer = null;

	public SHPStoreProvider(SHPStoreParameters params,
			DataStoreProviderServices storeServices)
			throws InitializeException {
		super(params, storeServices,ToolsLocator
				.getDynObjectManager().createDynObject(DYNCLASS));
	}

	protected void init(DBFStoreParameters params,
			DataStoreProviderServices storeServices) throws InitializeException {

		SHPStoreParameters shpParams = (SHPStoreParameters) params;
		shpResource = this.createResource(FileResource.NAME,
				new Object[] { shpParams.getSHPFileName() });
		shpResource.addConsumer(this);

		shxResource = this.createResource(FileResource.NAME,
				new Object[] { shpParams.getSHXFileName() });
		shxResource.addConsumer(this);

		this.shpFile = new SHPFile(shpParams);
		super.init(params, storeServices);

	}

	protected void resourcesBegin() throws ResourceBeginException {
		super.resourcesBegin();
		this.shpResource.begin();
		this.shxResource.begin();
		// TODO .prj

	}

	protected void resourcesEnd() {
		super.resourcesEnd();
		this.shpResource.end();
		this.shxResource.end();
		// TODO .prj

	}

	/**
	 *
	 * @throws ResourceNotifyChangesException
	 */
	protected void resourcesNotifyChanges()
			throws ResourceNotifyChangesException {
		super.resourcesNotifyChanges();
		this.shpResource.notifyChanges();
		this.shxResource.notifyChanges();
		// TODO .prj

	}

	/**
	 * @throws ResourceNotifyCloseException
	 *
	 */
	protected void resourcesNotifyClose() throws ResourceNotifyCloseException {
		super.resourcesNotifyClose();
		this.shpResource.notifyClose();
		this.shxResource.notifyClose();
		// TODO .prj

	}

	public void dispose() throws CloseException {
		super.dispose();
		this.shpResource.removeConsumer(this);
		this.shxResource.removeConsumer(this);
		this.shpResource = null;
		this.shxResource = null;
		this.writer = null;
		this.shpFile = null;
	}

	/**
	 * @throws ResourceNotifyOpenException
	 *
	 */
	protected void resourcesOpen() throws ResourceNotifyOpenException {
		super.resourcesOpen();
		this.shpResource.notifyOpen();
		this.shxResource.notifyOpen();
	}


	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {

			DynClass dbfDynClass = DBFStoreProvider.DYNCLASS;
			dynClass = dynman.add(DYNCLASS_NAME);


			field = dynClass.addDynField(DYNFIELD_SRSORIGINALPARAMETERS_NAME);
			field.setType(DataTypes.STRING);
			field.setDescription("SRS original parameters");

            // The SHP store parameters extend the DBF store parameters
			dynClass.extend(dbfDynClass);

			DYNCLASS = dynClass;
		}

	}


	protected static EditableFeatureAttributeDescriptor addGeometryColumn(
			EditableFeatureType fType) {

		EditableFeatureAttributeDescriptor attrTmp = null;
		EditableFeatureAttributeDescriptor attr = null;
		Iterator iter = fType.iterator();
		while (iter.hasNext()) {
			attrTmp = (EditableFeatureAttributeDescriptor) iter.next();
			if (attrTmp.getDataType() == DataTypes.GEOMETRY) {
				if (attr != null) {
					// Two geom fields not allowed
					fType.remove(attrTmp.getName());
				} else {
					attr = attrTmp;
					attr.setName(GEOMETRY_ATTIBUTE_NAME);
				}
			}


		}


		if (attr == null){
			attr = fType.add(
					GEOMETRY_ATTIBUTE_NAME, DataTypes.GEOMETRY);
			try {
				attr.setDefaultValue(geomManager.createNullGeometry(SUBTYPES.GEOM2D));
			} catch (CreateGeometryException e) {
				logger.error("Error creating the envelope", e);
			}
		}

		fType.setDefaultGeometryAttributeName(attr.getName());
		return attr;

	}

	protected static FeatureType removeGeometryColumn(
			EditableFeatureType fType) {
		Iterator iter = fType.iterator();
		FeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.getDataType() == DataTypes.GEOMETRY) {
				iter.remove();
			}
		}
		fType.setDefaultGeometryAttributeName(null);
		return fType.getNotEditableCopy();
	}

	protected EditableFeatureType getTheFeatureType()
			throws InitializeException {
		EditableFeatureType fType = super.getTheFeatureType();
		try {
			this.open();
			this.resourcesBegin();
		} catch (DataException e) {
			throw new InitializeException(this.getName(), e);
		}
		try {

			EditableFeatureAttributeDescriptor attr = addGeometryColumn(fType);
			attr.setGeometryType(this.shpFile.getGeometryType());
			attr.setGeometrySubType(this.shpFile.getGeometrySubType());
			// String srs = this.getSRSFromPrj(this.shpFile.getSRSParameters());
			// if (srs == null){
			// // TODO petar ??
			// srs = "EPSG:23030";
			// }
			IProjection srs = getShpParameters().getSRS();
			if (srs == null){
				srs = CRSFactory.getCRS("EPSG:23030");
			}

			attr.setSRS(srs);


			return fType;
		} catch (ReadException e) {
			throw new InitializeException(e);
		} finally {
			this.resourcesEnd();
		}
	}

	private String getSRSFromPrj(String srsParameters) {
		// TODO identificar que SRS hay que usar, ya sea
		// el que se recibe de los parametros o el que
		// conicida con el que se ha encontrado en el
		// prg... y si ninguna de las dos que?
		return null;
	}

	protected SHPStoreParameters getShpParameters() {
		return (SHPStoreParameters) getParameters();
	}

	public String getName() {
		return NAME;
	}

	public boolean allowWrite() {
		return super.allowWrite() && this.shpFile.isEditable();
	}

	/**
	 *
	 * @param index
	 * @param featureType
	 * @return
	 * @throws ReadException
	 */
	protected FeatureProvider getFeatureProviderByIndex(long index,
			FeatureType featureType) throws DataException {
		this.open();
		this.resourcesBegin();
		try {

			FeatureProvider featureProvider = super.getFeatureProviderByIndex(index,
					featureType);
			featureProvider.setDefaultEnvelope(this.shpFile.getBoundingBox(index));
			return featureProvider;
		} catch (DataException e) {
			throw e;
		} catch (CreateEnvelopeException e) {
			throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(e);
		} finally {
			this.resourcesEnd();
		}

	}

	protected void initFeatureProviderByIndex(FeatureProvider featureProvider,
			long index, FeatureType featureType) throws DataException {
		this.open();
		this.resourcesBegin();
		try {
			super.initFeatureProviderByIndex(featureProvider, index, featureType);
			featureProvider.setDefaultEnvelope(this.shpFile.getBoundingBox(index));
		} catch (CreateEnvelopeException e) {
			throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(e);
		} finally {
			this.resourcesEnd();
		}

	}

	/**
	 *
	 * @param featureProvider
	 * @throws DataException
	 */
	protected void loadFeatureProviderByIndex(FeatureProvider featureProvider)
			throws DataException {
		this.open();
		this.resourcesBegin();
		try {
			long index = ((Long) featureProvider.getOID()).longValue();
			super.loadFeatureProviderByIndex(featureProvider);
			if (featureProvider.getType().getIndex(
					featureProvider.getType().getDefaultGeometryAttributeName()) >= 0) {
				try {
					featureProvider.setDefaultGeometry(this.shpFile.getGeometry(index));
				} catch (CreateGeometryException e) {
					throw new ReadException(getName(), e);
				}
			}

		} finally {
			this.resourcesEnd();
		}
	}


	protected void loadValue(FeatureProvider featureProvider, int rowIndex,
			FeatureAttributeDescriptor descriptor) throws ReadException {
		if (descriptor.getDataType() == DataTypes.GEOMETRY) {
			return;
		} else {
			super.loadValue(featureProvider, rowIndex, descriptor);
		}
	}

	public FeatureProvider createFeatureProvider(FeatureType type) throws DataException {
		FeatureProvider data = new SHPFeatureProvider(this, type);
		return data;
	}


	protected void openFile() throws IOException, DataException {
		super.openFile();
		this.shpFile.open();

	}

	protected void loadMetadataValues() throws DynFieldNotFoundException,
			ReadException {
		super.loadMetadataValues();

		this.setDynValue(DYNFIELD_SRSORIGINALPARAMETERS_NAME,
				this.shpFile.getSRSParameters());

		this.setDynValue("DefaultSRS", this.getShpParameters()
				.getSRS());

		this.setDynValue("Envelope", shpFile.getFullExtent());

	}


	protected void closeFile() throws CloseException {
		super.closeFile();
		if (!this.shpFile.isOpen()) {
			return;
		}
		this.shpFile.close();
	}

	public boolean canWriteGeometry(int geometryType, int geometrySubType) throws DataException {
		this.open();
		this.resourcesBegin();
		try {
			return this.shpFile.canWriteGeometry(geometryType);

		} finally {
			this.resourcesEnd();
		}
	}

	public void performChanges(Iterator deleteds, Iterator inserteds,
			Iterator updateds, Iterator originalFeatureTypesUpdated) throws PerformEditingException {
		FeatureType fType;
		try {
			fType = this.getStoreServices().getDefaultFeatureType();
		} catch (DataException e) {
			throw new PerformEditingException(this.getName(), e);
		}
		// TODO Comprobar el campo de geometria

		EditableFeatureType dbfFtype = fType.getEditable();

		removeGeometryColumn(dbfFtype);

		try {
			this.resourcesBegin();
		} catch (ResourceBeginException e1) {
			throw new PerformEditingException(this.getName(), e1);
		}

		try {

			FeatureSet set = this.getFeatureStore().getFeatureSet();
			writer = new SHPFeatureWriter(this.getName());

			SHPStoreParameters shpParams = this.getShpParameters();
			SHPStoreParameters tmpParams = (SHPStoreParameters) shpParams
					.getCopy();
			tmpParams.setDBFFileName(tmpParams.getDBFFileName() + ".tmp");
			tmpParams.setSHPFileName(tmpParams.getSHPFileName() + ".tmp");
			tmpParams.setSHXFileName(tmpParams.getSHXFileName() + ".tmp");

			writer.begin(tmpParams, fType, dbfFtype, set.getSize());

			DisposableIterator iter = set.fastIterator();
			while (iter.hasNext()) {
				Feature feature=(Feature) iter.next();
				writer.append(feature);
			}

			writer.end();

			this.close();
			this.resourceCloseRequest();

			if (!shpParams.getDBFFile().delete()) {
				throw new PerformEditingException(this.getName(),
						new IOException(shpParams.getDBFFileName()));
			}
			if (!shpParams.getSHPFile().delete()) {
				throw new PerformEditingException(this.getName(),
						new IOException(shpParams.getSHPFileName()));
			}
			if (!shpParams.getSHXFile().delete()) {
				throw new PerformEditingException(this.getName(),
						new IOException(shpParams.getSHXFileName()));
			}
			if (!tmpParams.getDBFFile().renameTo(shpParams.getDBFFile())) {
				throw new PerformEditingException(this.getName(),
						new IOException(shpParams.getSHPFileName()));
			}
			if (!tmpParams.getSHPFile().renameTo(shpParams.getSHPFile())) {
				throw new PerformEditingException(this.getName(),
						new IOException(shpParams.getSHPFileName()));
			}
			if (!tmpParams.getSHXFile().renameTo(shpParams.getSHXFile())) {
				throw new PerformEditingException(this.getName(),
						new IOException(shpParams.getSHXFileName()));
			}


			this.resourcesNotifyChanges();
			this.initFeatureType();

		} catch (Exception e) {
			throw new PerformEditingException(this.getName(), e);
		} finally {
			this.resourcesEnd();
		}


	}

	protected void resourceCloseRequest() throws ResourceException {
		super.resourceCloseRequest();
		this.shpResource.closeRequest();
		this.shxResource.closeRequest();
	}

	public Envelope getEnvelope() throws DataException {
		this.open();
		return (Envelope) this.getDynValue("Envelope");
	}

	public void append(FeatureProvider featureProvider) throws DataException {
		this.resourcesBegin();
		try {
			writer
					.append(this.getStoreServices()
					.createFeature(
							featureProvider));
		} finally {
			this.resourcesEnd();
		}

	}

	public void beginAppend() throws DataException {
		this.resourcesBegin();
		try {

			FeatureStore store = this.getFeatureStore();
			FeatureType fType = store.getDefaultFeatureType();

			// TODO Comprobar el campo de geometria

			EditableFeatureType dbfFtype = fType.getEditable();

			removeGeometryColumn(dbfFtype);
			FeatureSet set = store.getFeatureSet();

			writer = new SHPFeatureWriter(this.getName());

			writer.begin(getShpParameters(), fType, dbfFtype, set.getSize());
		} finally {
			this.resourcesEnd();
		}

	}

	public void endAppend() throws DataException {
		this.resourcesBegin();
		try {
			writer.end();
			this.resourcesNotifyChanges();
		} finally {
			this.resourcesEnd();
		}

	}

	public Object getSourceId() {
		return this.getShpParameters().getFile();
	}

}
