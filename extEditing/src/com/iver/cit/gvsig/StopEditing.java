package com.iver.cit.gvsig;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.CancelEditingLayerException;
import org.gvsig.fmap.mapcontext.exceptions.StartEditionLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.plugins.status.IExtensionStatus;
import com.iver.andami.plugins.status.IUnsavedData;
import com.iver.andami.plugins.status.UnsavedData;
import com.iver.cit.gvsig.gui.cad.CADToolAdapter;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.cit.gvsig.project.documents.table.exceptions.CancelEditingTableException;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.swing.threads.IMonitorableTask;

/**
 * @author Francisco Jos�
 *
 * Cuando un tema se pone en edici�n, puede que su driver implemente
 * ISpatialWriter. En ese caso, es capaz de guardarse sobre s� mismo. Si no lo
 * implementa, esta opci�n estar� deshabilitada y la �nica posibilidad de
 * guardar este tema ser� "Guardando como..."
 */
public class StopEditing extends Extension {
	private View vista;

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();

		vista = (View) f;
		boolean isStop=false;
		IProjectView model = vista.getModel();
		MapContext mapa = model.getMapContext();
		FLayers layers = mapa.getLayers();
		EditionManager edMan = CADExtension.getEditionManager();
		if (s.equals("STOPEDITING")) {
			vista.getMapControl().getCanceldraw().setCanceled(true);
			FLayer[] actives = layers.getActives();
			// TODO: Comprobar que solo hay una activa, o al menos
			// que solo hay una en edici�n que est� activa, etc, etc
			for (int i = 0; i < actives.length; i++) {
				if (actives[i] instanceof FLyrVect && actives[i].isEditing()) {
					FLyrVect lv = (FLyrVect) actives[i];
					MapControl mapControl = vista.getMapControl();
					VectorialLayerEdited lyrEd = (VectorialLayerEdited)	edMan.getActiveLayerEdited();
					try {
						lv.getFeatureStore().deleteObserver(lyrEd);
					} catch (ReadException e) {
						NotificationManager.addError("Remove Selection Listener",e);
					}
					isStop=stopEditing(lv, mapControl);
					if (isStop){
						lv.removeLayerListener(edMan);
						mapControl.getMapContext().removeLayerDrawListener(
								lyrEd);
//						if (lv instanceof FLyrAnnotation){
//							FLyrAnnotation lva=(FLyrAnnotation)lv;
//				            lva.setMapping(lva.getMapping());
//						}
					} else {
						try {
							lv.getFeatureStore().addObserver(lyrEd);
						} catch (ReadException e) {
							NotificationManager.addError(
									"Remove Selection Listener", e);
						}

					}
				}
			}
			if (isStop) {
				vista.getMapControl().setTool("zoomIn");
				vista.hideConsole();
				vista.repaintMap();
				CADExtension.clearView();

			}
		}
		PluginServices.getMainFrame().enableControls();
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		FLayer[] lyrs = EditionUtilities.getActiveAndEditedLayers();
		if (lyrs == null) {
			return false;
		}
		FLyrVect lyrVect = (FLyrVect) lyrs[0];
		if (lyrVect.isEditing()) {
			return true;
		}
		return false;
	}
	/**
	 * DOCUMENT ME!
	 */
	public boolean stopEditing(FLyrVect layer, MapControl mapControl) {
//		FeatureStore featureStore=null;
//		try {
//			featureStore = layer.getFeatureStore();
//		} catch (ReadException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		int resp = JOptionPane.NO_OPTION;

		try {
			if (layer.isWritable()) {
				resp = JOptionPane.showConfirmDialog((Component) PluginServices
						.getMainFrame(), PluginServices.getText(this,
						"realmente_desea_guardar_la_capa")
						+ " : " + layer.getName()+"?", PluginServices.getText(this,
						"guardar"), JOptionPane.YES_NO_OPTION);
				if (resp == JOptionPane.YES_OPTION) { // GUARDAMOS EL TEMA
					saveLayer(layer);

				} else if (resp == JOptionPane.NO_OPTION){ // CANCEL EDITING
					cancelEdition(layer);
				} else {
					return false;
				}

//				featureStore.deleteObservers();
				layer.setEditing(false);
//				if (layer.isSpatiallyIndexed())
//	            {
//	            	if(vea.getSpatialIndex() != null)
//	                {
//	            		layer.setISpatialIndex(vea.getSpatialIndex());
//	            		if(layer.getISpatialIndex() instanceof IPersistentSpatialIndex)
//	                        ((IPersistentSpatialIndex) layer.getISpatialIndex()).flush();
//	            		PluginServices.
//								cancelableBackgroundExecution(new CreateSpatialIndexMonitorableTask(layer));
//
//	                }
//	            }

				return true;
			}
			// Si no existe writer para la capa que tenemos en edici�n
				resp = JOptionPane
						.showConfirmDialog(
								(Component) PluginServices.getMainFrame(),
								PluginServices
										.getText(
												this,
												"no_existe_writer_para_este_formato_de_capa_o_no_tiene_permisos_de_escritura_los_datos_no_se_guardaran_desea_continuar")
										+ " : " + layer.getName(),
								PluginServices.getText(this, "cancelar_edicion"),
								JOptionPane.YES_NO_OPTION);
				if (resp == JOptionPane.YES_OPTION) { // CANCEL EDITING
					cancelEdition(layer);

//					featureStore.deleteObservers();
//					if (!(layer.getSource().getDriver() instanceof IndexedShpDriver)){
//						VectorialLayerEdited vle=(VectorialLayerEdited)CADExtension.getEditionManager().getLayerEdited(layer);
//						layer.setLegend((IVectorLegend)vle.getLegend());
//					}
					layer.setEditing(false);
					return true;
				}

		} catch (StartEditionLayerException e) {
			NotificationManager.addError(e);
		} catch (ReadException e) {
			NotificationManager.addError(e);
		} catch (CancelEditingTableException e) {
			NotificationManager.addError(e);
		} catch (CancelEditingLayerException e) {
			NotificationManager.addError(e);
		}
		return false;

	}


	private void saveLayer(FLyrVect layer) throws ReadException{
		layer.setProperty("stoppingEditing",new Boolean(true));
		FeatureStore featureStore=layer.getFeatureStore();
//		VectorialEditableAdapter vea = (VectorialEditableAdapter) layer
//				.getSource();

//		ISpatialWriter writer = (ISpatialWriter) vea.getWriter();
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices
				.getMDIManager().getAllWindows();
		for (int j = 0; j < views.length; j++) {
			if (views[j] instanceof FeatureTableDocumentPanel) {
				FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) views[j];
				if (table.getModel().getAssociatedLayer() != null
						&& table.getModel().getAssociatedLayer().equals(layer)) {
//					table.stopEditing();
				}
			}
		}
//		vea.cleanSelectableDatasource();
//		layer.setRecordset(vea.getRecordset()); // Queremos que el recordset del layer
		// refleje los cambios en los campos.
//		ILayerDefinition lyrDef = EditionUtilities.createLayerDefinition(layer);
//		String aux="FIELDS:";
//		FieldDescription[] flds = lyrDef.getFieldsDesc();
//		for (int i=0; i < flds.length; i++){
//			aux = aux + ", " + flds[i].getFieldAlias();
//		}
//		System.err.println("Escribiendo la capa " + lyrDef.getName() +
//				" con los campos " + aux);
//		lyrDef.setShapeType(layer.getShapeType());
//		writer.initialize(lyrDef);
//		vea.stopEdition(writer, EditionEvent.GRAPHIC);
		try {
			featureStore.finishEditing();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		layer.setProperty("stoppingEditing",new Boolean(false));
	}

	private void cancelEdition(FLyrVect layer) throws CancelEditingTableException, CancelEditingLayerException {
		layer.setProperty("stoppingEditing",new Boolean(true));
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices
			.getMDIManager().getAllWindows();
		FeatureStore featureStore=null;
		try {
			featureStore = layer.getFeatureStore();

			featureStore.cancelEditing();
//			VectorialEditableAdapter vea = (VectorialEditableAdapter) layer
//			.getSource();
//			vea.cancelEdition(EditionEvent.GRAPHIC);
			for (int j = 0; j < views.length; j++) {
				if (views[j] instanceof FeatureTableDocumentPanel) {
					FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) views[j];
					if (table.getModel().getAssociatedLayer() != null
							&& table.getModel().getAssociatedLayer().equals(layer)) {
//						table.stopEditing();
					}
				}
			}
			layer.setProperty("stoppingEditing",new Boolean(false));
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		if (EditionUtilities.getEditionStatus() == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE) {
			return true;
		}
		return false;

	}
	public IExtensionStatus getStatus() {
		return new StopEditingStatus();
	}
	/**
	 * Show the dialogs to save the layer without ask if don't like to save.
	 * @param layer Layer to save.
	 */
	public boolean executeSaveLayer(FLyrVect layer ) {
//		EditionManager edMan = CADExtension.getEditionManager();
		CADToolAdapter cadtoolAdapter=CADExtension.getCADToolAdapter(layer);
		EditionManager edMan =cadtoolAdapter.getEditionManager();
		VectorialLayerEdited lyrEd = (VectorialLayerEdited)	edMan.getLayerEdited(layer);
		boolean isStop=false;
		try {
			lyrEd.clearSelection();


		if (layer.isWritable()) {
				saveLayer(layer);
				layer.setEditing(false);
//				if (layer.isSpatiallyIndexed())
//		            {
//		            	if(layer.getISpatialIndex() != null)
//		                {
//		                	PluginServices.
//									cancelableBackgroundExecution(new CreateSpatialIndexMonitorableTask((FLyrVect)layer));
//						}
//		            }

			isStop=true;
		}else {
//			 Si no existe writer para la capa que tenemos en edici�n
			int resp = JOptionPane
					.showConfirmDialog(
							(Component) PluginServices.getMainFrame(),
							PluginServices
									.getText(
											this,
											"no_existe_writer_para_este_formato_de_capa_o_no_tiene_permisos_de_escritura_los_datos_no_se_guardaran_desea_continuar")
									+ " : " + layer.getName(),
							PluginServices.getText(this, "cancelar_edicion"),
							JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) { // CANCEL EDITING
				try {
					cancelEdition(layer);
					layer.setEditing(false);
//				if (!(layer.getSource().getDriver() instanceof IndexedShpDriver)){
//					VectorialLayerEdited vle=(VectorialLayerEdited)CADExtension.getEditionManager().getLayerEdited(layer);
//					layer.setLegend((IVectorLegend)vle.getLegend());
//				}
				} catch (CancelEditingTableException e) {
					PluginServices.getLogger().error(e.getMessage(),e);
//					NotificationManager.addError(e.getMessage(),e);
					return isStop;
				} catch (CancelEditingLayerException e) {
					PluginServices.getLogger().error(e.getMessage(),e);
//					NotificationManager.addError(e.getMessage(),e);
					return isStop;
				}
				isStop=true;
			}

		}
//		boolean isStop=stopEditing((FLyrVect)layer, null);
		if (isStop){
			layer.removeLayerListener(edMan);
//			if (layer instanceof FLyrAnnotation){
//				FLyrAnnotation lva=(FLyrAnnotation)layer;
//	            lva.setMapping(lva.getMapping());
//			}
			com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();
			if (f instanceof View) {
				vista = (View) f;
				FLayer auxLayer=vista.getMapControl().getMapContext().getLayers().getLayer(layer.getName());
				if (auxLayer!=null && auxLayer.equals(layer)) {
					vista.getMapControl().setTool("zoomIn");
					vista.hideConsole();
					vista.repaintMap();
					CADExtension.clearView();
				}
			}
		}
		} catch (ReadException e1) {
//			PluginServices.getLogger().error(e1.getMessage(),e1);
			NotificationManager.showMessageError(e1.getMessage(),e1);
		} catch (StartEditionLayerException e) {
//			PluginServices.getLogger().error(e.getMessage(),e);
			NotificationManager.showMessageError(e.getMessage(),e);
		} catch (DataException e) {
			NotificationManager.showMessageError(e.getMessage(),e);
		}
		return isStop;
	}

	private class UnsavedLayer extends UnsavedData{

		private FLayer layer;

		public UnsavedLayer(IExtension extension) {
			super(extension);
		}

		public String getDescription() {
			return PluginServices.getText(this,"editing_layer_unsaved");
		}

		public String getResourceName() {
			return layer.getName();
		}



		public boolean saveData() {
			return executeSaveLayer((FLyrVect)layer);
		}

		public void setLayer(FLayer layer) {
			this.layer=layer;

		}

		public ImageIcon getIcon() {
			return layer.getTocImageIcon();
		}

	}

	/**
	 * <p>This class provides the status of extensions.
	 * If this extension has some unsaved editing layer (and save them), and methods
	 * to check if the extension has some associated background tasks.
	 *
	 * @author Vicente Caballero Navarro
	 *
	 */
	private class StopEditingStatus implements IExtensionStatus {
		/**
	     * This method is used to check if this extension has some unsaved editing layer.
	     *
	     * @return true if the extension has some unsaved editing layer, false otherwise.
	     */
		public boolean hasUnsavedData() {
			ProjectExtension pe=(ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
			ProjectView[] views=pe.getProject().getDocumentsByType(ProjectViewFactory.registerName).toArray(new ProjectView[0]);
			for (int i=0;i<views.length;i++) {
				FLayers layers=views[i].getMapContext().getLayers();
				LayersIterator iter = getEditingLayer(layers);
				if (iter.hasNext()) {
					return true;
				}
			}
			return false;
		}
		/**
	     * This method is used to check if the extension has some associated
	     * background process which is currently running.
	     *
	     * @return true if the extension has some associated background process,
	     * false otherwise.
	     */
		public boolean hasRunningProcesses() {
			return false;
		}
		 /**
	     * <p>Gets an array of the traceable background tasks associated with this
	     * extension. These tasks may be tracked, canceled, etc.</p>
	     *
	     * @return An array of the associated background tasks, or null in case there is
	     * no associated background tasks.
	     */
		public IMonitorableTask[] getRunningProcesses() {
			return null;
		}
		/**
	     * <p>Gets an array of the UnsavedData objects, which contain information about
	     * the unsaved editing layers and allows to save it.</p>
	     *
	     * @return An array of the associated unsaved editing layers, or null in case the extension
	     * has not unsaved editing layers.
	     */
		public IUnsavedData[] getUnsavedData() {
			ProjectExtension pe=(ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
			ProjectView[] views=pe.getProject().getDocumentsByType(ProjectViewFactory.registerName).toArray(new ProjectView[0]);
			ArrayList unsavedLayers=new ArrayList();
			for (int i=0;i<views.length;i++) {
				FLayers layers = views[i].getMapContext().getLayers();
				LayersIterator iter = getEditingLayer(layers);
				while (iter.hasNext()){
					UnsavedLayer ul=new UnsavedLayer(StopEditing.this);
					ul.setLayer(iter.nextLayer());
					unsavedLayers.add(ul);
				}
			}
			return (IUnsavedData[])unsavedLayers.toArray(new IUnsavedData[0]);
		}
	}
	private LayersIterator getEditingLayer(FLayers layers){
		return new LayersIterator(layers){
			public boolean evaluate(FLayer layer) {
				return layer.isEditing();
			}
		};
	}
}

