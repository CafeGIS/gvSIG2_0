package es.unex.sextante.gvsig.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

import es.unex.sextante.core.Sextante;
import es.unex.sextante.gui.core.SextanteGUI;
import es.unex.sextante.gui.modeler.ModelerPanel;

public class ModelerDialog extends JPanel implements IWindow, IWindowListener {

	private WindowInfo viewInfo;
	private ModelerPanel m_Panel;

	public ModelerDialog(){

		super();

		if (SextanteGUI.getInputFactory().getDataObjects() == null){
			SextanteGUI.getInputFactory().createDataObjects();
		}

		initGUI();

	}

	private void initGUI() {

		m_Panel = new ModelerPanel(null);
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		this.setSize(new Dimension(m_Panel.getWidth(), m_Panel.getHeight()));
		this.add(m_Panel);

	}

	public WindowInfo getWindowInfo() {

        if (viewInfo == null) {
            viewInfo=new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
            viewInfo.setTitle(Sextante.getText("Modelizador"));
        }
        return viewInfo;

	}
	
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

	public void windowActivated() {}

	public void windowClosed() {

		SextanteGUI.getInputFactory().clearDataObjects();

	}

}
