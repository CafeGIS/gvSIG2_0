/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.store.jdbc.exception;

import java.sql.SQLException;
import java.util.List;


/**
 * @author jmvivo
 *
 */
public class JDBCExecutePreparedSQLException extends JDBCException {

	/**
	 *
	 */
	private static final long serialVersionUID = 634889167216570034L;

	private final static String MESSAGE_FORMAT = "An JDBC exception was throw when execute SQL: '%(sql)' with params %(params)";
	private final static String MESSAGE_KEY = "_JDBCExecutePreparedSQLException";

	public JDBCExecutePreparedSQLException(String sql, Object[] parameters,
			SQLException cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("sql", sql);
		String params = "{unknow}";
		if (parameters != null) {

			StringBuilder strb = new StringBuilder();
			strb.append('[');
			for (int i = 0; i < parameters.length - 1; i++) {
				strb.append(getParamValue(parameters[i]));
				strb.append(", ");
			}
			strb.append(getParamValue(parameters[parameters.length - 1]));
			strb.append(']');
			params = strb.toString();
		}
		setValue("params", params);
	}

	public JDBCExecutePreparedSQLException(String sql, List parameters,
			SQLException cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("sql", sql);
		String params = "{unknow}";
		if (parameters != null) {

			StringBuilder strb = new StringBuilder();
			strb.append('[');
			for (int i = 0; i < parameters.size() - 1; i++) {
				strb.append(getParamValue(parameters.get(i)));
				strb.append(", ");
			}
			strb.append(getParamValue(parameters.get(parameters.size() - 1)));
			strb.append(']');
			params = strb.toString();
		}
		setValue("params", params);
	}

	private String getParamValue(Object param) {

		if (param instanceof String) {
			return "'" + param + "'";
		}
		if (param == null) {
			return "null";
		}
		return param.toString();
	}

}
