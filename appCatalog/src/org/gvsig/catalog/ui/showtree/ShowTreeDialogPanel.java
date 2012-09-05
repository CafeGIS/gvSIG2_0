
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.i18n.Messages;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowTreeDialogPanel extends JPanel implements ActionListener {
//It is needed to close the frame
/**
 * 
 * 
 */
    private JDialog parent;
//Panels
/**
 * 
 * 
 */
    private JPanel ppalPanel = null;
/**
 * 
 * 
 */
    private ShowTreePanel controlsPanel = null;
/**
 * 
 * 
 */
    private JPanel buttonsPanel = null;
//Buttons
/**
 * 
 * 
 */
    protected JButton close = null;

/**
 * 
 * 
 * 
 * @param node 
 * @param translator 
 */
    public  ShowTreeDialogPanel(XMLNode node) {
        ppalPanel = new JPanel();
        ppalPanel.setLayout(new BoxLayout(ppalPanel, BoxLayout.Y_AXIS));
        ppalPanel.add(getControlsPanel(node), null);
        ppalPanel.add(getButtonPanel(), null);
        add(ppalPanel);
        setDefaultButtonListeners();
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public JPanel getControlsPanel(XMLNode node) {        
        if (controlsPanel == null) {
            controlsPanel = new ShowTreePanel(node);
        }
        return controlsPanel;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public JPanel getButtonPanel() {        
        if (buttonsPanel == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
        	buttonsPanel = new JPanel(flowLayout);
            buttonsPanel.add(getCloseButton());
        }
        return buttonsPanel;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public JButton getCloseButton() {        
        if (close == null) {
            close = new JButton(Messages.getText("close"));
            close.setPreferredSize(new Dimension(80, 23));
            close.setActionCommand("close");
        }
        return close;
    } 

/**
 * 
 * 
 */
    public void setDefaultButtonListeners() {        
        getCloseButton().addActionListener(this);
    } 

/**
 * 
 * 
 */
    public void closeButtonActionPerformed() {        
        parent.setVisible(false);
    } 
/* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void actionPerformed(ActionEvent e) {        
        //Buscar
        if (e.getActionCommand() == "close") {
            closeButtonActionPerformed();
        }
    } 

/**
 * 
 * 
 * 
 * @param parent The parent to set.
 */
    public void setParent(JDialog parent) {        
        this.parent = parent;
    } 
 }
