package com.iver.cit.gvsig.project.documents.table.exceptions;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;
/**
 * @author Vicente Caballero Navarro
 */
public class TableEditingException extends BaseException{
	private String table;
	public TableEditingException(String table,Throwable exception) {
		this.table = table;
		init();
		initCause(exception);
	}

	private void init() {
		messageKey = "error_editing_table";
		formatString = "Can�t edit the table: %(table) ";
	}

	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("table",table);
		return params;
	}
}
