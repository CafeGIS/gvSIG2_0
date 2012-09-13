package com.iver.cit.gvsig;

import java.awt.Component;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.store.dxf.DXFStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPNewStoreParameters;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.PostProcessSupport;
import com.iver.utiles.SimpleFileFilter;

public class ExportTo extends Extension {
	private String lastPath = null;
//	private class WriterTask extends AbstractMonitorableTask
//	{
//		FLyrVect lyrVect;
//		IWriter writer;
//		int rowCount;
//		ReadableVectorial va;
//		SelectableDataSource sds;
//		FBitSet bitSet;
//		MapContext mapContext;
//		VectorialDriver reader;
//
//		public WriterTask(MapContext mapContext, FLyrVect lyr, IWriter writer, Driver reader) throws ReadDriverException
//		{
//			this.mapContext = mapContext;
//			this.lyrVect = lyr;
//			this.writer = writer;
//			this.reader = (VectorialDriver) reader;
//
//			setInitialStep(0);
//			setDeterminatedProcess(true);
//			setStatusMessage(PluginServices.getText(this, "exportando_features"));
//
//			va = lyrVect.getSource();
//			sds = lyrVect.getRecordset();
//
//			bitSet = sds.getSelection();
//
//			if (bitSet.cardinality() == 0)
//				rowCount = va.getShapeCount();
//			else
//				rowCount = bitSet.cardinality();
//
//			setFinalStep(rowCount);
//
//		}
//		public void run() throws Exception {
//			va.start();
//			ICoordTrans ct = lyrVect.getCoordTrans();
//			DriverAttributes attr = va.getDriverAttributes();
//			boolean bMustClone = false;
//			if (attr != null) {
//				if (attr.isLoadedInMemory()) {
//					bMustClone = attr.isLoadedInMemory();
//				}
//			}
//			if (lyrVect instanceof FLyrAnnotation && lyrVect.getShapeType()!=FShape.POINT) {
//				SHPLayerDefinition lyrDef=(SHPLayerDefinition)writer.getTableDefinition();
//				lyrDef.setShapeType(FShape.POINT);
//				writer.initialize(lyrDef);
//			}
//
//			// Creamos la tabla.
//			writer.preProcess();
//
//			if (bitSet.cardinality() == 0) {
//				rowCount = va.getShapeCount();
//				for (int i = 0; i < rowCount; i++) {
//					IGeometry geom = va.getShape(i);
//					if (geom == null) {
//						continue;
//					}
//					if (lyrVect instanceof FLyrAnnotation && geom.getGeometryType()!=FShape.POINT) {
//						Point2D p=FLabel.createLabelPoint((FShape)geom.getInternalShape());
//						geom=ShapeFactory.createPoint2D(p.getX(),p.getY());
//					}
//					if (ct != null) {
//						if (bMustClone)
//							geom = geom.cloneGeometry();
//						geom.reProject(ct);
//					}
//					reportStep();
//					setNote(PluginServices.getText(this, "exporting_") + i);
//					if (isCanceled())
//						break;
//
//					if (geom != null) {
//						Value[] values = sds.getRow(i);
//						IFeature feat = new DefaultFeature(geom, values, "" + i);
//						DefaultRowEdited edRow = new DefaultRowEdited(feat,
//								DefaultRowEdited.STATUS_ADDED, i);
//						writer.process(edRow);
//					}
//				}
//			} else {
//				int counter = 0;
//				for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet
//						.nextSetBit(i + 1)) {
//					IGeometry geom = va.getShape(i);
//					if (geom == null) {
//						continue;
//					}
//					if (lyrVect instanceof FLyrAnnotation && geom.getGeometryType()!=FShape.POINT) {
//						Point2D p=FLabel.createLabelPoint((FShape)geom.getInternalShape());
//						geom=ShapeFactory.createPoint2D(p.getX(),p.getY());
//					}
//					if (ct != null) {
//						if (bMustClone)
//							geom = geom.cloneGeometry();
//						geom.reProject(ct);
//					}
//					reportStep();
//					setNote(PluginServices.getText(this, "exporting_") + counter);
//					if (isCanceled())
//						break;
//
//					if (geom != null) {
//						Value[] values = sds.getRow(i);
//						IFeature feat = new DefaultFeature(geom, values, "" + i);
//						DefaultRowEdited edRow = new DefaultRowEdited(feat,
//								DefaultRowEdited.STATUS_ADDED, i);
//
//						writer.process(edRow);
//					}
//				}
//
//			}
//
//			writer.postProcess();
//			va.stop();
//			if (reader != null){
//				int res = JOptionPane.showConfirmDialog(
//					(JComponent) PluginServices.getMDIManager().getActiveWindow()
//					, PluginServices.getText(this, "insertar_en_la_vista_la_capa_creada"),
//					PluginServices.getText(this,"insertar_capa"),
//					JOptionPane.YES_NO_OPTION);
//
//				if (res == JOptionPane.YES_OPTION)
//				{
//					PostProcessSupport.executeCalls();
//					ILayerDefinition lyrDef = (ILayerDefinition) writer.getTableDefinition();
//					FLayer newLayer = LayerFactory.createLayer(
//							lyrDef.getName(), reader, mapContext.getProjection());
//					mapContext.getLayers().addLayer(newLayer);
//				}
//			}
//
//		}
//		/* (non-Javadoc)
//		 * @see com.iver.utiles.swing.threads.IMonitorableTask#finished()
//		 */
//		public void finished() {
//			// TODO Auto-generated method stub
//
//		}
//
//	}
//	private class MultiWriterTask extends AbstractMonitorableTask{
//		Vector tasks=new Vector();
//		public void addTask(WriterTask wt) {
//			tasks.add(wt);
//		}
//		public void run() throws Exception {
//			for (int i = 0; i < tasks.size(); i++) {
//				((WriterTask)tasks.get(i)).run();
//			}
//			tasks.clear();
//		}
//		/* (non-Javadoc)
//		 * @see com.iver.utiles.swing.threads.IMonitorableTask#finished()
//		 */
//		public void finished() {
//			// TODO Auto-generated method stub
//
//		}
//
//
//	}
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
						if (actionCommand.equals("SHP")) {
							saveToSHP(mapa, lv);
						}
						if (actionCommand.equals("DXF")) {
							saveToDXF(mapa, lv);
						}
//						if (actionCommand.equals("POSTGIS")) {
//							saveToPostGIS(mapa, lv);
//						}
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

//	public void saveToPostGIS(MapContext mapContext, FLyrVect layer){
//		try {
//			String tableName = JOptionPane.showInputDialog(PluginServices
//					.getText(this, "intro_tablename"));
//			if (tableName == null)
//				return;
//			tableName = tableName.toLowerCase();
//			DlgConnection dlg = new DlgConnection();
//			dlg.setModal(true);
//			dlg.setVisible(true);
//			ConnectionSettings cs = dlg.getConnSettings();
//			if (cs == null)
//				return;
//			IConnection conex = ConnectionFactory.createConnection(cs
//					.getConnectionString(), cs.getUser(), cs.getPassw());
//
//
//			DBLayerDefinition originalDef = null;
//			if (layer.getSource().getDriver() instanceof IVectorialDatabaseDriver) {
//				originalDef=((IVectorialDatabaseDriver) layer.getSource().getDriver()).getLyrDef();
//			}
//
//
//			DBLayerDefinition dbLayerDef = new DBLayerDefinition();
//			// Fjp:
//			// Cambio: En Postgis, el nombre de catálogo está siempre vacío. Es algo heredado de Oracle, que no se usa.
//			// dbLayerDef.setCatalogName(cs.getDb());
//			dbLayerDef.setCatalogName("");
//
//			// Añadimos el schema dentro del layer definition para poder tenerlo en cuenta.
//			dbLayerDef.setSchema(cs.getSchema());
//
//			dbLayerDef.setTableName(tableName);
//			dbLayerDef.setName(tableName);
//			dbLayerDef.setShapeType(layer.getShapeType());
//			SelectableDataSource sds = layer.getRecordset();
//
//			FieldDescription[] fieldsDescrip = sds.getFieldsDescription();
//			dbLayerDef.setFieldsDesc(fieldsDescrip);
//	        // Creamos el driver. OJO: Hay que añadir el campo ID a la
//	        // definición de campos.
//
//			if (originalDef != null){
//				dbLayerDef.setFieldID(originalDef.getFieldID());
//				dbLayerDef.setFieldGeometry(originalDef.getFieldGeometry());
//
//			}else{
//				// Search for id field name
//				int index=0;
//				String fieldName="gid";
//				while (findFileByName(fieldsDescrip,fieldName) != -1){
//					index++;
//					fieldName="gid"+index;
//				}
//				dbLayerDef.setFieldID(fieldName);
//
//				// search for geom field name
//				index=0;
//				fieldName="the_geom";
//				while (findFileByName(fieldsDescrip,fieldName) != -1){
//					index++;
//					fieldName="the_geom"+index;
//				}
//				dbLayerDef.setFieldGeometry(fieldName);
//
//			}
//
//			// if id field dosen't exist we add it
//			if (findFileByName(fieldsDescrip,dbLayerDef.getFieldID()) == -1)
//			{
//	        	int numFieldsAnt = fieldsDescrip.length;
//	        	FieldDescription[] newFields = new FieldDescription[dbLayerDef.getFieldsDesc().length + 1];
//	            for (int i=0; i < numFieldsAnt; i++)
//	            {
//	            	newFields[i] = fieldsDescrip[i];
//	            }
//	            newFields[numFieldsAnt] = new FieldDescription();
//	            newFields[numFieldsAnt].setFieldDecimalCount(0);
//	            newFields[numFieldsAnt].setFieldType(Types.INTEGER);
//	            newFields[numFieldsAnt].setFieldLength(7);
//	            newFields[numFieldsAnt].setFieldName("gid");
//	            dbLayerDef.setFieldsDesc(newFields);
//
//	        }
//
//
//			dbLayerDef.setWhereClause("");
//			String strSRID = layer.getProjection().getAbrev();
//			dbLayerDef.setSRID_EPSG(strSRID);
//			dbLayerDef.setConnection(conex);
//
//			PostGISWriter writer=(PostGISWriter)LayerFactory.getWM().getWriter("PostGIS Writer");
//			writer.setWriteAll(true);
//			writer.setCreateTable(true);
//			writer.initialize(dbLayerDef);
//			PostGisDriver postGISDriver=new PostGisDriver();
//			postGISDriver.setLyrDef(dbLayerDef);
//			postGISDriver.open();
//			PostProcessSupport.clearList();
//			Object[] params = new Object[2];
//			params[0] = conex;
//			params[1] = dbLayerDef;
//			PostProcessSupport.addToPostProcess(postGISDriver, "setData",
//					params, 1);
//
//			writeFeatures(mapContext, layer, writer, postGISDriver);
//
//		} catch (DriverLoadException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		} catch (DBException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		} catch (InitializeWriterException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		} catch (ReadDriverException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		}
//
//	}

	/**
	 * Lanza un thread en background que escribe las features. Cuando termina, pregunta al usuario si quiere
	 * añadir la nueva capa a la vista. Para eso necesita un driver de lectura ya configurado.
	 * @param mapContext
	 * @param layer
	 * @param writer
	 * @param reader
	 */
//	private void writeFeatures(MapContext mapContext, FLyrVect layer, IWriter writer, Driver reader) throws ReadDriverException
//	{
//		PluginServices.cancelableBackgroundExecution(new WriterTask(mapContext, layer, writer, reader));
//	}
//	private void writeMultiFeatures(MapContext mapContext, FLyrVect layers, IWriter[] writers, Driver[] readers) throws ReadDriverException{
//		MultiWriterTask mwt=new MultiWriterTask();
//		for (int i=0;i<writers.length;i++) {
//			mwt.addTask(new WriterTask(mapContext, layers, writers[i], readers[i]));
//		}
//		PluginServices.cancelableBackgroundExecution(mwt);
//	}
	/**
	 * @param layer
	 *            FLyrVect to obtain features. If selection, only selected
	 *            features will be precessed.
	 * @param writer
	 *            (Must be already initialized)
	 * @throws DataException
	 * @throws ReadException
	 * @throws ProcessWriterException
	 * @throws ExpansionFileReadException
	 * @throws EditionException
	 */
//	public void writeFeaturesNoThread(FLyrVect layer, IWriter writer) throws ReadDriverException, VisitorException, ExpansionFileReadException{
//		ReadableVectorial va = layer.getSource();
//		SelectableDataSource sds = layer.getRecordset();
//
//		// Creamos la tabla.
//		writer.preProcess();
//
//		int rowCount;
//		FBitSet bitSet = layer.getRecordset().getSelection();
//
//		if (bitSet.cardinality() == 0)
//			rowCount = va.getShapeCount();
//		else
//			rowCount = bitSet.cardinality();
//
//		ProgressMonitor progress = new ProgressMonitor(
//				(JComponent) PluginServices.getMDIManager().getActiveWindow(),
//				PluginServices.getText(this, "exportando_features"),
//				PluginServices.getText(this, "exportando_features"), 0,
//				rowCount);
//
//		progress.setMillisToDecideToPopup(200);
//		progress.setMillisToPopup(500);
//
//		if (bitSet.cardinality() == 0) {
//			rowCount = va.getShapeCount();
//			for (int i = 0; i < rowCount; i++) {
//				IGeometry geom = va.getShape(i);
//
//				progress.setProgress(i);
//				if (progress.isCanceled())
//					break;
//
//				if (geom != null) {
//					Value[] values = sds.getRow(i);
//					IFeature feat = new DefaultFeature(geom, values, "" + i);
//					DefaultRowEdited edRow = new DefaultRowEdited(feat,
//							IRowEdited.STATUS_ADDED, i);
//					writer.process(edRow);
//				}
//			}
//		} else {
//			int counter = 0;
//			for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet
//					.nextSetBit(i + 1)) {
//				IGeometry geom = va.getShape(i);
//
//				progress.setProgress(counter++);
//				if (progress.isCanceled())
//					break;
//
//				if (geom != null) {
//					Value[] values = sds.getRow(i);
//					IFeature feat = new DefaultFeature(geom, values, "" + i);
//					DefaultRowEdited edRow = new DefaultRowEdited(feat,
//							IRowEdited.STATUS_ADDED, i);
//
//					writer.process(edRow);
//				}
//			}
//
//		}
//
//		writer.postProcess();
//		progress.close();
//	}



	public void saveToDXF(MapContext mapContext, FLyrVect lv)
			throws DataException, ValidateDataParametersException {
		JFileChooser jfc = new JFileChooser(lastPath);
		SimpleFileFilter filterShp = new SimpleFileFilter("dxf",
				PluginServices.getText(this, "dxf_files"));
		jfc.setFileFilter(filterShp);
		if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
			File newFile = jfc.getSelectedFile();
			String path = newFile.getAbsolutePath();
			if( newFile.exists()){
				int resp = JOptionPane.showConfirmDialog(
						(Component) PluginServices.getMainFrame(),PluginServices.getText(this,"fichero_ya_existe_seguro_desea_guardarlo"),
						PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
				if (resp != JOptionPane.YES_OPTION) {
					return;
				}
			}
			if (!(path.toLowerCase().endsWith(".dxf"))){
				path = path + ".dxf";
			}
			newFile = new File(path);

			DataManager datamanager=DALLocator.getDataManager();

			FilesystemServerExplorerParameters explorerParams=(FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			explorerParams.setRoot(newFile.getParent());

			FilesystemServerExplorer explorer=(FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);

			NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(newFile);
			((DXFStoreParameters)newParams).setSRS(mapContext.getProjection());

			lv.getFeatureStore().export(explorer, newParams);

			openLayer(newParams, newFile.getName(), mapContext);
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

	public void saveToSHP(MapContext mapContext, FLyrVect lv)
			throws DataException, ValidateDataParametersException {
		JFileChooser jfc = new JFileChooser(lastPath);
		SimpleFileFilter filterShp = new SimpleFileFilter("shp",
				PluginServices.getText(this, "shp_files"));
		jfc.setFileFilter(filterShp);
		if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
			File newFile = jfc.getSelectedFile();
			String path = newFile.getAbsolutePath();
			if( newFile.exists()){
				int resp = JOptionPane.showConfirmDialog(
						(Component) PluginServices.getMainFrame(),PluginServices.getText(this,"fichero_ya_existe_seguro_desea_guardarlo"),
						PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
				if (resp != JOptionPane.YES_OPTION) {
					return;
				}
			}
			if (!(path.toLowerCase().endsWith(".shp"))){
				path = path + ".shp";
			}
			newFile = new File(path);

			DataManager datamanager=DALLocator.getDataManager();

			FilesystemServerExplorerParameters explorerParams=(FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			explorerParams.setRoot(newFile.getParent());

			FilesystemServerExplorer explorer=(FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);

			NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(newFile);


			FeatureStore store=lv.getFeatureStore();
			FeatureQuery featureQuery = store.createFeatureQuery();
			((SHPNewStoreParameters) newParams).setSRS(mapContext.getProjection());

			if (lv.getShapeType()==Geometry.TYPES.GEOMETRY){
				//POINT
				Evaluator evaluator=new GeometryTypeEvaluator(store.getDefaultFeatureType(),Geometry.TYPES.POINT,store.getFeatureSelection());
				featureQuery.setFilter(evaluator);
				FeatureSet set=store.getFeatureSet(featureQuery);
				String pathFile=((FilesystemStoreParameters) newParams).getFile().getAbsolutePath();
				String withoutShp=pathFile.replaceAll("\\.shp", "");
				((SHPNewStoreParameters) newParams).setSHPFile(new File(withoutShp + "_point" + ".shp"));
				((SHPNewStoreParameters) newParams).setDBFFile(new File(withoutShp + "_point" + ".dbf"));
				((SHPNewStoreParameters) newParams).setSHXFile(new File(withoutShp + "_point" + ".shx"));
				export(explorer, newParams, set, store,Geometry.TYPES.POINT);
				openLayer(newParams,((SHPNewStoreParameters) newParams).getFile().getName(),mapContext);

				//CURVE
				evaluator=new GeometryTypeEvaluator(store.getDefaultFeatureType(),Geometry.TYPES.CURVE,store.getFeatureSelection());
				featureQuery.setFilter(evaluator);
				set=store.getFeatureSet(featureQuery);
				((SHPNewStoreParameters) newParams).setSHPFile(new File(withoutShp + "_curve" + ".shp"));
				((SHPNewStoreParameters) newParams).setDBFFile(new File(withoutShp + "_curve" + ".dbf"));
				((SHPNewStoreParameters) newParams).setSHXFile(new File(withoutShp + "_curve" + ".shx"));
				export(explorer, newParams, set, lv.getFeatureStore(),Geometry.TYPES.CURVE);
				openLayer(newParams,((SHPNewStoreParameters) newParams).getFile().getName(),mapContext);

				//SURFACE
				evaluator=new GeometryTypeEvaluator(store.getDefaultFeatureType(),Geometry.TYPES.SURFACE,store.getFeatureSelection());
				featureQuery.setFilter(evaluator);
				set=store.getFeatureSet(featureQuery);
				((SHPNewStoreParameters) newParams).setSHPFile(new File(withoutShp + "_surface" + ".shp"));
				((SHPNewStoreParameters) newParams).setDBFFile(new File(withoutShp + "_surface" + ".dbf"));
				((SHPNewStoreParameters) newParams).setSHXFile(new File(withoutShp + "_surface" + ".shx"));
				export(explorer, newParams, set, lv.getFeatureStore(),Geometry.TYPES.SURFACE);
				openLayer(newParams,((SHPNewStoreParameters) newParams).getFile().getName(),mapContext);
			}else{
				store.export(explorer, newParams);
				openLayer(newParams,newFile.getName(),mapContext);
			}
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

	private void export(FilesystemServerExplorer explorer,
			NewFeatureStoreParameters params, FeatureSet set, FeatureStore featureStore, int geometryType) {
//		if (this.getFeatureTypes().size() != 1) {
//			throw new NotYetImplemented(
//					"export whith more than one type not yet implemented");
//		}
//		FeatureSelection featureSelection=(FeatureSelection)getSelection();
		DisposableIterator it1 = null;
		try {
			EditableFeatureType type = featureStore.getDefaultFeatureType().getEditable();
			FeatureAttributeDescriptor fad=(FeatureAttributeDescriptor)type.get(type.getDefaultGeometryAttributeName());
			type.remove(fad.getName());
			EditableFeatureAttributeDescriptor efad=type.add(fad.getName(), fad.getDataType(), fad.getSize());
			efad.setDefaultValue(fad.getDefaultValue());
			efad.setGeometryType(geometryType);
			efad.setPrecision(fad.getPrecision());
			type.setDefaultGeometryAttributeName(fad.getName());
//			if (params.getDefaultFeatureType() == null
//					|| params.getDefaultFeatureType().size() == 0) {
				params.setDefaultFeatureType(type);
//			}
			explorer.add(params, true);

			DataManager manager = DALLocator.getDataManager();
			FeatureStore target = (FeatureStore) manager
					.createStore(params);
			FeatureType targetType = target.getDefaultFeatureType();

			target.edit(FeatureStore.MODE_APPEND);
//			FeatureSet features=null;
//			if (featureSelection.getSize()>0){
//				features = this.getFeatureSelection();
//			}else{
//				features = this.getFeatureSet();
//			}
			it1 = set.iterator();
			while (it1.hasNext()) {
				Feature feature = (Feature) it1.next();
				target.insert(target.createNewFeature(targetType, feature));
			}
			set.dispose();
			target.finishEditing();
			target.dispose();
		} catch (Exception e) {
			NotificationManager.showMessageError(e.getLocalizedMessage(),e);
		} finally {
			if (it1 != null) {
				it1.dispose();
			}
		}

	}
	/**
	 * This method saves a layer to GML
	 * @param mapContext
	 * @param layer
	 * @throws EditionException
	 * @throws DriverIOException
	 */
//	public void saveToGml(MapContext mapContext, FLyrVect layer)  {
//		try {
//			JFileChooser jfc = new JFileChooser();
//			SimpleFileFilter filterShp = new SimpleFileFilter("gml",
//					PluginServices.getText(this, "gml_files"));
//			jfc.setFileFilter(filterShp);
//			if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
//				File newFile = jfc.getSelectedFile();
//				String path = newFile.getAbsolutePath();
//				if (!(path.toLowerCase().endsWith(".gml"))) {
//					path = path + ".gml";
//				}
//				newFile = new File(path);
//
//				GMLWriter writer = (GMLWriter)LayerFactory.getWM().getWriter("GML Writer");
//
//				SHPLayerDefinition lyrDef = new SHPLayerDefinition();
//				SelectableDataSource sds = layer.getRecordset();
//				FieldDescription[] fieldsDescrip = sds.getFieldsDescription();
//				lyrDef.setFieldsDesc(fieldsDescrip);
//				lyrDef.setName(newFile.getName());
//				lyrDef.setShapeType(layer.getShapeType());
//
//				writer.setFile(newFile);
//				writer.setSchema(lyrDef);
//				writer.setBoundedBy(layer.getFullExtent(),layer.getProjection());
//				writer.initialize(lyrDef);
//				GMLDriver gmlDriver=new GMLDriver();
//				gmlDriver.open(newFile);
//				writeFeatures(mapContext, layer, writer, gmlDriver);
//			}
//
//		} catch (SchemaEditionException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		} catch (InitializeWriterException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		} catch (ReadDriverException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		} catch (DriverLoadException e) {
//			NotificationManager.addError(e.getMessage(),e);
//		}
//
//	}
//	private IndexedShpDriver getOpenShpDriver(File fileShp) throws OpenDriverException {
//		IndexedShpDriver drv = new IndexedShpDriver();
//		if (!fileShp.exists()) {
//			try {
//				fileShp.createNewFile();
//				File newFileSHX=new File(fileShp.getAbsolutePath().replaceAll("[.]shp",".shx"));
//				newFileSHX.createNewFile();
//				File newFileDBF=new File(fileShp.getAbsolutePath().replaceAll("[.]shp",".dbf"));
//				newFileDBF.createNewFile();
//			} catch (IOException e) {
//				throw new FileNotFoundDriverException("SHP",e,fileShp.getAbsolutePath());
//			}
//		}
//		drv.open(fileShp);
//		return drv;
//	}
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		int status = EditionUtilities.getEditionStatus();
		if (( status == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE || status == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE)
				|| (status == EditionUtilities.EDITION_STATUS_MULTIPLE_VECTORIAL_LAYER_ACTIVE)|| (status == EditionUtilities.EDITION_STATUS_MULTIPLE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE))
		{
			return true;
		}
		return false;
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
