package com.iver.cit.gvsig.gui.toc;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.dialogs.WFSPropertiesDialog;
import com.iver.cit.gvsig.gui.panels.WFSParamsPanel;
import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: WFSPropertiesTocMenuEntry.java 17595 2007-12-18 13:29:26Z jpiera $
 * $Log$
 * Revision 1.6  2006-12-15 13:51:35  ppiqueras
 * Notificar que se deben cargar los campos y valores conocidos de la capa actual cargada.
 *
 * Revision 1.5  2006/10/27 12:07:18  ppiqueras
 * Nueva funcionalidad
 *
 * Revision 1.4  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.2  2006/08/29 07:56:12  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.1  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 *
 */

/**
 * <p>Loads a dialog the properties of an WFS layer.</p>
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class WFSPropertiesTocMenuEntry extends AbstractTocContextMenuAction {
	private FLyrVect lyr = null; 
	private WFSPropertiesDialog properties = null;
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		if (selectedItems.length == 1){
			lyr = (FLyrVect) selectedItems[0];
		} else {
			return;
		}

		try {
			PanelGroupManager manager = PanelGroupManager.getManager();
			manager.registerPanelGroup(WFSParamsPanel.class);
			manager.setDefaultType(WFSParamsPanel.class);

			// Creates the panel group
			WFSParamsPanel panelGroup = (WFSParamsPanel) manager.getPanelGroup(lyr);
//			panelGroup.setBounds(4, 9, 502, 423);

			// Creates the loader
			PanelGroupLoaderFromExtensionPoint loader = new PanelGroupLoaderFromExtensionPoint("WFSPropertiesDialog");

			// Creates the dialog
			properties = new WFSPropertiesDialog(PluginServices.getText(this, "wfs_properties"), panelGroup);

			// Loads the panels
			properties.loadPanels(loader);

			PluginServices.getMDIManager().addWindow(properties);
		} catch (Exception e) {
			NotificationManager.addError(e);			
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 100;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 10;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return PluginServices.getText(this, "wfs_properties");
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item))
			return (getNodeLayer(item) instanceof FLyrVect);

		return false;
	}
}
