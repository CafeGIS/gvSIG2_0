
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;

import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.utils.Frames;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowTreeDialog extends JDialog implements WindowListener {

/**
 * 
 * 
 */
    int currentNode;

/**
 * 
 * 
 * 
 * @param node 
 */
    public  ShowTreeDialog(XMLNode node) {        
        super();
        initialize(node);
    } 

/**
 * This method initializes jDialog
 * 
 * 
 * @param node 
 */
    private void initialize(org.gvsig.catalog.metadataxml.XMLNode node) {        
        Frames.centerFrame(this,800,675);
        this.setTitle("Cliente de Catálogo");
        ShowTreeDialogPanel panel = new ShowTreeDialogPanel(node);
        panel.setParent(this);
        getContentPane().add(panel);
        
        this.setVisible(true);
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowActivated(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowClosed(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowClosing(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowDeactivated(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowDeiconified(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowIconified(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
/* (non-Javadoc)
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */

/**
 * 
 * 
 * 
 * @param e 
 */
    public void windowOpened(WindowEvent e) {        
        // TODO Auto-generated method stub
    } 
 }
