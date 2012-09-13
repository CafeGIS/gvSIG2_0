package es.unex.sextante.gvsig.core;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.sql.Types;
import java.util.Iterator;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.*;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.*;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.vividsolutions.jts.geom.Geometry;

import es.unex.sextante.dataObjects.AbstractVectorLayer;
import es.unex.sextante.dataObjects.IFeatureIterator;
import es.unex.sextante.dataObjects.IVectorLayer;

public class gvVectorLayer extends AbstractVectorLayer {

	private final int PRECISION = 5;

	private String m_sFilename;
	private FeatureStore featureStore;
	private int m_iGeometry;
	private String m_sName;
	private IProjection m_Projection;

	private FeatureType featureType;

	public void create(String sName, String sFilename, int iShapeType,
			Class[] types, String[] sFields, Object crs) {

		int iTypes[];
		try {
			FeatureType fType=getFeatureType(sFields, types, iShapeType, crs);
//			LayerDefinition tableDef;

			m_sName = sName;
			m_sFilename = sFilename;
			m_iGeometry = 0;
			m_Projection = (IProjection) crs;

			DataManager datamanager=DALLocator.getDataManager();
			FilesystemServerExplorerParameters explorerParams=(FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			explorerParams.setRoot(new File(sFilename).getParent());
			FilesystemServerExplorer explorer=(FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);
			NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(new File(sFilename));

			newParams.setDefaultFeatureType(featureType);
//			((FilesystemStoreParameters)newParams).setSRS((IProjection) crs);
			explorer.add(newParams, true);
			DataManager manager = DALLocator.getDataManager();
			featureStore = (FeatureStore) manager
			.createStore(newParams);
			featureStore.edit(FeatureStore.MODE_APPEND);




			if (sFilename.toLowerCase().endsWith("dxf")){
//				m_Writer = new DxfWriter();
//				((DxfWriter)m_Writer).setFile(new File(sFilename));
//				((DxfWriter)m_Writer).setProjection((IProjection) crs);
//				tableDef = new DXFLayerDefinition();
//				tableDef.setShapeType(getgvSIGShapeType(iShapeType));

//				DxfFieldsMapping fieldsMapping = new DxfFieldsMapping();
//				((DxfWriter)m_Writer).setFieldMapping(fieldsMapping);

			}
			else{
//				m_Writer = new ShpWriter();
//				((ShpWriter)m_Writer).setFile(new File(sFilename));
//				tableDef = new SHPLayerDefinition();
//				tableDef.setShapeType(getgvSIGShapeType(iShapeType));
			}

//			iTypes = DataTools.getgvSIGTypes(types);

//			FieldDescription[] fields = new FieldDescription[sFields.length];
//			for (int i = 0; i < fields.length; i++) {
//			fields[i] = new FieldDescription();
//			fields[i].setFieldName(sFields[i]);
//			fields[i].setFieldType(iTypes[i]);
//			fields[i].setFieldLength(getDataTypeLength(iTypes[i]));
//			fields[i].setFieldDecimalCount(PRECISION);
//			}
//			tableDef.setFieldsDesc(fields);
//			tableDef.setName(sFilename);

//			m_Writer.initialize(tableDef);
//			m_Writer.preProcess();

//			m_RV = null;


		} catch (Exception e){
			e.printStackTrace();
		}

	}

	private FeatureType getFeatureType(String[] fields, Class[] types,
			int shapeType, Object crs) {
		int[] iTypes=DataTools.getgvSIGTypes(types);
		EditableFeatureType eft=new DefaultEditableFeatureType();
		for (int i = 0; i < fields.length; i++) {
			EditableFeatureAttributeDescriptor efad=eft.add(fields[i], iTypes[i]);
			if (iTypes[i]==DataTypes.GEOMETRY){
				efad.setGeometryType(shapeType);
				efad.setSRS((IProjection)crs);
			}else{
				efad.setPrecision(PRECISION);
			}
		}
		return eft;
	}

//	private int getTypeToDAL(Class class1) {
//		if (class1.equals(Integer.class)){
//			return DataTypes.INT;
//		}else if (class1.equals(Long.class)){
//			return DataTypes.LONG;
//		}else if (class1.equals(Float.class)){
//			return DataTypes.FLOAT;
//		}else if (class1.equals(Double.class)){
//			return DataTypes.DOUBLE;
//		}else if (class1.equals(String.class)){
//			return DataTypes.STRING;
//		}else if (class1.equals(Date.class)){
//			return DataTypes.DATE;
//		}else if (class1.equals(Object.class)){
//			return DataTypes.OBJECT;
//		}else if (class1.equals(Byte.class)){
//			return DataTypes.BYTE;
//		}else if (class1.equals(Boolean.class)){
//			return DataTypes.BOOLEAN;
//		}else if (class1.equals(Geometry.class)){
//			return DataTypes.GEOMETRY;
//		}
//		return DataTypes.STRING;
//	}

	public void create(Object obj) {

		if (obj instanceof FLyrVect){
			m_BaseDataObject = obj;
			FLyrVect layer = (FLyrVect) m_BaseDataObject;
			try{
				featureStore = layer.getFeatureStore();
				featureType=featureStore.getDefaultFeatureType();
				m_Projection = layer.getProjection();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	public void open() {

//		if (featureStore != null) {
//
//			try {
//				featureStore.start();
//			} catch (InitializeDriverException e) {
//				e.printStackTrace();
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
//
//		};

	}

	public void close() {

//		if (m_RV != null){
//			try {
//				m_RV.stop();
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
//		};

	}

	/**
     * Returns the length of field
     * @param dataType
     * @return length of field
     */
	public int getDataTypeLength(int dataType) {

		switch (dataType) {
		case Types.NUMERIC:
		case Types.DOUBLE:
		case Types.REAL:
		case Types.FLOAT:
		case Types.BIGINT:
		case Types.INTEGER:
		case Types.DECIMAL:
			return 20;
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			return 254;
		case Types.DATE:
			return 8;
		case Types.BOOLEAN:
		case Types.BIT:
			return 1;
		}
		return 0;

	}

	public void addFeature(Geometry geom, Object[] values) {

		try {
            org.gvsig.fmap.geom.Geometry iGeo = Converter.jtsToGeometry(geom);
//		Value[] gvSIGValues = DataTools.getGVSIGValues(values);
//		DefaultFeature feat = new DefaultFeature(iGeo, gvSIGValues, Integer.toString(m_iGeometry));
//		IRowEdited editFeat = new DefaultRowEdited(feat, IRowEdited.STATUS_MODIFIED, m_iGeometry);
            m_iGeometry++;

            EditableFeature ef = featureStore.createNewFeature();
			Iterator<FeatureAttributeDescriptor> features=featureType.iterator();
			int i=0;
			while (features.hasNext()) {
				FeatureAttributeDescriptor featureAttributeDescriptor = features
						.next();
				if (!featureAttributeDescriptor.getName().equals(featureType.getDefaultGeometryAttributeName())){
					ef.set(featureAttributeDescriptor.getName(), values[i]);
					i++;
				}
			}
			ef.set(featureType.getDefaultGeometryAttributeIndex(), iGeo);
			featureStore.insert(ef);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IFeatureIterator iterator() {

		if (m_BaseDataObject instanceof FLyrVect){
			FLyrVect layer = (FLyrVect) m_BaseDataObject;
			gvFeatureIterator iter;
			try {
				iter = new gvFeatureIterator(layer);
			} catch (DataException e) {
				throw new RuntimeException(e);
			}
			return iter;
		}
		else{
			return null;
		}

	}

	public String getFieldName(int i) {

		if (featureStore != null){
			return ((FeatureAttributeDescriptor)featureType.get(i)).getName();
		}
		return null; //TODO
	}

	public Class getFieldType(int i) {

		if (featureStore != null){
			return DataTools.getTypeClass(((FeatureAttributeDescriptor)featureType.get(i)).getDataType());
		}
		return null; //TODO
	}


	public int getFieldCount() {

		if (featureStore != null){
			return featureType.size();
		}
		return 0; //TODO

	}

	public int getShapesCount() {


		if (featureStore != null){
			try {
				return (int)featureStore.getFeatureSet().getSize();
			} catch (DataException e) {
				e.printStackTrace();
				return 0;
			}
		}

		return 0; //TODO

	}

	public int getShapeType() {

		if (featureStore != null){
			return getShapeTypeFromGvSIGShapeType(((FeatureAttributeDescriptor)featureType.get(featureType.getDefaultGeometryAttributeIndex())).getGeometryType());
		}

		return 0;

	}

	private int getShapeTypeFromGvSIGShapeType(int shapeType) {

		switch (shapeType){
		case org.gvsig.fmap.geom.Geometry.TYPES.SURFACE:
			return IVectorLayer.SHAPE_TYPE_POLYGON;
		case org.gvsig.fmap.geom.Geometry.TYPES.CURVE:
			return IVectorLayer.SHAPE_TYPE_LINE;
		case org.gvsig.fmap.geom.Geometry.TYPES.POINT:
			return IVectorLayer.SHAPE_TYPE_POINT;
		default:
			return IVectorLayer.SHAPE_TYPE_POLYGON;
		}

	}

	private int getgvSIGShapeType(int shapeType) {

		switch (shapeType){
		case IVectorLayer.SHAPE_TYPE_POLYGON :
			return org.gvsig.fmap.geom.Geometry.TYPES.SURFACE;
		case IVectorLayer.SHAPE_TYPE_LINE:
			return org.gvsig.fmap.geom.Geometry.TYPES.CURVE;
		case IVectorLayer.SHAPE_TYPE_POINT:
			return org.gvsig.fmap.geom.Geometry.TYPES.POINT;
		default:
			return org.gvsig.fmap.geom.Geometry.TYPES.SURFACE;
		}

	}

	public String getName() {

		if (m_BaseDataObject instanceof FLyrVect){
			FLyrVect layer = (FLyrVect) m_BaseDataObject;
			return layer.getName();
		}
		else{
			return m_sName;
		}

	}

	public void postProcess() {

		if (featureStore == null){
			return;
		}
		try {
			featureStore.finishEditing();
		} catch (DataException e) {
			e.printStackTrace();
		}
		FLyrVect vectorLayer = (FLyrVect) FileTools.openLayer(
				m_sFilename, m_sName, m_Projection);
		create(vectorLayer);

	}

	public Rectangle2D getFullExtent() {

		if (m_BaseDataObject instanceof FLyrVect){
			FLyrVect layer = (FLyrVect) m_BaseDataObject;
			Envelope env=null;
			try {
				env = layer.getFullEnvelope();
			} catch (ReadException e) {
				e.printStackTrace();
			}
			return new Rectangle2D.Double(env.getMinimum(0), env.getMinimum(1), env.getLength(0), env.getLength(1));

		}
		return null;

	}

	public String getFilename() {

		if (m_BaseDataObject instanceof FLyrVect){
			FLyrVect layer = (FLyrVect) m_BaseDataObject;
			FeatureStore fs=null;
			try {
				fs = (layer).getFeatureStore();
			} catch (ReadException e) {
				e.printStackTrace();
			}
			DataStoreParameters dsp=fs.getParameters();
			if (dsp instanceof FilesystemStoreParameters) {
				return ((FilesystemStoreParameters)dsp).getFile().getAbsolutePath();
			}
			else{
				return null;
			}
		}
		else{
			return m_sFilename;
		}

	}

	public Object getCRS() {

		return m_Projection;

	}

	public void setName(String name) {

		m_sName = name;

	}

}
