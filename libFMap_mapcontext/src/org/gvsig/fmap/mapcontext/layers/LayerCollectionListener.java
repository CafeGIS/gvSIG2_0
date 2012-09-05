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
package org.gvsig.fmap.mapcontext.layers;


/**
 * <p><code>LayerCollectionListener</code> defines listeners for events that can be produced in a
 *  collection of layers.</p>
 */
public interface LayerCollectionListener {
    /**
 	 * <p>Called when a layer has been added or replaced in the internal list of layers.</p>
     *
     * @param e a layer collection event object
     */
    void layerAdded(LayerCollectionEvent e);

    /**
 	 * <p>Called when a layer has been moved in the internal list of layers.</p>
     *
     * @param e a layer collection event object
     */
    void layerMoved(LayerPositionEvent e);

    /**
  	 * <p>Called when a layer has been removed from the internal list of layers.</p>
     *
     * @param e a layer collection event object
     */
    void layerRemoved(LayerCollectionEvent e);

    /**
 	 * <p>Called when a layer is just going to be added or replaced in the internal list of layers.</p>
     *
     * @param e a layer collection event object
     *
     * @throws CancelationException if cancels the adding operation, this exception will have the message
     *  that user will see.
     */
    void layerAdding(LayerCollectionEvent e) throws CancelationException;

    /**
 	 * <p>Called when a layer is just going to be moved in the internal list of layers.</p>
     *
     * @param e a layer collection event object
     *
     * @throws CancelationException if cancels the moving operation, this exception will have the message
     *  that user will see.
     */
    void layerMoving(LayerPositionEvent e) throws CancelationException;

    /**
 	 * <p>Called when a layer is just going to be removed from the internal list of layers.</p>
     *
     * @param e a layer collection event object
     *
     * @throws CancelationException if cancels the removing operation, this exception will have the message
     *  that user will see.
     */
    void layerRemoving(LayerCollectionEvent e) throws CancelationException;


    ///**
    // * <p>Called when the activation state of the collection of layers has changed.</p>
    // *
    // * @param e a layer collection event object
    // *
    // * @throws CancelationException Si se quiere cancelar la operación. El
    // *         mensaje de la excepción es el que se mostrará al usuario en su
    // *         caso
    // */
    //  useless. Please, use LayerListener interface instead
    // void activationChanged(LayerCollectionEvent e) throws CancelationException;

    /**
 	 * <p>Called when the visibility of the collection of layers has changed.</p>
     *
     * @param e a layer collection event object
     *
     * @throws CancelationException if cancels the operation of change visibility, this exception will have the
     *  message that user will see.
     */
    void visibilityChanged(LayerCollectionEvent e) throws CancelationException;
}
