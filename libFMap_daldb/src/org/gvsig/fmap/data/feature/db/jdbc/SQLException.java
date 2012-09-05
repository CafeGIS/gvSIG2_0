package org.gvsig.fmap.data.feature.db.jdbc;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.gvsig.tools.exception.IExceptionTranslator;
import org.gvsig.fmap.dal.exception.OpenException;

public class SQLException extends OpenException {
	protected String sql="";
	protected String operation="";
	public SQLException(String description,String name) {
		super(description,name);
		init();
	}

	public SQLException(String sql, String operation,Throwable exception) {
		super("SqlException",exception);
		this.sql = sql;
		this.operation = operation;
		init();
	}
	/**
	 *
	 */
	protected void init() {
		messageKey = "SQL_exception";
		formatString = "Error in %(operation) executing the sql statement: %(sql): %(description) ";
	}

	protected Map values() {
		Map params = super.values();
		params.put("operation",operation);
		params.put("sql",sql);
		return params;
	}
}
