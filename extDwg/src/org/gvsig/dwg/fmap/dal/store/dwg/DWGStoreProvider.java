package org.gvsig.dwg.fmap.dal.store.dwg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cresques.cts.IProjection;
import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgVersionNotSupportedException;
import org.gvsig.dwg.lib.IDwg2FMap;
import org.gvsig.dwg.lib.IDwg3DTestable;
import org.gvsig.dwg.lib.objects.DwgMText;
import org.gvsig.dwg.lib.objects.DwgText;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.CreateGeometryException;
import org.gvsig.fmap.dal.feature.exception.PerformEditingException;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.feature.spi.memory.AbstractMemoryStoreProvider;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.exception.NotYetImplemented;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DWGStoreProvider extends AbstractMemoryStoreProvider implements
		ResourceConsumer {
	private static final Logger logger = LoggerFactory.getLogger(DWGStoreProvider.class);

	public static final String NAME = "DWG";
	public static final String DESCRIPTION = "DWG file";
	public static final String DYNCLASS_NAME = "DWGStore";
	protected static DynClass DYNCLASS = null;

	public static final String NAME_FIELD_ID = "ID";
	public static final String NAME_FIELD_GEOMETRY = "Geometry";
	public static final String NAME_FIELD_ENTITY = "Entity";
	public static final String NAME_FIELD_LAYER = "Layer";
	public static final String NAME_FIELD_COLOR = "Color";
	public static final String NAME_FIELD_ELEVATION = "Elevation";
	public static final String NAME_FIELD_THICKNESS = "Thickness";
	public static final String NAME_FIELD_TEXT = "Text";
	public static final String NAME_FIELD_HEIGHTTEXT = "HeightText";
	public static final String NAME_FIELD_ROTATIONTEXT = "Rotation";

	private int ID_FIELD_ID = 0;
	private int ID_FIELD_GEOMETRY = 1;
	private int ID_FIELD_ENTITY = 2;
	private int ID_FIELD_LAYER = 3;
	private int ID_FIELD_COLOR = 4;
	private int ID_FIELD_ELEVATION = 5;
	private int ID_FIELD_THICKNESS = 6;
	private int ID_FIELD_TEXT = 7;
	private int ID_FIELD_HEIGHTTEXT = 8;
	private int ID_FIELD_ROTATIONTEXT = 9;

	private IProjection projection;
	private ResourceProvider resource;
	private LegendBuilder legendBuilder;

	private long counterNewsOIDs = 0;
	protected GeometryManager geomManager = GeometryLocator.getGeometryManager();




	public DWGStoreProvider(DWGStoreParameters parameters,
			DataStoreProviderServices storeServices) throws InitializeException {
		super(parameters,storeServices,ToolsLocator
				.getDynObjectManager()
				.createDynObject(DYNCLASS));

		counterNewsOIDs = 0;
		//		projection = CRSFactory.getCRS(getParameters().getSRSID());

		File file = getDWGParameters().getFile();
		resource = this.createResource(
				FileResource.NAME,
				new Object[] { file.getAbsolutePath() }
			);

		resource.addConsumer(this);

		this.projection = this.getDWGParameters().getSRS();


		try {
			legendBuilder = (LegendBuilder) this.invokeDynMethod(
					LegendBuilder.DYNMETHOD_BUILDER_NAME, null);
		} catch (DynMethodException e) {
			legendBuilder = null;
		} catch (Exception e) {
			throw new InitializeException(e);
		}

		this.initializeFeatureTypes();


	}

	private DWGStoreParameters getDWGParameters() {
		return (DWGStoreParameters) this.getParameters();
	}


	public String getName() {
		return NAME;
	}

	public boolean allowWrite() {
		return false;
	}

	public Object getLegend() throws OpenException {
		this.open();
		if (legendBuilder == null) {
			return null;
		}
		return legendBuilder.getLegend();
	}

	public Object getLabeling() throws OpenException {
		this.open();
		if (legendBuilder == null) {
			return null;
		}
		return legendBuilder.getLabeling();
	}

	private class DWGData {
		public ArrayList data = null;
		public FeatureType defaultFType = null;
		public List fTypes = null;
		public Envelope envelope = null;
		public IProjection projection;
		public LegendBuilder legendBuilder = null;
		public Envelope getEnvelopeCopy() throws CreateEnvelopeException {
			if (envelope == null) {
				return null;
			}
			Envelope newEnvelope;
			if (envelope.getDimension() == 2) {
				newEnvelope = geomManager.createEnvelope(SUBTYPES.GEOM2D);
			} else {
				newEnvelope = geomManager.createEnvelope(SUBTYPES.GEOM3D);

			}
			newEnvelope.setLowerCorner(envelope.getLowerCorner());
			newEnvelope.setUpperCorner(envelope.getUpperCorner());
			return newEnvelope;
		}
	}

	public void open() throws OpenException {
		if (this.data != null) {
			return;
		}
		try {
			this.resource.begin();
		} catch (ResourceBeginException e2) {
			try {
				throw new OpenException(resource.getName(), e2);
			} catch (AccessResourceException e1) {
				throw new OpenException(this.getName(), e2);
			}

		}
		try {
			DWGData dwgData = null;
			FeatureStoreProviderServices store = getStoreServices();
			if (this.resource.getData() != null) {
				dwgData = (DWGData) ((Map) this.resource.getData())
						.get(this.projection
						.getAbrev()); // OJO no es del todo correcto (puede
										// llevar reproyeccion)
			} else {
				this.resource.setData(new HashMap());
			}
			if (dwgData == null) {
				dwgData = new DWGData();
				dwgData.data = new ArrayList();
				this.data = dwgData.data;
				this.counterNewsOIDs = 0;
				Reader reader = new Reader().initialice(
						this,
						new File((String) this.resource.get()),
						projection,
						this.legendBuilder
					);
				reader.begin(store);
				dwgData.defaultFType = reader.getDefaultType()
						.getNotEditableCopy();
				ArrayList types = new ArrayList();
				Iterator it = reader.getTypes().iterator();
				EditableFeatureType fType;
				while (it.hasNext()) {
					fType = (EditableFeatureType) it.next();
					if (fType.getId().equals(dwgData.defaultFType.getId())) {
						types.add(dwgData.defaultFType);
					} else {
						types.add(fType.getNotEditableCopy());
					}
				}
				dwgData.fTypes = types;
				dwgData.legendBuilder = this.legendBuilder;

				resource.notifyOpen();
				store.setFeatureTypes(dwgData.fTypes, dwgData.defaultFType);
				reader.load();
				//				this.envelope = reader.getEnvelope();

				dwgData.envelope = reader.getEnvelope();


				dwgData.projection = this.projection;

				reader.end();
				resource.notifyClose();
				((Map) this.resource.getData()).put(this.projection.getAbrev(),
						dwgData); // OJO la reproyeccion
			}

			this.data = dwgData.data;
			store.setFeatureTypes(dwgData.fTypes, dwgData.defaultFType);
			this.legendBuilder = dwgData.legendBuilder;
			this.setDynValue("Envelope", dwgData.getEnvelopeCopy());
			this.setDynValue("DefaultSRS", this.projection.getAbrev());
			this.counterNewsOIDs = this.data.size();
		} catch (Exception e) {
			this.data = null;
			try {
				throw new OpenException(resource.getName(), e);
			} catch (AccessResourceException e1) {
				throw new OpenException(this.getName(), e);
			}
		} finally {
			this.resource.end();
		}
	}


	public DataServerExplorer getExplorer() throws ReadException {
		DataManager manager = DALLocator.getDataManager();
		FilesystemServerExplorerParameters params;
		try {
			params = (FilesystemServerExplorerParameters) manager
				.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			params.setRoot(this.getDWGParameters().getFile().getParent());
			return manager.createServerExplorer(params);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		} catch (ValidateDataParametersException e) {
			throw new ReadException(this.getName(), e);
		}

	}



	public void performChanges(Iterator deleteds, Iterator inserteds, Iterator updateds, Iterator originalFeatureTypesUpdated) throws PerformEditingException {
		// FIXME Exception
		throw new UnsupportedOperationException();
	}

	public class Reader {
		private File file;
		private String fileName;
		private IProjection projection;
		private List types;
		private LegendBuilder leyendBuilder;
		private AbstractMemoryStoreProvider store;
		private Envelope envelope;

		public Reader initialice(AbstractMemoryStoreProvider store, File file,
				IProjection projection,
				LegendBuilder leyendBuilder) {
			this.store = store;
			this.file = file;
			this.fileName = file.getAbsolutePath();
			this.projection = projection;
			this.leyendBuilder = leyendBuilder;
			if (leyendBuilder != null) {
				leyendBuilder.initialize(store);
			}
			return this;
		}

		public Envelope getEnvelope() {
			return this.envelope;
		}

		public void begin(FeatureStoreProviderServices store) {

			EditableFeatureType featureType = store.createFeatureType();

			featureType.setHasOID(true);

			ID_FIELD_ID = featureType.add(NAME_FIELD_ID, DataTypes.INT)
					.setDefaultValue(Integer.valueOf(0))
				.getIndex();

			EditableFeatureAttributeDescriptor attr = featureType.add(
					NAME_FIELD_GEOMETRY, DataTypes.GEOMETRY);
			attr.setSRS(this.projection);
			attr.setGeometryType(Geometry.TYPES.GEOMETRY);
			ID_FIELD_GEOMETRY = attr.getIndex();

			featureType.setDefaultGeometryAttributeName(NAME_FIELD_GEOMETRY);

			// FIXME: Cual es el size y el valor por defecto para Entity ?
			ID_FIELD_ENTITY = featureType.add(NAME_FIELD_ENTITY,
					DataTypes.STRING, 100)
					.setDefaultValue("")
					.getIndex();

			// FIXME: Cual es el size de Layer ?
			ID_FIELD_LAYER = featureType.add(NAME_FIELD_LAYER,
					DataTypes.STRING, 100)
					.setDefaultValue(
					"default").getIndex();

			ID_FIELD_COLOR = featureType.add(NAME_FIELD_COLOR,
					DataTypes.INT)
					.setDefaultValue(
					Integer.valueOf(0)).getIndex();

			ID_FIELD_ELEVATION = featureType.add(NAME_FIELD_ELEVATION,
					DataTypes.DOUBLE)
					.setDefaultValue(
					Double.valueOf(0)).getIndex();

			ID_FIELD_THICKNESS = featureType.add(NAME_FIELD_THICKNESS,
					DataTypes.DOUBLE)
					.setDefaultValue(
					Double.valueOf(0)).getIndex();

			// FIXME: Cual es el size de Text ?
			ID_FIELD_TEXT = featureType.add(NAME_FIELD_TEXT,
					DataTypes.STRING, 100)
					.setDefaultValue("")
					.getIndex();

			ID_FIELD_HEIGHTTEXT = featureType.add(NAME_FIELD_HEIGHTTEXT,
					DataTypes.DOUBLE).setDefaultValue(
					Double.valueOf(10)).getIndex();

			ID_FIELD_ROTATIONTEXT = featureType.add(NAME_FIELD_ROTATIONTEXT,
					DataTypes.DOUBLE).setDefaultValue(
					Double.valueOf(0)).getIndex();



			// FIXME: Parece que el DXF puede tener mas atributos opcionales.
			// Habria que ver de pillarlos ?

			types = new ArrayList();
			types.add(featureType);

			if (leyendBuilder != null) {
				leyendBuilder.begin();
			}

		}

		public void end() {
			if (leyendBuilder != null) {
				leyendBuilder.end();
			}
		}

		public List getTypes() {
			return types;
		}

		public EditableFeatureType getDefaultType() {
			return (EditableFeatureType) types.get(0);
		}

		private Double toDouble(String value) {
			if (value == null) {
				return Double.valueOf(0);
			}
			return Double.valueOf(value);
		}

		public void load() throws DataException {

			this.envelope = null;

			DwgFile dwgFeatureFile = new DwgFile(file.getAbsolutePath());

			try {
				dwgFeatureFile.read();
			} catch (DwgVersionNotSupportedException e1) {
				throw new UnsupportedDWGVersionException(file.getName(), e1);
			} catch (IOException e) {
				throw new ReadException(NAME, e);
			}


			dwgFeatureFile.calculateGisModelDwgPolylines();
			dwgFeatureFile.blockManagement2();
			List entities = dwgFeatureFile.getDwgObjects();

			int id = -1;
			try{
				int envelopeSubType = Geometry.SUBTYPES.GEOM2D;
				boolean is3dFile = dwgFeatureFile.isDwg3DFile();
//				if (is3dFile) {
//					envelopeSubType = Geometry.SUBTYPES.GEOM3D;
//				}

				this.envelope = geomManager.createEnvelope(envelopeSubType);

				boolean updateEnvelope = true;

				double[] extMin = (double[]) dwgFeatureFile
						.getHeader("MSPACE_EXTMIN");
				double[] extMax = (double[]) dwgFeatureFile
						.getHeader("MSPACE_EXTMAX");
				if (extMin != null && extMax != null) {
					updateEnvelope = false;
					Point point = (Point) geomManager.create(
							Geometry.TYPES.POINT, envelopeSubType);
					point.setCoordinates(extMin);
					this.envelope.setLowerCorner(point);
					point = (Point) geomManager.create(Geometry.TYPES.POINT,
							envelopeSubType);
					point.setCoordinates(extMax);
					this.envelope.setUpperCorner(point);
				}


				Iterator iter = entities.iterator();

				FeatureProvider featureProvider;
				Envelope gEnvelope;

				while (iter.hasNext()) {
					id++;
					DwgObject entity = (DwgObject) iter.next();

					if (!(entity instanceof IDwg2FMap)) {
						logger
								.warn(
								"load: entity type {}(id:{}) not loadded",
														new Object[] { entity
																.getType() },
														id);

						continue;
					}

					IDwg2FMap dwgEnt = (IDwg2FMap) entity;
					Geometry geometry = dwgEnt.toFMapGeometry(is3dFile);
					if (geometry == null) { // || geometry.getGeneralPath() ==
						// null) {
						logger
								.warn(
								"load: entity {}(id:{}) with null geometry",
														new Object[] {
																entity
																		.getType(),
																id });

						continue;
					}
					// FIXME: Estas 6 lineas es por si la geometrí­a no se ha
					// creado correctamente
					gEnvelope = null;
					try {
						gEnvelope = geometry.getEnvelope();
					} catch (Exception e) {
						gEnvelope = null;
					}
					if (gEnvelope == null) {
						logger
								.warn(
								"load: entity {}(id:{}) with null envelope",
								new Object[] { entity.getType(), id });

						continue;
					}
					// we check for Region of Interest of the CAD file
					if (!this.envelope.intersects(geometry.getEnvelope())) {
						logger
								.warn(
								"load: entity {}(id:{}) out of envelope",
								new Object[] { entity.getType(), id });
						continue;
					}
					featureProvider = store.createFeatureProvider(store
							.getStoreServices().getDefaultFeatureType());

					featureProvider.set(ID_FIELD_ID, id);

					featureProvider.set(ID_FIELD_ENTITY, dwgEnt.toString());

					featureProvider.set(ID_FIELD_LAYER, dwgFeatureFile
							.getLayerName(entity));

					int colorByLayer = dwgFeatureFile.getColorByLayer(entity);
					int color = entity.getColor();
					if (color < 0) {
						color = Math.abs(color);
					}
					if (color > 255) {
						color = colorByLayer;
					}

					featureProvider.set(ID_FIELD_COLOR, color);

					if (entity instanceof IDwg3DTestable) {
						featureProvider.set(ID_FIELD_ELEVATION,
								((IDwg3DTestable) entity).getZ());
					}

					featureProvider.set(ID_FIELD_THICKNESS, 0.0);
					featureProvider.set(ID_FIELD_HEIGHTTEXT, 0.0);
					featureProvider.set(ID_FIELD_ROTATIONTEXT, 0.0);
					featureProvider.set(ID_FIELD_TEXT, "");

					if (entity instanceof DwgMText) {
						DwgMText mtext = (DwgMText) entity;
						featureProvider.set(ID_FIELD_HEIGHTTEXT, mtext.getHeight());
						featureProvider.set(ID_FIELD_TEXT, mtext.getText());
					} else if (entity instanceof DwgText) {
						DwgText text = (DwgText) entity;
						featureProvider
								.set(ID_FIELD_THICKNESS, text.getThickness());
						featureProvider.set(ID_FIELD_HEIGHTTEXT, text.getHeight());
						featureProvider.set(ID_FIELD_ROTATIONTEXT, text
								.getRotationAngle());
						featureProvider.set(ID_FIELD_TEXT, text.getText());
					}// if-else

					featureProvider.setDefaultGeometry(geometry);

					store.addFeatureProvider(featureProvider);
					if (this.leyendBuilder != null) {
						this.leyendBuilder.process(featureProvider);
					}

				}

			} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
				throw new CreateGeometryException(e);
			} catch (org.gvsig.fmap.geom.exception.CreateEnvelopeException e) {
				throw new CreateGeometryException(e);
			}

		}

	}


	public boolean closeResourceRequested(ResourceProvider resource) {
		return true;
	}

	public int getOIDType() {
		return DataTypes.LONG;
	}

	public boolean supportsAppendMode() {
		return false;
	}

	public void append(FeatureProvider featureProvider) {
		// FIXME Exception
		throw new UnsupportedOperationException();
	}

	public void beginAppend() {
		// FIXME Exception
		throw new UnsupportedOperationException();

	}

	public void endAppend() {
		// FIXME Exception
		throw new UnsupportedOperationException();
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub
		throw new NotYetImplemented();
	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub
		throw new NotYetImplemented();
	}

	public Object createNewOID() {
		return new Long(counterNewsOIDs++);
	}

	protected void initializeFeatureTypes() throws InitializeException {
		try {
			this.open();
		} catch (OpenException e) {
			throw new InitializeException(this.getName(), e);
		}
	}

	public Envelope getEnvelope() throws DataException {
		this.open();
		return (Envelope) this.getDynValue("Envelope");
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

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME, "DXF File Store");
			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));

			DYNCLASS = dynClass;
		}

	}

	public Object getSourceId() {
		return this.getDWGParameters().getFile();
	}

}
