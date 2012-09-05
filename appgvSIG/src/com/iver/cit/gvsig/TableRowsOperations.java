package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableRowsOperations extends Extension {
    private FeatureTableDocumentPanel table;
	/**
     * DOCUMENT ME!
     */
    public void initialize() {
    	registerIcons();
    }

    private void registerIcons(){
    	PluginServices.getIconTheme().registerDefault(
				"table-selection-up",
				this.getClass().getClassLoader().getResource("images/selectionUp.png")
			);

    	PluginServices.getIconTheme().registerDefault(
				"table-invert",
				this.getClass().getClassLoader().getResource("images/invertSelection.png")
			);
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionCommand DOCUMENT ME!
     */
    public void execute(String actionCommand) {
    	if (actionCommand.compareTo("SELECTIONUP") == 0) {
    		showsSelectedRows(table);
    	}
    	if (actionCommand.compareTo("INVERTSELECTION") == 0) {
    		invertSelection(table);
    	}
    	table.getModel().setModified(true);
    }

    /**
     * Flip the selection (inverts selection)
     * @param table
     */
    private void invertSelection(FeatureTableDocumentPanel table) {
    	try {
    		FeatureStore fs = table.getModel().getStore();
    		fs.getFeatureSelection().reverse();
    	} catch (DataException e) {
			e.printStackTrace();
			NotificationManager.addError(e);
		}
    }

	private void showsSelectedRows(FeatureTableDocumentPanel table) {
//    	long[] mapping=null;
//		try {
//			FeatureStore fs=table.getModel().getModel();
//			mapping = new long[fs.getDataCollection().size()];
//
//		FeatureCollection selectedRows=(FeatureCollection)fs.getSelection();
//
//
//		int m=0;
//		for (int i = selectedRows.nextSetBit(0); i >= 0;
//         	i = selectedRows.nextSetBit(i + 1)) {
//			mapping[m]=i;
//			m++;
//		}
//		for (int i = 0; i < mapping.length;
//     		i++) {
//			if (!selectedRows.get(i)) {
//				mapping[m]=i;
//				m++;
//			}
//		}
//		FIXME
//		table.setOrder("SELECTEDUP");
//		} catch (ReadException e) {
//			e.printStackTrace();
//		}

	}
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
	public boolean isEnabled() {
		try {
			return !table.getModel().getStore().getFeatureSelection().isEmpty();
		} catch (DataException e) {
			NotificationManager.addError(e);
		}

		return false;
	}

//    protected boolean doIsEnabled(Table table) {
//		try {
//			BitSet indices = table.getSelectedFieldIndices();
//
//			System.out.println("TableNumericFieldOperations.isEnabled: Tabla: "
//					+ table.getModel().getModelo().getName());
//
//			if (indices.cardinality() == 1) {
//				int type = table.getModel().getModelo().getRecordset()
//						.getFieldType(indices.nextSetBit(0));
//				if ((type == Types.BIGINT) || (type == Types.DECIMAL)
//						|| (type == Types.DOUBLE) || (type == Types.FLOAT)
//						|| (type == Types.INTEGER) || (type == Types.SMALLINT)
//						|| (type == Types.TINYINT) || (type == Types.REAL)
//						|| (type == Types.NUMERIC)) {
//					return true;
//				}
//
//			}
//		} catch (ReadException e) {
//			e.printStackTrace();
//		}
//
//		return false;
//	}

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean isVisible() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v != null && v instanceof FeatureTableDocumentPanel) {
            table=(FeatureTableDocumentPanel)v;
        	return true;
        }

        return false;
    }
}
