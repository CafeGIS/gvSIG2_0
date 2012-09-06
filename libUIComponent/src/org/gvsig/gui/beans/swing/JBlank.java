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
* $Id: JBlank.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.2  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.1  2006/10/23 09:53:07  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans.swing;

import java.awt.Dimension;

import javax.swing.JPanel;

public class JBlank extends JPanel {
  private static final long serialVersionUID = -8119213961484182678L;

	public JBlank(int width, int height) {
		super();
		Dimension dim = new Dimension(width, height);
		setSize(dim);
		setPreferredSize(dim);
	}
}
