
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
package org.gvsig.catalog.ui.showtree;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import next.swing.JTreeTable;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.utils.xmltreetable.XMLTreeTable;
import org.gvsig.catalog.utils.xmltreetable.XMLTreeTableModel;
import org.gvsig.i18n.Messages;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowTreePanel extends JPanel {
/**
 * 
 * 
 */
    private XMLTreeTable treeTable = null;
//  @jve:decl-index=0:
/**
 * 
 * 
 */
    private JScrollPane jScrollPane = null;
/**
 * 
 * 
 */
    private XMLNode node = null;

/**
 * This method initializes
 * 
 * 
 * @param node 
 * @param translator 
 */
    public  ShowTreePanel(XMLNode node) {        
        super();
        this.node = node;
        
        initialize();
    } 

/**
 * This method initializes this
 * 
 */
    private void initialize() {        
        this.setSize(900, 600);
        this.add(getJScrollPane(), null);
    } 

/**
 * This method initializes jTree
 * 
 * 
 * @return javax.swing.JTree
 */
    public JTreeTable getTreeTable() {        
        if (treeTable == null) {
            XMLTreeTableModel model = new XMLTreeTableModel(node);
            model.addJScrollPaneColumn("valueColumn", "tag-value", Messages.getText("value"), 0);
            model.addJScrollPaneColumn("attributeColumn", "attribute",
                    Messages.getText("attributes"), 0);
            treeTable = new XMLTreeTable(model);
            model.configureView(treeTable);
          
        }
        return treeTable;
    } 

/**
 * This method initializes jScrollPane
 * 
 * 
 * @return javax.swing.JScrollPane
 */
    private JScrollPane getJScrollPane() {        
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane(getTreeTable());
            jScrollPane.setPreferredSize(new java.awt.Dimension(775, 600));
            jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return jScrollPane;
    } 
 }
//  @jve:decl-index=0:visual-constraint="10,10"
