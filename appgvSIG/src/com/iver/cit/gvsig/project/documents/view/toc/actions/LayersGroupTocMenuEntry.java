package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;

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
 * $Id: LayersGroupTocMenuEntry.java 20994 2008-05-28 11:12:34Z jmvivo $
 * $Log$
 * Revision 1.7  2007-06-19 08:42:17  jcampos
 * New method to get new group layers
 *
 * Revision 1.6  2007/02/15 11:04:54  caballero
 * cancelar agrupación
 *
 * Revision 1.5  2007/02/14 17:09:43  caballero
 * posición layerGroup
 *
 * Revision 1.4  2007/01/04 07:24:31  caballero
 * isModified
 *
 * Revision 1.3  2006/10/02 13:52:34  jaume
 * organize impots
 *
 * Revision 1.2  2006/09/25 15:21:47  jmvivo
 * * Modificada la condicion de visibilidad, no permitir si no tienen el mismo padre.
 * * Modificada la implementacion.
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
 * Realiza una agrupación de capas, a partir de las capas que se encuentren activas.
 *
 * @author Vicente Caballero Navarro
 */
public class LayersGroupTocMenuEntry extends AbstractTocContextMenuAction {
	public String getGroup() {
		return "group4"; //FIXME
	}

	public int getGroupOrder() {
		return 40;
	}

	public int getOrder() {
		return 0;
	}

	public String getText() {
		return PluginServices.getText(this, "agrupar_capas");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return selectedItems.length > 1;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (selectedItems.length < 2) {
			return false;
		}
		FLayers parent = selectedItems[0].getParentLayer();
		for (int i = 1; i < selectedItems.length;i++){
			if (parent != selectedItems[i].getParentLayer()){
				return false;
			}
		}
		return true;

	}


	public void execute(ITocItem item, FLayer[] selectedItems) {
		//ITocItem tocItem = (ITocItem) getNodeUserObject();
		ChangeName changename=new ChangeName(null);
		PluginServices.getMDIManager().addWindow(changename);
		if (!changename.isAccepted())
			return;
		String nombre=changename.getName();

		if (nombre != null){

			getMapContext().beginAtomicEvent();
			FLayers parent = selectedItems[0].getParentLayer();
//			FLayers newGroup = new FLayers(getMapContext(),parent);
			FLayers newGroup = getMapContext().getNewGroupLayer(parent);
			newGroup.setName(nombre);
			int pos=0;
			for (int i=0;i<parent.getLayersCount();i++){
				if (parent.getLayer(i).equals(selectedItems[0])){
					pos=i;
					continue;
				}
			}
			for (int j=0;j < selectedItems.length;j++){
				FLayer layer = selectedItems[j];
				parent.removeLayer(layer);
				newGroup.addLayer(layer);
			}
			parent.addLayer(pos,newGroup);

			getMapContext().endAtomicEvent();
			// TRUCO PARA REFRESCAR.
			getMapContext().invalidate();
			Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
			project.setModified(true);
		}
	}
}
