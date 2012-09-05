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
package com.iver.utiles;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class DefaultCharSet implements SymbolSet {
    private HashSet caracteres = new HashSet();
    private ArrayList intervalos = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void addCharacter(char c) {
        caracteres.add(new Character(c));
    }

    /**
     * DOCUMENT ME!
     *
     * @param c1 DOCUMENT ME!
     * @param c2 DOCUMENT ME!
     */
    public void addInterval(char c1, char c2) {
        intervalos.add(new Interval(c1, c2));
    }

    /**
     * @see com.iver.utiles.SymbolSet#contains(char)
     */
    public boolean contains(char c) {
        if (caracteres.contains(new Character(c))) {
            return true;
        }

        for (int i = 0; i < intervalos.size(); i++) {
            if (((Interval) intervalos.get(i)).contains(c)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @author Fernando González Cortés
     */
    public class Interval implements SymbolSet {
        public int ini;
        public int fin;

        /**
         * Crea un nuevo Interval.
         *
         * @param c1 DOCUMENT ME!
         * @param c2 DOCUMENT ME!
         */
        public Interval(char c1, char c2) {
            ini = c1;
            fin = c2;
        }

        /**
         * @see com.iver.utiles.SymbolSet#contains(char)
         */
        public boolean contains(char c) {
            if ((ini <= c) && (c <= fin)) {
                return true;
            }

            return false;
        }
    }
}
