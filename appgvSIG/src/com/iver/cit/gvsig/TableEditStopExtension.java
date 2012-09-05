package com.iver.cit.gvsig;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.plugins.status.IExtensionStatus;
import com.iver.andami.plugins.status.IUnsavedData;
import com.iver.andami.plugins.status.UnsavedData;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.utiles.swing.threads.IMonitorableTask;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableEditStopExtension extends AbstractTableEditExtension {

    /**
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
    	if ("STOPEDITING".equals(actionCommand)) {
            stopEditing(table);
        }
    }

    /**
	 * DOCUMENT ME!
	 */
	public void stopEditing(FeatureTableDocumentPanel table) {
//		FIXME
		int resp = JOptionPane
				.showConfirmDialog(null, PluginServices.getText(this,
						"realmente_desea_guardar") +" : "+ table.getModel().getName(), "Guardar",
						JOptionPane.YES_NO_OPTION);
		try {
			if (resp == JOptionPane.NO_OPTION) { // CANCEL EDITING
				table.getModel().getStore().cancelEditing();
			} else { // GUARDAMOS LA TABLA
				table.getModel().getStore().finishEditing();
			}
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
       return true;
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();

        if (v == null) {
            return false;
        } else if (v instanceof FeatureTableDocumentPanel && ((FeatureTableDocumentPanel) v).getModel().getStore().isEditing() && ((FeatureTableDocumentPanel)v).getModel().getAssociatedLayer()==null) {
            table=(FeatureTableDocumentPanel)v;
        	return true;
        } else {
            return false;
        }
    }
    /**
	 * <p>This class provides the status of extensions.
	 * If this extension has some unsaved editing table (and save them), and methods
	 * to check if the extension has some associated background tasks.
	 *
	 * @author Vicente Caballero Navarro
	 *
	 */
	private class StopEditingStatus implements IExtensionStatus {
		/**
	     * This method is used to check if this extension has some unsaved editing tables.
	     *
	     * @return true if the extension has some unsaved editing tables, false otherwise.
	     */
		public boolean hasUnsavedData() {
			ProjectExtension pe=(ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
			FeatureTableDocument[] tables=pe.getProject().getDocumentsByType(FeatureTableDocumentFactory.registerName).toArray(new FeatureTableDocument[0]);
			for (int i=0;i<tables.length;i++) {
				if (tables[i].getStore() == null){
					continue;
				}
				if (tables[i].getStore().isEditing()) {
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
	     * the unsaved editing tables and allows to save it.</p>
	     *
	     * @return An array of the associated unsaved editing layers, or null in case the extension
	     * has not unsaved editing tables.
	     */
		public IUnsavedData[] getUnsavedData() {
			ProjectExtension pe=(ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
			FeatureTableDocument[] tables =pe.getProject().getDocumentsByType(FeatureTableDocumentFactory.registerName).toArray(new FeatureTableDocument[0]);
			ArrayList unsavedTables = new ArrayList();
			for (int i=0;i<tables.length;i++) {
				if (tables[i].getStore() == null){
					continue;
				}
				if (tables[i].getStore().isEditing()) {
					UnsavedTable ul=new UnsavedTable(TableEditStopExtension.this);
					ul.setTable(tables[i]);
					unsavedTables.add(ul);
				}
			}
			return (IUnsavedData[])unsavedTables.toArray(new IUnsavedData[0]);
		}
	}

	private class UnsavedTable extends UnsavedData{

		private FeatureTableDocument table;

		public UnsavedTable(IExtension extension) {
			super(extension);
		}

		public String getDescription() {
			return PluginServices.getText(this,"editing_table_unsaved");
		}

		public String getResourceName() {
			return table.getName();
		}



		public boolean saveData() {
			return executeSaveTable(table);
		}



		public void setTable(FeatureTableDocument table) {
			this.table=table;

		}
	}


	//TODO Este código está duplicado, también está en la clase Table en el método "public void stopEditing()"
	private boolean executeSaveTable(FeatureTableDocument table2) {
		FeatureStore fs = table2.getStore();
		try {
			fs.finishEditing();
		} catch (WriteException e) {
			NotificationManager.addError(PluginServices.getText(this,"error_saving_table"),e);
			return false;
		} catch (ReadException e) {
			NotificationManager.addError(PluginServices.getText(this,"error_saving_table"),e);
			return false;
		} catch (DataException e) {
			NotificationManager.addError(PluginServices.getText(this,"error_saving_table"),e);
			return false;
		}
//		if (ies instanceof IWriteable) {
//			IWriteable w = (IWriteable) ies;
//			IWriter writer = w.getWriter();
//			if (writer == null) {
//				return false;
//			}
//			try {
//				ITableDefinition tableDef = ies.getTableDefinition();
//				writer.initialize(tableDef);
//				ies.stopEdition(writer, EditionEvent.ALPHANUMERIC);
//				ies.getSelection().clear();
//			} catch (InitializeWriterException e) {
//				NotificationManager.addError(PluginServices.getText(this,"error_saving_table"),e);
//				return false;
//			} catch (StopWriterVisitorException e) {
//				NotificationManager.addError(PluginServices.getText(this,"error_saving_table"),e);
//				return false;
//			} catch (ReadDriverException e) {
//				NotificationManager.addError(PluginServices.getText(this,"error_saving_table"),e);
//				return false;
//			}
//		}
		return true;
	}
	public IExtensionStatus getStatus() {
		return new StopEditingStatus();
	}
}
