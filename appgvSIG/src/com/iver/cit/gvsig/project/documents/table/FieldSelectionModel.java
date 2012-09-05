package com.iver.cit.gvsig.project.documents.table;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;

import com.iver.utiles.swing.objectSelection.ObjectSelectionModel;
import com.iver.utiles.swing.objectSelection.SelectionException;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class FieldSelectionModel implements ObjectSelectionModel {
	private FeatureStore fs;
	private String msg;
	private int type=-1;

	/**
	 * Crea un nuevo FirstFieldSelectionModel.
	 *
	 * @param ds DOCUMENT ME!
	 * @param msg DOCUMENT ME!
	 * @param allowedTypes DOCUMENT ME!
	 */
	public FieldSelectionModel(FeatureStore fs, String msg,
		int type) {
		this.fs = fs;
		this.msg = msg;
		this.type = type;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws SelectionException
	 *
	 * @see com.iver.utiles.swing.objectSelection.ObjectSelectionModel#getObjects()
	 */
	public Object[] getObjects() throws SelectionException {
//		try {
//			ds.start();

			ArrayList fields = new ArrayList();
			Iterator iterator=null;
			try {
				iterator = fs.getDefaultFeatureType().iterator();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (iterator.hasNext()) {
				FeatureAttributeDescriptor descriptor = (FeatureAttributeDescriptor) iterator.next();
				if (type!=-1){
					if (descriptor.getDataType() == type) {
						fields.add(descriptor.getName());
					}
				}else{
					fields.add(descriptor.getName());
				}
			}
//			for (int i = 0; i < fs.getFieldCount(); i++) {
//				if (type != -1) {
//                    System.out.println(fs.getFieldName(i) + " tipo: " + fs.getFieldType(i));
//					if (fs.getFieldType(i) == type) {
//						fields.add(fs.getFieldName(i));
//					}
//				} else {
//					fields.add(fs.getFieldName(i));
//				}
//			}

//			ds.stop();

			return (String[]) fields.toArray(new String[0]);
//		} catch (ReadException e) {
//			throw new SelectionException(e);
//		}
	}

	/**
	 * @see com.iver.utiles.swing.objectSelection.ObjectSelectionModel#getMsg()
	 */
	public String getMsg() {
		return msg;
	}
}
