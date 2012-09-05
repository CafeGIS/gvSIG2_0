
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
package org.gvsig.catalog.ui.chooseresource;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.utils.resourcestable.ButtonEditor;
import org.gvsig.catalog.utils.resourcestable.ButtonRenderer;
import org.gvsig.i18n.Messages;


/**
 * this class represent the "choose resource frame". It is basically
 * contains a list with all the geo-resources that can be loaded. *
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ChooseResourceDialogPanel extends JPanel implements ActionListener {
//It is needed to close the frame
/**
 * 
 * 
 */
    private JFrame parent;
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
    private ChooseResourcePanel controlsPanel = null;
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
    private JButton cerrar = null;
//Others

/**
 * 
 * 
 */
    private java.util.Collection resources = new java.util.ArrayList();

/**
 * This method initializes
 * 
 * 
 * @param resources 
 * @param translator 
 */
    public  ChooseResourceDialogPanel(Collection resources) {        
               
        this.resources = resources;
          
        ppalPanel = new JPanel();
        ppalPanel.setLayout(new BoxLayout(ppalPanel, BoxLayout.Y_AXIS));
        
        ppalPanel.add(getControlsPanel(), null);
        ppalPanel.add(getButtonPanel(), null);
        
        
        add(ppalPanel);
        setDefaultButtonListeners();
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public ChooseResourcePanel getControlsPanel() {        
        if (controlsPanel == null) {
            controlsPanel = new ChooseResourcePanel(resources);
            controlsPanel.setPreferredSize(new java.awt.Dimension(575, 110));
            controlsPanel.setLocation(0, 0);
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
        if (cerrar == null) {
            cerrar = new JButton(Messages.getText("close"));
            cerrar.setPreferredSize(new Dimension(80, 23));
            cerrar.setActionCommand("close");
        }
        return cerrar;
    } 

/**
 * 
 * 
 */
    public void setDefaultButtonListeners() {        
        getCloseButton().addActionListener(this);
        getControlsPanel().getTable().getColumn(Messages.getText("resourceShowColumn")).setCellRenderer(new ButtonRenderer());
        getControlsPanel().getTable().getColumn(Messages.getText("resourceShowColumn")).setCellEditor(new ButtonEditor(new JCheckBox(),resources,this));
        
    } 

/**
 * 
 * 
 * 
 * @param e 
 */
    public void actionPerformed(ActionEvent e) {        
        //Cerrar
        if (e.getActionCommand() == "close") {
           closeButtonActionPerformed();
        }   
        System.out.println(e.getActionCommand());
        
    } 

/**
 * 
 * 
 * 
 * @param resource 
 */
    public void resourceButtonActionPerformed(Resource resource) {        
        System.out.println(resource.getLinkage() + " no se puede cargar sin usar gvSIG");
    } 

/**
 * 
 * 
 */
    public void closeButtonActionPerformed() {        
       parent.setVisible(false);
    } 

/**
 * 
 * 
 * 
 * @param parent The parent to set.
 */
    public void setParent(JFrame parent) {        
        this.parent = parent;
    } 
 }
