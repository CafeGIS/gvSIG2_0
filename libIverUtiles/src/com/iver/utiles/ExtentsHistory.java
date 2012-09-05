/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * Clase que representa un array circular de rect�ngulos
 *
 * @author Fernando Gonz�lez Cort�s
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
     *        rect�ngulos, por defecto 20
     */
    public ExtentsHistory(int numEntries) {
        NUMREC = numEntries;
    }

    /**
     * Pone un nuevo rect�ngulo al final del array
     *
     * @param ext Rect�ngulo que se a�ade al hist�rico
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
     * Devuelve true si hay alg�n rect�ngulo en el hist�rico
     *
     * @return true o false en caso de que haya o no haya rect�ngulos
     */
    public boolean hasPrevious() {
        return num > 0;
    }

    /**
     * Obtiene el �ltimo rect�ngulo que se a�adi� al hist�rico
     *
     * @return Ultimo rect�ngulo a�adido
     */
    public Rectangle2D.Double get() {
        Rectangle2D.Double ext = extents[num - 1];

        return ext;
    }

    /**
     * Devuelve el �ltimo rect�ngulo del hist�rico y lo elimina del mismo
     *
     * @return Ultimo rect�ngulo a�adido
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
