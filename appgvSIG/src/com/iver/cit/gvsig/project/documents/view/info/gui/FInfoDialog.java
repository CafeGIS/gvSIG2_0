
package com.iver.cit.gvsig.project.documents.view.info.gui;

import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;


/**
 * Dialog that contains the generic Feature Info Viewer
 *
 * @author laura
 *
 */
public class FInfoDialog extends InfoToolViewer implements SingletonWindow {


	public FInfoDialog(){
		super();
		setSize(600, 375);
	}

	public FInfoDialog(XMLItem[] layers) {
		super(layers);
		setSize(600, 375);
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {

		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG |
				WindowInfo.RESIZABLE | WindowInfo.PALETTE);
		m_viewinfo.setWidth(getWidth()+8);
		m_viewinfo.setHeight(getHeight());
		m_viewinfo.setTitle(PluginServices.getText(this,
				"Identificar_Resultados"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.SingletonWindow#getWindowModel()
	 */
	public Object getWindowModel() {
		return "FInfoDialog";
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
