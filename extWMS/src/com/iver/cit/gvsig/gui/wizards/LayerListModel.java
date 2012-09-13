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

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;

import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;



/**
 * NO SE USA
 *
 * @author Fernando González Cortés
 */
public class LayerListModel extends AbstractListModel {
    private ArrayList nodos = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param elemento DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    
    public boolean addElement(WMSLayerNode elemento) {
        if (elemento == null) {
            return false;
        }

        for (int i = 0; i < nodos.size(); i++) {
            if (((WMSLayerNode) nodos.get(i)).equals(elemento)) {
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

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WMSLayerNode delElement(int index) {
        WMSLayerNode ret = (WMSLayerNode) nodos.remove(index);
        this.fireContentsChanged(this, index, index);

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void delElements(Collection c) {
        nodos.removeAll(c);
        this.fireContentsChanged(this, 0, nodos.size());
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return nodos.size();
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return ((WMSLayerNode) nodos.get(index)).getTitle();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WMSLayerNode[] getElements() {
        return (WMSLayerNode[]) nodos.toArray(new WMSLayerNode[0]);
    }


}
