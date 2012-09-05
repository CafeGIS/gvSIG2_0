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
package org.gvsig.fmap.data.feature.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.resource.exception.ResourceChangedException;
import org.gvsig.fmap.data.feature.db.DBResource;

/**
 * @author jmvivo
 *
 */
public abstract class JDBCResource extends DBResource {
	protected Connection connection = null;
	protected String url;

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#doDispose()
	 */
	protected void doDispose() throws DataException {
		Connection con =this.connection;
		this.connection =null;
		if (con != null){
			try {
				con.close();
			} catch (SQLException e) {
				throw new DataException("Dispose Error",e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#doOpen()
	 */
	protected boolean doOpen() throws OpenException {
		try {
			this.connection = this.createConnection();
		} catch (ReadException e) {
			throw new OpenException(this.getName(),e);
		}
		this.setOpened();
		return true;
	}

	public JDBCResource(JDBCStoreParameters params){
		super(params);
		this.url = params.getUrl();
	}

	public JDBCResource(JDBCExplorerParameter params){
		super(params);
		this.url = params.getUrl();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#doClose()
	 */
	protected boolean doClose() throws CloseException {
		try {
			connection.close();
		} catch (java.sql.SQLException e) {
			throw new CloseException(this.getName(),e);
		}
		return super.doClose();
	}


	protected abstract Connection createConnection() throws ReadException;


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#description()
	 */
	public String getDescription() {
		return this.getName()+" JDBC Connection Resource: url='"+this.getUrl()+"' user='"+this.getUser()+"'";
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#doChanged()
	 */
	protected void doChanged() {
		// No Operation
	}

	public boolean inUse() {
		return this.connection !=null;
	}

	protected Connection getConnection() throws ReadException{
		this.checkOpen();
		return this.connection;
	}

	protected Connection getWriterConnection() throws ReadException{
		return this.createConnection();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.Resource#checkChanged()
	 */
	protected void checkChanged() throws ResourceChangedException {
		// NO Operation

	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 * @throws InitializeException
	 */
	public void setUrl(String url) throws InitializeException {
		if (inUse()){
			throw new InitializeException("Resource in use",this.getDescription());
		}
		this.url = url;
	}
}

