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
 */
package com.iver.utiles.listManager;

import java.util.Vector;

import javax.swing.AbstractListModel;
/**
 * Implementación por defecto de ListModel
 *
 * @author Fernando González Cortés
 */
public class DefaultListModel extends AbstractListModel implements ListModel {
    private Vector v;
    private boolean down=true;
    /**
     * Crea un nuevo DefaultListModel con un parámetro que indica si la lista
     * se crea en el orden que se van introcuciendo(true) o al contrario.
     */
    public DefaultListModel(boolean down) {
        v = new Vector();
        this.down=down;
    }

    /**
     * @see com.iver.utiles.listManager.ListModel#remove(int)
     */
    public Object remove(int i) throws ArrayIndexOutOfBoundsException {
        Object o = v.remove(i);
        super.fireIntervalRemoved(this, i, i);

        return o;
    }

    /**
     * @see com.iver.utiles.listManager.ListModel#insertAt(int,
     *      java.lang.Object)
     */
    public void insertAt(int i, Object o) {
        v.add(i, o);
        super.fireIntervalAdded(this, i, i);
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return v.size();
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int arg0) {
        return v.get(arg0);
    }

    /**
     * @see com.iver.utiles.listManager.ListModel#add(java.lang.Object)
     */
    public void add(Object o) {
      if (down) {
        v.add(o);
        super.fireIntervalAdded(this, v.size() - 1, v.size() - 1);
      } else {
        insertAt(0, o);
      }
    }

	/**
	 * @see com.iver.utiles.listManager.ListModel#getObjects()
	 */
	public Vector getObjects() {
		return v;
	}
}
