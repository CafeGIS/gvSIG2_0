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

/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class JEPUtilities {
    /**
     * Transforma una expresion en funcion de variables con nombre de la forma
     * xix  (donde i es un número cualquiera) en una expresión donde dichas
     * variables han sido sustituidas por los valores que se pasan como
     * parámetros.
     *
     * @param expression Expresion en funcion de xi
     * @param values Valores que toman las distintas variables x
     * @param cadena cadena[i] = true si xi es alfanumérica y false en caso
     *        contrario
     *
     * @return La nueva expresion
     */
    public static String fillExpression(String expression, String[] values,
        boolean[] cadena) {
        String expresionReal = expression;

        for (int i = 0; i < values.length; i++) {
            if (cadena[i]) {
                expresionReal = expresionReal.replaceAll("x" + i + "x",
                        "\"" + values[i] + "\"");
            } else {
                expresionReal = expresionReal.replaceAll("x" + i + "x",
                        values[i]);
            }
        }

        return expresionReal;
    }

    /*
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     *
       public static String getOrdinalValue(Object o, int type) {
               switch (type) {
               case FRecordset.STRING:
                       return (String) o;
               case FRecordset.DECIMAL:
               case FRecordset.INTEGER:
                       return ((Number) o).toString();
               case FRecordset.DATE:
                       return new Long(((Date) o).getTime()).toString();
               case FRecordset.BOOLEAN:
                       if (((Boolean) o).booleanValue()) {
                               return "1";
                       } else {
                               return "0";
                       }
               }
               throw new IllegalStateException("No se conoce el tipo del campo");
       }
     */
}
