package com.iver.cit.gvsig.gui.toc;

import javax.swing.JDialog;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ThemeManagerWindow;
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
 * $Id: WFSVectorialPropsTocMenuEntry.java 17739 2008-01-02 16:55:18Z ppiqueras $
 * $Log$
 * Revision 1.6  2007-03-09 11:28:44  jaume
 *  new properties panel
 *
 * Revision 1.5  2007/03/06 17:06:25  caballero
 * Exceptions
 *
 * Revision 1.4  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.2  2006/08/29 07:56:12  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.1  2006/07/05 12:05:41  jorpiell
 * Se ha modificado para que avise si se han recuperado las mismas features que marca el campo buffer
 *
 *
 */

/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSVectorialPropsTocMenuEntry extends AbstractTocContextMenuAction{
	private FLayer lyr = null;

	@Override
	public void execute(ITocItem item, FLayer[] selectedItems) {
		if (selectedItems.length == 1) {
			lyr = selectedItems[0];
		} else {
			return;
		}

		ThemeManagerWindow fThemeManagerWindow;
		fThemeManagerWindow = new ThemeManagerWindow(lyr);
		//PluginServices.getMDIManager().addView(fThemeManagerWindow);
		if (PluginServices.getMainFrame() == null) {
			JDialog dlg = new JDialog();
			fThemeManagerWindow.setPreferredSize(fThemeManagerWindow.getSize());
			dlg.getContentPane().add(fThemeManagerWindow);
			dlg.setModal(false);
			dlg.pack();
			dlg.show();
		} else {
			PluginServices.getMDIManager().addWindow(fThemeManagerWindow);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return PluginServices.getText(this, "propiedades");
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
		return 11;
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
