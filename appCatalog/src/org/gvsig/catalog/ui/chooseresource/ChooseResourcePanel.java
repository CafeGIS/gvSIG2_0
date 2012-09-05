
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
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.catalog.utils.resourcestable.TableModel;
import org.gvsig.i18n.Messages;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ChooseResourcePanel extends JPanel {
/**
 * 
 * 
 */
    private JTable table = null;
/**
 * 
 * 
 */
    private JScrollPane tablePane = null;

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
    public  ChooseResourcePanel(Collection resources) {        
		super();
		this.resources = resources; 
		initialize();
    } 

/**
 * It creates the table model
 * 
 * 
 * @return 
 */
    private TableModel createTableModel() {        
	    Object[][] columnValues = new Object[resources.size()][3];
	    String[] columnNames = {Messages.getText("resourceTypeColumn"),
	            Messages.getText("resourceLinkColumn"),
	            Messages.getText("resourceShowColumn")};	    
	    
	    for (int i=0 ; i<resources.size() ; i++){
	        Resource resource = (Resource)resources.toArray()[i];
            columnValues[i][0] = resource.getProtocol();
	        columnValues[i][1] = resource.getLinkage();
	        columnValues[i][2] = getNameButton(resource.getProtocol());
	   }
	   
	    return new TableModel(columnValues,columnNames);
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param protocol 
 */
    private String getNameButton(String protocol) {        
	    if (protocol.toUpperCase().indexOf(Resource.WCS) >= 0)
			return  Messages.getText("wcsColumn");
		
		if (protocol.toUpperCase().indexOf(Resource.WMS) >= 0)
			return Messages.getText("wmsColumn");
		
		if (protocol.toUpperCase().indexOf(Resource.WFS) >= 0)
			return Messages.getText("wfsColumn");
		
		if (protocol.toUpperCase().indexOf(Resource.POSTGIS) >= 0)
			return Messages.getText("postgisColumn");
		
		if (protocol.toUpperCase().indexOf(Resource.WEBSITE) >= 0)
		    return Messages.getText("linkColumn");
		
		if (protocol.toUpperCase().indexOf(Resource.DOWNLOAD) >= 0)
		    return Messages.getText("downloadColumn");
		
		if (protocol.toUpperCase().indexOf(Resource.ARCIMS_IMAGE) >= 0)
		    return Messages.getText("arcims_image_resource");
		
		if (protocol.toUpperCase().indexOf(Resource.ARCIMS_VECTORIAL) >= 0)
		    return Messages.getText("arcims_vect_resource");
		
		return Messages.getText("unknown");
    } 

/**
 * 
 * 
 */
    private void initialize() {        
	    this.setSize(510, 125);
        this.add(getTablePane(), null);
			
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public JScrollPane getTablePane() {        
		if (tablePane == null) {
		    tablePane = new JScrollPane(getTable());
		    tablePane.setPreferredSize(new java.awt.Dimension(575,100));
		    
		}
		return tablePane;
    } 

/**
 * This method initializes table
 * 
 * 
 * @return javax.swing.JTable
 */
    public JTable getTable() {        
		if (table == null) {
		    table = new JTable(createTableModel());
		    
		}
		return table;
    } 

/**
 * It Return the JButtons array
 * 
 * 
 * @return 
 */
    public JButton[] getButtons() {        
	    JButton[] buttons = new JButton[resources.size()];
	    for (int i=0 ; i<resources.size() ; i++)
	        buttons[i] = (JButton) getTable().getModel().getValueAt(i,2); 
	    
	    return buttons;
    } 
 }
