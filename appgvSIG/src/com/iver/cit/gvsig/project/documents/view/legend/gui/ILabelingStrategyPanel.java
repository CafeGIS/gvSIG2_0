/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.ILabelingStrategy;


/**
 * Interface to be implemented for those panels whose purpose is to
 * set up labeling strategies.
 *
 * ILabelingStrategyPanel.java
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es Jun 13, 2008
 *
 */
public interface ILabelingStrategyPanel {
	/**
	 * Invoked when de user accepts the settings, the returned value
	 * is a always ready-to-work labeling strategy.
	 *
	 * @return ILabelingStrategy, a labeling strategy completely set up.
	 */
	public ILabelingStrategy getLabelingStrategy();

	/**
	 * Sets the model of the panel. As a general contract, after invoke
	 * this method, the panel should be filled with all the values
	 * needed and be able to be repainted successfully.
	 *
	 * @param layer, the layer to whom the labeling strategy is associated.
	 * @param str, the current labeling strategy.
	 */
	public void setModel(FLayer layer, ILabelingStrategy str);

	/**
	 * A human-readable localized text to make this strategy easily
	 * recognizable among all the available strategies.
	 * @return
	 */
	public String getLabelingStrategyName();

	/**
	 * <p>
	 * The panel shown in the layer properties dialog is selected through
	 * the class of the labeling strategy. This method returns such class.
	 * <br>
	 * </p>
	 * <p>
	 * <b>Note</b> that, as a collateral effect, the panel that sets up a labeling strategy
	 * can be substituted programatically by other of your wish if
	 * you return the same labeling strategy class than the one to
	 * be replaced with your new one. The last panel installed, will
	 * be the one to be shown.
	 * </p>
	 * @return Class, the labeling strategy's Class
	 */
	public Class getLabelingStrategyClass();
}
