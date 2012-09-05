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

package org.gvsig;

import java.util.List;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.cit.gvsig.gui.WizardPanel;

public interface AppGvSigManager {

	void registerPrepareOpenDataStore(PrepareDataStore action);

	void registerPrepareOpenDataStoreParameters(
			PrepareDataStoreParameters action);

	void registerPrepareOpenLayer(PrepareLayer action);

	void registerAddTableWizard(String name, String description,
			Class<? extends WizardPanel> wpClass);

	List<WizardPanel> getWizardPanels() throws Exception;

	DataStore pepareOpenDataSource(DataStore store,
			PrepareContext context)
			throws Exception;

	DataStoreParameters prepareOpenDataStoreParameters(
			DataStoreParameters storeParameters, PrepareContext context)
			throws Exception;

	FLayer prepareOpenLayer(FLayer layer,
			PrepareContextView context)
			throws Exception;
}
