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
 * <p><code>LayerListener</code> defines the interface for an object that listens
 *  to changes in a layer.</p>
 */
public interface LayerListener {
	/**
	 * <p>Called when the visibility of a layer has changed.</p>
	 * 
	 * @param e a visibility changed layer event object
	 */
	public void visibilityChanged(LayerEvent e);

	/**
	 * <p>Called when the activation of a layer has changed.</p>
	 * 
	 * @param e an activation changed layer event object
	 */
	public void activationChanged(LayerEvent e);

	/**
	 * <p>Called when the name of a layer has changed.</p>
	 * 
	 * @param e a name changed layer event object
	 */
	public void nameChanged(LayerEvent e);

	/**
	 * <p>Called when the edition of a layer has changed.</p>
	 * 
	 * @param e an edition changed layer event object
	 */
	public void editionChanged(LayerEvent e);

	/**
	 * <p>
	 * Called when a draw value of a layer has changed.
	 * </p>
	 * 
	 * @param e
	 *            an edition changed layer event object
	 */
	public void drawValueChanged(LayerEvent e);
}
