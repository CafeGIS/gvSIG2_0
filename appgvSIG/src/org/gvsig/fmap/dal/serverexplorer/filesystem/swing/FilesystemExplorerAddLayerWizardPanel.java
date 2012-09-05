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

/**
 *
 */
package org.gvsig.fmap.dal.serverexplorer.filesystem.swing;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.cresques.cts.IProjection;
import org.gvsig.AppGvSigLocator;
import org.gvsig.AppGvSigManager;
import org.gvsig.PrepareContext;
import org.gvsig.PrepareContextView;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFLibrary;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.operations.SingleLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;

/**
 * @author jmvivo
 *
 */
public class FilesystemExplorerAddLayerWizardPanel extends
		FilesystemExplorerWizardPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -5054057255129168139L;
	private PrepareContext prepareContext;

	private static Logger logger = LoggerFactory
			.getLogger(FilesystemExplorerAddLayerWizardPanel.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.WizardPanel#execute()
	 */
	@Override
	public void execute() {
		if (this.getMapCtrl() == null){
			throw new IllegalArgumentException("MapControl need");
		}
		MapContext mapContext = this.getMapCtrl().getMapContext();
		FLayer layer;
		String layerName;
		LayerFactory layerFactor = LayerFactory.getInstance();

		AppGvSigManager appGvSigMan = AppGvSigLocator.getAppGvSigManager();

		for (DataStoreParameters params : this.getParameters()) {
			IProjection proj = null;

			proj = this.getMapCtrl().getProjection();

			// Buscamos por el parametro de la proyeccion
			// que sean obligatorios y estén a null
			// y le ponemos la proyeccion de la vista
			DynField[] fields = params.getDynClass().getDynFields();
			for (DynField field : fields) {
				if (field.getType() == DataTypes.SRS && field.isMandatory()) {
					if (params.getDynValue(field.getName()) == null) {
						params.setDynValue(field.getName(), proj);
					}
				}
			}




			//FIXME: el nombre deberia sugerirlo los parametros?
			layerName = ((FilesystemStoreParameters) params).getFile().getName();

			try {

				layer = layerFactor.createLayer(layerName,
						params);
			} catch (LoadLayerException e) {
				NotificationManager.addError(e);
				return;
			}
			try {
				layer = appGvSigMan.prepareOpenLayer(layer,
						(PrepareContextView) this.getPrepareDataStoreContext());
			} catch (Exception e) {
				NotificationManager.addError(e);
				if (layer instanceof SingleLayer) {
					try {
						((SingleLayer) layer).getDataStore().dispose();
					} catch (DataException e1) {
						logger
								.error(
										"Exeption disposing dataStore after prepareOpenLayer exception",
										e1);
					}
				}
				return;
			}
			if (layer == null) {
				continue;
			}

			mapContext.getLayers().addLayer(layer);
		}


	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		FilesystemStoreListModel model = (FilesystemStoreListModel) getFileList()
				.getModel();

		if (command == EDIT_COMMAND) {
			DynObject dynObject = model.getDynObjectAt(getFileList()
					.getSelectedIndex());


			DynObjectEditor editor = new DynObjectEditor(dynObject,
					DynObjectEditor.SHOW_ALL, null, true, true);
			editor.editObject(true);
			this.loadParamsList(dynObject);

		} else {
			super.actionPerformed(e);
		}
	}

	public static void main(String[] args) {
		try {
			ToolsLibrary toolsLibrary = new ToolsLibrary();

			toolsLibrary.initialize();
			toolsLibrary.postInitialize();

			DALLibrary dalLibrary = new DALLibrary();
			dalLibrary.initialize();
			dalLibrary.postInitialize();

			DALFileLibrary dalFileLibrary = new DALFileLibrary();
			dalFileLibrary.initialize();
			dalFileLibrary.postInitialize();

			DBFLibrary dbfLibrary = new DBFLibrary();
			dbfLibrary.initialize();
			dbfLibrary.postInitialize();

			SHPLibrary shpLibrary = new SHPLibrary();
			shpLibrary.initialize();
			shpLibrary.postInitialize();


			JFrame frame = new JFrame();

			AddLayerDialog addLayerDlg = new AddLayerDialog();

			FilesystemExplorerAddLayerWizardPanel wizardPanel = new FilesystemExplorerAddLayerWizardPanel();
			addLayerDlg.addWizardTab("File", wizardPanel);
			wizardPanel.initWizard();

			frame.setLayout(new BorderLayout());
			frame.add(addLayerDlg);

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBounds(10, 10, 400, 400);
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}
	}

	@Override
	protected PrepareContext getPrepareDataStoreContext() {
		if (this.prepareContext == null) {
			this.prepareContext = new PrepareContextView() {
				public Window getOwnerWindow() {
					return null;
				}

				public MapControl getMapControl() {
					return FilesystemExplorerAddLayerWizardPanel.this
							.getMapCtrl();
				}

			};
		}
		return this.prepareContext;
	}


}