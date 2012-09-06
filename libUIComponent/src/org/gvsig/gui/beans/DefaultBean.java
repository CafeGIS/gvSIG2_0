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
* $Id: DefaultBean.java 17327 2007-12-12 07:45:23Z bsanchez $
* $Log$
* Revision 1.2  2007-08-21 08:38:21  bsanchez
* - Quitados warnings en imports innecesarios
*
* Revision 1.1  2007/08/20 08:34:45  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.4  2006/09/14 08:30:11  cesar
* Remove static initialization of gvsig-i18n; it's done in the Messages class now
*
* Revision 1.3.2.1  2006/09/14 07:55:48  cesar
* Remove static initialization of gvsig-i18n; it's done in the Messages class now
*
* Revision 1.3  2006/08/10 07:33:12  cesar
* *** empty log message ***
*
* Revision 1.2  2006/07/11 12:42:10  cesar
* Load properties for libUI
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
* Revision 1.2.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.3  2006/01/10 13:11:38  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2006/01/10 11:33:31  jaume
* Time dimension working against Jet Propulsion Laboratory's WMS server
*
* Revision 1.1.2.1  2005/12/30 08:56:19  jaume
* *** empty log message ***
*
*
*/
/**
 *
 */
package org.gvsig.gui.beans;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import org.gvsig.gui.beans.listeners.BeanListener;
/**
 * A JPanel with addListener(BeanListener l), removeListener(BeanListener l),
 * and callValueChange(Object value) methods for adding and removing listeners,
 * and firing BeanValueChange events.
 *
 * @author jaume
 */
public abstract class DefaultBean extends JPanel {
	ArrayList<BeanListener> listeners = new ArrayList<BeanListener>();

	public void addListener(BeanListener l) {
		listeners.add(l);
	}

	public void removeListener(BeanListener l) {
		listeners.remove(l);
	}

	public void callValueChanged(Object value) {
		Iterator<BeanListener> i = listeners.iterator();

		while (i.hasNext()) {
			i.next().beanValueChanged(value);
		}
	}
}