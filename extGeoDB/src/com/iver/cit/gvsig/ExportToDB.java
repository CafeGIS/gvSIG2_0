package com.iver.cit.gvsig;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.fmap.dal.store.jdbc.JDBCServerExplorer;
import org.gvsig.fmap.dal.store.jdbc.JDBCServerExplorerParameters;
import org.gvsig.fmap.dal.store.mysql.MySQLServerExplorer;
import org.gvsig.fmap.dal.store.postgresql.PostgreSQLServerExplorer;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.vectorialdb.ConnectionSettings;
import com.iver.cit.gvsig.vectorialdb.DlgConnection;
import com.iver.utiles.PostProcessSupport;

public class ExportToDB extends Extension {
	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();

		if (f instanceof View) {
			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			FLayers layers = mapa.getLayers();
			FLayer[] actives = layers.getActives();
			try {
				// NOTA: SI HAY UNA SELECCIÓN, SOLO SE SALVAN LOS SELECCIONADOS
				for (int i = 0; i < actives.length; i++) {
					if (actives[i] instanceof FLyrVect) {
						FLyrVect lv = (FLyrVect) actives[i];
//						saveToDXF(mapa,lv);



						/////////

						long numSelec = ((FeatureSelection)lv.getFeatureStore().getSelection()).getSize();
						if (numSelec > 0) {
							int resp = JOptionPane.showConfirmDialog(
									(Component) PluginServices.getMainFrame(),
									PluginServices.getText(this,"se_van_a_guardar_") + numSelec
											+ PluginServices.getText(this,"features_desea_continuar"),
									PluginServices.getText(this,"export_to"), JOptionPane.YES_NO_OPTION);
							if (resp != JOptionPane.YES_OPTION) {
								continue;
							}
						} // if numSelec > 0
						// if (actionCommand.equals("SHP")) {
						// saveToSHP(mapa, lv);
						// }
						// if (actionCommand.equals("DXF")) {
						// saveToDXF(mapa, lv);
						// }
						if (actionCommand.equals("POSTGIS")) {
							saveToJDBC(mapa, lv, PostgreSQLServerExplorer.NAME);
						}
						if (actionCommand.equals("MYSQL")) {
							saveToJDBC(mapa, lv, MySQLServerExplorer.NAME);
						}
//						if (actionCommand.equals("GML")) {
//							saveToGml(mapa, lv);
//						}
					} // actives[i]
				} // for
			} catch (Exception e) {
				NotificationManager.showMessageError(e.getMessage(),e);
			}
		}
	}

	public void saveToJDBC(MapContext mapContext, FLyrVect layer,
			String explorerName) {
		try {
			String tableName = JOptionPane.showInputDialog(PluginServices
					.getText(this, "intro_tablename"));
			if (tableName == null) {
				return;
			}
			FeatureAttributeDescriptor[] pks = layer.getFeatureStore()
					.getDefaultFeatureType()
					.getPrimaryKey();
			String pkName = null;
			if (pks == null || pks.length < 1) {
				pkName = JOptionPane.showInputDialog(PluginServices.getText(this,"input_pk_field_name"));
			}

			tableName = tableName.toLowerCase();
			DlgConnection dlg = new DlgConnection();
			dlg.setModal(true);
			dlg.setVisible(true);
			ConnectionSettings cs = dlg.getConnSettings();
			if (cs == null) {
				return;
			}

			DataManager datamanager=DALLocator.getDataManager();

			JDBCServerExplorerParameters explorerParam = (JDBCServerExplorerParameters) datamanager
					.createServerExplorerParameters(explorerName);

			explorerParam.setHost(cs.getHost());
			if (cs.getPort() != null && cs.getPort().length() > 0) {
				explorerParam.setPort(Integer.valueOf(cs.getPort()));
			}
			explorerParam.setDBName(cs.getDb());
			explorerParam.setUser(cs.getUser());
			explorerParam.setPassword(cs.getPassw());
			if (cs.getSchema() != null && cs.getSchema().length() > 0) {
				explorerParam.setSchema(cs.getSchema());
			}

			JDBCServerExplorer explorer = (JDBCServerExplorer) datamanager
					.createServerExplorer(explorerParam);

			NewFeatureStoreParameters newStoreParams = (NewFeatureStoreParameters) explorer
					.getAddParameters();

			((DBStoreParameters) newStoreParams).setTable(tableName);

			if (export(explorer, newStoreParams, null, layer.getFeatureStore(),pkName)) {
				openLayer(newStoreParams, tableName, mapContext);
			}



		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (ValidateDataParametersException e) {
			NotificationManager.addError(e.getMessage(), e);
		}

	}



	class GeometryTypeEvaluator extends AbstractEvaluator{
		private FeatureType featureType=null;
		private int type=0;
		private FeatureSelection selection;
		private boolean hasSelection=false;

		public GeometryTypeEvaluator(FeatureType ft, int t, FeatureSelection selection) {
			featureType=ft;
			type=t;
			this.selection=selection;
			hasSelection=selection.getSelectedCount()>0;
			this.getFieldsInfo().addFieldValue(
					ft.getDefaultGeometryAttributeName());
		}
		public Object evaluate(EvaluatorData data) throws EvaluatorException {
			if (hasSelection){
				if (!selection.isSelected((Feature)data)){
					return false;
				}
			}
			Geometry geometry=(Geometry) data.getDataValue(featureType.getDefaultGeometryAttributeName());
			return new Boolean(geometry.getType()==type);
		}

		public String getName() {
			return "GeometryTypeEvaluator";
		}

	}



	private void openLayer(NewFeatureStoreParameters newParams, String name, MapContext mapContext) throws ReadException {
		int res = JOptionPane.showConfirmDialog(
				(JComponent) PluginServices.getMDIManager().getActiveWindow()
				, PluginServices.getText(this, "insertar_en_la_vista_la_capa_creada"),
				PluginServices.getText(this,"insertar_capa"),
				JOptionPane.YES_NO_OPTION);

		if (res == JOptionPane.YES_OPTION){
			PostProcessSupport.executeCalls();
			LayerFactory layerFactory=LayerFactory.getInstance();
			try {
				FLayer newLayer = layerFactory.getInstance().createLayer(
						name,newParams);

				mapContext.getLayers().addLayer(newLayer);
			} catch (LoadLayerException e) {
				throw new ReadException("Load layer",e);
			}
		}

	}

	private boolean export(JDBCServerExplorer explorer,
			NewFeatureStoreParameters params, FeatureSet set,
			FeatureStore featureStore, String pkName) {
		DisposableIterator it1 = null;
		try {
			EditableFeatureType fType = featureStore.getDefaultFeatureType()
					.getEditable();
			if (pkName != null) {
				EditableFeatureAttributeDescriptor pk = fType.add(pkName,
						DataTypes.LONG);
				pk.setIsPrimaryKey(true);
				pk.setIsAutomatic(true);
			}

			params.setDefaultFeatureType(fType);
			explorer.add(params, true);

			DataManager manager = DALLocator.getDataManager();
			FeatureStore target = (FeatureStore) manager
					.createStore(params);
			FeatureType targetType = target.getDefaultFeatureType();

			target.edit(FeatureStore.MODE_APPEND);
			if (set == null) {
				set = featureStore.getFeatureSet();
			}
			it1 = set.iterator();
			while (it1.hasNext()) {
				Feature feature = (Feature) it1.next();
				target.insert(target.createNewFeature(targetType, feature));
			}
			set.dispose();
			target.finishEditing();
			target.dispose();
			return true;
		} catch (Exception e) {
			NotificationManager.showMessageError(e.getLocalizedMessage(),e);
			return false;
		} finally {
			if (it1 != null) {
				it1.dispose();
			}
		}

	}
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (!(f instanceof View)) {
			return false;
		}

		FLayers layers = ((View) f).getMapControl().getMapContext().getLayers();
		FLayer[] actives = layers.getActives();
		if (actives.length != 1) {
			return false;
		}
		if (!(actives[0] instanceof FLyrVect)) {
			return false;
		}
		return true;

	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof View) {
			return true;
		}
		return false;
	}

//	private int findFileByName(FieldDescription[] fields, String fieldName){
//        for (int i=0; i < fields.length; i++)
//        {
//        	FieldDescription f = fields[i];
//        	if (f.getFieldName().equalsIgnoreCase(fieldName))
//        	{
//        		return i;
//        	}
//        }
//
//		return -1;
//
//	}

}
