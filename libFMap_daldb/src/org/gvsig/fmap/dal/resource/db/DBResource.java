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

package org.gvsig.fmap.dal.resource.db;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.spi.AbstractResource;

public abstract class DBResource extends AbstractResource {

	protected DBResource(DBResourceParameters parameters)
			throws InitializeException {
		super(parameters);
	}

	public Object get() throws AccessResourceException {
		if (!isConnected()) {
			try {
				this.connect();
			} catch (DataException e) {
				throw new AccessResourceException(this, e);
			}
		}
		try {
			return getTheConnection();
		} catch (DataException e) {
			throw new AccessResourceException(this, e);
		}
	}

	public Object getConnection() throws AccessResourceException {
		return get();
	}

	public abstract boolean isConnected();

	public final void connect() throws DataException {
		if (this.isConnected()) {
			return;
		}
		prepare();
		connectToDB();
	}

	// FIXME Excepcion
	protected abstract void connectToDB() throws DataException;


	// FIXME Exception
	protected abstract Object getTheConnection() throws DataException;

	public boolean isThis(ResourceParameters parameters)
			throws ResourceException {
		if (!(parameters instanceof DBResourceParameters)) {
			return false;
		}
		DBResourceParameters params = (DBResourceParameters) parameters
				.getCopy();
		prepare(params);
		DBResourceParameters myParams = (DBResourceParameters) this
				.getParameters();

		if (!equals(myParams.getHost(), params.getHost())) {
			return false;
		}
		if (!equals(myParams.getPort(), params.getPort())) {
			return false;
		}
		if (!equals(myParams.getUser(), params.getUser())) {
			return false;
		}

		return true;
	}

	protected boolean equals(Comparable v1, Comparable v2) {
		if (v1 == v2) {
			return true;
		}
		if (v1 != null) {
			return v1.compareTo(v2) == 0;
		}
		return false;
	}

}
