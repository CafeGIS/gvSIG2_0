/*
 * Created on 20-sep-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
* $Id: ShowLayerErrorsTocMenuEntry.java 20994 2008-05-28 11:12:34Z jmvivo $
* $Log$
* Revision 1.4  2007-03-06 16:37:08  caballero
* Exceptions
*
* Revision 1.3  2007/02/20 15:52:17  caballero
* no modified
*
* Revision 1.2  2007/01/04 07:24:31  caballero
* isModified
*
* Revision 1.1  2006/09/21 18:26:13  azabala
* first version in cvs
*
*
*/
package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class ShowLayerErrorsTocMenuEntry extends AbstractTocContextMenuAction{

	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayer layer = getNodeLayer(item);
		String introductoryText = "<h2 text=\"#000080\">La capa "+
								layer.getName()+
								" presenta los siguientes errores</h2>";
		((View)PluginServices.getMDIManager().getActiveWindow()).
			getMapControl().
			getMapContext().
			reportDriverExceptions(introductoryText, layer.getErrors());
//		Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
//		project.setModified(true);
	}

	public String getText() {
		return PluginServices.getText(this, "ver_error_capa");
	}

	public String getGroup() {
		return "group3"; //FIXME
	}

	public int getGroupOrder() {
		return 30;
	}

	public int getOrder() {
		return 2;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return !getNodeLayer(item).isOk();
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)) {
			return true;
		}
		return false;

	}

}

