package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.gui.FPopupMenu;

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
 * $Id: OldTocContextMenuAction.java 20994 2008-05-28 11:12:34Z jmvivo $
 * $Log$
 * Revision 1.1  2006-09-15 10:41:30  caballero
 * extensibilidad de documentos
 *
 * Revision 1.1  2006/09/12 15:58:14  jorpiell
 * "Sacadas" las opcines del menú de FPopupMenu
 *
 *
 */
public class OldTocContextMenuAction extends AbstractTocContextMenuAction {
	private TocMenuEntry entry;

	public int getGroupOrder() {
		return 999;
	}

	public String getGroup() {
		return "zzzOldMenus";
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {
		return; //No se llega a ejecutar nunca
	}

	public String getText() {
		return null; //No se llega a ejecutar nunca
	}

	public void initializeElement(FPopupMenu menu) {
		this.entry.initialize(menu);
	}

	public void setEntry(TocMenuEntry entry) {
		this.entry = entry;
	}

	public TocMenuEntry getEntry() {
		return this.entry;
	}
}
