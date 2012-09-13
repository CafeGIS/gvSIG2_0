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

package org.gvsig.app.join;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.app.join.dal.feature.JoinTransform;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;
import org.gvsig.project.document.table.gui.JoinWizardController;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.TableOperations;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class JoinToolExtension extends TableOperations {
	private FeatureStore featureStore = null;

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#updateUI(java.lang.String)
	 */
	public void execute(String actionCommand) {
		ProjectExtension pe = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
		com.iver.cit.gvsig.project.Project project=pe.getProject();
		FeatureTableDocument[] pts = project.getDocumentsByType(FeatureTableDocumentFactory.registerName)
		.toArray(new FeatureTableDocument[0]);

		JoinWizardController wizardController = new JoinWizardController(this);
		wizardController.runWizard(pts);
	}	

	public void execJoin(FeatureTableDocument sourceProjectTable,
			String field1, String prefix1,
			FeatureTableDocument targetProjectTable, String field2,
			String prefix2) {

		FeatureStore fs1 = sourceProjectTable.getStore();

		FeatureStore fs2 = targetProjectTable.getStore();

		if (fs1 == fs2) {
			NotificationManager
					.addInfo("no_es_posible_aplicar_join_sobre_la_misma_fuente");
		}

		try {
			DataManager dm = DALLocator.getDataManager();
			JoinTransform jt = new JoinTransform();
			ArrayList<String> fields = new ArrayList<String>();
			Iterator iterator2 = fs2.getDefaultFeatureType().iterator();
			while (iterator2.hasNext()) {
				FeatureAttributeDescriptor descriptor = (FeatureAttributeDescriptor) iterator2
						.next();
				String name = descriptor.getName();
				if (!name.equals(field2)) {
					fields.add(name);
				}
			}
			jt.initialize(fs1, fs2, field1, field2, prefix1, prefix2, fields
					.toArray(new String[0]));
			fs1.getTransforms().add(jt);
			// featureStore=fs1;

		} catch (ReadException e) {
			NotificationManager.addError("Error leyendo del driver", e);
		} catch (DataException e) {
			NotificationManager.addError(e);
		}

	}
	
	
}
