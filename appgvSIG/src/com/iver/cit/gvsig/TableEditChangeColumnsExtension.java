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

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class TableEditChangeColumnsExtension extends AbstractTableEditExtension {

    /**
     * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
     */
    public void execute(String actionCommand) {
    	try {
    		featureTableOperations.setStore(table.getModel().getStore());
    		if ("REMOVECOLUMN".equals(actionCommand)) {
    			featureTableOperations.deleteAttributes(table.getTablePanel().getTable());
    		}else if ("ADDCOLUMN".equals(actionCommand)) {
    			featureTableOperations.insertAttributes(table.getTablePanel().getTable());
    		}else if ("RENAMECOLUMN".equals(actionCommand)) {
    			featureTableOperations.renameAttributes(table.getTablePanel().getTable());
    		}
    	} catch (DataException e) {
    		NotificationManager.showMessageError(PluginServices.getText(this, "update_featuretype_error"), null);
    	}

    }

    /**
     * @see com.iver.andami.plugins.IExtension#isEnabled()
     */
    public boolean isEnabled() {
        try {
			if (table.getTablePanel().getTable().getSelectedColumnCount()>0){
				return true;
			}
		} catch (DataException e) {
			return false;
		}
		return false;
	}
}
