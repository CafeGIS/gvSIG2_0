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

package org.gvsig.fmap.dal.serverexplorer.filesystem.swing;

import java.awt.Window;
import java.io.File;

import org.gvsig.AppGvSigLocator;
import org.gvsig.AppGvSigManager;
import org.gvsig.PrepareContext;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;

public class FilesystemExplorerTableWizardPanel extends
		FilesystemExplorerWizardPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8469934188826417698L;

	private static Logger logger = LoggerFactory
			.getLogger(FilesystemExplorerTableWizardPanel.class);
	private PrepareContext prepareDSContext = null;

	@Override
	public void execute() {
		DataManager man = DALLocator.getDataManager();
		FeatureStore store, storeToAdd;
		File file;
		String tableName;
		FeatureTableDocument table;
		Project project = ((ProjectExtension) PluginServices
				.getExtension(ProjectExtension.class)).getProject();

		AppGvSigManager appGvSIGMan = AppGvSigLocator.getAppGvSigManager();
		PrepareContext context = this.getPrepareDataStoreContext();
		for (DataStoreParameters params : this.getParameters()) {
			file = ((FilesystemStoreParameters) params).getFile();
			tableName = file
					.getName();

			try {
				store = (FeatureStore) this.explorer.open(file);
			} catch (Exception e) {
				NotificationManager.addError(e);
				return;
			}

			try {
				storeToAdd = (FeatureStore) appGvSIGMan.pepareOpenDataSource(
						store, context);
			} catch (Exception e) {
				NotificationManager.addError(e);
				try {
					store.dispose();
				} catch (DataException e1) {
					logger
							.error(
									"Exception when disposing a store after prepareStore exception",
									e1);
				}
				return;
			}

			table = ProjectFactory.createTable(file.getName(), store);

			project.addDocument(table);


		}

	}

	@Override
	protected PrepareContext getPrepareDataStoreContext() {
		if (this.prepareDSContext == null){
			this.prepareDSContext = new PrepareContext(){
				public Window getOwnerWindow() {
					return null;
				}

			};
		}
		return this.prepareDSContext;
	}

	public String getTabName() {
		return PluginServices.getText(this, "File");
	}

}
