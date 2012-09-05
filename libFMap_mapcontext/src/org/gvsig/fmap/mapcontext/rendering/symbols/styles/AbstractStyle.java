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
* $Id: AbstractStyle.java 20989 2008-05-28 11:05:57Z jmvivo $
* $Log$
* Revision 1.6  2007-09-19 16:19:27  jaume
* removed unnecessary imports
*
* Revision 1.5  2007/08/16 06:55:30  jvidal
* javadoc updated
*
* Revision 1.4  2007/08/13 11:36:30  jvidal
* javadoc
*
* Revision 1.3  2007/05/08 08:47:39  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.1  2007/02/09 07:47:04  jaume
* Isymbol moved
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols.styles;



/**
 * Implements the IStyle interface in order to complete the methods
 * ot the IStyle interface that allow the users to add the description of an
 * object or obtain it.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */

public abstract class AbstractStyle implements IStyle {
	private String desc;


	public final void setDescription(String desc) {
		this.desc = desc;
	}

	public final String getDescription() {
		return desc;
	}

}
