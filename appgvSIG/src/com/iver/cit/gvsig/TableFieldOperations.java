package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.data.feature.swing.table.ConfigurableFeatureTableModel;
import org.gvsig.project.document.table.FeatureTableOperations;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;

/**
 * Ascending or descending order operations.
 *
 * @author Vicente Caballero Navarro
 */
public class TableFieldOperations extends Extension{
	private FeatureTableDocumentPanel table=null;
    /**
     * @see com.iver.andami.plugins.IExtension#initialize()
     */
    public void initialize() {
    	registerIcons();
    }

    private void registerIcons(){
    	PluginServices.getIconTheme().registerDefault(
				"table-order-asc",
				this.getClass().getClassLoader().getResource("images/orderasc.png")
			);

    	PluginServices.getIconTheme().registerDefault(
				"table-order-desc",
				this.getClass().getClassLoader().getResource("images/orderdesc.png")
			);
    }

    /**
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
		doExecute(actionCommand,table);
		table.getModel().setModified(true);
    }

    /**
     * "execute" method acction
     * @param actionCommand
     * The acction command that executes this method
     * @param table
     * Table to operate
     */
	protected void doExecute(String actionCommand,FeatureTableDocumentPanel table){
//		FIXME
		ConfigurableFeatureTableModel cftm=table.getTablePanel().getTableModel();
		try {
			if ("ORDERASC".equals(actionCommand)){
				cftm.orderByColumn(table.getTablePanel().getTable().getSelectedColumnsAttributeDescriptor()[0].getName(), true);
			}else if ("ORDERDESC".equals(actionCommand)){
				cftm.orderByColumn(table.getTablePanel().getTable().getSelectedColumnsAttributeDescriptor()[0].getName(), false);
			}
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

    /**
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
		try {
			return (table.getTablePanel().getTable().getSelectedColumnCount()==1);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return false;
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();
		if (v!=null && v instanceof FeatureTableDocumentPanel) {
		    table=(FeatureTableDocumentPanel)v;
			return true;
		}
		return false;
    }

}
