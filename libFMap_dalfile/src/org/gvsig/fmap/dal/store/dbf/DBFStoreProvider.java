package org.gvsig.fmap.dal.store.dbf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.UnsupportedVersionException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.PerformEditingException;
import org.gvsig.fmap.dal.feature.exception.UnknownDataTypeException;
import org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyChangesException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyCloseException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.dbf.utils.DbaseFile;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.exception.BaseException;

public class DBFStoreProvider extends AbstractFeatureStoreProvider implements
		ResourceConsumer {

	public static String NAME = "DBF";
	public static String DESCRIPTION = "DBF file";
	//	private DBFResource dbf = null;
	private static final Locale ukLocale = new Locale("en", "UK");
	private static final String DYNCLASS_NAME = "DBFStore";
	protected static DynClass DYNCLASS = null;

	public static final String DYNFIELD_CURRENT_ENCODING = "CurrentEncoding";
	public static final String DYNFIELD_ORIGINAL_ENCODING = "OriginalEncoding";

	private DbaseFile dbfFile = null;
	private ResourceProvider dbfResource;
	private long counterNewsOIDs = -1;
	private DBFFeatureWriter writer;

	public DBFStoreProvider(DBFStoreParameters params,
			DataStoreProviderServices storeServices)
			throws InitializeException {
		this(params, storeServices, ToolsLocator
				.getDynObjectManager().createDynObject(
				DYNCLASS));
	}

	protected DBFStoreProvider(DBFStoreParameters params,
			DataStoreProviderServices storeServices, DynObject metadata)
			throws InitializeException {
		super(params, storeServices, metadata);
		this.init(params, storeServices);
	}

	protected void init(DBFStoreParameters params,
			DataStoreProviderServices storeServices) throws InitializeException {
		this.setDynValue("DefaultSRS", null);
		this.setDynValue("Envelope", null);

		File theFile = getDBFParameters().getDBFFile();
		dbfResource = this.createResource(FileResource.NAME,
				new Object[] { theFile.getAbsolutePath() });
		dbfResource.addConsumer(this);

		Charset charset = params.getEncoding();
		this.dbfFile = new DbaseFile(theFile, charset);

		writer = new DBFFeatureWriter(this.getName());

		this.initFeatureType();

	}


	public String getName() {
		return NAME;
	}

	protected DBFStoreParameters getDBFParameters() {
		return (DBFStoreParameters) super.getParameters();
	}


	public DataServerExplorer getExplorer() throws ReadException {
		DataManager manager = DALLocator.getDataManager();
		FilesystemServerExplorerParameters params;
		try {
			params = (FilesystemServerExplorerParameters) manager
					.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			params.setRoot(this.getDBFParameters().getDBFFile().getParent());
			return manager.createServerExplorer(params);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		} catch (ValidateDataParametersException e) {
			// TODO Auto-generated catch block
			throw new ReadException(this.getName(), e);
		}
	}

	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference, FeatureType featureType)
			throws DataException {

		return this.getFeatureProviderByIndex(((Long) reference.getOID())
				.longValue(),
				featureType);
	}


	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference) throws DataException {
		return this.getFeatureProviderByReference(reference, this
				.getStoreServices()
				.getDefaultFeatureType());
	}

	public void performChanges(Iterator deleteds, Iterator inserteds,
			Iterator updateds, Iterator originalFeatureTypesUpdated)
			throws PerformEditingException {

		try {
			FeatureStore store = this.getStoreServices().getFeatureStore();
			this.resourcesBegin();
			FeatureSet set = store.getFeatureSet();
			DBFStoreParameters tmpParams = (DBFStoreParameters) this
					.getDBFParameters().getCopy();

			tmpParams.setDBFFileName(tmpParams.getDBFFileName() + ".tmp");

			writer.begin(tmpParams, store
					.getDefaultFeatureType(), set.getSize());

			DisposableIterator iter = set.fastIterator();
			while (iter.hasNext()) {
				Feature feature=(Feature) iter.next();
				writer.append(feature);
			}
			iter.dispose();


			writer.end();

			try {
				this.close();
			} catch (CloseException e1) {
				throw new PerformEditingException(this.getName(), e1);
			}
			this.getDBFParameters().getDBFFile().delete();
			tmpParams.getDBFFile().renameTo(
					this.getDBFParameters().getDBFFile());

			this.resourcesNotifyChanges();
			this.initFeatureType();
		} catch (Exception e) {
			throw new PerformEditingException(this.getName(), e);
		} finally {
			this.resourcesEnd();
		}

		this.counterNewsOIDs = -1;
	}

	/*
	 * ==================================================
	 */

	public FeatureProvider createFeatureProvider(FeatureType type) throws DataException {
		return new DBFFeatureProvider(this, type);
	}


	/*
	 * ===================================================
	 */



	protected void initFeatureType() throws InitializeException {
		FeatureType defaultType = this.getTheFeatureType().getNotEditableCopy();
		List types = new ArrayList(1);
		types.add(defaultType);
		this.getStoreServices().setFeatureTypes(types, defaultType);
	}

	protected EditableFeatureType getTheFeatureType() throws InitializeException {
		try {
			this.open();
			this.resourcesBegin();
		} catch (DataException e) {
			throw new InitializeException(this.getName(), e);
		}
		try {
			int fieldCount = -1;
			fieldCount = dbfFile.getFieldCount();

			EditableFeatureType fType = this.getStoreServices()
					.createFeatureType();

			fType.setHasOID(true);
			int precision;
			for (int i = 0; i < fieldCount; i++) {
				char fieldType = dbfFile.getFieldType(i);
				EditableFeatureAttributeDescriptor attr;

				if (fieldType == 'L') {
					attr = fType
							.add(dbfFile.getFieldName(i), DataTypes.BOOLEAN);
					attr.setDefaultValue(new Boolean(false));
					attr.setAllowNull(false);

				} else if ((fieldType == 'F') || (fieldType == 'N')) {
					precision = dbfFile.getFieldDecimalLength(i);
					if (precision > 0) {
						attr = fType.add(dbfFile.getFieldName(i),
								DataTypes.DOUBLE, dbfFile.getFieldLength(i));
						attr.setPrecision(precision);
						attr.setDefaultValue(new Double(0));

					} else {
						attr = fType
								.add(dbfFile.getFieldName(i), DataTypes.INT);
						attr.setDefaultValue(new Integer(0));
					}
					attr.setAllowNull(false);

				} else if (fieldType == 'C') {
					attr = fType.add(dbfFile.getFieldName(i), DataTypes.STRING);
					attr.setSize(dbfFile.getFieldLength(i));
					attr.setDefaultValue("");
					attr.setAllowNull(false);

				} else if (fieldType == 'D') {
					attr = fType.add(dbfFile.getFieldName(i), DataTypes.DATE);
					attr.setDefaultValue(null);
					attr.setAllowNull(true);
				} else {
					throw new InitializeException(this.getName(),
							new UnknownDataTypeException(
									dbfFile.getFieldName(i), "" + fieldType,
									this.getName()));
				}
			}
			return fType;
		} finally {
			this.resourcesEnd();
		}
	}


	protected void loadValue(FeatureProvider featureProvider, int rowIndex,
			FeatureAttributeDescriptor descriptor) throws ReadException {
		if (descriptor.getEvaluator() != null) {
			// Nothing to do
			return;
		}


		int dbfIndex = this.dbfFile.getFieldIndex(descriptor.getName());
		String value = null;
		try {
			value = this.dbfFile.getStringFieldValue(rowIndex, dbfIndex);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		}
		value = value.trim();
		int fieldType = descriptor.getDataType();
		switch (fieldType) {
		case DataTypes.STRING:
			featureProvider.set(descriptor.getIndex(), value);
			break;

		case DataTypes.DOUBLE:
			try {
				featureProvider.set(descriptor.getIndex(), new Double(value));
			} catch (NumberFormatException e) {
				featureProvider.set(descriptor.getIndex(), null);
			}
			break;

		case DataTypes.INT:
			try {
				featureProvider.set(descriptor.getIndex(), new Integer(value));
			} catch (NumberFormatException e) {
				featureProvider.set(descriptor.getIndex(), null);
			}
			break;

		case DataTypes.FLOAT:
			try {
				featureProvider.set(descriptor.getIndex(), new Float(value));
			} catch (NumberFormatException e) {
				featureProvider.set(descriptor.getIndex(), null);
			}
			break;

		case DataTypes.LONG:
			try {
				featureProvider.set(descriptor.getIndex(), new Long(value));
			} catch (NumberFormatException e) {
				featureProvider.set(descriptor.getIndex(), null);
			}
			break;

		case DataTypes.BOOLEAN:
			featureProvider.set(descriptor.getIndex(), new Boolean(value));
			break;

		case DataTypes.BYTE:
			try {
				featureProvider.set(descriptor.getIndex(), new Byte(value));
			} catch (NumberFormatException e) {
				featureProvider.set(descriptor.getIndex(), null);
			}
			break;

		case DataTypes.DATE:
			if (value.equals("")){
				value=null;
				return;
			}
			String year = value.substring(0, 4);
			String month = value.substring(4, 6);
			String day = value.substring(6, 8);
			DateFormat df;
			if (descriptor.getDateFormat() == null){
				df = DateFormat.getDateInstance(DateFormat.SHORT,
						ukLocale);
			} else{
				df = descriptor.getDateFormat();
			}
			/*
			 * Calendar c = Calendar.getInstance(); c.clear();
			 * c.set(Integer.parseInt(year), Integer.parseInt(month),
			 * Integer.parseInt(day)); c.set(Calendar.MILLISECOND, 0);
			 */
			String strAux = month + "/" + day + "/" + year;
			Date dat = null;
			try {
				dat = df.parse(strAux);
			} catch (ParseException e) {
				throw new ReadException(this.getName(), e);
			}
			featureProvider.set(descriptor.getIndex(), dat);
			break;


		default:
			featureProvider
					.set(descriptor.getIndex(), descriptor.getDefaultValue());
			break;
		}
	}


	/***
	 * NOT supported in Alter Mode
	 *
	 * @param index
	 * @return
	 * @throws ReadException
	 */
	protected FeatureProvider getFeatureProviderByIndex(long index) throws DataException {
		return this
				.getFeatureProviderByIndex(index, this.getStoreServices()
				.getDefaultFeatureType());
	}

	public long getFeatureCount() throws ReadException, OpenException,
			ResourceNotifyChangesException {
		this.open();
		try {
			this.resourcesBegin();
		} catch (ResourceBeginException e) {
			throw new ReadException(this.getName(), e);
		}
		try {
			return this.dbfFile.getRecordCount();
		} finally {
			this.resourcesEnd();
		}
	}

	public FeatureSetProvider createSet(FeatureQuery query, FeatureType featureType)
			throws DataException {
		return new DBFSetProvider(this, query, featureType);
	}

	public boolean canCreate() {
		return true;
	}

	public boolean canWriteGeometry(int geometryType, int geometrySubType) throws DataException {
		return false;
	}

	public void open() throws OpenException {
		if (this.dbfFile.isOpen()) {
			return;
		}
		try {
			this.resourcesBegin();
		} catch (ResourceBeginException e) {
			throw new OpenException(this.getName(), e);
		}
		try {
			this.openFile();
			this.resourcesOpen();

			// Load metadata values
			this.loadMetadataValues();

		} catch (UnsupportedVersionException e) {
			throw new OpenException(this.getName(), e);
		} catch (ResourceNotifyOpenException e) {
			throw new OpenException(this.getName(), e);
		} catch (FileNotFoundException e) {
			throw new OpenException(this.getName(), e);
		} catch (IOException e) {
			throw new OpenException(this.getName(), e);
		} catch (BaseException e) {
			throw new OpenException(this.getName(), e);
		} finally {
			this.resourcesEnd();
		}
	}

	protected void openFile() throws FileNotFoundException,
			UnsupportedVersionException, IOException, DataException {
		this.dbfFile.open();
	}

	protected void loadMetadataValues() throws DynFieldNotFoundException,
			ReadException {
		this.setDynValue(DBFLibrary.DYNFIELD_CODEPAGE_NAME, new Byte(
				this.dbfFile.getCodePage()));
		this.setDynValue(DYNFIELD_CURRENT_ENCODING, dbfFile
				.getCurrenCharset().name());
		this.setDynValue(DYNFIELD_ORIGINAL_ENCODING, dbfFile
				.getOriginalCharset().name());
	}

	public void close() throws CloseException {
		if (!this.dbfFile.isOpen()) {
			return;
		}
		super.close();
		//Cerrar recurso
		try {
			this.resourcesBegin();
		} catch (ResourceBeginException e) {
			throw new CloseException(this.getName(), e);
		}
		try {
			this.closeFile();
			this.resourcesNotifyClose();

		} catch (ResourceNotifyCloseException e) {
			throw new CloseException(this.getName(), e);
		} finally {
			this.resourcesEnd();
		}
	}

	protected void closeFile() throws CloseException {
		this.dbfFile.close();
	}

	public void dispose() throws CloseException {
		this.close();
		dbfFile = null;
		this.dbfResource.removeConsumer(this);
		dbfResource = null;
		super.dispose();
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
		File file;
		try {
			file = new File((String) this.dbfResource.get());
		} catch (AccessResourceException e) {
			return false;
		}
		return file.canWrite();
	}

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

	/**
	 *
	 * @param index
	 * @param featureType
	 * @return
	 * @throws ReadException
	 */
	protected FeatureProvider getFeatureProviderByIndex(long index,
			FeatureType featureType) throws DataException {
		FeatureProvider featureProvider = this.createFeatureProvider(featureType);
		featureProvider.setOID(new Long(index));
		return featureProvider;
	}

	protected void initFeatureProviderByIndex(FeatureProvider featureProvider,
			long index, FeatureType featureType) throws DataException {
		featureProvider.setOID(new Long(index));
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
		long index = ((Long) featureProvider.getOID()).longValue();
		try {
			if (index >= this.dbfFile.getRecordCount()) {
				// FIXME
				throw new ArrayIndexOutOfBoundsException("" + index);
			}
			Iterator iterator = featureProvider.getType().iterator();
			while (iterator.hasNext()) {
				FeatureAttributeDescriptor descriptor = (FeatureAttributeDescriptor) iterator
						.next();
				this.loadValue(featureProvider, (int) index, descriptor);
			}


		} finally {
			this.resourcesEnd();
		}
	}

	public int getOIDType() {
		return DataTypes.LONG;
	}

	public Object createNewOID() {
		if (this.counterNewsOIDs < 0) {
			try {
				this.counterNewsOIDs = this.getFeatureCount();
			} catch (DataException e) {
				e.printStackTrace();
			}

		}else{
			this.counterNewsOIDs++;
		}
		return new Long(counterNewsOIDs);
	}

	public boolean supportsAppendMode() {
		return true;
	}


	public void append(FeatureProvider featureProvider) throws DataException {
		this.resourcesBegin();
		try {
			writer.append(getStoreServices().createFeature(featureProvider));
		} finally {
			this.resourcesEnd();
		}
	}

	public void beginAppend() throws DataException {
		this.close();
		this.resourcesBegin();
		try {
			FeatureSet set = this.getStoreServices().getFeatureStore()
					.getFeatureSet();
			writer.begin(this.getDBFParameters(), this.getStoreServices()
					.getDefaultFeatureType(), set.getSize());
		} finally {
			this.resourcesEnd();
		}
	}

	public void endAppend() throws DataException {
		this.resourcesBegin();
		try {
			writer.end();

			this.resourcesNotifyChanges();
			this.counterNewsOIDs = -1;
		} finally {
			this.resourcesEnd();
		}
	}

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME,
					"DBF File Store");
			field = DBFLibrary.addCodePageField(dynClass);

			field = dynClass.addDynField(DYNFIELD_CURRENT_ENCODING);
			field.setType(org.gvsig.tools.dataTypes.DataTypes.STRING);

			field = dynClass.addDynField(DYNFIELD_ORIGINAL_ENCODING);
			field.setType(org.gvsig.tools.dataTypes.DataTypes.STRING);

			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));




			DYNCLASS = dynClass;
		}

	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		try {
			this.open();
		} catch (OpenException e) {
			// FIXME
			throw new RuntimeException(e);
		}
		return super.getDynValue(name);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.resource.spi.ResourceConsumer#resourceChanged(org.
	 * gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public void resourceChanged(ResourceProvider resource) {
		this.getStoreServices().notifyChange(
				DataStoreNotification.RESOURCE_CHANGED,
				resource);
	}

	protected void resourcesBegin() throws ResourceBeginException {
		this.dbfResource.begin();
	}

	protected void resourcesEnd() {
		this.dbfResource.end();
	}

	/**
	 *
	 * @throws ResourceNotifyChangesException
	 */
	protected void resourcesNotifyChanges()
			throws ResourceNotifyChangesException {
		this.dbfResource.notifyChanges();
	}

	/**
	 * @throws ResourceNotifyCloseException
	 *
	 */
	protected void resourcesNotifyClose() throws ResourceNotifyCloseException {
		this.dbfResource.notifyClose();
	}

	/**
	 * @throws ResourceNotifyOpenException
	 *
	 */
	protected void resourcesOpen() throws ResourceNotifyOpenException {
		this.dbfResource.notifyOpen();
	}

	public Object getSourceId() {
		return this.getDBFParameters().getFile();
	}

	protected void resourceCloseRequest() throws ResourceException {
		this.dbfResource.closeRequest();
	}
}
