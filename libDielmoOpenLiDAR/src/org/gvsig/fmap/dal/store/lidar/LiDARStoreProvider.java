/**
 *
 */
package org.gvsig.fmap.dal.store.lidar;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.geom.Geometry;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dielmo.lidar.BigByteBuffer2;
import com.dielmo.lidar.InicializeLidar;
import com.dielmo.lidar.LidarHeader;
import com.dielmo.lidar.LidarPoint;
import com.dielmo.lidar.UnexpectedLidarHeaderException;
import com.dielmo.lidar.fieldsDescription.fieldsDescription.ColumnDescription;
import com.dielmo.lidar.fieldsDescription.fieldsDescription.ContainerColumnDescription;

/**
 * @author jmvivo
 *
 */
public class LiDARStoreProvider extends AbstractFeatureStoreProvider implements
		ResourceConsumer {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(LiDARStoreProvider.class);

	public static String NAME = "LiDARStore";
	public static String DESCRIPTION = "LiDAR file";

	/**
	 * LiDAR file.
	 */
	private File m_Fich;

	/**
	 * Temporal LiDAR file for writer. This is used in edition time.
	 */
	private File fTemp;

	/**
	 * 8 Kbytes buffer
	 */
	private BigByteBuffer2 bb;

	/**
	 * Channel to read, write and manipulate the LiDAR file
	 */
	private FileChannel channel;

	/**
	 * management of input read data from LiDAR file.
	 */
	private FileInputStream fin;

	// informacion del LiDAR

	/**
	 * Definition of one type of LiDAR point.
	 */
	private LidarPoint lp;

	/**
	 * Definition of one type of LiDAR header.
	 */
	private LidarHeader hdr;

	// Writer
	/**
	 * Writer assigned to LiDAR
	 */
	// private LiDARWriter lidarWriter = new LiDARWriter();

	/**
	 * Temporal folder to work in edition mode.
	 */
	private static String tempDirectoryPath = System
			.getProperty("java.io.tmpdir");




	private static final String DYNCLASS_NAME = "LiDARStore";
	protected static DynClass DYNCLASS = null;
	private ResourceProvider liDARResource;


	private long counterNewsOIDs = -1;
	private boolean isOpen = false;



	public LiDARStoreProvider(LiDARStoreParameters params,
			DataStoreProviderServices storeServices)
			throws InitializeException {
		super(params, storeServices, ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));

		m_Fich = getLiDARParameters().getFile();
		liDARResource = this.createResource(FileResource.NAME,
				new Object[] { m_Fich.getAbsolutePath() });
		liDARResource.addConsumer(this);
		this.initFeatureType();

	}

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {

			dynClass = dynman.add(DYNCLASS_NAME);

            // The SHP store parameters extend the DBF store parameters
			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));

			DYNCLASS = dynClass;
		}

	}
	private LiDARStoreParameters getLiDARParameters() {
		return (LiDARStoreParameters) getParameters();
	}

	protected void initFeatureType() throws InitializeException {
		try {
			this.open();
		} catch (OpenException e) {
			throw new InitializeException(NAME, e);
		}
		LiDARStoreParameters liDARParams = getLiDARParameters();
		EditableFeatureType fType = this.getStoreServices().createFeatureType();
		FeatureType finalFType;

		fType.setHasOID(true);

		getFields(fType);

		EditableFeatureAttributeDescriptor attrGeo = fType.add("GEOMETRY",
				DataTypes.GEOMETRY);
		attrGeo.setAllowNull(false);
		attrGeo.setGeometryType(Geometry.TYPES.POINT);
		attrGeo.setSRS(liDARParams.getSRS());
		fType.setDefaultGeometryAttributeName(attrGeo.getName());


		List types = new ArrayList(1);
		finalFType = fType.getNotEditableCopy();
		types.add(finalFType);
		this.getStoreServices().setFeatureTypes(types, finalFType);
	}

	private void getFields(EditableFeatureType fType ){

		ContainerColumnDescription columns = new ContainerColumnDescription();
		lp.getColumnsDescription(columns);

		int type;
		for(int i=0;i<columns.getColumns().size();i++){

			type = columns.get(i).getFieldType();
			if(type==ColumnDescription.DOUBLE){
				fType.add(columns.get(i).getFieldName(),DataTypes.DOUBLE, columns.get(i).getFieldSize()).setPrecision(columns.get(i).getFieldPrecision()).setDefaultValue( (columns.get(i).getFieldDefaultValue()) );
			}else if(type==ColumnDescription.INT){
				fType.add(columns.get(i).getFieldName(),DataTypes.INT, columns.get(i).getFieldSize()).setPrecision(columns.get(i).getFieldPrecision()).setDefaultValue( (columns.get(i).getFieldDefaultValue()) );
			}else if(type==ColumnDescription.LONG){
				fType.add(columns.get(i).getFieldName(),DataTypes.LONG, columns.get(i).getFieldSize()).setPrecision(columns.get(i).getFieldPrecision()).setDefaultValue( (columns.get(i).getFieldDefaultValue()) );
			} else if(type==ColumnDescription.BYTE) {
				fType.add(columns.get(i).getFieldName(),DataTypes.BYTE, columns.get(i).getFieldSize()).setPrecision(columns.get(i).getFieldPrecision()).setDefaultValue( (columns.get(i).getFieldDefaultValue()) );
			}
		}

	}

	public long getFeatureCount() throws DataException {
		this.open();
		this.liDARResource.begin();
		try {
			return hdr.getNumPointsRecord();
		} finally {
			this.liDARResource.end();
		}
	}

	/**
	 *
	 * @param index
	 * @return
	 * @throws DataException
	 */
	 FeatureProvider getFeatureProviderByIndex(long index, FeatureType fType)
			throws DataException {
		 FeatureProvider data = this.createFeatureProvider(fType);
		this.loadFeatureProviderByIndex(data, index, fType);
		return data;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createNewOID()
	 */
	public Object createNewOID() {
		if (this.counterNewsOIDs < 0) {
			// try {
			// this.counterNewsOIDs = this.getFeatureCount();
			// } catch (DataException e) {
			// e.printStackTrace();
			// }

		} else {
			this.counterNewsOIDs++;
		}
		return new Long(counterNewsOIDs);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createSet(org.gvsig.fmap.dal.feature.FeatureQuery)
	 */
	public FeatureSetProvider createSet(FeatureQuery query, FeatureType featureType)
			throws DataException {
		return new LiDARSetProvider(this, query, featureType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getFeatureProviderByReference(org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices)
	 */
	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference) throws DataException {
		return this.getFeatureProviderByReference(reference, this
				.getFeatureStore()
				.getDefaultFeatureType());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getFeatureProviderByReference(org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices, org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference, FeatureType featureType)
			throws DataException {
		return this.getFeatureProviderByIndex((Long) reference.getOID(),
				featureType);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getFeatureReferenceOIDType()
	 */
	public int getOIDType() {
		return DataTypes.LONG;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getName()
	 */
	public String getName() {
		return NAME;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.spi.DataStoreProvider#open()
	 */
	public void open() throws OpenException {
		if (isOpen) {
			return;
		}
		try {
			liDARResource.begin();
		} catch (ResourceBeginException e1) {
			throw new OpenException(NAME, e1);
		}

		try {

			hdr = InicializeLidar.InizializeLidarHeader(m_Fich);

			lp = InicializeLidar.InizializeLidarPoint(m_Fich);

			fin = new FileInputStream(m_Fich);


			// Open the file and then get a channel from the stream
			channel = fin.getChannel();

			// Get the file's size and then map it into memory
			// bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
			bb = new BigByteBuffer2(channel, FileChannel.MapMode.READ_ONLY);
			bb.order(ByteOrder.LITTLE_ENDIAN);

			isOpen = true;
			liDARResource.notifyOpen();

			// Cargar Metadatos !!! (Envelope, SRS...)
			Envelope envelope = geomManager.createEnvelope(hdr.getMinX(), hdr
					.getMinY(), hdr.getMaxX(), hdr.getMaxY(), SUBTYPES.GEOM2D);
			this.setDynValue("Envelope", envelope);
		} catch (IOException e) {
			throw new OpenException(NAME, e);
		} catch (ResourceNotifyOpenException e) {
			throw new OpenException(NAME, e);
		} catch (UnexpectedLidarHeaderException e) {
			throw new OpenException(NAME, e);
		} catch (CreateEnvelopeException e) {
			throw new OpenException(NAME, e);
		} finally {
			liDARResource.end();
		}
	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		try {
			this.close();
		} catch (CloseException e) {
			return false;
		}
		return true;
	}

	public boolean allowWrite() {
		// TODO
		return false;
	}

	public void close() throws CloseException {
		if (!isOpen) {
			return;
		}
		try {
			liDARResource.begin();
		} catch (ResourceBeginException e2) {
			throw new CloseException(NAME, e2);
		}

		try {
			IOException ret = null;
			try {
				channel.close();
			} catch (IOException e) {
				ret = e;
			} finally {
				try {
					fin.close();
				} catch (IOException e1) {
					ret = e1;
				}
			}

			if (ret != null) {
				throw ret;
			}
			bb = null;
			channel = null;
			fin = null;
			isOpen = false;
		} catch (IOException e) {
			throw new CloseException(NAME, e);
		} finally {
			liDARResource.end();
		}

	}

	@Override
	public void dispose() throws CloseException {
		this.close();
		this.lp = null;
		this.hdr = null;
		liDARResource.removeConsumer(this);
		liDARResource = null;
		m_Fich = null;
		fTemp = null;
		super.dispose();
	}

	@Override
	public void refresh() throws OpenException {
		try {
			this.close();
		} catch (CloseException e) {
			throw new OpenException(this.getName(), e);
		}
		this.open();
		try {
			this.initFeatureType();
		} catch (InitializeException e) {
			throw new OpenException(this.getName(), e);
		}

	}

	public void resourceChanged(ResourceProvider resource) {
		this.getStoreServices().notifyChange(
				DataStoreNotification.RESOURCE_CHANGED,
				resource);
	}

	public void loadFeatureProviderByIndex(FeatureProvider data, long index,
			FeatureType featureType) throws DataException {
		this.open();
		liDARResource.begin();

		try {

			if (index < 0 || hdr.getNumPointsRecord() <= index) {
				// FIXME
				throw new ArrayIndexOutOfBoundsException("" + index);
			}

			// lp.readPoint(bb, hdr, index);

			data.setOID(index);

			FeatureAttributeDescriptor attr;
			Iterator iter = featureType.iterator();

			Point2D.Double point;
			while (iter.hasNext()) {
				attr = (FeatureAttributeDescriptor) iter.next();
				if (attr.getDataType() != DataTypes.GEOMETRY) {
					data.set(attr.getIndex(), lp.getFieldValueByName(bb, attr
							.getName(), hdr, index));
				} else {
					point = lp.readPoint2D(bb, hdr, index);
					try {
						data.set(attr.getIndex(), geomManager.createPoint(
								point.x, point.y, SUBTYPES.GEOM2D));
					} catch (CreateGeometryException e) {
						logger.error("Error adding a point", e);
					}
				}
			}


		} finally {
			liDARResource.end();
		}

	}

	public Object getSourceId() {
		return this.getLiDARParameters().getFile().getAbsolutePath();
	}

}
