package org.gvsig.remotesensing.decisiontrees.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class ClassEditorDialog extends JPanel implements ButtonsPanelListener, IWindow, IWindowListener {

	private static final long serialVersionUID = -2297193201725695467L;
	
	private ClassEditorPanel 	classEditorPanel = null;
	private DecisionTreePanel 	decisionTreePanel = null;

	public ClassEditorDialog(int width, int height, DecisionTreePanel decisionTreePanel) {
		this.setSize(width, height);
		this.setLayout(new BorderLayout());		
		this.decisionTreePanel = decisionTreePanel;
		this.add(getClassEditorPanel(), BorderLayout.CENTER);
	}
	
	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
			close();
		}
	}
	
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this, "editor_clase"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void close() {
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}

	public void windowActivated() {
	
	}

	public void windowClosed() {
			
	}

	public ClassEditorPanel getClassEditorPanel() {
		if (classEditorPanel == null){
			classEditorPanel = new ClassEditorPanel(this, getDecisionTreePanel());
		}
		return classEditorPanel;
	}

	public DecisionTreePanel getDecisionTreePanel() {
		return decisionTreePanel;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
