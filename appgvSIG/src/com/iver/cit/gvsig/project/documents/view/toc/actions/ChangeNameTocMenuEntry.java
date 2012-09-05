package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.gui.ChangeName;


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
 * $Id: ChangeNameTocMenuEntry.java 20994 2008-05-28 11:12:34Z jmvivo $
 * $Log$
 * Revision 1.2  2007-01-04 07:24:31  caballero
 * isModified
 *
 * Revision 1.1  2006/09/15 10:41:30  caballero
 * extensibilidad de documentos
 *
 * Revision 1.1  2006/09/12 15:58:14  jorpiell
 * "Sacadas" las opcines del menú de FPopupMenu
 *
 *
 */
/**
 * Realiza un cambio de nombre en la capa seleccionada
 *
 * @author Vicente Caballero Navarro
 */
public class ChangeNameTocMenuEntry extends AbstractTocContextMenuAction {
	public String getGroup() {
		return "group1"; //FIXME
	}

	public int getGroupOrder() {
		return 10;
	}

	public int getOrder() {
		return 1;
	}

	public String getText() {
		return PluginServices.getText(this, "cambio_nombre");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return selectedItems.length == 1;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return isTocItemBranch(item);
	}


	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayer lyr = getNodeLayer(item);
		if (!lyr.isAvailable()) return;
		ChangeName chn=new ChangeName(lyr.getName());
		PluginServices.getMDIManager().addWindow(chn);
		lyr.setName(chn.getName());
		Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		project.setModified(true);
	}
}
