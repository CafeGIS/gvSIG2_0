package org.gvsig.fmap.dal.store.dgn;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cresques.cts.IProjection;
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
import org.gvsig.fmap.dal.store.dgn.lib.DGNElemArc;
import org.gvsig.fmap.dal.store.dgn.lib.DGNElemCellHeader;
import org.gvsig.fmap.dal.store.dgn.lib.DGNElemComplexHeader;
import org.gvsig.fmap.dal.store.dgn.lib.DGNElemCore;
import org.gvsig.fmap.dal.store.dgn.lib.DGNElemMultiPoint;
import org.gvsig.fmap.dal.store.dgn.lib.DGNElemText;
import org.gvsig.fmap.dal.store.dgn.lib.DGNFileHeader;
import org.gvsig.fmap.dal.store.dgn.lib.DGNReader;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
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

public class DGNStoreProvider extends AbstractMemoryStoreProvider implements
		ResourceConsumer {
	private static final Logger logger = LoggerFactory.getLogger(DGNStoreProvider.class);

	public static final String NAME = "DGN";
	public static final String DESCRIPTION = "DGN file";
	public static final String DYNCLASS_NAME = "DGNStore";
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
	private int ID_FIELD_ENTITY = 1;
	private int ID_FIELD_LAYER = 2;
	private int ID_FIELD_COLOR = 3;
	private int ID_FIELD_ELEVATION = 4;
	private int ID_FIELD_THICKNESS = 5;
	private int ID_FIELD_TEXT = 6;
	private int ID_FIELD_HEIGHTTEXT = 7;
	private int ID_FIELD_ROTATIONTEXT = 8;
	private int ID_FIELD_GEOMETRY = 9;

	private IProjection projection;
	private ResourceProvider resource;
	private LegendBuilder legendBuilder;

	private long counterNewsOIDs = 0;
	protected GeometryManager geomManager = GeometryLocator.getGeometryManager();

	public DGNStoreProvider(DGNStoreParameters parameters,
			DataStoreProviderServices storeServices) throws InitializeException {
		super(parameters, storeServices, ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));

		counterNewsOIDs = 0;
		//		projection = CRSFactory.getCRS(getParameters().getSRSID());

		File file = getDGNParameters().getFile();
		resource = this.createResource(
				FileResource.NAME,
				new Object[] { file.getAbsolutePath() }
			);

		resource.addConsumer(this);

		this.projection = this.getDGNParameters().getSRS();


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

	private DGNStoreParameters getDGNParameters() {
		return (DGNStoreParameters) this.getParameters();
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

	private class DGNData {
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
			FeatureStoreProviderServices store = this.getStoreServices();
			DGNData DGNData = null;
			if (this.resource.getData() != null) {
				DGNData = (DGNData) ((Map) this.resource.getData())
						.get(this.projection
						.getAbrev()); // OJO no es del todo correcto (puede
										// llevar reproyeccion)
			} else {
				this.resource.setData(new HashMap());
			}
			if (DGNData == null) {
				DGNData = new DGNData();
				DGNData.data = new ArrayList();
				this.data = DGNData.data;
				this.counterNewsOIDs = 0;
				Reader reader = new Reader().initialice(
						this,
						new File((String) this.resource.get()),
						projection,
						this.legendBuilder
					);
				reader.begin(store);
				DGNData.defaultFType = reader.getDefaultType()
						.getNotEditableCopy();
				ArrayList types = new ArrayList();
				Iterator it = reader.getTypes().iterator();
				EditableFeatureType fType;
				while (it.hasNext()) {
					fType = (EditableFeatureType) it.next();
					if (fType.getId().equals(DGNData.defaultFType.getId())) {
						types.add(DGNData.defaultFType);
					} else {
						types.add(fType.getNotEditableCopy());
					}
				}
				DGNData.fTypes = types;

				resource.notifyOpen();
				store.setFeatureTypes(DGNData.fTypes, DGNData.defaultFType);
				reader.load();
				//				this.envelope = reader.getEnvelope();

				DGNData.envelope = reader.getEnvelope();

				DGNData.legendBuilder = this.legendBuilder;

				DGNData.projection = this.projection;

				reader.end();
				resource.notifyClose();
				((Map) this.resource.getData()).put(this.projection.getAbrev(),
						DGNData); // OJO la reproyeccion
			}

			this.data = DGNData.data;
			store.setFeatureTypes(DGNData.fTypes, DGNData.defaultFType);
			this.legendBuilder = DGNData.legendBuilder;
			this.setDynValue("Envelope", DGNData.getEnvelopeCopy());
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
			params.setRoot(this.getDGNParameters().getFile().getParent());
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


			EditableFeatureAttributeDescriptor attr = featureType.add(
					NAME_FIELD_GEOMETRY, DataTypes.GEOMETRY);
			attr.setSRS(this.projection);
			attr.setGeometryType(Geometry.TYPES.GEOMETRY);
			attr.setGeometrySubType(Geometry.SUBTYPES.GEOM2DZ);
			ID_FIELD_GEOMETRY = attr.getIndex();

			featureType.setDefaultGeometryAttributeName(NAME_FIELD_GEOMETRY);


			// FIXME: Parece que el DGN puede tener mas atributos opcionales.
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

			DGNReader dgnReader = new DGNReader(file.getAbsolutePath());

			FeatureType type = getDefaultType().getNotEditableCopy();
			int fTypeSize = type.size();
			Object[] auxRow = new Object[fTypeSize];
			Object[] cellRow = new Object[fTypeSize];
			Object[] complexRow = new Object[fTypeSize];

			boolean bElementoCompuesto = false;
			boolean bEsPoligono = false;
			boolean bInsideCell = false;
			boolean bFirstHoleEntity = false;
			boolean bConnect = false; // Se usa para que los polígonos cierren
										// bien cuando son formas compuestas
			int contadorSubElementos = 0;
			int numSubElementos = 0;
			int complex_index_fill_color = -1;
			int nClass; // Para filtrar los elementos de construcción, etc.
			GeneralPathX elementoCompuesto = new GeneralPathX(
					GeneralPathX.WIND_EVEN_ODD);

			for (int id = 0; id < dgnReader.getNumEntities(); id++) {
				// System.out.println("Elemento " + id + " de " +
				// dgnReader.getNumEntities());
				dgnReader.DGNGotoElement(id);

				DGNElemCore elemento = dgnReader.DGNReadElement();
				nClass = 0;
				auxRow[ID_FIELD_HEIGHTTEXT] = new Double(0);
				auxRow[ID_FIELD_ROTATIONTEXT] = new Double(0);
				auxRow[ID_FIELD_TEXT] = null;

				if (elemento.properties != 0) {
					nClass = elemento.properties & DGNFileHeader.DGNPF_CLASS;
				}

				if ((elemento != null) && (elemento.deleted == 0)
						&& (nClass == 0)) // Leer un elemento
				{

					// if ((elemento.element_id > 3800) && (elemento.element_id
					// < 3850))
					// dgnReader.DGNDumpElement(dgnReader.getInfo(),elemento,"");
					if ((elemento.stype == DGNFileHeader.DGNST_MULTIPOINT)
							|| (elemento.stype == DGNFileHeader.DGNST_ARC)
							|| (elemento.stype == DGNFileHeader.DGNST_CELL_HEADER)
							|| (elemento.stype == DGNFileHeader.DGNST_SHARED_CELL_DEFN)
							|| (elemento.stype == DGNFileHeader.DGNST_COMPLEX_HEADER)) {
						if (elemento.complex != 0) {
							bElementoCompuesto = true;
						} else {
							if (bElementoCompuesto) {
								if (bInsideCell) {
									auxRow[ID_FIELD_ENTITY] = cellRow[ID_FIELD_ENTITY];
								} else {
									auxRow = complexRow;
								}

								// System.err.println("Entidad compuesta. bInsideCell = "
								// + bInsideCell + " auxRow = " +
								// auxRow[ID_FIELD_ENTITY]);
								// addShape(new FPolyline2D(elementoCompuesto),
								// auxRow);
								addShape(createMultiCurve(elementoCompuesto),
										auxRow, type, dgnReader);

								if (bEsPoligono) {
									if (complex_index_fill_color != -1) {
										auxRow[ID_FIELD_COLOR] = complex_index_fill_color;
									}

									addShape(
											createMultiSurface(elementoCompuesto),
											auxRow, type, dgnReader);
								}

								elementoCompuesto = new GeneralPathX(
										GeneralPathX.WIND_EVEN_ODD);
							}

							// System.err.println("Entidad simple");
							bElementoCompuesto = false;
							bEsPoligono = false;
							bConnect = false;

							// elementoCompuesto = new GeneralPathX();
							bInsideCell = false;
						}
					}

					switch (elemento.stype) {
					case DGNFileHeader.DGNST_SHARED_CELL_DEFN:
						bInsideCell = true;
						cellRow[ID_FIELD_ID] = elemento.element_id;
						cellRow[ID_FIELD_LAYER] = String
								.valueOf(elemento.level);
						cellRow[ID_FIELD_COLOR] = elemento.color;
						cellRow[ID_FIELD_ENTITY] = "Shared Cell";

						break;

					case DGNFileHeader.DGNST_CELL_HEADER:
						bInsideCell = true;

						DGNElemCellHeader psCellHeader = (DGNElemCellHeader) elemento;
						cellRow[ID_FIELD_ID] = elemento.element_id;
						cellRow[ID_FIELD_LAYER] = String
								.valueOf(elemento.level);
						cellRow[ID_FIELD_COLOR] = elemento.color;
						cellRow[ID_FIELD_ENTITY] = "Cell";
						complex_index_fill_color = dgnReader
								.DGNGetShapeFillInfo(elemento);

						// System.err.println("Cell Header " +
						// complex_index_fill_color);
						break;

					case DGNFileHeader.DGNST_COMPLEX_HEADER:

						// bElementoCompuesto = true;
						// System.err.println("Complex Header");
						contadorSubElementos = 0;

						DGNElemComplexHeader psComplexHeader = (DGNElemComplexHeader) elemento;
						numSubElementos = psComplexHeader.numelems;
						complexRow[ID_FIELD_ID] = elemento.element_id;
						complexRow[ID_FIELD_LAYER] = String
								.valueOf(elemento.level);
						complexRow[ID_FIELD_COLOR] = elemento.color;
						complexRow[ID_FIELD_ENTITY] = "Complex";

						if (psComplexHeader.type == DGNFileHeader.DGNT_COMPLEX_SHAPE_HEADER) {
							bEsPoligono = true;

							// Si es un agujero, no conectamos con el anterior
							if ((psComplexHeader.properties & 0x8000) != 0) {
								bFirstHoleEntity = true;
							} else {
								// Miramos si tiene color de relleno
								// complex_index_fill_color = -1;
								// if (elemento.attr_bytes > 0) {
								complex_index_fill_color = dgnReader
										.DGNGetShapeFillInfo(elemento);

								// System.err.println("complex shape fill color = "
								// + elemento.color);
								// }
							}

							bConnect = true;
						} else {
							bEsPoligono = false;
							bConnect = false;
						}

						break;

					case DGNFileHeader.DGNST_MULTIPOINT:

						// OJO: Si lo que viene en este multipoint es un
						// elemento con type=11 (curve), se trata de una
						// "parametric
						// spline curve". La vamos a tratar como si no fuera
						// curva, pero según la documentación, los 2 primeros
						// puntos
						// y los 2 últimos puntos definen "endpoint derivatives"
						// y NO se muestran.
						// TODAVÍA HAY UN PEQUEÑO FALLO CON EL FICHERO
						// dgn-sample.dgn, pero lo dejo por ahora.
						// Es posible que tenga que ver con lo de los arcos
						// (arco distorsionado), que
						// todavía no está metido.
						DGNElemMultiPoint psLine = (DGNElemMultiPoint) elemento;
						auxRow[ID_FIELD_ID] = elemento.element_id;
						auxRow[ID_FIELD_ENTITY] = "Multipoint";
						auxRow[ID_FIELD_LAYER] = String.valueOf(elemento.level);
						auxRow[ID_FIELD_COLOR] = elemento.color;

						if ((psLine.num_vertices == 2)
								&& (psLine.vertices[0].x == psLine.vertices[1].x)
								&& (psLine.vertices[0].y == psLine.vertices[1].y)) {
							auxRow[ID_FIELD_ENTITY] = "Point";
							addShape(
									createPoint3D(psLine.vertices[0].x,
											psLine.vertices[0].y,
											psLine.vertices[0].z), auxRow,
									type, dgnReader);
						} else {
							GeneralPathX elShape = new GeneralPathX(
									GeneralPathX.WIND_EVEN_ODD);

							if (psLine.type == DGNFileHeader.DGNT_CURVE) {
								psLine.num_vertices = psLine.num_vertices - 4;

								for (int aux_n = 0; aux_n < psLine.num_vertices; aux_n++) {
									psLine.vertices[aux_n] = psLine.vertices[aux_n + 2];
								}
							}

							if ((psLine.type == DGNFileHeader.DGNT_SHAPE)
									&& ((psLine.properties & 0x8000) != 0)) {
								// Invertimos el orden porque es un agujero
								elShape
										.moveTo(
												psLine.vertices[psLine.num_vertices - 1].x,
												psLine.vertices[psLine.num_vertices - 1].y);

								for (int i = psLine.num_vertices - 2; i >= 0; i--) {
									elShape.lineTo(psLine.vertices[i].x,
											psLine.vertices[i].y);
								}
							} else {
								elShape.moveTo(psLine.vertices[0].x,
										psLine.vertices[0].y);

								for (int i = 1; i < psLine.num_vertices; i++) {
									elShape.lineTo(psLine.vertices[i].x,
											psLine.vertices[i].y);
								}
							}

							if ((psLine.vertices[0].x == psLine.vertices[psLine.num_vertices - 1].x)
									&& (psLine.vertices[0].y == psLine.vertices[psLine.num_vertices - 1].y)) {
								// Lo añadimos también como polígono
								bEsPoligono = true;

								// Miramos si tiene color de relleno
								if (elemento.attr_bytes > 0) {
									elemento.color = dgnReader
											.DGNGetShapeFillInfo(elemento);

									// System.err.println("fill color = " +
									// elemento.color);
									if (elemento.color != -1) {
										auxRow[ID_FIELD_COLOR] = elemento.color;
									}
								}

								if (elemento.complex == 0) {
									addShape(createSurface(elShape), auxRow,
											type, dgnReader);
								}
							}

							if (elemento.complex != 0) {
								// Si es un agujero o
								// es la primera entidad del agujero, lo
								// añadimos sin unir al anterior
								if (bFirstHoleEntity
										|| ((psLine.type == DGNFileHeader.DGNT_SHAPE) && ((psLine.properties & 0x8000) != 0))) {
									elementoCompuesto.append(elShape, false);
									bFirstHoleEntity = false;
								} else {
									elementoCompuesto.append(elShape, bConnect);
								}
							} else {
								addShape(createMultiCurve(elShape), auxRow,
										type, dgnReader);
							}
						}

						break;

					case DGNFileHeader.DGNST_ARC:

						// dgnReader.DGNDumpElement(dgnReader.getInfo(),
						// elemento,"");
						DGNElemArc psArc = (DGNElemArc) elemento;

						// La definición de arco de MicroStation es distinta a
						// la de Java.
						// En el dgn el origin se entiende que es el centro del
						// arco,
						// y a la hora de crear un Arc2D las 2 primeras
						// coordenadas son
						// la esquina inferior izquierda del rectángulo que
						// rodea al arco.
						// 1.- Creamos la elipse sin rotación.
						// 2.- Creamos el arco
						// 3.- Rotamos el resultado

						/*
						 * System.out.println("Arco con primari axis: " +
						 * psArc.primary_axis + " start angle: " +
						 * psArc.startang + " sweepang = " + psArc.sweepang);
						 * System.out.println("secondaria axis: " +
						 * psArc.secondary_axis + " rotation = " +
						 * psArc.rotation);
						 */
						AffineTransform mT = AffineTransform.getRotateInstance(
								Math.toRadians(psArc.rotation), psArc.origin.x,
								psArc.origin.y);

						// mT.preConcatenate(AffineTransform.getScaleInstance(100.0,100.0));
						Arc2D.Double elArco = new Arc2D.Double(psArc.origin.x
								- psArc.primary_axis, psArc.origin.y
								- psArc.secondary_axis,
								2.0 * psArc.primary_axis,
								2.0 * psArc.secondary_axis, -psArc.startang,
								-psArc.sweepang, Arc2D.OPEN);

						// Ellipse2D.Double elArco = new
						// Ellipse2D.Double(psArc.origin.x - psArc.primary_axis,
						// psArc.origin.y - psArc.secondary_axis,2.0 *
						// psArc.primary_axis, 2.0 * psArc.secondary_axis);
						GeneralPathX elShapeArc = new GeneralPathX(elArco);

						// Transformamos el GeneralPahtX porque si transformamos
						// elArco nos lo convierte
						// a GeneralPath y nos guarda las coordenadas en float,
						// con la correspondiente pérdida de precisión
						elShapeArc.transform(mT);

						if (dgnReader.getInfo().dimension == 3) {
							// Aquí podríamos hacer cosas con la coordenada Z
						}

						auxRow[ID_FIELD_ID] = elemento.element_id;
						auxRow[ID_FIELD_ENTITY] = "Arc";
						auxRow[ID_FIELD_LAYER] = String.valueOf(elemento.level);
						auxRow[ID_FIELD_COLOR] = elemento.color;

						/*
						 * Line2D.Double ejeMayor = new
						 * Line2D.Double(psArc.origin.x - psArc.primary_axis,
						 * psArc.origin.y, psArc.origin.x + psArc.primary_axis,
						 * psArc.origin.y);
						 *
						 * lyrLines.addShape(new
						 * FShape(FConstant.SHAPE_TYPE_POLYLINE, new
						 * GeneralPathX(ejeMayor)), auxRow);
						 */

						// lyrLines.addShape(new
						// FShape(FConstant.SHAPE_TYPE_POLYLINE, elShapeArc),
						// auxRow);
						if (elemento.complex != 0) {
							// Esto es una posible fuente de fallos si detrás de
							// una
							// elipse vienen más cosas pegadas. Deberíamos
							// volver
							// a conectar una vez pasada la elipse.
							if (elemento.type == DGNFileHeader.DGNT_ELLIPSE) {
								bConnect = false;
							}

							// SI LA ELIPSE ES UN AGUJERO, SE AÑADE SIN PEGAR
							// Y EL ELEMENTO ES UN POLIGONO
							if (bFirstHoleEntity
									|| ((elemento.type == DGNFileHeader.DGNT_SHAPE) && ((elemento.properties & 0x8000) != 0))) {
								elementoCompuesto.append(elShapeArc, false);
								bFirstHoleEntity = false;
							} else {
								elementoCompuesto.append(elShapeArc, bConnect);
							}
						} else {
							addShape(createMultiCurve(elShapeArc), auxRow,
									type, dgnReader);

							if (psArc.type == DGNFileHeader.DGNT_ELLIPSE) {
								addShape(createSurface(elShapeArc), auxRow,
										type, dgnReader);
							}
						}

						// System.err.println("Entra un Arco");
						break;

					case DGNFileHeader.DGNST_TEXT:

						DGNElemText psText = (DGNElemText) elemento;
						Geometry elShapeTxt = createPoint3D(psText.origin.x,
								psText.origin.y, psText.origin.z);

						auxRow[ID_FIELD_ID] = elemento.element_id;
						auxRow[ID_FIELD_ENTITY] = "Text";
						auxRow[ID_FIELD_LAYER] = String.valueOf(elemento.level);
						auxRow[ID_FIELD_COLOR] = elemento.color;
						auxRow[ID_FIELD_HEIGHTTEXT] = psText.height_mult;
						auxRow[ID_FIELD_ROTATIONTEXT] = psText.rotation;
						auxRow[ID_FIELD_TEXT] = psText.string; // .trim();
						addShape(elShapeTxt, auxRow, type, dgnReader);

						// System.out.println("Rotación texto: " +
						// psText.rotation + "Altura Texto = " + heightText);

						/*
						 * System.out.println("  origin=(" + psText.origin.x +
						 * ", " + psText.origin.y + ") rotation=" +
						 * psText.rotation + "\n" + "  font=" + psText.font_id +
						 * " just=" + psText.justification + "length_mult=" +
						 * psText.length_mult + " height_mult=" +
						 * psText.height_mult + "\n" + "  string =" + new
						 * String(psText.string).toString().trim() + "\n");
						 */
						break;

					/*
					 * default:
					 * dgnReader.DGNDumpElement(dgnReader.getInfo(),
					 * elemento, "");
					 */
					} // switch
				} // if
			} // for

			if (bElementoCompuesto) {
				if (bInsideCell) {
					auxRow = cellRow;
				} else {
					auxRow = complexRow;
				}

				// System.err.println("Entidad compuesta. bInsideCell = " +
				// bInsideCell + " auxRow = " + auxRow[ID_FIELD_ENTITY]);
				addShape(createMultiCurve(elementoCompuesto), auxRow, type,
						dgnReader);

				if (bEsPoligono) {
					if (complex_index_fill_color != -1) {
						auxRow[ID_FIELD_COLOR] = complex_index_fill_color;
					}

					addShape(createSurface(elementoCompuesto), auxRow, type,
							dgnReader);
				}
			}

		}

		private Geometry createMultiSurface(GeneralPathX elementoCompuesto)
				throws DataException {
			try {
				return geomManager.createMultiSurface(elementoCompuesto,
						SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(
						e);
			}

		}

		private Geometry createPoint3D(double x, double y, double z)
				throws DataException {
			Point point;
			try {
				// point = (Point) geomManager.create(TYPES.POINT,
				// SUBTYPES.GEOM3D);
				point = (Point) geomManager.create(TYPES.POINT,
						SUBTYPES.GEOM2DZ);
			} catch (CreateGeometryException e) {
				throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(
						e);
			}
			point.setCoordinates(new double[] { x, y, z });

			return point;
		}

		private void addShape(Geometry geometry, Object[] auxRow,
				FeatureType type, DGNReader dgnReader) throws DataException {
			FeatureProvider data = createFeatureProvider(type);
			for (int i=0;i<type.size();i++){
				data.set(i, auxRow[i]);
			}
			data.setDefaultGeometry(geometry);
			addFeatureProvider(data);
			if (this.envelope == null) {
				this.envelope = geometry.getEnvelope();
			} else {
				this.envelope.add(geometry.getEnvelope());
			}
			if (this.leyendBuilder != null) {
				this.leyendBuilder.process(data, dgnReader);
			}
		}

		private Geometry createMultiCurve(GeneralPathX elementoCompuesto)
				throws DataException {
			try {
				return geomManager.createMultiCurve(elementoCompuesto,
						SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(
						e);
			}
		}

		private Geometry createSurface(GeneralPathX elementoCompuesto)
				throws DataException {
			try {
				return geomManager.createCurve(elementoCompuesto,
						SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				throw new org.gvsig.fmap.dal.feature.exception.CreateGeometryException(
						e);
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
			dynClass = dynman.add(DYNCLASS_NAME, "DGN File Store");
			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));

			DYNCLASS = dynClass;
		}

	}

	public Object getSourceId() {
		return this.getDGNParameters().getFile();
	}

}
