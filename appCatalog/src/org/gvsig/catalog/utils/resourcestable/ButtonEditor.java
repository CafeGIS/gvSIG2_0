
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
package org.gvsig.catalog.utils.resourcestable;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.ui.chooseresource.ChooseResourceDialogPanel;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ButtonEditor extends DefaultCellEditor {
/**
 * 
 * 
 */
    protected JButton button;

/**
 * 
 * 
 */
    private String label;

/**
 * 
 * 
 */
    private boolean isPushed;
/**
 * 
 * 
 */
    private ChooseResourceDialogPanel dialog;

/**
 * 
 * 
 */
    private java.util.Collection resources = new java.util.ArrayList();

/**
 * 
 * 
 * 
 * @param checkBox 
 * @param resources 
 * @param dialog 
 */
    public  ButtonEditor(JCheckBox checkBox, Collection resources, ChooseResourceDialogPanel dialog) {        
      super(checkBox);
      this.resources = resources;
      this.dialog = dialog;
      button = new JButton();
      button.setOpaque(true);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          fireEditingStopped();
        }
      });
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param table 
 * @param value 
 * @param isSelected 
 * @param row 
 * @param column 
 */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {        
      if (isSelected) {
        button.setForeground(table.getSelectionForeground());
        button.setBackground(table.getSelectionBackground());
      } else{
        button.setForeground(table.getForeground());
        button.setBackground(table.getBackground());
      }
      label = (value == null) ? "" : value.toString();
      button.setText( label );
      button.setName(String.valueOf(row));
      isPushed = true;
      return button;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public Object getCellEditorValue() {        
      if (isPushed)  {
        Resource resource = (Resource)resources.toArray()[Integer.valueOf(button.getName()).intValue()];
        this.dialog.resourceButtonActionPerformed(resource);
        
      }
      isPushed = false;
      return new String(label) ;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public boolean stopCellEditing() {        
      isPushed = false;
      return super.stopCellEditing();
    } 

/**
 * 
 * 
 */
    protected void fireEditingStopped() {        
      super.fireEditingStopped();
    } 
 }
