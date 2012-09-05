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

import java.awt.geom.Rectangle2D;


/**
 * Clase que representa un array circular de rectángulos
 *
 * @author Fernando González Cortés
 */
public class ExtentsHistory {
    private int NUMREC;
    private Rectangle2D.Double[] extents;
    private int num = 0;

    /**
     * Creates a new ExtentsHistory object.
     */
    public ExtentsHistory() {
        NUMREC = 4;
        extents = new Rectangle2D.Double[NUMREC];
    }

    /**
     * Creates a new ExtentsHistory object.
     *
     * @param numEntries Numero de entradas que se guardan en el historico de
     *        rectángulos, por defecto 20
     */
    public ExtentsHistory(int numEntries) {
        NUMREC = numEntries;
    }

    /**
     * Pone un nuevo rectángulo al final del array
     *
     * @param ext Rectángulo que se añade al histórico
     */
    public void put(Rectangle2D.Double ext) {
        if ((ext != null) && ((num < 1) || (ext != extents[num - 1]))) {
            if (num < (NUMREC)) {
                extents[num] = ext;
                num = num + 1;
            } else {
                for (int i = 0; i < (NUMREC - 1); i++) {
                    extents[i] = extents[i + 1];
                }

                extents[num - 1] = ext;
            }
        }
    }

    /**
     * Devuelve true si hay algún rectángulo en el histórico
     *
     * @return true o false en caso de que haya o no haya rectángulos
     */
    public boolean hasPrevious() {
        return num > 0;
    }

    /**
     * Obtiene el último rectángulo que se añadió al histórico
     *
     * @return Ultimo rectángulo añadido
     */
    public Rectangle2D.Double get() {
        Rectangle2D.Double ext = extents[num - 1];

        return ext;
    }

    /**
     * Devuelve el último rectángulo del histórico y lo elimina del mismo
     *
     * @return Ultimo rectángulo añadido
     */
    public Rectangle2D.Double removePrev() {
        Rectangle2D.Double ext = extents[num - 1];
        num = num - 1;

        return ext;
    }
}

/*
   public class Extents{
           private final int NUMREC=20;
           private Rectangle2D.Double[] extents;
           private int num=0;
           public Extents(){
             extents = new Rectangle2D.Double[NUMREC];
           }
           public void put (Rectangle2D.Double ext){
           if((ext!=null)&&((num<1)||(ext!=extents[num-1]))){
            if (num<(NUMREC)){
             extents[num]=ext;
             num=num+1;
            }else{
                    for (int i=0;i<(NUMREC-1);i++){
                            extents[i]=extents[i+1];
                    }
                    extents[num-1]=ext;
            }
           }
           }

           public Rectangle2D.Double get(){
                   if(num>1){
                   Rectangle2D.Double ext = extents[num-2];
                     return ext;
                   }

             return null;
           }
           public Rectangle2D.Double getPrev(){
                           if(num>1){
                           Rectangle2D.Double ext = extents[num-2];
                           num=num-1;
                           return ext;
                           }

                     return null;
                   }
     }
 */
