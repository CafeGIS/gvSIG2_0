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

import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.AppGvSigLocator;
import org.gvsig.AppGvSigManager;
import org.gvsig.PrepareContext;
import org.gvsig.PrepareContextView;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorer;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.SingleVectorialDBConnectionExtension;
import com.iver.cit.gvsig.project.documents.view.gui.View;


/**
 * Driver-independent GeoDB wizard. Queries the drivers to fill GUI controls.
 * Multi-table selection available.
 *
 * @author jldominguez
 *
 */
public class WizardVectorialDB extends WizardDB {
    private static Logger logger = LoggerFactory
			.getLogger(WizardVectorialDB.class.getName());

    private static final String GEODB_WIZARD_TAB_NAME = "GeoDB";
	private View view = null;

	private PrepareContextView prepareContext;

    /**
     * This method initializes this
     *
     * @return void
     */
    protected void initialize() {
		super.initialize();
        setTabName(GEODB_WIZARD_TAB_NAME);
        setLayout(null);
        setSize(512, 478);

        IWindow iw = PluginServices.getMDIManager().getActiveWindow();

        if (iw == null) {
            return;
        }

        if ((iw instanceof View)) {
        	 view = (View) iw;
             setMapCtrl(view.getMapControl());
        }
    }


	protected TablesListItem createTabeListItem(DBServerExplorer dbExplorer,
			DBStoreParameters param) {
		return new TablesListItemVectorial(dbExplorer, param, getMapCtrl(),
				this);
	}

	public void setSettingsPanels(TablesListItem actTable) {
		super.setSettingsPanels(actTable);
	}

	protected UserTableSettingsPanel createSettingsPanel(
			TablesListItem actTable) {
		if (actTable == null) {
			return new UserTableSettingsVectorialPanel(null, null, "",
					getMapCtrl(), true, this, null, null);
		}
		String abrev = null;
		if (getMapCtrl() != null) {
			abrev = getMapCtrl().getViewPort().getProjection().getAbrev();
		}

		return ((TablesListItemVectorial) actTable)
				.getUserTableSettingsPanel(abrev);
	}


	public DataStoreParameters[] getParameters() {
		try {
			TablesListItem[] selected = getSelectedTables();
			int count = selected.length;
			DBStoreParameters[] dbParameters = new DBStoreParameters[count];
			String strEPSG = null;
			if (getMapCtrl() != null) {
				strEPSG = getMapCtrl().getViewPort().getProjection().getAbrev();
			}

			for (int i = 0; i < count; i++) {
				TablesListItemVectorial item = (TablesListItemVectorial) selected[i];

				dbParameters[i] = getParameterForTable(item);
			}

			return dbParameters;// layerArrayToGroup(all_layers, groupName);
		} catch (Exception e) {
			logger.error("While creating jdbc layer: " + e.getMessage(), e);
			NotificationManager.addError("Error al cargar la capa: "
					+ e.getMessage(), e);
		}

		return null;
	}

	public void execute() {
		SingleVectorialDBConnectionExtension.saveAllToPersistence();

		MapControl mapControl = this.getMapCtrl();
		IProjection proj = null;
		TablesListItem[] tables = getSelectedTables();


		List all_layers = new ArrayList();
		String strEPSG = mapControl.getViewPort().getProjection().getAbrev();
		LayerFactory layerFactory = LayerFactory.getInstance();
		String groupName = null;
		Envelope env = null;
		DBStoreParameters parameter;
		TablesListItem table;
		FeatureStore store, storeToAdd;

		DataManager man = DALLocator.getDataManager();

		AppGvSigManager appGvSIGMan = AppGvSigLocator.getAppGvSigManager();
		PrepareContext context = this.getPrepareDataStoreContext();


		FLayer layer;
		for (int i = 0; i < tables.length; i++) {
			table = tables[i];
			UserTableSettingsVectorialPanel userTableSettingsPanel = (UserTableSettingsVectorialPanel) table
					.getUserTableSettingsPanel();
			parameter = getParameterForTable(table);
			if (i == 0) {
				groupName = parameter.getDBName() + " (" + parameter.getHost()
						+ ")";
			}
			try {
				parameter = (DBStoreParameters) appGvSIGMan
						.prepareOpenDataStoreParameters(parameter, context);

			} catch (Exception e2) {
				NotificationManager.addError(e2);
				continue;
			}

			try {
				store = (FeatureStore) man.createStore(parameter);
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
				continue;
			}

			try {

				layer = layerFactory.createLayer(userTableSettingsPanel
						.getUserLayerName(), storeToAdd);
				all_layers.add(layer);
				if (env == null) {
					env = layer.getFullEnvelope();
				} else {
					env.add(layer.getFullEnvelope());
				}
			} catch (Exception e) {
				try {
					storeToAdd.dispose();
				} catch (DataException e1) {
					logger
							.error(
									"Exception when disposing a store after createLayer exception",
									e1);
				}

				NotificationManager.addError(e);
			}
		}

		MapContext mc = mapControl.getMapContext();
		FLayers root = mapControl.getMapContext().getLayers();
		if (all_layers.size() > 1) {
			FLayers group = new FLayers();// (mc,root);
			group.setMapContext(mc);
			group.setParentLayer(root);
			group.setName(groupName);

			Iterator iter = all_layers.iterator();
			while (iter.hasNext()) {
				group.addLayer((FLayer) iter.next());
			}

			if ((group != null) && !(group.isOk())) {
				// if the layer is not okay (it has errors) process them
				processErrorsOfLayer(group, mapControl);
			}

			if (group != null) {
				group.setVisible(true);
				mapControl.getMapContext().beginAtomicEvent();
				try {
					// checkProjection(group, mapControl.getViewPort());
					try {
						mapControl.getMapContext().getLayers().addLayer(group);
					} catch (CancelationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (mapControl.getViewPort().getExtent() == null) {
						mapControl.getViewPort().setEnvelope(env);
					}
				} finally {
					mapControl.getMapContext().endAtomicEvent();
				}
				return;
			}
		} else if (all_layers.size() == 1) {
			layer = (FLayer) all_layers.get(0);
			if (!(layer.isOk())) {
				// if the layer is not okay (it has errors) process them
				processErrorsOfLayer(layer, mapControl);
			}

			layer.setVisible(true);
			mapControl.getMapContext().beginAtomicEvent();
			// checkProjection(all_layers[0], mapControl.getViewPort());
			try {
				try {
					mapControl.getMapContext().getLayers().addLayer(layer);
				} catch (CancelationException e) {
					return;
				}

				if (mapControl.getViewPort().getExtent() == null) {
					try {
						mapControl.getViewPort().setEnvelope(
								layer.getFullEnvelope());
					} catch (ReadException e) {
						NotificationManager.addError(e);
						return;
					}
				}
			} finally {

				mapControl.getMapContext().endAtomicEvent();
			}
			return;
		}
	}

	protected PrepareContext getPrepareDataStoreContext() {
		if (this.prepareContext == null) {
			this.prepareContext = new PrepareContextView() {
				public Window getOwnerWindow() {
					return null;
				}

				public MapControl getMapControl() {
					return WizardVectorialDB.this
							.getMapCtrl();
				}

			};
		}
		return this.prepareContext;
	}

	protected DBStoreParameters getParameterForTable(TablesListItem table) {
		DBStoreParameters parameters = table.getParameters();

		String strEPSG = null;
		if (getMapCtrl() != null) {
			strEPSG = getMapCtrl().getViewPort().getProjection().getAbrev();
		}

		UserTableSettingsVectorialPanel userTableSettingsPanel = (UserTableSettingsVectorialPanel) table
				.getUserTableSettingsPanel();

		Envelope _wa = userTableSettingsPanel.getWorkingArea();

		String geomField = userTableSettingsPanel.getGeoFieldName();
		String fidField = userTableSettingsPanel.getIdFieldName();
		if (!(fidField.startsWith("{") && fidField.endsWith("}"))) {
			parameters.setPkFields(new String[] { fidField });
			fidField = null;
		}
		String[] fields = table.getUserSelectedFieldsPanel()
				.getUserSelectedFields(fidField, geomField);

		if (userTableSettingsPanel.isSqlActive()) {
			String whereClause = userTableSettingsPanel.getWhereClause();
			parameters.setInitialFilter(whereClause);
		} else {
			parameters.setInitialFilter("");
		}

		parameters.setFields(fields);
		parameters.setDefaultGeometry(geomField);
		if (_wa != null) {
			parameters.setWorkingArea(_wa);
		}

		parameters.setSRSID(strEPSG);

		return parameters;

	}


}