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
* $Id: BeanListener.java 13136 2007-08-20 08:38:34Z evercher $
* $Log$
* Revision 1.1  2007-08-20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.2  2006/07/03 09:29:09  jaume
* javadoc
*
* Revision 1.1  2006/03/22 11:18:29  jaume
* *** empty log message ***
*
* Revision 1.4  2006/02/28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.3  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2006/01/26 12:59:33  jaume
* 0.5
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.1  2006/01/10 13:11:38  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2006/01/02 18:08:01  jaume
* Tree de estilos
*
* Revision 1.1.2.1  2005/12/30 08:56:19  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans.listeners;

/**
 * Defines an object which listens for changes in the value produced by a bean.
 *
 * @author jaume dominguez faus
 */
public interface BeanListener {
    /**
     * Invoked when the target of the listener has changed its value.
     * @param value, the new value obtained from the target.
     */
    public void beanValueChanged(Object value);
}
