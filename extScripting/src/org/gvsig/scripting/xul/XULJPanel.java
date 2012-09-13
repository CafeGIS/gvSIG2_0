package org.gvsig.scripting.xul;

import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.gvsig.tools.ToolsLocator;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.MDIManager;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

public class XULJPanel extends JPanel implements IWindow, IPersistence, IXULPanel {

	private static final long serialVersionUID = -8512371289780040873L;

	private WindowInfo windowInfo = null;

	private XULPanel thinlet = null;


	public XULJPanel() {
		this.addAncestorListener(new XULJPanelAncestorListener(this));
	}

	public XULJPanel(XULPanel thinlet) {
		this.thinlet = thinlet;
		this.addAncestorListener(new XULJPanelAncestorListener(this));
	}

	protected XULPanel getThinlet() {
		if (thinlet == null) {
			thinlet = new XULPanel(this);
		}
		return thinlet;
	}

	//==============================================================
	// Implementacion del interface IWindow
	//
	public WindowInfo getWindowInfo() {

		if (windowInfo == null) {
			windowInfo = new WindowInfo(WindowInfo.RESIZABLE| WindowInfo.MODELESSDIALOG);
			windowInfo.setWidth(100);
			windowInfo.setHeight(100);
		}

		int width = getThinlet().getPreferredSize().width;
		int height = getThinlet().getPreferredSize().height;
		if (height > 0 && width > 0) {
			if (width != windowInfo.getWidth()) {
				windowInfo.setWidth(width);
			}
			if (height != windowInfo.getHeight()) {
				this.windowInfo.setHeight(height);
			}
		}
		return this.windowInfo;
	}

	//==============================================================
	// Implementacion del interface IPersistance
	//
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClass().getName());
		xml.putProperty("xulFile", getThinlet().getXULFile());
		xml.putProperty(
			"classNameOfThinlet",
			getEngine().getClass().getName()
		);
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		try {
			String classNameOfThinlet = xml.getStringProperty("classNameOfThinlet");
			if (classNameOfThinlet != null) {
				Object[] args = { this };
				thinlet = (XULPanel) ToolsLocator
						.getExtensionPointManager().createObject(
								Class.forName(classNameOfThinlet), args);

			}
			setXULFile(xml.getStringProperty("xulFile"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getClassName() {
		return this.getClassName();
	}

	//==============================================================
	// Implementacion del interface IXULPanel
	//
	public Object get(Object key) {
		return getThinlet().get(key);
	}

	public Object put(Object key, Object value) {
		return getThinlet().put(key,value);
	}

	public void setXULFile(String xulFile) throws IOException {
		getThinlet().setXULFile(xulFile);
	}

	public String getXULFile() {
		return getThinlet().getXULFile();
	}

	public Object getEngine() {
		return getThinlet();
	}

	public JPanel getJPanel() {
		return this;
	}

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}

	public void showWindow() {
		MDIManager mdim = PluginServices.getMDIManager();
		mdim.addWindow(this);
	}

	public void setTitle(String title) {
		getWindowInfo().setTitle(title);
	}

	public void setPalette(boolean state) {
		getWindowInfo().toPalette(state);
	}

	public Object find(String name) {
		return getThinlet().find(name);
	}

	public Object createComponent(String name) {
		return getThinlet().find(name);
	}

	public void setString(Object component, String propertyName, String value) {
		getThinlet().setString(component,propertyName,value);
	}

	public void setInteger(Object component, String propertyName, int value) {
		getThinlet().setInteger(component,propertyName,value);
	}

	public void setBoolean(Object component, String propertyName, boolean value) {
		getThinlet().setBoolean(component,propertyName,value);
	}

	public Object getItem(Object component, int index) {
		return getThinlet().getItem(component,index);
	}

	public int getSize(Object component) {
		return getThinlet().getCount(component);
	}

	public String getString(Object component, String propertyName) {
		return getThinlet().getString(component,propertyName);
	}

	public int getInteger(Object component, String propertyName) {
		return getThinlet().getInteger(component,propertyName);
	}

	public boolean getBoolean(Object component, String propertyName) {
		return getThinlet().getBoolean(component,propertyName);
	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}


}

class XULJPanelAncestorListener implements AncestorListener {

	private XULJPanel panel = null;

	public XULJPanelAncestorListener(XULJPanel panel) {
		this.panel = panel;
	}

	public void ancestorMoved(AncestorEvent event) {
		if (panel.getThinlet() == null) {
			return;
		}
		panel.getThinlet().repaint();
	}

	public void ancestorRemoved(AncestorEvent event) {
		// Do nothing

	}

	public void ancestorAdded(AncestorEvent event) {
		if (panel.getThinlet() == null) {
			return;
		}
		panel.getThinlet().repaint();

	}
}
