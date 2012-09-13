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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.dal.store.wfs;

import java.io.IOException;
import java.util.List;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorerParameters;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.gpe.GPEStoreProvider;
import org.gvsig.remoteClient.wfs.WFSClient;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.exceptions.WFSException;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;


/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSStoreProvider extends GPEStoreProvider implements
ResourceConsumer {
	public static String NAME = "WFSStore";
	public static String DESCRIPTION = "WFS store to load WFS resources";
	private static final String DYNCLASS_NAME = "WFSStore";
	protected static DynClass DYNCLASS = null;

	//WFS Parameters
	private WFSClient wfsClient = null;
	private WFSStatus wfsStatus = null;


	public WFSStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices)
	throws InitializeException {
		super(params, storeServices, ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));

		WFSStoreParameters wfsParameters = getWFSParameters();
		try {
			if (wfsParameters.getVersion() == null){
				wfsClient = new WFSClient(wfsParameters.getUrl());
				wfsParameters.setVersion(wfsClient.getVersion());
			}else{
				wfsClient = new WFSClient(wfsParameters.getUrl(), wfsParameters.getVersion());
			}
		} catch (IOException e) {
			throw new InitializeException(e);
		}
		wfsStatus = new WFSStatus( wfsParameters.getFeatureType(),
				wfsParameters.getFeaturePrefix());
		wfsStatus.setNamespace(wfsParameters.getFeatureNamespace());
		wfsStatus.setFields(wfsParameters.getFields());
		//wfsStatus.setFilterQuery(wfsParameters.getFilterEncoding());
		wfsStatus.setTimeout(wfsParameters.getTimeOut());
		wfsStatus.setBuffer(wfsParameters.getMaxFeatures());
		wfsStatus.setUserName(wfsParameters.getUser());
		wfsStatus.setPassword(wfsParameters.getPassword());
	}

	private WFSStoreParameters getWFSParameters() {
		return (WFSStoreParameters) getParameters();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.store.gpe.GPEStoreProvider#open()
	 */
	public void open() throws OpenException {
		super.open();
		try {
			List featureTypes = this.getFeatureStore().getFeatureTypes();
			for (int i=0 ; i<featureTypes.size() ; i++){
				FeatureType featureType = (FeatureType)featureTypes.get(i);

			}
		} catch (DataException e) {
			throw new OpenException("Reading the geometry type", e);
		}
	}

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {

			dynClass = dynman.add(DYNCLASS_NAME);
			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));

			DYNCLASS = dynClass;
		}

	}

	protected void retrieveFile() throws InitializeException{
		try {
			m_Fich = wfsClient.getFeature(wfsStatus, true, null);
		} catch (WFSException e) {
			throw new InitializeException("Impossible to retrieve the file", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider#getExplorer()
	 */
	public DataServerExplorer getExplorer() throws ReadException {
		DataManager manager = DALLocator.getDataManager();
		WFSServerExplorerParameters params;
		try {
			params = (WFSServerExplorerParameters) manager
			.createServerExplorerParameters(WFSServerExplorer.NAME);
			params.setUrl(wfsClient.getHost());
			return manager.createServerExplorer(params);
		} catch (Exception e) {
			throw new ReadException(this.getName(), e);
		}
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.store.gpe.GPEStoreProvider#createSet(org.gvsig.fmap.dal.feature.FeatureQuery)
	 */
	@Override
	public FeatureSetProvider createSet(FeatureQuery query)
	throws DataException {
		executeQuery(query);
		return super.createSet(query);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.store.gpe.GPEStoreProvider#createSet(org.gvsig.fmap.dal.feature.FeatureQuery, org.gvsig.fmap.dal.feature.FeatureType)
	 */
	@Override
	public FeatureSetProvider createSet(FeatureQuery query,
			FeatureType featureType) throws DataException {
		executeQuery(query);
		return super.createSet(query, featureType);
	}

	/**
	 * Executes a new query sending the FilterEncoding request to the server
	 * @param query
	 * The Query to send
	 * @throws InitializeException
	 * If there is a problem with the request or the parsing process
	 */
	private void executeQuery(FeatureQuery query) throws DataException{
//		if (query.getFilter() instanceof FEIntersectsEvaluator){
//			wfsStatus.setFilterQuery(((FEIntersectsEvaluator)query.getFilter()).getFilterEncoding());
//			wfsStatus.setProtocol(OGCClientOperation.PROTOCOL_POST);
//			//Retrieve the new GML and parse it
//			initialize(getStoreServices());
//		}
	}
}

