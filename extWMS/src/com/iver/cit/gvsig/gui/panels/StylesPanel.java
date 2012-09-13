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
* $Id: StylesPanel.java 6500 2006-07-21 11:51:13Z jaume $
* $Log$
* Revision 1.7  2006-07-21 11:51:13  jaume
* improved appearance in wms panel and a wmc bug fixed
*
* Revision 1.6  2006/06/30 11:05:36  jaume
* improved appearance
*
* Revision 1.5  2006/03/27 15:10:06  jaume
* images moved to libUI, and some code clean up
*
* Revision 1.4  2006/02/28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.1.2.5  2006/02/17 12:57:34  jaume
* oculta/eXconde los nombres de las capas y además corrige el error de selección de varios styles si hay alguna capa seleccionada repetida
*
* Revision 1.1.2.4  2006/02/06 15:19:50  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.3  2006/01/31 10:40:31  jaume
* *** empty log message ***
*
* Revision 1.2  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.1  2006/01/25 09:08:53  jaume
* test save and reload project
*
*
*/
package com.iver.cit.gvsig.gui.panels;

import javax.swing.JScrollPane;

import org.gvsig.gui.beans.DefaultBean;


/**
 * This is the Styles tab of the WMS wizard.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
class StylesPanel extends DefaultBean{
    private JScrollPane jScrollPane = null;
    private StyleTree treeStyles = null;
    private WMSParamsPanel parent;

    public StylesPanel(WMSParamsPanel parent){
        super();
        setLayout(null);
        add(getJScrollPane(), null);
        this.parent = parent;
    }
    /**
     * This method initializes jScrollPane3
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(7, 10, 483, 367);
            jScrollPane.setViewportView(getStyleTree());
        }
        return jScrollPane;
    }

    /**
     * This method initializes treeStyles
     *
     * @return javax.swing.JTree
     */
    public StyleTree getStyleTree() {
        if (treeStyles == null) {
            treeStyles = new StyleTree();
            treeStyles.addMouseListener(new java.awt.event.MouseAdapter() {
            	public void mouseClicked(java.awt.event.MouseEvent e) {

            		StyleTreeModel model = (StyleTreeModel) treeStyles.getModel();
            		//callValueChanged(model.getStylesSelection(/*parent.selectedLayersToArray()*/));
            		callValueChanged(null);
            	}
            });
        }
        return treeStyles;
    }
}