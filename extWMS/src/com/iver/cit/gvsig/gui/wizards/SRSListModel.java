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
import java.util.TreeSet;

import javax.swing.AbstractListModel;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class SRSListModel extends AbstractListModel {
	ArrayList srs = new ArrayList();
	
	public int getSize() {
		return srs.size();
	}

	public Object getElementAt(int index) {
		return srs.get(index);
	}
	
	public void setAll(Collection c) {
		
		srs.clear();
		srs.addAll(c);
	}
	
	public Collection intersect(Collection c) {
		TreeSet resul = new TreeSet();
    	for (int i = 0; i < srs.size(); i++) {
			if (c.contains(srs.get(i)))
				resul.add(srs.get(i));
		}
    	return resul;	
	}
	/*ArrayList comunes = new ArrayList();
	
    private HashSet srsSet = new HashSet();
	private HashMap cuenta = new HashMap();
	private ArrayList nombresSRS = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param srs DOCUMENT ME!
     * /
    public void add(String srs) {
    	System.out.println("Añadiendo " + srs);
        if (!srsSet.add(srs)) {
            Integer i = (Integer) cuenta.get(srs);
            cuenta.put(srs, new Integer(i.intValue() + 1));
            System.out.println("Ya había, llevamos " + i.intValue() + 1);
        } else {
			nombresSRS.add(srs);
            cuenta.put(srs, new Integer(1));
			fireContentsChanged(this, nombresSRS.size()-1,nombresSRS.size()-1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param srs DOCUMENT ME!
     * /
    public void del(String srs) {
    	System.out.println("Eliminando SRS: "+ srs);
        Integer i = (Integer) cuenta.get(srs);

        if (i == null) {
        	System.out.println("Eliminando un elemento que no estaba");
            return;
        }

        int nuevo = i.intValue() - 1;

		System.out.println("quedan " + nuevo);
		
        if (nuevo == 0) {
            cuenta.remove(srs);
            srsSet.remove(srs);
            nombresSRS.remove(srs);
			fireContentsChanged(this, 0,nombresSRS.size()-1);
        } else {
            cuenta.put(srs, new Integer(nuevo));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     * /
    public Iterator iterator() {
        return srsSet.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     * /
    public int size() {
        return srsSet.size();
    }

	/**
	 * @see javax.swing.ListModel#getSize()
	 * /
	public int getSize() {
		return nombresSRS.size();
	}

	/**
	 * @see javax.swing.ListModel#getElementAt(int)
	 * /
	public Object getElementAt(int index) {
		return nombresSRS.get(index);
	}
	
	public ArrayList intersect(Collection col) {
    	ArrayList resul = new ArrayList();
    	for (int i = 0; i < nombresSRS.size(); i++) {
			if (col.contains(nombresSRS.get(i)))
				resul.add(nombresSRS.get(i));
		}
    	return resul;
    }
    */
}
