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
package com.iver.utiles.listManager;

/**
 * Interfaz listener de los eventos que lanza el control listManager
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface ListManagerListener {
    /**
     * Invocado cuando se pulsa el bot�n de a�adir, deber� retornar el objeto
     * que se a�ade y se mostrar� en la lista el resultado de su m�todo
     * toString(). Si implementa la interfaz Propertiable se habilitar� el
     * control Properties cuando se seleccione dicho elemento
     *
     * @return Objetos que se a�ade.
     */
    public Object[] addObjects();

    /**
     * Invocado cuando se selecciona un elemento Propertiable de la lista y
     * luego se pincha en el boton de propiedades
     *
     * @param selected Objeto seleccionado
     *
     * @return retornar� las propiedades de dicho objeto
     */
    public Object getProperties(Object selected);
}
