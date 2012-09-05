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
package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableEditStartExtension extends AbstractTableEditExtension {

    /**
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
    	if ("STARTEDIT".equals(actionCommand)) {
    		try {
				table.getModel().getStore().edit(FeatureStore.MODE_FULLEDIT);
			} catch (DataException e) {
				e.printStackTrace();
			}
       	}
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();

        if (v == null) {
            return false;
        }
        if (v instanceof FeatureTableDocumentPanel)
        {
        	FeatureTableDocumentPanel t = (FeatureTableDocumentPanel) v;
	    	FeatureStore fs = t.getModel().getStore();
	    	// FJP:
	    	// Si está linkada, por ahora no dejamos editar
	    	// TODO: Esto evita la edición en un sentido, pero no en el otro
	    	// Hay que permitir la edición, pero evitar que toquen el/los
	    	// campos de unión. Para eso tendremos que añadir alguna función
	    	// que indique si un campo está involucrado en alguna unión, o
	    	// quizás algo más genérico, algo que permita bloquear campos
	    	// para que no se puedan editar.
	    	if (t.getModel().getLinkTable() != null) {
				return false;
			}
//	    	if (ies.getOriginalDriver() instanceof IWriteable)
//	    	{
//	    		return true;
//	    	}
	    	return fs.allowWrite();
        }
    	return false;
    }

    /**
     * @see com.iver.andami.plugins.IExtension#isVisible()
     */
    public boolean isVisible() {
        IWindow v = PluginServices.getMDIManager().getActiveWindow();

        if (v == null) {
            return false;
        }

        if (v instanceof FeatureTableDocumentPanel && !((FeatureTableDocumentPanel) v).getModel().getStore().isEditing() && ((FeatureTableDocumentPanel)v).getModel().getAssociatedLayer()==null) {
       		table=(FeatureTableDocumentPanel)v;
        	return true;
        }

        return false;
    }
}
