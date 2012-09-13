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

package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorer;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.messages.NotificationManager;

public class TablesListItemVectorial extends TablesListItem {
	private MapControl mc;


	public TablesListItemVectorial(DBServerExplorer explorer,
			DBStoreParameters param,
			MapControl _mc, WizardVectorialDB _parent) {
		super(explorer, param, _parent);
		mc = _mc;
	}

    public UserTableSettingsPanel getUserTableSettingsPanel(String espView) {
		if (tableSettingsPanel == null) {

			String[] ids = new String[0];
			FeatureType ft = null;

			try {
				ft = explorer.getFeatureType(parameters);
			} catch (DataException e) {
				NotificationManager.addError(e);
				return null;
			}

			ArrayList auxAll = new ArrayList();
			String[] geos = new String[0];
			ArrayList auxGeo = new ArrayList();
			ArrayList auxId = new ArrayList();
			Iterator iter = ft.iterator();
			while (iter.hasNext()) {
				FeatureAttributeDescriptor dbattr = (FeatureAttributeDescriptor) iter
						.next();
				if (dbattr.getDataType() == DataTypes.GEOMETRY) {
					auxGeo.add(dbattr.getName());
				}
				if (dbattr.isPrimaryKey()) {
					auxId.add(dbattr.getName());
				}
				auxAll.add(dbattr.getName());

			}

			geos = (String[]) auxGeo.toArray(new String[0]);
			if (auxId.size() > 0) {
				StringBuilder strb = new StringBuilder();
				strb.append('{');
				for (int i = 0; i < auxId.size() - 1; i++) {
					strb.append(((FeatureAttributeDescriptor) auxId.get(i))
							.getName());
					strb.append(',');
				}
				strb.append(auxId.get(auxId.size() - 1));

				strb.append('}');
				if (auxId.size() == 1) {
					auxAll.remove(auxId.get(0));
				}
				auxAll.add(0, strb.toString());
			}
			ids = (String[]) auxAll.toArray(new String[auxAll.size()]);
			int ids_size = ids.length;
			FieldComboItem[] ids_ci = new FieldComboItem[ids_size];

			for (int i = 0; i < ids_size; i++) {
				ids_ci[i] = new FieldComboItem(ids[i]);
			}


			int geos_size = geos.length;
			FieldComboItem[] geos_ci = new FieldComboItem[geos_size];

			for (int i = 0; i < geos_size; i++) {
				geos_ci[i] = new FieldComboItem(geos[i]);
			}

			tableSettingsPanel = new UserTableSettingsVectorialPanel(ids_ci,
					geos_ci,
					tableName, mc, false, (WizardVectorialDB) parent,
					getParameters(),
					currentProjection(espView,
							ids_ci, geos_ci));
		}

		return tableSettingsPanel;
	}

    public void setEnabledPanels(boolean b) {
    	super.setEnabledPanels(b);
		tableSettingsPanel.enableSpatialControls(b);
		tableSettingsPanel.enableAlphaControls(b);
	}

}
