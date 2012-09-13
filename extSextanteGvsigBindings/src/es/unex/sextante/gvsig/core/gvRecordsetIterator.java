package es.unex.sextante.gvsig.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;

import es.unex.sextante.dataObjects.IRecord;
import es.unex.sextante.dataObjects.IRecordsetIterator;
import es.unex.sextante.dataObjects.RecordImpl;

public class gvRecordsetIterator implements IRecordsetIterator {

	private FeatureSet featureSet;
//	long m_iIndex;
	private DisposableIterator featureIterator;

	gvRecordsetIterator(FeatureSet obj) throws DataException {

		featureSet = obj;
		try {
			featureIterator=featureSet.iterator();
		} catch (DataException e) {
			throw e;
		}
//		m_iIndex = 0;

	}

	public boolean hasNext() {
		return featureIterator.hasNext();
//		long iRecordCount = 0;
//		try{
//			if (m_Object instanceof TableMemoryDriver) {
//				TableMemoryDriver table = (TableMemoryDriver) m_Object;
//				iRecordCount = table.getRowCount();
//			}
//			else{
//				FeatureTableDocument table = (FeatureTableDocument) m_Object;
//				featureIterator = table.getStore().getFeatureSet(table.getQuery()).iterator();
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//
//		return iRecordCount > m_iIndex;

	}

	public IRecord next() {
		Feature feature = (Feature) featureIterator.next();
		FeatureType fType=feature.getType();
		Iterator<FeatureAttributeDescriptor> descriptors=fType.iterator();
		ArrayList<Object> objects=new ArrayList<Object>();
		while (descriptors.hasNext()) {
			FeatureAttributeDescriptor descriptor = descriptors.next();
			objects.add(feature.get(descriptor.getName()));
		}

		IRecord record = new RecordImpl(objects.toArray(new Object[0]));
//		int iFieldCount;
//		try{
//			if (m_Object instanceof TableMemoryDriver) {
//				TableMemoryDriver table = (TableMemoryDriver) m_Object;
//				iFieldCount = table.getFieldCount();
//				Object[] obj = new Object[iFieldCount];
//				for (int i = 0; i < iFieldCount; i++) {
//					obj[i] = table.getFieldValue(m_iIndex, i);
//				}
//				record = new RecordImpl(obj);
//			}
//			else{
//				FeatureTableDocument table = (FeatureTableDocument) m_Object;
//				Value[] obj = table.getModelo().getRecordset().getRow(m_iIndex);
//				record = new RecordImpl(DataTools.getSextanteValues(obj));
//			}
//		}
//		catch(Exception e){}
//
//		m_iIndex++;

		return record;

	}

	public void close() {
		if (featureIterator != null) {
			featureIterator.dispose();
			featureIterator = null;
		}
	}

}
