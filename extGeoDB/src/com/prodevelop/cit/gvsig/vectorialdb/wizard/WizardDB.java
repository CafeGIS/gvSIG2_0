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

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.AppGvSigLocator;
import org.gvsig.AppGvSigManager;
import org.gvsig.PrepareContext;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorerParameters;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.SingleVectorialDBConnectionExtension;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;

public class WizardDB extends WizardPanel implements ActionListener,
		ListSelectionListener {

	private static final String WIZARD_TAB_NAME = "DB";
	private static Logger logger = LoggerFactory.getLogger(WizardDB.class
			.getName());

    private JPanel namePanel = null;
	private JPanel tablesPanel = null;
	private JScrollPane tablesScrollPane = null;
	private AvailableTablesCheckBoxList tablesList = null;
	private JComboBox datasourceComboBox = null;
	private UserSelectedFieldsPanel fieldsPanel = null;
	private UserSelectedFieldsPanel emptyFieldsPanel = null;
	private JButton dbButton = null;
	private DBServerExplorerParameters dbExplorerParameters;

    private UserTableSettingsPanel settingsPanel = null;
	protected UserTableSettingsPanel emptySettingsPanel = null;
	private PrepareContext prepareDSContext;

    public WizardDB() {
		super();
		initialize();
	}


	protected void initialize() {
		setTabName(WIZARD_TAB_NAME);
		setLayout(null);
		setSize(512, 478);



		emptyFieldsPanel = new UserSelectedFieldsPanel(null, true, this);
		add(emptyFieldsPanel);

        add(getNamePanel(), null);
		loadVectorialDBDatasourcesCombo(null);

		add(getTablesPanel(), null);

        emptySettingsPanel = createSettingsPanel(null);
        add(emptySettingsPanel);

	}


    private void loadVectorialDBDatasourcesCombo(MyExplorer sel) {
		getDatasourceComboBox().removeAllItems();
		DataManager dm = DALLocator.getDataManager();
		List names = dm.getStoreProviders();
		if (sel != null) {
			getDatasourceComboBox().addItem(sel);
		}

		if (names == null) {
			return;
		}

	}

	public void initWizard() {
	}

	public void execute() {
		SingleVectorialDBConnectionExtension.saveAllToPersistence();

		TablesListItem[] tables = getSelectedTables();

		DataManager man = DALLocator.getDataManager();
		FeatureStore store, storeToAdd;

		String docName;
		FeatureTableDocument document;
		Project project = ((ProjectExtension) PluginServices
				.getExtension(ProjectExtension.class)).getProject();

		AppGvSigManager appGvSIGMan = AppGvSigLocator.getAppGvSigManager();
		PrepareContext context = this.getPrepareDataStoreContext();
		DBStoreParameters storeParams;
		for (TablesListItem table : tables) {
			storeParams = getParameterForTable(table);

			try {
				storeParams = (DBStoreParameters) appGvSIGMan
						.prepareOpenDataStoreParameters(storeParams, context);
			} catch (Exception e2) {
				NotificationManager.addError(e2);
				continue;
			}

			UserTableSettingsPanel userTableSettingsPanel = table
					.getUserTableSettingsPanel();

			docName = userTableSettingsPanel.getUserLayerName();
			try {
				store = (FeatureStore) man.createStore(storeParams);
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

			document = ProjectFactory.createTable(docName, store);

			project.addDocument(document);

		}

	}

	protected DBStoreParameters getParameterForTable(TablesListItem table) {
		DBStoreParameters parameters = table.getParameters();

		UserTableSettingsPanel userTableSettingsPanel = table
				.getUserTableSettingsPanel();



		String fidField = userTableSettingsPanel.getIdFieldName();
		if (!(fidField.startsWith("{") && fidField.endsWith("}"))) {
			parameters.setPkFields(new String[] { fidField });
			fidField = null;
		}
		String[] fields = table.getUserSelectedFieldsPanel()
				.getUserSelectedFields(fidField, null);

		if (userTableSettingsPanel.isSqlActive()) {
			String whereClause = userTableSettingsPanel
					.getWhereClause();
			parameters.setInitialFilter(whereClause);
		} else {
			parameters.setInitialFilter("");
		}

		parameters.setFields(fields);


		return parameters;

	}

	protected TablesListItem[] getSelectedTables() {
		int count = tablesList.getModel().getSize();
		ArrayList resp = new ArrayList();

		for (int i = 0; i < count; i++) {
			TablesListItem item = (TablesListItem) tablesList.getModel()
					.getElementAt(i);

			if (item.isSelected()) {
				resp.add(item);
			}
		}

		return (TablesListItem[]) resp.toArray(new TablesListItem[0]);
	}

	/**
	 * This method initializes namePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getNamePanel() {
		if (namePanel == null) {
			namePanel = new JPanel();
			namePanel.setLayout(null);
			namePanel.setBounds(new java.awt.Rectangle(5, 5, 501, 51));
			namePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "choose_connection"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			namePanel.add(getDatasourceComboBox(), null);
			namePanel.add(getJdbcButton(), null);
		}

		return namePanel;
	}

	/**
	 * This method initializes tablesPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getTablesPanel() {
		if (tablesPanel == null) {
			tablesPanel = new JPanel();
			tablesPanel.setLayout(new BorderLayout());
			tablesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "choose_table"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			tablesPanel.setBounds(new java.awt.Rectangle(5, 55, 246, 166));
			tablesPanel
					.add(getTablesScrollPane(), java.awt.BorderLayout.CENTER);
		}

		return tablesPanel;
	}

	/**
	 * This method initializes settingsPanel
	 *
	 * @return javax.swing.JPanel
	 */

	/**
	 * This method initializes tablesScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getTablesScrollPane() {
		if (tablesScrollPane == null) {
			tablesScrollPane = new JScrollPane();
			tablesScrollPane.setViewportView(getTablesList());
		}

		return tablesScrollPane;
	}

	/**
	 * This method initializes tablesList
	 *
	 * @return javax.swing.JList
	 */
	protected AvailableTablesCheckBoxList getTablesList() {
		if (tablesList == null) {
			tablesList = new AvailableTablesCheckBoxList(this);
			tablesList.addListSelectionListener(this);
			tablesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		return tablesList;
	}

	/**
	 * This method initializes layerNameTextField
	 *
	 * @return javax.swing.JTextField
	 */

	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDatasourceComboBox() {
		if (datasourceComboBox == null) {
			datasourceComboBox = new JComboBox();
			datasourceComboBox
					.setBounds(new java.awt.Rectangle(10, 20, 446, 21));
			datasourceComboBox.addActionListener(this);
		}

		return datasourceComboBox;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (datasourceComboBox.getItemCount() == 0) {
			setEmptyPanels();
		}
		Object src = arg0.getSource();

		if (src == datasourceComboBox) {
			Object sel_obj = datasourceComboBox.getSelectedItem();

			if (sel_obj == null) {
				return;
			}
			getDatasourceComboBox().repaint();
			return;
		}

		if (src == dbButton) {
			MyExplorer sel = addNewConnection();

			if (sel != null) {
				dbExplorerParameters = sel.getDbSeverExplorerParameters();
				loadVectorialDBDatasourcesCombo(sel);
				getDatasourceComboBox().setSelectedItem(sel);

			}
			updateTableList(dbExplorerParameters);
		}
	}

	private MyExplorer addNewConnection() {
		MyExplorer myExplorer = new MyExplorer();
		DBServerExplorerParameters resp = null;

		VectorialDBConnectionParamsDialog newco = new VectorialDBConnectionParamsDialog();
		newco.showDialog();

		if (newco.isOkPressed()) {
			try {
				resp = newco.getParameters();
			} catch (Exception e) {
				showConnectionErrorMessage(e.getMessage());
				return null;
			}
			SingleVectorialDBConnectionExtension.saveAllToPersistence();
			myExplorer.setDbExplorerParameters(resp);
			myExplorer.setName(newco.getConnectionName());
			return myExplorer;
		} else {
			return null;
		}
	}

	protected TablesListItem createTabeListItem(DBServerExplorer dbExplorer,
			DBStoreParameters param) {
		return new TablesListItem(dbExplorer, param, this);
	}

	private void updateTableList(
			DBServerExplorerParameters dbSeverExplorerParameters2) {
		if (dbSeverExplorerParameters2 == null) {
			return;
		}
		DataManager dm = DALLocator.getDataManager();
		DBServerExplorer dbExplorer;
		try {
			dbExplorer = (DBServerExplorer) dm
					.createServerExplorer(dbSeverExplorerParameters2);
			List parameters = dbExplorer.list();

			DefaultListModel lmodel = new DefaultListModel();

			Iterator iter = parameters.iterator();
			DBStoreParameters param;
			while (iter.hasNext()) {
				param = (DBStoreParameters) iter.next();
				lmodel.addElement(createTabeListItem(dbExplorer, param));
			}

			getTablesList().setModel(lmodel);
			getTablesScrollPane().setViewportView(tablesList);
			tablesScrollPane.updateUI();
		} catch (InitializeException e) {
			logger.error("While getting table names: " + e.getMessage(), e);
			NotificationManager.showMessageError("While getting table names: "
					+ e.getMessage(), e);
			return;
		} catch (DataException e) {
			logger.error("While getting table names: " + e.getMessage(), e);
			NotificationManager.showMessageError("While getting table names: "
					+ e.getMessage(), e);
			return;
		} catch (ValidateDataParametersException e) {
			logger.error("While getting table names: " + e.getMessage(), e);
			NotificationManager.showMessageError("While getting table names: "
					+ e.getMessage(), e);
			return;
		}
	}

	public void valueChanged(ListSelectionEvent arg0) {
		Object src = arg0.getSource();

		if (src == tablesList) {
			TablesListItem selected = (TablesListItem) tablesList
					.getSelectedValue();

			setSettingsPanels(selected);
			checkFinishable();
		}
	}

	private boolean validFormSettings() {
		int count = tablesList.getModel().getSize();

		boolean at_least_one = false;
		boolean resp = true;

		for (int i = 0; i < count; i++) {
			TablesListItem item = (TablesListItem) tablesList.getModel()
					.getElementAt(i);

			if (item.isSelected()) {
				at_least_one = true;
			}

			if (item.disturbsWizardValidity()) {
				resp = false;
			}
		}

		return (at_least_one && resp);
	}

	public void checkFinishable() {
		boolean finishable = validFormSettings();
		callStateChanged(finishable);
	}

	/**
	 * This method initializes jdbcButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJdbcButton() {
		if (dbButton == null) {
			dbButton = new JButton();
			dbButton.addActionListener(this);
			dbButton.setToolTipText(PluginServices.getText(this,
					"add_connection"));
			dbButton.setBounds(new java.awt.Rectangle(465, 20, 26, 21));

			// FIXME use standard icon theme
			String _file = createResourceUrl("images/jdbc.png").getFile();
			dbButton.setIcon(new ImageIcon(_file));
		}

		return dbButton;
	}

	private void showConnectionErrorMessage(String _msg) {
		String msg = (_msg.length() > 300) ? "" : (": " + _msg);
		String title = PluginServices.getText(this, "connection_error");
		JOptionPane.showMessageDialog(this, title + msg, title,
				JOptionPane.ERROR_MESSAGE);
	}

	private java.net.URL createResourceUrl(String path) {
		return getClass().getClassLoader().getResource(path);
	}

	public void setSettingsPanels(TablesListItem actTable) {
		if (actTable == null) {
			setEmptyPanels();

			return;
		}
		fieldsPanel = actTable.getUserSelectedFieldsPanel();

		removeFieldPanels();
		add(fieldsPanel);
		fieldsPanel.repaint();
		removeSettingsPanels();
		add(emptySettingsPanel);

		settingsPanel = createSettingsPanel(actTable);

		removeSettingsPanels();
		add(settingsPanel);
		settingsPanel.repaint();


		repaint();
	}



	protected UserTableSettingsPanel createSettingsPanel(TablesListItem actTable) {
		if (actTable == null) {
			return new UserTableSettingsPanel(null, "", true, this, null);
		}

		return actTable.getUserTableSettingsPanel();
	}

	protected void setEmptyPanels() {
		removeFieldPanels();
		add(emptyFieldsPanel);
		fieldsPanel = emptyFieldsPanel;

		repaint();
	}

	private void removeFieldPanels() {
		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i) instanceof UserSelectedFieldsPanel) {
				remove(i);
			}
		}
	}

	public DataStoreParameters[] getParameters() {
		try {
			TablesListItem[] selected = getSelectedTables();
			int count = selected.length;
			DBStoreParameters[] dbParameters = new DBStoreParameters[count];

			for (int i = 0; i < count; i++) {
				TablesListItem item = selected[i];


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

	/**
	 * This method process the errors found in a layer
	 *
	 * @param lyr
	 * @param mapControl
	 */

	protected void processErrorsOfLayer(FLayer lyr, MapControl mapControl) {
		// List errors = lyr.getErrors();
		// wp.callError(null);
		mapControl.getMapContext().callNewErrorEvent(null);
	}

	private void removeSettingsPanels() {
		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i) instanceof UserTableSettingsPanel) {
				remove(i);
			}
		}
	}

	protected PrepareContext getPrepareDataStoreContext() {
		if (this.prepareDSContext == null) {
			this.prepareDSContext = new PrepareContext() {
				public Window getOwnerWindow() {
					return null;
				}

			};
		}
		return this.prepareDSContext;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
