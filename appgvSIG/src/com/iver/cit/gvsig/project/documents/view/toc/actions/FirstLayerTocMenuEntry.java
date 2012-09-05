package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;

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
 * $Id: FirstLayerTocMenuEntry.java 29055 2009-05-28 15:40:29Z vcaballero $
 * $Log$
 * Revision 1.3  2007-01-04 07:24:31  caballero
 * isModified
 *
 * Revision 1.2  2006/10/02 13:52:34  jaume
 * organize impots
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
 * Cambia la posición actual del layer a la primera posición.
 *
 * @author Vicente Caballero Navarro
 */
public class FirstLayerTocMenuEntry extends AbstractTocContextMenuAction {
	public String getGroup() {
		return "group4"; //FIXME
	}

	public int getGroupOrder() {
		return 40;
	}

	public int getOrder() {
		return 2;
	}

	public String getText() {
		return PluginServices.getText(this, "colocar_delante");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)){
			return true;
		}
		return false;

	}


	public void execute(ITocItem item, FLayer[] selectedItems) {
      	if (isTocItemBranch(item)){
            if (getMapContext().getLayers().getActives().length == 1) {
                FLayer layer=((TocItemBranch)item).getLayer();
                FLayers layers=layer.getParentLayer();
                for (int i=0;i<layers.getLayersCount();i++){
                    if(layers.getLayer(i).equals(layer)){
                    	layers.moveTo(layers.getLayersCount()-1-i, 0);
                    }
                }
            } else if (selectedItems.length > 1) {
                FLayer[] actives =selectedItems;
                FLayers layers=actives[0].getParentLayer();
                for (int i=0;i<actives.length;i++){
                    for (int j=0;j<layers.getLayersCount();j++){
                        if(layers.getLayer(j).equals(actives[i])){
                        	layers.moveTo(layers.getLayersCount()-1-j, 0);
                        }
                    }
                }
            }
		}
		// TRUCO PARA REFRESCAR.
        getMapContext().invalidate();
        Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		project.setModified(true);
		PluginServices.getMainFrame().enableControls();
	}
}
