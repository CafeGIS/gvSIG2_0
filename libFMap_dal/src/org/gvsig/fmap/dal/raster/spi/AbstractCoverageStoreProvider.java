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
package org.gvsig.fmap.dal.raster.spi;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.raster.CoverageSelection;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author jmvivo
 *
 */
public abstract class AbstractCoverageStoreProvider implements
		CoverageStoreProvider {
	protected CoverageStoreProviderServices store;
	protected Logger logger;
	protected DelegatedDynObject dynObject;

	public AbstractCoverageStoreProvider() {
		this.store = null;
		this.logger = null;
		this.dynObject = null;
	}

	public CoverageStoreProvider initialize(CoverageStoreProviderServices store)
			throws InitializeException {
		this.store = store;
		return this;
	}

	protected ResourceProvider createResource(String type, Object[] params)
			throws InitializeException {
		ResourceManagerProviderServices manager = (ResourceManagerProviderServices) DALLocator
				.getResourceManager();
		ResourceProvider resource = manager.createResource(type, params);
		return resource;
	}

	public CoverageStoreProviderServices getStoreServices() {
		return this.store;
	}

	public String getClassName() {
		return this.getClass().getName();
	}

	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = LoggerFactory.getLogger(this.getClass());
		}
		return this.logger;
	}

	public boolean allowWrite() {
		return false;
	}

	public CoverageSelection createCoverageSelection() throws DataException {
		return this.store.createDefaultCoverageSelection();
	}

	public void refresh() throws OpenException {
		// Do nothing by default
	}

	public void close() throws CloseException {
		// Do nothing by default
	}

	public void dispose() throws CloseException {
		this.dynObject = null;
		this.store = null;
		this.logger = null;
	}

	public Envelope getEnvelope() throws DataException {
		return null;
	}

	public abstract DataServerExplorer getExplorer() throws ReadException,
			ValidateDataParametersException;

	public void delegate(DynObject dynObject) {
		if (this.dynObject == null) {
			return;
		}
		this.dynObject.delegate(dynObject);
	}

	public DynClass getDynClass() {
		if (this.dynObject == null) {
			return null;
		}
		return this.dynObject.getDynClass();
	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		if (this.dynObject == null) {
			return null;
		}
		// TODO this.open??
		return this.dynObject.getDynValue(name);
	}

	public boolean hasDynValue(String name) {
		if (this.dynObject == null) {
			return false;
		}
		// TODO this.open??
		return this.dynObject.hasDynValue(name);
	}

	public void implement(DynClass dynClass) {
		if (this.dynObject == null) {
			return;
		}
		this.dynObject.implement(dynClass);

	}

	public Object invokeDynMethod(int code, DynObject context)
			throws DynMethodException {
		if (this.dynObject == null) {
			return null;
		}
		// TODO this.open??
		return this.dynObject.invokeDynMethod(this, code, context);
	}

	public Object invokeDynMethod(String name, DynObject context)
			throws DynMethodException {
		if (this.dynObject == null) {
			return null;
		}
		// TODO this.open??
		return this.dynObject.invokeDynMethod(this, name, context);
	}

	public void setDynValue(String name, Object value)
			throws DynFieldNotFoundException {
		if (this.dynObject == null) {
			return;
		}
		// TODO this.open??
		this.dynObject.setDynValue(name, value);
	}

}
