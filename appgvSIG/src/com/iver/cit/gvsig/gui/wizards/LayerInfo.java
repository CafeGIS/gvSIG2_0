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


//	Nodo[] nodos;
public class LayerInfo {
    public String text;
    public String name;
    public boolean queryable;
    private ArrayList srs = new ArrayList();
    
    public ArrayList hijos = new ArrayList();
    public LayerInfo padre;

    /**
     * DOCUMENT ME!
     *
     * @param srs DOCUMENT ME!
     */
    public void addSRS(String srs) {
    	String[] srsArray = srs.split(" ");
    	for (int i = 0; i < srsArray.length; i++){
			this.srs.add(srsArray[i]);
    	}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getSRSs() {
		ArrayList ret = new ArrayList();
		ret.addAll(srs);

        if (padre != null) {
            ret.addAll(padre.getSRSs());
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return text;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
    	try{
	        LayerInfo objeto = (LayerInfo) obj;
			return this.name.equals(objeto.name);
		}catch(ClassCastException e){
			e.printStackTrace();
			return false;
		}catch (NullPointerException e) {
			return false;
		}
    }
}
