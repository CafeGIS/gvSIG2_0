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

package org.gvsig.fmap.dal.store.raster;

import java.util.Iterator;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.raster.spi.AbstractCoverageStoreProvider;
import org.gvsig.fmap.dal.raster.spi.CoverageStoreProviderServices;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;

public class RasterStoreProvider extends AbstractCoverageStoreProvider {
	public static String NAME = "RasterStore";
	public static String DESCRIPTION = "Raster file";

	private static final String DYNCLASS_NAME = "RasterStore";
	protected static DynClass DYNCLASS = null;

	private RasterStoreParameters rasterParams;

	public RasterStoreProvider(RasterStoreParameters params) {
		super();
		init(params);
	}

	private void init(RasterStoreParameters params) {
		this.rasterParams = params;

		this.dynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(DYNCLASS);

		this.dynObject.setDynValue("DefaultSRS", this.getRasterParameters()
				.getSRS());
		this.dynObject.setDynValue("Envelope", null);

	}

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			// The SHP store parameters extend the DBF store parameters
			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));

			DYNCLASS = dynClass;
		}

	}


	public void close() throws CloseException {
		// TODO Auto-generated method stub

	}

	public void dispose() throws CloseException {
		// TODO Auto-generated method stub

	}

	public Iterator getChilds() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataServerExplorer getExplorer() throws ReadException,
			ValidateDataParametersException {
		DataManager manager = DALLocator.getDataManager();
		FilesystemServerExplorerParameters params;
		try {
			params = (FilesystemServerExplorerParameters) manager
					.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			params.setRoot(this.getRasterParameters().getFile().getParent());
			return manager.createServerExplorer(params);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		}
	}

	public RasterStoreParameters getRasterParameters() {
		return this.rasterParams;
	}



	public void open() throws OpenException {
		// TODO Auto-generated method stub

	}

	public void refresh() throws OpenException {
		// TODO Auto-generated method stub

	}

	public void delegate(DynObject arg0) {
		this.dynObject.delegate(arg0);

	}

	public DynClass getDynClass() {
		return this.dynObject.getDynClass();
	}

	public Object getDynValue(String arg0) throws DynFieldNotFoundException {
		return this.dynObject.getDynValue(arg0);
	}

	public boolean hasDynValue(String arg0) {
		return this.dynObject.hasDynValue(arg0);
	}

	public void implement(DynClass arg0) {
		this.dynObject.implement(arg0);

	}

	public Object invokeDynMethod(String arg0, DynObject arg1)
			throws DynMethodException {
		return this.dynObject.invokeDynMethod(arg0, arg1);
	}

	public Object invokeDynMethod(int arg0, DynObject arg1)
			throws DynMethodException {
		return this.dynObject.invokeDynMethod(arg0, arg1);
	}

	public void setDynValue(String arg0, Object arg1)
			throws DynFieldNotFoundException {
		this.dynObject.setDynValue(arg0, arg1);
	}

	public CoverageStoreProviderServices getStoreServices() {
		return this.store;
	}

	public String getName() {
		return NAME;
	}

	public Object getSourceId() {
		return this.getRasterParameters().getFile();
	}
}
