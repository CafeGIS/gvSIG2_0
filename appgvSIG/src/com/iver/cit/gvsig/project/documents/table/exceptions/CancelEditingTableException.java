package com.iver.cit.gvsig.project.documents.table.exceptions;


/**
 * @author Vicente Caballero Navarro
 */
public class CancelEditingTableException extends TableEditingException {

	public CancelEditingTableException(String table,Throwable exception) {
		super(table,exception);
		init();
		initCause(exception);
	}

	private void init() {
		messageKey = "error_cancel_editing_table";
		formatString = "Can�t cancel editing the table: %(table) ";
	}

}
