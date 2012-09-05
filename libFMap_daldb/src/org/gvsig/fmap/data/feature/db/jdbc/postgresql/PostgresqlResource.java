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
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.resource.exception.ResourceChangedException;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCDriverNotFoundException;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCExplorerParameter;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCResource;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStoreParameters;

/**
 * @author jmvivo
 *
 */
public class PostgresqlResource extends JDBCResource {

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCResource#getConnection()
	 */
	protected Connection getConnection() throws ReadException {
		return super.getConnection();
	}

	/**
	 * @param params
	 */
	PostgresqlResource(JDBCStoreParameters params) {
		super(params);
	}

	/**
	 * @param params
	 */
	PostgresqlResource(JDBCExplorerParameter params) {
		super(params);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCResource#createConnection()
	 */
	protected Connection createConnection() throws ReadException {
		Connection conn = null;

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new JDBCDriverNotFoundException("org.postgresql.Driver",e);
		}
		try {
			conn = DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());

		} catch (java.sql.SQLException e1) {
			throw new InitializeException(this.getName(),e1);
		}
		return conn;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#generateKey()
	 */
	protected String generateKey() {
		return this.getName()+";"+this.getUrl()+";"+this.getUser();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#getName()
	 */
	public String getName() {
		return "Postgresql";
	}

}

