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

package org.gvsig.app.eventlayer.daltransform;

import java.io.File;

import org.gvsig.app.eventtheme.dal.feature.EventThemeTransform;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.store.dbf.DBFLibrary;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class EventThemeTransformTest extends BaseFeatureStoreTransform {
	private final File dbf = new File("testdata/events.dbf");
	private final String xFieldName = "x";
	private final String yFieldName = "y";
	private final String geometryFieldName = "geom";
	protected boolean testDBFInitialized = false;

	protected void setUp() throws Exception {
		super.setUp();
		if (testDBFInitialized) {
			return;
		}
		ToolsLocator.registerDefaultPersistenceManager(XMLEntityManager.class);

		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();

		DBFLibrary dbfLib = new DBFLibrary();
		dbfLib.initialize();

		libFile.postInitialize();
		dbfLib.postInitialize();
		testDBFInitialized = true;
	}
	
	public DataStoreParameters getDefaultDataStoreParameters() throws DataException {
		DBFStoreParameters dbfParameters = null;
		dbfParameters = (DBFStoreParameters) dataManager
				.createStoreParameters(DBFStoreProvider.NAME);

		dbfParameters.setFile(dbf);

		return dbfParameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseFeatureStoreTransform#getFeatureStoreTransform()
	 */
	public FeatureStoreTransform getFeatureStoreTransform() throws DataException, ValidateDataParametersException {
		FeatureStore store = (FeatureStore) dataManager.createStore(this
				.getDefaultDataStoreParameters());
		EventThemeTransform transform = new EventThemeTransform();
		transform.initialize(store, geometryFieldName, xFieldName, yFieldName);
		return transform;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */	
	public boolean hasExplorer() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#usesResources()
	 */	
	public boolean usesResources() {
		// TODO Auto-generated method stub
		return false;
	}
}

