package org.gvsig.fmap.dal.store.dxf;

import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.cresques.cts.IProjection;
import org.cresques.geo.Point3D;
import org.cresques.io.DxfFile;
import org.cresques.io.DxfGroup;
import org.cresques.io.DxfGroupVector;
import org.cresques.px.IObjList;
import org.cresques.px.dxf.DxfEntityMaker;
import org.cresques.px.dxf.DxfFeatureMaker;
import org.cresques.px.dxf.DxfHeaderManager;
import org.cresques.px.gml.Feature;
import org.cresques.px.gml.LineString;
import org.cresques.px.gml.LineString3D;
import org.cresques.px.gml.Point;
import org.cresques.px.gml.Polygon;
import org.cresques.px.gml.Polygon3D;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.LoadException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.PerformEditingException;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.feature.spi.memory.AbstractMemoryStoreProvider;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyCloseException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;
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
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.distance.PointDistance;
import org.gvsig.fmap.geom.operation.utils.PointGetAngle;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Ellipse;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.util.Converter;
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

public class DXFStoreProvider extends AbstractMemoryStoreProvider implements
		ResourceConsumer {
	private static final Logger logger = LoggerFactory.getLogger(DXFStoreProvider.class);

	public static final String NAME = "DXF";
	public static final String DESCRIPTION = "DXF file";
	public static final String DYNCLASS_NAME = "DXFStore";
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
	//	private Envelope envelope;
	private Writer writer;
	protected GeometryManager geomManager = GeometryLocator.getGeometryManager();




	public DXFStoreProvider(DXFStoreParameters parameters,
			DataStoreProviderServices storeServices) throws InitializeException {
		super(parameters, storeServices, ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));

		counterNewsOIDs = 0;
		//		projection = CRSFactory.getCRS(getParameters().getSRSID());

		File file = getDXFParameters().getFile();
		resource = this.createResource(
				FileResource.NAME,
				new Object[] { file.getAbsolutePath() }
			);

		resource.addConsumer(this);

		this.projection = this.getDXFParameters().getSRS();


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

	private DXFStoreParameters getDXFParameters() {
		return (DXFStoreParameters) this.getParameters();
	}

	public String getName() {
		return NAME;
	}

	public boolean allowWrite() {
		return true;
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

	private class DXFData {
		public ArrayList data = null;
		public FeatureType defaultFType = null;
		public List fTypes = null;
		public Envelope envelope = null;
		public IProjection projection;
		public LegendBuilder legendBuilder;
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
			DXFData dxfData = null;
			if (this.resource.getData() != null) {
				dxfData = (DXFData) ((Map) this.resource.getData())
						.get(this.projection
						.getAbrev()); // OJO no es del todo correcto (puede
										// llevar reproyeccion)
			} else {
				this.resource.setData(new HashMap());
			}
			FeatureStoreProviderServices store = getStoreServices();
			if (dxfData == null) {
				dxfData = new DXFData();
				dxfData.data = new ArrayList();
				this.data = dxfData.data;
				this.counterNewsOIDs = 0;
				Reader reader = new Reader().initialice(
						this,
						new File((String) this.resource.get()),
						projection,
						this.legendBuilder
					);
				reader.begin(store);
				dxfData.defaultFType = reader.getDefaultType()
						.getNotEditableCopy();
				ArrayList types = new ArrayList();
				Iterator it = reader.getTypes().iterator();
				EditableFeatureType fType;
				while (it.hasNext()) {
					fType = (EditableFeatureType) it.next();
					if (fType.getId().equals(dxfData.defaultFType.getId())) {
						types.add(dxfData.defaultFType);
					} else {
						types.add(fType.getNotEditableCopy());
					}
				}
				dxfData.fTypes = types;

				resource.notifyOpen();
				store.setFeatureTypes(dxfData.fTypes, dxfData.defaultFType);
				reader.load();
				//				this.envelope = reader.getEnvelope();

				dxfData.envelope = reader.getEnvelope();

				dxfData.legendBuilder = this.legendBuilder;

				dxfData.projection = this.projection;

				reader.end();
				resource.notifyClose();
				((Map) this.resource.getData()).put(this.projection.getAbrev(),
						dxfData); // OJO la reproyeccion
			}

			this.data = dxfData.data;
			store.setFeatureTypes(dxfData.fTypes, dxfData.defaultFType);
			this.legendBuilder = dxfData.legendBuilder;
			this.setDynValue("Envelope", dxfData.getEnvelopeCopy());
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
			params.setRoot(this.getDXFParameters().getFile().getParent());
			return manager.createServerExplorer(params);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		} catch (ValidateDataParametersException e) {
			throw new ReadException(this.getName(), e);
		}

	}



	public void performChanges(Iterator deleteds, Iterator inserteds, Iterator updateds, Iterator originalFeatureTypesUpdated) throws PerformEditingException {

		String fileName = "";

		try {
			resource.begin();
		} catch (ResourceBeginException e1) {
			throw new PerformEditingException(this.getName(), e1);
		}
		try {


			File file = (File) resource.get();
			fileName = file.getAbsolutePath();
			writer = new Writer().initialice(file, projection);
			FeatureSet features = this.getStoreServices().getFeatureStore()
					.getFeatureSet();

			writer.begin();
			DisposableIterator it = features.fastIterator();
			while (it.hasNext()) {
				writer.add(getStoreServices().getFeatureProviderFromFeature(
						(org.gvsig.fmap.dal.feature.Feature) it.next()));
			}
			it.dispose();
			writer.end();
			resource.notifyChanges();
			features.dispose();
			counterNewsOIDs = 0;

		} catch (Exception e) {
			throw new PerformEditingException(fileName, e);
		} finally {
			resource.end();
		}
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

			DxfFile.EntityFactory featureMaker = new DxfFeatureMaker(projection);
			DxfFile.VarSettings headerManager = new DxfHeaderManager();
			DxfFile dxfFeatureFile = new DxfFile(projection, file
					.getAbsolutePath(), featureMaker, headerManager);

			try {
				dxfFeatureFile.load();
			} catch (Exception e1) {
				throw new LoadException(e1, fileName);
			}

			IObjList.vector features = (IObjList.vector) ((DxfFeatureMaker) featureMaker)
					.getObjects();
			String acadVersion = ((DxfHeaderManager) headerManager)
					.getAcadVersion();


			logger.info("load: acadVersion = '" + acadVersion + "'");

			GeometryManager gManager = GeometryLocator.getGeometryManager();

			if (!featureMaker.isDxf3DFile() && !headerManager.isWritedDxf3D()) {
				// y no están todos a 9999
				Feature[] features2D = new Feature[features.size()];
				for (int i = 0; i < features.size(); i++) {
					Feature fea = (Feature) features.get(i);
					if (fea.getGeometry() instanceof org.cresques.px.gml.Point3D) {
						Point point = (Point) fea.getGeometry();
						Point point2 = new Point();
						for (int j = 0; j < point.pointNr(); j++) {
							point2.add(point.get(j));
						}
						point2.setTextPoint(point.isTextPoint());
						fea.setGeometry(point2);
						features2D[i] = fea;

					} else if (fea.getGeometry() instanceof LineString3D) {
						LineString lineString = (LineString) fea.getGeometry();
						LineString lineString2 = new LineString();
						for (int j = 0; j < lineString.pointNr(); j++) {
							lineString2.add(lineString.get(j));
						}
						fea.setGeometry(lineString2);
						features2D[i] = fea;
					} else if (fea.getGeometry() instanceof Polygon3D) {
						Polygon polygon = (Polygon) fea.getGeometry();
						Polygon polygon2 = new Polygon();
						for (int j = 0; j < polygon.pointNr(); j++) {
							polygon2.add(polygon.get(j));
						}
						fea.setGeometry(polygon2);
						features2D[i] = fea;
					}
				}
				features.clear();
				for (int i = 0; i < features2D.length; i++) {
					features.add(features2D[i]);
				}
			}



			for (int i = 0; i < features.size(); i++) {

				FeatureProvider feature = store.createFeatureProvider(store
						.getStoreServices().getDefaultFeatureType());

				try {
					Feature fea = (Feature) features.get(i);

					feature.setOID(new Long(i));
					feature.set(ID_FIELD_ID, Integer.valueOf(i));
					feature.set(ID_FIELD_ENTITY, fea.getProp("dxfEntity"));
					feature.set(ID_FIELD_LAYER, fea.getProp("layer"));
					feature.set(ID_FIELD_COLOR, Integer.valueOf(fea
							.getProp("color")));
					feature.set(ID_FIELD_TEXT, fea.getProp("text"));
					feature.set(ID_FIELD_HEIGHTTEXT, toDouble(fea
							.getProp("textHeight")));
					feature.set(ID_FIELD_ROTATIONTEXT, toDouble(fea
							.getProp("textRotation")));
					feature.set(ID_FIELD_ELEVATION, toDouble(fea
							.getProp("elevation")));
					feature.set(ID_FIELD_THICKNESS, toDouble(fea
							.getProp("thickness")));
					// FIXME: Abria que pillar el resto de atributos del DXF.

					store.addFeatureProvider(feature);


					// FIXME: Habia una incongruencia en el codigo ya que al
					// campo
					// ID_FIELD_GEOMETRY igual le asignaba una geometria que un
					// valor de cadena como 'Point3D', 'Polyline2D' o
					// 'Polyline3D'
					// Faltaria un atributo ID_FIELD_FSHAPE ?
					//

					if (fea.getGeometry() instanceof Point
							&& !(fea.getGeometry() instanceof org.cresques.px.gml.Point3D)) {
						Point point = (Point) fea.getGeometry();
						Point2D pto = new Point2D.Double();
						pto = point.get(0);

						org.gvsig.fmap.geom.primitive.Point geom = (org.gvsig.fmap.geom.primitive.Point)gManager.create(TYPES.POINT , SUBTYPES.GEOM2D);
						geom.setX(pto.getX());
						geom.setY(pto.getY());
						feature.set(ID_FIELD_GEOMETRY, geom);
						if (this.envelope == null) {
							this.envelope = geom.getEnvelope();
						} else {
							this.envelope.add(geom.getEnvelope());
						}

						if (point.isTextPoint()) {
							/// TODO labeling
						}
					} else if (fea.getGeometry() instanceof org.cresques.px.gml.Point3D) {
						org.cresques.px.gml.Point3D point = (org.cresques.px.gml.Point3D) fea
								.getGeometry();
						Point3D pto = new Point3D();
						pto = point.getPoint3D(0);
						org.gvsig.fmap.geom.primitive.Point geom = (org.gvsig.fmap.geom.primitive.Point)gManager.create(TYPES.POINT , SUBTYPES.GEOM2DZ);
						geom.setX(pto.getX());
						geom.setY(pto.getY());
						geom.setCoordinateAt(2, pto.getZ());

						feature.set(ID_FIELD_GEOMETRY, geom);
						if (this.envelope == null) {
							this.envelope = geom.getEnvelope();
						} else {
							this.envelope.add(geom.getEnvelope());
						}

						if (point.isTextPoint()) {
							/// TODO labeling
						}
					} else if (fea.getGeometry() instanceof LineString
							&& !(fea.getGeometry() instanceof LineString3D)) {
						GeneralPathX genPathX = new GeneralPathX();
						Point2D[] pts = new Point2D[fea.getGeometry().pointNr()];
						for (int j = 0; j < fea.getGeometry().pointNr(); j++) {
							pts[j] = fea.getGeometry().get(j);
						}
						genPathX.moveTo(pts[0].getX(), pts[0].getY());
						for (int j = 1; j < pts.length; j++) {
							genPathX.lineTo(pts[j].getX(), pts[j].getY());
						}
						Curve geom = (Curve)gManager.create(TYPES.CURVE , SUBTYPES.GEOM2D);
						geom.setGeneralPath(genPathX);
						feature.set(ID_FIELD_GEOMETRY, geom);
						if (this.envelope == null) {
							this.envelope = geom.getEnvelope();
						} else {
							this.envelope.add(geom.getEnvelope());
						}

					} else if (fea.getGeometry() instanceof LineString3D) {
						GeneralPathX genPathX = new GeneralPathX();
						Point3D[] pts = new Point3D[fea.getGeometry().pointNr()];
						for (int j = 0; j < fea.getGeometry().pointNr(); j++) {
							pts[j] = ((LineString3D) fea.getGeometry())
									.getPoint3D(j);
						}
						genPathX.moveTo(pts[0].getX(), pts[0].getY());
						for (int j = 1; j < pts.length; j++) {
							genPathX.lineTo(pts[j].getX(), pts[j].getY());
						}
						double[] elevations = new double[pts.length];
						for (int j = 0; j < pts.length; j++) {
							elevations[j] = pts[j].getZ();
						}
						Curve geom = (Curve)gManager.create(TYPES.CURVE , SUBTYPES.GEOM2DZ);
						geom.setGeneralPath(genPathX);
						for (int j=0 ; j<elevations.length ; j++){
							geom.setCoordinateAt(j, 2, elevations[j]);
						}
						feature.set(ID_FIELD_GEOMETRY, geom);
						if (this.envelope == null) {
							this.envelope = geom.getEnvelope();
						} else {
							this.envelope.add(geom.getEnvelope());
						}

					} else if (fea.getGeometry() instanceof Polygon
							&& !(fea.getGeometry() instanceof Polygon3D)) {
						GeneralPathX genPathX = new GeneralPathX();
						Point2D firstPt = new Point2D.Double();
						firstPt = fea.getGeometry().get(0);
						Point2D[] pts = new Point2D[fea.getGeometry().pointNr() + 1];
						for (int j = 0; j < fea.getGeometry().pointNr(); j++) {
							pts[j] = fea.getGeometry().get(j);
						}
						pts[fea.getGeometry().pointNr()] = firstPt;
						genPathX.moveTo(pts[0].getX(), pts[0].getY());
						for (int j = 1; j < pts.length; j++) {
							genPathX.lineTo(pts[j].getX(), pts[j].getY());
						}
						Surface geom = (Surface)gManager.create(TYPES.SURFACE , SUBTYPES.GEOM2D);
						geom.setGeneralPath(genPathX);
						feature.set(ID_FIELD_GEOMETRY, geom);
						if (this.envelope == null) {
							this.envelope = geom.getEnvelope();
						} else {
							this.envelope.add(geom.getEnvelope());
						}

					} else if (fea.getGeometry() instanceof Polygon3D) {
						GeneralPathX genPathX = new GeneralPathX();
						Point3D firstPt = new Point3D();
						firstPt = ((Polygon3D) fea.getGeometry()).getPoint3D(0);
						Point3D[] pts = new Point3D[fea.getGeometry().pointNr() + 1];
						for (int j = 0; j < fea.getGeometry().pointNr(); j++) {
							pts[j] = ((Polygon3D) fea.getGeometry())
									.getPoint3D(j);
						}
						pts[fea.getGeometry().pointNr()] = firstPt;
						genPathX.moveTo(pts[0].getX(), pts[0].getY());
						for (int j = 1; j < pts.length; j++) {
							genPathX.lineTo(pts[j].getX(), pts[j].getY());
						}
						double[] elevations = new double[pts.length];
						for (int j = 0; j < pts.length; j++) {
							elevations[j] = pts[j].getZ();
						}
						Surface geom = (Surface)gManager.create(TYPES.SURFACE , SUBTYPES.GEOM2DZ);
						geom.setGeneralPath(genPathX);
						for (int j=0 ; j<elevations.length ; j++){
							geom.setCoordinateAt(j, 2, elevations[j]);
						}
						feature.set(ID_FIELD_GEOMETRY, geom);
						if (this.envelope == null) {
							this.envelope = geom.getEnvelope();
						} else {
							this.envelope.add(geom.getEnvelope());
						}
					} else {
						logger.warn(
							MessageFormat.format(
								"load: geometry type {1} not supported",
								new Object[] { fea.getGeometry().getClass().getName() }
							)
						);
					}
				} catch (Exception e) {
					throw new LoadException(e, fileName);
				}
				if (leyendBuilder != null) {
					leyendBuilder.process(feature);
				}

			}
		}

	}

	public class Writer {
		private Double DEFAULT_ELEVATION = new Double(0);

		private DxfFile.EntityFactory entityMaker;

		private IProjection proj = null;

		private int handle = 40; // Revisar porqué es 40.

		private int k = 0;

		private boolean dxf3DFile = false;
		private String fileName;

		public Writer initialice(File file, IProjection projection) {
			this.proj = projection;
			this.fileName = file.getAbsolutePath();
			entityMaker = new DxfEntityMaker(proj);

			return this;
		}

		public void begin() {
			entityMaker = new DxfEntityMaker(proj);
		}

		public void end() throws WriteException {
			try {
				DxfFile dxfFile = new DxfFile(null, fileName, entityMaker);
				dxfFile.setCadFlag(true);
				if (dxf3DFile) {
					dxfFile.setDxf3DFlag(true);
				}
				dxfFile.save(fileName);
				dxfFile.close();
			} catch (Exception e) {
				throw new WriteException(fileName, e);
			}
		}

		public void add(FeatureProvider feature) throws WriteException {
			try {
				Geometry geom = feature.getDefaultGeometry();
				GeometryType type = geom.getGeometryType();

				if ((TYPES.POINT == type.getType()) && (SUBTYPES.GEOM2DZ == type.getSubType())) {
					dxf3DFile = true;
					k = createPoint3D(handle, k, feature);

				} else if ((TYPES.POINT == type.getType()) && (SUBTYPES.GEOM2D == type.getSubType())) {
					k = createPoint2D(handle, k, feature);

				} else if ((TYPES.CURVE == type.getType()) && (SUBTYPES.GEOM2DZ == type.getSubType())) {
					dxf3DFile = true;
					k = createPolyline3D(handle, k, feature);

				} else if ((TYPES.ARC == type.getType()) && (SUBTYPES.GEOM2D == type.getSubType())) {
					k = createArc2D(handle, k, feature);

				} else if ((TYPES.CURVE == type.getType()) && (SUBTYPES.GEOM2D == type.getSubType())) {
					k = createLwPolyline2D(handle, k, feature, false);

				} else if ((TYPES.SURFACE == type.getType()) && (SUBTYPES.GEOM2DZ == type.getSubType())) {
					dxf3DFile = true;
					k = createPolyline3D(handle, k, feature);

				} else if ((TYPES.CIRCLE == type.getType()) && (SUBTYPES.GEOM2D == type.getSubType())) {
					k = createCircle2D(handle, k, feature);

				} else if ((TYPES.ELLIPSE == type.getType()) && (SUBTYPES.GEOM2D == type.getSubType())) {
					k = createEllipse2D(handle, k, feature);

				} else if ((TYPES.SURFACE == type.getType()) && (SUBTYPES.GEOM2D == type.getSubType())) {
					k = createLwPolyline2D(handle, k, feature, true);

				} else {
					logger.warn(
							MessageFormat.format(
									"Geometry '{1}' not yet supported",
									new Object[] {geom.getClass().getName()}
								)
						);
					k++;
				}
			} catch (Exception e) {
				throw new WriteException(fileName, e);
			}

		}

		private boolean hasText(FeatureProvider feature) {
			if (feature.isNull(ID_FIELD_TEXT)) {
				return false;
			}
			if (feature.get(ID_FIELD_TEXT).equals("")) {
				return false;
			}
			return true;
		}

		private DxfGroupVector updateProperties(FeatureProvider feature, int k) {
			DxfGroupVector polv = new DxfGroupVector();

			String layer = (String) feature.get(ID_FIELD_LAYER);
			Integer color = (Integer) feature.get(ID_FIELD_COLOR);
			Double thickness = (Double) feature.get(ID_FIELD_THICKNESS);

			DxfGroup geometryLayer = new DxfGroup(8, layer);

			DxfGroup handleGroup = new DxfGroup();
			handleGroup.setCode(5);
			handleGroup.setData(new Integer(handle + k).toString());

			DxfGroup handleColor = new DxfGroup();
			handleColor.setCode(62);
			handleColor.setData(color);

			DxfGroup handleThickness = new DxfGroup();
			handleThickness.setCode(39);
			handleThickness.setData(thickness);

			polv.add(geometryLayer);
			polv.add(handleGroup);
			polv.add(handleColor);
			return polv;
		}

		private int createPoint2D(int handle, int k, FeatureProvider feature)
				throws Exception {

			if (hasText(feature)) {
				return createText2D(handle, k, feature);
			}
			org.gvsig.fmap.geom.primitive.Point point = geomManager.createPoint(0, 0, SUBTYPES.GEOM2D);
			double[] pointCoords = new double[6];
			PathIterator pointIt = (feature.getDefaultGeometry())
					.getPathIterator(null);
			while (!pointIt.isDone()) {
				pointIt.currentSegment(pointCoords);
				point = geomManager.createPoint(pointCoords[0], pointCoords[1], SUBTYPES.GEOM2D);
				pointIt.next();
			}
			Point2D pto = new Point2D.Double(point.getX(), point.getY());

			DxfGroup px = new DxfGroup();
			DxfGroup py = new DxfGroup();
			DxfGroup pz = new DxfGroup();
			px.setCode(10);
			px.setData(new Double(pto.getX()));
			py.setCode(20);
			py.setData(new Double(pto.getY()));
			pz.setCode(30);
			// FIXME: POINT del DXF tiene cota. Le asigno cero arbitrariamente.
			pz.setData(new Double(0.0));
			DxfGroupVector pv = updateProperties(feature, k);
			pv.add(px);
			pv.add(py);
			pv.add(pz);
			entityMaker.createPoint(pv);
			k++;
			return k;
		}

		private int createText2D(int handle, int k, FeatureProvider feature)
				throws Exception {

			String text = feature.get(ID_FIELD_TEXT).toString();
			Double heightText = (Double) feature.get(ID_FIELD_HEIGHTTEXT);
			Double rotationText = (Double) feature.get(ID_FIELD_ROTATIONTEXT);

			DxfGroup handleText = new DxfGroup();
			handleText.setCode(1);
			handleText.setData(text);

			DxfGroup handleHeightText = new DxfGroup();
			handleHeightText.setCode(40);
			handleHeightText.setData(heightText);

			DxfGroup handleRotationText = new DxfGroup();
			handleRotationText.setCode(50);
			handleRotationText.setData(rotationText);

			org.gvsig.fmap.geom.primitive.Point point = geomManager.createPoint(0, 0, SUBTYPES.GEOM2D);
			double[] pointCoords = new double[6];
			PathIterator pointIt = (feature.getDefaultGeometry())
					.getPathIterator(null);
			while (!pointIt.isDone()) {
				pointIt.currentSegment(pointCoords);
				point = geomManager.createPoint(pointCoords[0], pointCoords[1], SUBTYPES.GEOM2D);
				pointIt.next();
			}
			Point2D pto = new Point2D.Double(point.getX(), point.getY());
			DxfGroup handleGroup = new DxfGroup();
			handleGroup.setCode(5);
			handleGroup.setData(new Integer(handle + k).toString());
			DxfGroup px = new DxfGroup();
			DxfGroup py = new DxfGroup();
			DxfGroup pz = new DxfGroup();
			px.setCode(10);
			px.setData(new Double(pto.getX()));
			py.setCode(20);
			py.setData(new Double(pto.getY()));
			pz.setCode(30);
			// FIXME: POINT del DXF tiene cota. Le asigno cero arbitrariamente.
			pz.setData(new Double(0.0));
			DxfGroupVector pv = updateProperties(feature, k);
			pv.add(handleText);
			pv.add(handleHeightText);
			pv.add(handleRotationText);
			pv.add(handleGroup);
			pv.add(px);
			pv.add(py);
			pv.add(pz);
			entityMaker.createText(pv);
			k++;
			return k;
		}

		private int createPoint3D(int handle, int k, FeatureProvider feature)
				throws Exception {
			if (hasText(feature)) {
				return createText3D(handle, k, feature);
			}
			org.gvsig.fmap.geom.primitive.Point point = (org.gvsig.fmap.geom.primitive.Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
			double[] pointCoords = new double[6];
			PathIterator pointIt = (feature.getDefaultGeometry())
					.getPathIterator(null);
			while (!pointIt.isDone()) {
				pointIt.currentSegment(pointCoords);
				point = (org.gvsig.fmap.geom.primitive.Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
				point.setCoordinateAt(0, pointCoords[0]);
				point.setCoordinateAt(1, pointCoords[1]);
				point.setCoordinateAt(2, pointCoords[2]);
				pointIt.next();
			}
			org.gvsig.fmap.geom.primitive.Point pto = (org.gvsig.fmap.geom.primitive.Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2DZ);
			pto.setCoordinateAt(0, point.getCoordinateAt(0));
			pto.setCoordinateAt(1,  point.getCoordinateAt(1));
			pto.setCoordinateAt(2, point.getCoordinateAt(2));
			DxfGroup px = new DxfGroup();
			DxfGroup py = new DxfGroup();
			DxfGroup pz = new DxfGroup();
			px.setCode(10);
			px.setData(new Double(pto.getX()));
			py.setCode(20);
			py.setData(new Double(pto.getY()));
			pz.setCode(30);
			pz.setData(new Double(pto.getCoordinateAt(2)));
			double velev = ((org.gvsig.fmap.geom.primitive.Point) feature.getDefaultGeometry())
					.getCoordinateAt(2);
			Double elevation = DEFAULT_ELEVATION;
			elevation = new Double(velev);
			DxfGroup handleElevation = new DxfGroup();
			handleElevation.setCode(38);
			handleElevation.setData(elevation);

			DxfGroupVector pv = updateProperties(feature, k);
			pv.add(handleElevation);
			pv.add(px);
			pv.add(py);
			pv.add(pz);
			entityMaker.createPoint(pv);
			k++;
			return k;
		}

		private int createText3D(int handle, int k, FeatureProvider feature)
				throws Exception {

			double velev = ((org.gvsig.fmap.geom.primitive.Point) feature.getDefaultGeometry())
			.getCoordinateAt(0);

			Double elevation = new Double(velev);
			String text = feature.get(ID_FIELD_TEXT).toString();
			Double heightText = (Double) feature.get(ID_FIELD_HEIGHTTEXT);
			Double rotationText = (Double) feature.get(ID_FIELD_ROTATIONTEXT);

			DxfGroup handleText = new DxfGroup();
			handleText.setCode(1);
			handleText.setData(text);

			DxfGroup handleHeightText = new DxfGroup();
			handleHeightText.setCode(40);
			handleHeightText.setData(heightText);

			DxfGroup handleRotationText = new DxfGroup();
			handleRotationText.setCode(50);
			handleRotationText.setData(rotationText);

			DxfGroup handleElevation = new DxfGroup();
			handleElevation.setCode(38);
			handleElevation.setData(elevation);

			org.gvsig.fmap.geom.primitive.Point point =
				(org.gvsig.fmap.geom.primitive.Point) (feature
					.getDefaultGeometry()).getInternalShape();

			DxfGroup handleGroup = new DxfGroup();
			handleGroup.setCode(5);
			handleGroup.setData(new Integer(handle + k).toString());
			DxfGroup px = new DxfGroup();
			DxfGroup py = new DxfGroup();
			DxfGroup pz = new DxfGroup();
			px.setCode(10);
			px.setData(new Double(point.getX()));
			py.setCode(20);
			py.setData(new Double(point.getY()));
			pz.setCode(30);
			pz.setData(new Double(point.getCoordinateAt(2)));
			DxfGroupVector pv = updateProperties(feature, k);
			pv.add(handleElevation);
			pv.add(handleText);
			pv.add(handleHeightText);
			pv.add(handleRotationText);
			pv.add(handleGroup);
			pv.add(px);
			pv.add(py);
			pv.add(pz);
			entityMaker.createText(pv);
			k++;
			return k;
		}

		private int createLwPolyline2D(int handle, int k, FeatureProvider feature,
				boolean isPolygon) throws Exception {
			boolean first = true;
			DxfGroupVector polv = updateProperties(feature, k);
			Vector vpoints = new Vector();

			DxfGroup polylineFlag = new DxfGroup();
			polylineFlag.setCode(70);
			if (isPolygon) {
				polylineFlag.setData(new Integer(1)); // cerrada
			} else {
				polylineFlag.setData(new Integer(0)); // abierta
			}

			PathIterator theIterator = (feature.getDefaultGeometry())
					.getPathIterator(null, Converter.FLATNESS); // polyLine.
																// getPathIterator
																// (null,
			// flatness);

			double[] theData = new double[6];
			while (!theIterator.isDone()) {
				int theType = theIterator.currentSegment(theData);
				switch (theType) {
				case PathIterator.SEG_MOVETO:
					if (!first) {
						for (int j = 0; j < vpoints.size(); j++) {
							DxfGroup xvertex = new DxfGroup();
							xvertex.setCode(10);
							xvertex
									.setData(new Double(
											((org.gvsig.fmap.geom.primitive.Point) vpoints
													.get(j)).getX()));
							DxfGroup yvertex = new DxfGroup();
							yvertex.setCode(20);
							yvertex
									.setData(new Double(
											((org.gvsig.fmap.geom.primitive.Point) vpoints
													.get(j)).getY()));
							polv.add(xvertex);
							polv.add(yvertex);
						}

						entityMaker.createLwPolyline(polv);
						k++;
						polv = updateProperties(feature, k);

					}
					first = false;
					polv.add(polylineFlag);
					vpoints.clear();
					vpoints.add(geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D));
					break;
				case PathIterator.SEG_LINETO:
					vpoints.add(geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D));
					break;
				case PathIterator.SEG_QUADTO:
					break;
				case PathIterator.SEG_CUBICTO:
					break;
				case PathIterator.SEG_CLOSE:
					polylineFlag.setData(new Integer(1)); // cerrada
					break;

				}
				theIterator.next();
			}

			for (int j = 0; j < vpoints.size(); j++) {
				DxfGroup xvertex = new DxfGroup();
				xvertex.setCode(10);
				xvertex
						.setData(new Double(
								((org.gvsig.fmap.geom.primitive.Point) vpoints
										.get(j)).getX()));
				DxfGroup yvertex = new DxfGroup();
				yvertex.setCode(20);
				yvertex
						.setData(new Double(
								((org.gvsig.fmap.geom.primitive.Point) vpoints
										.get(j)).getY()));
				polv.add(xvertex);
				polv.add(yvertex);
			}

			entityMaker.createLwPolyline(polv);
			k++;
			return k;
		}

		private int createPolyline3D(int handle, int k, FeatureProvider feature)
				throws Exception {
			DxfGroupVector polv = updateProperties(feature, k);
			Vector vpoints = new Vector();
			PathIterator theIterator = (feature.getDefaultGeometry())
					.getPathIterator(null, Converter.FLATNESS); // polyLine.
																// getPathIterator
																// (null,
			// flatness);
			double[] theData = new double[6];
			Curve curve = (Curve) feature.getDefaultGeometry();
			double[] velev = new double[curve.getNumVertices()];
			for (int i = 0; i < curve.getNumVertices(); i++) {
				velev[i] = curve.getCoordinateAt(i, 2);
			}

			while (!theIterator.isDone()) {
				int theType = theIterator.currentSegment(theData);
				switch (theType) {
				case PathIterator.SEG_MOVETO:
					vpoints.add(geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D));
					break;
				case PathIterator.SEG_LINETO:
					vpoints.add(geomManager.createPoint(theData[0], theData[1], SUBTYPES.GEOM2D));
					break;
				}
				theIterator.next();
			}
			if (constantElevation(velev)) {
				DxfGroup polylineFlag = new DxfGroup();
				polylineFlag.setCode(70);
				polylineFlag.setData(new Integer(0));
				polv.add(polylineFlag);
				DxfGroup elevation = new DxfGroup();
				elevation.setCode(38);
				elevation.setData(new Double(velev[0]));
				polv.add(elevation);
				for (int j = 0; j < vpoints.size(); j++) {
					DxfGroup xvertex = new DxfGroup();
					xvertex.setCode(10);
					xvertex.setData(new Double(
							((org.gvsig.fmap.geom.primitive.Point) vpoints
									.get(j)).getX()));
					DxfGroup yvertex = new DxfGroup();
					yvertex.setCode(20);
					yvertex.setData(new Double(
							((org.gvsig.fmap.geom.primitive.Point) vpoints
									.get(j)).getY()));
					polv.add(xvertex);
					polv.add(yvertex);
				}
				entityMaker.createLwPolyline(polv);
				k++;
			} else {
				DxfGroup polylineFlag = new DxfGroup();
				polylineFlag.setCode(70);
				polylineFlag.setData(new Integer(8));
				polv.add(polylineFlag);
				DxfGroup xgroup = new DxfGroup();
				xgroup.setCode(10);
				xgroup.setData(new Double(0.0));
				polv.add(xgroup);
				DxfGroup ygroup = new DxfGroup();
				ygroup.setCode(20);
				ygroup.setData(new Double(0.0));
				polv.add(ygroup);
				DxfGroup elevation = new DxfGroup();
				elevation.setCode(30);
				elevation.setData(new Double(0.0));
				polv.add(elevation);
				DxfGroup subclassMarker = new DxfGroup(100, "AcDb3dPolyline");
				polv.add(subclassMarker);
				entityMaker.createPolyline(polv);
				k++;
				for (int j = 0; j < vpoints.size(); j++) {
					DxfGroupVector verv = new DxfGroupVector();
					DxfGroup entityType = new DxfGroup(0, "VERTEX");
					verv.add(entityType);
					DxfGroup generalSubclassMarker = new DxfGroup(100,
							"AcDbEntity");
					verv.add(generalSubclassMarker);
					DxfGroup layerName = new DxfGroup(8, "default");
					verv.add(layerName);
					DxfGroup vertexSubclassMarker = new DxfGroup(100,
							"AcDbVertex");
					verv.add(vertexSubclassMarker);
					DxfGroup xvertex = new DxfGroup();
					xvertex.setCode(10);
					xvertex.setData(new Double(
							((org.gvsig.fmap.geom.primitive.Point) vpoints
									.get(j)).getX()));
					DxfGroup yvertex = new DxfGroup();
					yvertex.setCode(20);
					yvertex.setData(new Double(
							((org.gvsig.fmap.geom.primitive.Point) vpoints
									.get(j)).getY()));
					DxfGroup zvertex = new DxfGroup();
					zvertex.setCode(30);
					zvertex.setData(new Double(velev[j]));
					verv.add(xvertex);
					verv.add(yvertex);
					verv.add(zvertex);
					entityMaker.addVertex(verv);
					k++;
				}
				DxfGroupVector seqv = new DxfGroupVector();
				DxfGroup entityType = new DxfGroup(0, "SEQEND");
				seqv.add(entityType);
				DxfGroup generalSubclassMarker = new DxfGroup(100, "AcDbEntity");
				seqv.add(generalSubclassMarker);
				DxfGroup layerName = new DxfGroup(8, "default");
				seqv.add(layerName);
				DxfGroup handleSeqGroup = new DxfGroup();
				handleSeqGroup.setCode(5);
				handleSeqGroup.setData(new Integer(handle + k).toString());
				seqv.add(handleSeqGroup);
				entityMaker.endSeq();
				k++;
			}
			return k;
		}

		private boolean constantElevation(double[] velev) {
			boolean constant = true;
			for (int i = 0; i < velev.length; i++) {
				for (int j = 0; j < velev.length; j++) {
					if (j > i) {
						if (velev[i] != velev[j]) {
							constant = false;
							break;
						}
					}
				}
				break;
			}
			return constant;
		}

		private int createCircle2D(int handle, int k, FeatureProvider feature)
				throws Exception {
			DxfGroupVector polv = updateProperties(feature, k);
			DxfGroup circleFlag = new DxfGroup();
			circleFlag.setCode(100);
			polv.add(circleFlag);

			DxfGroup xvertex = new DxfGroup();
			xvertex.setCode(10);
			Circle circle = (Circle) (feature
					.getDefaultGeometry()).getInternalShape();
			xvertex.setData(new Double(circle.getCenter().getX()));
			DxfGroup yvertex = new DxfGroup();
			yvertex.setCode(20);
			yvertex.setData(new Double(circle.getCenter().getY()));
			DxfGroup zvertex = new DxfGroup();
			zvertex.setCode(30);
			// TODO: COORDENADA Z. REVISAR ESTO PARA ENTIDADES 3D
			zvertex.setData(new Double(0));

			DxfGroup radius = new DxfGroup();
			radius.setCode(40);
			radius.setData(new Double(circle.getRadious()));

			polv.add(xvertex);
			polv.add(yvertex);
			polv.add(zvertex);
			polv.add(radius);

			entityMaker.createCircle(polv);
			k++;
			return k;
		}

		private int createArc2D(int handle, int k, FeatureProvider feature)
				throws Exception {
			Arc arc = (Arc) (feature.getDefaultGeometry())
					.getInternalShape();
			org.gvsig.fmap.geom.primitive.Point[] pts = new org.gvsig.fmap.geom.primitive.Point[3];
			pts[0] = arc.getInitPoint();
			pts[1] = arc.getCenterPoint();
			pts[2] = arc.getEndPoint();
			org.gvsig.fmap.geom.primitive.Point center = arc.getCenterPoint();
			GeometryOperationContext ctx = new GeometryOperationContext();
			ctx.setAttribute("geom", pts[0]);
			double radius = ((Double)geomManager.invokeOperation(PointDistance.CODE, center, ctx)).doubleValue();

			double initAngle = ((Double)geomManager.invokeOperation(PointGetAngle.CODE, center, ctx)).doubleValue();
			initAngle = Math.toDegrees(initAngle);
			ctx.setAttribute("geom", pts[1]);
			double midAngle = ((Double)geomManager.invokeOperation(PointGetAngle.CODE, center, ctx)).doubleValue();
			midAngle = Math.toDegrees(midAngle);
			ctx.setAttribute("geom", pts[2]);
			double endAngle = ((Double)geomManager.invokeOperation(PointGetAngle.CODE, center, ctx)).doubleValue();
			endAngle = Math.toDegrees(endAngle);

			DxfGroup ax = new DxfGroup();
			DxfGroup ay = new DxfGroup();
			DxfGroup ac = new DxfGroup();
			DxfGroup ai = new DxfGroup();
			DxfGroup ae = new DxfGroup();
			ax.setCode(10);
			ax.setData(new Double(center.getX()));
			ay.setCode(20);
			ay.setData(new Double(center.getY()));
			ac.setCode(40);
			ac.setData(new Double(radius));
			ai.setCode(50);
			ai.setData(new Double(initAngle));
			ae.setCode(51);
			ae.setData(new Double(endAngle));
			DxfGroupVector av = updateProperties(feature, k);
			av.add(ax);
			av.add(ay);
			av.add(ac);
			av.add(ai);
			av.add(ae);
			entityMaker.createArc(av);
			k++;
			return k;
		}

		private int createEllipse2D(int handle, int k, FeatureProvider feature)
				throws Exception {
			Ellipse ellipse = (Ellipse) (feature
					.getDefaultGeometry()).getInternalShape();

			org.gvsig.fmap.geom.primitive.Point center = (org.gvsig.fmap.geom.primitive.Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			center.setCoordinateAt(0, (ellipse.getAxis1Start().getX() + ellipse.getAxis1End().getX()) / 2);
			center.setCoordinateAt(1, (ellipse.getAxis1Start().getY() + ellipse.getAxis1End().getY()) / 2);

			double mAxisL = ellipse.getAxis2Dist() * 2;
			GeometryOperationContext ctx = new GeometryOperationContext();
			ctx.setAttribute("geom", ellipse.getAxis1End());
			double maAxisL = ((Double)geomManager.invokeOperation(PointDistance.CODE, ellipse.getAxis1Start(), ctx)).doubleValue();

			Point2D endPointOfMajorAxis = new Point2D.Double(ellipse.getAxis1End().getX(), ellipse.getAxis1End().getY());
			double azimut = Math
					.atan2(endPointOfMajorAxis.getX() - center.getX(),
							endPointOfMajorAxis.getY() - center.getY());
			double azimut2 = azimut + Math.PI / 2.0;
			if (azimut2 >= Math.PI * 2) {
				azimut2 = azimut2 - Math.PI * 2;
			}
			Point2D endPointOfMinorAxis = new Point2D.Double(center.getX()
					+ (ellipse.getAxis2Dist() * Math.sin(azimut2)), center.getY()
					+ (ellipse.getAxis2Dist() * Math.cos(azimut2)));

			if (mAxisL >= maAxisL) {
				// El menor debe ser menor que el mayor. Los cambiamos.
				double aux = mAxisL;
				mAxisL = maAxisL;
				maAxisL = aux;
				// También cambiamos los puntos finales de los ejes.
				Point2D pAux = endPointOfMinorAxis;
				endPointOfMinorAxis = endPointOfMajorAxis;
				endPointOfMajorAxis = pAux;
			}
			double mToMAR = mAxisL / maAxisL;
			DxfGroup x = new DxfGroup();
			DxfGroup y = new DxfGroup();
			DxfGroup xc = new DxfGroup();
			DxfGroup yc = new DxfGroup();
			DxfGroup minToMaj = new DxfGroup();
			x.setCode(10);
			x.setData(new Double(center.getX()));
			y.setCode(20);
			y.setData(new Double(center.getY()));
			xc.setCode(11);
			xc.setData(new Double(endPointOfMajorAxis.getX() - center.getX()));
			yc.setCode(21);
			yc.setData(new Double(endPointOfMajorAxis.getY() - center.getY()));
			minToMaj.setCode(40);
			minToMaj.setData(new Double(mToMAR));
			DxfGroupVector av = updateProperties(feature, k);
			av.add(x);
			av.add(y);
			av.add(xc);
			av.add(yc);
			av.add(minToMaj);
			entityMaker.createEllipse(av);
			k++;
			return k;
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
		try {
			writer.add(featureProvider);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void beginAppend() {
		try {
			writer = new Writer().initialice((File) resource.get(), projection);
			writer.begin();
		} catch (AccessResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void endAppend() {
		try {
			resource.notifyOpen();
			writer.end();
			resource.notifyClose();
			counterNewsOIDs = 0;
			resource.end();
		} catch (ResourceNotifyOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceNotifyCloseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return this.getDXFParameters().getFile();
	}

}
