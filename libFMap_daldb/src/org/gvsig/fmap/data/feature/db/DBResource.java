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
* 2008 IVER T.I. S.A.   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.data.feature.db;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.spi.AbstractResource;

/**
 * @author jmvivo
 *
 */
public abstract class DBResource extends AbstractResource {
	protected DBParameters dbparameters;
	protected String user;
	protected String password;


	protected DBResource(ResourceParameters parameters)
			throws InitializeException {
		super(parameters);
		this.dbparameters = (DBParameters) parameters;
		this.user = this.dbparameters.getUser();
		this.password = this.dbparameters.getPassw();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 * @throws InitializeException
	 */
	public void setPassword(String password) throws InitializeException {
		if (inUse()){
			throw new InitializeException("Resource in use",this.getDescription());
		}
		this.password = password;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 * @throws InitializeException
	 */
	public void setUser(String user) throws InitializeException {
		if (this.getRefencesCount() > 0){
			throw new InitializeException("Resource in use",this.getDescription());
		}
		this.user = user;
	}

	public boolean testConnection() {
		try{
			this.open();
		} catch (OpenException e) {
			return false;
		}
		return true;
	}

	public abstract boolean inUse();

	//	protected boolean equalsValues(String v1, String v2){
	//		if (v1 == null || v1.length()== 0){
	//			return v2 == null || v2.length() == 0;
	//		} else if (v2 == null || v2.length() == 0){
	//			return false;
	//		}
	//		return v1.equals(v2);
	//
	//	}
	//

	//	public String getCatalog() {
	//		return this.parameters.getCatalog();
	//	}
	//
	//	public String getDb() {
	//		return this.parameters.getDb();
	//	}
	//
	//	public String getHost() {
	//		return this.parameters.getHost();
	//	}
	//
	//	public String getPassw() {
	//		return this.parameters.getHost();
	//	}
	//
	//	public String getPort() {
	//		return this.parameters.getPort();
	//	}
	//
	//	public String getSchema() {
	//		return this.parameters.getSchema();
	//	}

}
