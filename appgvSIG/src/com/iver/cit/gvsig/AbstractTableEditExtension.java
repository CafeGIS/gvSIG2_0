package com.iver.cit.gvsig;

import org.gvsig.project.document.table.FeatureTableOperations;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;

public abstract class AbstractTableEditExtension extends Extension{
	protected FeatureTableDocumentPanel table=null;
	protected FeatureTableOperations featureTableOperations=null;

	/**
     * @see com.iver.andami.plugins.IExtension#initialize()
     */
    public void initialize() {
    	featureTableOperations=FeatureTableOperations.getInstance();
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();
        if (v!=null && v instanceof FeatureTableDocumentPanel && ((FeatureTableDocumentPanel) v).getModel().getStore().isEditing()) {
            table=(FeatureTableDocumentPanel)v;
        	return true;
        }
        return false;
    }

}
