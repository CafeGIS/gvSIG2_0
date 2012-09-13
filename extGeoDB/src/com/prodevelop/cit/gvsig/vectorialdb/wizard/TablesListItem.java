/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JCheckBox;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorer;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;


/**
 * Utility class that represents a table list item as a selectable check box.
 *
 * @author jldominguez
 *
 */
public class TablesListItem extends JCheckBox {
    protected String tableName = "";
    private UserSelectedFieldsPanel selectedFieldsPanel = null;
    protected WizardDB parent = null;
    private boolean activated = false;
    private CRSSelectPanel jPanelProj;
	protected DBStoreParameters parameters;
	protected DBServerExplorer explorer;
	protected UserTableSettingsPanel tableSettingsPanel = null;

    public TablesListItem(DBServerExplorer explorer,
			DBStoreParameters param,
			WizardDB _parent) {
        tableName = param.getTable();
        setText(tableName);
        this.parameters = param;
        this.explorer=explorer;
        parent = _parent;
    }

    public void activate() {
        activated = true;
        selectedFieldsPanel.loadValues();
        tableSettingsPanel.loadValues();
    }

    public boolean isActivated() {
        return activated;
    }

    /**
     * Tells whether this item prevents the wizard from being in a valid final state.
     * @return whether this item prevents the wizard from being in a valid final state.
     */
    public boolean disturbsWizardValidity() {
        if (isSelected()) {
            return (!hasValidValues());
        }
        else {
            return false;
        }
    }

    private boolean hasValidValues() {
        return tableSettingsPanel.hasValidValues();
    }

    public String toString() {
        return tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setEnabledPanels(boolean b) {
        selectedFieldsPanel.enableControls(b);
        tableSettingsPanel.enableAlphaControls(b);
        tableSettingsPanel.enableSpatialControls(b);

    }

    public UserSelectedFieldsPanel getUserSelectedFieldsPanel() {
        if (selectedFieldsPanel == null) {
        	FeatureType ft=null;
			try {
				ft = explorer.getFeatureType(parameters);
			} catch (DataException e) {
				NotificationManager.addError(e);
				return null;
			}
			ArrayList<FeatureAttributeDescriptor> attList = new ArrayList<FeatureAttributeDescriptor>();

			Iterator<FeatureAttributeDescriptor> iter = ft.iterator();
			while (iter.hasNext()) {
				attList.add(iter.next());
			}

        	FeatureAttributeDescriptor[] allf = attList
					.toArray(new FeatureAttributeDescriptor[0]);


            selectedFieldsPanel = new UserSelectedFieldsPanel(allf, false,
                    parent);
        }

        return selectedFieldsPanel;
    }

    public UserTableSettingsPanel getUserTableSettingsPanel() {
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
			ArrayList auxId = new ArrayList();
			Iterator iter = ft.iterator();
			while (iter.hasNext()) {
				FeatureAttributeDescriptor dbattr = (FeatureAttributeDescriptor) iter
						.next();
				if (dbattr.isPrimaryKey()) {
					auxId.add(dbattr.getName());
				}
				auxAll.add(dbattr.getName());

			}

			if (auxId.size() > 0) {
				StringBuilder strb = new StringBuilder();
				strb.append('{');
				for (int i = 0; i < auxId.size()-1; i++) {
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



			tableSettingsPanel = new UserTableSettingsPanel(ids_ci, tableName,
					true, parent, getParameters());
		}

		return tableSettingsPanel;
	}


    public IProjection currentProjection(String espView,
			FieldComboItem[] ids_ci, FieldComboItem[] geos_ci) {
		IProjection proj = AddLayerDialog.getLastProjection();
		try {
			ArrayList list = new ArrayList(1);
			list.add(espView);
			FeatureType ft = null;
			try {
				ft = explorer.getFeatureType(parameters);
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			NotificationManager.addInfo("Incorrect projection", e);
		}
		return proj;
	}

    public DBStoreParameters getParameters() {
    	return parameters;
    }
}
