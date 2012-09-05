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
package org.gvsig.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.AppGvSigManager;
import org.gvsig.PrepareContext;
import org.gvsig.PrepareContextView;
import org.gvsig.PrepareDataStore;
import org.gvsig.PrepareDataStoreParameters;
import org.gvsig.PrepareLayer;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.cit.gvsig.gui.WizardPanel;



/**
 * @author jmvivo
 *
 */
public class DefaultAppGvSigManager implements AppGvSigManager {

	private static final String EPNAME_PREPARE_OPEN_DATASTORE = "PrepareOpenDataStore";
	private static final String EPNAME_PREPARE_OPEN_DATASTOREPARAMETERS = "PrepareOpenDataStoreParameters";
	private static final String EPNAME_PREPARE_OPEN_LAYER = "PrepareOpenLayer";

	private static final String EPNAME_ADD_TABLE_WIZARD = "AddLayerWizard";

	private ExtensionPointManager epManager;



	public DefaultAppGvSigManager() {
		epManager = ToolsLocator.getExtensionPointManager();
		epManager.add(EPNAME_PREPARE_OPEN_DATASTORE,
				"Actions to do when open a DataStore");
		epManager.add(EPNAME_PREPARE_OPEN_DATASTOREPARAMETERS,
				"Actions to do before open a DataStore with parameters");
		epManager.add(EPNAME_PREPARE_OPEN_LAYER,
			"Actions to do after create a Layer");
		epManager.add(EPNAME_ADD_TABLE_WIZARD,
				"Wizards to add new document table");

	}

	/* (non-Javadoc)
	 * @see org.gvsig.appGvSigManager#pepareOpenDataSource(org.gvsig.fmap.dal.DataStore)
	 */
	public DataStore pepareOpenDataSource(DataStore store,
			PrepareContext context) throws Exception {
		ExtensionPoint ep = epManager.get(EPNAME_PREPARE_OPEN_DATASTORE);
		if (ep.getCount() == 0) {
			return store;
		}
		DataStore result = store;
		Iterator<ExtensionPoint.Extension> iter = ep.iterator();
		PrepareDataStore prepare;
		while (iter.hasNext()) {
			prepare = (PrepareDataStore) iter.next().create();
			result = prepare.prepare(store, context);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.appGvSigManager#prepareOpenDataStoreParameters(org.gvsig.fmap.dal.DataStoreParameters)
	 */
	public DataStoreParameters prepareOpenDataStoreParameters(
			DataStoreParameters storeParameters, PrepareContext context)
			throws Exception {

		ExtensionPoint ep = epManager
				.get(EPNAME_PREPARE_OPEN_DATASTOREPARAMETERS);
		if (ep.getCount() == 0) {
			return storeParameters;
		}
		DataStoreParameters result = storeParameters;
		Iterator<ExtensionPoint.Extension> iter = ep.iterator();
		PrepareDataStoreParameters prepare;
		while (iter.hasNext()) {
			prepare = (PrepareDataStoreParameters) iter.next().create();
			result = prepare.prepare(storeParameters, context);
		}

		return result;
	}

	public void registerPrepareOpenDataStore(PrepareDataStore action) {
		ExtensionPoint ep = epManager.get(EPNAME_PREPARE_OPEN_DATASTORE);
		ep.append(action.getName(), action.getDescription(), action);
	}

	public void registerPrepareOpenDataStoreParameters(
			PrepareDataStoreParameters action) {
		ExtensionPoint ep = epManager
				.get(EPNAME_PREPARE_OPEN_DATASTOREPARAMETERS);
		ep.append(action.getName(), action.getDescription(), action);


	}

	public FLayer prepareOpenLayer(FLayer layer,
			PrepareContextView context)
			throws Exception {

		ExtensionPoint ep = epManager.get(EPNAME_PREPARE_OPEN_LAYER);

		if (ep.getCount() == 0) {
			return layer;
		}
		FLayer result = layer;
		Iterator<ExtensionPoint.Extension> iter = ep.iterator();
		PrepareLayer prepare;
		while (iter.hasNext()) {
			prepare = (PrepareLayer) iter.next().create();
			result = prepare.prepare(layer, context);
		}

		return result;

	}

	public void registerPrepareOpenLayer(PrepareLayer action) {
		ExtensionPoint ep = epManager.get(EPNAME_PREPARE_OPEN_LAYER);
		ep.append(action.getName(), action.getDescription(), action);
	}

	public void registerAddTableWizard(String name, String description,
			Class<? extends WizardPanel> wpClass) {
		ExtensionPoint ep = epManager.get(EPNAME_ADD_TABLE_WIZARD);
		ep.append(name, description, wpClass);
	}

	public List<WizardPanel> getWizardPanels() throws Exception {
		ExtensionPoint ep = epManager.get(EPNAME_ADD_TABLE_WIZARD);
		List<WizardPanel> result = new ArrayList<WizardPanel>();
		Iterator<Extension> iter = ep.iterator();
		while (iter.hasNext()) {
			result.add((WizardPanel) iter.next().create());
		}
		return result;
	}
}
