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
package com.iver.cit.gvsig.gui.wizards;

import javax.swing.AbstractListModel;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class FormatListModel extends AbstractListModel {
    String[] formatos;

    /**
     * Creates a new FormatListModel object.
     *
     * @param f DOCUMENT ME!
     */
    public FormatListModel(String[] f) {
        formatos = f;
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return formatos.length;
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return formatos[index];
    }
}
