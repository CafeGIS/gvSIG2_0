/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package com.iver.cit.gvsig.gui.wcs;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;

import com.iver.cit.gvsig.fmap.layers.WCSLayer;


/**
 * DOCUMENT ME!
 *
 * @author Jaume - jaume.dominguez@iver.es
 */
public class CoverageListModel extends AbstractListModel {
    private ArrayList nodos = new ArrayList();


    public boolean addElement(WCSLayer elemento) {
        if (elemento == null) {
            return false;
        }

        for (int i = 0; i < nodos.size(); i++) {
            if (((WCSLayer) nodos.get(i)).equals(elemento)) {
                return false;
            }
        }

        nodos.add(elemento);

        fireContentsChanged(this, nodos.size() - 1, nodos.size() - 1);

        return true;
    }

	public void clear(){
		nodos.clear();
		fireContentsChanged(this, 0, 0);
	}


    public WCSLayer delElement(int index) {
    	WCSLayer ret = (WCSLayer) nodos.remove(index);
        this.fireContentsChanged(this, index, index);

        return ret;
    }

    public void delElements(Collection c) {
        nodos.removeAll(c);
        this.fireContentsChanged(this, 0, nodos.size());
    }

    public int getSize() {
        return nodos.size();
    }


    public Object getElementAt(int index) {
        return ((WCSLayer) nodos.get(index));
    }


    public WCSLayer[] getElements() {
        return (WCSLayer[]) nodos.toArray(new WCSLayer[0]);
    }


    public WCSLayer getLayerInfo(int index) {
        return (WCSLayer) nodos.get(index);
    }
}
