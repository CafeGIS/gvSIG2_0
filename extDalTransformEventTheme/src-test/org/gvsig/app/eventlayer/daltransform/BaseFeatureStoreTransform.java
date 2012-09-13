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

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.BaseTestFeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public abstract class BaseFeatureStoreTransform extends BaseTestFeatureStore {
	public abstract DataStoreParameters getDefaultDataStoreParameters() throws DataException;
		
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#setUp()
	 */	
	protected void setUp() throws Exception {		
		super.setUp();
		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();
		lib.initialize();
		lib.postInitialize();
	}

	public void testInitializeStore() throws Exception {
		FeatureStore store = (FeatureStore) dataManager.createStore(this
				.getDefaultDataStoreParameters());
		store.dispose();
	}
	
	public abstract FeatureStoreTransform getFeatureStoreTransform() throws DataException, ValidateDataParametersException;
	
	public void testTransform() throws Exception {
		FeatureStore store = (FeatureStore) dataManager.createStore(this
				.getDefaultDataStoreParameters());
		store.getTransforms().add(getFeatureStoreTransform());
		this.testSimpleIteration(store);		
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
		return false;
	}	
	
	
}

