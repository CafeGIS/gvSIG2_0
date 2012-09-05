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

/* CVS MESSAGES:
 *
 * $Id: IZoomConstraints.java 10815 2007-03-20 16:16:20Z jaume $
 * $Log$
 * Revision 1.3  2007-03-20 16:16:20  jaume
 * refactored to use ISymbol instead of FSymbol
 *
 * Revision 1.2  2007/03/09 08:33:43  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2007/02/09 07:47:05  jaume
 * Isymbol moved
 *
 *
 */

package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import com.iver.utiles.IPersistence;

/**
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IZoomConstraints extends IPersistence{
	/**
	 * Uses the same constraints than the layer. Which is the same than no constraints.
	 */
	public static final int DEFINED_BY_THE_LAYER = 0;

	/**
	 * The user defines when the labes will be drawn.
	 * Notice that if the layer is not drawn it does not take effect.
	 */
	public static final int DEFINED_BY_THE_USER = 1;

	/**
	 * Sets the mode of the zoom constraints.
	 * @param mode, int one of DEFINED_BY_THE_LAYER or DEFINED_BY_THE_USER.
	 */
	public abstract void setMode(int mode);

	/**
	 * Returns the max scale limit of the constraints.
	 * @return   float, max limit
	 * @uml.property  name="maxScale"
	 */
	public abstract long getMaxScale();

	/**
	 * Sets the max scale limit of the constraints
	 * @param   maxScale, float
	 * @uml.property  name="maxScale"
	 */
	public abstract void setMaxScale(long maxScale);

	/**
	 * Returns the min scale limit of the constraints.
	 * @return   float, min limit
	 * @uml.property  name="minScale"
	 */
	public abstract long getMinScale();

	/**
	 * Sets the min scale limit of the constraints
	 * @param   minScale, float
	 * @uml.property  name="minScale"
	 */
	public abstract void setMinScale(long minScale);

	/**
	 * Returns <b>true</b> if the mode is DEFINED_BY_THE_USER.
	 * @return
	 */
	public abstract boolean isUserDefined();

	/**
	 * Returns <b>true</b> if the mode is DEFINED_BY_THE_USER.
	 * @return
	 */
	public abstract boolean isLayerDefined();

}