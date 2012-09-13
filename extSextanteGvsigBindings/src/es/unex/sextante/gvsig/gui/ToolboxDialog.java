package es.unex.sextante.gvsig.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionEvent;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionListener;
import org.gvsig.fmap.mapcontext.layers.LayerPositionEvent;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocumentListener;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.unex.sextante.core.Sextante;
import es.unex.sextante.gui.core.SextanteGUI;
import es.unex.sextante.gui.toolbox.ToolboxPanel;

public class ToolboxDialog extends JPanel implements SingletonWindow, IWindowListener, LayerCollectionListener,
														PropertyChangeListener, ProjectDocumentListener {

	private ArrayList<FLayers> listLayers = new ArrayList<FLayers>();
	private WindowInfo viewInfo;
	private ToolboxPanel m_Panel;
	private int m_iCount;

	public ToolboxDialog(){

		super();

		if (SextanteGUI.getInputFactory().getDataObjects() == null){
			SextanteGUI.getInputFactory().createDataObjects();
		}

		initGUI();
		addListeners();

	}

	private void addListeners() {

		//Register a propertyChangeListener to capture event when a new document is added in gvSIG
		Project p = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
		p.addPropertyChangeListener(this);

		//Register addLayerCollectionListener in existing views
		IWindow[] window = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < window.length; i++) {
			if(window[i] instanceof BaseView) {
				FLayers layers = ((BaseView)window[i]).getMapControl().getMapContext().getLayers();
				if(listLayers.indexOf(layers) == -1) {
					layers.addLayerCollectionListener(this);
					listLayers.add(layers);
				}
			}
		}


	}

	private void removeListeners() {

		//TODO:RELLENAR ESTO

	}

	private void initGUI() {

		m_Panel = new ToolboxPanel(null, null);
		m_iCount = m_Panel.getAlgorithmsCount();
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		this.setSize(new Dimension(m_Panel.getWidth(), m_Panel.getHeight()));
		this.add(m_Panel);

	}

	public WindowInfo getWindowInfo() {

        if (viewInfo == null) {
            viewInfo=new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE);

            viewInfo.setTitle("SEXTANTE - " + Integer.toString(m_iCount)
            		+ Sextante.getText("Algoritmos"));
        }
        return viewInfo;

	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}

	public Object getWindowModel() {

		return "SEXTANTE";

	}

	public ToolboxPanel getToolboxPanel(){

		return m_Panel;
	}

	public void cancel(){

		removeListeners();

		if (PluginServices.getMainFrame() == null){
			((JDialog) (getParent().getParent().getParent().getParent())).dispose();
		}
		else{
			PluginServices.getMDIManager().closeWindow(ToolboxDialog.this);
		}

	}

	public void windowActivated() {}

	public void windowClosed() {

		removeListeners();
		SextanteGUI.getInputFactory().clearDataObjects();

	}

	/**
	 * Event throwed when a new document is added
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("addDocument")) {
			if(evt.getNewValue() instanceof ProjectView) {
				ProjectView pd = (ProjectView)evt.getNewValue();
				pd.addProjectViewListener(this);
			}
		}
	}

	/**
	 * Event throwed when a window (View) is created. This method register a listener
	 * in FLayers. When a new layer is going to be added or removed the methods layerAdded and
	 * layerRemoved will catch this event.
	 */
	public void windowCreated(IWindow window) {
		if(window instanceof BaseView) {
			FLayers layers = ((BaseView)window).getMapControl().getMapContext().getLayers();
			if(listLayers.indexOf(layers) == -1) {
				layers.addLayerCollectionListener(this);
				listLayers.add(layers);
			}
		}
	}

	public void layerAdded(LayerCollectionEvent e) {

		// this could be done more elegantly, just adding the new layers...
		// but it works fine this way ;-)
		SextanteGUI.getInputFactory().clearDataObjects();
		SextanteGUI.getInputFactory().createDataObjects();

	}

	public void layerRemoved(LayerCollectionEvent e) {

		SextanteGUI.getInputFactory().clearDataObjects();
		SextanteGUI.getInputFactory().createDataObjects();

	}

	public void layerMoved(LayerPositionEvent e) {}
	public void layerAdding(LayerCollectionEvent e) throws CancelationException {}
	public void layerMoving(LayerPositionEvent e) throws CancelationException {}
	public void layerRemoving(LayerCollectionEvent e) throws CancelationException {}
	public void visibilityChanged(LayerCollectionEvent e) throws CancelationException {}

}
