/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*/


package org.gvsig.gvsig3dgui.layer.properties;

import org.gvsig.exceptions.BaseException;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.panelGroup.PanelGroupDialog;
import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;


/**
 * Entrada en del menú contextual del TOC correspondiente al cuadro de
 * propiedades 3D
 * 
 * @author Ángel Fraile Griñán. (angel.fraile@iver.es)
 */

public class PropertiesTocMenuEntry3D extends AbstractTocContextMenuAction {

	

	static private PropertiesTocMenuEntry3D singleton = null;
	private PanelGroupDialog properties = null;
	private FLayer lyr = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private PropertiesTocMenuEntry3D() {
	}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * 
	 * @return
	 */
	static public PropertiesTocMenuEntry3D getSingleton() {
		if (singleton == null)
			singleton = new PropertiesTocMenuEntry3D();
		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return PluginServices.getText(this, "Layer3D");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 60;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return (PluginServices.getText(this, "Propiedades_3D"));
	}

	/**
	 * Enable options
	 * Is enabled when a view 3D is selected
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;
		else
			return true;
	}

	/**
	 * Visibility options
	 * Is visible when a view 3D is selected
	 *     
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		boolean visible;
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;
		Layer3DProps prop3D;
		prop3D = Layer3DProps.getLayer3DProps(selectedItems[0]);
		visible = (prop3D == null) ? false : true;

		return visible;
	}

	/**
	 * Gestiona la apertura del diálogo de propiedades 3D cuando se pulsa la
	 * opción asignando a este las propiedades iniciales.
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {

		if ((selectedItems == null) || (selectedItems.length != 1))
			return;

		lyr = selectedItems[0];

		try {
			PanelGroupManager manager = PanelGroupManager.getManager();

			manager.registerPanelGroup(TabbedPanel.class);
			manager.setDefaultType(TabbedPanel.class);

			TabbedPanel panelGroup = (TabbedPanel) manager.getPanelGroup(lyr);
			PanelGroupLoaderFromExtensionPoint loader = new PanelGroupLoaderFromExtensionPoint(
					"PropertiesDialog3D");

			properties = new PanelGroupDialog(lyr.getName(), PluginServices.getText(this, "Propiedades_3D"),
					550, 450, (byte) (WindowInfo.MODELESSDIALOG
							| WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE),
					panelGroup);
			properties.loadPanels(loader);
			PluginServices.getMDIManager().addWindow(properties);
		} catch (BaseException be) {
			System.out.println(be.getLocalizedMessageStack());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}