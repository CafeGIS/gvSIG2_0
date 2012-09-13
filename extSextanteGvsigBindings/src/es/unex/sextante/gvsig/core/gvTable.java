package es.unex.sextante.gvsig.core;

import java.io.File;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.cit.gvsig.project.ProjectFactory;

import es.unex.sextante.dataObjects.AbstractTable;
import es.unex.sextante.dataObjects.IRecordsetIterator;

public class gvTable extends AbstractTable {

	private Object m_BaseDataObject;
	private String m_sName;
	private String m_sFilename;
	private FeatureSet featureSet;
	private FeatureType featureType;
	private FeatureStore featureStore;

	public String getName() {

		if (m_BaseDataObject instanceof FeatureTableDocument){
			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
			return table.getName();
		}
		else{
			return m_sName;
		}

	}

	public void create(Object obj) {

		if (obj instanceof FeatureTableDocument){
			try {
				featureStore =((FeatureTableDocument)obj).getStore();
				featureSet = featureStore.getFeatureSet(((FeatureTableDocument)obj).getQuery());
				featureType = featureStore.getDefaultFeatureType();

			} catch (DataException e) {
				e.printStackTrace();
			}
		}

	}

	public void create(String sName, String sFilename, Class[] types, String[] sFields) {

//		m_sFilename = sFilename;
//		TableMemoryDriver table = new TableMemoryDriver(sFields, DataTools.getgvSIGTypes(types));
//		m_sName = sName;
//		m_BaseDataObject = table;

	}

	public void addRecord(Object[] record) {

//		if (m_BaseDataObject instanceof TableMemoryDriver) {
//			TableMemoryDriver table = (TableMemoryDriver) m_BaseDataObject;
//			table.addRow(DataTools.getGVSIGValues(record));
//		}

	}

	public IRecordsetIterator iterator() {

		try {
			return new gvRecordsetIterator(featureSet);
		} catch (DataException e) {
			throw new RuntimeException(e);
		}

	}

	public String getFieldName(int i) {
		return ((FeatureAttributeDescriptor)featureType.get(i)).getName();
//		if (m_BaseDataObject instanceof FeatureTableDocument) {
//			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
//			try {
//				return table.getModelo().getRecordset().getFieldName(i);
//			} catch (Exception e) {
//				return "";
//			}
//		}
//		else{
//			TableMemoryDriver table = (TableMemoryDriver) m_BaseDataObject;
//			return table.getFieldName(i);
//		}

	}

	public Class getFieldType(int i) {
		return DataTools.getTypeClass(((FeatureAttributeDescriptor)featureType.get(i)).getDataType());
//		if (m_BaseDataObject instanceof FeatureTableDocument) {
//			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
//			try {
//				int iType = table.getModelo().getRecordset().getFieldType(i);
//				return DataTools.getTypeClass(iType);
//			} catch (Exception e) {
//				return String.class;
//			}
//		}
//		else{
//			TableMemoryDriver table = (TableMemoryDriver) m_BaseDataObject;
//			int iType;
//			iType = table.getFieldType(i);
//			return DataTools.getTypeClass(iType);
//		}

	}

	public int getFieldCount() {
		return featureType.size();
//		if (m_BaseDataObject instanceof FeatureTableDocument) {
//			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
//			try {
//				return table.getModelo().getRecordset().getFieldCount();
//			} catch (Exception e) {
//				return 0;
//			}
//		}
//		else{
//			TableMemoryDriver table = (TableMemoryDriver) m_BaseDataObject;
//			return table.getFieldCount();
//		}

	}

	public long getRecordCount() {
		try {
			return featureSet.getSize();
		} catch (DataException e) {
			e.printStackTrace();
		}
		return 0;
//		if (m_BaseDataObject instanceof FeatureTableDocument) {
//			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
//			try {
//				return table.getModelo().getRecordset().getRowCount();
//			} catch (Exception e) {
//				return 0;
//			}
//		}
//		else{
//			TableMemoryDriver table = (TableMemoryDriver) m_BaseDataObject;
//			return table.getRowCount();
//		}

	}

	public void postProcess() {

//		SelectableDataSource source;
//		ITableDefinition orgDef;
//		FileDriver driver;
		File file;
		try {
//			LayerFactory.getDataSourceFactory().addDataSource((TableMemoryDriver)m_BaseDataObject,
//					m_sName);
//			DataSource dataSource = LayerFactory.getDataSourceFactory().
//			createRandomDataSource(m_sName);
//			dataSource.start();
//			SelectableDataSource sds = new SelectableDataSource(dataSource);
//			EditableAdapter auxea = new EditableAdapter();
//			auxea.setOriginalDataSource(sds);
			FeatureTableDocument table = ProjectFactory.createTable(m_sName, featureStore);
			file = new File(m_sFilename);
//			driver = (FileDriver) LayerFactory.getDM().getDriver("gdbms dbf driver");
//			source = table.getModelo().getRecordset();
//			source.start();
//			orgDef = table.getModelo().getTableDefinition();
		} catch (Exception e) {
			return;
		}

//		try {
//			if (!file.exists()){
//				driver.createSource(file.getAbsolutePath(),new String[] {"0"},new int[] {Types.INTEGER} );
//				file.createNewFile();
//			}
//			driver.open(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		} catch (ReadException ex) {
//			ex.printStackTrace();
//			return;
//		}

//		IWriter writer = ((IWriteable)driver).getWriter();
//		try {
//			writer.initialize(orgDef);
//			writer.preProcess();
//			SourceIterator sourceIter = new SourceIterator(source);
//			IFeature feature;
//			int i=0;
//			while (sourceIter.hasNext()){
//				 feature = sourceIter.nextFeature();
//
//				 DefaultRowEdited edRow = new DefaultRowEdited(feature,
//						 DefaultRowEdited.STATUS_ADDED, i);
//				 writer.process(edRow);
//				 i++;
//			}
//			writer.postProcess();
//		} catch (Exception e) {
//			return;
//		}

		FeatureTableDocument table = FileTools.openTable(m_sFilename, m_sName);
		create(table);

	}

	public void open() {

//		if (m_BaseDataObject instanceof FeatureTableDocument){
//			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
//			try {
//				table.getModelo().getRecordset().start();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

	}

	public void close() {

//		if (m_BaseDataObject instanceof FeatureTableDocument){
//			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
//			try {
//				table.getModelo().getRecordset().stop();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

	}

	public String getFilename() {

		return m_sFilename;

	}

	public void setName(String name) {

		if (m_BaseDataObject instanceof FeatureTableDocument){
			FeatureTableDocument table = (FeatureTableDocument) m_BaseDataObject;
			table.setName(name);
		}
		else{
			m_sName = name;
		}

	}

	public Object getBaseDataObject() {

		return m_BaseDataObject;

	}


}
