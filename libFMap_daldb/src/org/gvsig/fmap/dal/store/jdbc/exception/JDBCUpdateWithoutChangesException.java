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

package org.gvsig.fmap.dal.store.jdbc.exception;

import java.util.List;



public class JDBCUpdateWithoutChangesException extends JDBCException {


	/**
	 *
	 */
	private static final long serialVersionUID = 3706210974175686915L;
	private final static String MESSAGE_FORMAT = "An update SQL statement has no efect: SQL=\"{sql}\" Values='{sqlValues}'";
	private final static String MESSAGE_KEY = "_JDBCUpdateWithoutChangesException";


	public JDBCUpdateWithoutChangesException(String sql, List values) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.setValue("sql", sql);
		if (values == null || values.size() == 0) {
			this.setValue("sqlValues", "[]");
		} else {
			StringBuffer buff = new StringBuffer();
			buff.append('[');
			for (int i = 0; i < values.size() - 1; i++) {
				buff.append(values.get(i));
				buff.append(',');
			}
			buff.append(values.get(values.size() - 1));
			buff.append(']');
			this.setValue("sqlValues", buff.toString());
		}

	}

}
