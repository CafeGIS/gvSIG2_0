package com.iver.cit.gvsig.project.documents.view.toc.actions;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
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
 * $Id: EliminarCapaTocMenuEntry.java 24964 2008-11-11 17:56:07Z vcaballero $
 * $Log$
 * Revision 1.8  2007-08-29 10:52:01  nacho
 * Bug en eliminar capa
 *
 * Revision 1.7  2007/08/29 10:44:00  nacho
 * Comprobación de null para que no salte la consola en caso de que la capa no esté seleccionada
 *
 * Revision 1.6  2007/08/21 11:47:26  nacho
 * Cerrar cuadros asociados a la capa al eliminar esta
 *
 * Revision 1.5  2007/06/06 15:53:09  jmvivo
 * Added check for edition layers: It cannot be removed
 *
 * Revision 1.4  2007/03/06 16:37:08  caballero
 * Exceptions
 *
 * Revision 1.3  2007/01/04 07:24:31  caballero
 * isModified
 *
 * Revision 1.2  2006/12/19 09:09:07  jmvivo
 * Faltaba un beginAtomicEvent para que no refrescase cada vez
 *
 * Revision 1.1  2006/09/15 10:41:30  caballero
 * extensibilidad de documentos
 *
 * Revision 1.1  2006/09/12 15:58:14  jorpiell
 * "Sacadas" las opcines del menú de FPopupMenu
 *
 *
 */
public class EliminarCapaTocMenuEntry extends AbstractTocContextMenuAction {
	public String getGroup() {
		return "group3"; //FIXME
	}

	public int getGroupOrder() {
		return 30;
	}

	public int getOrder() {
		return 0;
	}

	public String getText() {
		return PluginServices.getText(this, "eliminar_capa");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)) {
			return true;
		}
		return false;

	}


	public void execute(ITocItem item, FLayer[] selectedItems) {
    	// 050209, jmorell: Para poder borrar todas las capas seleccionadas desde el FPopUpMenu.
		//					Es necesario pulsar Mayúsculas a la vez que el botón derecho.

    	FLayer[] actives = selectedItems;

    	int i;
    	for (i= 0; i < actives.length;i++){
    		if (actives[i].isEditing() && actives[i].isAvailable()){
    			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this,"no_se_puede_borrar_una_capa_en_edicion"), PluginServices.getText(this, "eliminar_capa"), JOptionPane.WARNING_MESSAGE);
    			return;
    		}
    	}

    	int option=-1;
    	if (actives.length>0) {
    		option=JOptionPane.showConfirmDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"desea_borrar_la_capa"));
    	} else {
    		return;
    	}
    	if (option!=JOptionPane.OK_OPTION)
    		return;
    	getMapContext().beginAtomicEvent();
    	for (i = actives.length-1; i>=0; i--){
        	try {
				//actives[i].getParentLayer().removeLayer(actives[i]);
				//FLayers lyrs=getMapContext().getLayers();
				//lyrs.addLayer(actives[i]);
				actives[i].getParentLayer().removeLayer(actives[i]);

                if (actives[i] instanceof FLyrVect){
                    Project project = ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
                    FeatureTableDocument pt = project.getTable((FLyrVect) actives[i]);

                    ArrayList tables = project.getDocumentsByType(FeatureTableDocumentFactory.registerName);
                    for (int j = 0; j < tables.size(); j++) {
                        if (tables.get(j) == pt){
                        	project.delDocument((ProjectDocument)tables.get(j));
                            break;
                        }
                    }

                    PluginServices.getMDIManager().closeSingletonWindow(pt);
                }

                //Cierra todas las ventanas asociadas a la capa
        		IWindow[] wList = PluginServices.getMDIManager().getAllWindows();
        		for (int j = 0; j < wList.length; j++) {
        			String name = wList[j].getWindowInfo().getAdditionalInfo();
        			for (int k = 0; k < actives.length; k++) {
        				if( name != null && actives != null && actives[k] != null &&
                			actives[k].getName() != null &&
                			name.compareTo(actives[k].getName()) == 0)
                			PluginServices.getMDIManager().closeWindow(wList[j]);
					}
        		}

    		} catch (CancelationException e1) {
    			e1.printStackTrace();
    		}
    	}
    	getMapContext().endAtomicEvent();
    	Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		project.setModified(true);
    	PluginServices.getMainFrame().enableControls();
		// 050209, jmorell: Así solo borra una capa (sobre la que pulsas).
    	/*FLayer lyr = getNodeLayer();
        try {
        	getMapContext().getLayers().removeLayer(lyr);
        	if (getMapContext().getLayers().getLayersCount()==0)PluginServices.getMainFrame().enableControls();
		} catch (CancelationException e1) {
			e1.printStackTrace();
		}*/
    }
}