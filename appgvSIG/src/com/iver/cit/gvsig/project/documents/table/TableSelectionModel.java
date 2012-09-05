package com.iver.cit.gvsig.project.documents.table;

import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.utiles.swing.objectSelection.ObjectSelectionModel;

/**
 * @author Fernando González Cortés
 */
public class TableSelectionModel implements ObjectSelectionModel{

	private FeatureTableDocument[] tables;
	private String msg;

	public TableSelectionModel(FeatureTableDocument[] tables, String msg){
		this.tables = tables;
		this.msg = msg;
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.table.TableSelectionModel#getTables()
	 */
	public Object[] getObjects() {
		return tables;
	}

	/**
	 * @see com.iver.utiles.swing.objectSelection.ObjectSelectionModel#getMsg()
	 */
	public String getMsg() {
		return msg;
	}

}
