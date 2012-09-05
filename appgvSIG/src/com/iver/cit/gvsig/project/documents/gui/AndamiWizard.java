package com.iver.cit.gvsig.project.documents.gui;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.swing.wizard.Wizard;


/**
 * Vista de andami que contiene un asistente
 *
 * @author Fernando González Cortés
 */
public class AndamiWizard extends Wizard implements IWindow {
	/**
     * @param backText
     * @param nextText
     * @param finishText
     * @param cancelText
     */
    public AndamiWizard(String backText, String nextText, String finishText, String cancelText) {
        super(backText, nextText, finishText, cancelText);
    }

    /**
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		return new WindowInfo(WindowInfo.ICONIFIABLE | WindowInfo.MAXIMIZABLE |
			WindowInfo.RESIZABLE);
	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
